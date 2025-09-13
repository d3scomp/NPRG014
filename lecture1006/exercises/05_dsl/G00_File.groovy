File.metaClass.div = { path ->
     new File(delegate, path) 
}  

def file = new File(".")/'test'/'hello'/'file.txt'
println file.exists()



String.metaClass.div = { path ->
     new File(delegate, path) 
}  

file = "."/'test'/'hello'/'file.txt'
println file.exists()



//TASK Allow for omiting apostrophes for plain file names
//file = "."/test/hello/'file.txt'
//println file.exists()