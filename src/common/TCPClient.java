package common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
	public static void connect() throws IOException {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
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
			}
		}
	}
}
