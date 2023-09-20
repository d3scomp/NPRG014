//2022/2023
//Four channels deliver daily prices of a particular stock traded at four locations - Paris, Vienna, Frankfurt and Chicago.
//The prices are in EUR for Paris, Vienna and Frankfurt, and in USD for Chicago.
//The prices in Paris may contain a value '0' indicating that no price is available for that day. The latest recorded non-zero price in Paris
//should be used instead of value '0'. The latest recorded value for Paris when the computation starts is '0'.
//A fifth channel delivers daily exchange rates for EUR to USD conversion (price of one USD in EUR).

//The operator-based calculation uses several intermediate channels and four operators to:
//  1. convert USD prices to EUR
//  2. calculate daily average price in EUR of the stock across all four markets
//  3. propagate the latest non-zero Paris value back as an alternative input value for the daily average calculation in case the regular value is '0'
//  4. calculate a five-day moving average of the prices in EUR calculated in 2.
//The output of 2. and 4. each comes from a separate channel (avgPrices and fiveDayAverages, respectively).
//The two channels are consumed by provided tasks that print the values on the screen.

//TASK Implement the missing four operators, possibly connected with additional channels so that the calculation passes
//The first operator will convert Chicago prices from USD to EUR.
//The second operator will calculate a daily average price in EUR using the four daily prices from different locations as its input. It must be able
//to replace a value '0' for Paris with the latest non-zero Paris value.
//The third operator will take the daily average prices and output a five-day moving average (an average of the last five days, including today).
//If the history is shorter than five days, use only the days that are available.
//
//Hint for possible state management in operators:
/*
    operator(inputs: [...], outputs: [...], stateObject: [key: value]) { a, b, c, d ->
        assert stateObject.key == value //access the state object through the key
    
        bindOutput("something")  //output to the only output channel, if there's only one
        bindOutput(0, "something")  //output to the first output channel
        bindAllOutputs("something")  //output to all output channels
    }

*/

import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.group.DefaultPGroup

//Dummy price and exchange rate data
final prices1 = [10, 11, 12, 10, 9, 8, 7, 9, 6, 7,
                 10, 9, 12, 0, 14, 0, 12, 13, 12, 14,
                 10, 11, 10, 12, 14, 15, 12, 13, 14, 12]
final prices2 = [10, 11, 12, 10, 9, 8, 7, 9, 6, 7,
                 10, 9, 12, 12, 14, 14, 12, 13, 12, 14,
                 10, 11, 10, 12, 14, 15, 12, 13, 14, 12]
final prices3 = [9, 8, 4, 10, 9, 8, 7, 9, 6, 7,
                 10, 9, 10, 12, 13, 15, 12, 11, 12, 15,
                 10, 11, 10, 12, 14, 15, 12, 13, 14, 12]
final prices4 = [12, 12, 12, 12, 12, 9, 8, 9, 7, 9,
                 11, 11, 13, 11, 14, 15, 13, 14, 12, 14,
                 11, 12, 11, 10, 13, 15, 13, 14, 15, 13]
final rates = [0.81, 0.82, 0.82, 0.81, 0.81, 0.81, 0.82, 0.81, 0.81, 0.82,
               0.79, 0.81, 0.85, 0.88, 0.81, 0.81, 0.83, 0.83, 0.84, 0.81,
               0.81, 0.80, 0.83, 0.81, 0.85, 0.89, 0.93, 0.87, 0.82, 0.81]

//a thread pool to use
final group = new DefaultPGroup()

group.with {
    //input channels
    final parisEURPrices = new DataflowQueue()
    final viennaEURPrices = new DataflowQueue()
    final frankfurtEURPrices = new DataflowQueue()    
    final chicagoUSDPrices = new DataflowQueue()
    final usd2eurRates = new DataflowQueue()

    //output channels
    final avgPrices = new DataflowQueue()
    final fiveDayAverages = new DataflowQueue()
    
    //================================= do not modify above this point
    
    //implement the three operators and utility intermediate channels here
















    //================================= do not modify beyond this point    


    //Generate the input data streams from the dummy data
    task {
        prices1.each { parisEURPrices << it }
    }
    task {
        prices2.each { viennaEURPrices << it }
    }
    task {
        prices3.each { frankfurtEURPrices << it }
    }
    task {
        prices4.each { chicagoUSDPrices << it }
    }
    task {
        rates.each { usd2eurRates << it }
    }

    //Retrieve the results
    def results = task {
        def result = []
        30.times {
            result << (int) avgPrices.val
        }
        [dailyAveragesKey: result]
    }.then { results ->
        def result = []
        30.times {
            result << (int) fiveDayAverages.val
        }
        results["fiveDayAveragesKey"] = result
        results
    }.get()

    //Print the results
    println "Daily averages:    \t${results['dailyAveragesKey'].join(',\t')}"
    println "Five day averages: \t${results['fiveDayAveragesKey'].join(',\t')}"

    assert results['dailyAveragesKey'] == [9, 9, 9, 9, 9, 7, 6, 8, 5, 7, 9, 8, 11, 11, 13, 13, 11, 12, 11, 13, 9, 10, 9, 11, 13, 14, 12, 12, 13, 11]
    assert results['fiveDayAveragesKey'] == [9, 9, 9, 9, 9, 9, 8, 8, 7, 7, 7, 8, 8, 9, 10, 11, 12, 12, 12, 12, 11, 11, 11, 10, 10, 11, 12, 12, 13, 12]
}

group.shutdown()
println 'done'