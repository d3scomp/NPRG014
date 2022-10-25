import static groovyx.gpars.GParsPool.withPool

final numbers = [1G, 2G, 3G, 4G, 5G, 6G, 7G, 8G, 9G, 9G, 9G, 5G, 6G, 7G, 8G, 4G, 5G, 6G, 3G, 4G, 5G, 7G, 2g, 23g, 43g, 235G, 64G]

def product1 = 1
numbers.each {
    product1 = product1 * it
}
println product1

//TODO Calculate the product of all the numbers in parallel so that the script passes
def product2 = 1
withPool {
    product2 = numbers.parallel.reduce(1) {a, b -> a * b}
}

println product2

assert product1 == product2