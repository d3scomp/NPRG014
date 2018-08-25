import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
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

        AstBuilder ab = new AstBuilder()
        List<ASTNode> res = ab.buildFromSpec {
            method('convertToNumber', Opcodes.ACC_PUBLIC, Integer) {
                parameters {
                    parameter 'valueToConvert': String.class
                }
                exceptions {
                    classNode NumberFormatException
                }
                block {
                    returnStatement {
                        staticMethodCall(Integer, "parseInt") {
                            argumentList {
                                variable "valueToConvert"
                            }
                        }
                    }
                }
                annotations {}
            }
        }
        annotatedClass.addMethod(res[0])
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@NumberConversion3
class Calculator {}

new Calculator()
''')

println calculator.convertToNumber("20")