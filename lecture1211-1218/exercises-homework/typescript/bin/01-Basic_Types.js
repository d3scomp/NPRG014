var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
var e01;
(function (e01) {
    var isDone = false;
    var fullName;
    fullName = "John";
    var age = 20;
    var sentence = "Hello, my name is " + fullName + ", I'll be " + (age + 1) + " years old next month.";
    console.log(sentence);
    var list = [1, 2, 3, 4];
    var anotherList = [1, 2, 3, 4];
    var tuple;
    tuple = ["hello", 10];
    var Color;
    (function (Color) {
        Color[Color["Red"] = 0] = "Red";
        Color[Color["Green"] = 1] = "Green";
        Color[Color["Blue"] = 2] = "Blue";
    })(Color || (Color = {}));
    ;
    var color = Color.Green;
    var notSure = 4;
    notSure = "some string";
    notSure = false;
    var obj = { a: "xxx", b: 42 };
    var manyObjs = [obj];
    function plus(left, right) {
        return left + right;
    }
    var op = plus;
    op = function (left, right) {
        return left + right;
    };
    op = function (left, right) { return left + right; };
    function printHello() {
        console.log("Hello world.");
    }
    var u = undefined;
    var n = null;
    function error(message) {
        throw new Error(message);
    }
    function fail() {
        return error("Something failed");
    }
    function infiniteLoop() {
        while (true) {
        }
    }
    var msg = tuple[0], priority = tuple[1];
    var second = list[1], rest = list.slice(2);
    var objA = obj.a, objB = obj.b;
    var _a = obj, objA2 = _a.a, objB2 = _a.b, _b = _a.c, objC2 = _b === void 0 ? false : _b;
    var tail = [second].concat(rest);
    var newObj = __assign({ c: false }, obj);
})(e01 || (e01 = {}));
//# sourceMappingURL=01-Basic_Types.js.map