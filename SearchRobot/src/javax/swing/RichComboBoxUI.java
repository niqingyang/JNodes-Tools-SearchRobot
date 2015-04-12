package javax.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.plaf.basic.BasicComboBoxUI;

public class RichComboBoxUI extends BasicComboBoxUI implements KeyListener{
	public RichComboBoxUI(){
		
	}
	
	public KeyListener createKeyListener(KeyEvent e){
		System.out.println(e);
		return this;
	}
	
	@Override
	public void installKeyboardActions(){
		
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("--->");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
