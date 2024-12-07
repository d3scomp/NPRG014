// These three functions create the corresponding shape
// The color is string or {r,g,b}
import {Color, NumericColor} from "./shapes";

export function createCircle(radius, color) {
    return {
        type: "circle",
        radius,
        color
    };
}

export function createSquare(width, color) {
    return {
        type: "square",
        width,
        color
    };
}

export function createRectangle(width, height, color) {
    return {
        type: "rectangle",
        width,
        height,
        color
    };
}

export function createTriangle(base, height, color) {
    return {
        type: "triangle",
        base,
        height,
        color
    }
}

const shapeFactories = {
    circle: createCircle,
    square: createSquare,
    rectangle: createRectangle,
    triangle: createTriangle,
}

// This should recognize types "circle", "square", "rectangle", "triangle" and return the correct type.
export function createShape(type, ...args) {
    return shapeFactories[type](...args);
}

// This function works only of Circle, Rectangle and Triangle
export function calculateShapeArea(shape) {
    if (shape.type === "circle") {
        return Math.PI * shape.radius ** 2;
    } else if (shape.type === "rectangle") {
        return shape.width * shape.height;
    } else if (shape.type === "triangle") {
        return 0.5 * shape.base * shape.height;
    } else {
        throw new Error("Unknown shape type");
    }
}

export const colorTable = {
    red: { r: 255, g: 0, b: 0 },
    green: { r: 0, g: 255, b: 0 },
    blue: { r: 0, g: 0, b: 255 },
};


// This function accepts any shape (not just the shapes that are known above) that contains "color",
// which is string or {r,g,b}. It returns the same object with color guaranteed to be {r,g,b} (i.e., no string)
export function invertColor(shape) {
    let color = shape.color;
    if (typeof color === "string") {
        color = colorTable[color];
    }

    color = {
        r: 255 - color.r,
        g: 255 - color.g,
        b: 255 - color.b
    }

    return {
        ...shape,
        color
    };
}

// Returns an array of shapes which have only the types as specified by the types parameter.
// The type can be any of "rectangle", "square", "circle", "triangle"
export function filterShapesByType(shapes, types) {
    return shapes.filter((shape) => types.includes(shape.type));
}

export function makeObservable(shape) {
    const observers = {};

    return {
        ...shape,
        on(eventName, callback) {
            observers[eventName] = callback;
        },
        update(changes) {
            for (const key in changes) {
                const oldValue = shape[key];
                shape[key] = changes[key];
                if (key in observers && key) {
                    observers[key](oldValue, changes[key]);
                }
            }
        }
    };
}
