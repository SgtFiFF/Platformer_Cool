package Levels;

import gamestates.GameState;
import main.Game;
import utiz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.LVL_SPRITE_GR;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private  int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if(lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("no more levels for u to explore");
            GameState.state = GameState.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getSecondplaying().getEnemyManager().loadEnemies(newLevel);
        game.getSecondplaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getSecondplaying().getSecondPlayer().loadLvlData(newLevel.getLvlData());
        game.getSecondplaying().setMaxLevelOffset(newLevel.getLvlOffsetX(), newLevel.getLvlOffsetY());
        game.getSecondplaying().getObjectManager().loadObjects(newLevel);

    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevels)
            levels.add(new Level(img));
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
        for( int j = 0; j < levels.get(lvlIndex).getLvlData().length; j++)
            for(int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++){
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xlvloffset,Game.TILES_SIZE * j - yLvlOffset, Game.TILES_SIZE, Game.TILES_SIZE,null);
            }


    }
    public void update() {

    }

    public Level getCurrentLevel() { //Getter for current Level we are in => usable for Collision
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}
