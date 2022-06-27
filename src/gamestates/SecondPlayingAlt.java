//package gamestates;
//
//import Levels.LevelManager;
//import entities.Player;
//import entities.Second_Player;
//import main.Game;
//import ui.PauseOverlay;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseEvent;
//
//public class SecondPlayingAlt extends State implements Statemethods{
//        private Player player;
//        private Second_Player secondPlayer;
//        private LevelManager levelManager;
//        private PauseOverlay pauseOverlay;
//        private boolean paused = false;
//
//        public SecondPlayingAlt(Game game) {
//            super(game);
//            initClasses();
//        }
//
//        private void initClasses() {
//            levelManager = new LevelManager(game);
//            player = new Player(200,200,(int) (64 * Game.SCALE),(int) (40 * Game.SCALE));
//            secondPlayer = new Second_Player(180,180,(int) (64 * Game.SCALE),(int) (40 * Game.SCALE));
//            player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
//            pauseOverlay = new PauseOverlay(this);
//
//
//        }
//        @Override
//        public void update() {
//            if(!paused) {
//                levelManager.update();
//                player.update();
//                secondPlayer.update();
//            }else {
//                pauseOverlay.update();
//            }
//
//        }
//
//        @Override
//        public void draw(Graphics g) {
//            levelManager.draw(g);
//            player.render(g);
//            secondPlayer.render(g);
//            if(paused)
//                pauseOverlay.draw(g);
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            if(e.getButton() == MouseEvent.BUTTON1)
//                secondPlayer.setAttack(true);
//
//        }
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            if(paused)
//                pauseOverlay.mousePressed(e);
//
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            if(paused)
//                pauseOverlay.mouseReleased(e);
//
//        }
//
//        @Override
//        public void mouseMoved(MouseEvent e) {
//            if(paused)
//                pauseOverlay.mouseMoved(e);
//
//        }
//
//        @Override
//        public void keyPressed(KeyEvent e) {
//
//            switch (e.getKeyCode()) {
//                case KeyEvent.VK_W:
//                    player.setJump(true);
//                    break;
//                case KeyEvent.VK_A:
//                    player.setLeft(true);
//                    break;
//                case KeyEvent.VK_D:
//                    player.setRight(true);
//                    break;
//                case KeyEvent.VK_SPACE:
//                    player.setAttack(true);
//                    break;
//                case KeyEvent.VK_ESCAPE:
//                    paused = !paused;
//                    break;
//                case KeyEvent.VK_UP:
//                    secondPlayer.setJump(true);
//                    break;
//                case KeyEvent.VK_LEFT:
//                    secondPlayer.setLeft(true);
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    secondPlayer.setRight(true);
//                    break;
//                case KeyEvent.VK_NUMPAD0:
//                    secondPlayer.setAttack(true);
//                    break;
//            }
//        }
//
//        @Override
//        public void keyReleased(KeyEvent e) {
//
//            switch (e.getKeyCode()) {
//                case KeyEvent.VK_W:
//                    player.setJump(false);
//                    break;
//                case KeyEvent.VK_A:
//                    player.setLeft(false);
//                    break;
//                case KeyEvent.VK_D:
//                    player.setRight(false);
//                    break;
//                case KeyEvent.VK_UP:
//                    secondPlayer.setJump(false);
//                    break;
//                case KeyEvent.VK_LEFT:
//                    secondPlayer.setLeft(false);
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    secondPlayer.setRight(false);
//                    break;
//            }
//
//        }
//        public Second_Player getSecondPlayer() {
//            return secondPlayer;
//        }
//        public Player getPlayer() {
//            return player;
//        }
//
//        public void  windowFocusLost(){
//            player.resetDirBooleans();
//            secondPlayer.resetDirBooleans();
//        }
//
//    }
//
//
//