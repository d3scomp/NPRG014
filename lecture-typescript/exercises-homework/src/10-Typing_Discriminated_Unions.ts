/*
Features:
- discriminated unions

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e10 {

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

    let square: Square = { kind: "square", size: 100 };
    let rectangle: Rectangle = { kind: "rectangle", width: 100, height: 200 };
    let circle: Circle = { kind: "circle", radius: 100 };

    console.log(area(square));
}



