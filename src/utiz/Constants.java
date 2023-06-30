package utiz;

import main.Game;

public class Constants {

    public static class EnemyConstants{
        public static final int STALKER = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int STALKER_WIDTH_DEFAULT = 72;
        public static final int STALKER_HEIGHT_DEFAULT = 32;
        public static final int STALKER_WIDTH = (int)(STALKER_WIDTH_DEFAULT * Game.SCALE);
        public static final int STALKER_HEIGHT = (int)(STALKER_HEIGHT_DEFAULT * Game.SCALE);

        // Texture offset
        public static final int STALKER_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int STALKER_DRAWOFFSET_Y = (int) (9 * Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case STALKER:
                switch (enemy_state) {
                    case IDLE:
                        return 9;
                    case RUNNING:
                        return 6;
                    case ATTACK:
                        return 7;
                    case HIT:
                        return 4;
                    case DEAD:
                        return 5;
                }
            }
            return 0;
        }

        public static int getMaxHealth(int enemy_type){
            switch(enemy_type){
                case STALKER:
                    return 10;
                default:
                    return 10;
            }
        }

        public static int getEnemyDmg(int enemy_type) {
            switch(enemy_type){
                case STALKER:
                    return 2;
                default:
                    return 0;
            }
        }
    }

    public static class Background{
        public static final int HOUSE_WIDTH_DEFAULT = 50;
        public static final int HOUSE_HEIGHT_DEFAULT = 100;
        public static final int HOUSE_BIG_WIDTH_DEFAULT = 100;
        public static final int HOUSE_BIG_HEIGHT_DEFAULT = 120;

        public static final int HOUSE_WIDTH = (int)(HOUSE_WIDTH_DEFAULT * Game.SCALE * 3);
        public static final int HOUSE_HEIGHT = (int)(HOUSE_HEIGHT_DEFAULT * Game.SCALE * 3);
        public static final int HOUSE_BIG_WIDTH = (int)(HOUSE_BIG_WIDTH_DEFAULT * Game.SCALE * 2);
        public static final int HOUSE_BIG_HEIGHT = (int)(HOUSE_BIG_HEIGHT_DEFAULT * Game.SCALE * 2);
    }
    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 64;
            public static final int B_HEIGHT_DEFAULT = 24;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE * 2.5);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE * 2.5);
        }
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }
        public static class URMButtons {
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE = (int) (URM_SIZE_DEFAULT * Game.SCALE);
        }
        public static class VolumeButtons{
            public static final int VOLUME_HEIGHT_DEFAULT = 44;
            public static final int VOLUME_WIDTH_DEFAULT = 28;
            public static final int VOLUME_SLIDER_DEFAULT = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_WIDTH_DEFAULT * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_HEIGHT_DEFAULT * Game.SCALE);
            public static final int VOLUME_SLIDER = (int) (VOLUME_SLIDER_DEFAULT * Game.SCALE);
        }
    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        public static final int IDLE = 0;
        public static final int FALL = 1;
        public static final int RUN = 2;
        public static final int JUMP = 3;
        public static final int ATK_1 = 4;
        public static final int DEATH = 6;
        public static final int DMG = 7;



        public static int GetSpriteAmount(int player_action) {

            switch(player_action) {

                case IDLE:
                    return 8;
                case FALL:
                    return 3;
                case RUN:
                    return 6;
                case JUMP:
                case DEATH:
                case DMG:
                    return 4;
                case ATK_1:
                    return 7;
                default:
                    return 1;

            }
        }


    }
}
