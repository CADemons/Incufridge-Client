import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class TextFileReader {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return lineToReturn;

		} else {
			System.err.println("File " + file + " does not exist!");
		}

		return "";
	}

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
		}
		
		return entireFile;
	}
}
