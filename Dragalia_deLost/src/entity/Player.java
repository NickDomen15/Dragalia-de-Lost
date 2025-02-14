package entity;

import main.ClickListener;
import main.GamePanel;
import main.KeyHandler;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {

	KeyHandler keyH;
	ClickListener cListener;
	
	/**
	 *  indicate where to draw player on the screen
	 *  they don't change throughout the game
	 */
	public final int screenX;
	public final int screenY;
	
	public int standCounter = 0;
	public int attackCounter = 0; // tracks frames for attack animation
	
	public Player(GamePanel gp, KeyHandler keyH, ClickListener cListener) {
		
		super(gp); // passes gp to the Entity superclass

		this.keyH = keyH;
		this.cListener = cListener;
		
		// draw player in the center of the screen
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		/**
		 *  determines the area on the player sprite 
		 *  that can make collisions with objects
		 */
		solidArea = new Rectangle();
		solidArea.x = 15;
		solidArea.y = 18;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 18;
		solidArea.height = 30;
		
		attackArea = new Rectangle();
		attackArea.width = 75;
		attackArea.height = 36;
		
		
		setDefaultValues();
		getPlayerImages();
		getPlayerAttackImages();
		
	}
	
	
	public void setDefaultValues() {
		
		// starting position of player on world map
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
		
		// PLAYER STATUS
		maxLife = 6;
		currentLife = maxLife;
		
	}
	
	
	// gets player images for standing/walking
	public void getPlayerImages() {
		
		up1 = setup("/player/Euden_Up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/Euden_Up_2", gp.tileSize, gp.tileSize);
		up3 = setup("/player/Euden_Up_3", gp.tileSize, gp.tileSize);
		down1 = setup("/player/Euden_Down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/Euden_Down_2", gp.tileSize, gp.tileSize);
		down3 = setup("/player/Euden_Down_3", gp.tileSize, gp.tileSize);
		left1 = setup("/player/Euden_Left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/Euden_Left_2", gp.tileSize, gp.tileSize);
		left3 = setup("/player/Euden_Left_3", gp.tileSize, gp.tileSize);
		right1 = setup("/player/Euden_Right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/Euden_Right_2", gp.tileSize, gp.tileSize);
		right3 = setup("/player/Euden_Right_3", gp.tileSize, gp.tileSize);
		
	}
	
	// gets player images for attacking
	public void getPlayerAttackImages() {
		
		// TODO add attacking images
		attackUp1 = setup("/player/Euden_Attack_Up_1", gp.tileSize*2, gp.tileSize*2);
		attackUp2 = setup("/player/Euden_Attack_Up_2", gp.tileSize*2, gp.tileSize*2);
		attackUp3 = setup("/player/Euden_Attack_Up_3", gp.tileSize*2, gp.tileSize*2);
		attackDown1 = setup("/player/Euden_Attack_Down_1", gp.tileSize*2, gp.tileSize*2);
		attackDown2 = setup("/player/Euden_Attack_Down_2", gp.tileSize*2, gp.tileSize*2);
		attackDown3 = setup("/player/Euden_Attack_Down_3", gp.tileSize*2, gp.tileSize*2);
		
	}
	
	
	/**
	 * Updates the player information to be drawn
	 */
	public void update() {
		
		// BASIC ATTACKS
		if (cListener.mouseClicked == true && chargeCounter == 0 && chargeReleased == false) {
			
			System.out.println("Clicked");
			attacking = true;
			cListener.mouseClicked = false;
			
		}
		if (attacking == true) {
			attacking();
		}
		
		
		// CHARGED ATTACKS
		if (cListener.mousePressed == true && attacking != true) {
			
			chargeCounter++;
			
			if (chargeCounter < 30) {
				//System.out.println("Charging...");
			}
			
			if (chargeCounter >= 30) {
				
				charged = true;
				//System.out.println("Charged!");
			}
			
		}
		
		if (cListener.mouseReleased == true && charged == false && attacking != true) {
			
			cListener.mousePressed = false;
			cListener.mouseReleased = false;
			chargeCounter = 0;
			
			//System.out.println("Charge didn't finish");
			
		}
		
		if (cListener.mouseReleased == true && charged == true && attacking != true) {
			
			cListener.mousePressed = false;
			cListener.mouseReleased = false;
			charged = false;
			chargeReleased = true;
			chargeCounter = 0;
			
			//System.out.println("Charged attack!!");
			
		}
		
		if (chargeReleased == true) {
			
			chargedAttack();
		}
		
		// if there is a key input, change the players direction and start moving
		if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true && attacking == false) {
			
			if (keyH.upPressed == true) {
				direction = "up";
			}
			else if (keyH.downPressed == true) {
				direction = "down";
			}
			else if (keyH.leftPressed == true) {
				direction = "left";
			}
			else if (keyH.rightPressed == true) {
				direction = "right";
			}
			
			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this); // passes player class as an entity
			
			// CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			// CHECK MONSTER COLLISION
			int monIndex = gp.cChecker.checkEntity(this, gp.mon);
			contactMonster(monIndex);
			
			// CHECK EVENT
			gp.eHandler.checkEvent();
		
			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (collisionOn == false && keyH.enterPressed == false && attacking == false && chargeCounter == 0) {
				
				if (direction == "up") {
					worldY -= speed;
				}
				else if (direction == "down") {
					worldY += speed;
				}
				else if (direction == "left") {
					worldX -= speed;
				}
				else if (direction == "right") {
					worldX += speed;
				}
				
			}
			
			gp.keyH.enterPressed = false;
			
			if (chargeCounter == 0) {
				spriteCounter++;
			}
			
			/**
			 * At each interval, checks the currently displayed 
			 * sprite and changes it to the next one in the walk cycle
			 */
			if (spriteCounter > 10) {
				
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 3;
				}
				else if (spriteNum == 3) {
					spriteNum = 4;
				}
				else if (spriteNum == 4) {
					spriteNum = 1;
				}
				
				spriteCounter = 0;
			}
		}
		
		/**
		 *  if no directions are being pressed, default to 
		 *  standing sprite (spriteNum = 2 for each direction)
		 */
		else {
			
			standCounter++;
			
			if (standCounter == 20) {
				spriteNum = 2;
				standCounter = 0;
			}
			
		}
		
		/*
		 * iFrames counter
		 * Needs to be outside of key input if statement
		 */
		if (invincible == true) {
			iFrames++;
			if (iFrames > 60) {
				invincible = false;
				iFrames = 0;
			}
		}
		
	}
	
	
	public void attacking() {
		
		attackCounter++;
		
		// save the current worldX, worldY, and solidArea
		int currentWorldX = worldX;
		int currentWorldY = worldY;
		int solidAreaWidth = solidArea.width;
		int solidAreaHeight = solidArea.height;
		solidArea.x = 0;
		solidArea.y = 0;
		
		
		// adjust player's worldX/Y for attackArea
		if (direction == "up") {
			worldX -= 15;
			worldY -= 6;
		}
		else if (direction == "down") {
			worldX -= 12;
			worldY += 30;
		}
		else if (direction == "left") {
			
		}
		else if (direction == "right") {
			
		}
		
		// attackArea becomes solidArea
		solidArea.width = attackArea.width;
		solidArea.height = attackArea.height;
		
		
		// check monster collision with the updated worldX/Y and solidArea
		int monsterIndex = gp.cChecker.checkEntity(this, gp.mon);
		System.out.println(monsterIndex);
		damageMonster(monsterIndex);
		
		// restore original worldX/Y and solidArea
		worldX = currentWorldX;
		worldY = currentWorldY;
		solidArea.width = solidAreaWidth;
		solidArea.height = solidAreaHeight;
		solidArea.x = solidAreaDefaultX;
		solidArea.y = solidAreaDefaultY;
		
		
		if (attackCounter <= 5) {
			spriteNum = 1;
		}
		if (attackCounter > 5 && attackCounter <= 13) {
			spriteNum = 2;
		}
		if (attackCounter > 13 && attackCounter <= 30) {
			spriteNum = 3;
		}
		if (attackCounter > 30 && attackCounter <= 35) {
			attackCounter = 0;
			attacking = false;
		}
		
		
	}
	
	
	public void chargedAttack() {
		
		chargeReleased = false;
		// TODO add charged attack
		
	}
	
	
	/**
	 * 
	 * @param index
	 */
	public void pickUpObject(int i) {
		
		// if index == 999, no object was touched
		if (i != 999) {
			
		}
	}
	
	
	public void interactNPC(int i) {
		
		// if index == 999, no NPC was touched
		if (i != 999) {
			
			if (gp.keyH.enterPressed == true) {
				gp.gameState = gp.dialougeState;
				gp.npc[i].speak();
			}
			
		}
		
	}
	
	
	// player takes damage and gains iFrames when contacting a monster
	public void contactMonster(int i) {
		
		// if i == 999, no monster was touched
		if (i != 999) {
			
			if (invincible == false) {
				currentLife -= 1;
				invincible = true;
			}
			
		}
		
	}
	
	
	
	public void damageMonster(int i) {
		
		// if i == 999, no monster was hit
		if (i != 999) {
			
			if (gp.mon[i].invincible == false) {
				
				gp.mon[i].currentLife -= 1;
				gp.mon[i].invincible = true;
				
				if (gp.mon[i].currentLife <= 0) {
					gp.mon[i] = null;
				}
			}
			
		}
		
	}
	
	
	/**
	 * Draws the player on the screen
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		// cycles thru the different images for walk cycle
		if (direction == "up") {
			if (attacking == false) {
				if (spriteNum == 1) { image = up1; }
				if (spriteNum == 2 || spriteNum == 4) { image = up2; }
				if (spriteNum == 3) { image = up3; }
			}
			if (attacking == true) {
				tempScreenX = screenX - 24;
				tempScreenY = screenY - 6;
				if (spriteNum == 1) { image = attackUp1; }
				if (spriteNum == 2) { image = attackUp2; }
				if (spriteNum == 3) { image = attackUp3; }
			}
		}
		else if (direction == "down") {
			if (attacking == false) {
				if (spriteNum == 1) { image = down1; }
				if (spriteNum == 2 || spriteNum == 4) { image = down2; }
				if (spriteNum == 3) { image = down3; }
			}
			if (attacking == true) {
				tempScreenX = screenX - 24;
				if (spriteNum == 1) { image = attackDown1; }
				if (spriteNum == 2) { image = attackDown2; }
				if (spriteNum == 3) { image = attackDown3; }
			}
		}
		else if (direction == "left") {
			if (attacking == false) {
				if (spriteNum == 1) { image = left1; }
				if (spriteNum == 2 || spriteNum == 4) { image = left2; }
				if (spriteNum == 3) { image = left3; }
			}
			if (attacking == true) {
				// ADD SPRITES HERE
			}
		}
		else if (direction == "right") {
			if (attacking == false) {
				if (spriteNum == 1) { image = right1; }
				if (spriteNum == 2 || spriteNum == 4) { image = right2; }
				if (spriteNum == 3) { image = right3; }
			}
			if (attacking == true) {
				// ADD SPRITES HERE
			}
		}
		
		// if player has iFrames, make them slightly transparent
		if (invincible == true) {
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			
		}
		
		if (charged == true) {
			
			// TODO draw charge attack arrow
			
		}
		
		// draws the image
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		// reset transparancy
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		// DEBUG
//		g2.setFont(new Font("Arial", Font.PLAIN,26));
//		g2.setColor(Color.white);
//		g2.drawString("iFrames: " + iFrames, 10, 400);
	
		// draw player hitbox
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(1));
		g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		
		// draw player attack hitbox
		if (direction == "up") {
			
			g2.drawRect(screenX - 15, screenY - 6, attackArea.width, attackArea.height);
			
		}
		else if (direction == "down") {
			
			g2.drawRect(screenX - 12, screenY + 30, attackArea.width, attackArea.height);
		
		}
		
	}
	
}
