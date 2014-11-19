package swing;

import javax.swing.JTextArea;

import common.Input;

@SuppressWarnings("serial")
public class ConsoleTextArea extends JTextArea {
	public ConsoleTextArea(int rows, int cols) {
		super(rows, cols);
	}
	
	@Override
	public void append(String s) {
		super.append(s);
		Input.setInput(s);
	}
}
