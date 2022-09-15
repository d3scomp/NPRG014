import java.util.regex.Matcher
import java.util.regex.Pattern

// ~ creates a Pattern from String
def pattern = ~/foo/
assert pattern instanceof Pattern
assert pattern.matcher("foo").matches()    // returns TRUE
assert !pattern.matcher("foobar").matches() // returns FALSE, because matches() must match whole String

// =~ creates a Matcher, and in a boolean context, it's "true" if it has at least one match, "false" otherwise.
assert "cheesecheese" =~ "cheese"
assert "cheesecheese" =~ /cheese/
assert "cheese" == /cheese/   /*they are both string syntaxes*/
assert !("cheese" =~ /ham/)

// ==~ tests, if String matches the pattern
assert "2009" ==~ /\d+/  // returns TRUE
assert !("holla" ==~ /\d+/) // returns FALSE

// lets create a Matcher
def matcher = "cheesecheese" =~ /cheese/
assert matcher instanceof Matcher

// lets do some replacement
def cheese = ("cheesecheese" =~ /cheese/).replaceFirst("nice")
assert cheese == "nicecheese"
assert "color" == "colour".replaceFirst(/ou/, "o")

cheese = ("cheesecheese" =~ /cheese/).replaceAll("nice")
assert cheese == "nicenice"

//Grouping
def m = "foobarfoo faobirfao" =~ /o(b.?r)f/
assert m[0] == ["obarf", "bar"]
assert m[0][1] == "bar"

assert m[1] == ["obirf", "bir"]
assert m[1][1] == "bir"

println 'ok'
