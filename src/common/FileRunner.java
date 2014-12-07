package common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class FileRunner {
	private String myFileText;
	
	private ArrayList<ScheduledRunner> schedules = new ArrayList<ScheduledRunner>();
	
	public FileRunner(String filename) {
		myFileText = TextFileReader.readEntireFile(filename);
	}

	public void updloadAndRun() {
		// Get each line separately
		String[] lines = myFileText.split("\n");
		String[] compiled = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			// Parse each line and add it to the compiled array
			compiled[i] = LineParser.parseCommand(lines[i]).trim();
		}

		if (Communicator.isConnected()) {
			// Send the compiled code to the incufridge
			for (int i = 0; i < compiled.length; i++) {
				if (compiled[i].contains("schedule")) {
					
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
					
					// Date is formatted MM/dd/yyyy
					String[] date = parts[3].split("/");
					
					// Time is formatted hh:mm
					String[] time = parts[4].split(":");
					
					int year = Integer.parseInt(date[2]);
					int month = Integer.parseInt(date[0]);
					int day = Integer.parseInt(date[1]);
					int hourOfDay = Integer.parseInt(time[0]);
					int minute = Integer.parseInt(time[1]);
					
					// Make the calendar
					Calendar c = new GregorianCalendar();
					c.set(year, month - 1, day, hourOfDay, minute);
					System.out.println(c.getTime());
					
					// Make sure the time specified is not in the past
					if (c.getTimeInMillis() < System.currentTimeMillis()) {
						JOptionPane.showMessageDialog(null, parts[3] + " at" + parts[4] + " is in the past");
						return;
					}
					
					// Add a new scheduler
					schedules.add(new ScheduledRunner(minutes, new Date(c.getTimeInMillis()), parts[5]));
				} else if (compiled[i].equals("log")) {
					Log.createLog();
				} else if (compiled[i].equals("cancel")) {
					for (ScheduledRunner s : schedules) {
						s.cancel();
					}
				} else {
					Communicator.sendCommand(compiled[i]);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No connection");
		}
	}
}
