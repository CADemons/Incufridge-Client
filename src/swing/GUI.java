package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import common.Communicator;
import common.ConsoleWriter;
import common.Info;
import common.LineParser;
import common.SerialConnector;

/* This class runs the GUI of the incu-fridge */
@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {
	
	// Manage the various tabs of the GUI
	private JTabbedPane tabManager = new JTabbedPane(JTabbedPane.TOP);
	// The main object to make serial connections
	public SerialConnector serial = new SerialConnector();
	
	private JMenuBar menuBar = new JMenuBar();
	
	public GUI() {
		super("Incu-Fridge");
		
		ConsoleWriter.origout = System.out;
		
		this.setSize(512, 512);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	// Make sure to close the serial port when the program is closed
		    	serial.close();
		    	System.out.println("Closing");
		    }
		});
		
		// Add the menubar
		this.setJMenuBar(menuBar);
	}
	
	public void init() {
		Communicator.setSerial(serial);
		Info.init();

		// Add all the various tabs
		addTab("Data", new DataDisplayPanel());
		addTab("Recipe", new CommandsPanel());
		addTab("Console", new ConsolePanel(new ConsoleWriter(false)));
		addTab("Connection Data", new ConnectionPanel(serial));
		
		// Add the menubar
		JMenu m = new JMenu("File");
		// File -> help
		JMenuItem item = new JMenuItem("Help");
		item.addActionListener(this);
		m.add(item);
		addMenu(m);

		// The commands for the line parser
		LineParser.init(new String[] {"PWM", "FAN_ON", "FAN_OFF", "LIGHT_ON", 
			"LIGHT_OFF", "READ_DISPLAY", "SET_TEMP", "PRESS_BUTTON"});

		setVisible(true);
	}
	
	public void addMenu(JMenu m) {
		menuBar.add(m);
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

	public void actionPerformed(ActionEvent e) {
		if (((JMenuItem) e.getSource()).getText().equals("Help")) {
			// What to do when File -> help is pressed
			// Make a new JFrame containing the help panel
			JFrame frame = new JFrame("Incu-Fridge Help");
			frame.setSize(512, 512);
			frame.setResizable(false);
			frame.setVisible(true);
			frame.add(new HelpPanel());
		}
	}

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.init();
	}
}
