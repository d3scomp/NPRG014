final out = new StringWriter()
def builder = new groovy.xml.MarkupBuilder(out)  // Construct a builder

final doc = builder.order() {
    beer(count: 2) {
        price {
            amount 10
            current 'CZK'
        }
    }
    tea(count: 3) {
        price {
            amount 20
            current 'CZK'
        }
    }
}

//TASK enhance the builder code so as the following test passes

assert '''<order>
  <beer count='2'>
    <price>
      <amount>10</amount>
      <current>CZK</current>
    </price>
  </beer>
  <tea count='3'>
    <price>
      <amount>20</amount>
      <current>CZK</current>
    </price>
  </tea>
  <coffee count='1'>
    <price>
      <amount>25</amount>
      <current>CZK</current>
    </price>
    <sugar />
    <cream />
  </coffee>
  <vodka count='10'>
    <price>
      <amount>30</amount>
      <current>CZK</current>
    </price>
    <size>big</size>
  </vodka>
</order>''' == out.toString()

println 'done'