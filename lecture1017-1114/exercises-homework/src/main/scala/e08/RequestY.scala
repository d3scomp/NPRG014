package e08

class RequestY(val method: Symbol) {
  def execute() {
    if (method eq 'POST) {
      println("POST")
    } else {
      println("Unknown")
    }
  }
}
