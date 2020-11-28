/**
 * @author http://joesgroovyblog.blogspot.com/2011/09/ast-transformations-prerequisites-and.html
 */

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("RequiresTransformation")
public @interface Requires {
    String value() default "true"
}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class RequiresTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        MethodNode annotatedMethod = astNodes[1]
        def annotationExpression = astNodes[0].members.value

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
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''


class Calculator {
    @Requires("divisor != 0")
    public int divide10By(divisor) {
        10/divisor
    }

    @Requires("a > b")
    public int subtract(a, b) {
        a - b
    }
}

new Calculator()
''')

println calculator.divide10By(5)
println calculator.subtract(10, 5)

//TASK uncomment the following lines and see the reported problems, then compare with the error you'd get without the applying the transformation to the divide10By() method
//println calculator.divide10By(0)
//println calculator.subtract(5, 10)