package E10;

public class InteropJ {

	public static void main(String[] args) {

		Greeter greeter = Hello.getDefaultGreeter();
		
		greeter.saySomething(5);
		greeter.saySomethingOnce();
	}

}
