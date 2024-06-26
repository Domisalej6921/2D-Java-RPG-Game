package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import interactive_tiles.InteractiveTiles;
import tile.Map;
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
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 10;
    public int currentMap = 0;

    public boolean fullScreenOn = false;

    // FPS
    int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    EnvironmentManager eManager = new EnvironmentManager(this);
    Map map = new Map(this);
    SaveLoad saveLoad = new SaveLoad(this);
    public EntityGenerator eGenerator = new EntityGenerator(this);
    Thread gameThread;

    //ENTITY & OBJECT
    public Player player = new Player(this,keyH);
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTiles iTiles[][] = new InteractiveTiles[maxMap][50];
    public Entity projectile[][] = new Entity[maxMap][20];
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
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;

    //AREA STATE
    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int indoor = 51;
    public final int dungeon = 52;

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
        eManager.setup();
        gameState = titleState;
        currentArea = outside;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn) {
            setFullScreen();//Needs fixing causes a bug
        }
    }

    public void resetGame(boolean restart) {

        currentArea = outside;
        player.setDefaultPositions();
        player.restoreStatus();
        assetSetter.setMonster();
        assetSetter.setNPC();
        player.resetCounter();

        if(restart) {
            player.setDefaultValues();
            assetSetter.setObject();
            assetSetter.setInteractiveTile();
            eManager.lighting.resetDay();
        }
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
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            //MONSTER UPDATE
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    if(monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }
                    if(!monster[currentMap][i].alive) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            //PROJECTILE UPDATE
            for (int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    if(projectile[currentMap][i].alive) {
                        projectile[currentMap][i].update();
                    }
                    if(!projectile[currentMap][i].alive) {
                        projectile[currentMap][i] = null;
                    }
                }
            }

            //INTERACTIVE TILES UPDATE
            for(int i = 0; i < iTiles[1].length; i++) {
                if(iTiles[currentMap][i] != null) {
                    iTiles[currentMap][i].update();
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

            //ENVIRONMENT UPDATE
            eManager.update();
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
        }
        //MAP SCREEN
        else if(gameState == mapState) {
            map.drawFullMapScreen(g2);
        }
        else {
            //Tile
            tileM.draw(g2);

            //DRAWING INTERACTIVE TILES
            for(int i = 0; i < iTiles[1].length; i++) {
                if(iTiles[currentMap][i] != null) {
                    iTiles[currentMap][i].draw(g2);
                }
            }

            //Adds entities to entityList
            entityList.add(player);
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }

            //adds objects to entityList
            for (int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }

            //adds monsters to entity list
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            //adds projectiles to entity list
            for (int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
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

            //ENVIRONMENT
            eManager.draw(g2);

            //Mini MAP
            map.drawMiniMap(g2);

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

    public void changeArea() {

        if (nextArea != currentArea) {

            stopMusic();

            if (nextArea == outside) {
                playMusic(0);
            }
            if (nextArea == indoor) {
                playMusic(18);
            }
            if (nextArea == dungeon) {
                playMusic(19);
            }
            assetSetter.setNPC();
        }

        currentArea = nextArea;
        assetSetter.setMonster();
    }
}
