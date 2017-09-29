final capitals = [
        'cz': 'Prague',
        'ch': 'Bern',
        'fr': 'Paris',
        'uk': 'London',
        'de': 'Berlin',
        'at': 'Vienna',
        'sk': 'Bratislava',
        'it': 'Roma',
        'se': 'Stockholm',
        'es': 'Madrid',
]
assert capitals.size() == 10

//TASK Add Poland and Ireland to the map
//assert capitals.size() == 12

println "All countries: ${capitals.keySet()}"
println "The capital of CZ: ${capitals['cz']}"

//TASK Print in upper case the names of all capitals of countries, the name of which starts with 's'
//assert ['BRATISLAVA', 'STOCKHOLM'] == capitals...


final cities = capitals.values()
assert ['Prague', 'Paris'] == cities.findAll {it.startsWith 'P'}

println 'done'