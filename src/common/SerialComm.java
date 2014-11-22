package common;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class SerialComm implements SerialPortEventListener {
	int bytes = 0;
	SerialPort serialPort;
	//static byte[] temps = new byte[12];
	private ArrayList<Byte> outBytes = new ArrayList<Byte>();
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = new String[1];
	/**
	 * A BufferedReader which will be fed by a InputStreamReader 
	 * converting the bytes into characters 
	 * making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	public OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	public boolean receivedInput;

	public void initialize(String port) {
		//		Set<String> without = new HashSet<String>(Arrays.asList(Serial.list()));
		//		try {
		//			Thread.sleep(5000);
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		//		Set<String> with = new HashSet<String>(Arrays.asList(Serial.list()));
		//		with.removeAll(without);
		//		String[] newports = with.toArray(new String[0]);
		//		for(int a=0; a<newports.length;a++){
		//			System.out.println(newports[a]);
		//		}
		PORT_NAMES[0] = port;
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		System.out.println("Closing..");
		if (serialPort != null) {
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			bytes++;
			try {
				String inputLine = input.readLine();
				System.out.println(inputLine);
				if (inputLine.matches("\\d+(.\\d+)?")) {
					Input.setInput(inputLine);
					receivedInput = true;
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
			if (outBytes.size() > 0) {
				try {
					System.out.println("Sending data");
//					output.write('~');
//					for (int c = 0; c != 12; c++) {
//						output.write(temps[c]);
//					}
//					output.write('D');
					for(int i = 0; i < outBytes.size(); i++){
						output.write(outBytes.get(i));
					}
					outBytes.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//			if (bytes == 5) {
			//				close();
			//			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public String getPort(String[] arrWithout, String[] arrWith) {
		Set<String> without = new HashSet<String>(Arrays.asList(arrWithout));
		Set<String> with = new HashSet<String>(Arrays.asList(arrWith));
		with.removeAll(without);
		try {
			String[] newports = with.toArray(new String[0]);
			return newports[0];
		} catch(ArrayIndexOutOfBoundsException e) {
			return "Could not find port.";
		}
	}
	
	public void writeByte(byte in){
		outBytes.add(in);
	}
	
	public void writeBytes(byte[] in){
		for(int i = 0; i < in.length; i++){
			writeByte(in[i]);
		}
	}
}