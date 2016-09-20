def (day, month, year) = '28. 2. 2012'.split('. ')
assert day == '28'
assert month == '2'
assert year == '2012'

println([day, month, year].join('. '))

println 'ok'