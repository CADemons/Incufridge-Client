package common;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// Class for writing strings to text files
public class TextFileWriter {
	// Write a string to a file (this will overwrite the file)
	public static void writeToFile(String filePath, String toWrite) {
		File file  = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
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
}
