package common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	private static int numLogs;
	
	/** Creates a log file **/
	public static void createLog() {
		File logDir = new File("Logs");
		if (!logDir.exists()) {
			System.out.println("Here");
			logDir.mkdir();
		}
		
		double temp = Communicator.getTemperature();
		
		DateFormat dateFormat = new SimpleDateFormat("EEEE' 'MMMM' 'd', 'yyyy' at 'h:mm:ss' 'aa");
		Date date = new Date();
		
		String output = "Log File\n";
		output += "Created on: " + dateFormat.format(date) + "\n\n";
		output += "Temperature: " + temp + "\n";

		numLogs = logDir.listFiles().length + 1;
		TextFileWriter.writeToFile("Logs/Log" + (logDir.listFiles().length + 1) + ".txt", output);
		TextFileWriter.writeToFile("Logs/NumLogs.txt", numLogs + "");
		
		System.out.println("Created log");
	}
	
	public static void uploadLogFile(File f) {
		SFTPConnection c = new SFTPConnection();
		SFTP s = c.connect(Info.username, Info.hostname, Info.password, Info.portnum);
		s.upload(f.getAbsolutePath(), "Logs/");
		s.upload("Logs/NumLogs.txt", "Logs/");
		c.disconnect();
	}
}
