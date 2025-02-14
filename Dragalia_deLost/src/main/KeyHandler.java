package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
	
	// DEBUG
	public boolean checkDrawTime = false;
	
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// don't use, but need it in the class for implementation
	}

	
	/**
	 * Detects when keys are pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();
		
		// TITLE STATE
		if (gp.gameState == gp.titleScreenState) {
			
			if(code == KeyEvent.VK_W) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) {
					gp.ui.commandNum = 2;
				}
			}
			
			if(code == KeyEvent.VK_S) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 2) {
					gp.ui.commandNum = 0;
				}
			}
			
			if(code == KeyEvent.VK_ENTER) {
				if (gp.ui.commandNum == 0) {
					gp.gameState = gp.playState;
					//gp.playMusic(0);
				}
				else if (gp.ui.commandNum == 1) {
					// load game, add later
				}
				else if (gp.ui.commandNum == 2) {
					System.exit(0);
				}
			}
			
		}
		
		// PLAY STATE
		if (gp.gameState == gp.playState) {
			
			// player movement
			if(code == KeyEvent.VK_W) {
				upPressed = true;
			}
			
			if(code == KeyEvent.VK_S) {
				downPressed = true;
			}
			
			if(code == KeyEvent.VK_A) {
				leftPressed = true;
			}
			
			if(code == KeyEvent.VK_D) {
				rightPressed = true;
			}
			
			// pausing the game
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.pauseState;
			}
			
			if (code == KeyEvent.VK_ENTER) {
				enterPressed = true;
			}
			
			// debug
			if (code == KeyEvent.VK_T) {
				if (checkDrawTime == false) {
					checkDrawTime = true;
				}
				else if (checkDrawTime == true) {
					checkDrawTime = false;
				}
			}
			
		}
		
		// PAUSE STATE
		else if (gp.gameState == gp.pauseState) {
			
			// unpausing
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			}
			
		}
		
		// DIALOGUE STATE
		else if (gp.gameState == gp.dialougeState) {
			
			if (code == KeyEvent.VK_ENTER) {
				gp.gameState = gp.playState;
			}
			
		}
		
	}

	
	/**
	 * Detects when keys are released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W) {
			upPressed = false;
		}
		
		if(code == KeyEvent.VK_S) {
			downPressed = false;
		}
		
		if(code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		
		if(code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		
	}

}
