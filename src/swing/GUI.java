package swing;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import common.Communicator;
import common.ConsoleWriter;
import common.LineParser;
import common.SerialConnector;

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
		
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	serial.close();
		    	System.out.println("Closing");
		    }
		});
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
		Communicator.setSerial(gui.serial);
		// Add all the various tabs
		gui.addTab("Data", new DataDisplayPanel());
		gui.addTab("Recipe", new CommandsPanel());
		gui.addTab("Console", new ConsolePanel(new ConsoleWriter(false)));
		gui.addTab("Connection Data", new ConnectionPanel(gui.serial));
		LineParser.init(new String[] {"PWM", "FAN_ON", "FAN_OFF", "LIGHT_ON", 
			"LIGHT_OFF", "READ_DISPLAY", "SET_TEMP", "PRESS_BUTTON"});
		gui.setVisible(true);
		
	}
}
