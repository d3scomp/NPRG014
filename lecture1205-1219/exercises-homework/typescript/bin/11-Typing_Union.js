var e11;
(function (e11) {
    function padLeft(value, padding) {
        if (typeof padding === "number") {
            return Array(padding + 1).join(" ") + value;
        }
        if (typeof padding === "string") {
            return padding + value;
        }
        throw new Error("Expected string or number, got '" + padding + "'.");
    }
    padLeft("Hello world", 4);
})(e11 || (e11 = {}));
//# sourceMappingURL=11-Typing_Union.js.map