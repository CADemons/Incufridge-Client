import java.io.PrintStream;

import processing.core.*;
import processing.event.MouseEvent;
import processing.serial.*;

@SuppressWarnings("serial")
public class Client extends PApplet {

	static Client app = null;
	static int state;
	String[] without;
	/**Array of inputs for detecting clicks.*/
	IBox[] inputs;
	/**Where to enter text.*/
	public IBox selected;
	PFont f;
	/**Whether to force GUI to open for testing.*/
	static boolean forceGui = false;
	String port;
	IButton[] buttons = new IButton[2];
	IBox commandInputBox;
	public SerialComm main;
	int numIBoxes;
	IBox numIBoxChoice;
	
	int mouseWheelChange = 15;

	public static void main(String[] args) {
		ConsoleWriter.origout = System.out;
		System.setOut(new PrintStream(new ConsoleWriter()));
		
		if (args.length > 0 && args[0].equals("forcegui")) {
			forceGui = true;
			System.out.println("Forcing GUI...");
		} else {
			System.out.println("Not forcing GUI.");
		}
		
		PApplet.main(new String[] {"Client"});
	}

	public void setup() {
		
		inputs = new IBox[0];
		app = this;
		//SerialComm main = new SerialComm();
		size(500, 350);
		background(255);
		f = createFont("Helectiva", 16, true);
		//textFont(f,16);
		textSize(16);

		fill(0);
		state = 1;
		/*main.initialize(port);
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");*/
		text("Detach the Incufridge, then click here.", 20, 100);
		super.redraw();
	}

	/**
	 * Called by redraw(), runs continuously once setup() finishes.
	 */
	public void draw() {
		/*if(state == 1){
			text("Detach the Incufridge, then click here.",20,100);
		}else if(state == 2){
			background(255);
			text("Reattach the Incufridge, then click here.",20,100);
		}else if(state == 3){
			background(255);
			text(port,20,100);
		}*/
	}

	/**
	 * Called by Processing when app window is clicked.
	 */
	public void mousePressed() {
		if (state == 1) {
			without = Serial.list();
			state = 2;
			background(255);
			numIBoxChoice = new IBox(app, 20, 150, 50, 20, 2);
			numIBoxChoice.render();
			numIBoxChoice.write(1);;
			text("Reattach the Incufridge, then click here.", 20, 100);
			super.redraw();
		} else if (state == 2) {
			numIBoxes = Integer.parseInt(numIBoxChoice.intext);
			inputs = new IBox[numIBoxes];
			String[] with = Serial.list();
			SerialComm portFinder = new SerialComm();
			this.port = portFinder.getPort(without,with);

			if (port == "Could not find port.") {
				if (forceGui) {
					port = "invalid-forced";
					initGui();
					state = 3;
				} else {
					background(255);
					text("Could not find port.",20,100);
					redraw();
				}
			} else {
				startSerial();
				initGui();
				state = 3;
			}
		} else if (state == 3) {
			for (int c = 0; c < inputs.length; c++) {
				if (mouseX >= inputs[c].x && mouseX <= inputs[c].x+inputs[c].w && mouseY >= inputs[c].y 
						&& mouseY <= inputs[c].y+inputs[c].h) {
					inputs[c].onClick();
				}
			}
			
			if (mouseX >= commandInputBox.x && mouseX <= commandInputBox.x + commandInputBox.w && mouseY >= commandInputBox.y && mouseY <= commandInputBox.y + commandInputBox.h) {
				commandInputBox.onClick();
			}
			
			for (int b = 0; b < buttons.length; b++) {
				if (mouseX >= buttons[b].x && mouseX <= buttons[b].x+buttons[b].w && mouseY >= buttons[b].y 
						&& mouseY <= buttons[b].y+buttons[b].h) {
					buttons[b].onClick();
					buttons[b].render(255, 0, 0);
					redraw();
				} else {

				}
			}
		}
	}

	public void mouseReleased() {
		for (int c = 0; c < buttons.length; c++) {
			if (buttons[c] != null) {
				buttons[c].render();
				redraw();
			}
		}
	}
	
