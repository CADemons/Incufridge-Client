import java.io.*;
import java.net.*;

class TCPClient {
    public static void main(String args[]) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("108.168.213.183", 26517);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes("incufridge\n");
        boolean run = true;
        while(run) {
            String msg = inFromServer.readLine();
            System.out.println(msg);
            if (msg.equalsIgnoreCase("quit")) {
                run = false;
                break;
            }
        }
        clientSocket.close();
    }
}
