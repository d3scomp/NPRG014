/*
Features:
- string literal types

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e09 {

    type Easing = "ease-in" | "ease-out" | "ease-in-out";

    class UIElement {
        animate(dx: number, dy: number, easing: Easing) {
            if (easing === "ease-in") {
            }
            else if (easing === "ease-out") {
            }
            else if (easing === "ease-in-out") {
            }
            else {
                // error! should not pass null or undefined.
            }
        }
    }

    let button = new UIElement();
    button.animate(0, 0, "ease-in");
    // button.animate(0, 0, "uneasy");  Compile error


    type Bit = 0 | 1
    type MiniByte = [Bit,Bit,Bit,Bit]
}



