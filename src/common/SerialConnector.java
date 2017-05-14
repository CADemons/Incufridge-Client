package common;
import javax.swing.JOptionPane;

import processing.serial.Serial;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find port.";
	private int timeout = 1000;

	// Find the port that the arduino is connected to
	// This takes some time
	public String findPort() {
		if (main == null) {
			SerialComm portFinder = new SerialComm();
			port = "Could not find port.";

			// Get the ports that exist
			String[] portTest = Serial.list();
			// Go through each port
			for(int i = 0; i < portTest.length; i++) {
				System.out.println(portTest[i]);
				portFinder.initialize(portTest[i]);
				long startTime = System.currentTimeMillis();
				// The arduino should send 'A' when we initially connect so we wait a timout length (1000 ms)
				// for an 'A' to be read. If no 'A' arrives then that port was the wrong port and we look
				// for the next one.
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
		}
		return port;
	}

	// Is there a valid port that we can use?
	public boolean canConnect() {
		port = findPort();

		return !port.equals("Could not find port.");
	}

	// Try to connect
	public void tryConnect() {
		if (canConnect()) {
			main = new SerialComm();
			main.initialize(port);
			// JOptionPane.showMessageDialog(null, "Connected to Incu-fridge on port: " + port);
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
