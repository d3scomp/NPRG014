class ProgrammingLanguage {
    String name
    String version
    boolean easy = true
}

//TASK Make the easy constructor parameter true by default
//TASK Use name parameters to create the instance
ProgrammingLanguage lang = new ProgrammingLanguage(name: 'Groovy', version: '1.8', easy: true)

println lang.dump()
lang.version = '2.0'

//TASK Provide a customized toString() method to print the name and the version
println lang
//assert 'A cool programming language: Groovy version 2.0' == lang.toString()