final a = 'foo'

assert a.respondsTo('trim')
assert a.respondsTo('trim', [].toArray())
println 'trim() ' + a.respondsTo('trim')
println 'MetaMethod for trim() ' + a.metaClass.getMetaMethod('trim')
println '==========================================='
println 'getBytes ' + a.hasProperty('bytes')
println 'MetaProperty for getBytes ' + a.metaClass.getMetaProperty('bytes')
println '==========================================='

if (a.metaClass.methods.any {it.name.startsWith 'trim'}) println "'a' can be trimmed"
println '==========================================='
