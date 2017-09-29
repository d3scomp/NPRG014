//Monad - (Tx, unit, bind)

//The Tx class is supposed to offer transactional (all or none) behavior for imaginary rocket strat-up procedures.
//A transaction starts with a budget, which when exhausted forces the transaction to abort.
//Submitted functions have their cost, which gets subtracted from the transaction budget, as soon as the function is applied.
//These functions also log necessary commands to execute, when the transaction successfully commits.
@groovy.transform.Immutable
class Command {
    String action
    
    public void run() {
        println action
    }
}

@groovy.transform.ToString
abstract class Tx {
    Integer budget
    List<Command> commands
    
    public abstract void commit();
    
    //TASK Implement the necessary monad functionality for the Tx class. The order of commands must be preserved.    
}

@groovy.transform.ToString(includeSuper=true)
class ActiveTx extends Tx {
    @Override
    public void commit() {
        commands.each {it.run()}
    }

}
@groovy.transform.ToString(includeSuper=false)
class AbortedTx extends Tx {
    @Override
    public void commit() {}
}

import static Tx.bind
import static Tx.unit
import static Tx.map
import static Tx.lift

Closure<Tx> refuel = {Integer budget -> 
    return new ActiveTx(budget: budget - 1, commands: [new Command("Open lid"), new Command("Add fuel"), new Command("Close lid")])
}

Closure<Tx> preStartCheck = {Integer budget -> 
    return new ActiveTx(budget: budget - 5, commands: [new Command("Check engine"), new Command("Check cabin")])
}

Closure<Tx> ignite = {Integer budget -> 
    return new ActiveTx(budget: budget - 3, commands: [new Command("Turn on electricity"), new Command("Open fuel intake"), new Command("Start engines")])
}

Closure<Tx> takeOff = {Integer budget -> 
    return new ActiveTx(budget: budget - 4, commands: [new Command("Full thrust"), new Command("Release locks")])
}

Tx transaction1 = (unit(9) >> refuel >> preStartCheck >> ignite >> takeOff)
println ("Transaction 1: " + transaction1)

Tx transaction2 = (unit(20) >> refuel >> preStartCheck >> ignite >> takeOff)
println ("Transaction 2: " + transaction2)
println "\nCommiting\n--------------------------------"
transaction2.commit()