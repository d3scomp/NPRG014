String.metaClass.backToFront = {->
    delegate[-1..0]
}

println 'cimanyd si yvoorG'.backToFront()



//TASK define a starTrim() method to surround the original trimmed string with '*' 

def s = '   core   '
//assert '*core*' == s.starTrim()

println 'done'


















