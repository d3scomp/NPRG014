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
// TASK Uncomment and observe the stack trace of the caught exception
//    throw new RuntimeException('test')
    return [theme: 'dark', language: 'en']
}

def userTask = async { fetchUser() }
def ordersTask = async { fetchOrders() }
def settingsTask = async { fetchSettings() }

// The tasks have already started and can run concurrently.
def user = await userTask
def orders = await ordersTask
def settings = await settingsTask

println user
println orders
println settings