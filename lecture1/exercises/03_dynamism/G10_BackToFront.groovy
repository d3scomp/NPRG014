String.metaClass.backToFront = {->
    delegate[-1..0]
}


println 'cimanyd si yvoorG'.backToFront()

//TASK re-define the standard trim() method to surround the original trimmed string with '*' 

//final oldTrim = String.metaClass.getMetaMethod('trim')

//assert '*core*' == '   core   '.trim()

println 'done'




















