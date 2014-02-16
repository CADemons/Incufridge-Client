import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleWriter extends OutputStream {

	static PrintStream origout;
	static String[] lines = new String[10];
	String fullstring = "";

	public ConsoleWriter() {
		for (int c = 0; c < 10; c++) {
			lines[c] = "";
		}
	}

	public void write(int arg) throws IOException {
		int[] parsearr = {arg};
		String outstring = new String(parsearr, 0, 1);
		origout.print(outstring);
		fullstring += outstring;
		if (arg == 10){
			for (int c = 0; c < 9; c++) {
				lines[c] = lines[c+1];
			}
			lines[9] = fullstring;
			fullstring = "";
			if (Client.state == 3) {Client.app.redraw();}
		}
	}
	
	public static void render() {
		render(15);
	}
	
	public static void render(int changeyArg) {
		int x = (int) (15 * (Client.myYSize / 350.0));
		int y = (int) (210 * (Client.myYSize / 350.0));
		
		int changey = changeyArg;
		Client.app.textSize(10);
		for(int c=0; c<10; c++){
			Client.app.text(lines[c], x, y);
			y += changey;
		}
	}
}
