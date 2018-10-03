class City {
    String name
    int size
    boolean capital = false
    
    //TASK comment out the following line and see what happens
    public City() {}
    
    static def create(String n, int v, boolean e = true) {
        return new City(name: n, size: v, capital: e)
    }
}

println City.create("Brno", 400000).dump()

//TASK Use name parameters to create the instance
City c = new City(name: 'Písek', size: 25000, capital: false)

println c.dump()
c.size = 25001
println c.dump()

println c
//TASK Provide a customized toString() method to print the name and the version
//assert 'City of Písek, population: 25001' == c.toString()
//c.capital = true
//assert 'Capital city of Písek, population: 25001' == c.toString()