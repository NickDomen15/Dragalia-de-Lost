package main;

import entity.NPC_OldMan;
import monster.MON_Slime;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		
		this.gp = gp;
		
	}
	
	
	/**
	 * Adds objects to the GamePanel obj array 
	 * and sets their worldX and worldY
	 */
	public void setObject() {

	}
	
	
	/**
	 * Adds NPCs to the GamePanel npc array
	 * and sets their worldX and worldY
	 */
	public void setNPC() {
		
		gp.npc[0] = new NPC_OldMan(gp);
		gp.npc[0].worldX = gp.tileSize*21;
		gp.npc[0].worldY = gp.tileSize*21;
		
	}
	
	
	/**
	 * Adds monsters to the GamePanel mon array
	 * and sets their worldX and worldY
	 */
	public void setMonsters() {
		
		gp.mon[0] = new MON_Slime(gp);
		gp.mon[0].worldX = gp.tileSize*23;
		gp.mon[0].worldY = gp.tileSize*36;
		
		gp.mon[1] = new MON_Slime(gp);
		gp.mon[1].worldX = gp.tileSize*23;
		gp.mon[1].worldY = gp.tileSize*37;
		
	}
	
}
