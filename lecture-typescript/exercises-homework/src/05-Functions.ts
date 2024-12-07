/*
Features:
- function parameters
- function overloading

https://www.typescriptlang.org/docs/handbook/functions.html
*/

namespace e05 {

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
    function pickCard(x: any): any {
        // Check to see if we're working with an object/array
        // if so, they gave us the deck and we'll pick the card
        if (x instanceof Array) {
            console.log('pickCard(Card[]) called');
            let pickedCard = Math.floor(Math.random() * x.length);
            return pickedCard;
        }
        // Otherwise just let them pick the card
        else if (typeof x == "number") {
            console.log('pickCard(number) called');
            let pickedSuit = Math.floor(x / 13);
            return { suit: suits[pickedSuit], card: x % 13 };
        }
    }

    let myDeck = [...diamonds(3, 4), ...spades(5, 3, 6)]
    let pickedCard1 = myDeck[pickCard(myDeck)];
    console.log("card: " + pickedCard1.card + " of " + pickedCard1.suit);

    let pickedCard2 = pickCard(15);
    console.log("card: " + pickedCard2.card + " of " + pickedCard2.suit);



    // Another example on function overloading
    // Uses literals true/false

    const books = [
        { title: "Ubik",
            year: 1969,
            description: `Ubik is a 1969 science fiction novel by American writer Philip K. Dick. 
      The story is set in a future 1992 where psychic powers are utilized in corporate 
      espionage, while cryonic technology allows recently deceased  people to be maintained 
      in a lengthy state of hibernation.` },
        { title: "The Man in the High Castle",
            year: 1962,
            description: `The Man in the High Castle (1962) is an alternative history novel by Philip 
      K. Dick wherein the Axis Powers won  World War II. The story occurs in 1962, fifteen years 
      after the end of the war in 1947, and depicts the life of several characters living 
      under Imperial Japan or Nazi Germany as they rule the partitioned United States.` }
    ]

    type BookInfoBasic = { title: string, year: number };
    type BookInfoDetailed = BookInfoBasic & { description: string };

    function getBook(index:number, detailed: false): BookInfoBasic;
    function getBook(index:number, detailed: true): BookInfoDetailed;

    function getBook(index:number, detailed: boolean): any {
        if (detailed) {
            return {
                title: books[index].title,
                year: books[index].year,
                description: books[index].description
            }
        } else {
            return {
                title: books[index].title,
                year: books[index].year
            }
        }
    }

    let b1 = getBook(0, true)
    b1.description

    let b2 = getBook(0, false)
// b2.description  -- Does not compile
}



