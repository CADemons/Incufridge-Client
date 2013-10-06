import processing.core.*;
import processing.serial.*;

public class Client extends PApplet{

	int state;
	//String port;
	String[] without;
	/**
	 * Array of inputs for detecting clicks.
	 */
	IBox[] inputs = new IBox[12];
	PFont f;

	public static void main(String[] args){
		PApplet.main(new String[] {"Client"});
	}

	public void setup(){
		//SerialComm main = new SerialComm();
		size(500,200);
		background(255);
		f = createFont("Helectiva",16,true);
		textFont(f,16);
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
		text("Detach the Incufridge, then click here.",20,100);
		redraw();
	}

	public void draw(){
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

	public void mousePressed(){
		if(state == 1){
			without = Serial.list();
			state = 2;
			background(255);
			text("Reattach the Incufridge, then click here.",20,100);
			redraw();
		}else if(state == 2){
			String[] with = Serial.list();
			SerialComm portFinder = new SerialComm();
			String port = portFinder.getPort(without,with);
			if(port == "Could not find port."){
				background(255);
				text("Could not find port.",20,100);
				redraw();
			}else{
				startSerial(port);
				initGui(port);
				state = 3;
			}
		}else if(state == 3){
			
		}
	}
	
	/**
	 * Create initial GUI layout.
	 */
	public void initGui(String port) {
		background(255);
		textFont(f,10);
		text("Incufridge on " + port,10,190);
		textFont(f,16);
		text("Incufridge Connect",175,20);
		redraw();
	}
	
	/**
	 * Initialize serial communication class. See playground.arduino.cc/Interfacing/Java
	 * @param port Port name.
	 */
	public void startSerial(String port){
		SerialComm main = new SerialComm();
		main.initialize(port);
		Thread t=new Thread() {
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
