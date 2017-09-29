class ProgrammingLanguage {
    String name
    String version
    boolean easy = true
    
    //TASK comment out the following line and see what happens
    public ProgrammingLanguage() {}
    
    public ProgrammingLanguage(String lang) {
        this.name = lang
        this.version = "1.0"
    }
    
    static def create(String n, String v, boolean e = true) {
        return new ProgrammingLanguage(name: n, version: v, easy: e)
    }
}

println ProgrammingLanguage.create("Ruby", "1.9").dump()

//TASK Use name parameters to create the instance
ProgrammingLanguage lang = new ProgrammingLanguage(name: 'Groovy', version: '2.4', easy: true)

println lang.dump()
lang.version = '2.5'

//TASK Provide a customized toString() method to print the name and the version
println lang
//assert 'A cool programming language: Groovy version 2.5' == lang.toString()