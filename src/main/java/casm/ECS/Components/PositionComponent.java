package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.DyncamicColliderComponent;
import casm.ECS.Components.Collision.MovementMediator;
import casm.Utils.Mediator;
import casm.Utils.Setting;
import casm.Utils.Vector2D;

public class PositionComponent extends Component {

    public Vector2D position, velocity = new Vector2D(), sign = new Vector2D();
    double scale = 1, speed = 1;
    private int height, width;
    boolean gravity = false, isJumping = false, isClimbing = false;
    private Mediator mediator;

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
        mediator = new MovementMediator();
    }

    @Override
    public void update() {
        velocity = getPotentialVelocity();
        mediator.notify(this);
//        if(gameObject.hasComponent(DyncamicColliderComponent.class))
//            System.out.println(position);
    }


    public Vector2D getPotentialVelocity() {
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
            if (sign.y == -1 && Math.abs(new_velocity_y) >= Setting.MAX_JUMP) {
                sign.y = 1;
                System.out.println("Max Jump");
            }
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

    public Vector2D getPotentialPosition() {
        Vector2D potPosition = (Vector2D) position.clone();
        potPosition.x += velocity.x * Setting.DELTA_TIME * speed;
        potPosition.y += velocity.y * Setting.DELTA_TIME * speed;
        return potPosition;
    }
}
