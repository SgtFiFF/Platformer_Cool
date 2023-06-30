package ui;

import gamestates.GameState;
import gamestates.SecondPlaying;
import main.Game;
import utiz.Constants;
import utiz.LoadSave;
import static utiz.Constants.UI.URMButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelFinishoverlay {

    private SecondPlaying secondPlaying;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public LevelFinishoverlay(SecondPlaying secondPlaying) {
        this.secondPlaying = secondPlaying;
        initImg();
        initButtons();

    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_FINISHED);
        bgH = (int) (img.getHeight() * Game.SCALE *2);
        bgW = (int) (img.getWidth() * Game.SCALE *2);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g) {

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }
    public void update() {
        next.update();
        menu.update();

    }
    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(),e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }
    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                secondPlaying.resetAll();
                GameState.state = GameState.MENU;
            }
        } else if (isIn(next, e))
            if (next.isMousePressed())
                secondPlaying.loadNextLevel();

        menu.resetBools();
        next.resetBools();
    }
    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }
}
