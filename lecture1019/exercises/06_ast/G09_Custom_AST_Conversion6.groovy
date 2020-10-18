import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import groovyjarjarasm.asm.Opcodes
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("NumberConversionTransformation3")
public @interface NumberConversion3 {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class NumberConversionTransformation3 implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        ClassNode annotatedClass = astNodes[1]

        def value = new VariableExpression("valueToConvert")
        ASTNode body = macro(CompilePhase.SEMANTIC_ANALYSIS, true) {
                        return java.lang.Integer.parseInt($v{value})
                    }


        def param = new Parameter(ClassHelper.STRING_TYPE, "valueToConvert")
        annotatedClass.addMethod("convertToNumber", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [param] as Parameter[], [] as ClassNode[], body)
        

        /* the add(a, b) method */        
        ASTNode exprstmt = macro(CompilePhase.SEMANTIC_ANALYSIS, true) {
            a + b
        }
        def param1 = new Parameter(ClassHelper.int_TYPE, "a")
        def param2 = new Parameter(ClassHelper.int_TYPE, "b")        
        annotatedClass.addMethod("add", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [param1, param2] as Parameter[], [] as ClassNode[], exprstmt)


        /* the subtract(a, b) method with param names not hard-coded */  
        String p1Name = "a"+System.currentTimeMillis()
        String p2Name = "b"+System.currentTimeMillis()        
        def varA = new VariableExpression(p1Name)      
        def varB = new VariableExpression(p2Name)              
        ASTNode exprstmtSubtract = macro(CompilePhase.SEMANTIC_ANALYSIS, true) {
            $v{varA} - $v{varB}
        }
        def p1 = new Parameter(ClassHelper.int_TYPE, p1Name)
        def p2 = new Parameter(ClassHelper.int_TYPE, p2Name)        
        annotatedClass.addMethod("subtract", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [p1, p2] as Parameter[], [] as ClassNode[], exprstmtSubtract)
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@NumberConversion3
class Calculator {}

new Calculator()
''')

println calculator.convertToNumber("20")
println calculator.add(3, 5)
println calculator.subtract(30, 5)