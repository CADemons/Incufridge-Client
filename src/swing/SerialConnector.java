package swing;
import java.util.Scanner;

import processing.serial.Serial;

import common.SerialComm;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find COM port.";

	public SerialConnector() {
		//port = findPort();

		//tryConnect();
	}

	public String findPort() {
		System.out.println("Called");
		SerialComm portFinder = new SerialComm();
		Scanner s = new Scanner(System.in);
		System.out.println("Detach the incufridge");
		s.next();
		String[] without = Serial.list();
		System.out.println("Reattach the indufridge");
		s.next();
		String[] with = Serial.list();
		port = portFinder.getPort(without, with);
		System.out.println("Port: " + port);
		s.close();

		return port;
	}

	public boolean canConnect() {
		port = findPort();

		return !port.equals("Could not find COM port.");
	}

	public void tryConnect() {
		if (canConnect()) {
			main = new SerialComm();
			main.initialize(port);
		}
	}
}