package entities;


import main.Game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecker;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
    }
    public void hurt(int amount) {
        currentHealth -= amount;
        if(currentHealth <= 0) {
            currentHealth = 0;
            stateChange(DEAD);
        }else
            stateChange(HIT); //not gonna be in the finished game ... no time sry ... always onehit
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
    protected void turnToPlayer(ArrayList<Player> players) {
        for(Player player : players) {
            if (player.hitbox.x > hitbox.x)
                walkDir = RIGHT;
            else
                walkDir = LEFT;
        }
    }

    protected boolean playerVisibleCheck(int[][] lvlData, ArrayList<Player> players) {
        for (Player player : players) {
            int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
            if (playerTileY == tileY)
                if (isPlayerInRange(player)) {
                    if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                        return true;
                }
        }
        return false;
    }
    protected boolean isPlayerInRange(Player player) {
            int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
            return absValue <= atkDistance * 4;

    }
    protected boolean playerInRangeToAtk(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= atkDistance;
    }
    protected void enemyHitChecker(Rectangle2D.Float attackHitbox, ArrayList<Player> players){
        for (Player player : players){
            if (attackHitbox.intersects(player.hitbox)) {
                player.changeHealth((-getEnemyDmg(enemyType)));
                attackChecker = true;
            }
        }
    }



    protected void updateAnimationTick(){
        aniTick ++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex ++;
            if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;

                switch (enemyState){
                    case ATTACK,HIT -> enemyState = IDLE;
                    case DEAD -> active = false;

                }
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
    public boolean isActive(){
        return active;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        stateChange(IDLE);
        active = true;
        fallspeed = 0;
    }
}
