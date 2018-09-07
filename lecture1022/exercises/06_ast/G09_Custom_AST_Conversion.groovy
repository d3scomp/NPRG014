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
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@NumberConversion
class Calculator {}

new Calculator()
''')

println calculator.convertToNumber("20")