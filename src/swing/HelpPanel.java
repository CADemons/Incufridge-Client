package swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HelpPanel extends JPanel implements ActionListener {
	private JTextArea textArea;
	private JButton nextHelpButton;
	
	private int curHelp = 0;
	private String[] helpTags = {"Data display Help", "Recipe Help", "Console Help", "Connection Help"};
	
	public HelpPanel() {
		textArea = new JTextArea(20, 35);
		textArea.setEditable(false);
		nextHelpButton = new JButton(helpTags[0]);
		
		nextHelpButton.addActionListener(this);
		
		this.add(textArea);
		this.add(nextHelpButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextHelpButton) {
			nextHelpButton.setText(helpTags[curHelp == helpTags.length - 1 ? curHelp = 0 : ++curHelp]);
		}
	}
}
