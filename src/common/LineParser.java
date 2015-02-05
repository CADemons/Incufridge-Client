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
	
	static String possibleUnitsRegex = "((second(s)?)|(minute(s)?)|(hour(s)?)|(day(s)?))";
	static String dateRegex = "(\\d+/\\d+/\\d+)|(today)";
	static String timeRegex = "(\\d+:\\d+)|(now)";
	
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
	}
	
	public static String parseCommand(String str) {
		str = str.toLowerCase();
		
		String parsedCommand = "";

		Scanner s = new Scanner(str);

		if (s.hasNext("every")) {
			// Gobble up the "every"
			s.next();
			String interval, units, date, time, fileToRun;
			try {
				interval = s.next();
				units = s.next();
				date = s.next();
				time = s.next();
				fileToRun = s.next();
			} catch (NoSuchElementException e) {
				s.close();
				return "Error";
			}
			s.close();
			
			if (isInt(interval) && units.matches(possibleUnitsRegex) && date.matches(dateRegex) && time.matches(timeRegex)) {
				return "schedule-" + interval + "-" + units + "-" + date + "-" + time + "-" + fileToRun;
			} else {
				return "Error";
			}

		}
		
		if (s.hasNext("createlog")) {
			s.close();
			return "log";
		}
		
		if (s.hasNext("cancel")) {
			s.close();
			return "cancel";
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
