package utiz;

import entities.Stalker;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static main.Game.LVL_SPRITE_GR;
import static utiz.Constants.EnemyConstants.STALKER;

public class LoadSave {
    public static final String PLAYER_ATLAS = "Game_char_tile.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String BUTTON_SPRITES = "Button_Sprites.png";
    public static final String MENU_SPRITES = "Menu_sprites.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String LEVEL_BACKGROUND = "backgr_level.png";
    public static final String BACKGROUND_HOUSE = "backgr_house.png";
    public static final String BACKGROUND_HOUSE_BIG = "background_house_big.png";
    public static final String ENEMY_STALKER = "enemy_stalker_sprite.png";
    public static final String STATUS_BAR = "status_bar.png";
    public static final String LEVEL_FINISHED = "level_complete_screen.png";

    public static BufferedImage GetSpriteAtlas(String filename) {
        BufferedImage img = null;                           // null => sonst maybe stuck im try and catch
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        try {                                               //falls res nicht vorhanden
            img = ImageIO.read(is);

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{                                               //inputstream closing
                is.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];

            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }




}
