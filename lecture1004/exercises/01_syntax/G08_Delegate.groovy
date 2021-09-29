class UniversityContext {
    String leader = "Ms. Smith (the university leader)"
    
    def runInContext(code) {
       code.delegate = this    
       code.resolveStrategy = Closure.DELEGATE_FIRST
       code.call()
    }
}

class FacultyContext {
    String leader = "Mr. Brown (the faculty leader)"
    
    def runInContext(code) {
       code.delegate = this
       code.call()
    }
}

class CabinetContext {
    String leader = "Ms. Gray (the cabinet leader)"
    
    def runInContext(code) {
       code.delegate = this
       code.call()
    }                       
    
    def request = {println """\
                      This leader: ${this.leader}
                      Owner leader: ${owner.leader}
                      Delegate leader: ${delegate.leader}
                      Plain leader: -> ${leader} <-
                           """}
    
    def runRequest() {
        println 'Run in the cabinet context'
        runInContext(request)
        
        println 'Run in the faculty context'        
        new FacultyContext().runInContext(request)
        
        println 'Run in the university context'        
        new UniversityContext().runInContext(request)        
    }
}

new CabinetContext().runRequest()

//TASK Experiment with different resolution strategies