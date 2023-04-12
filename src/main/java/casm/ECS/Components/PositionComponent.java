package casm.ECS.Components;

import casm.ECS.Component;
import casm.Entities.Tile;
import casm.Game;
import casm.Map.Map;
import casm.Utils.Setting;
import casm.Utils.Vector2D;

import java.util.List;

public class PositionComponent extends Component {

    public Vector2D position, velocity = new Vector2D(), sign = new Vector2D();
    double scale = 1, speed = 1;
    private int height, width;
    boolean gravity = false;

    public PositionComponent() {
        this.position = new Vector2D();
    }

    public PositionComponent(double x, double y) {
        this.position = new Vector2D(x, y);
    }

    public PositionComponent(double scale) {
        this.position = new Vector2D();
        this.scale = scale;
    }

    public PositionComponent(double x, double y, double scale) {
        this.position = new Vector2D(x, y);
        this.scale = scale;
    }

    public PositionComponent(double x, double y, int w, int h) {
        this.position = new Vector2D(x, y);
        this.height = h;
        this.width = w;
    }

    public PositionComponent(double x, double y, int w, int h, double scale, double speed) {
        this.position = new Vector2D(x, y);
        this.height = h;
        this.width = w;
        this.speed = speed;
        this.scale = scale;
    }

    public PositionComponent(double x, double y, int w, int h, double scale, double speed, boolean gravity) {
        this.position = new Vector2D(x, y);
        this.height = h;
        this.width = w;
        this.speed = speed;
        this.scale = scale;
        this.gravity = gravity;
    }

    @Override
    public void init() {
        velocity = new Vector2D();
    }

    @Override
    public void update() {
        velocity = getPotentialVelocitiy();
        Vector2D potentialPosition = getPotentialPosition();
        if (gameObject.getComponent(DyncamicColliderComponent.class) != null) {
            //System.out.println(velocity.x);
            collisionSolver(potentialPosition);
        } else
            position = potentialPosition;
    }

    private void collisionSolver(Vector2D potentialPosition) {
        DyncamicColliderComponent dynamicColl = gameObject.getComponent(DyncamicColliderComponent.class);

        Vector2D xPot = new Vector2D(potentialPosition.x, position.y);
        Vector2D yPot = new Vector2D(position.x, potentialPosition.y);

        int width = gameObject.getComponent(ColliderComponent.class).rectWidth;
        int height = gameObject.getComponent(ColliderComponent.class).rectHeight;

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
                    position.y = yPot.y - 1;
                else
                    position.y = yPot.y - 2;
            } else
                position.y = yPot.y - 1;

        }
        dynamicColl.setOnGround(dynamicColl.getCollisionCorrnersFlags()[2] || dynamicColl.getCollisionCorrnersFlags()[3]);

        if (!collisionX) {

            xPot.x += 1;
            collisionX = dynamicColl.checkCollision(xPot, width, height,
                    Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
            if (!collisionX) {
                xPot.x += 1;
                collisionX = dynamicColl.checkCollision(xPot, width, height,
                        Game.getCurrentScene().getLayeringObjects().get(0), Map.getTileWidth(), Map.getTileHeight());
                if (collisionX)
                    position.x = xPot.x - 1;
                else
                    position.x = xPot.x - 2;
            } else
                position.x = xPot.x - 1;

        }
    }


    Vector2D getPotentialVelocitiy() {
        Vector2D potVelocity = (Vector2D) velocity.clone();
        double new_velocity_x = potVelocity.x + (Setting.DELTA_TIME / 1000) * sign.x;
        double new_velocity_y = potVelocity.y + (Setting.DELTA_TIME / 1000) * sign.y;

        // Miscare pe axa X
        if (sign.x != 0 && Math.abs(new_velocity_x) < Setting.MAX_SPEED) {
            potVelocity.x = new_velocity_x;
        }

        // Daca entitatea are gravitatie
        if (gravity) {

            // Daca ajunge la inaltimea maxima a sariturii aplicam inapoi gravitatiea
            if (sign.y == -1 && Math.abs(new_velocity_y) >= Setting.MAX_JUMP)
                sign.y = 1;
            // Daca e nevoie de gravidatie, nu o lasam sa treaca de maximul acesteia
            if (sign.y == 1 && new_velocity_y >= Setting.GRAVITY)
                new_velocity_y = potVelocity.y;
            if (sign.y == 0 && new_velocity_y < Setting.GRAVITY)
                new_velocity_y = potVelocity.y + (Setting.DELTA_TIME / 1000) * Setting.GRAVITY;
            potVelocity.y = new_velocity_y;
        } else
            // Miscare pe axa Y(ca o fantoma ;))
            if (sign.y != 0 && Math.abs(new_velocity_y) < Setting.MAX_SPEED) {
                potVelocity.y = new_velocity_y;
            }

        return potVelocity;
    }

    private Vector2D getPotentialPosition() {
        Vector2D potPosition = (Vector2D) position.clone();
        potPosition.x += velocity.x * Setting.DELTA_TIME * speed;
        potPosition.y += velocity.y * Setting.DELTA_TIME * speed;
        return potPosition;
    }
}
