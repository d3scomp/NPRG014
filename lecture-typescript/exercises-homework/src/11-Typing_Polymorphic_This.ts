/*
Features:
- polymorphic this

https://www.typescriptlang.org/docs/handbook/advanced-types.html
*/

namespace e11 {

    class BasicCalculator {
        public constructor(protected value: number = 0) { }
        public currentValue(): number {
            return this.value;
        }
        public add(operand: number): this {
            this.value += operand;
            return this;
        }
        public multiply(operand: number): this {
            this.value *= operand;
            return this;
        }
        // ... other operations go here ...
    }

    class ScientificCalculator extends BasicCalculator {
        public constructor(value = 0) {
            super(value);
        }
        public sin() {
            this.value = Math.sin(this.value);
            return this;
        }
        // ... other operations go here ...
    }

    let v = new ScientificCalculator(2)
        .multiply(5)
        .sin()
        .add(1)
        .currentValue();

    console.log(v);
}



