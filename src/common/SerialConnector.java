package common;
import javax.swing.JOptionPane;

import processing.serial.Serial;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find port.";

	public String findPort() {
		SerialComm portFinder = new SerialComm();
		JOptionPane.showMessageDialog(null, "Detach the IncuFridge, then click \"OK\"");
		String[] without = Serial.list();
		JOptionPane.showMessageDialog(null, "Reattach the IncuFridge, then click \"OK\"");
		String[] with = Serial.list();
		port = portFinder.getPort(without, with);

		return port;
	}

	public boolean canConnect() {
		port = findPort();

		return !port.equals("Could not find port.");
	}

	public void tryConnect() {
		if (canConnect()) {
			main = new SerialComm();
			main.initialize(port);
			JOptionPane.showMessageDialog(null, "Connected to Incu-fridge on port: " + port);
		} else {
			JOptionPane.showMessageDialog(null, "Unable to connect to Incu-fridge");
			main = null;
		}
	}
	
	public void close() {
		if (main != null) {
			main.close();
		}
	}
}