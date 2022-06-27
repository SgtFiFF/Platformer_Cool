package gamestates;

import Levels.LevelManager;
import entities.EnemyManager;
import entities.Player;
import main.Game;
import ui.PauseOverlay;
import utiz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utiz.Constants.Background.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.85 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
    private BufferedImage backgroundImg, background_house, background_house_big;
    private int[] housePos;
    private Random rnd = new Random();

    // testing
    private int yLvlOffset;
    private int topBorder = (int)(0.2 * Game.GAME_HEIGHT);
    private int botBorder = (int)(0.8 * Game.GAME_HEIGHT);
    private int lvlTilesHeight = LoadSave.GetLevelData().length;
    private int maxTilesOffsetY = lvlTilesHeight - Game.TILES_IN_HEIGHT;
    private int maxLvlOffsetY = maxTilesOffsetY * Game.TILES_SIZE;

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_BACKGROUND);
        background_house = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_HOUSE);
        background_house_big = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_HOUSE_BIG);
        housePos = new int[8];
        for(int i = 0; i < housePos.length; i++)
            housePos[i] = (int )(90 * Game.SCALE )+ rnd.nextInt(140);
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200,200,(int) (64 * Game.SCALE),(int) (40 * Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        pauseOverlay = new PauseOverlay(this);
    }
    @Override
    public void update() {
    if(!paused) {
        levelManager.update();
        enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
        player.update();
        checkCloseToBorder();
    }else {
        pauseOverlay.update();
    }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diffX = playerX - xLvlOffset;

        int playerY = (int) player.getHitbox().y;
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
        // check that we dont get too close to the end of the lvl
        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if(xLvlOffset < 0)
            xLvlOffset = 0;
        else if(yLvlOffset > maxLvlOffsetY)
            yLvlOffset = maxLvlOffsetY;
        else if(yLvlOffset < 0)
            yLvlOffset = 0;

        //Test


    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH , Game.GAME_HEIGHT, null);
        drawBackgroundObj(g);
        
        levelManager.draw(g, xLvlOffset, yLvlOffset);
        player.render(g, xLvlOffset, yLvlOffset);
        enemyManager.draw(g, xLvlOffset, yLvlOffset);
        if(paused)
            pauseOverlay.draw(g);
    }

    private void drawBackgroundObj(Graphics g) {
        g.drawImage(background_house_big, 1300 - (int) (xLvlOffset * 0.3), 300 - (int) (yLvlOffset * 0.5), HOUSE_BIG_WIDTH, HOUSE_BIG_HEIGHT, null);
        for( int i = 0; i < housePos.length; i++) {
            g.drawImage(background_house, HOUSE_WIDTH * 4 * i - (int) (xLvlOffset * 0.6), housePos[i] - (int) (yLvlOffset * 0.6), HOUSE_WIDTH, HOUSE_HEIGHT, null);
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            player.setAttack(true);

    }
    public void mouseDragged(MouseEvent e) {
        if(paused)
            pauseOverlay.mouseDragged(e);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(paused)
            pauseOverlay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused)
            pauseOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //General keys
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
            case KeyEvent.VK_W:
                player.setJump(true);
                break;
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setJump(false);
                break;
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
        }
    }
    public void unpauseGame() {
        paused = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void  windowFocusLost(){
        player.resetDirBooleans();
    }

}
