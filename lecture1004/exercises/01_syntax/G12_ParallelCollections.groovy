import static groovyx.gpars.GParsPool.withPool

final numbers = [1G, 2G, 3G, 4G, 5G, 6G, 7G, 8G, 9G, 9G, 9G, 5G, 6G, 7G, 8G, 4G, 5G, 6G, 3G, 4G, 5G, 7G, 2G, 23G, 43G, 235G, 64G]

//TASK Modify the calculations so that it instead detects whether the collection of numbers contains any even numbers

final even1 = numbers.findAll {it % 2 == 0}

withPool {
    final even2 = numbers.findAllParallel {it % 2 == 0}
    assert even1 == even2
}

println 'ok'