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
import groovyjarjarasm.asm.Opcodes
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("NumberConversionTransformation1")
public @interface NumberConversion1 {}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class NumberConversionTransformation1 implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
        ClassNode annotatedClass = astNodes[1]

        AstBuilder ab = new AstBuilder()
        List<ASTNode> res = ab.buildFromString('''
                Integer.parseInt("$valueToConvert")
            ''')

        def param = new Parameter(ClassHelper.STRING_TYPE, "valueToConvert")
        annotatedClass.addMethod("convertToNumber", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [param] as Parameter[], [] as ClassNode[], res[0])
        

        /* the add(a, b) method */
        List<ASTNode> exprstmt = ab.buildFromString('''
              //empty
            ''')
        annotatedClass.addMethod("add", Opcodes.ACC_PUBLIC, ClassHelper.Integer_TYPE, [] as Parameter[], [] as ClassNode[], exprstmt[0])
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@NumberConversion1
class Calculator {}

new Calculator()
''')

println calculator.convertToNumber("20")
//TASK: Enable the add(a, b) method that sums a and b
//println calculator.add(3, 5)