package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.Communicator;
import common.Log;


@SuppressWarnings("serial")
public class DataDisplayPanel extends JPanel {
	private JLabel tempLabel;
	private JButton updateButton;
	private JButton createLogButton;
	
	public DataDisplayPanel() {
		tempLabel = new JLabel("Temperature: ");
		updateButton = new JButton("Update");
		createLogButton = new JButton("Create log");
		
		AL AL = new AL();
		updateButton.addActionListener(AL);
		createLogButton.addActionListener(AL);
		
		this.add(tempLabel);
		this.add(updateButton);
		this.add(createLogButton);
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == updateButton) {
				if (Communicator.isConnected()) {
					tempLabel.setText("Temperature: " + Communicator.getTemperature());
				} else {
					JOptionPane.showMessageDialog(null, "No connection");
				}
			}
			
			if (e.getSource() == createLogButton) {
				if (Communicator.isConnected()) {
					Log.createLog();
				} else {
					JOptionPane.showMessageDialog(null, "No connection");
				}
			}
		}
		
	}
}
