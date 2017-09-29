class BusinessPerson {}
class Turist {}
class Homeless {}
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

println receptionist.welcome(new BusinessPerson())
println receptionist.welcome(new Turist())
println receptionist.welcome(new Burglar())
println receptionist.welcome(new Homeless())

//TASK Make the following code pass
//println receptionist(new BusinessPerson())
//println receptionist(new Turist())
//println receptionist(new Burglar())
//println receptionist(new Homeless())