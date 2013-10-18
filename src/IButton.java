import processing.core.PApplet;

public class IButton {
	
	int xpad = 5;
	int ypad = 5;
	int x;
	int y;
	float w;
	int h;
	int textSize;
	Client app;
	String label;
	
	/**
	 * Create a new button, automatically scales to text.
	 * @param parent PApplet to act on, this when called from Client.
	 * @param x of upper-left corner
	 * @param y of upper-left corner
	 * @param label Text to display
	 * @param textSize
	 */
	public IButton(Client parent, int x, int y, String label, int textSize){
		app = parent;
		app.textSize(textSize);
		float twidth = app.textWidth(label);
		//System.out.println(twidth);
		w = twidth+(2*xpad);
		h = textSize+(2*ypad);
		this.x = x;
		this.y = y;
		this.textSize = textSize;
		this.label = label;
		
	}
	
	public void render(){
		app.textSize(textSize);
		app.stroke(0);
		app.fill(255);
		app.rect(x, y, w, h);
		app.fill(0);
		app.text(label, x+xpad, y+ypad+textSize);
	}
}
