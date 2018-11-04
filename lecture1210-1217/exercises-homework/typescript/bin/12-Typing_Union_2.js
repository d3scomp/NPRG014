var e12;
(function (e12) {
    var Bird = (function () {
        function Bird() {
        }
        Bird.prototype.fly = function () {
            console.log("Flying ...");
        };
        Bird.prototype.layEggs = function () {
            console.log("Laying eggs ...");
        };
        return Bird;
    }());
    var Fish = (function () {
        function Fish() {
        }
        Fish.prototype.swim = function () {
            console.log("Swimming ...");
        };
        Fish.prototype.layEggs = function () {
            console.log("Laying eggs ...");
        };
        return Fish;
    }());
    function getSmallPet() {
        return new Fish();
    }
    var pet = getSmallPet();
    pet.layEggs();
    if (pet instanceof Fish) {
        pet.swim();
    }
    else {
        pet.fly();
    }
    var x = "Hello";
    if (typeof x === "string") {
        console.log(x.charAt(0));
    }
    function canFly(pet) {
        return pet.fly !== undefined;
    }
    if (canFly(pet)) {
        pet.fly();
    }
})(e12 || (e12 = {}));
//# sourceMappingURL=12-Typing_Union_2.js.map