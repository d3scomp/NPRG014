//TASK make the Story class a functor, so that it accepts String -> String functions
class Story {
    String words
    
}

def story = new Story(words: "")

//All functions have the signature String -> String
def initiate = {text -> "Once upon a time, there was a king and a queen."}
def repeat = {text -> text*3}
def upper = {text -> text.toUpperCase()}
def print = {text -> println(text);return text;}

def result = story.map(initiate).map(repeat).map(upper).map(print)
println result