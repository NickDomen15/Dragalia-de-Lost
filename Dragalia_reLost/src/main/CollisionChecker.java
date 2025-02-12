package main;

import entity.Entity;

public class CollisionChecker {

	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) {
		
		this.gp = gp;
		
	}
	
	
	/**
	 * Checks collision of entities with solid tiles
	 * @param entity
	 */
	public void checkTile(Entity entity) {
		
		// coordinates of the entity's solid area
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
		
		// world map column and row numbers of entity's solid area
		int entityLeftCol = entityLeftWorldX/gp.tileSize;
		int entityRightCol = entityRightWorldX/gp.tileSize;
		int entityTopRow = entityTopWorldY/gp.tileSize;
		int entityBottomRow = entityBottomWorldY/gp.tileSize;
		
		int tileNum1, tileNum2;
		
		if (entity.direction == "up") {
			// row above entity
			entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
			
			// 2 tiles that entity is trying to step on
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			
			// if either tile has collision set to true, entity cannot move
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			}
		}
		else if (entity.direction == "down") {
			// row below entity
			entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
			
			// 2 tiles that entity is trying to step on
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			
			// if either tile has collision set to true, entity cannot move
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			}
		}
		else if (entity.direction == "left") {
			// column to the left of entity
			entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
			
			// 2 tiles that entity is trying to step on
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			
			// if either tile has collision set to true, entity cannot move
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			}
		}
		else if (entity.direction == "right") {
			// column to the right of entity
			entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
			
			// 2 tiles that entity is trying to step on
			tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			
			// if either tile has collsion set to true, entity cannot move
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
				entity.collisionOn = true;
			}
		}
		
	}
	
	
	/**
	 * Check if player is hitting an object, 
	 * if they are, return the index of the object
	 * @param entity
	 * @param player
	 * @return
	 */
	public int checkObject(Entity entity, boolean player) {
		
		int index = 999;
		
		for (int i = 0; i < gp.obj.length; i++) {
			
			if (gp.obj[i] != null) {
				
				// Get the entity's solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				
				// Get the object's solid area position
				gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;
				
				// simulating entity's movement to check where it will be after moving
				if (entity.direction == "up") {
					entity.solidArea.y -= entity.speed;
				}
				else if (entity.direction == "down") {
					entity.solidArea.y += entity.speed;
				}
				else if (entity.direction == "left") {
					entity.solidArea.x -= entity.speed;
				}
				else if (entity.direction == "right") {
					entity.solidArea.x += entity.speed;
				}
				
				// checks if 2 solidArea's are touching
				if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
					
					// checks if object is solid
					if (gp.obj[i].collision == true) {
						entity.collisionOn = true;
					}
					
					/**
					 *  if the player touches an object, returns its 
					 *  index, if an NPC or monster does, don't
					 */
					if (player == true) {
						index = i;
					}
					
				}
				
				// reset entity and object solid area positions
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
				
			}
			
		}
		
		return index;
		
	}
	
	
	/**
	 * Checks player collisions with NPCs or monsters
	 * @param entity (player)
	 * @param target (Entity array, NPCs or monsters)
	 * @return
	 */
	public int checkEntity(Entity entity, Entity[] target) {
		
		int index = 999;
		
		for (int i = 0; i < target.length; i++) {
			
			if (target[i] != null) {
				
				// Get the players's solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				
				// Get the NPC or monster's solid area position
				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
				
				// simulating player's movement to check where it will be after moving
				if (entity.direction == "up") {
					entity.solidArea.y -= entity.speed;
				}
				else if (entity.direction == "down") {
					entity.solidArea.y += entity.speed;
				}
				else if (entity.direction == "left") {
					entity.solidArea.x -= entity.speed;
				}
				else if (entity.direction == "right") {
					entity.solidArea.x += entity.speed;
				}
				
				// checks if 2 solidArea's are touching
				if (entity.solidArea.intersects(target[i].solidArea)) {
					
					// entities cannot collide with themselves
					if (target[i] != entity) {
						// all entities are solid
						entity.collisionOn = true;
						index = i;
					}
					
				}
				
				// reset solid area positions
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;
				
			}
			
		}
		
		return index;
		
	}
	
	
	// Checks NPC or monster collisions with the player
	public boolean checkPlayer(Entity entity) {
		
		boolean contactPlayer = false;
		
		// Get the entity's solid area position
		entity.solidArea.x = entity.worldX + entity.solidArea.x;
		entity.solidArea.y = entity.worldY + entity.solidArea.y;
		
		// Get the player's solid area position
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		
		// simulating entity's movement to check where it will be after moving
		if (entity.direction == "up") {
			entity.solidArea.y -= entity.speed;
		}
		else if (entity.direction == "down") {
			entity.solidArea.y += entity.speed;
		}
		else if (entity.direction == "left") {
			entity.solidArea.x -= entity.speed;
		}
		else if (entity.direction == "right") {
			entity.solidArea.x += entity.speed;
		}
		
		// simulating entity's movement to check where it will be after moving
		entity.solidArea.y -= entity.speed;
					
		// checks if 2 solidArea's are touching
		if (entity.solidArea.intersects(gp.player.solidArea)) {
						
			// all entities are solid
			entity.collisionOn = true;
			contactPlayer = true;
						
		}
		
		// reset solid area positions
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		
		return contactPlayer;
		
	}
	
}
