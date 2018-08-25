//Person with mappable 'pay'
@groovy.transform.ToString
class Person {
    String name
    Integer pay
    
    //f: Integer -> Integer
    def Person map(Closure<Integer> f) {
        new Person(name: this.name, pay: f(this.pay))
    }
}

//Integer -> Integer, not aware of Person class and its fields
def annualIncrement = {it + 0.05 * it}
def toEurCurrencyAdjustment = {it/25}

def p = new Person(name: 'Joe', pay: 300000)

//Person -> Person
p = p.map(annualIncrement)
println p

println (p.map {it + 0.1 * it})

println (p.map(toEurCurrencyAdjustment))