def o1 = order pizza with tomato and mushrooms to "Malostranske namesti"
def o2 = order cake with plums and apples and cream to "Malostranske namesti"
def o3 = order pizza menuDuJour() to "Malostranske namesti"

//TASK Add support for house numbers in address (aka to "Malostranske namesti", 25)

[o1, o2, o3].each {println it}

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
        "${description()} with ${ingredients.join(' and ')}"
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