package common;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ScheduledRunner {
	Timer timer;
	String name;
	
	// Run a file every x minutes starting at firstTime
	public ScheduledRunner(String name, long minutes, Date firstTime, String commands) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new RunTask(commands), firstTime, TimeUnit.MINUTES.toMillis(minutes));
		this.name = name;
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
			FileRunner.uploadAndRun(myCommands);
		}
	}
}