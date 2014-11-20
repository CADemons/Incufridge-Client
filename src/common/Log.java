package common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	/** Creates a log file **/
	public static void createLog(SerialConnector serial) {
		File logDir = new File("Logs");
		if (!logDir.exists()) {
			System.out.println("Here");
			logDir.mkdir();
		}
		
		sendCommand(serial, "READ_DISPLAY");
		String temp = Input.getInput();
		
		DateFormat dateFormat = new SimpleDateFormat("EEEE' 'MMMM' 'd', 'yyyy' at 'h:mm' 'aa");
		Date date = new Date();
		
		String output = "Log File\n";
		output += "Created on: " + dateFormat.format(date) + "\n\n";
		output += "Temperature: " + temp + "\n";

		TextFileWriter.writeToFile("Logs/Log" + (logDir.listFiles().length + 1), output);
	}
	
	public static void uploadLogFile(File f) {
	}

	// Sends a command to the incu-fridge
	private static void sendCommand(SerialConnector serial, String comm) {
		if (serial.main != null) {
			serial.main.writeBytes(comm.getBytes());
		} else {
			System.out.println("No connection to transmit data");
		}
	}
}
