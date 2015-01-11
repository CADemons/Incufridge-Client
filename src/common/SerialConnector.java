package common;
import javax.swing.JOptionPane;

import processing.serial.Serial;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find port.";
	private int timeout = 1000;

	public String findPort() {
		SerialComm portFinder = new SerialComm();
		port = "Could not find port.";

		String[] portTest = Serial.list();
		for(int i = 0; i < portTest.length; i++) {
			System.out.println(portTest[i]);
			portFinder.initialize(portTest[i]);
			long startTime = System.currentTimeMillis();
			while (true) {
				if (portFinder.receivedA) {
					portFinder.close();
					return portTest[i];
				}
				if (System.currentTimeMillis() - startTime > timeout) {
					break;
				}
			}
			portFinder.close();
		}
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