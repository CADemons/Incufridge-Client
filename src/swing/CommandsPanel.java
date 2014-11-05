package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	
	public CommandsPanel() {
		commandsText = new JTextArea(20, 35);
		filenameField = new JTextField(30);
		fileLabel = new JLabel("File Name: ");
		
		scroll = new JScrollPane(commandsText);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		saveButton = new JButton("Save Recipe");
		loadButton = new JButton("Load Recipe");
		uploadButton = new JButton("Upload Recipe");
		
		commandsText.setText("");
		commandsText.setEditable(true);
		
		AL AL = new AL();
		saveButton.addActionListener(AL);
		loadButton.addActionListener(AL);
		uploadButton.addActionListener(AL);
		
		this.add(fileLabel);
		this.add(filenameField);
		this.add(scroll);
		this.add(saveButton);
		this.add(loadButton);
		this.add(uploadButton);
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == saveButton) {
				File file = new File(filenameField.getText());
				file.delete();
				
				TextFileWriter.writeToFile(filenameField.getText(), commandsText.getText());
			}
			
			if (e.getSource() == loadButton) {
				commandsText.setText(TextFileReader.readEntireFile(filenameField.getText()));
			}
		}
	}
}
