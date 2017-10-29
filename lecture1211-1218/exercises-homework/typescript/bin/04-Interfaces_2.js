var e04;
(function (e04) {
    function getCounter() {
        var counter = function (start) { };
        counter.interval = 123;
        counter.reset = function () { };
        return counter;
    }
    var c = getCounter();
    c(10);
    c.reset();
    c.interval = 5.0;
    var myArray;
    myArray = ["Bob", "Fred"];
    var myStr = myArray[0];
    var myDict = {};
    myDict["xxx"] = "AAA";
})(e04 || (e04 = {}));
//# sourceMappingURL=04-Interfaces_2.js.map