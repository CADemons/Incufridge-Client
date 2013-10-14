import processing.core.PApplet;

public class IButton {
	
	int xpad = 5;
	int ypad = 5;
	PApplet app;
	
	public IButton(PApplet parent, int x, int y, String label){
		app = parent;
		app.textSize(16);
		float twidth = app.textWidth(label);
		System.out.println(twidth);
		app.stroke(0);
		app.fill(255);
		app.rect(x, y, twidth+(2*xpad), 16+(2*ypad));
		//app.rect(x, y, 20, 20);
		app.fill(0);
		app.text(label, x+xpad, y+ypad+16);
	}
}
