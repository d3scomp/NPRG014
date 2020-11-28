class UniversityContext {
    String leader = "Ms. Smith"
    
    def runInContext(code) {
       code.delegate = this    
       code.resolveStrategy = Closure.DELEGATE_FIRST
       code.call()
    }
}

class FacultyContext {
    String leader = "Mr. Brown"
    def runInContext(code) {
       code.delegate = this
       code.call()
    }
}

class CabinetContext {
    String leader = "Ms. Gray"
    def request = {println """\
                      This leader: ${this.leader}
                      Owner leader: ${owner.leader}
                      Delegate leader: ${delegate.leader}
                      Just leader: -> ${leader} <-
                           """}

    def runInContext(code) {
       code.delegate = this
       code.call()
    }                       
    
    def runRequest() {
        runInContext(request)
        new FacultyContext().runInContext(request)
        new UniversityContext().runInContext(request)        
    }
}

new CabinetContext().runRequest()

//TASK Experiment with different resolution strategies