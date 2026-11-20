import groovy.concurrent.Actor
import java.util.concurrent.CompletableFuture

def doneSignal = new CompletableFuture<Boolean>()

record ScoreUpdate(String who, int points) {}

// ACTOR 4: The scoreboard
def scoreboard = Actor.stateful([Alice: 0, Bob: 0, updates: 0]) { scores, msg ->

    if (msg == 'snapshot') {
        return scores
    } else if (msg instanceof ScoreUpdate) {
        try{
        // TODO 1:
        // Add the received points to the appropriate contestant.
    
        scores[msg.who]=msg.points
        
        } catch (e) {e.printStackTrace()}
        scores['updates']++
        if (scores['updates'] >= 6) doneSignal.complete(true)
        return scores
    } else {
        return 'Unknown request'
    }
}

// Factory for contestant actors
def makePlayer = { final name, final answers ->

    // ACTOR 2 or 3: A contestant
    Actor.stateful([score: 0]) { state, question ->

        // TODO 2:
        // 1. Check whether the contestant's answer is correct - `answers[question.id] - question.answer`.
        // 2. Send a message to the scoreboard with the updated score for this contestant - ScoreUpdate.
        // 3. Return the updated contestant state.

        def points = (answers[question.id] == question.answer) ? 1 : 0
        scoreboard.send(new ScoreUpdate(name, state.score + points))
        [score: state.score + points]
    }
}

def alice = makePlayer('Alice', [
    1: 'Mars',
    2: '4',
    3: 'Pacific'
])

def bob = makePlayer('Bob', [
    1: 'Venus',
    2: '4',
    3: 'Atlantic'
])

// ACTOR 1: The quiz master
def quizMaster = Actor.stateful(0) { rounds, question ->

    // TODO 3:
    // Send the question to BOTH contestants concurrently.
    // Wait for both responses.
    alice.send(question)
    bob.send(question)

    return rounds + 1
}

// The quiz
def questions = [
    [id: 1, text: 'Which planet is known as the Red Planet?',
     answer: 'Mars'],

    [id: 2, text: 'What is 2 + 2?',
     answer: '4'],

    [id: 3, text: 'What is the largest ocean?',
     answer: 'Pacific']
]

try {
    questions.each { question ->
        quizMaster.send(question)
    }

    doneSignal.get()
    println "Final scores: ${await(scoreboard.sendAndGet('snapshot'))}"

} finally {
    [quizMaster, alice, bob, scoreboard].each { it.stop() }
}