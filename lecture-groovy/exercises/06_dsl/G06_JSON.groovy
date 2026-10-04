def builder = new groovy.json.JsonBuilder()
def root = builder.people {
    person {
        firstName 'Guillaume'
        lastName 'Laforge'
        // Named arguments are valid values for objects too
        address(
                city: 'Paris',
                country: 'France',
                zip: 12345,
        )
        married true
        // a list of values
        conferences 'JavaOne', 'Gr8conf'
    }
}

// creates a data structure made of maps (Json object) and lists (Json array)
assert root instanceof Map

assert builder.toString() == '{"people":{"person":{"firstName":"Guillaume","lastName":"Laforge","address":{"city":"Paris","country":"France","zip":12345},"married":true,"conferences":["JavaOne","Gr8conf"]}}}'

println 'ok'