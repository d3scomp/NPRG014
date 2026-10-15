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
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("LockingTransformation")
public @interface Locking {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class LockingTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        ClassNode clazz = astNodes[1]
        final initializer = new AstBuilder().buildFromString('new java.util.concurrent.locks.ReentrantLock()')
        clazz.addField('myLock', Opcodes.ACC_PRIVATE, ClassHelper.make(java.util.concurrent.locks.ReentrantLock), initializer[0].statements[0].expression)
        final methods = clazz.getMethods()
        methods.each {method ->
            final newCode = new AstBuilder().buildFromString('myLock.lock();try {} finally {myLock.unlock()}')[0]
            final lockingStatement = newCode.statements[0]
            final tryBlock = newCode.statements[1]

            final oldBody = method.getCode()
            tryBlock.tryStatement = oldBody
            
            final newBody = new BlockStatement()
            newBody.addStatement(lockingStatement)
            newBody.addStatement(tryBlock)
            method.setCode(newBody)
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
@Locking
class Calculator {
    int sum = 0
    
    def add(int value) {
        int v = sum + value
        sum = v
    }

    def subtract(int value) {
        sum -= value
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