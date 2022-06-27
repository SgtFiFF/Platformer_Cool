package gamestates;

import main.Game;
import ui.MenuButton;
import ui.MenuBackground;
import utiz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utiz.Constants.PlayerConstants.GetSpriteAmount;

public class Menu extends State implements Statemethods{
    private MenuButton[] buttons = new MenuButton[4];               //Number off menu buttons ... needs change if the amount of buttons changes

    private BufferedImage[] menuanimations;
    private BufferedImage menubackground;
    private float animationIndex  = 0;
    private int animationTick,animationSpeed = 30;                  // animation speed  Higher = slower
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadMenuAnimations();
        updateMenuAnimations();
        menubackground = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);

    }

    private void loadMenuAnimations() {
        BufferedImage background = LoadSave.GetSpriteAtlas(LoadSave.MENU_SPRITES);

        menuanimations = new BufferedImage[4];
        for (int j = 0; j < menuanimations.length; j++)
            menuanimations[j]= background.getSubimage(j*150, 0, 150, 300);

        menuWidth = (int) (150  * 1.5 * Game.SCALE );
        menuHeight = (int) (300   * 1.5 * Game.SCALE );
        menuX =  (Game.GAME_WIDTH / 2 ) - (menuWidth /2);
        menuY =  (int)( Game.SCALE);
    }
    private void updateMenuAnimations() {
        animationTick ++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex ++;

            if (animationIndex >= menuanimations.length) {
                animationIndex = 0;
            }
        }
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH /2, (int) ((100 -45)  * Game.SCALE), 0, GameState.PLAYING);
        buttons[2] = new MenuButton(Game.GAME_WIDTH /2, (int) ((170 -45) * Game.SCALE), 2, GameState.SECONDPLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH /2 , (int) ((240  -45) * Game.SCALE), 1, GameState.OPTIONS);
        buttons[3] = new MenuButton(Game.GAME_WIDTH /2 , (int) ((310 -45)   * Game.SCALE), 3, GameState.QUIT);
    }



    @Override
    public void update() {
        updateMenuAnimations();
        for(MenuButton mb : buttons)
            mb.update();

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(menubackground,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT, null);

        g.drawImage(menuanimations[(int) animationIndex], menuX, menuY, menuWidth,menuHeight,null );
        for(MenuButton mb : buttons)
            mb.draw(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isIn(e, mb)){
                mb.setMousePressed(true);
                break;
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isIn(e, mb)){
                if(mb.isMouseOver())
                    mb.applyGamestate();
            }
        }
        resetButtons();

    }

    private void resetButtons() {
       for(MenuButton mb : buttons) {
           mb.resetBoolean();
       }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
            mb.setMouseOver(false);

        for(MenuButton mb : buttons)
            if(isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            GameState.state = GameState.PLAYING;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
