package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/* This class contains the code for the JPanel containing information about connection to the 
 * Incu-Fridge */
@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel {
	
	// Used to perform serial operations
	private SerialConnector serial;
	// Textarea used to give feedback to the user about the connection
	private JTextArea connectionStatus;
	// Allows the user to try to reconnect if the connection failed
	private JButton retryButton;
	
	// The main SerialConnector object is passed in the constructor by the GUI
	public ConnectionPanel(SerialConnector serial) {
		this.serial = serial;
		
		connectionStatus = new JTextArea(5, 30);
		connectionStatus.setEditable(false);
		retryButton = new JButton("Retry Connection");
		
		// Attempt to connect
		connect();
		
		AL AL = new AL();
		
		retryButton.addActionListener(AL);
		
		// Add the components to the panel
		this.add(connectionStatus);
		this.add(retryButton);
	}
	
	public boolean getConnection() {
		// The connection was successful if the serial.main object is not null
		return serial.main != null;
	}
	
	public void connect() {
		// Attempt to connect
		serial.tryConnect();
		
		// Update the status based on whether or not the connection failed
		if (getConnection()) {
			connectionStatus.setText("Connected to Incu-Fridge on port " + serial.port);
		} else {
			connectionStatus.setText("Was not able to connect to Incu-Fridge");
		}
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == retryButton) {
				System.out.println("Retry button pressed");
				connect();
			}
		}
	}
}
