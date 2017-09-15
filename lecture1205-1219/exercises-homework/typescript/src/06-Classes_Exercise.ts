/*
Features:
- assignment on classes

https://www.typescriptlang.org/docs/handbook/interfaces.html
https://www.typescriptlang.org/docs/handbook/classes.html
https://www.typescriptlang.org/docs/handbook/generics.html
*/

namespace e06 {

    /* Not to be uncommented. Just for reference.

    function Comparable() {
    }

    Comparable.prototype = {
        lt: function(that) {
            return this.compare(that) < 0;
        },

        gt: function(that) {
            return this.compare(that) > 0;
        }
    }

    function Rational(num, denom) {
        function gcd(a, b) {
            if (b == 0)
                return a
            else
                return gcd(b, a % b);
        }

        var g = gcd(Math.abs(num), Math.abs(denom));
        this.num = num / g;
        this.denom = denom / g;
    }

    Rational.prototype = Object.create(Comparable.prototype, {
        plus: function(that) {
            return new Rational(this.num * that.denom + that.num * this.denom, this.denom * that.denom);
        },

        compare: function(that) {
            return (this.num * that.denom) - (that.num * this.denom)
        },

        toString: function() {
            return this.num + '/' + this.denom;
        }
    })
    */

    /* ASSIGNMENT
     Reimplement the Javascript objects above to TypeScript such that one can execute the code below:

     let ra: Rational = new Rational(3, 4);
     let rb = new Rational(4, 6);
     let rc = ra.plus(rb);
     console.log("ra = " + ra); // ra = 3/4
     console.log("rb = " + rb); // rb = 2/3
     console.log("rc = " + rc); // rc = 17/12

     console.log("ra < rb ... " + ra.lt(rb)); // ra < rb ... false
     console.log("ra > rb ... " + ra.gt(rb)); // ra > rb ... true
    */


}



