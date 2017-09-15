// Javascript is object-based language
console.log('\n=== Object #1 ===');

// object definition
var course = { 
    name: "Concepts of programming languages", 
    code: 'NPRG014', 
    "short note": "this is a note", 
    getName: function() { return this.name; }
};

// property access
console.log('Object value:', course);
console.log('Object property name value:', course.name);
console.log('\n - deleting property "short note');

delete(course['short note']);
console.log('Object value:', course);
console.log('Object property "short note" value:', course["short note"]);

console.log('\n - adding a new property "comment');

course.comment = "Overview of interesting language concepts";
console.log('Object value:', course);

// test if course has property 'comment'
console.log('\nObject contains comment property:', 'comment' in course);

console.log('\nCalling the method course.getName():', course.getName());

// ---------------------------------
// This keyword
console.log('\n=== Object #2 ===');
function printComment(prefix) {
    console.log(prefix, '"' + this.comment + '"');
}

printComment.apply(course, ['This is the value of course.comment property:']);
var course2 = { name: 'Eclipse lesson', comment: 'Intro into Eclipse platform'};

printComment.apply(course2, ['This is the value of course2.comment property:']);
var course3 = {
    name: 'D3S seminar', comment: 'Various research-related talks', 
    printComment: printComment
};
course3.printComment('This is the value of course3.comment property:');

// ---------------------------------
// Object constructor
console.log('\n=== Object #3 ===');
function Course(name, id) {
    this.name = name;
    this.id = id;
    this.print = function(prefix) {
        console.log(prefix || "", name, '(' + id + ')');
    };
}
var course4 = new Course("Seminar", "NPRGXXX");
course4.print();

// or via function returning the object
function makeCourse(name, id) {
    return { 
        name: name,
        id: id,
        print: function(prefix) {
            console.log(prefix || "", name, '(' + id + ')');
        }
    };
}
var course5 = makeCourse("C#", "NPRGYYY");
course5.print();


// But that is not entirely the same. new does a few things behind the scenes. For one thing, our course4 has a property called constructor, which points at the Course function that created it. course5 also has such a property, but it points at the Object function.
console.log('course4.constructor =', course4.constructor);
console.log('course5.constructor =', course5.constructor);

// ---------------------------------
// Object 
console.log('\n=== Object #4 ===');
var simpleObject = {}; // is equal to new Object();

console.log('simpleObject =', simpleObject);
console.log('simpleObject.constructor =', simpleObject.constructor);
