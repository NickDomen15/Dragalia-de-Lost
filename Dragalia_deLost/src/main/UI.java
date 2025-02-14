package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import object.OBJ_Heart;
import entity.Entity;


public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font dragaliaFont;
	BufferedImage heart_full, heart_half, heart_blank;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	public String currentDialogue = "";
	public String speakerName = "";
	public int commandNum = 0;
	
	
	public UI(GamePanel gp) {
		
		this.gp = gp;
		
		// intializes font from filepath
		InputStream is = getClass().getResourceAsStream("/font/tw-cen-mt-condensed-bold.ttf");
		try {
			dragaliaFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
		
	}
	
	
	// adds text to the message String
	public void showMessage(String text) {
		
		message = text;
		messageOn = true;
		
	}
	
	
	// draws UI elements on screen
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(dragaliaFont);
		g2.setColor(Color.white);
		
		// TILTE SCREEN STATE
		if (gp.gameState == gp.titleScreenState) {
			drawTitleScreen();
		}
		
		// PLAY STATE
		if (gp.gameState == gp.playState) {
			drawPlayerLife();
		}
		
		// PAUSE STATE
		if (gp.gameState == gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		
		// DIALOUGE STATE
		if (gp.gameState == gp.dialougeState) {
			drawPlayerLife();
			drawDialogueScreen();
		}
		
	}
	
	
	// Draw UI for current life
	public void drawPlayerLife() {
		
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		
		// Draw blank hearts for max life
		while (i < gp.player.maxLife/2) {
			
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
			
		}
		
		// reset values
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0;
		
		// Draw current life
		while (i < gp.player.currentLife) {
			
			g2.drawImage(heart_half, x, y, null); // first half heart
			i++;
			
			if (i < gp.player.currentLife) {
				g2.drawImage(heart_full, x, y, null); // first full heart
			}
			i++;
			x += gp.tileSize; // move to next heart
			
		}
		
	}
	
	
	// draws the title screen
	public void drawTitleScreen() {
		
		// BACKGROUND COLOR
		g2.setColor(new Color(69,103,233,255));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		// TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
		String text = "DRAGALIA de:LOST";
		int x = getCenteredTextX(text);
		int y = gp.tileSize*3;
		
		// TITLE OUTLINE
		g2.setColor(new Color(134,102,230,255));
		TextLayout tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,96F), g2.getFontRenderContext());
		Shape shape = tl.getOutline(null);
		g2.setStroke(new BasicStroke(9));
		g2.translate(x, y);
		g2.draw(shape);
		
		// TITLE
		g2.translate(-x, -y);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		// EUDEN IMAGE
		x = (gp.screenWidth/2 - (gp.tileSize*2)/2) / 2;
		y += gp.tileSize*2;
		g2.drawImage(gp.player.down2, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		// NOTTE IMAGE
		BufferedImage notteImage = null;
		try {
			
			notteImage = ImageIO.read(getClass().getResourceAsStream("/npcs/Notte_Down_2.png"));
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		x = gp.screenWidth/2 - (gp.tileSize*2)/2;
		g2.drawImage(notteImage, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		// ZETHIA IMAGE
		BufferedImage zethiaImage = null;
		try {
			
			zethiaImage = ImageIO.read(getClass().getResourceAsStream("/npcs/Zethia_Down_2.png"));
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		x = (gp.screenWidth/2 - (gp.tileSize*2)/2) + (gp.screenWidth/2 - (gp.tileSize*2)/2) / 2;
		g2.drawImage(zethiaImage, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		// MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
		
		// NEW GAME TEXT WITH OUTLINE
		text = "New Game";
		x = getCenteredTextX(text);
		y += gp.tileSize*4;
		g2.drawString(text, x, y);
		g2.setColor(new Color(32,66,129,255));
		tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,48F), g2.getFontRenderContext());
		shape = tl.getOutline(null);
		g2.setStroke(new BasicStroke(2));
		g2.translate(x, y);
		g2.draw(shape);
		g2.translate(-x, -y);
		g2.setColor(Color.white);
		
		// NEW GAME CURSOR
		if (commandNum == 0) {
			text = ">";
			g2.drawString(text, x - gp.tileSize, y);
			g2.setColor(new Color(32,66,129,255));
			tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,48F), g2.getFontRenderContext());
			shape = tl.getOutline(null);
			g2.setStroke(new BasicStroke(2));
			g2.translate(x - gp.tileSize, y);
			g2.draw(shape);
			g2.translate(-(x - gp.tileSize), -y);
			g2.setColor(Color.white);
		}
		
		// LOAD GAME TEXT WITH OUTLINE
		text = "Load Game";
		x = getCenteredTextX(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		g2.setColor(new Color(32,66,129,255));
		tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,48F), g2.getFontRenderContext());
		shape = tl.getOutline(null);
		g2.setStroke(new BasicStroke(2));
		g2.translate(x, y);
		g2.draw(shape);
		g2.translate(-x, -y);
		g2.setColor(Color.white);
		
		// LOAD GAME CURSOR
		if (commandNum == 1) {
			text = ">";
			g2.drawString(text, x - gp.tileSize, y);
			g2.setColor(new Color(32,66,129,255));
			tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,48F), g2.getFontRenderContext());
			shape = tl.getOutline(null);
			g2.setStroke(new BasicStroke(2));
			g2.translate(x - gp.tileSize, y);
			g2.draw(shape);
			g2.translate(-(x - gp.tileSize), -y);
			g2.setColor(Color.white);
		}
		
		// QUIT TEXT WITH OUTLINE
		text = "Quit";
		x = getCenteredTextX(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		g2.setColor(new Color(32,66,129,255));
		tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,48F), g2.getFontRenderContext());
		shape = tl.getOutline(null);
		g2.setStroke(new BasicStroke(2));
		g2.translate(x, y);
		g2.draw(shape);
		g2.translate(-x, -y);
		g2.setColor(Color.white);
		
		// QUIT CURSOR
		if (commandNum == 2) {
			text = ">";
			g2.drawString(text, x - gp.tileSize, y);
			g2.setColor(new Color(32,66,129,255));
			tl = new TextLayout(text, g2.getFont().deriveFont(Font.BOLD,48F), g2.getFontRenderContext());
			shape = tl.getOutline(null);
			g2.setStroke(new BasicStroke(2));
			g2.translate(x - gp.tileSize, y);
			g2.draw(shape);
			g2.translate(-(x - gp.tileSize), -y);
			g2.setColor(Color.white);
		}
		
	}
	
	
	// draws the pause screen text
	public void drawPauseScreen() {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String text = "PAUSED";
		int x = getCenteredTextX(text);
		int y = gp.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	
	
	// draws text for NPC dialogue
	public void drawDialogueScreen() {
		
		// WINDOW
		int x = gp.tileSize*2;
		int y = gp.tileSize*7 + 32;
		int width = gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height);
		
		// TEXT
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		g2.setColor(Color.white);
		g2.drawString(speakerName, x - 30, y - 34);
		
		
		Color c = new Color(0,32,52,255);
		g2.setColor(c);
		// displays text and moves to new lines
		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x - 20, y + 25);
			y += 40;
		}
		
	}
	
	
	// draws the window for dialouge box
	public void drawSubWindow(int x, int y, int width, int height) {

		// main white text box
		Color c = new Color(240,244,240,255);
		g2.setColor(c);
		g2.fillRect(x, y, width, height);
		
		// light blue/white accents
		c = new Color(208,228,240,255);
		g2.setColor(c);
		
		int topTextXValues[] = {x, x, x + width};
		int topTextYValues[] = {y + 30, y + 60, y + 42};
		g2.fillPolygon(topTextXValues, topTextYValues, 3);
		g2.fillRect(x, y, width, height/4 -6);
		
		int bottomTextXValues1[] = {x, x, x + width};
		int bottomTextYValues1[] = {y + height, y + 165, y + 140};
		int bottomTextXValues2[] = {x, x + width, x + width};
		int bottomTextYValues2[] = {y + height, y + 140, y + height};
		g2.fillPolygon(bottomTextXValues1, bottomTextYValues1, 3);
		g2.fillPolygon(bottomTextXValues2, bottomTextYValues2, 3);
		
		// speaker name bar (dark blue)
		c = new Color(0,32,52,255);
		g2.setColor(c);
		g2.fillRect(x - 5, y - 20, width + 10, height / 4);
		
		// triangle in bottom right
		int triangleXValues[] = {635, 645, 655};
		int triangleYValues[] = {y + 160, y + 175, y + 160};
		g2.fillPolygon(triangleXValues, triangleYValues, 3);
		
		// speaker name bar (lighter blue)
		c = new Color(12,60,104,255);
		g2.setColor(c);
		g2.fillRect(x + 388, y - 20, width / 3, height / 4);
		
	}
	
	
	public int getCenteredTextX(String text) {
		
		int x;
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		x = gp.screenWidth/2 - length/2;
		return x;
		
	}
	
}
