import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class ConsolePanel extends JPanel {
	public JTextArea console;
	private JScrollPane scroll;
	private JTextField inputField;
	private JButton sendButton;
	private SerialComm main;
	private String port = "Could not find COM port.";
	
	public ConsolePanel() {
		console = new JTextArea(20, 35);
		
		console.setEditable(false);
		
		scroll = new JScrollPane(console);
		inputField = new JTextField("Commands", 30);
		sendButton = new JButton("Send");
		
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		AL AL = new AL();
		
		sendButton.addActionListener(AL);
		inputField.addActionListener(AL);
		
		this.add(inputField);
		this.add(sendButton);
		this.add(scroll);
		
		ConsoleWriter.origout = System.out;
		System.setOut(new PrintStream(new ConsoleWriter(this)));
		
		if (port != "Could not find COM port.") {
			main = new SerialComm();
			main.initialize(port);
		}
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == sendButton) {
				sendCommand();
			}
			
			if (e.getSource() == inputField) {
				sendCommand();
			}
		}
		
		private void sendCommand() {
			if (main != null) {
				main.writeBytes(inputField.getText().getBytes());
				System.out.println("Sent command: " + inputField.getText());
			} else {
				System.out.println("No connection to transmit data");
			}
			
			inputField.setText("");
		}
	}
}
