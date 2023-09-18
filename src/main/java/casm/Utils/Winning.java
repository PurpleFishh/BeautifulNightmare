package casm.Utils;

import casm.ECS.Components.Collision.Rectangle;
import casm.Game;
import casm.Scenes.Level.LevelSaverLoader;
import casm.Scenes.Level.LeveleScene;

import java.sql.SQLException;

public class Winning {
    private static Winning instance = null;

    private Winning() {
    }

    public static Winning getInstance() {
        if (instance == null)
            instance = new Winning();
        return instance;
    }


    /**
     * If the game was won, it verify if the player is in contact with the door that will send him to the next level
     *
     * @param playerCollider rectangle collider of the player
     */
    public void verifyIfGameWon(Rectangle playerCollider) {
        if (Game.getCurrentScene().isEmpty())
            return;
        if (Game.getCurrentScene().get() instanceof LeveleScene)
            if (((LeveleScene) Game.getCurrentScene().get()).isWon()) {
                for (Rectangle winCollider : ((LeveleScene) Game.getCurrentScene().get()).getWinColliders()) {
                    if (playerCollider.intersects(winCollider)) {
                        try {
                            LevelSaverLoader.getInstance().saveHighScore();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        Game.changeLevel(((LeveleScene) Game.getCurrentScene().get()).getLevel() + 1, false);
                        break;
                    }
                }
            }
    }
}
