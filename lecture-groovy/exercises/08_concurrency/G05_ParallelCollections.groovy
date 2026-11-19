final numbers = [
    1G, 2G, 3G, 4G, 5G, 6G, 7G, 8G, 9G, 9G, 9G, 5G, 6G, 7G,
    8G, 4G, 5G, 6G, 3G, 4G, 5G, 7G, 2g, 23g, 43g, 235G, 64G
]

// Idiomatic sequential reduction
final product1 = numbers.inject(1G) { acc, n -> acc * n }
println product1

// Idiomatic parallel reduction using the JDK ForkJoinPool
final product2 = numbers.parallelStream().reduce(1G) { a, b -> a * b }
println product2

assert product1 == product2