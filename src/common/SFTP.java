package common;

import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

// Class to provide access to the server
public class SFTP {
	private ChannelSftp mySftpChannel;

	public SFTP(ChannelSftp sftpChannel) {
		mySftpChannel = sftpChannel;
	}
	
	// Upload a file to the server
	public void upload(String filePath, String destination) {
		try {
			mySftpChannel.put(filePath, destination);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	// Download a file from the server
	public void download(String fileToDownload, String destination) {
		try {
			mySftpChannel.get(fileToDownload, destination);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	// Download all of the programs stored on the server
	public void downloadProgramsDir() {
		try {
			mySftpChannel.cd("cademons/incu/Programs");
			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> files = mySftpChannel.ls(mySftpChannel.pwd());
			for (ChannelSftp.LsEntry file : files) {
				if (!file.getFilename().startsWith(".")) {
					System.out.println(file.getFilename());
					mySftpChannel.get(file.getFilename(), "Programs/");
				}
			}
			mySftpChannel.cd("../../../");
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	// Check if the file 'checked' on the server exists
	public boolean fileExists() {
		try {
			mySftpChannel.cd("cademons/incu/");
			boolean result = mySftpChannel.ls(mySftpChannel.pwd()).toString().contains(" Checked,");
			mySftpChannel.cd("../../");
			
			return result;
		} catch (SftpException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Create a directory on the server called 'checked'
	public void createFile() {
		try {
			mySftpChannel.cd("cademons/incu/");
			mySftpChannel.mkdir("Checked");
			mySftpChannel.cd("../../");
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	// Remove the directory on the server called 'checked'
	public void removeFile() {
		try {
			mySftpChannel.cd("cademons/incu/");
			mySftpChannel.rmdir("Checked");
			mySftpChannel.cd("../../");
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	// Disconnect from the server
	public void exit() {
		mySftpChannel.exit();
	}
}
