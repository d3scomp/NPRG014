#!/usr/bin/env groovy

import helpers.*
import groovy.swing.SwingBuilder
import groovy.transform.Field

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.swing.JOptionPane

@Field final pathToProjectRepo = '/Users/'

// ── Prompt Templates ─────────────────────────────────────────────────────────

@Field final String PROMPT_MAIN_AGENT = 'You are a helpful personal agent.'

// ── LLM Connectors ───────────────────────────────────────────────────────────
/*
 * MAIN LLM
 *
 * Generates the actual answer to the user's request.
 */
@Field def connector = new LLMChatConnectorWithTools(
    debug: false,
    model: 'gemma4',
    systemPrompt: PROMPT_MAIN_AGENT
)

/*
 * REGISTER MCP TOOLS
 *
 * Register tools on the connector. The LLMChatConnector handles the execution
 * of each registered closure automatically during its internal agent loop.
 */
connector.registerTool(
    "list_directories",
    "Lists the contents of a directory, returning a multi-line string with the file type (DIR/FILE), UTC updated timestamp, and file name.",
    [
        type: "object",
        properties: [
            targetPath: [
                type: "string",
                description: "The absolute or relative path of the directory to list"
            ]
        ],
        required: ["targetPath"]
    ]
) { args ->
    return list_directories(args.targetPath as String)
}
connector.registerTool(
    "read_file",
    "Reads the content of a file and returns it as a multi-line string. Both absolute and relative paths are allowed.",
    [
        type: "object",
        properties: [
            path: [
                type: "string",
                description: "The absolute or relative path to the file to read."
            ]
        ],
        required: ["path"]
    ]
) { args ->
    return read_file(args.path as String)
}
connector.registerTool(
    "write_file",
    "Writes content to a file. Only files within the allowed project directory can be written.",
    [
        type: "object",
        properties: [
            path: [
                type: "string",
                description: "The absolute or relative path to the file to write."
            ],
            content: [
                type: "string",
                description: "The content to write to the file."
            ]
        ],
        required: ["path", "content"]
    ]
) { args ->
    return write_file(args.path as String, args.content as String)
}
connector.registerTool(
    "run_command",
    "Runs a command-line command and returns its output. Only allowed commands can be executed.",
    [
        type: "object",
        properties: [
            command: [
                type: "string",
                description: "The command-line command to execute."
            ]
        ],
        required: ["command"]
    ]
) { args ->
    return run_command(args.command as String)
}

String list_directories(String targetPath) {
    Path path = Paths.get(targetPath).toAbsolutePath().normalize()

    // 1. Verify existence and type
    if (!Files.exists(path)) {
        return "ERROR: Path does not exist - ${path}"
    }
    if (!Files.isDirectory(path)) {
        return "ERROR: Path is not a directory - ${path}"
    }

    StringBuilder output = new StringBuilder()
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC)

    // 2. Provide a schema header for LLM-friendly parsing
    output.append("TYPE\tUPDATED\tNAME\n")

    try {
        // 3. Stream directory contents and close the stream to prevent resource leaks
        Files.list(path).withCloseable { stream ->
            stream.sorted().each { child ->
                BasicFileAttributes attrs = Files.readAttributes(child, BasicFileAttributes.class)
                String type = attrs.isDirectory() ? "DIR" : "FILE"
                String updated = formatter.format(attrs.lastModifiedTime().toInstant())
                
                output.append("${type}\t${updated}\t${child.fileName}\n")
            }
        }
    } catch (Exception e) {
        return "ERROR: Failed to read directory contents - ${e.message}"
    }

    return output.toString().trim()
}

/**
 * Reads the content of a file and returns it as a multi-line string.
 * Designed for MCP tool invocation by LLM agents.
 *
 * @param path The absolute or relative path to the file.
 * @return The contents of the file as a String.
 * @throws IllegalArgumentException if the path is invalid, missing, or a directory.
 */
String read_file(String path) {
    if (path == null || path.trim().isEmpty()) {
        return "System Error: The 'path' argument cannot be null or empty."
    }

    File targetFile = new File(path)

    // Resolve the absolute path for error reporting,
    // so the LLM knows exactly where the system looked.
    String absolutePath = targetFile.absolutePath

    if (!targetFile.exists()) {
        return "System Error: File not found. No file exists at path: ${absolutePath}. Please verify the directory contents or path."
    }

    if (targetFile.isDirectory()) {
        return "System Error: The provided path points to a directory, not a file: ${absolutePath}. Please provide a path to a specific file, or use a directory listing tool."
    }

    // Groovy's .text property handles opening, reading, and closing the stream,
    // returning the full file content as a multi-line string.
    return targetFile.text
}

