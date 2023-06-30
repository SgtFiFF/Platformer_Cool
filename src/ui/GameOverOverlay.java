package ui;

import gamestates.GameState;
import gamestates.SecondPlaying;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private SecondPlaying secondPlaying;

    public GameOverOverlay(SecondPlaying secondPlaying){
        this.secondPlaying = secondPlaying;
    }

    public void draw(Graphics g){
        g.setColor(new Color(30,0,0,200));
        g.fillRect(0,0 ,Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("You Died!", Game.GAME_WIDTH /2, Game.GAME_HEIGHT);
        g.drawString("Press Esc to get back to the Main Menu", Game.GAME_WIDTH /2, 300);

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            secondPlaying.resetAll();
            GameState.state = GameState.MENU;

        }

    }
}
