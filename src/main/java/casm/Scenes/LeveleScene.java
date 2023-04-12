package casm.Scenes;

import casm.ECS.Components.PositionComponent;
import casm.ECS.Components.SpriteComponent;
import casm.ECS.GameObject;
import casm.ECS.Scene;
import casm.Entities.Player;
import casm.Map.Map;
import casm.SpriteUtils.AssetsCollection;

public class LeveleScene extends Scene {

    public LeveleScene() {

    }

    public enum layers {
        BACKGROUND,
        PLAYER,
        FOREGROUND
    }

    private void levelConstruct() {
        AssetsCollection.addSpritesheet("player_single_frame.png", 16, 22);

        Player player = new Player();
        addGameObjectToScene(player);
        addGameOjectToLayer(player, layers.PLAYER.ordinal());

        Map.loadMap(this, "level1.tmj", "tiles2.png");
        GameObject obj = new GameObject("my Obj2");
        obj.addComponent(new PositionComponent(112, 67,
                AssetsCollection.getSpritesheet("tiles2.png").getSprite(1).getWidth(),
                AssetsCollection.getSpritesheet("tiles2.png").getSprite(1).getHeight()));
        //obj.getComponent(PositionComponent.class).sign.y = 1;
//        obj.getComponent(PositionComponent.class).sign.x = 1;
        obj.addComponent(new SpriteComponent(AssetsCollection.getSpritesheet("tiles2.png").getSprite(1)));
        obj.init();
        addGameObjectToScene(obj);
        addGameOjectToLayer(obj, layers.FOREGROUND.ordinal());
    }

    @Override
    public void init() {
        levelConstruct();
        gameObjects.forEach(GameObject::init);
    }
}
