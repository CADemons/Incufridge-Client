package common;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ScheduledRunner {
	Timer timer;
	String name;
	
	// Run a file every x minutes starting at firstTime
	public ScheduledRunner(String name, long minutes, Date firstTime, String fileToRun) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new RunTask(fileToRun), firstTime, TimeUnit.MINUTES.toMillis(minutes));
		System.out.println("Here");
		this.name = name;
	}
	
	public void cancel() {
		timer.cancel();
	}
	
	class RunTask extends TimerTask {
		String myFileName;
		public RunTask(String fileToRun) {
			myFileName = fileToRun;
		}
		public void run() {
			System.out.println("Here");
			FileRunner.uploadAndRun("Programs/" + myFileName);
		}
	}
}
