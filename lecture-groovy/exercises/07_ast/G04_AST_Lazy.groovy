class TaxCalculator {
    @Lazy int taxRate = downloadTaxRate()

    long calculateTotalPrice(long price) {
        price + price * taxRate / 100
    }

    private static int downloadTaxRate() {
        println 'Downloading'
        Math.abs('http://groovy.cz'.toURL().text.hashCode())
    }
}

final calculator = new TaxCalculator()

println 'First calculation'
println calculator.calculateTotalPrice(10)

println 'Second calculation'
println calculator.calculateTotalPrice(20)
