// ---------------------------------
// variable definition, overriding does not work
console.log('\n=== Function #1 ===');
function test(msg) {
    console.log('this is just a test with message' + msg);
}

function test() {
    console.log('this is just a test...');
}

test('CALLING the test');

// ---------------------------------
// functions are first-level entities
console.log('\n=== Function #2 ===');
var add = function(a, b) {
      return a + b;
}; // <--- semicolon IS IMPORTANT because add = <expecting expression here>

console.log(add(5, 5)); 

// ---------------------------------
// function as object constructor
console.log('\n=== Function #3 ===');
function createCourse(name, id) {
    return {name: name, id: id};
}

console.log("Defined course: " + createCourse("Concepts...", 'NPRG014'));


// ---------------------------------
// variable number of arguments 
console.log('\n=== Function #4 ===');
function varArgs() {
    console.log('- The function takes ' + arguments.length + ' arguments');
    console.log('- The variable arguments is an object', arguments);
    console.log('- To convert object to array use Array.prototype.slice.call(arguments):', Array.prototype.slice.call(arguments)); 
}

varArgs();
varArgs(1);
varArgs(1,2,3,4,5);

// ---------------------------------
// default parameters in function
console.log('\n=== Function #4 ===');
function defaultParams(msg, count) {
    console.log('Repeating', msg, count, '-times');
    for(var i = 0; i < (count || 2); i++) {
        console.log('-', msg);
    }
}

defaultParams("Hi", 1);
defaultParams("Hello");

