import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.syntax.*
import groovyjarjarasm.asm.Opcodes
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("NumberConversionTransformation")
public @interface NumberConversion {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class NumberConversionTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        ClassNode annotatedClass = astNodes[1]
                
        ClassNode clazz = org.codehaus.groovy.ast.ClassHelper.make(Integer)
        ASTNode call = new StaticMethodCallExpression(clazz, "parseInt", new VariableExpression('valueToConvert'))
        ASTNode stmt = new ExpressionStatement(call)
        
        def param = new Parameter(ClassHelper.STRING_TYPE, "valueToConvert")
        annotatedClass.addMethod("convertToNumber", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [param] as Parameter[], [] as ClassNode[], stmt)


        /* the add(a, b) method */
        ASTNode left = new VariableExpression('a')
        ASTNode right = new VariableExpression('b')        
        Token operation = new Token(Types.PLUS, '+', -1, -1)
        ASTNode plus = new BinaryExpression(left, operation, right)
        ASTNode exprstmt = new ExpressionStatement(plus)        
        def param1 = new Parameter(ClassHelper.int_TYPE, "a")
        def param2 = new Parameter(ClassHelper.int_TYPE, "b")        
        annotatedClass.addMethod("add", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [param1, param2] as Parameter[], [] as ClassNode[], exprstmt)
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@NumberConversion
class Calculator {}

new Calculator()
''')

println calculator.convertToNumber("20")
println calculator.add(3, 5)