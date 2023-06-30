package entities;

//import gamestates.Playing;
import gamestates.SecondPlaying;
import utiz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utiz.Constants.EnemyConstants.*;

public class EnemyManager {

    //private Playing playing;
    private SecondPlaying secondPlaying;
    private BufferedImage[][] stalkerArr;
    private ArrayList<Stalker> stalkerAll = new ArrayList<>();

   // public EnemyManager(Playing playing) {
   //     this.playing = playing;
   //     loadEnemyImgs();
   //     addEnemies();
   // }
    public EnemyManager(SecondPlaying secondPlaying) {
        this.secondPlaying = secondPlaying;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        stalkerAll = LoadSave.GetStalker();
    }

    public void update(int[][] lvlData, ArrayList<Player> players) {
            for (Stalker s : stalkerAll)
                if (s.isActive())
                    s.update(lvlData, players);

    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawStalks(g, xLvlOffset, yLvlOffset);
    }

    private void drawStalks(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Stalker s : stalkerAll)
        if(s.isActive())
        {
            g.drawImage(stalkerArr[s.getEnemyState()][s.getAniIndex()],
                    (int) s.getHitbox().x - xLvlOffset - STALKER_DRAWOFFSET_X + s.flipX(),
                    (int) s.getHitbox().y - yLvlOffset - STALKER_DRAWOFFSET_Y,
                    STALKER_WIDTH * s.flipW(), STALKER_HEIGHT, null);
        }
    }
    public void enemyHitChecker(Rectangle2D.Float attackHitbox){
        for(Stalker s : stalkerAll)
            if(s.isActive()) {
                if (attackHitbox.intersects(s.getHitbox())) {
                    s.hurt(10);
                    return;
                }
            }
    }


    private void loadEnemyImgs() {
        stalkerArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_STALKER);
        for(int j = 0; j < stalkerArr.length; j++)
            for( int i = 0; i < stalkerArr[j].length; i++ )
                stalkerArr[j][i] = temp.getSubimage(i * STALKER_WIDTH_DEFAULT, j * STALKER_HEIGHT_DEFAULT, STALKER_WIDTH_DEFAULT, STALKER_HEIGHT_DEFAULT);
    }

    public void resetAllEnemys() {
        for(Stalker s : stalkerAll){
            s.resetEnemy();
        }
    }
}
