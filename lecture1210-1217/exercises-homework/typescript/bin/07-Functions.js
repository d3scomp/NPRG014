var e07;
(function (e07) {
    var Card = (function () {
        function Card(suit, card) {
            this.suit = suit;
            this.card = card;
        }
        return Card;
    }());
    function hearts(card) {
        var restOfCards = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            restOfCards[_i - 1] = arguments[_i];
        }
        return [card].concat(restOfCards).map(function (card) { return new Card("hearts", card); });
    }
    function spades(card) {
        var restOfCards = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            restOfCards[_i - 1] = arguments[_i];
        }
        return [card].concat(restOfCards).map(function (card) { return new Card("spades", card); });
    }
    function clubs(card) {
        var restOfCards = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            restOfCards[_i - 1] = arguments[_i];
        }
        return [card].concat(restOfCards).map(function (card) { return new Card("clubs", card); });
    }
    function diamonds(card) {
        var restOfCards = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            restOfCards[_i - 1] = arguments[_i];
        }
        return [card].concat(restOfCards).map(function (card) { return new Card("diamonds", card); });
    }
    var suits = ["hearts", "spades", "clubs", "diamonds"];
    function pickCard(x) {
        if (typeof x == "object") {
            var pickedCard = Math.floor(Math.random() * x.length);
            return pickedCard;
        }
        else if (typeof x == "number") {
            var pickedSuit = Math.floor(x / 13);
            return { suit: suits[pickedSuit], card: x % 13 };
        }
    }
    var myDeck = diamonds(3, 4).concat(spades(5, 3, 6));
    var pickedCard1 = myDeck[pickCard(myDeck)];
    console.log("card: " + pickedCard1.card + " of " + pickedCard1.suit);
    var pickedCard2 = pickCard(15);
    console.log("card: " + pickedCard2.card + " of " + pickedCard2.suit);
})(e07 || (e07 = {}));
//# sourceMappingURL=07-Functions.js.map