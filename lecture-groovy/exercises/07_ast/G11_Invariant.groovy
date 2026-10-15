import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.codehaus.groovy.ast.stmt.TryCatchStatement
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.expr.ConstantExpression
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("InvariantTransformation")
public @interface Invariant {
    String value() default ""
}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class InvariantTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        MethodNode annotatedMethod = astNodes[1]
        def annotationExpression = astNodes[0].members.value

        if (!annotationExpression || annotationExpression.class != ConstantExpression) {
            addError("The invariant is not a constant expression", astNodes[0], source);
            return
        }

        String annotationValueString = annotationExpression.value

        final res = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, "xxx")
        Statement assertStmt = (Statement) res[0].statements[0]
        if (assertStmt instanceof BlockStatement) {
            assertStmt = assertStmt.statements[0]
        }
        assertStmt.setLineNumber(-1)

        BlockStatement body = annotatedMethod.getCode()

        //...
        
        // TASK The "Invariant" transformation should insert the specified invariant assert statement right behind each existing statement in each annotated method
        // Fill in the missing AST generation code to make the script pass
        // You can take inspiration from exercises
        // Documentation and hints:
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/expr/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/stmt/package-summary.html
        // buildFromString() returns an array of Statements constructed by the builder.
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
    int sum = 0
    
    @Invariant('sum == 0')
    def change(int value) {
        println 'Changing...'
        sum = value - value
    }

    int a = 10
    int b = 0
    
    @Invariant('a+b == 10')
    def adjust(int value) {
        (a, b) = [a+value, b-value]
        println 'Adjusting...'        
    }
}

new Calculator()
''')

calculator.change(10)
calculator.adjust(5)

assert 0 == calculator.sum

println 'well done'