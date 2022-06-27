package Levels;

import main.Game;
import utiz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.LVL_SPRITE_GR;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[LVL_SPRITE_GR];
        for(int j = 0; j < LVL_SPRITE_GR/12 ; j++)
            for(int i = 0; i < 12; i++) {
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32,j*32,32,32);
            }
    }

    public void draw(Graphics g, int xlvloffset, int yLvlOffset) {
        for( int j = 0; j < levelOne.getLvlData().length; j++)
            for(int i = 0; i < levelOne.getLvlData()[0].length; i++){
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xlvloffset,Game.TILES_SIZE * j - yLvlOffset, Game.TILES_SIZE, Game.TILES_SIZE,null);
            }


    }
    public void update() {

    }

    public Level getCurrentLevel() { //Getter for current Level we are in => usable for Collision
        return levelOne;
    }
}
