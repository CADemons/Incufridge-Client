

public class LineParser {
	String[] commands = new String[] {"GO", "PWM", "FAN_ON", "FAN_OFF", "LIGHT_ON", 
			"LIGHT_OFF", "READ_DISPLAY", "SET_TEMP"};

	String[] wordsToIgnore = new String[] {"the", "degrees", "celsius", "farenheit", "in", "about", "turn", "and"};	
	String[] importantWords = new String[] {"light", "on", "off", "display", "set", "temp", "read", "fan", "go", "pwm"};
	
	public String parseName(String s) {
		String parsed = "";

		s = s.trim();

		s = s.toUpperCase();

		s = s.replace(' ', '_');

		parsed = s;

		for (int i = 0; i < commands.length; i++) {
			if (parsed.equals(commands[i])) {
				return parsed;
			}
		}

		return "ERROR: " + parsed;
	}

	public String parseArg(String s) {
		String parsed = "";

		s = s.trim();

		parsed = s;

		return parsed;
	}
	
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}

	public String parseCommand(String s) {
		String parsed = "";

		String[] spaceSplit = s.split(" ");

		for (int i = 0; i < spaceSplit.length; i++) {
			for (int b = 0; b < wordsToIgnore.length; b++) {
				if (spaceSplit[i].equals(wordsToIgnore[b])) {
					spaceSplit[i] = "";
				}
			}
						
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

		s = combine(spaceSplit, " ");
		
		String[] parts = s.split("-");
		if (!(parts.length > 1)) {
			parts = s.split("to");
		}

		parsed = parseName(parts[0]);

		for (int i = 1; i < parts.length; i++) {
			parsed += " " + parseArg(parts[i]);
		}

		parsed += ";";

		return parsed;
	}

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

	public static void main(String[] args) {
		LineParser lp = new LineParser();
		String command = "set temp - 30";

		System.out.println(lp.parseCommand(command));

	}
}
