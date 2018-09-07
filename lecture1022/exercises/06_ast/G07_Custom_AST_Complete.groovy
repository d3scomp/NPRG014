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
import org.codehaus.groovy.ast.stmt.BlockStatement
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
@GroovyASTTransformationClass("RequiresTransformation")
public @interface Requires {
    String value() default "true";
}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class RequiresTransformation implements ASTTransformation {

    def annotationType = Requires.class.name

    private boolean checkNodes(ASTNode[] astNodes, String annotationType) {
        if (!astNodes) return false
        if (!astNodes[0]) return false
        if (!astNodes[1]) return false
        if (!(astNodes[0] instanceof AnnotationNode)) return false
        if (!astNodes[0].classNode?.name == annotationType) return false
        if (!(astNodes[1] instanceof MethodNode)) return false
        true
    }

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        if (!checkNodes(astNodes, annotationType)) {
            addError("Internal error on annotation", astNodes[0], source);
            return
        }
        MethodNode annotatedMethod = astNodes[1]
        def annotationExpression = astNodes[0].members.value

        if (annotationExpression.class != ConstantExpression) {
            addError("The condition is not a constant expression", astNodes[0], source);
            return
        }

        String annotationValueString = annotationExpression.value
        BlockStatement block = createStatements(annotationValueString)

        def methodStatements = annotatedMethod.code.statements
        methodStatements.add(0, block)
    }

    def createStatements(String clause) {
        def statements = """
            if(!($clause)) {
                throw new Exception('Precondition violated: {$clause}')
            }
        """

        AstBuilder ab = new AstBuilder()
        List<ASTNode> res = ab.buildFromString(CompilePhase.SEMANTIC_ANALYSIS, statements)
        BlockStatement bs = res[0]
        return bs
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
    @Requires("divisor != 0")
    public int divide10By(divisor) {
        10/divisor
    }
}

new Calculator()
''')

println calculator.divide10By(5)
println calculator.divide10By(0)