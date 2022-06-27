package ui;

import gamestates.GameState;
import gamestates.Playing;
import gamestates.SecondPlaying;
import main.Game;
import utiz.LoadSave;

import static utiz.Constants.UI.PauseButtons.*;
import static utiz.Constants.UI.URMButtons.*;
import static utiz.Constants.UI.VolumeButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay {
    private Playing playing;
    private SecondPlaying secondplaying;
    private BufferedImage backgroundImg;
    private int bgX,bgY,bgW,bgH;
    private SoundButton musicButton, sfxButton;
    private UrmButton menuB, replayB, unpauseB;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButtons();

    }
    public PauseOverlay(SecondPlaying secondPlaying) {
        this.secondplaying = secondPlaying;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButtons();

    }

    private void createVolumeButtons() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, VOLUME_SLIDER, VOLUME_HEIGHT);

    }



    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgH = (int)(backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH /2 -bgW / 2;
        bgY = Game.GAME_HEIGHT /2 -bgH / 2;

    }
    private void createSoundButtons() {
        int soundX = (int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createUrmButtons() {
        int menuX = (int)(313 * Game.SCALE);
        int replayX = (int)(387 * Game.SCALE);
        int unpauseX = (int)(462 * Game.SCALE);
        int urmY = (int)(325 * Game.SCALE);

        menuB = new UrmButton(menuX, urmY, URM_SIZE, URM_SIZE,2);
        replayB = new UrmButton(replayX, urmY, URM_SIZE, URM_SIZE,1);
        unpauseB = new UrmButton(unpauseX, urmY, URM_SIZE, URM_SIZE,0);

    }




    public void update() {
        musicButton.update();
        sfxButton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();

    }
    protected void drawBackgr(Graphics g) {
        Color C = new Color(20, 20, 30, 200);
        g.setColor(C);
        g.fillRect(0,0,(int) (Game.GAME_WIDTH * Game.SCALE) ,(int) (Game.GAME_HEIGHT * Game.SCALE));

    }

    public void draw(Graphics g) {
        drawBackgr(g);

        //Backgr.
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        //Pause Menu Buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        //Volume Slider
        volumeButton.draw(g);

    }


    public void mouseDragged(MouseEvent e){
        if(volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }

    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e,musicButton))
            musicButton.setMousePressed(true);
        else if(isIn(e,sfxButton))
                sfxButton.setMousePressed(true);
        else if(isIn(e,menuB))
            menuB.setMousePressed(true);
        else if(isIn(e,replayB))
            replayB.setMousePressed(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMousePressed(true);
        else if(isIn(e,volumeButton))
            volumeButton.setMousePressed(true);

    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e,musicButton)) {
            if(musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if(isIn(e,sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }else if(isIn(e,menuB)) {
            if (menuB.isMousePressed()) {
                if(GameState.state == GameState.PLAYING){
                    playing.unpauseGame();
                }
                else {secondplaying.unpauseGame();
                }
                GameState.state = GameState.MENU;
            }
        }else if(isIn(e,replayB)) {
            if (replayB.isMousePressed()) {
                System.out.println("Replaying! oder so");
            }
        }
        else if(isIn(e,unpauseB)) {
            if (unpauseB.isMousePressed()) {
                if(GameState.state == GameState.PLAYING){
                    playing.unpauseGame();
                }
                else if(GameState.state == GameState.SECONDPLAYING){
                    secondplaying.unpauseGame();
                }
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if(isIn(e,musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e,sfxButton))
            sfxButton.setMouseOver(true);
        else if(isIn(e,menuB))
            menuB.setMouseOver(true);
        else if(isIn(e,replayB))
            replayB.setMouseOver(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMouseOver(true);
        else if(isIn(e,volumeButton))
            volumeButton.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
