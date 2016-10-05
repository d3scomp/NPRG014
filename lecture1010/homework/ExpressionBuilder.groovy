import javax.swing.*
import java.awt.*

//TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
//Create a NumericExpressionBuilder builder that will read a user-specified hierarchy of simple math expressions and build a tree representation of it.
//It will feature a toString() method that will pretty-print the expression tree into a string, as verified by the assert on the last line.
//Change or add to the code in the script. Reuse the infrastructure code at the bottom of the script.
class NumericExpressionBuilder extends BuilderSupport {
    //...
    
    @Override
    String toString() {
        //...
    }
}

class Item {
//...
}

//------------------------- Do not modify beyond this point!

def build(builder, String specification) {
    def binding = new Binding()
    binding['builder'] = builder
    new GroovyShell(binding).evaluate(specification)
    return builder
}

//Custom expression to display
String description = '''
builder.'+' {
    number(value: 1)
    '*' {
        number(value: 7)
        '-' {
            number(value: 2)
            number(value: 3)
        }
    }
}
'''

//XML builder building an XML document
def xml = build(new groovy.xml.MarkupBuilder(), description)
println xml.toString()

//NumericExpressionBuilder displaying the expression
def expression = build(new NumericExpressionBuilder(), description)
println (expression.toString())
assert '(1 + (7 * (2 - 3)))' == expression.toString()

