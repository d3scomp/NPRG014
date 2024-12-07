/*
Features:
- index types

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e12 {
    // It is possible to refer to types of object attributes
    type Easing = "ease-in" | "ease-out" | "ease-in-out";

    interface Animation {
        duration: number;
        easing: Easing;
    }

    type NarrowedAnimation<A extends Animation, B extends Animation> = {
        duration: number;
        easing: A["easing"] | B["easing"];
    }

    function mergeAnimations<A extends Animation, B extends Animation>(a: A, b: B): NarrowedAnimation<A, B>[] {
        throw new Error("Not implemented");
    }

    const ma = mergeAnimations({ duration: 1000, easing: "ease-in" }, { duration: 500, easing: "ease-out" });
    const ease = ma[0].easing; // Type is "ease-in"|"ease-out" (no "ease-in-out")


    // -------------------------------------
    // Generic keys using keyof
    function pluck<T, K extends keyof T>(o: T, names: K[]): T[K][] { // keyof T is the union of known, public property names of T
        return names.map(n => o[n]);
    }

    interface Person {
        name: string;
        age: number;
    }

    let person: Person;

    person = {
        name: "Bob",
        age: 30
    }

    let name = pluck(person, ['name']); // ok, string[]
    let nameAndAge = pluck(person, ['name', 'age']);
    console.log(name);
    console.log(nameAndAge);
}