/*
 * Groovy Regular Expressions Overview:
 * Groovy significantly simplifies Java's regex handling by providing native operators 
 * and "slashy" strings (/.../) that eliminate the need to escape backslashes.
 * 
 * Key Regex Operators in Groovy:
 * 1. The Pattern Operator (~): 
 *    Transforms a String into a compiled `java.util.regex.Pattern`. 
 *    Example: ~/pattern/
 * 
 * 2. The Find Operator (=~): 
 *    Creates a `java.util.regex.Matcher`. In a boolean context, it acts like 
 *    Matcher.find(), returning true if there is at least one partial match.
 *    Example: "text" =~ /pattern/
 * 
 * 3. The Match Operator (==~): 
 *    Evaluates to a boolean, acting like Matcher.matches(). It returns true 
 *    strictly if the *entire* string matches the pattern.
 *    Example: "text" ==~ /exact_pattern/
 * 
 * The Matcher object created by `=~` also supports array-like indexing for 
 * easy access to capture groups, and strings have built-in regex replacement 
 * methods like `replaceFirst` and `replaceAll`.
 */
 
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