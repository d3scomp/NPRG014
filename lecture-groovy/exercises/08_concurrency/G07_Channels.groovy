
import groovy.concurrent.AsyncChannel

def cardStream = AsyncChannel.create(3)

// Dealer — sends cards concurrently
async {
    for (card in ['A♠', 'K♥', 'Q♦', 'J♣']) {
        await cardStream.send(card)
    }
    cardStream.close()
}

// Player — receives cards as they arrive
def hand = []

for await (card in cardStream) {
    hand << card
}
assert hand == ['A♠', 'K♥', 'Q♦', 'J♣']
println 'done'