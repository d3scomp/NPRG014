class UniversityContext {
    String leader = "Ms. Smith"
    
    def runInContext(code) {
//       code.delegate = this    
//        f.resolveStrategy = Closure...
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

class StudentGroup {
    String leader = "Ms. Novak"
    def request = {println """This leader: ${this.leader}
Owner leader: ${owner.leader}
Delegate leader: ${delegate.leader}
Just leader: ${leader}
        """}
    
    def initiateDiscussion() {
        new FacultyContext().runInContext request
        new UniversityContext().runInContext request        
    }
}

new StudentGroup().initiateDiscussion()
//TASK Experiment with different resolution strategies