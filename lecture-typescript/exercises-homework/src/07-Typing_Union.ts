/*
Features:
- union types

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e07 {

    /**
     * Takes a string and adds "padding" to the left.
     * If 'padding' is a string, then 'padding' is appended to the left side.
     * If 'padding' is a number, then that number of spaces is added to the left side.
     */
    function padLeft(value: string, padding: string | number) {
        if (typeof padding === "number") {
            return Array(padding + 1).join(" ") + value;
        }
        if (typeof padding === "string") {
            return padding + value;
        }
        throw new Error(`Expected string or number, got '${padding}'.`);
    }

    const x = padLeft("Hello world", 4); // returns "    Hello world"
    console.log(x);
}



