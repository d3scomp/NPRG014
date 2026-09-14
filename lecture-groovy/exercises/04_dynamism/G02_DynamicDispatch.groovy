class BusinessPerson {}
class Turist {}
class Visitor {}
class Burglar {}

class Receptionist {
    String welcome(BusinessPerson visitor) {
        'Welcome sir!'
    }

    String welcome(Turist visitor) {
        'Welcome, what can I do for you?'
    }

    String welcome(Burglar visitor) {
        'Stop, or I call the police!'
    }

    String welcome(final visitor) {
        'We\'re full, please go away!'
    }
}

final receptionist = new Receptionist()

final visitors = [new BusinessPerson(), new Turist(), new Burglar(), new Visitor()]
final greetings = visitors.collect {
                    receptionist.welcome(it)
                  }
greetings.each {println it}

//TASK Make the following code pass
//println receptionist(new BusinessPerson())
//println receptionist(new Turist())
//println receptionist(new Burglar())
//println receptionist(new Homeless())