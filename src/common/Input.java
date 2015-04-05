package common;

// Simple class that provides an interface with which you can access the data sent from the incufridge
public class Input {
	private static String currentInput = "";
	private static String totalInput = "";
	
	/** Returns the last line to be received **/
	public static String getInput() {
		return currentInput;
	}
	
	/** Waits for a new input to be received and returns it. Pauses the thread **/
	public static String getNextInput() {
		while (true) {
			// Check for new input until some input is received
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (Communicator.receivedInput()) {
				break;
			}
		}
		Communicator.setReceivedInput(false);
		
		return currentInput;
	}
	
	/** Set the current input for other classes to access **/
	public static void setInput(String s) {
		currentInput = s;
		addInput(s);
	}
	
	private static void addInput(String s) {
		totalInput += s;
	}
	
	/** Returns a String with all the input received since start **/
	public static String getTotalInput() {
		return totalInput;
	}
}
