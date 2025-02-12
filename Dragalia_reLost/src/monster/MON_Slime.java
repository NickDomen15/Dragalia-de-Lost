package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_Slime extends Entity {

	GamePanel gp;
	
	public MON_Slime(GamePanel gp) {
		
		super(gp);
		
		this.gp = gp;
		
		type = 2; // monster
		name = "Slime";
		speed = 1;
		animationSpeed = 16;
		maxLife = 4;
		currentLife = maxLife;
		
		solidArea.x = 6;
		solidArea.y = 9;
		solidArea.width = 36;
		solidArea.height = 39;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
		
	}
	
	
	// Get and scale images from filepaths
	public void getImage() {
		
		up1 = setup("/monsters/Slime_1", gp.tileSize, gp.tileSize);
		up2 = setup("/monsters/Slime_2", gp.tileSize, gp.tileSize);
		down1 = setup("/monsters/Slime_1", gp.tileSize, gp.tileSize);
		down2 = setup("/monsters/Slime_2", gp.tileSize, gp.tileSize);
		left1 = setup("/monsters/Slime_1", gp.tileSize, gp.tileSize);
		left2 = setup("/monsters/Slime_2", gp.tileSize, gp.tileSize);
		right1 = setup("/monsters/Slime_1", gp.tileSize, gp.tileSize);
		right2 = setup("/monsters/Slime_2", gp.tileSize, gp.tileSize);
		
	}
	
	
	// Set AI
	public void setAction() {
		
//		actionLockCounter++;
//		
//		if (actionLockCounter == 120) {
//			
//			Random rand = new Random();
//			int i = rand.nextInt(100) + 1; // random num from 1-100
//			
//			// randomly picks a direction with 1/4 chance for each
//			if (i <= 25) {
//				direction = "up";
//			}
//			if (i > 25 && i <= 50) {
//				direction = "down";
//			}
//			if (i > 50 && i <= 75) {
//				direction = "left";
//			}
//			if (i > 75 && i <= 100) {
//				direction = "right";
//			}
//			
//			actionLockCounter = 0;
//			
//		}
		
	}
	
}
