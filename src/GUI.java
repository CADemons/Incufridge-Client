import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	private JTabbedPane tabbed = new JTabbedPane(JTabbedPane.TOP);
	public Serial serial = new Serial();
	
	public GUI() {
		super("Incu-Fridge");
		
		ConsoleWriter.origout = System.out;
		
		this.setSize(512, 512);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
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
	
	public void removeTab(int index) {
		tabbed.removeTabAt(index);
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.addTab("Data", new DataDisplayPanel());
		gui.addTab("Recipe", new CommandsPanel());
		gui.addTab("Console", new ConsolePanel(new ConsoleWriter(false), gui.serial));
		gui.addTab("Connection Data", new ConnectionPanel(gui.serial));
	}
}