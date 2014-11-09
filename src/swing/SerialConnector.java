package swing;
import javax.swing.JOptionPane;

import processing.serial.Serial;

import common.SerialComm;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find port.";

	public String findPort() {
		SerialComm portFinder = new SerialComm();
		JOptionPane.showMessageDialog(null, "Detach the IncuFridge, then click ok");
		String[] without = Serial.list();
		JOptionPane.showMessageDialog(null, "Reattach the IncuFridge, then click ok");
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
		} else {
			main = null;
		}
	}
}