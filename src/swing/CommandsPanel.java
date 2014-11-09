package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class CommandsPanel extends JPanel {
	
	private JLabel fileLabel;
	private JTextField filenameField;
	private JButton saveButton;
	private JTextArea commandsText;
	private JScrollPane scroll;
	private JButton loadButton;
	private JButton uploadButton;
	private JButton checkButton;
	private SerialConnector serial;
	
	public CommandsPanel(SerialConnector serial) {
		commandsText = new JTextArea(20, 35);
		filenameField = new JTextField(30);
		fileLabel = new JLabel("File Name: ");
		
		this.serial = serial;
		
		scroll = new JScrollPane(commandsText);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		saveButton = new JButton("Save Recipe");
		loadButton = new JButton("Load Recipe");
		uploadButton = new JButton("Upload Recipe");
		checkButton = new JButton("Check for Errors");
		
		commandsText.setText("");
		commandsText.setEditable(true);
		
		AL AL = new AL();
		saveButton.addActionListener(AL);
		loadButton.addActionListener(AL);
		uploadButton.addActionListener(AL);
		checkButton.addActionListener(AL);
		
		this.add(fileLabel);
		this.add(filenameField);
		this.add(scroll);
		this.add(saveButton);
		this.add(loadButton);
		this.add(uploadButton);
		this.add(checkButton);
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == saveButton) {
				saveRecipe();
			}
			
			if (e.getSource() == loadButton) {
				commandsText.setText(TextFileReader.readEntireFile(filenameField.getText()));
			}
			
			if (e.getSource() == uploadButton) {
				String[] lines = commandsText.getText().split("\n");
				String[] compiled = new String[lines.length];
				for (int i = 0; i < lines.length; i++) {
					compiled[i] = LineParser.parseCommand(lines[i]).trim();
				}
				
				if (serial.main != null) {
					for (int i = 0; i < compiled.length; i++) {
						serial.main.writeBytes(compiled[i].getBytes());
						System.out.println("Sent command: " + compiled[i]);
					}
				} else {
					System.out.println("No connection to transmit data");
				}
			}
			
			if (e.getSource() == checkButton) {
				checkError();
			}
		}
		
		private void saveRecipe() {
			File file = new File(filenameField.getText());
			file.delete();

			TextFileWriter.writeToFile(filenameField.getText(), commandsText.getText());
		}
		
		private void checkError() {
			String[] lines = commandsText.getText().split("\n");
			for (int i = 0; i < lines.length; i++) {
				if (LineParser.parseCommand(lines[i]).trim().equals("Error")) {
					JOptionPane.showMessageDialog(null, "You have an error on line " + (i + 1));
				}
			}
		}
	}
}
