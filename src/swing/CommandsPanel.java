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

import common.FileRunner;
import common.Info;
import common.LineParser;
import common.SFTP;
import common.SFTPConnection;
import common.TextFileReader;
import common.TextFileWriter;

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
	// Contains all lines which have errors in them
	private ArrayList<String> errorLines;

	public CommandsPanel() {
		errorLines = new ArrayList<String>();
		
		commandsText = new JTextPane();
		commandsText.setPreferredSize(new Dimension(400, 300));
		filenameField = new JTextField(30);
		fileLabel = new JLabel("File Name: ");

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

		// Add the action listeners
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
				loadFile();
			}

			if (e.getSource() == saveButton) {
				saveRecipe();
				if (filenameField.getText().startsWith("Server:")) {
					SFTPConnection c = new SFTPConnection();
					SFTP s = c.connect(Info.username, Info.hostname, Info.password, Info.portnum);
					s.upload(filenameField.getText().split(":")[1].trim(), "cademons/incu/Programs/");
					c.disconnect();
				}
			}

			if (e.getSource() == loadButton) {
				loadFile();
			}

			if (e.getSource() == uploadButton) {
				if (checkError()) {
					JOptionPane.showMessageDialog(null, "Found Errors. Aborting upload");
					return;
				}
				saveRecipe();
				FileRunner.uploadAndRun("Programs/" + filenameField.getText());
			}

			if (e.getSource() == checkButton) {
				checkError();
			}
			
		}

		private void saveRecipe() {
			File file = new File("Programs/" + filenameField.getText());
			if (filenameField.getText().startsWith("Server:")) {
				file = new File("Programs/" + filenameField.getText().split(":")[1].trim());
			}
			
			// Delete the old file
			file.delete();

			// Write the recipe to a new file
			TextFileWriter.writeToFile("Programs/" + file.getName(), commandsText.getText());
		}
		
		private void loadFile() {
			errorLines.clear();
			if (!filenameField.getText().startsWith("Server:")) {
				String text = TextFileReader.readEntireFile("Programs/" + filenameField.getText());
				if (text.split(":")[0].equals("Error")) {
					JOptionPane.showMessageDialog(null, text.split(":")[1]);
					return;
				}
				commandsText.setText(text);
			} else {
				String filename = filenameField.getText().split(":")[1].trim();
				SFTPConnection c = new SFTPConnection();
				SFTP s = c.connect(Info.username, Info.hostname, Info.password, Info.portnum);
				s.download("cademons/incu/Programs/" + filename, filename);
				c.disconnect();
				String text = TextFileReader.readEntireFile(filename);
				if (text.split(":")[0].equals("Error")) {
					JOptionPane.showMessageDialog(null, text.split(":")[1]);
					return;
				}
				commandsText.setText(text);
				s.exit();
			}
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
			
			recolor();
			
			if (!hasError) {
				JOptionPane.showMessageDialog(null, "No Errors :D");
			}

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
