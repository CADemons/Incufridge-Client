package common;

/* 
 * NOTE:
 * This file is no longer used because the incufridge now connects
 * directly to the server via TCP instead of using SFTP
 */

// Provides a small amount of info for the SFTP to connect to the server
public class Info {
	public static String username = "";
	public static String hostname = "";
	public static String password = "";
	public static int portnum = 22;
	
	public static void init() {
		// Read from the auth file which should be stored one directory higher than the root folder of the project
		// Do not commit the auth file to the git repository (otherwise it will be public and anyone can login to the incufridge server)
		int amount = Integer.parseInt(TextFileReader.readLineFromFile("../auth", 1).trim());
		String user = TextFileReader.readLineFromFile("../auth", 2);
		String host = TextFileReader.readLineFromFile("../auth", 3);
		String pass = TextFileReader.readLineFromFile("../auth", 4);

		username = shift(user, amount);
		hostname = shift(host, amount);
		password = shift(pass, amount);
	}
	
	public static String shift(String s, int amount) {
		// Small amount of security to prevent people from being able to just read the auth file
		String out = "";
		for (Character c : s.toCharArray()) {
			out += (char) (c - (amount << 1)) + "";
		}
		return out.trim();
	}
}