	/*public void mouseWheel(MouseEvent e) {
		if ((int) e.getAmount() < 0) {
			mouseWheelChange = -15;
		} else {
			mouseWheelChange = 15;
		}
		redraw();
	}*/

	public void keyPressed() {
		if (state == 2) {
			System.out.println("State 2");
			int num = Character.getNumericValue(key);
			
			if (num >= 0 && num <= 9) {
				System.out.println("num: " + num);
				if (numIBoxChoice != null) numIBoxChoice.write(num);
			} else if (key == BACKSPACE) {
				if (numIBoxChoice != null) numIBoxChoice.backspace();
				System.out.println("backspace");
			} else {
				System.out.println("not num:" + key);
			}
		}
		if(state == 3){
			int num = Character.getNumericValue(key);


			if (num >= 0 && num <= 9) {
				System.out.println("num: " + num);
				if(selected != null) {selected.write(num);}
			} else if (key == BACKSPACE) {
				if (selected != null) {selected.backspace();}
				System.out.println("backspace");
			} else {
				System.out.println("not num:" + key);
			}


			if (selected == commandInputBox && ((key>='A' && key<='z') || key == ' ' || key == ';')) {
				selected.write(key);
			}
		}

	}

	/**
	 * Refresh GUI.
	 */
	public void redraw() {
		//Layout vars
		int startx = 20;
		int starty = 40;
		int textwidth = 25;
		int bufferh = 15;
		int boxw = 40;
		int boxh = 20;
		int boxx = startx;
		int boxy = starty;
		background(255);
		textSize(10);
		text("Incufridge on " + port, 10, 190);
		textSize(16);
		text("Incufridge Client", 175, 20);
		//		IButton upload = new IButton(this, 410, 160, "Upload", 20);
		//		textSize(16);
		
		if (state == 2) {
			System.out.println("Redrawing in state 2");
			text("Reattach the Incufridge, then click here.", 20, 100);
			numIBoxChoice.render();
		} else {
		
			buttons[0].render();
			ConsoleWriter.render(mouseWheelChange);
			textSize(16);
			
			text("Commands:", commandInputBox.x, commandInputBox.y - 5);
			commandInputBox.render();
			buttons[1].render();
			
			for (int c = 0; c < numIBoxes; c++) {
				inputs[c].render();
				text(Integer.toString(c+1) + ".", boxx, boxy + 20);
				boxx += textwidth;
				//			inputs[c] = new IBox(this,boxx,boxy,boxw,boxh);
				boxx += (boxw + 15);
				if ( c== 5) {
					boxx = startx;
					boxy = starty + boxh + bufferh;
				}
			}
	
			if (selected != null) {
				selected.render(255, 0, 0);
			}
		}

		stroke(0);
		line(0, 200, 500, 200);
		super.redraw();
	}

	public void initGui() {
		//Layout vars
		int startx = 20;
		int starty = 40;
		int textwidth = 25;
		int bufferh = 15;
		int boxw = 40;
		int boxh = 20;
		int boxx = startx;
		int boxy = starty;
		buttons[0] = new IButton(this, 410, 160, "Upload", 20);
		textSize(16);


		commandInputBox = new IBox(this, 10
				, 155, boxw + 100, boxh, 15);

		text("Commands:", commandInputBox.x, commandInputBox.y - 5
				);
		buttons[1] = new IButton(this, 160, 155, "Send", 12);

		for (int c = 0; c < numIBoxes; c++) {
			fill(0);
			stroke(0);
			text(Integer.toString(c+1) + ".", boxx, boxy + 20);
			boxx += textwidth;
			inputs[c] = new IBox(this, boxx, boxy, boxw, boxh, 3);
			boxx += (boxw + 15);
			if (c == 5) {
				boxx = startx;
				boxy = starty + boxh + bufferh;
			}

		}
		redraw();
	}

	/**
	 * Initialize serial communication class. See playground.arduino.cc/Interfacing/Java
	 * @param port Port name.
	 */
	public void startSerial() {
		main = new SerialComm();
		main.initialize(port);
		Thread t = new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
}
