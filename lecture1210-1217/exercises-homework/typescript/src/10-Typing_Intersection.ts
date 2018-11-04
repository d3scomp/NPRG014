/*
Features:
- intersection types
- type alises
- attribute definition in constructor (of Person)

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e10 {

    // Intersection Types
    function extend<T, U>(first: T, second: U): T & U {
        let result = <T & U>{};
        for (let id in first) {
            (<any>result)[id] = (<any>first)[id];
        }
        for (let id in second) {
            if (!result.hasOwnProperty(id)) {
                (<any>result)[id] = (<any>second)[id];
            }
        }
        return result;
    }

    class Person {
        constructor(public name: string) { }
    }
    interface Loggable {
        log(): void;
    }
    class ConsoleLogger implements Loggable {
        log() {
            // ...
        }
    }

    type LoggablePerson = Person & Loggable

    var jim: LoggablePerson = extend(new Person("Jim"), new ConsoleLogger());
    var n = jim.name;
    jim.log();




    interface Car {
        brand: string
    }
    type LinkedList<T> = T & { next: LinkedList<T> }

    let cars: LinkedList<Car> = { brand: "Volvo", next: { brand: "VW", next: { brand: "Toyota", next: null } } }
    console.log(cars.brand)
    console.log(cars.next.brand)
    console.log(cars.next.next.brand)

    /* ASSIGNMENT
     Modify the type declaration of LinkedList<T> above such that the following is possible:

     let cars: LinkedList<Car> = { brand: "Volvo", next: { brand: "VW", next: { brand: "Toyota" } } }
     */
 }



