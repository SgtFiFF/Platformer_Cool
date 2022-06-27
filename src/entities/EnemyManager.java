package entities;

import gamestates.Playing;
import utiz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utiz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] stalkerArr;
    private ArrayList<Stalker> stalkerAll = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        stalkerAll = LoadSave.GetStalker();
    }

    public void update(int[][] lvlData, Player player) {
        for(Stalker s : stalkerAll)
            s.update(lvlData, player);
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawStalks(g, xLvlOffset, yLvlOffset);
    }

    private void drawStalks(Graphics g, int xLvlOffset, int yLvlOffset){
        for(Stalker s : stalkerAll)
            g.drawImage(stalkerArr[s.getEnemyState()][s.getAniIndex()], (int)s.getHitbox().x - xLvlOffset - STALKER_DRAWOFFSET_X,(int)s.getHitbox().y - yLvlOffset - STALKER_DRAWOFFSET_Y,STALKER_WIDTH, STALKER_HEIGHT, null);
    }


    private void loadEnemyImgs() {
        stalkerArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_STALKER);
        for(int j = 0; j < stalkerArr.length; j++)
            for( int i = 0; i < stalkerArr[j].length; i++ )
                stalkerArr[j][i] = temp.getSubimage(i * STALKER_WIDTH_DEFAULT, j * STALKER_HEIGHT_DEFAULT, STALKER_WIDTH_DEFAULT, STALKER_HEIGHT_DEFAULT);
    }
}
