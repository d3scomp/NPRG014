class Money {
    int amount
    String currency

    String toString() {"$amount $currency"}

    def asType(Class clazz) {
        switch (clazz) {
            case Integer: return amount
            case String: return "Money: ${toString()}".toString()
            case Loan: return new Loan(balance: new Money(amount: this.amount, currency: this.currency), interestRate: 5, startDate: new Date())
        }
    }

    def asBoolean() {
        return amount>0
    }
}

class Loan {
    Money balance
    int interestRate
    Date startDate

    String toString() {"Loan with interest rate $interestRate of $balance created on $startDate"}
}

def m = new Money(amount: 2000, currency: "EUR")

println(m)

println "============= as Integer ============="
println(m as Integer)
println (10 + (m as Integer))

println "============= as String ============="
println(m as String)

println "============= as Boolean ============="
def report(String msg) {println "LOG: $msg"}
if(m) {
    report(m as String)
}
println(m?:"Empty")

println "============= LOAN ============="
Loan yourNewLoan = new Money(amount: 300, currency: "EUR") as Loan
report((yourNewLoan) as String)

println 'ok'