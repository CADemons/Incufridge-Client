import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel {
	
	private Serial serial;
	private JTextArea connectionStatus;
	private JButton retryButton;
	
	public ConnectionPanel(Serial serial) {
		this.serial = serial;
		
		connectionStatus = new JTextArea(5, 30);
		connectionStatus.setEditable(false);
		retryButton = new JButton("Retry Connection");
		
		if (getConnection()) {
			connectionStatus.setText("Connected to Incu-Fridge on port " + serial.port);
		} else {
			connectionStatus.setText("Was not able to connect to Incu-Fridge");
		}
		
		AL AL = new AL();
		
		retryButton.addActionListener(AL);
		
		this.add(connectionStatus);
		this.add(retryButton);
	}
	
	public boolean getConnection() {
		return serial.main != null;
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == retryButton) {
				
			}
		}
	}
}
