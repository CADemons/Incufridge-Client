
/* This class parses the recipes that the user writes and turns them into Incu-Fridge code */
public class LineParser {
	String[] commands = new String[] {"GO", "PWM", "FAN_ON", "FAN_OFF", "LIGHT_ON", 
			"LIGHT_OFF", "READ_DISPLAY", "SET_TEMP"};

	// Ignore these words
	String[] wordsToIgnore = new String[] {"the", "degrees", "celsius", "farenheit", "in", "about", "turn", "and"};	
	// Unused for now
	String[] importantWords = new String[] {"light", "on", "off", "display", "set", "temp", "read", "fan", "go", "pwm"};
	
	// Parse the command without the arguments
	public String parseName(String s) {
		String parsed = "";

		// Trim the command
		s = s.trim();

		// Make it uppercase
		s = s.toUpperCase();

		// Replace spaces in commands with '_'
		s = s.replace(' ', '_');

		parsed = s;

		// Check if the parsed command is a valid command
		for (int i = 0; i < commands.length; i++) {
			if (parsed.equals(commands[i])) {
				return parsed;
			}
		}

		return "ERROR: " + parsed;
	}

	// Parse the argument without the command
	public String parseArg(String s) {
		String parsed = "";

		// Trim the argument
		s = s.trim();

		parsed = s;

		return parsed;
	}
	
	// Check if a string is an integer
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}

	// Parse a command and argument
	public String parseCommand(String s) {
		String parsed = "";

		String[] spaceSplit = s.split(" ");

		// Get rid of all words to ignore
		for (int i = 0; i < spaceSplit.length; i++) {
			for (int b = 0; b < wordsToIgnore.length; b++) {
				if (spaceSplit[i].equals(wordsToIgnore[b])) {
					spaceSplit[i] = "";
				}
			}
						
			// Turn temperature into temp
			if (spaceSplit[i].equals("temperature")) {
				spaceSplit[i] = "temp";
			}
			
		}
		
		/*for (int a = 0; a < spaceSplit.length; a++) {
			if (spaceSplit[a].equals("temperature")) {
				spaceSplit[a] = "temp";
			}
			
			for (int b = 0; b < importantWords.length; b++) {
				System.out.println(spaceSplit[a] + ", " + importantWords[b]);
				
				if (!spaceSplit[a].equals(importantWords[b])) {
					if (!isInteger(spaceSplit[a])) {
						spaceSplit[a] = "";
					}
				}
			}
		}*/

		// Combine everything back up
		s = combine(spaceSplit, " ");
		
		//Split the arguments from the command name
		// User can use '-' or "to"
		String[] parts = s.split("-");
		// If splitting with the dash did not do anything split with "to"
		if (!(parts.length > 1)) {
			parts = s.split("to");
		}

		// Parse the command name
		parsed = parseName(parts[0]);

		// Parse all the arguments
		for (int i = 1; i < parts.length; i++) {
			parsed += " " + parseArg(parts[i]);
		}

		// Add the semicolon at the end of the line
		parsed += ";";

		return parsed;
	}

	// Combine two strings together
	public String combine(String[] s, String glue) {
		int k = s.length;
		if (k == 0) {
			return null;
		}
		StringBuilder out = new StringBuilder();
		out.append(s[0]);
		for (int x = 1; x < k; x++) {
			if (s[x] != "") {
				out.append(glue).append(s[x]);
			}
		}
		return out.toString();
	}

	// Test main method
	public static void main(String[] args) {
		LineParser lp = new LineParser();
		String command = "set temp - 30";

		System.out.println(lp.parseCommand(command));

	}
}
