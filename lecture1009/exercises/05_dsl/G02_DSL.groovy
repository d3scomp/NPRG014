def o1 = order pizza with tomato and mushrooms to "Malostranske namesti"
def o2 = order cake with plums and apples and cream to "Malostranske namesti"
def o3 = order pizza menuDuJour() to "Malostranske namesti"

[o1, o2, o3].each {println it}

println 'done'


def order(requestedMeal) {
    final newOrder = new Order2(meal: requestedMeal)
    newOrder.meals[requestedMeal]
}

class Order2 {
    String meal
    final ingredients = []
    String address = ''
    final meals = [
            pizza: [with: {ingredient -> ingredients << ingredient; additions},
                    menuDuJour: {-> ingredients << 'surprise'; additions}
            ],
            cake: [with: {ingredient -> ingredients << ingredient; additions}],
    ]
    final additions = [
            and: {ingredient -> ingredients << ingredient; additions},
            to: {place -> address = place; delegate}
    ]

    String toString() {
        "*An order of $meal with ${ingredients.join(' and ')} to $address*"
    }
}

def propertyMissing(String name) {name}