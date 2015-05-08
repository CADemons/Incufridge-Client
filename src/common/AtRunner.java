package common;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// Run a file once at a certain time
public class AtRunner {
	Timer timer;
	
	// Pass in the date when to run, and the file to run
	public AtRunner(Date time, String commands) {
		timer = new Timer();
		// Schedule a new task at the time
		timer.schedule(new RunTask(commands), time);
	}
	
	public void cancel() {
		timer.cancel();
	}
	
	class RunTask extends TimerTask {
		String myCommands;
		public RunTask(String commands) {
			myCommands = commands;
		}
		public void run() {
			// Run the file
			FileRunner.uploadAndRun(myCommands);
		}
	}
}
