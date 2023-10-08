enum DAYS {
    Sun, Mon, Tue, Wed, Thu, Fri, Sat
}

import static DAYS.*

// ================ Magic with enums
def currentDay = Tue

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

assert Fri in (Wed..Fri)

// ================= Switch customization

class Month {
    String name
    
    public boolean isCase(final o) {
        this.name.startsWith(o.toString())
    }
}

switch("Feb") {
    case new Month(name: "January"): println "January detected";break;
    case new Month(name: "February"): println "February detected";break;
    case new Month(name: "March"): println "March detected";break;        
    default: println "Failure to match"
}