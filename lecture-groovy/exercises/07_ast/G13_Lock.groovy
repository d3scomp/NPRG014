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

    /**
     * AST Transformation: Adds a ReentrantLock field and wraps all methods with lock/unlock.
     *
     * <p>Purpose: This {@link ASTTransformation} visits the class annotated with {@code @Locking}
     * and adds a private {@code ReentrantLock} field. It then wraps every method body with
     * lock/unlock logic to ensure thread-safe execution. The transformation uses the pattern:
     * {@code lock.lock(); try { originalBody } finally { lock.unlock() }}.</p>
     *
     * <p>Expected behavior: When the test code at the bottom of this file runs with 500 concurrent
     * threads calling {@code add} and {@code subtract}, the final sum should be 0 because the lock
     * ensures atomic operations.</p>
     *
     * <p>Desired state: After transformation, the annotated class has a private {@code myLock} field
     * of type {@code ReentrantLock}, and every method is wrapped in lock/unlock statements.</p>
     *
     * <p>Hints:</p>
     * <ul>
     *   <li>{@code astNodes[1]} is the annotated {@link ClassNode}.</li>
     *   <li>Use {@code ClassHelper.make(java.util.concurrent.locks.ReentrantLock)} to get the ClassNode for ReentrantLock.</li>
     *   <li>Use {@code new AstBuilder().buildFromString('new java.util.concurrent.locks.ReentrantLock()')}</li>
     *   <li>to create the field initializer expression.</li>
     *   <li>Use {@code clazz.addField('myLock', Opcodes.ACC_PRIVATE, classNode, initializerExpression)}</li>
     *   <li>to add the private lock field to the class.</li>
     *   <li>Use {@code new AstBuilder().buildFromString('myLock.lock(); try {} finally {myLock.unlock()}')}</li>
     *   <li>to create the lock/try/finally structure.</li>
     *   <li>The result is a list of statements: the lock() call and the TryCatchStatement.</li>
     *   <li>Set the try block's body with {@code tryBlock.tryStatement = oldBody}.</li>
     *   <li>Create a new {@link BlockStatement}, add the lock statement and try block, and set it as the method code.</li>
     *   <li>Use {@code method.setCode(newBody)} to replace the method body.</li>
     *   <li>Use full class names in AstBuilder strings (e.g., {@code java.util.concurrent.locks.ReentrantLock}).</li>
     * </ul>
     */
    public void visit(ASTNode[] astNodes, SourceUnit source) {
        // TASK Ensure the annotated class has a private ReentrantLock field and properly locks and unlocks in its all methods
        
        // Use ClassHelper.make(java.util.concurrent.locks.ReentrantLock) to get a ClassNode instance for a given class
        // Use a complete class name, such as java.util.concurrent.locks.ReentrantLock when referring to classes from within AstBuilder code
        // Ast nodes return read-only data structures, use x.setY(new Y(new Z())) instead of x.getY().addZ()
        // buildFromString() returns an array of Statements constructed by the builder.
        // ClassNode.addField() accepts an expression, which can be obtained from a BlockStatement as blockStatement.statements.expression
        // ClassNode.addMethod() accepts a BlockStatement
        
        ClassNode clazz = astNodes[1]
        //...
        final methods = clazz.getMethods()
        methods.each {method ->
            final oldBody = method.getCode()
            final newBody = new BlockStatement()
            //...
        }
                
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

(1..500).collect {index ->
    Thread.start {
        calculator.add(index)
        sleep(3)
        calculator.subtract(index)
    }
}*.join()

assert 0 == calculator.sum

println 'well done'