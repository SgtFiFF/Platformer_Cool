package gamestates;

import Levels.LevelManager;
import entities.Player;
import entities.Second_Player;
import main.Game;
import ui.PauseOverlay;
import utiz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SecondPlaying extends State implements Statemethods{
        private Player player;
        private Second_Player secondPlayer;
        private LevelManager levelManager;
        private PauseOverlay pauseOverlay;
        private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;

    // testing
    private int yLvlOffset;
    private int topBorder = (int)(0.2 * Game.GAME_HEIGHT);
    private int botBorder = (int)(0.8 * Game.GAME_HEIGHT);
    private int lvlTilesHeight = LoadSave.GetLevelData().length;
    private int maxTilesOffsetY = lvlTilesHeight - Game.TILES_IN_HEIGHT;
    private int maxLvlOffsetY = maxTilesOffsetY * Game.TILES_SIZE;


    public SecondPlaying(Game game) {
            super(game);
            initClasses();
        }

        private void initClasses() {
            levelManager = new LevelManager(game);
            player = new Player(200,200,(int) (64 * Game.SCALE),(int) (40 * Game.SCALE));
            secondPlayer = new Second_Player(180,180,(int) (64 * Game.SCALE),(int) (40 * Game.SCALE));
            player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
            secondPlayer.loadLvlData(levelManager.getCurrentLevel().getLvlData());
            pauseOverlay = new PauseOverlay(this);


        }
        @Override
        public void update() {
            if(!paused) {
                levelManager.update();
                player.update();
                secondPlayer.update();
                checkCloseToBorder();
            }else {
                pauseOverlay.update();
            }

        }
        @Override
        public void draw(Graphics g) {
            levelManager.draw(g, xLvlOffset, yLvlOffset);
            player.render(g,xLvlOffset, yLvlOffset);
            secondPlayer.render(g, xLvlOffset,yLvlOffset);
            if(paused)
                pauseOverlay.draw(g);
        }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int playerY = (int) player.getHitbox().y;
        int diffX = playerX - xLvlOffset;
        int diffY = playerY - yLvlOffset;

        int secondPlayerY = (int) secondPlayer.getHitbox().y;
        int secondPlayerX = (int) secondPlayer.getHitbox().x;
        int secDiffX = secondPlayerX - xLvlOffset;
        int secDiffY = secondPlayerY - yLvlOffset;

        //border checker
        if(diffX > rightBorder)
            xLvlOffset += diffX - rightBorder;
        else if( diffX < leftBorder)
            xLvlOffset += diffX - leftBorder;
        else if(diffY < topBorder)
            yLvlOffset += diffY - topBorder;
        else if( diffY > botBorder)
            yLvlOffset += diffY - botBorder;
        else if(secDiffX > rightBorder)
            xLvlOffset += diffX - leftBorder;
        else if( secDiffX < leftBorder)
            xLvlOffset += diffX - rightBorder;



      // if (diffX > rightBorder && secDiffX -leftBorder <= 0) {
      //     player.setCanMoveRight(false);
      // }else if (diffX > rightBorder ) {
      //     xLvlOffset += diffX - rightBorder;
      //     player.setCanMoveRight(true);
      // }
      // if (diffX < leftBorder && secDiffX -rightBorder >= 0) {
      //     player.setCanMoveLeft(false);
      // }else if (diffX < leftBorder ) {
      //     xLvlOffset += diffX - leftBorder;
      //     player.setCanMoveLeft(true);

      // }else
      //     return;



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


        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1)
                secondPlayer.setAttack(true);

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

            switch (e.getKeyCode()) {
                //General keys
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
                    //First Player
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setAttack(true);
                    break;

                    //Second Player
                case KeyEvent.VK_UP:
                    secondPlayer.setJump(true);
                    break;
                case KeyEvent.VK_LEFT:
                    secondPlayer.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    secondPlayer.setRight(true);
                    break;
                case KeyEvent.VK_NUMPAD0:
                    secondPlayer.setAttack(true);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            switch (e.getKeyCode()) {
                //First Player
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                    //Second Player
                case KeyEvent.VK_UP:
                    secondPlayer.setJump(false);
                    break;
                case KeyEvent.VK_LEFT:
                    secondPlayer.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    secondPlayer.setRight(false);
                    break;
            }

        }
    public void unpauseGame() {
        paused = false;
    }
        public Second_Player getSecondPlayer() {
            return secondPlayer;
        }
        public Player getPlayer() {
            return player;
        }

        public void  windowFocusLost(){
            player.resetDirBooleans();
            secondPlayer.resetDirBooleans();
        }

    }


