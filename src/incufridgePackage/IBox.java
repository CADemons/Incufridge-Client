package incufridgePackage;


public class IBox {

	Client app;
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
	public IBox(Client parent, int x, int y, int w, int h){
		app = parent;
		//		app.stroke(0);
		//		app.fill(255);
		//		app.rect(x,y,w,h);
		//		app.fill(0);
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
	}

	public void onClick(){
		app.selected = this;
	}

	public void write(int digit){
		if(intext.getBytes().length < 3){
			intext += Integer.toString(digit);
		}else{
			System.out.println("Too long!");
		}
		app.rewrite();
		//		app.textSize(16);
		//		app.text(intext, x+5, y+18);
		//		app.redraw();
	}

	public void backspace() {
		if(!intext.isEmpty()){
			System.out.println("doing backspace...");
			intext = intext.substring(0, intext.length()-1);
			app.rewrite();
			//			app.text(intext, x+5, y+18);
			//			app.redraw();
		}
	}

	public void render(){
		app.stroke(0);
		app.fill(255);
		app.rect(x,y,w,h);
		app.fill(0);
		app.textSize(16);
		app.text(intext, x+5, y+18);
	}
}
