package entity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

	GamePanel gp;
	
	// BufferedImage: describes an image with an accessible buffer of image data
	public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
	public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2, attackDown3;
	public BufferedImage image, image2, image3; // used for objects
	
	// defines the area of the sprite that can collide with objects
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

	String dialogues[] = new String[20]; // array for NPC dialogue
	public boolean collision = false; // collision for tiles
	
	
	// STATE
	public int worldX, worldY; // position on world map
	public String direction = "down"; // default direction
	public int spriteNum = 1; // default spriteNum
	int dialogueIndex = 0; // current position in dialogue array
	public boolean collisionOn = false; // collision for entities
	public boolean invincible = false; // makes entity invincible when true
	public boolean attacking = false; 
	public int chargeCounter = 0;
	public boolean charged = false;
	public boolean chargeReleased = false;
	
	
	// COUNTER
	public int spriteCounter = 0; // switching between images for animation
	public int animationSpeed = 10; // how fast animation plays
	public int actionLockCounter = 0; // adds a delay for when NPC actions can be updated
	public int iFrames = 0; // invincibility frames
	
	
	// ENTITY ATRIBUTES
	public int type; // entity type, 0 = player, 1 = npc, 2 = monster
	public String name;
	public int speed; // how fast the entity moves
	public int maxLife;
	public int currentLife;
	
	
	public Entity(GamePanel gp) {
		
		this.gp = gp;
	}
	
	
	// placeholder
	public void setAction() {}
	
	public void speak() {
		
		gp.ui.speakerName = name;
		
		if (dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		// turn NPC to face player
		if (gp.player.direction == "up") {
			direction = "down";
		}
		else if (gp.player.direction == "down") {
			direction = "up";
		}
		else if (gp.player.direction == "left") {
			direction = "right";
		}
		else if (gp.player.direction == "right") {
			direction = "left";
		}
		
	}
	
	
	// updates the entity's information to be drawn
	public void update() {
		
		setAction(); // will call method from subclass
		
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.mon);
		
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		// if monster contacts player
		if (this.type == 2 && contactPlayer == true) {
			
			if (gp.player.invincible == false) {
				gp.player.currentLife -= 1; // player takes damage
				gp.player.invincible = true;
			}
			
		}
		
		// IF COLLISION IS FALSE, ENTITY CAN MOVE
//		if (collisionOn == false) {
//						
//			if (direction == "up") {
//				worldY -= speed;
//			}
//			else if (direction == "down") {
//				worldY += speed;
//			}
//			else if (direction == "left") {
//				worldX -= speed;
//			}
//			else if (direction == "right") {
//				worldX += speed;
//			}
//						
//		}
					
		spriteCounter++;
					
		/**
		 * At each interval, checks the currently displayed 
		 * sprite and changes it to the next one in the walk cycle
		 */
		if (spriteCounter > animationSpeed) {
			
			if (spriteNum == 1) {
				spriteNum = 2;
			}
			else if (spriteNum == 2) {
				spriteNum = 1;
			}
			
			spriteCounter = 0;
		}
		
		
		if (invincible == true) {
			iFrames++;
			if (iFrames > 40) {
				invincible = false;
				iFrames = 0;
			}
		}
		
	}
	
	
	// draws the entity on the screen
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		// only draws entities that are in view of the player
		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
			worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
			worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
			worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			// cycles thru the different images for walk cycle
			if (direction == "up") {
				if (spriteNum == 1) { image = up1; }
				if (spriteNum == 2) { image = up2; }
			}
			else if (direction == "down") {
				if (spriteNum == 1) { image = down1; }
				if (spriteNum == 2) { image = down2; }
			}
			else if (direction == "left") {
				if (spriteNum == 1) { image = left1; }
				if (spriteNum == 2) { image = left2; }
			}
			else if (direction == "right") {
				if (spriteNum == 1) { image = right1; }
				if (spriteNum == 2) { image = right2; }
			}
			
			// make image transparent when iFrames are active
			if (invincible == true) {
				
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
				
			}
			
			g2.drawImage(image, screenX, screenY, null);
			
			// reset transparancy
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			
			g2.setColor(Color.red);
			g2.setStroke(new BasicStroke(1));
			g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		
		}
		
	}
	
	
	// gets a entity image from the filepath and scales it to tileSize
	public BufferedImage setup(String imagePath, int width, int height) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage originalImage = null;
		BufferedImage scaledImage = null;
		
		try {
			
			originalImage = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			scaledImage = uTool.scaleImage(originalImage, width, height);
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
			
		return scaledImage;
		
	}
	
}
