import groovy.concurrent.Awaitable

String fetchUser() {
    // Simulate an HTTP request or database query
    Thread.sleep(500)
    return 'Alice'
}

List fetchOrders() {
    Thread.sleep(700)
    return ['Order-101', 'Order-102']
}

Map fetchSettings() {
    Thread.sleep(300)
    return [theme: 'dark', language: 'en']
}

def userTask = async { fetchUser() }
def ordersTask = async { fetchOrders() }
def settingsTask = async { fetchSettings() }

def (user, orders, settings) = await Awaitable.all(userTask, ordersTask, settingsTask)
def first = await Awaitable.any(userTask, ordersTask, settingsTask)
def firstSuccessful = await Awaitable.first(userTask, ordersTask, settingsTask)

assert user == 'Alice'
assert orders.size() == 2
assert settings.theme == 'dark'
println 'done'