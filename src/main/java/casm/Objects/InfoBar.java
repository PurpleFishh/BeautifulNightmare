package casm.Objects;

import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Scenes.Scene;
import casm.Utils.Vector2D;

import java.util.ArrayList;

/**
 * Object used to display information on the screen(player stats).<br>
 * It displays multiple text objects on below others.
 */
public class InfoBar extends Object {

    /**
     * <b>healthInfo</b> - The text object that displays the health of the player.
     * <b>scoreInfo</b> - The text object that displays the score of the player.
     */
    private TextObject healthInfo, scoreInfo;
    /**
     * The template for the health bar
     */
    private final String healthInfoTemplate = "Health: ";
    /**
     * The template for the score bar
     */
    private final String scoreInfoTemplate = "Score: ";

    /**
     * @param scene         - The scene where the object will be displayed.
     * @param layer         - The layer where the object will be displayed, see layers from {@link Scene}
     * @param spawnPosition - The position where the object will be displayed.
     */
    public InfoBar(Scene scene, int layer, Vector2D spawnPosition) {
        super("Info Bar", spawnPosition);
        initialize(scene, layer, spawnPosition);
    }

    /**
     * @param scene         - The scene where the object will be displayed.
     * @param layer         - The layer where the object will be displayed, see layers from {@link Scene}
     * @param spawnPosition - The position where the object will be displayed.
     */
    public void initialize(Scene scene, int layer, Vector2D spawnPosition) {
        healthInfo = new TextObject(healthInfoTemplate, (Vector2D) spawnPosition.clone(), "Minecraft", 23);
        int height = healthInfo.getHeight();
        spawnPosition.y += height + 2;
        scoreInfo = new TextObject(scoreInfoTemplate, (Vector2D) spawnPosition.clone(), "Minecraft", 23);

        scene.addGameObjectToScene(healthInfo);
        scene.addGameObjectToLayer(healthInfo, layer);
        scene.addGameObjectToScene(scoreInfo);
        scene.addGameObjectToLayer(scoreInfo, layer);
    }

    /**
     * Updates the health bar of the player.
     * @param health - The health of the player.
     */
    public void updateHealth(double health) {
        healthInfo.setText(healthInfoTemplate + (health >= 0 ? health : 0));
    }

    /**
     * Updates the score bar of the player.
     * @param score - The score of the player.
     */
    public void updateScore(double score) {
        scoreInfo.setText(scoreInfoTemplate + score);
    }
}
