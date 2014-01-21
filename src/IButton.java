
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
	public IButton(Client parent, int x, int y, String label, int textSize) {
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

	public void render(int r, int g, int b) {
		app.textSize(textSize);
		app.stroke(r, g, b);
		app.fill(255);
		app.rect(x, y, w, h);
		app.fill(r, g, b);
		app.text(label, x + xpad, y + ypad + textSize);
		app.fill(0);
	}

	public void render() {
		render(0, 0, 0);
	}

	public void onClick() {
		if (label == "Upload") {
			app.main.writeByte((byte)'~');
			for (int c = 0; c < app.inputs.length; c++) {
				int number;
				try {
					number = Integer.parseInt(app.inputs[c].intext);
				} catch(NumberFormatException e){
					number = 0;
				}
				if (app.inputs[c].intext != "" && number < 128) {
					app.main.writeByte(Byte.parseByte(app.inputs[c].intext));
					System.out.println("Uploaded: " + app.inputs[c].intext + " at slot " + c);
				} else {
					app.main.writeByte((byte)0);
				}
			}
			app.main.writeByte((byte)'D');
			System.out.println("Button clicked");
		} else if (label == "Send") {
			app.main.writeBytes(app.commandInputBox.intext.getBytes());
			System.out.println("Sent command: " + app.commandInputBox.intext);
		}
	}
}

