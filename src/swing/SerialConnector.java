package swing;
import javax.swing.JOptionPane;

import processing.serial.Serial;

import common.SerialComm;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find port.";

	public SerialConnector() {
		//port = findPort();

		//tryConnect();
	}

	public String findPort() {
		System.out.println("Called");
		
		SerialComm portFinder = new SerialComm();
		JOptionPane.showMessageDialog(null, "Detach the IncuFridge, then click ok");
		String[] without = Serial.list();
		System.out.println("Here");
		JOptionPane.showMessageDialog(null, "Reattach the IncuFridge, then click ok");
		String[] with = Serial.list();
		System.out.println("Here");
		port = portFinder.getPort(without, with);
		System.out.println("Port: " + port);

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
		}
	}
}