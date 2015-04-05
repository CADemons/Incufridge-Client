package common;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

// Class for reading text files
public class TextFileReader {
	// Read a specific line from a file
	public static String readLineFromFile(String filePath, int lineNumber) {
		String lineToReturn = "";
		int lineNumberCounter = 0;

		File file = new File(filePath);

		if (file.exists()) {
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));

				String line;
				while ((line = br.readLine()) != null) {
					lineNumberCounter += 1;

					if (lineNumberCounter == lineNumber) {
						br.close();
						return line;
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return lineToReturn;

		} else {
			System.err.println("File " + file + " does not exist!");
		}

		return "";
	}

	// Return the entire file as a string
	public static String readEntireFile(String filePath) {
		String entireFile = "";

		File file = new File(filePath);

		if (file.exists()) {
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));

				String line;
				while ((line = br.readLine()) != null) {
					entireFile += line + "\n";
				}

				br.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.err.println("File " + file + " does not exist!");
			return "Error:File " + file + " does not exist!";
		}

		return entireFile;
	}

	// Count the number of lines in a file
	public static int countLines(String filePath) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;
	}
}
