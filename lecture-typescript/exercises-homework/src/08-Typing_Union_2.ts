/*
Features:
- union types
- type guards (built in, user-defined)

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e08 {

    class Bird {
        fly() {
            console.log("Flying ...")
        }

        layEggs() {
            console.log("Laying eggs ...")
        }
    }

    class Fish {
        swim() {
            console.log("Swimming ...")
        }

        layEggs() {
            console.log("Laying eggs ...")
        }
    }

    function getSmallPet(): Fish | Bird {
        return new Fish()
    }

    let pet = getSmallPet();
    pet.layEggs();
    // pet.swim();  Compile error. Only methods common to both Fish and Bird are allowed.

    // Type guards (typeof, instanceof)
    if (pet instanceof Fish) {
        pet.swim()
    } else {
        pet.fly() // The compiler knows that pet is Bird since it is not a Fish
    }

    // Another example of type guards
    let x: any = "Hello"
    if (typeof x === "string") {
        console.log(x.charAt(0))
    }


    // User-defined type guards
    interface Flying {
        fly(): void
    }

    function canFly(pet: any): pet is Flying {
        return (<Flying>pet).fly !== undefined;
    }

    if (canFly(pet)) {
        pet.fly()
    }

}



