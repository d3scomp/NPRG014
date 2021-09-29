def (day, month, year) = '28. 2. 2012'.split('. ')
//def (day, month, year) = '28. 2. 2012'.split('\\.')*.trim()
assert day == '28'
assert month == '2'
assert year == '2012'

println([day, month, year].join('. '))

//TASK Concatenate upper-case variants of these words with "spaces" between them using the join() method.
//assert "HELLO GROOVY HOW ARE YOU?" == ["Hello", "Groovy", "how", "are", "you?"]

println 'ok'