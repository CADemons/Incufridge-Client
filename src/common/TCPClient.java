package common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
	public static void sendFile(DataOutputStream outToServer, String fileText) throws IOException {
		outToServer.writeBytes(fileText.replaceAll("\n", "_newline_") + "\n");
	}
	
	public static void connect() throws IOException {
		Socket clientSocket = new Socket("108.168.213.183", 26517);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes("incufridge\n");
		while (true) {
			String msg = inFromServer.readLine().trim();
			System.out.println(msg);
			if (msg.equals("get_temp")) {
				double temp = Communicator.getTemperature();
				System.out.println(temp);
				outToServer.writeBytes(temp + "\n");
			} else if (msg.startsWith("run_command ")) {
				String command = msg.substring(11);
				Communicator.sendCommand(LineParser.parseCommand(command));
				outToServer.writeBytes("done\n");
			} else if (msg.startsWith("send_file ")) {
				String txt = msg.substring(10).replaceAll("_newline_", "\n");
				TextFileWriter.deleteFile("Programs/main.incu");
				TextFileWriter.writeToFile("Programs/main.incu", txt);
				System.out.println("Wrote file to disk");
				outToServer.writeBytes("0\n");
				System.out.println("Uploading file");
				FileRunner.uploadAndRun("Programs/main.incu");
			} else if (msg.startsWith("get_file ")) {
				String filename = msg.substring(9);
				String txt = TextFileReader.readEntireFile("Programs/"+filename);
				System.out.println("Read file.");
				sendFile(outToServer, txt);
			} else if (msg.startsWith("get_log")) {
				Log.createLog();
				String txt = TextFileReader.readEntireFile("log.txt");
				outToServer.writeBytes(txt.replaceAll("\n", "_newline_") + "\n");
			}
		}
	}
}
