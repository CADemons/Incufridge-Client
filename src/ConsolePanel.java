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


@SuppressWarnings("serial")
public class ConsolePanel extends JPanel {
	public JTextArea console;
	private JScrollPane scroll;
	private JTextField inputField;
	private JButton sendButton;
	private JButton newWindowButton;
	private ConsoleWriter consoleWriter;
	private Serial serial;
	
	public ConsolePanel(ConsoleWriter consoleWriter, Serial serial) {
		this.consoleWriter = consoleWriter;
		this.serial = serial;
		
		console = new JTextArea(20, 35);
		
		console.setEditable(false);
		
		scroll = new JScrollPane(console);
		inputField = new JTextField("Commands", 20);
		sendButton = new JButton("Send");
		newWindowButton = new JButton("Open in new window");
		
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		AL AL = new AL();
		
		sendButton.addActionListener(AL);
		inputField.addActionListener(AL);
		newWindowButton.addActionListener(AL);
		
		this.add(inputField);
		this.add(sendButton);
		this.add(scroll);
		this.add(newWindowButton);
		
		System.setOut(new PrintStream(consoleWriter));
		consoleWriter.addCp(this);
	}
	
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
		
		private void sendCommand() {
			if (serial.main != null) {
				serial.main.writeBytes(inputField.getText().getBytes());
				System.out.println("Sent command: " + inputField.getText());
			} else {
				System.out.println("No connection to transmit data");
			}
			
			inputField.setText("");
		}
	}
}
