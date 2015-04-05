package common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class FileRunner {
	private static ArrayList<ScheduledRunner> schedules = new ArrayList<ScheduledRunner>();

	public static void uploadAndRun(String filename) {
		String fileText = TextFileReader.readEntireFile(filename);
		// Get each line separately
		String[] lines = fileText.split("\n");
		String[] compiled = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			// Parse each line and add it to the compiled array
			compiled[i] = LineParser.parseCommand(lines[i]).trim();
		}

		if (Communicator.isConnected()) {
			// Send the compiled code to the incufridge
			for (int i = 0; i < compiled.length; i++) {
				if (compiled[i].contains("schedule") || compiled[i].contains("at")) {

					// Split everything
					String[] parts = compiled[i].split("-");

					// Get the interval, and the units
					int interval = Integer.parseInt(parts[1]);
					long minutes = interval;
					String units = parts[2];

					// Convert the interval + units to minutes
					if (units.matches("hour(s)?")) {
						minutes = TimeUnit.HOURS.toMinutes(interval);
					} else if (units.matches("day(s)?")) {
						minutes = TimeUnit.DAYS.toMinutes(interval);
					}

					int year = 0;
					int month = 0;
					int day = 0;
					Calendar now = new GregorianCalendar();
				    now.setTimeZone(TimeZone.getTimeZone("EST"));
					if (parts[3].equals("today")) {
						year = now.get(Calendar.YEAR);
						month = now.get(Calendar.MONTH) + 1;
						day = now.get(Calendar.DAY_OF_MONTH);
						// Allow syntax like today+3 which means 3 days from today
						if (parts[3].split("\\+").length > 1) {
							day += Integer.parseInt(parts[3].split("\\+")[1]);
						}
					} else {
						// Date is formatted MM/dd/yyyy
						String[] date = parts[3].split("/");

						year = Integer.parseInt(date[2]);
						month = Integer.parseInt(date[0]);
						day = Integer.parseInt(date[1]);
					}

					int hourOfDay = 0;
					int minute = 0;
					
					if (parts[4].startsWith("now")) {
						hourOfDay = now.get(Calendar.HOUR_OF_DAY);
						minute = now.get(Calendar.MINUTE);
						// Allow syntax like now+3 which means 3 minutes from now
						if (parts[4].split("\\+").length > 1) {
							minute += Integer.parseInt(parts[4].split("\\+")[1]);
						}
					} else {
						// Time is formatted hh:mm
						String[] time = parts[4].split(":");

						hourOfDay = Integer.parseInt(time[0]);
						minute = Integer.parseInt(time[1]);
					}

					// Make the calendar
					Calendar c = new GregorianCalendar();
					c.setTimeZone(TimeZone.getTimeZone("EST"));
					c.set(year, month - 1, day, hourOfDay, minute, now.get(Calendar.SECOND) + 1);
					System.out.println(c.getTime());

					// Make sure the time specified is not in the past
					if (c.getTimeInMillis() < System.currentTimeMillis()) {
						JOptionPane.showMessageDialog(null, parts[3] + " at" + parts[4] + " is in the past.");
						return;
					}

					// Add a new scheduler
					if (compiled[i].contains("schedule")) {
						schedules.add(new ScheduledRunner(parts[6], minutes, new Date(c.getTimeInMillis()), parts[5]));
					} else {
						new AtRunner(new Date(c.getTimeInMillis()), parts[5]);
					}
				} else if (compiled[i].equals("log")) {
					// Create and upload a log file
					Log.createLog();
				} else if (compiled[i].equals("cancelAll")) {
					// Cancel all current jobs. At runners and Scheduled runners will be stopped
					cancelAll();
				} else if (compiled[i].startsWith("cancel-")) {
					// Cancel a certain job (pass in the id, syntax: cancel-job1)
					// Get the name
					String name = compiled[i].split("-")[1];
					for (ScheduledRunner r : schedules) {
						// Check if the names match
						if (r.name.equals(name)) {
							r.cancel();
						}
					}
				} else {
					// Send the command to the incufridge if it is just a simple arduino command
					Communicator.sendCommand(compiled[i]);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No connection");
		}
	}

	public static void cancelAll() {
		for (ScheduledRunner s : schedules) {
			s.cancel();
		}
	}
}
