package utiz;

import entities.Stalker;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static main.Game.LVL_SPRITE_GR;
import static utiz.Constants.EnemyConstants.STALKER;

public class LoadSave {
    public static final String PLAYER_ATLAS = "Game_char_tile.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long_deep.png";
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

    public static ArrayList<Stalker> GetStalker(){
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Stalker> list = new ArrayList<>();

        for(int j =0; j < img.getHeight(); j++)
            for(int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if(value == STALKER)
                    list.add(new Stalker(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static int[][] GetLevelData() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for(int j =0; j < img.getHeight(); j++)
            for(int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if(value >= LVL_SPRITE_GR-1)                     //so you don't get error if ur file has more than 48 red colors
                    value = 0;
                lvlData[j][i] = value;
            }
        return lvlData;

    }
}
