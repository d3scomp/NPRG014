var e17;
(function (e17) {
    var Proxy = (function () {
        function Proxy(value) {
            this.value = value;
        }
        Proxy.prototype.get = function () {
            return this.value;
        };
        Proxy.prototype.set = function (value) {
            this.value = value;
        };
        return Proxy;
    }());
    function proxify(o) {
        var result = {};
        for (var k in o) {
            result[k] = new Proxy(o[k]);
        }
        return result;
    }
    function unproxify(t) {
        var result = {};
        for (var k in t) {
            result[k] = t[k].get();
        }
        return result;
    }
    var props = {
        a: "XXX",
        b: 12
    };
    var proxyProps = proxify(props);
    var props2 = unproxify(proxyProps);
})(e17 || (e17 = {}));
//# sourceMappingURL=17-Typing_Mapped_Types.js.map