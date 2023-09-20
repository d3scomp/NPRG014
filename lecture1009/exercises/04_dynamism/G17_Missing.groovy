enum FRIENDS {
    Joe, Dave, Alice

    String toString() {
        "my dear friend ${super.toString()}"
    }
}

import static FRIENDS.Alice



hi Alice






def methodMissing(String name, args) {
    println "$name ${args.join(', ')}"
}


