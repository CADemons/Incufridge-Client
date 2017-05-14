package common;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/* This class parses the recipes that the user writes and turns them into Incu-Fridge code */
public class LineParser {
	// Possible commands, any others will be considered errors
	static String[] commands;

	// Only pay attention to these words
	static String[] importantWords;
	
	// Regex to match units
	static String possibleUnitsRegex = "((second(s)?)|(minute(s)?)|(hour(s)?)|(day(s)?))";
	// Regex to match dates (this could be either 10/4/17 or today+3)
	static String dateRegex = "(\\d+/\\d+/\\d+)|(today(\\+\\d+)?)";
	// Regex to match times (this could be either 10:00, or now+2)
	static String timeRegex = "(\\d+:\\d+)|(now(\\+\\d+)?)";
	
	public static void init(String[] commandsList) {
		// A list of all possible commands
		// Will be split into important words (words that the parser should look for)
		// All words that are parsed and not in the importantWords array will be ignored by the parser
		commands = commandsList;
		
		ArrayList<String> importantWordsList = new ArrayList<String>();
		for (int i = 0; i < commands.length; i++) {
			// Split by '_'
			String[] words = commands[i].split("_");
			for (int j = 0; j < words.length; j++) {
				// Make lowercase and add it to the list
				importantWordsList.add(words[j].toLowerCase());
			}
		}
		
		// Turn the arraylist into an array
		importantWords = importantWordsList.toArray(new String[importantWordsList.size()]);
	}
	
	// Parse a command (1 line in the recipe)
	// This function is going to be called by the filed runner when it is compiling the commands
	public static String parseCommand(String str) {
		// Make it lowercase
		str = str.toLowerCase();
		
		String parsedCommand = "";

		// Create a scanner to parse the string
		Scanner s = new Scanner(str);

		// Scheduled runner declaration
		// Syntax: every 1 minute today now ( fileToRun/command ) job1
		if (s.hasNext("every")) {
			// Gobble up the "every"
			s.next();
			String interval, units, date, time, commands, name;
			commands = "";
			try {
				// Get all the data needed for a scheduled runner
				interval = s.next();
				units = s.next();
				date = s.next();
				time = s.next();
				// Left Paren
				s.next();
				while (!s.hasNext("\\)")) {
					commands += s.next() + " ";
				}
				commands = commands.trim();
				// Right paren
				s.next();
				name = s.next();
			} catch (NoSuchElementException e) {
				s.close();
				return "Error";
			}
			s.close();
			
			if (isInt(interval) && units.matches(possibleUnitsRegex) && date.matches(dateRegex) && time.matches(timeRegex)) {
				// Append everything together and return it for the filerunner
				return "schedule-" + interval + "-" + units + "-" + date + "-" + time + "-" + commands.replaceAll(";", "\n") + "-" + name;
			} else {
				return "Error";
			}
		}
		
		// At runner declaration
		// Syntax: at today now+3 ( fileToRun/command ) job1
		if (s.hasNext("at")) {
			s.next();
			String date, time, commands;
			commands = "";
			try {
				date = s.next();
				time = s.next();
				s.next();
				while (!s.hasNext("\\)")) {
					commands += s.next() + " ";
				}
				commands = commands.trim();
				// Right paren
				s.next();
			} catch (NoSuchElementException e) {
				s.close();
				return "Error";
			}
			s.close();
			
			if (date.matches(dateRegex) && time.matches(timeRegex)) {
				// This is a bit of a hack
				// The '10 minutes' will be ignored by the filerunner because this is an at runner
				return "at-10-minutes-" + date + "-" + time + "-" + commands.replaceAll("\n", ";");
			}
		}
		
		if (s.hasNext("createlog")) {
			s.close();
			return "log";
		}
		
		if (s.hasNext("cancelall")) {
			s.close();
			return "cancelall";
		}

		if (s.hasNext("cancel")) {
			String name = "";
			try {
				s.next();
				name = s.next();
				s.close();
			} catch (NoSuchElementException e) {
				return "Error";
			}
			return "cancel-" + name;
		}

		// Compile a simple incufridge command
		// light on -> LIGHT_ON
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
	
	private static boolean contains(String[] arr, String str) {
		for (String s : arr) {
			if (s.equals(str)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
