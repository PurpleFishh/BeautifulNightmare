package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.ECS.GameObject;
import casm.Game;
import casm.Map.Map;
import casm.Utils.Mediator;
import casm.Utils.Vector2D;

public class MovementMediator implements Mediator {
    @Override
    public void notify(Component component) {
        if(component.gameObject.hasComponent(DyncamicColliderComponent.class))
            collisionManager((PositionComponent) component);
        
        positionUpdate((PositionComponent) component);
    }

    private void collisionManager(PositionComponent positionComponent)
    {
        GameObject gameObject = positionComponent.gameObject;
        DyncamicColliderComponent dynamicColl = gameObject.getComponent(DyncamicColliderComponent.class);
        Vector2D potentialPosition = positionComponent.getPotentialPosition();

        Vector2D xPot = new Vector2D(potentialPosition.x, positionComponent.position.y);
        Vector2D yPot = new Vector2D(positionComponent.position.x, potentialPosition.y);

        int width = gameObject.getComponent(ColliderComponent.class).getRectWidth();
        int height = gameObject.getComponent(ColliderComponent.class).getRectHeight();

        boolean collisionX = dynamicColl.checkCollision(xPot, width, height,
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
        boolean collisionY = dynamicColl.checkCollision(yPot, width, height,
                Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());

//            if (!collisionY) {
//                position.y = yPot.y;
//            }


        if (!collisionY) {

            yPot.y += 1;
            collisionY = dynamicColl.checkCollision(yPot, width, height,
                    Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
            if (!collisionY) {
                yPot.y += 1;
                collisionY = dynamicColl.checkCollision(yPot, width, height,
                        Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
                if (collisionY)
                    positionComponent.position.y = yPot.y - 1;
                else
                    positionComponent.position.y = yPot.y - 2;
            } else
                positionComponent.position.y = yPot.y - 1;

        }
        dynamicColl.setOnGround(dynamicColl.getCollisionCorrnersFlags()[2] == ColliderType.MAP_TILE || dynamicColl.getCollisionCorrnersFlags()[3] == ColliderType.MAP_TILE);

        if (!collisionX) {

            xPot.x += 1;
            collisionX = dynamicColl.checkCollision(xPot, width, height,
                    Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
            if (!collisionX) {
                xPot.x += 1;
                collisionX = dynamicColl.checkCollision(xPot, width, height,
                        Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
                if (collisionX)
                    positionComponent.position.x = xPot.x - 1;
                else
                    positionComponent.position.x = xPot.x - 2;
            } else
                positionComponent.position.x = xPot.x - 1;

        }
    }

    private void positionUpdate(PositionComponent positionComponent)
    {
        positionComponent.position = positionComponent.getPotentialPosition();
    }
}
