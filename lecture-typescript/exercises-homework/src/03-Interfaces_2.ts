/*
Features:
- interfaces
- function types
- indexable types

 https://www.typescriptlang.org/docs/handbook/interfaces.html
*/

namespace e03 {

    // Function types
    interface Counter {
        (start: number): string
        interval: number
        reset(): void
    }

    function getCounter(): Counter {
        let counter = <Counter>function (start: number) { }
        counter.interval = 123
        counter.reset = function () { }
        return counter
    }

    let c = getCounter()
    c(10)
    c.reset()
    c.interval = 5.0


    // Indexable types
    interface StringArray {
        [index: number]: string
    }

    let myArray: StringArray
    myArray = ["Bob", "Fred"]

    let myStr: string = myArray[0]


    interface StringDictionary {
        [index: string]: string
    }

    let myDict: StringDictionary = {}
    myDict["xxx"] = "AAA"
    // myDict["yyy"] = 3  - Compile error

}