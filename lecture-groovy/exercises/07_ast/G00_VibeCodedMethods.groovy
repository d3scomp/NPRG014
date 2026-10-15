#!/usr/bin/env groovy
import helpers.*

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

// This script demonstrates a Groovy AST transformation that automatically generates method bodies
// from natural-language descriptions using an LLM.

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("VibeCodedTransformation")
public @interface VibeCoded {
    String value() default "Print a 'Hello' message to the console."
}

@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class VibeCodedTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        MethodNode annotatedMethod = astNodes[1]
        def annotationExpression = astNodes[0].members.value
        String annotationValueString = annotationExpression.value
        
        BlockStatement block = createStatements(annotationValueString)        
                
        def methodStatements = annotatedMethod.code.statements
        methodStatements.add(0, block)
    }
    
    BlockStatement createStatements(String methodDescription) {
        // Ask LLM for the method body
        String methodBody = generateMethodBody(methodDescription)
        
        // Parse the method body and insert the parsed AST into the method body
        AstBuilder ab = new AstBuilder()
        List<ASTNode> res = ab.buildFromString(SEMANTIC_ANALYSIS, methodBody)
        BlockStatement bs = res[0]
        return bs
    }
        
    String generateMethodBody(String request) {
    
        // Use the LLMChatConnector helper for a conversation
        def connector = new LLMChatConnector(debug: false, systemPrompt: '''
        You are a coding assistant. All your responses must be valid idiomatic Groovy code.
        You receive a description of the desired behavior of a single Groovy method and you return the body of that method.
        Do not include the method signature or the wrapping braces.
        ''')
        
        def currentRequest = request
        def counter = 0
        final MAX_COUNT = 3
        String codeToRun = ''
        do {
            def answer = connector.ask(currentRequest)
        
            // EXTRACT CODE FROM MARKDOWN
            codeToRun = answer
            def matcher = answer =~ /(?s)```(?:groovy)?\s*(.*?)```/
            if (matcher.find()) {
                codeToRun = matcher.group(1).trim()
            }
        
            println 'Code suggestion for: ' + request
            println codeToRun
            println ''
            
            println 'Validating --------------------------------'
            def validationAnswer = validate(codeToRun, request)
            println "Validation: $validationAnswer"
        
            if (validationAnswer.startsWith('INVALID')) {
                currentRequest = "Please correct the code. Validation failed. Result: $validationAnswer"
                counter++
            } else {
                break
            }
        
        } while (counter < MAX_COUNT)
        
        if (counter >= MAX_COUNT) {
            throw new IllegalArgumentException("The supplied code is not valid: $codeToRun")
        }
        
        println 'Validated --------------------------------'
        return codeToRun
    }


    String validate(String code, String originalRequest) {
        def validator = new LLMChatConnector(debug: false, systemPrompt: """
    You validate whether the provided code is valid Groovy script code.
    It must represent a method body and correctly implement the desired functionality.
    It must not include the method signature.
    The original user request was: 
    ---------------
    $originalRequest.
    ---------------
    
    You must respond with text starting with either 'CORRECT' or 'INVALID'.
    A detailed explanation of your decision should follow only if the code is INVALID.
        """)
        String answer = validator.ask(code)
        return answer
    }    
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
class Calculator {
    def plus(a, b) {
        a + b
    }

    @VibeCoded("Subtracts the parameter b from parameter a")
    def minus(a, b) {}

    @VibeCoded("Divides parameters a and b, if b is 0 throw IllegalArgumentException")
    def divide(a, b) {}

    @VibeCoded("Multiplies parameters a and b")
    def multiply(a, b) {}
}

new Calculator()
''')

assert 5 == calculator.plus(2, 3)
assert 5 == calculator.minus(10, 5)
assert 50 == calculator.multiply(10, 5)
assert 2 == calculator.divide(10, 5)
try {
    calculator.divide(10, 0)
    assert false : "Should have thrown an exception"
} catch (e) {}

println 'well done'
