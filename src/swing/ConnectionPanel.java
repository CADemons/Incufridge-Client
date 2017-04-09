package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import common.SerialConnector;

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
		connectionStatus.setLineWrap(true);
		retryButton = new JButton("Retry Connection");

		// Attempt to connect
		connect(false);

		AL AL = new AL();

		retryButton.addActionListener(AL);

		// Add the components to the panel
		this.add(connectionStatus);
		this.add(retryButton);
	}

	public boolean isConnected() {
		// The connection was successful if the serial.main object is not null
		return serial.main != null;
	}

	public void connect(boolean isRetry) {
		// Attempt to connect
		int code = 0;
		// if (!isRetry) {
		// 	int messageType = JOptionPane.QUESTION_MESSAGE;
		// 	String[] options = {"Yes", "No"};
		// 	code = JOptionPane.showOptionDialog(null, 
		// 			"Are you connected to the Incufridge?", 
		// 			"Connected?", 0, messageType, 
		// 			null, options, "Yes");
		// }

		if (code == 0) {
			serial.tryConnect();
		}

		// Update the status based on whether or not the connection succeeded
		if (isConnected()) {
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
				connect(true);
			}
		}
	}
}
