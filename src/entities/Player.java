package entities;

import gamestates.GameState;
//import gamestates.Playing;
import gamestates.SecondPlaying;
import main.Game;
import utiz.Constants;
import utiz.LoadSave;

import static utiz.HelpMethods.*;
import static utiz.Constants.PlayerConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Player extends Entity {
    private int animationTick, animationIndex; //Animation constants
    private BufferedImage[][] animations;
    private int playerAction = IDLE;
    public boolean moving = false;
    private boolean atk = false;
    private boolean left, up, right, down, jump;
    public float playerSpeed = 1.0f * Game.SCALE;
    public float xSpeed = 0;
    // Collision
    private int[][] lvlData;                                    //stores the level data for collision
    private float xDrawOffset = 18 * Game.SCALE;                //Offset der Hitbox in X
    private float yDrawOffset = 8 * Game.SCALE;                 //Offset der Hitbox in Y
    //Jumping
    private float airSpeed = 0f;                                // stores the playerspeed in midair
    private float jumpSpeed = -2.25f * Game.SCALE;              //speed of the Jump
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;  //fall speed after collision detection
    private boolean inAir = false;                              // self explaining
    //Statusbar
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (35 * Game.SCALE * 4);
    private int statusBarHeight = (int) (10 * Game.SCALE * 4);
    private int statusBarX = (int) (15 * Game.SCALE);
    private int statusBarY = (int) (400 * Game.SCALE);

    private int indicatorBarWidth = (int) (25 * Game.SCALE * 4);
    private int indicatorBarHeight = (int) (4 * Game.SCALE * 4);
    private int indicatorBarX = (int) (36 * Game.SCALE);
    private int indicatorBarY = (int) (12 * Game.SCALE);
    private int health = 20;
    private int currenHealth = health;
    private int indicatorWidth = indicatorBarWidth;

    //hitbox for attack
    private Rectangle2D.Float attackHitbox;

    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecker;
    private SecondPlaying secondPlaying;


    public Player(float x, float y, int width, int height, SecondPlaying secondPlaying) {
        super(x, y, width, height);
        this.secondPlaying = secondPlaying;
        loadAnimations();
        initHitbox(x, y, (int) (30 * Game.SCALE), (int) (30 * Game.SCALE)); //Einstellung der Hitbox
        initAttackHitbox();
    }

    //initialising the Hitbox of the Player
    private void initAttackHitbox(){
        attackHitbox = new Rectangle2D.Float(x, y,(int) (40 * Game.SCALE),(int)(30 * Game.SCALE));
    }

    public void update() {   //player update-loop
        updateHealth();
        if(currenHealth <= 0) {
            if(GameState.state == GameState.PLAYING)
            secondPlaying.setGameOver(true);
            else
                secondPlaying.setGameOver(true);
        return;
        }
        updateAttackHitbox();
        updatePos();
        if(moving)
            checkPotionTouched();
        if(atk)
            checkAttack();
        updateAnimation();
        setAnimation();

    }

    private void checkPotionTouched() {
        secondPlaying.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecker || animationIndex != 3)
            return;
        attackChecker = true;
        switch (GameState.state){
            case PLAYING:
                secondPlaying.enemyHitChecker(attackHitbox);
                secondPlaying.checkObjectHit(attackHitbox);
                break;
            case SECONDPLAYING:
                secondPlaying.enemyHitChecker(attackHitbox);
                secondPlaying.checkObjectHit(attackHitbox);
                break;

        }
    }

    private void updateAttackHitbox() {
        if(right){
            attackHitbox.x = hitbox.x + hitbox.width + (int) (Game.SCALE *10);
        }else if(left){
            attackHitbox.x = hitbox.x - hitbox.width - (int) (Game.SCALE *10);
        }
        attackHitbox.y = hitbox.y + (Game.SCALE *2);
    }

    private void updateHealth() {
                indicatorWidth = (int) ((currenHealth / (float) health) * indicatorBarWidth);
    }

    public void render(Graphics g, int xlvloffset, int ylvloffset, int pCount) {                    //Player Visual stuff

        g.drawImage(animations[playerAction][animationIndex],
                (int) (hitbox.x - xDrawOffset) - xlvloffset + flipX,
                (int) (hitbox.y - yDrawOffset) - ylvloffset,
                width * flipW, height, null);
        // drawHitbox(g, xlvloffset, ylvloffset);
        // drawAttackHitbox(g, xlvloffset, ylvloffset);
        drawUI(g, pCount);

    }

    private void drawAttackHitbox(Graphics g,int xlvloffset, int ylvloffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackHitbox.x -xlvloffset), (int)(attackHitbox.y - ylvloffset), (int)attackHitbox.width, (int)attackHitbox.height);
    }

    private void drawUI(Graphics g, int pCount) {
        int offset = 0;
        if(pCount == 1) {
            offset += 300;
        }
        g.drawImage(statusBarImg, statusBarX + offset, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(statusBarX + indicatorBarX + offset, statusBarY + indicatorBarY, indicatorWidth, indicatorBarHeight);
    }

    private void updateAnimation() {

        animationTick++;
        if (animationTick >= Constants.ANI_SPEED) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                atk = false;
                attackChecker = false;
            }
        }
    }

    private void setAnimation() {

        int startAnimation = playerAction;


        if (right || left)
            playerAction = RUN;
        else
            playerAction = IDLE;
        if (inAir)
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALL;


        if (atk) {
            playerAction = ATK_1;
            if(startAnimation != ATK_1){
                animationIndex = 3;
                animationTick = 0;
                return;
            }
        }
        if (startAnimation != playerAction) {
            resetAnimationTick();
        }

    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;

    }

    public void changeHealth(int value) {
        switch (GameState.state) {
            case PLAYING:
                currenHealth += value;

                if (currenHealth <= 0) {
                    currenHealth = 0;
                    //gameOver();
                } else if (currenHealth >= health)
                    currenHealth = health;
                break;
            case SECONDPLAYING:
                    currenHealth += value;

                    if (currenHealth <= 0) {
                        currenHealth = 0;
                        //gameOver();
                    } else if (currenHealth >= health)
                        currenHealth = health;
                }

    }

    private void updatePos() {

        moving = false;


        if (jump) {
            jump();
        }
        if (!inAir)
            if ((!left && !right) || (right && left))
                return;

        float xSpeed = 0;
            if (left) {
                xSpeed -= playerSpeed;
                flipX = width;
                flipW = -1;
            }
            if (right){
                xSpeed += playerSpeed;
                flipX = 0;
                flipW = 1;
        }
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += Constants.GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        moving = true;


    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void updateXPos(float xSpeed) {

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }


    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[8][8];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttack(boolean atk) {
        this.atk = atk;



    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        playerAction = IDLE;
        currenHealth = health;
        hitbox.x = x;
        hitbox.y = y;

        atk = false;
        moving = false;
        inAir = false;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;

    }
}

