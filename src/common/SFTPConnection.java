package common;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

// Provides a connection the a server
public class SFTPConnection {
	private Session session;
	private SFTP sftp;
	public boolean connected;
	
	// Establish a connection using the passed credentials and return an SFTP object to interact with the server
	public SFTP connect(String username, String hostname, String password, int portNum) {
		try {
			JSch jsch = new JSch();
			
			session = jsch.getSession(username, hostname, portNum);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();

			System.out.println("Successfully connected to: " + username + "@" + hostname);

			Channel channel = session.openChannel("sftp");
			channel.connect();

			sftp = new SFTP((ChannelSftp) channel);
			connected = true;
			
			return sftp;
		} catch (JSchException e) {
			System.out.print("An error has occurred: ");
			e.printStackTrace();
			return null;
		}
	}
	
	// Disconnect from the server
	public void disconnect() {
		sftp.exit();
		session.disconnect();
		System.out.println("Disconnected");
		
		connected = false;
	}
}
