var e02;
(function (e02) {
    var a = 0;
    var b = 0;
    var c = 0;
    function test1() {
        a = 1;
        b = 1;
        console.log("test1: a = " + a + ", b = " + b + ", c = " + c);
    }
    function test2() {
        var a = 2;
        var b = 2;
        var c = 2;
        console.log("test2: a = " + a + ", b = " + b + ", c = " + c);
    }
    function test3() {
        var a = 3;
        var b = 3;
        var c = 3;
        {
            var a = 4;
            var b_1 = 4;
            var c_1 = 4;
            console.log("test3a: a = " + a + ", b = " + b_1 + ", c = " + c_1);
        }
        console.log("test3b: a = " + a + ", b = " + b + ", c = " + c);
    }
    function test4() {
        for (var i = 0; i < 5; i++) {
            setTimeout(function () {
                console.log("test4a: " + i);
            });
        }
        var _loop_1 = function (j) {
            setTimeout(function () {
                console.log("test4b: " + j);
            });
        };
        for (var j = 0; j < 5; j++) {
            _loop_1(j);
        }
    }
    test1();
    test2();
    test3();
    console.log("global: a = " + a + ", b = " + b + ", c = " + c);
    test4();
})(e02 || (e02 = {}));
//# sourceMappingURL=02-Var_vs_Let_vs_Const.js.map