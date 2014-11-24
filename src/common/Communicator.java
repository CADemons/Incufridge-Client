package common;

import javax.swing.JOptionPane;

public class Communicator {
	private static SerialConnector serial;
	
	public static void setSerial(SerialConnector s) {
		serial = s;
	}
	
	public static void sendCommand(String comm) {
		if (serial.main != null) {
			serial.main.writeBytes(comm.getBytes());
			System.out.println("Send command: " + comm);
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
		return serial.main.receivedInput;
	}
	
	public static void setReceivedInput(boolean b) {
		serial.main.receivedInput = b;
	}
}
