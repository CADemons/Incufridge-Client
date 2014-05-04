import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class CommandsPanel extends JPanel {
	
	private JButton saveButton;
	private JTextArea commandsText;
	private JScrollPane scroll;
	
	public CommandsPanel() {
		commandsText = new JTextArea(20, 35);
		
		scroll = new JScrollPane(commandsText);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		saveButton = new JButton("Save Recipe");
		
		commandsText.setText(TextFileReader.readEntireFile("Commands"));
		commandsText.setEditable(true);
		
		AL AL = new AL();
		saveButton.addActionListener(AL);
		
		this.add(scroll);
		this.add(saveButton);
	}
	
	private class AL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == saveButton) {
				File file = new File("Commands");
				file.delete();
				
				TextFileWriter.writeToFile("Commands", commandsText.getText());
			}
		}
	}
}
