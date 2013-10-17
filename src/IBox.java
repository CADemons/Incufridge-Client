import processing.core.PApplet;

public class IBox {

	PApplet app;
	int x=0,y=0,w=0,h=0;
	String intext = "";

	/**
	 * Create a new IBox.
	 * @param parent PApplet to act on, this when called from Client.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public IBox(PApplet parent, int x, int y, int w, int h){
		app = parent;
		app.stroke(0);
		app.fill(255);
		app.rect(x,y,w,h);
		app.fill(0);
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
	}

	public void onClick(){
		Client.selected = this;
	}

	public void write(int digit){
		intext += Integer.toString(digit);
		app.textSize(16);
		app.text(intext, x+5, y+18);
		app.redraw();
	}

	public void backspace() {
		if(!intext.isEmpty()){
			System.out.println("doing backspace...");
			intext = intext.substring(0, intext.length());
			app.text(intext, x+5, y+18);
			app.redraw();
		}
	}
}
