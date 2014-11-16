package swing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
/* Provides a mini text editor for the user to write and upload recipes */
public class CommandsPanel extends JPanel {

	private JLabel fileLabel;
	// The user writes the filename here
	private JTextField filenameField;
	// Save the recipe to a file
	private JButton saveButton;
	// The textArea
	private JTextPane commandsText;
	// Allows scrolling on the textArea
	private JScrollPane scroll;
	// Load a new file
	private JButton loadButton;
	// Upload the recipe to the incu fridge
	private JButton uploadButton;
	// Check the recipe for errors
	private JButton checkButton;
	// Necessary for uploading the recipe to the incu fridge
	private SerialConnector serial;
	
	private ArrayList<String> errorLines;

	public CommandsPanel(SerialConnector serial) {
		errorLines = new ArrayList<String>();
		
		commandsText = new JTextPane();
		commandsText.setPreferredSize(new Dimension(400, 300));
		filenameField = new JTextField(30);
		fileLabel = new JLabel("File Name: ");

		this.serial = serial;

		// Add scrolling
		scroll = new JScrollPane(commandsText);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		saveButton = new JButton("Save Recipe");
		loadButton = new JButton("Load Recipe");
		uploadButton = new JButton("Upload Recipe");
		checkButton = new JButton("Check for Errors");

		commandsText.setText("");
		commandsText.setEditable(true);

		// Add the action listener
		AL AL = new AL();
		saveButton.addActionListener(AL);
		loadButton.addActionListener(AL);
		uploadButton.addActionListener(AL);
		checkButton.addActionListener(AL);
		filenameField.addActionListener(AL);

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
			if (e.getSource() == filenameField) {
				errorLines.clear();
				String text = TextFileReader.readEntireFile(filenameField.getText());
				if (text.split(":")[0].equals("Error")) {
					JOptionPane.showMessageDialog(null, text.split(":")[1]);
					return;
				}
				commandsText.setText(text);
			}

			if (e.getSource() == saveButton) {
				saveRecipe();
			}

			if (e.getSource() == loadButton) {
				errorLines.clear();
				String text = TextFileReader.readEntireFile(filenameField.getText());
				if (text.split(":")[0].equals("Error")) {
					JOptionPane.showMessageDialog(null, text.split(":")[1]);
					return;
				}
				commandsText.setText(text);
			}

			if (e.getSource() == uploadButton) {
				if (checkError()) {
					JOptionPane.showMessageDialog(null, "Found Errors. Aborting upload");
					return;
				}
				// Get each line separately
				String[] lines = commandsText.getText().split("\n");
				String[] compiled = new String[lines.length];
				for (int i = 0; i < lines.length; i++) {
					// Parse each line and add it to the compiled array
					compiled[i] = LineParser.parseCommand(lines[i]).trim();
				}

				// Send the compiled code to the incufridge
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
			// Delete the old file
			File file = new File(filenameField.getText());
			file.delete();

			// Write the recipe to a new file
			TextFileWriter.writeToFile(filenameField.getText(), commandsText.getText());
		}

		private boolean checkError() {
			boolean hasError = false;
			// Check for any errors and report them
			String[] lines = commandsText.getText().split("\n");
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].trim().equals("")) {
					continue;
				}
				if (LineParser.parseCommand(lines[i]).trim().equals("Error")) {
					errorLines.add(lines[i]);
					hasError = true;
				}
			}
			
			if (!hasError) {
				JOptionPane.showMessageDialog(null, "No Errors :D");
			}
			
			recolor();

			return hasError;
		}
		
		private void recolor() {
			String[] lines = commandsText.getText().split("\n");
			commandsText.setText("");

			for (int i = 0; i < lines.length; i++) {
				StyledDocument doc = commandsText.getStyledDocument();

				Style style = commandsText.addStyle("Error Style", null);
				StyleConstants.setForeground(style, Color.red);

				try {
					if (errorLines.contains(lines[i])) {
						doc.insertString(doc.getLength(), lines[i] + "\n", style);
					} else {
						doc.insertString(doc.getLength(), lines[i] + "\n", null);
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
