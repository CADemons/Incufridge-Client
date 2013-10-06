import processing.core.PApplet;

public class IBox {
	
	PApplet app;
	
	public IBox(PApplet parent, int x, int y, int w, int h){
		app = parent;
		app.rect(x,y,w,h);
	}
	
}
