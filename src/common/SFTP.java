package common;

import java.util.Vector;

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
	
	public void createFile() {
		try {
			mySftpChannel.cd("cademons/incu/");
			mySftpChannel.mkdir("Checked");
			mySftpChannel.cd("../../");
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFile() {
		try {
			mySftpChannel.cd("cademons/incu/");
			mySftpChannel.rmdir("Checked");
			mySftpChannel.cd("../../");
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	public void exit() {
		mySftpChannel.exit();
	}
}
