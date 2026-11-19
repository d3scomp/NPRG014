import groovy.concurrent.Agent

interface Cart {
    void addIfNotPresent(String product)

    List getContents()
}

//TODO Correct the implementation of the CartImpl class using Agents so that it is thread-safe and correct in concurrent execution

class CartImpl implements Cart {
    private contents = Agent.create([])

    void addIfNotPresent(String product) {
        contents.send {cartContent ->
            if (!cartContent.contains(product)) {
                sleep 10 //simulate calculation here
                cartContent << product
            }
            return cartContent
        }
    }

    List getContents() {
        await(contents.getAsync())
    }
}


//User code, do not modify below this line
Cart cart = new CartImpl()
50.times {
    Thread.start {
        cart.addIfNotPresent("Groovy in Action, 2nd Edition")
    }
}
sleep 2000
assert cart.contents.size() == 1

println "Done"