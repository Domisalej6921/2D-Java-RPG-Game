package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import interactive_tiles.InteractiveTiles;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile --- actual size displayed on screen
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;

    //Size of Game Screen
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 1;

    public boolean fullScreenOn = false;

    // FPS
    int FPS = 60;

    //SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    Thread gameThread;

    //ENTITY & OBJECT
    public Player player = new Player(this,keyH);
    public Entity obj[] = new Entity[20];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public InteractiveTiles iTiles[] = new InteractiveTiles[50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetSetter.setObject();
        assetSetter.setNPC();
        assetSetter.setMonster();
        assetSetter.setInteractiveTile();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn) {
            setFullScreen();//Needs fixing causes a bug
        }
    }

    public void retry() {

        player.setDefaultPositions();
        player.restoreLifeAndMana();
        assetSetter.setMonster();
        assetSetter.setNPC();
    }

    public void restart() {

        player.setDefaultValues();
        player.setItems();
        assetSetter.setMonster();
        assetSetter.setNPC();
        assetSetter.setObject();
        assetSetter.setInteractiveTile();
    }

    public void setFullScreen() {

        //GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        //SET FULL SCREEN
        gd.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; //0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while (gameThread != null) {
            
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                drawToTempScreen(); //draw everything to buffered image
                drawToScreen(); //draw buffered image to screen
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
            
        }

    }

    public void update() {

        if (gameState == playState) {
            //PLAYER UPDATE
            player.update();

            //NPC UPDATE
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            //MONSTER UPDATE
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if(monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if(!monster[i].alive) {
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }

            //PROJECTILE UPDATE
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if(projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if(!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

            //INTERACTIVE TILES UPDATE
            for(int i = 0; i < iTiles.length; i++) {
                if(iTiles[i] != null) {
                    iTiles[i].update();
                }
            }

            //PARTICLE UPDATE
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if(particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    if(!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }

        }
        if (gameState == pauseState) {
            //Pause
        }
    }

    public void drawToTempScreen() {

        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);

        } else {
            //Tile
            tileM.draw(g2);

            //DRAWING INTERACTIVE TILES
            for(int i = 0; i < iTiles.length; i++) {
                if(iTiles[i] != null) {
                    iTiles[i].draw(g2);
                }
            }

            //Adds entities to entityList
            entityList.add(player);
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            //adds objects to entityList
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            //adds monsters to entity list
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }

            //adds projectiles to entity list
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            //adds particles to entity list
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }

            //SORT ENTITIES LIST
            Collections.sort(entityList, new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();

            //UI
            ui.draw(g2);
        }

        //DEBUG
        if(keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(new Color(10, 36, 122));

            int x = 10;
            int y = 400;
            int lineheight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y); y += lineheight;
            g2.drawString("WorldY: " + player.worldY, x, y); y += lineheight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineheight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineheight;

            g2.drawString("FPS: " + FPS, x, y); y += lineheight;
            g2.drawString("Draw Time: " + passed + " ns", x, y);
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
