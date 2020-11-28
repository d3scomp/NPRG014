var text = "this is a simple text";
var text2 = 'this is a simple text';

// property access
console.log('Text length =', text["length"]);
console.log('Text length =', text.length);

var nothing = null;
try { 
    console.log(nothing.length);
} catch (error) {
    console.log('Calling nothing.length throws the exception', error);
}

// no scoping
var var1 = '3';
console.log('Variable value outside the scope', var1);
{ 
    console.log('Variable value inside the scope', var1);
    var var1 = '!value from the inside scope!';
}

console.log('Variable value outside the scope', var1);

