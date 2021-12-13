/*
Features:
- function parameters
- function overloading

https://www.typescriptlang.org/docs/handbook/functions.html
*/

namespace e07 {

    class Card {
        constructor(suit: string, card: number) {
            this.suit = suit
            this.card = card
        }

        readonly suit: string
        readonly card: number
    }

    function hearts(card: number, ...restOfCards: number[]): Card[] {
        return [card, ...restOfCards].map(card => new Card("hearts", card))
    }

    function spades(card: number, ...restOfCards: number[]): Card[] {
        return [card, ...restOfCards].map(card => new Card("spades", card))
    }

    function clubs(card: number, ...restOfCards: number[]): Card[] {
        return [card, ...restOfCards].map(card => new Card("clubs", card))
    }

    function diamonds(card: number, ...restOfCards: number[]): Card[] {
        return [card, ...restOfCards].map(card => new Card("diamonds", card))
    }


    // Function overloading
    let suits = ["hearts", "spades", "clubs", "diamonds"];

    function pickCard(x: Card[]): number;
    function pickCard(x: number): Card;
    function pickCard(x): any {
        // Check to see if we're working with an object/array
        // if so, they gave us the deck and we'll pick the card
        if (typeof x == "object") {
            let pickedCard = Math.floor(Math.random() * x.length);
            return pickedCard;
        }
        // Otherwise just let them pick the card
        else if (typeof x == "number") {
            let pickedSuit = Math.floor(x / 13);
            return { suit: suits[pickedSuit], card: x % 13 };
        }
    }

    let myDeck = [...diamonds(3, 4), ...spades(5, 3, 6)]
    let pickedCard1 = myDeck[pickCard(myDeck)];
    console.log("card: " + pickedCard1.card + " of " + pickedCard1.suit);

    let pickedCard2 = pickCard(15);
    console.log("card: " + pickedCard2.card + " of " + pickedCard2.suit);
}



