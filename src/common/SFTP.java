package common;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public class SFTP {
	private ChannelSftp mySftpChannel;

	public SFTP(ChannelSftp sftpChannel) {
		mySftpChannel = sftpChannel;
	}
	
	public void upload(String filePath, String destination) {
		try {
			mySftpChannel.put(filePath, destination);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	public void download(String fileToDownload, String destination) {
		try {
			mySftpChannel.get(fileToDownload, destination);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	public void exit() {
		mySftpChannel.exit();
	}
}
