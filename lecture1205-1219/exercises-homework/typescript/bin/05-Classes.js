var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var e05;
(function (e05) {
    var Animal = (function () {
        function Animal(theName) {
            this._name = theName;
            Animal._allAnimalsCount++;
        }
        Object.defineProperty(Animal.prototype, "name", {
            get: function () {
                return this._name;
            },
            set: function (newName) {
                this._name = newName;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(Animal, "allAnimalsCount", {
            get: function () {
                return Animal._allAnimalsCount;
            },
            enumerable: true,
            configurable: true
        });
        return Animal;
    }());
    Animal._allAnimalsCount = 0;
    var Snake = (function (_super) {
        __extends(Snake, _super);
        function Snake(name) {
            return _super.call(this, name) || this;
        }
        Snake.prototype.move = function (distanceInMeters) {
            if (distanceInMeters === void 0) { distanceInMeters = 5; }
            console.log(this.name + " slithered " + distanceInMeters + "m.");
        };
        return Snake;
    }(Animal));
    var Horse = (function (_super) {
        __extends(Horse, _super);
        function Horse(name) {
            return _super.call(this, name) || this;
        }
        Horse.prototype.move = function (distanceInMeters) {
            if (distanceInMeters === void 0) { distanceInMeters = 45; }
            console.log(this.name + " galloped " + distanceInMeters + "m.");
        };
        Horse.prototype.mount = function () {
            console.log("Mounted " + this.name);
        };
        return Horse;
    }(Animal));
    var sam = new Snake("Sammy the Python");
    var tom = new Horse("Tommy the Palomino");
    sam.move();
    tom.move(34);
    var mnt = tom;
    mnt.mount();
    console.log(Animal.allAnimalsCount);
    var Herd = (function (_super) {
        __extends(Herd, _super);
        function Herd() {
            return _super.apply(this, arguments) || this;
        }
        return Herd;
    }(Array));
    var herd = new Herd();
    herd.push(tom);
    function createAndMove(ctor, name, distanceInMeters) {
        var animal = new ctor(name);
        animal.move(distanceInMeters);
        return animal;
    }
    var jenny = createAndMove(Horse, "Jenny the Palomino", 50);
})(e05 || (e05 = {}));
//# sourceMappingURL=05-Classes.js.map