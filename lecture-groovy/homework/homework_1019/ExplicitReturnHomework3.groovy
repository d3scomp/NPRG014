/**
 * @author http://joesgroovyblog.blogspot.com/2011/09/ast-transformations-prerequisites-and.html
 */

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("ExplicitReturnTransformation")
public @interface ExplicitReturn {
    String value() default "true";
}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class ExplicitReturnTransformation implements ASTTransformation {

    def annotationType = ExplicitReturn.class.name

    private boolean checkNodes(ASTNode[] astNodes, String annotationType) {
        if (!astNodes) return false
        if (!astNodes[0]) return false
        if (!astNodes[1]) return false
        if (!(astNodes[0] instanceof AnnotationNode)) return false
        if (!astNodes[0].classNode?.name == annotationType) return false
        if (!(astNodes[1] instanceof MethodNode)) return false
        true
    }
    
    public void addError(String msg, ASTNode expr, SourceUnit source) {
        int line = expr.lineNumber
        int col = expr.columnNumber
        SyntaxException se = new SyntaxException(msg + '\n', line, col)
        SyntaxErrorMessage sem = new SyntaxErrorMessage(se, source)
        source.errorCollector.addErrorAndContinue(sem)
    }    

    /**
     * AST Transformation: Enforces explicit return statements in annotated methods.
     *
     * <p>Purpose: This {@link ASTTransformation} visits every method annotated with {@code @ExplicitReturn}
     * and verifies that the method body contains at least one statement and that the last statement
     * is an explicit {@code return}.</p>
     *
     * <p>Expected compilation errors when running the test code at the bottom of this file:</p>
     * <pre>
     * The method incorrectAdd does not have an explicit return
     *  at line: 8, column: 5
     *
     * Empty method cannot have an explicit return
     *  at line: 13, column: 5
     * </pre>
     *
     * <p>Desired state: A method passes validation only if its body is non-empty and ends with
     * a {@link ReturnStatement}. Methods using implicit returns (last expression only) or empty
     * method bodies will trigger a compilation error.</p>
     *
     * <p>Hints:</p>
     * <ul>
     *   <li>{@code astNodes[0]} is the annotation node; {@code astNodes[1]} is the annotated {@link MethodNode}.</li>
     *   <li>Use {@code annotatedMethod.code.statements} to get the list of statements in the method body.</li>
     *   <li>Check {@code instanceof ReturnStatement} to verify the last statement is an explicit return.</li>
     *   <li>Use the existing {@link #addError(String, ASTNode, SourceUnit)} helper to report errors.</li>
     * </ul>
     */
    public void visit(ASTNode[] astNodes, SourceUnit source) {
        if (!checkNodes(astNodes, annotationType)) {
            addError("Internal error on annotation", astNodes[0], source);
            return
        }
        // TODO implement so that the compiler reports the two compilation errors
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
class Calculator {
    @ExplicitReturn
    public int correctAdd(a, b) {
        return a + b
    }
    
    @ExplicitReturn
    public int incorrectAdd(a, b) {
        a + b
    }    
    
    @ExplicitReturn
    public void incorrectSinceEmpty(a, b) {
    }        
}

new Calculator()
''')

println calculator.correctAdd(2, 5)
println "Error: the compilation should have failed!"