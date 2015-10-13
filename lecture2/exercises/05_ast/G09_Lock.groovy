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

        //...
        // TASK Ensure the annotated class has a private ReentrantLock field and properly locks and unlocks in its all methods
        // Fill in the missing AST generation code to make the script pass
        // You can take inspiration from exercises
        // Documentation and hints:
        // http://groovy.codehaus.org/api/org/codehaus/groovy/ast/MethodNode.html
        // http://groovy.codehaus.org/api/org/codehaus/groovy/ast/stmt/BlockStatement.html
        // http://groovy.codehaus.org/api/org/codehaus/groovy/ast/ClassNode.html
        // http://groovy.codehaus.org/api/org/codehaus/groovy/ast/ClassHelper.html
        // Use ClassHelper.make(java.util.concurrent.locks.ReentrantLock) to get a ClassNode instance for a given class
        // Use a complete class name, such as java.util.concurrent.locks.ReentrantLock when referring to classes from within AstBuilder code
        // Ast nodes return read-only data structures, use x.setY(new Y(new Z())) instead of x.getY().addZ()
        // buildFromString() returns an array, which holds a BlockStatement for the passed-in code as its first element.
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

(1..500).collect {index ->
    Thread.start {
        calculator.add(index)
        calculator.subtract(index)
    }
}*.join()

assert 0 == calculator.sum

println 'well done'