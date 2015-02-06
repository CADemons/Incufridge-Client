package common;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ServerChecker {
	Timer timer;

	public ServerChecker() {
		timer = new Timer();

		timer.scheduleAtFixedRate(new CheckTask(), new Date(), TimeUnit.MINUTES.toMillis(10));
	}

	class CheckTask extends TimerTask {
		public void run() {
			System.out.println("Checking");

			SFTPConnection c = new SFTPConnection();
			SFTP s = c.connect(Info.username, Info.hostname, Info.password, Info.portnum);
			if (!s.fileExists()) {
				s.downloadProgramsDir();
				s.createFile();
				if (new File("Programs/main").exists()) {
					FileRunner.uploadAndRun("Programs/main");
				}
			}
			c.disconnect();
		}
	}
}
