package common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
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

		TextFileWriter.writeToFile("Logs/Log" + (logDir.listFiles().length + 1) + ".txt", output);
		
		System.out.println("Created log");
	}
	
	public static void uploadLogFile(File f) {
		SFTPConnection c = new SFTPConnection();
		SFTP s = c.connect("", "", "", 22);
		s.upload(f.getAbsolutePath(), "Logs/");
	}
}
