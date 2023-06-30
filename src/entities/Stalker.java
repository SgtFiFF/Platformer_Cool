package entities;

//import gamestates.Playing;
import main.Game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static utiz.Constants.Directions.*;
import static utiz.Constants.EnemyConstants.*;
import static utiz.HelpMethods.*;

public class Stalker extends Enemy {

    //hitbox for attack
    private Rectangle2D.Float attackHitbox;
    private int attackHitboxOffsetX;

    public Stalker(float x, float y) {
        super(x, y, STALKER_WIDTH, STALKER_HEIGHT, STALKER);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
        initAttackHitbox();

    }

    private void initAttackHitbox() {
        attackHitbox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackHitboxOffsetX = (int) (Game.SCALE * 30);
        updateAttackHitbox();

    }

    private void updateAttackHitbox() {
        attackHitbox.x = hitbox.x - attackHitboxOffsetX;
        attackHitbox.y = hitbox.y;
    }

    public void update(int[][] lvlData, ArrayList<Player> players) {
        updateInteractions(lvlData, players);
        updateAnimationTick();

}

    private void updateInteractions(int[][] lvlData, ArrayList<Player> players){

            if (firstUpdate)
                firstUpdateCheck(lvlData);

            if (inAir)
                updateInAir(lvlData);
            else {
                switch (enemyState) {
                    case IDLE:
                        stateChange(RUNNING);
                        break;
                    case RUNNING:
                        for (Player player : players) {
                        if (playerVisibleCheck(lvlData, players)) {
                            turnToPlayer(players);

                                if (playerInRangeToAtk(player))
                                    stateChange(ATTACK);
                            }
                        }
                        running(lvlData);
                        break;
                    case ATTACK:
                        if (aniIndex == 0)
                            attackChecker = false;
                        if (aniIndex == 4 && !attackChecker)
                            enemyHitChecker(attackHitbox, players);
                        break;
                    case HIT:
                        break;
                }

            }

    }

    public int flipX(){
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }
    public int flipW() {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
