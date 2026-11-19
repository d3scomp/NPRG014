import groovy.concurrent.AsyncScope

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

def result = AsyncScope.withScope { scope ->
    def userTask = scope.async { fetchUser() }
    def ordersTask = scope.async { fetchOrders() }
    def settingsTask = scope.async { fetchSettings() }

    def (user, orders, settings) =
        await(userTask, ordersTask, settingsTask)

    [
        user: user,
        orders: orders,
        settings: settings
    ]
}

println result.user
println result.orders
println result.settings