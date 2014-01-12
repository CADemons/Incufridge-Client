package incufridgePackage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleWriter extends OutputStream {

	static PrintStream origout;
	static String[] lines = new String[10];
	String fullstring = "";

	public ConsoleWriter(){
		for(int c=0; c<10; c++){
			lines[c] = "";
		}
	}

	public void write(int arg) throws IOException {
		int[] parsearr = {arg};
		String outstring = new String(parsearr,0,1);
		origout.print(outstring);
		fullstring += outstring;
		if(arg == 10){
			for(int c=0; c<9; c++){
				lines[c] = lines[c+1];
			}
			lines[9] = fullstring;
			fullstring = "";
			if(Client.state == 3){Client.app.rewrite();}
		}
	}

	public static void render(){
		int x = 15;
		int y = 210;
		int changey = 15;
		Client.app.textSize(10);
		for(int c=0; c<10; c++){
			Client.app.text(lines[c],x,y);
			y += changey;
		}
	}
}
