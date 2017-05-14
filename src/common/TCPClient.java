package common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// This class stores code for connecting and communicating with the incufridge server via TCP
public class TCPClient {
	public static void sendFile(DataOutputStream outToServer, String fileText) throws IOException {
		outToServer.writeBytes(fileText.replaceAll("\n", "_newline_") + "\n");
	}
	
	public static void connect() throws IOException {
		// Connect to the right IP and Port
		Socket clientSocket = new Socket("108.168.213.183", 26517);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		// Tell the server that this is the incufridge
		outToServer.writeBytes("incufridge\n");
		while (true) {
			String msg = inFromServer.readLine().trim();
			System.out.println(msg);
			// Here are the commands that we might receive from the server
			if (msg.equals("get_temp")) {
				// Send the server the current temperature
				double temp = Communicator.getTemperature();
				System.out.println(temp);
				outToServer.writeBytes(temp + "\n");
			} else if (msg.startsWith("run_command ")) {
				// Run a command on the incufridge provided by the incufridge
				String command = msg.substring(11);
				Communicator.sendCommand(LineParser.parseCommand(command));
				outToServer.writeBytes("done\n");
			} else if (msg.startsWith("send_file ")) {
				// Run a file that is sent by the server to be run
				String txt = msg.substring(10).replaceAll("_newline_", "\n");
				TextFileWriter.deleteFile("Programs/main.incu");
				TextFileWriter.writeToFile("Programs/main.incu", txt);
				System.out.println("Wrote file to disk");
				outToServer.writeBytes("0\n");
				System.out.println("Uploading file");
				FileRunner.uploadAndRun("Programs/main.incu");
			} else if (msg.startsWith("get_file ")) {
				// Send one of the files stored that may be run
				String filename = msg.substring(9);
				String txt = TextFileReader.readEntireFile("Programs/"+filename);
				System.out.println("Read file.");
				sendFile(outToServer, txt);
			} else if (msg.startsWith("get_log")) {
				// Create a log and send the log to the server
				Log.createLog();
				String txt = TextFileReader.readEntireFile("log.txt");
				outToServer.writeBytes(txt.replaceAll("\n", "_newline_") + "\n");
			}
		}
	}
}
