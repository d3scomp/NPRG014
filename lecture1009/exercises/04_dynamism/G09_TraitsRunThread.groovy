trait SafeStarter {
    private boolean started = false
    public void run() {
        if (!started) this.start()
        started = true
    }
}

println "Main thread: ${Thread.currentThread()}"
def t = new Thread({println "Running in ${Thread.currentThread()}"}).withTraits SafeStarter
t.run()  //This is normally a mistake, start() should be called on a thread to start running asynchronously
