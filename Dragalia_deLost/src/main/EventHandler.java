package main;

public class EventHandler {

	GamePanel gp;
	EventRect eventRect[][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	
	public EventHandler(GamePanel gp) {
		
		this.gp = gp;
		
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 23;
			eventRect[col][row].y = 23;
			eventRect[col][row].width = 2;
			eventRect[col][row].height = 2;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
			
			col++;
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
		
	}
	
	
	public void checkEvent() {
		
		// check if the player character is more than 1 tile away from the last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > gp.tileSize) {
			canTouchEvent = true;
		}
		
		if (canTouchEvent == true) {
			
			if (hit(27, 16, "right") == true) {
				damagePit(27, 16, gp.dialougeState);
			}
			if (hit(23, 12, "up") == true) {
				healingPool(23, 12, gp.dialougeState);
			}
			
		}
		
	}
	
	
	public boolean hit(int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		// get player's current solidArea position
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		
		// get eventRect's solidArea position
		eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
		eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;
		
		// check if player's solidArea is colliding with eventRect's solidArea
		if (gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
			if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				
				hit = true;
				
				previousEventX = gp.player.worldX;
				previousEventY = gp.player.worldY;
				
			}
		}
		
		// after checking the collision, reset the solidArea x and y
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		
		return hit;
		
	}
	
	
	public void damagePit(int col, int row, int gameState) {
		
		gp.gameState = gameState;
		gp.ui.currentDialogue = "You fall into a pit!";
		gp.player.currentLife -= 1;
		
		//eventRect[col][row].eventDone = true;
		canTouchEvent = false;
		
	}
	
	public void healingPool(int col, int row, int gameState) {
		
		if (gp.keyH.enterPressed == true) {
			
			gp.gameState = gameState;
			gp.ui.currentDialogue = "You drink the water.\nYour life has been recovered";
			gp.player.currentLife = gp.player.maxLife;
			
		}
		
	}
	
}
