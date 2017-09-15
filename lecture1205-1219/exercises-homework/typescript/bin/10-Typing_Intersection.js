var e10;
(function (e10) {
    function extend(first, second) {
        var result = {};
        for (var id in first) {
            result[id] = first[id];
        }
        for (var id in second) {
            if (!result.hasOwnProperty(id)) {
                result[id] = second[id];
            }
        }
        return result;
    }
    var Person = (function () {
        function Person(name) {
            this.name = name;
        }
        return Person;
    }());
    var ConsoleLogger = (function () {
        function ConsoleLogger() {
        }
        ConsoleLogger.prototype.log = function () {
        };
        return ConsoleLogger;
    }());
    var jim = extend(new Person("Jim"), new ConsoleLogger());
    var n = jim.name;
    jim.log();
    var cars = { brand: "Volvo", next: { brand: "VW", next: { brand: "Toyota", next: null } } };
    console.log(cars.brand);
    console.log(cars.next.brand);
    console.log(cars.next.next.brand);
})(e10 || (e10 = {}));
//# sourceMappingURL=10-Typing_Intersection.js.map