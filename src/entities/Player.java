package entities;

import main.Game;
import utiz.LoadSave;
import static utiz.HelpMethods.*;
import static utiz.Constants.PlayerConstants.*;
import java.awt.*;
import java.awt.image.BufferedImage;




public class Player  extends Entity {
    private int animationTick, animationIndex, animationSpeed = 20; //Animation Speed
    private BufferedImage[][] animations;
    private int playerAction = IDLE;
    public boolean movingRight = false;
    public boolean movingLeft = false;
    public boolean canMoveRight = true;
    public boolean canMoveLeft = true;
    private boolean atk = false;
    private boolean left, up, right, down, jump;
    public float playerSpeed =  1.0f * Game.SCALE;
    public float xSpeed = 0;
    // Collision
    private int[][] lvlData;                                    //stores the level data for collision
    private float xDrawOffset = 18 * Game.SCALE;                //Offset der Hitbox in X
    private float yDrawOffset = 8 * Game.SCALE;                 //Offset der Hitbox in Y
    // Gravity and Jumping
    private float airSpeed = 0f;                                // stores the playerspeed in midair
    private float gravity = 0.02f * Game.SCALE;                  // how fast the player will fall TODO: Jumpboost stat
    private float jumpSpeed = -2.25f * Game.SCALE;              //speed of the Jump
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;  //fall speed after collision detection
    private boolean inAir = false;                              // self explaining


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y,(int) (30*Game.SCALE), (int)(30*Game.SCALE)); //Einstellung der Hitbox
    }

    public void update(){                                                   //player update-loop
        updatePos();
        updateAnimation();
        setAnimation();

    }

    public void render(Graphics g, int xlvloffset, int ylvloffset) {                    //Player Visual stuff
        g.drawImage(animations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset) - xlvloffset, (int) (hitbox.y - yDrawOffset) - ylvloffset,width,height, null );
        //drawHitbox(g);

    }

    private void updateAnimation() {

        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                atk = false;
                if(animationSpeed != 20){
                    animationSpeed = 20;
                }
            }
        }
    }

    private void setAnimation() {

        int startAnimation = playerAction;


        if (right)
            playerAction = RUNRIGHT;
        else if (left)
            playerAction = RUNLEFT;
        else
            playerAction = IDLE;
        if (inAir)
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALL;


        if(atk)
            playerAction = ATK_1;

        if(startAnimation != playerAction) {
            resetAnimationTick();
        }

    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;

    }

    private void updatePos() {

        movingRight = false;
        movingLeft = false;

        if (jump){
            jump();
        animationSpeed = 20;
        }
     // if (!left && !right && !inAir) // TODO: Double jump
     //     return;
        if(!inAir)
            if((!left && !right) || (right && left) )
                return;

        xSpeed = 0;
        if(canMoveLeft) {
            if (left) {
                xSpeed -= playerSpeed;
            }
        }if(canMoveRight) {
            if (right)
                xSpeed += playerSpeed;
        } else
            return;
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity ;
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
        movingLeft = true;
        movingRight = true;

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
                for(int i=0;i < animations[j].length; i++ )
                    animations[j][i]= img.getSubimage(i*64, j*40, 64, 40);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
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
        animationSpeed = 15;


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

    public boolean isCanMoveRight() {
        return canMoveRight;
    }

    public void setCanMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
    }
    public boolean isCanMoveLeft() {
        return canMoveLeft;
    }

    public void setCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }
}

