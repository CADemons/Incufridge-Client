package common;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

// Check the server every 10 minutes to see if new files have been added
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
				FileRunner.cancelAll();
				removePrograms();
				TextFileWriter.deleteFile("rawLog.txt");
				TextFileWriter.deleteFile("log.txt");
				System.out.println("Downloading new files");
				s.downloadProgramsDir();
				s.createFile();
				// If the files that were downloaded contain a main file
				// Run the main file
				if (new File("Programs/main").exists()) {
					FileRunner.uploadAndRun("Programs/main");
				}
			}
			c.disconnect();
		}

		// Remove the programs directory
		public void removePrograms() {
			File dir = new File("Programs");
			for(File file: dir.listFiles()) file.delete();
		}
	}
}
