package main;
import javax.swing.JLabel;
import javax.swing.JPanel; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tiles
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; // 48x48 tiles scaled up from 16x16
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12; // 4:3 screen ratio
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	// FPS
	int FPS = 60;
	
	// SYSTEM
	TileManager tileM = new TileManager(this); // manages map tiles
	public KeyHandler keyH = new KeyHandler(this); // manages key input
	public ClickListener cListener = new ClickListener(this); // manages mouse input
	Sound music = new Sound(); // plays sounds
	Sound soundEffects = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this); // checks for collision with tiles
	public AssetSetter aSetter = new AssetSetter(this); // adds objects to the map
	public UI ui = new UI(this); // draws UI elements on the screen
	public EventHandler eHandler = new EventHandler(this); // handles output of events
	Thread gameThread; // keeps the program running until you stop it
	
	// ENTITIES AND OBJECTS
	public Player player = new Player(this, keyH, cListener); // player character
	public Entity obj[] = new Entity[10]; // 10 slots to display objects at the same time
	public Entity npc[] = new Entity[10]; // 10 slots for npcs
	public Entity mon[] = new Entity[20]; // 20 slots for monsters
	ArrayList<Entity> entityList = new ArrayList<>(); // used for determining render order
	
	// GAME STATE
	public int gameState;
	public final int titleScreenState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialougeState = 3;
	
	
	
	/**
	 * Constructor, sets the window settings
	 */
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // helps with rendering
		this.addKeyListener(keyH); // GamePanel can recognize key inputs
		this.addMouseListener(cListener);
		this.setFocusable(true); // allows the window to be "focused" to recieve key input
		
	}

	
	// adds objects to the map
	public void setupGame() {
		
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonsters();
//		playMusic(0);
		gameState = titleScreenState;
		
	}
	
	
	/**
	 * Creates a new thread and runs the game
	 */
	public void startGameThread() {
		
		gameThread = new Thread(this); // passes GamePanel class to the Thread constructor
		gameThread.start(); // will call the run method
		
	}
	
	
	/**
	 * Handles the game loop of updating and 
	 * repainting to draw graphics
	 */
	@Override
	public void run() {
		
		// handles how often update and repaint are called
		double drawInterval = 1000000000/FPS; // interval = 1 second/60 FPS
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime(); // nanoTime counts time in nanoseconds
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			lastTime = currentTime;
			
			/**
			 * When 1/60 of a second passes, delta becomes 1, update and repaint 
			 * are called, then 1 is subtracted from delta for the next interval
			 */
			if (delta >= 1) {
				update(); // updates info such as character position
				repaint(); // calls the paintComponent method
				delta--;
				drawCount++;
			}
			
//			// prints the FPS every second
//			if (timer >= 1000000000) {
//				System.out.println("FPS: " + drawCount);
//				drawCount = 0;
//				timer = 0;
//			}
		}
		
	}
	
	
	/**
	 * Updates the information of moving objects on the screen
	 */
	public void update() {
		
		if (gameState == playState) {
			
			// PLAYER
			player.update();
			
			// NPCs
			for (int i = 0; i < npc.length; i++) {
				
				if (npc[i] != null) {
					npc[i].update();
				}
			}
			
			// MONSTERS
			for (int i = 0; i < mon.length; i++) {
				
				if (mon[i] != null) {
					mon[i].update();
				}
			}
			
		}
		
		if (gameState == pauseState) {
			// nothing
		}
		
	}
	
	
	/**
	 * Built in Java method for JPanel, draws sprites on the screen
	 */
	public void paintComponent(Graphics g) { // Graphics class: has many functions for drawing objects
		
		super.paintComponent(g); // calls the JPanel paintComponent method
		
		Graphics2D g2 = (Graphics2D)g; // Graphics2D: extends Graphics class and provides more functions
		
		// DEBUG
		long drawStart = 0;
		if (keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		
		// TITLE SCREEN
		if (gameState == titleScreenState) {
			
			ui.draw(g2);
		}
		
		else {
			
			// TILES
			tileM.draw(g2);
			
			// ADD PLAYER, NPCs, OBJECTS, AND MONSTERS TO ARRAYLIST
			entityList.add(player);
			
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					entityList.add(npc[i]);	
				}
			}
			
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] != null) {
					entityList.add(obj[i]);
				}
			}
			
			for (int i = 0; i < mon.length; i++) {
				if (mon[i] != null) {
					entityList.add(mon[i]);
				}
			}
			
			// SORT ENTITY LIST BY WORLD Y
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
				
			});
			
			// DRAW ENTITIES
			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			
			// EMPTY ENTITY LIST
			entityList.clear();
			
			
			// UI
			ui.draw(g2);
			
		}
		
		// DEBUG
		if (keyH.checkDrawTime == true) {
			long drawEnd = System.nanoTime();
			long timePassed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw time: " + timePassed, 10, 400);
			System.out.println("Draw time: " + timePassed);
		}
		
		g2.dispose();
		
	}
	
	
	// plays and loops background music
	public void playMusic(int i) {
		
		music.setFile(i);
		music.play();
		music.loop();
		
	}
	
	
	// stops currently playing background music
	public void stopMusic() {
		
		music.stop();
		
	}
	
	
	// plays sound effects
	public void playSE(int i) {
		
		soundEffects.setFile(i);
		soundEffects.play(); // only plays once
		
	}
	
}
