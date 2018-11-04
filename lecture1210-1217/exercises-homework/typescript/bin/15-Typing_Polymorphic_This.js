var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var e15;
(function (e15) {
    var BasicCalculator = (function () {
        function BasicCalculator(value) {
            if (value === void 0) { value = 0; }
            this.value = value;
        }
        BasicCalculator.prototype.currentValue = function () {
            return this.value;
        };
        BasicCalculator.prototype.add = function (operand) {
            this.value += operand;
            return this;
        };
        BasicCalculator.prototype.multiply = function (operand) {
            this.value *= operand;
            return this;
        };
        return BasicCalculator;
    }());
    var ScientificCalculator = (function (_super) {
        __extends(ScientificCalculator, _super);
        function ScientificCalculator(value) {
            if (value === void 0) { value = 0; }
            return _super.call(this, value) || this;
        }
        ScientificCalculator.prototype.sin = function () {
            this.value = Math.sin(this.value);
            return this;
        };
        return ScientificCalculator;
    }(BasicCalculator));
    var v = new ScientificCalculator(2)
        .multiply(5)
        .sin()
        .add(1)
        .currentValue();
})(e15 || (e15 = {}));
//# sourceMappingURL=15-Typing_Polymorphic_This.js.map