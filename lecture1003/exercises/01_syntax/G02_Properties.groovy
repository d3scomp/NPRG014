class ProgrammingLanguage {
    String name
    String version
    boolean easy = true

    ProgrammingLanguage() {}

    ProgrammingLanguage(String name, String version, boolean easy) {
        this.name = name
        this.version = version
        this.easy = easy
    }
}

//TASK Make the easy constructor parameter true by default
//TASK Use name parameters to create the instance
ProgrammingLanguage lang = new ProgrammingLanguage('Groovy', '1.8', true)

println lang.dump()
lang.version = '2.0'

//TASK Provide a customized toString() method to print the name and the version
println lang
//assert 'A cool programming language: Groovy version 2.0' == lang.toString()