var e08;
(function (e08) {
    var Disposable = (function () {
        function Disposable() {
        }
        Disposable.prototype.dispose = function () {
            this.isDisposed = true;
        };
        return Disposable;
    }());
    var Activatable = (function () {
        function Activatable() {
        }
        Activatable.prototype.activate = function () {
            this.isActive = true;
        };
        Activatable.prototype.deactivate = function () {
            this.isActive = false;
        };
        return Activatable;
    }());
    var SmartObject = (function () {
        function SmartObject() {
            var _this = this;
            this.isDisposed = false;
            this.isActive = false;
            setInterval(function () { return console.log(_this.isActive + " : " + _this.isDisposed); }, 500);
        }
        SmartObject.prototype.interact = function () {
            this.activate();
        };
        return SmartObject;
    }());
    function applyMixins(derivedCtor, baseCtors) {
        baseCtors.forEach(function (baseCtor) {
            Object.getOwnPropertyNames(baseCtor.prototype).forEach(function (name) {
                derivedCtor.prototype[name] = baseCtor.prototype[name];
            });
        });
    }
    applyMixins(SmartObject, [Disposable, Activatable]);
    var smartObj = new SmartObject();
    setTimeout(function () { return smartObj.interact(); }, 1000);
})(e08 || (e08 = {}));
//# sourceMappingURL=08-Mixins.js.map