package casm.Utils.Settings;

/**
 * Constants used for entities
 */
public class EntitiesSettings {
    public static final double MAX_SPEED = 0.1;
    public static final double MAX_JUMP = 0.3;
    public static final double GRAVITY = 0.3;
    public static final double MAX_CLIMBING_SPEED = 0.27;
    public static final double LAVA_DAMAGE = 2;

    /**
     * Constants used for the player
     */
    public static class PlayerInfo {
        public static final double PLAYER_MAX_SPEED = 0.2;
        public static final double PLAYER_MAX_JUMP_PARKOUR = 0.33;
    }

    /**
     * Constants used for the weasel enemy
     */
    public static class WeaselInfo {
        public static final double WEASEL_MAX_SPEED = 0.17;
    }

    /**
     * Constants used for the catfish enemy
     */
    public static class CatfishInfo {
        public static final double CATFISH_MAX_SPEED = 0.19;
    }
    /**
     * Constants used for the turtle king enemy
     */
    public static class TurtleInfo {
        public static final double TURTLE_MAX_SPEED = 0.15;
    }
}
