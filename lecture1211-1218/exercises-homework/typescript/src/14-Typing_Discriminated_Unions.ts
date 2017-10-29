/*
Features:
- discriminated unions

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e14 {

    interface Square {
        kind: "square";
        size: number;
    }
    interface Rectangle {
        kind: "rectangle";
        width: number;
        height: number;
    }
    interface Circle {
        kind: "circle";
        radius: number;
    }

    type Shape = Square | Rectangle | Circle;

    function area(s: Shape) {
        switch (s.kind) {
            case "square": return s.size * s.size;
            case "rectangle": return s.height * s.width;
            case "circle": return Math.PI * s.radius ** 2;
        }
    }

}



