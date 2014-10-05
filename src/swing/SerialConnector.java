package swing;
import common.SerialComm;


/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find COM port.";
	
	public SerialConnector() {
		port = findPort();
		
		tryConnect();
	}
	
	public String findPort() {
		return "Could not find COM port.";
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