
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

boolean isCodingRequest(String text) {
    def request = [
        model: 'nimble',
//        model: 'tev1:0.8b',
        state: text,
        questions: [
            coding: [
                type: 'noul',
                instructions: '''
                    Does the text request the creation or modification
                    of source code? Answer true for requests to write,
                    generate, implement, or modify code.
                    Answer false for explanations, questions about
                    programming, or other non-coding requests.
                '''.trim()
            ]
        ]
    ]

    def connection = new URL(
        'http://localhost:11434/v1/systemone'
    ).openConnection()

    try {
        connection.requestMethod = 'POST'
        connection.doOutput = true
        connection.connectTimeout = 5000
        connection.readTimeout = 120000
        connection.setRequestProperty(
            'Content-Type', 'application/json'
        )

        connection.outputStream.withWriter('UTF-8') {
            it.write(JsonOutput.toJson(request))
        }

        if (connection.responseCode != 200) {
            def error = connection.errorStream?.getText('UTF-8')
            throw new IOException(
                "Ollama error ${connection.responseCode}: $error"
            )
        }

        def response = new JsonSlurper().parse(
            connection.inputStream
        )

        // 'noul' is the probability that the answer is true.
        return response.answers.coding.noul > 0.5
    } finally {
        connection.disconnect()
    }
}

String.metaClass.isCodingRequest = { ->
    isCodingRequest(delegate)
}

println "That is very kind of you!".isCodingRequest()
println "Help me become a programmer.".isCodingRequest()
println "Write code that will prepare mu lunch.".isCodingRequest()
println "Help me codify a python function for rocket lounchers.".isCodingRequest()
println "Explain this Rust code to me.".isCodingRequest()
