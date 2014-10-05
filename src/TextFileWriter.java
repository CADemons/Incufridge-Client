import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JEditorPane;
import javax.swing.JTextField;


public class TextFileWriter {

	public static void writeToFile(String filePath, String toWrite) {
		File file  = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println(filePath + " does not exist. Failed to create new file");
			}
		}
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
			out.println(toWrite);
			out.close();
		} catch (IOException e) {
			System.err.println("Could not write to file: " + filePath);
		}
	}

	public static void main(String[] args) {
		TextFileWriter.writeToFile("~/Hello.txt", "Hello");
	}
}
