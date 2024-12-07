/*
Features:
- intersection types
- type alises
- attribute definition in constructor (of Person)

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e06 {

    interface Person {
        name: string;
        age: number;
    }

    interface Loggable {
        log(): void;
    }

    const jim = {
        name: "Jim",
        age: 30,
        log: function () {
            console.log(`Hello world, my name is ${this.name}!`);
        }
    }

    type LoggablePerson = Person & Loggable

    const x: LoggablePerson = jim;
    x.log();




    interface Car {
        brand: string
    }
    type LinkedList<T> = T & { next: LinkedList<T> | null }

    let cars: LinkedList<Car> = { brand: "Volvo", next: { brand: "VW", next: { brand: "Toyota", next: null } } }
    console.log(cars.brand)
    console.log(cars.next?.brand)
    console.log(cars.next?.next?.brand)

 }



