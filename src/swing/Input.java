package swing;

// Simple class that provides an interface with which you can access the data sent from the incufridge
public class Input {
	private static String currentInput = "";
	private static String totalInput = "";
	
	public static String getInput() {
		return currentInput;
	}
	
	public static void setInput(String s) {
		currentInput = s;
		addInput(s);
	}
	
	public static void addInput(String s) {
		totalInput += s;
	}
	
	public static String getTotalInput() {
		return totalInput;
	}
}
