import processing.core.PApplet;

public class IBox {
	
	PApplet app;
	int x=0,y=0,w=0,h=0;
	
	public IBox(PApplet parent, int x, int y, int w, int h){
		app = parent;
		app.rect(x,y,w,h);
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
	}
	
}
