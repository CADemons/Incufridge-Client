package common;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AtRunner {
	Timer timer;
	
	public AtRunner(Date time, String fileToRun) {
		timer = new Timer();
		timer.schedule(new RunTask(fileToRun), time);
		System.out.println("Here");
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
			System.out.println("Upload and run Programs/" + myFileName);
			FileRunner.uploadAndRun("Programs/" + myFileName);
		}
	}
}
