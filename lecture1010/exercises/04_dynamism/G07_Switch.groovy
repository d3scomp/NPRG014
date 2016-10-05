enum DAYS {
    Sun, Mon, Tue, Wed, Thu, Fri, Sat
}

import static DAYS.*

final currentDay = Tue

assert Wed in [Mon, Wed, Fri]
assert Wed in Mon..Fri
assert ++currentDay == Wed
assert currentDay.previous() == Tue
assert currentDay.next() == Thu

switch (currentDay) {
    case [Sun, Sat]: println 'Vivat weekend'
        break
    case Mon..Thu: println 'Hard work today'
        break
    case Fri: println 'It is coming...'
        break
    case null: println 'What?'
        break
    default: println 'Sorry, no help here'
}

class TrainingDays {
    final days = [Mon, Tue, Fri]
}

//TASK Make the following pass by implementing the isCase() method on the TrainingDays class
//assert Fri in new TrainingDays()

println 'ok'