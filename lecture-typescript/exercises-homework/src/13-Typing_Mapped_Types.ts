/*
Features:
- mapped types

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

import {SuiteContext} from "node:test";

namespace e13 {



    class Proxy<T> {
        private value: T

        constructor(value: T) {
            this.value = value
        }

        get(): T {
            return this.value;
        }

        set(value: T) {
            this.value = value
        }
    }

    type Proxified<T> = {
        [P in keyof T]: Proxy<T[P]>;
    }

    function proxify<T>(o: T): Proxified<T> {
        let result = {} as Proxified<T>

        for (const k in o) {
            result[k] = new Proxy(o[k])
        }

        return result;
    }

    function unproxify<T>(t: Proxified<T>): T {
        let result = {} as T;

        for (const k in t) {
            result[k] = t[k].get();
        }

        return result;
    }

    interface SampleObj {
        a: string
        b: number
    }

    let props = {
        a: "XXX",
        b: 12
    }

    let proxyProps: Proxified<SampleObj> = proxify(props);
    console.log(proxyProps);

    let props2 = unproxify(proxyProps)
    console.log(props2);


    // Use of specific keys
    type SpecificKeys = 'a' | 'c';

    // Mapped type that only proxies a specified subset of properties
    type SubsetProxy<T> = {
        [P in (SpecificKeys & keyof T)]: Proxy<T[P]>;
    }
    const subProxy: SubsetProxy<SampleObj> = proxify(props);
    // console.log(subProxy.b); --- does not compile - b is not recognized
}



