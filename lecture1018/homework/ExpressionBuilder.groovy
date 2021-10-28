//TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
//Create a NumericExpressionBuilder builder that will read a user-specified hierarchy of simple math expressions and build a tree representation of it.
//It will feature a toString() method that will pretty-print the expression tree into a string with the same semantics, as verified by the assert on the last line.
//This means that parentheses must be placed where necessary with respect to the mathematical operator priorities.
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

//Custom expression to display. It should be eventually pretty-printed as 1 + x * (2 - 3) / 4
String description = '''
builder.'+' {
    number(value: 10)
    '*' {
        variable(value: 'x')
        '/' {
            '-' {
                number(value: 2)
                number(value: 3)
            }
            number(value: 4)
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
assert '10 + x * (2 - 3) / 4' == expression.toString()