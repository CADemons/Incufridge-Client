
/* This class manages serial operations using the SerialComm class */
public class SerialConnector {
	public SerialComm main;
	public String port = "Could not find COM port.";
	
	public SerialConnector() {
		port = findPort();
		
		if (tryConnect()) {
			main = new SerialComm();
			main.initialize(port);
		}
	}
	
	public String findPort() {		
		return "Could not find COM port.";
	}
	
	public boolean tryConnect() {
		port = findPort();
		
		return port != "Could not find COM port.";
	}
}