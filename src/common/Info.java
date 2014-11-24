package common;

// Provides a small amount of info for the SFTP to connect to the server
public class Info {
	public static String username = "";
	public static String hostname = "";
	public static String password = "";
	public static int portnum = 22;
	
	public static void init() {
		int amount = Integer.parseInt(TextFileReader.readLineFromFile("../auth", 1).trim());
		String user = TextFileReader.readLineFromFile("../auth", 2);
		String host = TextFileReader.readLineFromFile("../auth", 3);
		String pass = TextFileReader.readLineFromFile("../auth", 4);

		username = shift(user, amount);
		hostname = shift(host, amount);
		password = shift(pass, amount);
	}
	
	public static String shift(String s, int amount) {
		String out = "";
		for (Character c : s.toCharArray()) {
			out += (char) (c - (amount << 1)) + "";
		}
		return out.trim();
	}
}
