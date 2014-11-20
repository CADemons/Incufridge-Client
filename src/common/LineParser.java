package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/* This class parses the recipes that the user writes and turns them into Incu-Fridge code */
public class LineParser {
	// Possible commands, any others will be considered errors
	static String[] commands;

	// Only pay attention to these words
	static String[] importantWords;
	
	static String possibleUnitsRegex = "(second(s)?) || (minute(s)?) || (hour(s)?) || (day(s)?)";
	static String dateRegex = "\\d/\\d/\\d";
	static String timeRegex = "\\d:\\d";
	
	public static void init(String[] commandsList) {
		commands = commandsList;
		
		ArrayList<String> importantWordsList = new ArrayList<String>();
		for (int i = 0; i < commands.length; i++) {
			String[] words = commands[i].split("_");
			for (int j = 0; j < words.length; j++) {
				importantWordsList.add(words[j].toLowerCase());
			}
		}
		
		importantWords = importantWordsList.toArray(new String[importantWordsList.size()]);
		System.out.println(Arrays.toString(importantWords));
	}
	
	public static String parseCommand(String str) {
		str = str.toLowerCase();
		
		
		String parsedCommand = "";


		Scanner s = new Scanner(str);

		if (s.hasNext("every")) {
			// Gobble up the "every"
			s.next();
			String interval = s.next();
			String units = s.next();
			String date = s.next();
			String time = s.next();
			String fileToRun = s.next();
			s.close();
			
			if (isInt(interval) && units.matches(possibleUnitsRegex) && date.matches(dateRegex) && time.matches(timeRegex)) {
				return "schedule-" + interval + "-" + units + "-" + date + "-" + time + "-" + fileToRun;
			} else {
				return "Error";
			}

		}

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
		LineParser.init(new String[] {"GO", "PWM", "FAN_ON", "FAN_OFF", "LIGHT_ON", 
			"LIGHT_OFF", "READ_DISPLAY", "SET_TEMP"});
		String command = "set the blah blah TEMP 30 to 40 and blah";
		
		System.out.println(LineParser.parseCommand(command));
	}
}