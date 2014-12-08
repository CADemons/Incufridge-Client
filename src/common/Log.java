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
			logDir.mkdir();
		}
		
		double temp = Communicator.getTemperature();
		
		DateFormat dateFormat = new SimpleDateFormat("EEEE' 'MMMM' 'd', 'yyyy' at 'h:mm:ss' 'aa");
		Date date = new Date();
		
		String output = "Created on " + dateFormat.format(date) + "\n\n";
		output += "Temperature: " + temp + "\n";
		output += "Target Temperature: " + TextFileReader.readLineFromFile("targetTemp.txt", 1) + "\n";

		TextFileWriter.writeToFile("Logs/log.txt", output + "\n\n");
		
		System.out.println("Created log");
		
		uploadLogFile();
	}
	
	public static void uploadLogFile() {
		File f = new File("Logs/log.txt");
		SFTPConnection c = new SFTPConnection();
		SFTP s = c.connect(Info.username, Info.hostname, Info.password, Info.portnum);
		s.upload(f.getAbsolutePath(), "cademons/incuTest/Logs/");
		c.disconnect();
	}
}
