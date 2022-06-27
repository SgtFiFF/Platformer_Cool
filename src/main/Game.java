package main;


import gamestates.GameState;
import gamestates.Playing;
import gamestates.Menu;
import gamestates.SecondPlaying;

import java.awt.*;


public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private  final int UPS_SET = 200;

    private Playing playing;
    private SecondPlaying secondplaying;
    private Menu menu;

    public static int LVL_SPRITE_GR = 60;                                               // falls die Lvl_Sprites datei erweitert wird
    public final static int TILES_DEFAULT_SIZE = 32;                                    //Game window calculation for ez access
    public final static float SCALE = 2.0f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final  static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final  static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final  static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;




    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {

        menu = new Menu(this);
        playing = new Playing(this);
        secondplaying = new SecondPlaying(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
       switch (GameState.state){
           case MENU:
                menu.update();
                break;
           case PLAYING:
                playing.update();
                break;
           case SECONDPLAYING:
               secondplaying.update();
               break;

           case QUIT:
           default:
               System.exit(0);
               break;
       }
    }

    public void render(Graphics g) {

        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case SECONDPLAYING:
                secondplaying.draw(g);
                break;
            default:
                break;
        }
    }

    @Override // Gameloop
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previouseTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        double deltaF = 0;

        while(true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previouseTime) / timePerUpdate;
            deltaF += (currentTime - previouseTime) / timePerFrame;
            previouseTime = currentTime;

            if(deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if(deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
                frames++;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "| UPS " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void  windowFocusLost(){
        if(GameState.state == GameState.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }
    public Menu getMenu() {
        return menu;
    }
    public Playing getPlaying() {
        return playing;
    }
    public SecondPlaying getSecondplaying(){
        return secondplaying;
    }
}
