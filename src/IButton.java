import processing.core.PApplet;

public class IButton {
	
	int xpad = 5;
	int ypad = 5;
	PApplet app;
	
	/**
	 * Create a new button, automatically scales to text.
	 * @param parent PApplet to act on, this when called from Client.
	 * @param x of upper-left corner
	 * @param y of upper-left corner
	 * @param label Text to display
	 * @param textSize
	 */
	public IButton(PApplet parent, int x, int y, String label, int textSize){
		app = parent;
		app.textSize(textSize);
		float twidth = app.textWidth(label);
		System.out.println(twidth);
		app.stroke(0);
		app.fill(255);
		app.rect(x, y, twidth+(2*xpad), textSize+(2*ypad));
		//app.rect(x, y, 20, 20);
		app.fill(0);
		app.text(label, x+xpad, y+ypad+textSize);
	}
}
