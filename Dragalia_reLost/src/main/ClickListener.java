package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClickListener implements MouseListener {

	GamePanel gp;
	
	public boolean mouseClicked, mousePressed, mouseReleased;
	
	public ClickListener(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClicked = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseReleased = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
