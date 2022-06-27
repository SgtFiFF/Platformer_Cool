package entities;


import main.Game;

import static utiz.Constants.EnemyConstants.*;
import static utiz.Constants.Directions.*;
import static utiz.HelpMethods.*;
public abstract class Enemy extends Entity {

    // protected bec used in Stalker class
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallspeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.7f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float atkDistance = 2 * Game.TILES_SIZE;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }
    protected void stateChange(int enemyState) {
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData){
            if(CanMoveHere(hitbox.x + fallspeed, hitbox.y + fallspeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += fallspeed;
                fallspeed += gravity;
            }else{
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofAboveFloor(hitbox, fallspeed);
                tileY = (int) (hitbox.y / Game.TILES_SIZE);
            }
    }
    protected void running(int[][] lvlData){
        float xSpeed = 0;

        if(walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }
    protected void turnToPlayer(Player player) {
        if(player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean playerVisibleCheck(int[][] lvlData, Player player) {
        int PlayerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (isPlayerInRange(player)) {
            if(IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                return true;
        }

        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= atkDistance * 4;
    }
    protected boolean playerInRangeToAtk(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= atkDistance ;
    }



    protected void updateAnimationTick(){
        aniTick ++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex ++;
            if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            if(enemyState == ATTACK)
                enemyState = IDLE;
            }

        }
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    public int getAniIndex() {
        return aniIndex;
    }
    public int getEnemyState(){
        return enemyState;
    }
}
