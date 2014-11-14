package swing;

import java.util.Scanner;

/* This class parses the recipes that the user writes and turns them into Incu-Fridge code */
public class LineParser {
	// Possible commands, any others will be considered errors
	static String[] commands = new String[] {"GO", "PWM", "FAN_ON", "FAN_OFF", "LIGHT_ON", 
			"LIGHT_OFF", "READ_DISPLAY", "SET_TEMP"};

	// Only pay attention to these words
	static String[] importantWords = new String[] {"light", "on", "off", "display", "set", "temp", "read", "fan", "go", "pwm"};
	
	public static String parseCommand(String str) {
		String parsedCommand = "";
		Scanner s = new Scanner(str);
		while (s.hasNext()) {
			String next = s.next();
			if (contains(importantWords, next) || isInt(next)) {
				if (!parsedCommand.equals("")) {
					parsedCommand += isInt(next) ? " " : "_";
				}
				parsedCommand += next.toUpperCase();
			}
		}
		s.close();
		
		if (!contains(commands, parsedCommand.split(" ")[0])) {
			return "Error";
		}
		
		return parsedCommand + ";";
	}
	
	public static boolean contains(String[] arr, String str) {
		for (String s : arr) {
			if (s.equals(str)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// Test main method
	public static void main(String[] args) {
		String command = "set the blah blah temp 30 to 40 and blah";

		System.out.println(LineParser.parseCommand(command));
	}
}