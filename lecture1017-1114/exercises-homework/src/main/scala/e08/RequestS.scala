package e08

class RequestS(val method: String) {
  def execute() {
    if (method eq "POST") {
      println("POST")
    } else {
      println("Unknown")
    }
  }
}
