package Levels;

import entities.Stalker;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import utiz.HelpMethods;
import utiz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static utiz.HelpMethods.GetLevelData;
import static utiz.HelpMethods.GetStalker;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Stalker> stalkers;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private int lvlTilesWide ;
    private int lvlTilesHeight ;
    private int maxTilesOffset ;
    private int maxTilesOffsetY ;
    private int maxLvlOffsetY ;
    private int maxLvlOffsetX;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainer();
        calculateLevelOffset();

    }

    private void createContainer() {
        containers = HelpMethods.GetContainers(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    private void calculateLevelOffset() {
        lvlTilesWide = img.getWidth();
        lvlTilesHeight = img.getHeight();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxTilesOffsetY =  lvlTilesHeight - Game.TILES_IN_HEIGHT;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
        maxLvlOffsetY = Game.TILES_SIZE * maxTilesOffsetY;

    }

    private void createEnemies() {
        stalkers = GetStalker(img);

    }

    private void createLevelData() {
        lvlData = GetLevelData(img);

    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][]  getLvlData() {
        return lvlData;
    }
    public int getLvlOffsetX() {
        return maxLvlOffsetX;
    }
    public int getLvlOffsetY() {
        return maxLvlOffsetY;
    }
    public ArrayList<Stalker> getStalkers() {
        return stalkers;
    }
    public ArrayList<Potion> getPotions() {
        return potions;
    }
    public ArrayList<GameContainer> getContainers() {
        return containers;
    }
}
