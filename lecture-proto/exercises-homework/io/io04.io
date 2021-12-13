m := message(x println ; (x + 1) * 2)

m println                                         // x println ; (x +(1)) *(2)

m name println                                    // x
m arguments println                               // list()
m next name println                               // println
m next arguments println                          // list()
m next next name println                          // ;
m next next arguments println                     // list()
m next next next name println                     // 
m next next next arguments println                // list(x +(1))
m next next next arguments first name println            // x
m next next next arguments first arguments println       // list()
m next next next arguments first next name println       // +
m next next next arguments first next arguments println  // list(1)
m next next next next name println                // *
m next next next next arguments println           // list(2)
m next next next next arguments first name println       // 2
m next next next next arguments first arguments println  // list()


tgt := Object clone
tgt x := 2
m doInContext(tgt) println     // 2 \n 6

