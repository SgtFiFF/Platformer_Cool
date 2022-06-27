package entities;

import gamestates.Playing;
import main.Game;

import static utiz.Constants.Directions.LEFT;
import static utiz.Constants.EnemyConstants.*;
import static utiz.HelpMethods.*;

public class Stalker extends Enemy{

    public Stalker(float x, float y) {
        super(x, y, STALKER_WIDTH, STALKER_HEIGHT, STALKER);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }


    private void updateMove(int[][] lvlData, Player player){
        if(firstUpdate)
            firstUpdateCheck(lvlData);

        if(inAir)
            updateInAir(lvlData);
        else {
            switch(enemyState) {
                case IDLE:
                    stateChange(RUNNING);
                    break;
                case RUNNING:
                    if(playerVisibleCheck(lvlData, player ))
                        turnToPlayer(player);
                    if(playerInRangeToAtk(player))
                        stateChange(ATTACK);

                        running(lvlData);
                    break;
            }

        }



    }
}
