/*
Features:
- index types

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e16 {

    function pluck<T, K extends keyof T>(o: T, names: K[]): T[K][] { // keyof T is the union of known, public property names of T
        return names.map(n => o[n]);
    }

    interface Person {
        name: string;
        age: number;
    }

    let person: Person;

    let name: string[] = pluck(person, ['name']); // ok, string[]
    let nameAndAge: (string | number)[] = pluck(person, ['name', 'age']);
}



