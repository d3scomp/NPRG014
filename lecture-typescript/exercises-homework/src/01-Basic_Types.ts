/*
Features:
- namespaces
- variable declarations with types
- type parameterization
- deconstructing
- type assertions
- spread

https://www.typescriptlang.org/docs/handbook/basic-types.html
https://www.typescriptlang.org/docs/handbook/variable-declarations.html
*/

namespace e01 {

    // Basic types
    let isDone: boolean = false

    let fullName: string
    fullName = "John"

    let age = 20  // age is of type number


    // String interpolation
    let sentence = `Hello, my name is ${ fullName }, I'll be ${ age +1 } years old next month.`
    console.log(sentence)


    // Arrays
    let list: number[] = [1, 2, 3, 4]
    let anotherList: Array<number> = [1, 2, 3, 4]


    // Tuples
    let tuple: [string, number]
    tuple = ["hello", 10];


    // Enum
    enum Color {Red, Green, Blue};
    let color: Color = Color.Green;


    // Any and unknown - both mean any value, but unknown prohibits to anything with it
    let notSure: any = 4
    notSure = "some string"
    notSure = false

    function f1(a: any) {
        a.b(); // OK
    }
    function f2(a: unknown) {
        // a.b(); - not possible because a is unknown
    }


    // Objects
    let obj = { a: "xxx", b: 42 }
    let manyObjs: { a: string, b: number }[] = [ obj ]


    // Functions
    function plus(left: number, right: number): number {
        return left + right
    }

    let op: (left: number, right: number) => number = plus

    op = function(left: number, right: number) {
        return left + right
    }

    op = (left: number, right: number) => left + right

    function printHello(): void { // void can be omited
        console.log("Hello world.")
    }


    // Null and undefined - correspond to Null type in Scala
    let u: undefined = undefined;
    let n: null = null;


    // Never - corresponds to Nothing in Scala
    function error(message: string): never {
        throw new Error(message);  // Function returning never must have unreachable end point
    }

    function fail() { // Inferred return type is never
        return error("Something failed");
    }

    function infiniteLoop(): never {
        while (true) { // Function returning never must have unreachable end point
        }
    }


    // Destructing
    let [msg, priority] = tuple
    let [, second, ...rest] = list

    // Object deconstructing with renaming
    let { a: objA, b: objB } = obj;

    // The same with default values. TSC knows that obj does not have "c" and thus reports an error.
    // The type assertion prevents that.
    let { a: objA2, b: objB2, c: objC2 = false } = <any>obj;


    // Spread
    let tail = [second, ...rest]
    let newObj = { c: false, ...obj }

}