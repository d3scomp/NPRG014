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


@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("UnsupportedTransformation")
public @interface Unsupported {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class UnsupportedTransformation implements ASTTransformation {

    /**
     * AST Transformation: Makes annotated methods throw UnsupportedOperationException and validates empty body.
     *
     * <p>Purpose: This {@link ASTTransformation} visits every method annotated with {@code @Unsupported}
     * and replaces its body with a throw statement that creates an {@link UnsupportedOperationException}
     * with a message like "The {methodName} operation is not supported". Additionally, it reports a
     * compilation error if the annotated method has a non-empty body.</p>
     *
     * <p>Expected compilation error when running the test code at the bottom of this file:</p>
     * <pre>
     * Unsupported operations must be empty
     *  at line: 13, column: 5
     * </pre>
     * <p>(The {@code multiply} method has a non-empty body {@code a * b}, which should trigger this error.)</p>
     *
     * <p>Expected runtime behavior: Calling {@code minus} or {@code divide} (which have empty bodies)
     * should throw {@link UnsupportedOperationException} with the message "The minus operation is not supported"
     * or "The divide operation is not supported" respectively.</p>
     *
     * <p>Desired state: After transformation, methods with empty bodies throw the exception at runtime,
     * and methods with non-empty bodies trigger a compilation error.</p>
     *
     * <p>Hints:</p>
     * <ul>
     *   <li>{@code astNodes[1]} is the annotated {@link MethodNode}.</li>
     *   <li>Check if the method body is empty with {@code annotatedMethod.code.statements.empty}.</li>
     *   <li>If not empty, use the existing {@link #addError(String, ASTNode, SourceUnit)} helper to report an error.</li>
     *   <li>Use {@code new AstBuilder().buildFromString("throw new UnsupportedOperationException('The ${name} operation is not supported')")}</li>
     *   <li>to create the throw statement, where {@code name} is the method name from {@code annotatedMethod.name}.</li>
     *   <li>Insert the throw statement at the beginning of the method body using {@code methodStatements.add(0, block)}.</li>
     *   <li>The AstBuilder returns a list of {@link BlockStatement}s; extract the first one.</li>
     * </ul>
     */
    public void visit(ASTNode[] astNodes, SourceUnit source) {
        // TASK The "Unsupported" transformation should make the annotated methods throw the UnsupportedOperationException
        // Fill in the missing AST generation code to make the script pass
        // You can take inspiration from exercises
        // TASK Report an error when the method declared as @Unsupported has nonnempty body - e.g. multiply()
        // Documentation and hints:
        // http://docs.groovy-lang.org/docs/next/html/documentation/
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/expr/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/stmt/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/tools/package-summary.html        
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/tools/GeneralUtils.html        
        
        MethodNode annotatedMethod = astNodes[1]
        
    }
    
    public void addError(String msg, ASTNode expr, SourceUnit source) {
        int line = expr.lineNumber
        int col = expr.columnNumber
        SyntaxException se = new SyntaxException(msg + '\n', line, col)
        SyntaxErrorMessage sem = new SyntaxErrorMessage(se, source)
        source.errorCollector.addErrorAndContinue(sem)
    }    
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
class Calculator {
    def plus(a, b) {
        a + b
    }

    @Unsupported
    def minus(a, b) {}

    @Unsupported
    def divide(a, b) {}

    @Unsupported
    def multiply(a, b) {
        a * b
    }
}

new Calculator()
''')

assert 5 == calculator.plus(2, 3)
try {
    calculator.minus(10, 5)
    assert false, "Exception should have been thrown since the minus method is not supported"
} catch (RuntimeException e) {
    assert e instanceof UnsupportedOperationException
    assert e.message == 'The minus operation is not supported'
}
try {
    calculator.divide(10, 5)
    assert false, "Exception should have been thrown since the divide method is not supported"
} catch (RuntimeException e) {
    assert e instanceof UnsupportedOperationException
    assert e.message == 'The divide operation is not supported'
}

println 'well done'