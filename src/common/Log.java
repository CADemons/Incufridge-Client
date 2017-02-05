package common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	/** Creates a log file **/
	public static void createLog() {
		double temp = Communicator.getTemperature();
		
		DateFormat dateFormat = new SimpleDateFormat("EEEE' 'MMMM' 'd', 'yyyy' at 'h:mm:ss' 'aa");
		Date date = new Date();
		
		String output = "Created on " + dateFormat.format(date) + "\n\n";
		output += "Temperature: " + temp + "\n";
		String targetTemp = TextFileReader.readLineFromFile("lastTargetTemp.txt", 1);
		output += "Target Temperature: " + targetTemp + "\n";

		String rawLog = date.getTime() + "\n";
		rawLog += temp + "\n";
		rawLog += targetTemp + "\n";

		TextFileWriter.writeToFile("log.txt", output + "\n");
		TextFileWriter.writeToFile("rawLog.txt", rawLog);
		
		System.out.println("Created log");
		
//		uploadLogFile();
	}
	
	public static void uploadLogFile() {
		System.out.println("Uploading");
		File f = new File("log.txt");
		// Open a connection to the server
		SFTPConnection c = new SFTPConnection();
		SFTP s = c.connect(Info.username, Info.hostname, Info.password, Info.portnum);
		// Upload log.txt
		s.upload(f.getAbsolutePath(), "cademons/incu/");
		f = new File("rawLog.txt");
		// Upload rawLog.txt
		s.upload(f.getAbsolutePath(), "cademons/incu/");
		// Disconnect from the server
		c.disconnect();
		System.out.println("Uploaded");
	}
}
