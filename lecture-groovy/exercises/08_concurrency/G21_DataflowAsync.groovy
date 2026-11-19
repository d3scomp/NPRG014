import java.util.concurrent.CompletableFuture

def fetchTemp = {
    sleep(300)
    return 18
}

def fetchCondition = {
    sleep(300)
    if (Math.random() < 0.3) throw new RuntimeException("Sensor offline")
    return "rainy"
}

// 1. Run fetchTemp asynchronously
def tempFuture = CompletableFuture.supplyAsync(fetchTemp)

// 2. Run fetchCondition asynchronously with fallback
def conditionFuture = CompletableFuture.supplyAsync(fetchCondition)
    .exceptionally { ex -> "unknown" }

// 3. Combine both results
def summaryFuture = tempFuture.thenCombine(conditionFuture) { temp, cond ->
    "${temp}°C and ${cond}"
}

println "Result: ${summaryFuture.get()}"