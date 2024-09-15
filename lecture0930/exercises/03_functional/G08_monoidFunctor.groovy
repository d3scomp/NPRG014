class Story {
    String words
    
    def Story plus(Story other) {
        new Story(words: this.words + ' ' + other.words)
    }
    
    public static final Story id = new Story(words: '')
    
    def Story map(Closure<String> f) {
        new Story(words: f(this.words))
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

def result = stories.inject(Story.id) {story1, story2 -> story1 + story2}
println result
println result.words

def allcaps = result.map {it.toUpperCase()}
println allcaps.words

def shortVersion = result.map {it.toUpperCase()}.map {it.substring(0, 40)}.map {it.reverse()}
println shortVersion.words