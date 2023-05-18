package casm.Objects;

import casm.Observer.Observable;
import casm.Observer.Observer;
import casm.Scenes.Scene;
import casm.Utils.Vector2D;

import java.util.ArrayList;

public class InfoBar extends Object {

    private TextObject healthInfo, scoreInfo;
    private final String healthInfoTemplate = "Health: ";
    private final String scoreInfoTemplate = "Score: ";

    public InfoBar(Scene scene, int layer, Vector2D spawnPosition) {
        super("Info Bar", spawnPosition);
        initialize(scene, layer, spawnPosition);
    }

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

    public void updateHealth(double health) {
        healthInfo.setText(healthInfoTemplate + (health >= 0 ? health : 0));
    }

    public void updateScore(double score) {
        scoreInfo.setText(scoreInfoTemplate + score);
    }
}
