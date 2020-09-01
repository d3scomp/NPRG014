class Story {
    String words
    
    def Story plus(Story other) {
        new Story(words: this.words + (this.words.size()>0 && other.words.size()>0?' ':'') + other.words)
    }
    
    public static final Story id = new Story(words: '')
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Story) return words.equals(other.words)
        return false
    }
}

def stories = [
new Story(words: "Once upon a time."),
new Story(words: "A dog went on a walk."),
new Story(words: "A cat sang a beautiful song."),
new Story(words: "A bear ate honey."),
new Story(words: "A man married a woman of his heart."),
new Story(words: "A child was born."),
new Story(words: "A school was closed."),
new Story(words: "Friends got together."),
new Story(words: "A fairy danced.")
]

import static Story.id

def a = stories[0]
def b = stories[1]
def c = stories[2]

assert id + a == a
assert a + id == a
assert a + id == id + a
assert a + (b + c) == (a + b) + c

println ((stories[0] + stories[1]).words)

def result = stories.inject(Story.id) {story1, story2 -> story1 + story2}
println result
println result.words