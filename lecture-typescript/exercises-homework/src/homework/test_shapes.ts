import {
    calculateShapeArea, Color,
    colorTable,
    createCircle,
    createRectangle,
    createShape,
    createSquare,
    createTriangle,
    filterShapesByType,
    invertColor,
    makeObservable,
    Rectangle,
    Square
} from './shapes';

const shapes = [
    createCircle(10, "red"),
    createRectangle(20, 10, "blue"),
    createTriangle(15, 10, "green"),
    createSquare(10, {r: 255, g: 255, b: 0})
];

// const t1 = createTriangle(10, 10, "yellow"); --- does not compile - "yellow" is not recognized

const r1 = createShape('rectangle', 10, 10, "red");
// const r2: Circle = createShape('rectangle', 10, 10, "red"); --- does not compile, the result cannot be assigned to Circle
// const r2 = createShape('rectangle', 10, "red"); --- does not compile, rectangle requires width and height
// const r2 = createShape('rectangle', 10, 10, "yellow"); --- does not compile, "yellow" is not recognized
console.log(calculateShapeArea(r1));

const s1 = createSquare(10, "blue");
// console.log(calculateShapeArea(s1)); --- does not compile - s1 is not accepted


const {r: blueR, g: blueG, b: blueB} = colorTable["blue"];
// const {z} = colorTable["blue"]; --- does not compile - "z" is not recognized
// const {r: yellowR, g: yellowG, b: yellowB}: NumericColor = colorTable["yellow"]; --- does not compile - "yellow" is not recognized

const is1 = invertColor(s1);
console.log(`is1 color: rgb(${is1.color.r}, ${is1.color.g}, ${is1.color.b})`);
const is2: Square = is1;
// const ic1: Circle = is1; --- does not compile, is1 cannot be assigned to Circle


const os1 = makeObservable(s1);
os1.on('width', (oldValue, newValue) => {
    console.log(`Width changed from ${oldValue} to ${newValue}`);
});

function colorToString(color: Color): string {
    if (typeof color === "string")
        return color;
    else
        return `rgb(${color.r},${color.g},${color.b})`;
}

const or1 = makeObservable(r1);
or1.on('height', (oldValue, newValue) => {
    console.log(`Height changed from ${oldValue} to ${newValue}`);
});
or1.on('color', (oldValue, newValue) => { // oldValue and newValue are typed as Color
    console.log(`Color changed from ${colorToString(oldValue)} to ${colorToString(newValue)}`);
})

os1.update({ width: 15 });
// os1.update({ height: 15 }); --- does not compile "height" is not recognized
or1.update({ height: 20, color: {r: 100, g: 100, b: 100} });


const filteredShapes: (Rectangle|Square)[] = filterShapesByType(shapes, ['rectangle', 'square']);
const filteredShapes2: Rectangle[] = filterShapesByType(filteredShapes, ['rectangle', 'triangle']);
