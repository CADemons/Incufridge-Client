import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	JTabbedPane tabbed = new JTabbedPane(JTabbedPane.TOP);
	
	public GUI() {
		super("Incu-Fridge");
		
		this.setSize(512, 512);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JPanel addTab(String name) {
		JPanel panel = new JPanel();

		tabbed.setBounds(20, 20, 360, 360);
		getContentPane().add(tabbed);

		tabbed.addTab(name, null, panel, "Does nothing");

		return panel;
	}

	public void addTab(String name, JPanel panel) {
		
		tabbed.setBounds(20, 20, 360, 360);
		getContentPane().add(tabbed);

		tabbed.addTab(name, null, panel, "Does nothing");
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.addTab("Recipe", new CommandsPanel());
		gui.addTab("Console", new ConsolePanel());
	}
}