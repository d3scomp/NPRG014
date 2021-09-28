def o1 = order pizza to "Malostranske namesti"


println o1

println 'done'


def order(requestedMeal) {
    new Order(meal: requestedMeal)
}

class Order {
    String meal
    String address = ''

    def to(place) {
        address = place
        this
    }

    String toString() {
        "*An order of $meal to $address*"
    }
}

def propertyMissing(String name) {name}