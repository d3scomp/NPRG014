class City {
    String name
    int size
    boolean capital = false
    
    static def create(String n, int v, boolean e = false) {
        return new City(name: n, size: v, capital: e)
    }
}

println City.create("Brno", 400000).dump()
println City.create("Praha", 1300000, true).dump()

City pisek = new City(name: 'Písek', size: 25000, capital: false)
City tabor = new City(size: 35000, capital: false, name: 'Tábor')

println pisek.dump()
pisek.size = 25001
println pisek.dump()

println tabor
//TASK Provide a customized toString() method to print the name and the population
//assert 'City of Písek, population: 25001' == pisek.toString()
//assert 'Capital city of Praha, population: 1300000' == praha.toString()