package swing;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import common.ConsoleWriter;

/* This class runs the GUI of the incu-fridge */
@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	// Manage the various tabs of the GUI
	private JTabbedPane tabManager = new JTabbedPane(JTabbedPane.TOP);
	// The main object to make serial connections
	public SerialConnector serial = new SerialConnector();
	
	public GUI() {
		super("Incu-Fridge");
		
		ConsoleWriter.origout = System.out;
		
		this.setSize(512, 512);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
	}
	
	// Add a tab to the GUI
	public void addTab(String name, JPanel panel) {
		getContentPane().add(tabManager);

		tabManager.addTab(name, null, panel, name);
		System.currentTimeMillis();
	}
	
	// Remove a tab from the GUI
	public void removeTab(int index) {
		tabManager.removeTabAt(index);
	}

	public static void main(String[] args) {
		GUI gui = new GUI();
		// Add all the various tabs
		//gui.addTab("Data", new DataDisplayPanel());
		gui.addTab("Recipe", new CommandsPanel(gui.serial));
		gui.addTab("Console", new ConsolePanel(new ConsoleWriter(false), gui.serial));
		gui.addTab("Connection Data", new ConnectionPanel(gui.serial));
		gui.setVisible(true);
	}
}
