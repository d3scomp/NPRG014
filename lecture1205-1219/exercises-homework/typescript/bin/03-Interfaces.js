var e03;
(function (e03) {
    function printLabel(labelledObj) {
        console.log(labelledObj.label);
    }
    function createObj(label, size) {
        return { label: label, size: size };
    }
    function printLabel2(labelledObj) {
        console.log(labelledObj.label);
    }
    function printLabel3(labelledObj) {
        console.log(labelledObj.label);
    }
    var myObj = createObj("Size 10 Object", 10);
    printLabel(myObj);
    printLabel2(myObj);
    printLabel3(myObj);
    var shoe = myObj;
    printLabel2(shoe);
    var shoe2 = { label: "Size 11 shoe", size: 11, brand: "Adidas" };
})(e03 || (e03 = {}));
//# sourceMappingURL=03-Interfaces.js.map