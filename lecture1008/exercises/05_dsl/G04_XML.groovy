final out = new StringWriter()
def builder = new groovy.xml.MarkupBuilder(out)  // Construct a builder

final doc = builder.order() {
    beer(count: 2) {
        price {
            amount 10
            currency 'CZK'
        }
    }
    tea(count: 3) {
        price {
            amount 20
            currency 'CZK'
        }
    }
    coffee(count: 1) {
        price {
            amount 25
            currency 'CZK'
        }
    }
    vodka() {
    }    
}

//TASK enhance the builder code so as the following test passes

assert '''<order>
  <beer count='2'>
    <price>
      <amount>10</amount>
      <currency>CZK</currency>
    </price>
  </beer>
  <tea count='3'>
    <price>
      <amount>20</amount>
      <currency>CZK</currency>
    </price>
  </tea>
  <coffee count='1'>
    <price>
      <amount>25</amount>
      <currency>CZK</currency>
    </price>
    <sugar />
    <cream />
  </coffee>
  <vodka count='10'>
    <price>
      <amount>30</amount>
      <currency>CZK</currency>
    </price>
    <size>big</size>
  </vodka>
</order>''' == out.toString()

println 'done'