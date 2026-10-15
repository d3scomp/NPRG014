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
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("InvariantTransformation")
public @interface Invariant {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class InvariantTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        MethodNode annotatedMethod = astNodes[1]
        def annotationExpression = astNodes[0].members.value

        if (annotationExpression.class != ConstantExpression) {
            addError("The invariant is not a constant expression", astNodes[0], source);
            return
        }

        String annotationValueString = annotationExpression.value

        
        final assertStmt = new AstBuilder().buildFromString('assert ' + annotationValueString)

        final methods = clazz.getMethods()
        methods.each {method ->
            final body = method.getCode()
            final allStatements = body.statements
            allStatements.each{stms ->
                body.addStatement(assertStms.clone())
            }
        }

                
        //...
        // TASK Ensure the annotated class has a private ReentrantLock field and properly locks and unlocks in its all methods
        // Fill in the missing AST generation code to make the script pass
        // You can take inspiration from exercises
        // Documentation and hints:
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/expr/package-summary.html
        // http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/ast/stmt/package-summary.html
        // buildFromString() returns an array of Statements constructed by the builder.
        // ClassNode.addField() accepts an expression, which can be obtained from a BlockStatement as blockStatement.statements.expression
        // ClassNode.addMethod() accepts a BlockStatement
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
class Calculator {
    int sum = 0
    
    @Invariant(sum == 0)
    def change(int value) {
        println 'Hello'
        println 'Hi'        
        sum = value - value
    }

}

new Calculator()
''')

(1..500).collect {index -> Thread.start {
    calculator.add(index)
    sleep(3)
    calculator.subtract(index)
}}*.join()

assert 0 == calculator.sum

println 'well done'