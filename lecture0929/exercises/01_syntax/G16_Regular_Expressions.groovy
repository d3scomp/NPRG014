final datePattern = /th |st |nd |rd | |:/
println '21st Feb 2012 11:58:30 a.m.'.split(datePattern)
println '22nd Feb 2012'.split(datePattern)
println '28th Feb 2012'.split(datePattern)
