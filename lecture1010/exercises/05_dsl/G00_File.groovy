File.metaClass.div = { path ->
     new File(delegate, path) 
}  

String.metaClass.div = { path ->
     new File(delegate, path) 
}  

def file = new File(".")/'test'/'hello'/'file.txt'
println file.exists()

file = "."/'test'/'hello'/'file.txt'
println file.exists()