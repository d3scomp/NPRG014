var e14;
(function (e14) {
    function area(s) {
        switch (s.kind) {
            case "square": return s.size * s.size;
            case "rectangle": return s.height * s.width;
            case "circle": return Math.PI * Math.pow(s.radius, 2);
        }
    }
})(e14 || (e14 = {}));
//# sourceMappingURL=14-Typing_Discriminated_Unions.js.map