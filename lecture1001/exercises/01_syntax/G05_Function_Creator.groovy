Closure computeTax = {rate, amount -> amount + amount * rate / 100}

//TASK Implement the 'factory' function leveraging the "computeTax" function above
//so that it creates correct taxation functions

Closure taxatorFactory = {rate ->

}

Closure lowRateVat = taxatorFactory 5
assert 1050 == lowRateVat(1000)
assert 1210 == taxatorFactory(21)(1000)

println 'ok'