/*
Features:
- classes
- properties
- visibility (public by default)
- extends, (class-class, interface-class)
- implements
- generics
- class types

https://www.typescriptlang.org/docs/handbook/interfaces.html
https://www.typescriptlang.org/docs/handbook/classes.html
https://www.typescriptlang.org/docs/handbook/generics.html
*/

namespace e04 {

    interface Moving {
        move(distanceInMeters: number): void
    }

    abstract class Animal implements Moving {
        private _name: string

        get name(): string {
            return this._name
        }

        set name(newName: string) {
            this._name = newName
        }


        private static _allAnimalsCount = 0
        static get allAnimalsCount(): number {
            return Animal._allAnimalsCount
        }

        constructor(theName: string) {
            this._name = theName
            Animal._allAnimalsCount++
        }

        abstract move(distanceInMeters: number): void
    }

    class Snake extends Animal {
        constructor(name: string) {
            super(name)
        }

        move(distanceInMeters = 5) {
            console.log(`${this.name} slithered ${distanceInMeters}m.`)
        }
    }

    class Horse extends Animal {
        constructor(name: string) {
            super(name)
        }

        move(distanceInMeters = 45) {
            console.log(`${this.name} galloped ${distanceInMeters}m.`)
        }

        mount() {
            console.log(`Mounted ${this.name}`)
        }
    }

    interface MountableAnimal extends Animal {
        mount(): void
        move(): void
    }

    let sam = new Snake("Sammy the Python");
    let tom = new Horse("Tommy the Palomino");

    sam.move();
    tom.move(34);

    let mnt: MountableAnimal = tom

    mnt.mount()

    console.log(Animal.allAnimalsCount)


    // Generics
    class Herd<AnimalType extends Animal> extends Array<AnimalType> {
    }

    let herd = new Herd<Horse>()
    herd.push(tom)


    // Class types - describe the static part of the class
    interface AnimalConstructor {
        new(name: string): Animal
    }

    function createAndMove(ctor: AnimalConstructor, name: string, distanceInMeters: number) {
        let animal = new ctor(name)
        animal.move(distanceInMeters)
        return animal
    }

    let jenny = createAndMove(Horse, "Jenny the Palomino", 50)

}



