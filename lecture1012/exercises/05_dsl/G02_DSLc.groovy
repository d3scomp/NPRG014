import org.codehaus.groovy.control.*
import org.codehaus.groovy.antlr.*
import org.codehaus.groovy.syntax.*

def conf = new CompilerConfiguration(pluginFactory: new SourcePreProcessor())
def shell = new GroovyShell(new Binding(), conf)

shell.evaluate('''
def o3 = order pizza menuDuJour to "Malostranske namesti"
println o3

println 'done'


def order(requestedMeal) {
    final newOrder = new Order2()
    final meal = newOrder.meals[requestedMeal]
    newOrder.meal = meal
    meal
}

class Order2 {
    Food meal
    String address = ''
    final meals = [
            pizza: new Pizza(order: this),
            cake: new Cake(order: this)
    ]

    String toString() {
        "*An order of $meal to $address*"
    }
}

abstract class Food {
    def order
    final ingredients = []

    def with(ingredient) {
        ingredients << ingredient
        this
    }

    def and(ingredient) {
        ingredients << ingredient
        this
    }

    def to(address) {
        order.address = address
        order
    }

    abstract String description();

    String toString() {
        "pizza with ${ingredients.join(' and ')}"
    }
}

class Pizza extends Food {
    String description() {'pizza'}

    def menuDuJour() {
        ingredients << 'surprise'
        this
    }

}

class Cake extends Food {
    String description() {'cake'}
}

def propertyMissing(String name) {name}
''')


class SourceModifierParserPlugin extends AntlrParserPlugin {
    Reduction parseCST(SourceUnit sourceUnit, Reader reader) throws CompilationFailedException {
        def text = addParens(reader.text)
        StringReader stringReader = new StringReader(text)
        super.parseCST(sourceUnit, stringReader)
    }

    String addParens(text) {
        def pattern = ~/(.*) (menuDuJour) (.*)/
        def replacement = /$1 $2\(\) $3/
        (text =~ pattern).replaceAll(replacement)
    }
}

class SourcePreProcessor extends ParserPluginFactory {
    ParserPlugin createParserPlugin() {
        new SourceModifierParserPlugin()
    }
}