class Money {
    Integer amount
    String currency

    Money plus(Money other) {
        if (this.currency != other.currency) throw new IllegalArgumentException('Cannot add different currencies');
        new Money(amount: this.amount + other.amount, currency: this.currency)
    }

    public String toString() {
        "${amount} ${currency}"
    }
}

class MoneyCategory {
//TASK Define methods of the MoneyCategory class so that the code below passes, x.eur defined as a property getter - x.getEur()
}

use(MoneyCategory) {
    println 10.eur
    println 10.eur + 20.eur
    println 10.usd + 20.usd
}