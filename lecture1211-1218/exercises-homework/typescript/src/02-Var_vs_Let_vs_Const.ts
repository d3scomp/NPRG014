/*
Features:
- variable declaration - var, let, const

https://www.typescriptlang.org/docs/handbook/variable-declarations.html
*/

namespace e02 {

    var a = 0
    let b = 0
    const c = 0

    function test1() {
        a = 1
        b = 1
        // c = 1  -- Compiler error
        console.log(`test1: a = ${a}, b = ${b}, c = ${c}`)
    }

    function test2() {
        var a = 2
        let b = 2
        const c = 2
        console.log(`test2: a = ${a}, b = ${b}, c = ${c}`)
    }

    function test3() {
        var a = 3
        let b = 3
        const c = 3

        {
            var a = 4 // Var is not properly scoped. It is either global, or local to a function
            let b = 4
            const c = 4
            console.log(`test3a: a = ${a}, b = ${b}, c = ${c}`)
        }

        console.log(`test3b: a = ${a}, b = ${b}, c = ${c}`)
    }

    function test4() {
        for (var i = 0; i < 5; i++) {
            setTimeout(function () {
                console.log(`test4a: ${i}`)
            })
        }

        for (let j = 0; j < 5; j++) {
            setTimeout(function () {
                console.log(`test4b: ${j}`)
            })
        }
    }

    test1()
    test2()
    test3()

    console.log(`global: a = ${a}, b = ${b}, c = ${c}`)

    test4()
}