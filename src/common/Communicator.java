package common;

import javax.swing.JOptionPane;

// Provides easy means to communicate with the Arduino
public class Communicator {
	private static SerialConnector serial;
	
	public static void setSerial(SerialConnector s) {
		serial = s;
	}
	
	public static void sendCommand(String comm) {
		if (isConnected()) {
			// When the "set temp X" command is run, record it in file
			if (comm.contains("SET_TEMP")) {
				// Write the last set target temperature to file
				String targetTemp = comm.split(" ")[1];
				TextFileWriter.deleteFile("lastTargetTemp.txt");
				TextFileWriter.writeToFile("lastTargetTemp.txt", targetTemp.substring(0, targetTemp.length() - 1));
			}
			
			serial.main.writeBytes(comm.getBytes());
			System.out.println("Sent command: " + comm);
		} else {
			JOptionPane.showMessageDialog(null, "No connection to transmit data");
		}
	}
	
	public static double getTemperature() {
		// Send the read display command and then get the next input
		sendCommand("READ_DISPLAY;");
		double temp = -1;
		try {
			temp = Double.parseDouble(Input.getNextInput());
		} catch (NumberFormatException e) {
			// If the input is not a double then there is an error
			JOptionPane.showMessageDialog(null, "There was an error with the temperature received. Please try again");
		}

		return temp;
	}
	
	public static boolean isConnected() {
		return serial.main != null;
	}
	
	public static boolean receivedInput() {
		// Return serial.main.receivedInput if serial.main is not equal to null, else return false
		return serial.main != null ? serial.main.receivedInput : false;
	}
	
	// Has new input been received? This will be set by the Input class
	public static void setReceivedInput(boolean b) {
		if (serial.main != null) {
			serial.main.receivedInput = b;
		}
	}
}