/**
 * Writes content to a file.
 * Designed for MCP tool invocation by LLM agents.
 *
 * The file can only be written if its canonical absolute path starts with an agreed prefix.
 *
 * @param path The absolute or relative path to the file.
 * @param content The content to write to the file.
 * @return A confirmation message.
 * @throws IllegalArgumentException if the path is invalid, outside the allowed directory, or a directory.
 */
String write_file(String path, String content) {
    if (path == null || path.trim().isEmpty()) {
        return "System Error: The 'path' argument cannot be null or empty."
    }

    File targetFile = new File(path)

    // Resolve the canonical path to prevent directory traversal via symlinks
    String canonicalPath = targetFile.canonicalPath

    // Security check: restrict writes to the allowed project directory only
    // TODO Set the path to your project
    final allowedPath = "${pathToProjectRepo}lecture-groovy/exercises/"
    if (!canonicalPath.startsWith(allowedPath)) {
        return "Write not allowed. Path ${canonicalPath} is outside the allowed directory. Create files in ${allowedPath}."
    }

    // Ensure parent directories exist
    targetFile.parentFile?.mkdirs()

    // Groovy's .text property handles opening, writing, and closing the stream
    targetFile.text = content

    return "File written successfully: ${canonicalPath}"
}

/**
 * Runs a command-line command and returns its output.
 * Designed for MCP tool invocation by LLM agents.
 *
 * Only whitelisted commands are executed directly; all other commands are
 * validated by an LLM gate before they may proceed.
 *
 * @param command The command-line command to execute.
 * @return A confirmation message with the command output, or an error message.
 */
String run_command(String command) {
    if (command == null || command.trim().isEmpty()) {
        return "System Error: The 'command' argument cannot be null or empty."
    }

    // Validate against a whitelist of allowed commands
    def allowedCommands = ['java -version', 'groovy -version', 'ls']
    if (!allowedCommands.any { command.startsWith(it) }) {
        def connector = new LLMChatConnector(systemPrompt: '''
        Your task is to validate whether the provided string is a safe, read-only CLI command.
        Calling `groovy` in order to run a Groovy script must be allowed.
        Reply with ALLOWED or DENIED only. Nothing else ever.
        ''')
        
        // Ask an LLM gate to validate whether the command is safe
        def answer = connector.chat(command)
        println "*** COMMAND VERIFIED: $command : $answer"
        if (!answer.startsWith('ALLOWED')) {
            // Command denied by the LLM verifier
            return "System Error: Command not allowed. Only 'java -version', 'groovy -version', 'ls' and LLM-validated commands are permitted."
        } else {
            // Command allowed by the LLM verifier
            def result = JOptionPane.showConfirmDialog(
                null,              // parent component (null = center on screen)
                "Do you allow to run this command? $command", // your message
                "Confirmation",    // dialog title
                JOptionPane.YES_NO_OPTION, // button set
                JOptionPane.QUESTION_MESSAGE // question mark icon
            )
            
            if (result == JOptionPane.YES_OPTION) {
                println "✅ User approved the command."
            } else {
                return "Running the command was rejected by the user."
            }
            // WARNING: If you remove the mock below, a potentially harmful command could be executed on your machine!
//            return "Command should run, but we just mock it here without actually running it. Invent an answer yourself."
        }
    }

    def process = Runtime.getRuntime().exec(command)
    process.waitFor()

    def stdout = process.in.text.trim()
    def stderr = process.err.text.trim()

    if (process.exitValue() != 0) {
        return "Command failed with exit code ${process.exitValue()}. Error: ${stderr}"
    }

    def output = stdout ?: stderr
    return "Command ran successfully. Output: ${output}"
}

def handleUserMessage(String message) {
        println('Asking main LLM -------------------------------')

        /*
         * The LLMChatConnector internally orchestrates the agent loop:
         * it intercepts tool-calling requests, invokes the registered
         * Groovy closures, and sends the results back to the LLM.
         */
        String answer = connector.ask(message)

        return answer
}

// TASK Look into the run_command implementation and comment on the security measures taken to prevent running harmful commands


println '*** Groovy script creation and run-----------------------------'
def result4 = handleUserMessage("""
   Create and run a Groovy script that prints the current date and time.
""")
println result4
println ''

println 'well done'