// Prototyping and inheritance
console.log('\n=== Prototypes #1 ===');

var Car = function (color) {
    this.color = color || "black";
}

Car.prototype = {
    type: null,
    printColor: function () { console.log(this.color); },
    constructor: Car // This field is in the prototype object created by default. 
                     // When replacing the prototype object with something else, we have to add it manually.
};

Car.prototype.type = 'abstract-car';
Car.prototype.getColor = function() { return this.color; };

var car = new Car("blue");
car.type = '';
console.log('car =', car);
console.log('cat.getColor() =', car.getColor());
console.log('car.prototype =', car.prototype);
console.log('car.__proto__ =', car.__proto__);
console.log('Car.prototype =', Car.prototype);
console.log('Car.prototype.constructor =', Car.prototype.constructor); // !!!

// Inheritance method and extending the core objects 
console.log('\n=== Prototypes #2 ===');

Object.prototype.properties = function(inheritedProperties) {
    var showInheritedProperties = inheritedProperties || false;
    var result = [];
    for(var property in this) {
        if (showInheritedProperties) {
            result.push(property);
        } else {
            if (this.hasOwnProperty(property)) {
                result.push(property);
            }
        }
    }
    return result;
}

console.log('car.properties() =', car.properties());

// Every object has a __proto__ object property (except Object); every function has a prototype object property. So objects can be related by 'prototype inheritance' to other objects.  You can test  for inheritance by comparing an object's __proto__ to a function's prototype object.  JavaScript provides a shortcut: the instanceof operator tests an object against a function and returns true if the object inherits from the function prototype.  For example, 
// chris.__proto__ == Engineer.prototype;  
// chris.__proto__.__proto__ == WorkerBee.prototype;  
// chris.__proto__.__proto__.__proto__ == Employee.prototype;  
// chris.__proto__.__proto__.__proto__.__proto__ == Object.prototype;  
// chris.__proto__.__proto__.__proto__.__proto__.__proto__ == null;

// Inheritance ECMAScript 5 style  
console.log('\n=== Prototypes #3 ===');

/* No need for capitalization as it's not a constructor*/
var someCar = {
    drive: function() {},
    name: 'Mazda 3'    
};

/* Use Object.create to generate a new car */
var anotherCar = Object.create(someCar);
anotherCar.name = 'Toyota Camry';

// the same with classical prototype
var vehiclePrototype = {
    init: function(carModel) {
        this.model = carModel;
    },
    getModel: function() {
        console.log('The model of this vehicle is:', this.model);
    }
};


function vehicle(model) {
    function F() {};
    F.prototype = vehiclePrototype;
    var f = new F();
    f.init(model);
    return f;
}

var fordCar = vehicle('Ford Escort');
fordCar.getModel();


/* ASSIGNMENT:
 * Implement a function clone in Object that works in the similar way as in IO.
 */

//var myFord = fordCar.clone();
//myFord.model ="Ford Escord (blue)";

//console.log(myFord); // Prints out: "{ model: 'Ford Escord (blue)' }"