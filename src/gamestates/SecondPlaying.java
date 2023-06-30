package gamestates;

import Levels.LevelManager;
import entities.EnemyManager;
import entities.Player;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelFinishoverlay;
import ui.PauseOverlay;
import utiz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static utiz.Constants.Background.*;

public class SecondPlaying extends State implements Statemethods{

    public static ArrayList<Player> players;
        private Player player;
        private Player second_player;
        private LevelManager levelManager;
        private EnemyManager enemyManager;
        private ObjectManager objectManager;
        private PauseOverlay pauseOverlay;
        private GameOverOverlay gameOverOverlay;
        private LevelFinishoverlay levelFinishoverlay;
        private boolean paused = false;

    private BufferedImage backgroundImg, background_house, background_house_big;
    private int[] housePos;
    private Random rnd = new Random();

    private int xLvlOffset;
    private int yLvlOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
    private int topBorder = (int)(0.2 * Game.GAME_HEIGHT);
    private int botBorder = (int)(0.8 * Game.GAME_HEIGHT);

    private int maxLvlOffsetY;
    private int maxLvlOffsetX;
    private boolean gameOver;
    private boolean lvlFinished = false;


    public SecondPlaying(Game game) {
            super(game);
            initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_BACKGROUND);
        background_house = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_HOUSE);
        background_house_big = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_HOUSE_BIG);
        housePos = new int[8];
        for(int i = 0; i < housePos.length; i++)
            housePos[i] = (int )(90 * Game.SCALE )+ rnd.nextInt(140);

        calculateLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();

    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calculateLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffsetX();
        maxLvlOffsetY = levelManager.getCurrentLevel().getLvlOffsetY();
    }
    //initialises all the necessary classes for the player with its respective numbers
    private void initClasses() {

            //array list for 2 players :)
            players = new ArrayList<>();
            levelManager = new LevelManager(game);
            enemyManager = new EnemyManager(this);
            objectManager = new ObjectManager(this);

            player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
            second_player = new Player(200, 210, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
            players.add(player);
            players.add(second_player);
            for(Player p: players){
            p.loadLvlData(levelManager.getCurrentLevel().getLvlData());
            }

            pauseOverlay = new PauseOverlay(this);
            gameOverOverlay = new GameOverOverlay(this);
            levelFinishoverlay = new LevelFinishoverlay(this);
        }
        @Override
        public void update() {
            if(paused) {
                pauseOverlay.update();
            }else if(lvlFinished) {
                levelFinishoverlay.update();
            }else if(!gameOver){
                levelManager.update();
                objectManager.update();
                enemyManager.update(levelManager.getCurrentLevel().getLvlData(), players);
                if (GameState.state == GameState.PLAYING) {
                    player.update();
                } else if (GameState.state == GameState.SECONDPLAYING) {
                    for (Player p : players)
                        p.update();
                }
                checkCloseToBorder();
            }



        }
        @Override
        public void draw(Graphics g) {
            g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH , Game.GAME_HEIGHT, null);
            drawBackgroundObj(g);
            levelManager.draw(g, xLvlOffset, yLvlOffset);
            enemyManager.draw(g, xLvlOffset, yLvlOffset);
            objectManager.draw(g, xLvlOffset, yLvlOffset);


            int i = 0;
            if(GameState.state == GameState.SECONDPLAYING ){
                for (Player p : players) {
                    p.render(g, xLvlOffset, yLvlOffset, i);
                    i++;
                }
            }
            if(GameState.state == GameState.PLAYING ){
                player.render(g, xLvlOffset, yLvlOffset, i);
            }

            if(paused)
                pauseOverlay.draw(g);

            else if (gameOver) {
                gameOverOverlay.draw(g);
            }else if (lvlFinished){
                levelFinishoverlay.draw(g);
            }

        }

    private void drawBackgroundObj(Graphics g) {
        g.drawImage(background_house_big, 1300 - (int) (xLvlOffset * 0.3), 300 - (int) (yLvlOffset * 0.5), HOUSE_BIG_WIDTH, HOUSE_BIG_HEIGHT, null);
        for( int i = 0; i < housePos.length; i++) {
            g.drawImage(background_house, HOUSE_WIDTH * 4 * i - (int) (xLvlOffset * 0.6), housePos[i] - (int) (yLvlOffset * 0.6), HOUSE_WIDTH, HOUSE_HEIGHT, null);
        }
    }
    private void checkCloseToBorder() {
        for(Player p: players) {
            int playerX = (int) player.getHitbox().x;
            int playerY = (int) player.getHitbox().y;
            int diffX = playerX - xLvlOffset;
            int diffY = playerY - yLvlOffset;


        //border checker
        if(diffX > rightBorder)
            xLvlOffset += diffX - rightBorder;
        else if( diffX < leftBorder)
            xLvlOffset += diffX - leftBorder;
        else if(diffY < topBorder)
            yLvlOffset += diffY - topBorder;
        else if( diffY > botBorder)
            yLvlOffset += diffY - botBorder;
        }

        // check that we dont get too close to the end of the lvl
        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
        if (yLvlOffset > maxLvlOffsetY)
            yLvlOffset = maxLvlOffsetY;
        else if (yLvlOffset < 0)
            yLvlOffset = 0;

    }
    public void resetAll(){
        gameOver = false;
        paused = false;
        lvlFinished = false;
        if(GameState.state == GameState.PLAYING){
            player.resetAll();
        }else {
            player.resetAll();
            second_player.resetAll();
        }
        enemyManager.resetAllEnemys();
        objectManager.resetAllObjects();

    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public void enemyHitChecker(Rectangle2D.Float attackHitbox){
        enemyManager.enemyHitChecker(attackHitbox);
    }
    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }
    public void checkObjectHit(Rectangle2D.Float attackHitbox){
        objectManager.checkObjectHit(attackHitbox);
    }


    @Override
    public void mouseClicked(MouseEvent e) {


    }
    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlFinished)
                levelFinishoverlay.mousePressed(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if(lvlFinished)
                levelFinishoverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if(lvlFinished)
                levelFinishoverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        switch (e.getKeyCode()) {
            //General keys

            //First Player
            case KeyEvent.VK_W:
                player.setJump(true);
                break; case KeyEvent.VK_A: player.setLeft(true);
                break; case KeyEvent.VK_D: player.setRight(true);
                break; case KeyEvent.VK_SPACE: player.setAttack(true);
                break;

                //Second Player
            case KeyEvent.VK_UP:
                second_player.setJump(true);
                break; case KeyEvent.VK_LEFT: second_player.setLeft(true);
                break; case KeyEvent.VK_RIGHT: second_player.setRight(true);
                break; case KeyEvent.VK_NUMPAD0: second_player.setAttack(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                System.out.println("Button has been pressed " + paused);
                break;
                //First Player
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break; case KeyEvent.VK_A: player.setLeft(false);
                    break; case KeyEvent.VK_D: player.setRight(false);
                    break;
                    //Second Player
                case KeyEvent.VK_UP:
                    second_player.setJump(false);
                    break; case KeyEvent.VK_LEFT: second_player.setLeft(false);
                    break; case KeyEvent.VK_RIGHT: second_player.setRight(false);
                    break;
        }

    }

    public void setLevelCompleted(boolean levelFinished) {
        this.lvlFinished = levelFinished;
    }
    public  void setMaxLevelOffset(int lvlOffsetX, int lvlOffsetY) {
        this.maxLvlOffsetX = lvlOffsetX;
        this.maxLvlOffsetY = lvlOffsetY;

    }

    public void unpauseGame() {
        paused = false;
    }
    public Player getSecondPlayer() {
        return second_player;
    }
    public Player getPlayer() {
        return player;
    }

    public void  windowFocusLost(){
        player.resetDirBooleans();
        second_player.resetDirBooleans();
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

}


