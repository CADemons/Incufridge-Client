package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import common.ConsoleWriter;
import common.LineParser;
import common.SerialConnector;

/* This class contains the code for the JPanel used to display the console */
@SuppressWarnings("serial")
public class ConsolePanel extends JPanel {
	public JTextArea console;
	private JScrollPane scroll;
	private JTextField inputField;
	private JButton sendButton;
	private JButton newWindowButton;
	private ConsoleWriter consoleWriter;
	private SerialConnector serial;
	
	// Pass in the GUI's consoleWriter and main SerialConnector
	public ConsolePanel(ConsoleWriter consoleWriter, SerialConnector serial) {
		this.consoleWriter = consoleWriter;
		this.serial = serial;
		
		console = new JTextArea(20, 35);
		
		console.setEditable(false);
		
		scroll = new JScrollPane(console);
		inputField = new JTextField("Commands", 20);
		sendButton = new JButton("Send");
		newWindowButton = new JButton("Open in new window");
		
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		AL AL = new AL();
		
		sendButton.addActionListener(AL);
		inputField.addActionListener(AL);
		newWindowButton.addActionListener(AL);
		
		// Add the components to the panel
		this.add(inputField);
		this.add(sendButton);
		this.add(scroll);
		this.add(newWindowButton);
		
		// Create the consoleWriter and add this panel to its list of panels to update
		System.setOut(new PrintStream(consoleWriter));
		consoleWriter.addCp(this);
	}
	
	// Make a new ConsolePanel in a new window
	public void addInNewWindow() {
		JFrame frame = new JFrame("Incu-Fridge - Console");
		frame.setSize(512, 512);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.add(new ConsolePanel(consoleWriter, serial));
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
			
			if (e.getSource() == newWindowButton) {
				addInNewWindow();
			}
		}
		
		// Send the command to the incu-fridge
		private void sendCommand() {
			if (serial.main != null) {
				serial.main.writeBytes(LineParser.parseCommand(inputField.getText()).getBytes());
				System.out.println("Sent command: " + LineParser.parseCommand(inputField.getText()));
			} else {
				System.out.println("No connection to transmit data");
			}
			
			inputField.setText("");
		}
	}
}
