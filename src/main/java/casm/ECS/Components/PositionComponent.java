package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.MovementMediator;
import casm.Utils.Mediator;
import casm.Utils.Settings.EntitiesSettings;
import casm.Utils.Settings.Setting;
import casm.Utils.Vector2D;

/**
 * Position component that will specify the entity position, its velocity and the direction of movement
 */
public class PositionComponent extends Component {

    /**
     * <b>position</b> - the entity position<br>
     * <b>velocity</b> - the entity velocity of movement<br>
     * <b>sign</b> - the direction of movement<i>(0 -> stationer,for x: -1 -> left, 1 -> right, for y: -1 -> up, 1 -> down)</i>
     */
    public Vector2D position, velocity = new Vector2D(), sign = new Vector2D();
    /**
     * <b>scale</b> - the entity scale<br>
     * <b>speed</b> -
     * the movement speed<i>(if you want the entity to move faster or slower, normal value: 1)</><br>
     * <b>maxSpeed</b> - the speed movement for the entity, can be modified from {@link EntitiesSettings}
     */
    private double scale = 1, speed = 1, maxSpeed = EntitiesSettings.MAX_SPEED;
    /**
     * <b>width</b> - the entity velocity of width<br>
     * <b>height</b> - the entity height<br>
     */
    private int height, width;
    /**
     * <b>gravity</b> - if it will be applied gravity for the entity, or it will fly<br>
     * <b>isJumping</b> - if the entity is now in a jump or not<br>
     * <b>isClimbing</b> - if the entity is now climbing a leader or not<br>
     * <b>canMove</b> - if the entity can move,or it is frozen<br>
     */
    private boolean gravity = false, isJumping = false, isClimbing = false, canMove = true;
    /**
     * The mediator that will be used for mediating movement with keyboard movement, collisions
     *
     * @see MovementMediator
     */
    private Mediator mediator;

    /**
     * Create {@link PositionComponent} with position (0,0)
     */
    public PositionComponent() {
        this.position = new Vector2D();
    }

    /**
     * Create {@link PositionComponent}
     *
     * @param x position x
     * @param y position y
     */
    public PositionComponent(double x, double y) {
        this.position = new Vector2D(x, y);
    }

    /**
     * Create {@link PositionComponent} with position (0,0)
     *
     * @param scale entity scale
     */
    public PositionComponent(double scale) {
        this.position = new Vector2D();
        this.scale = scale;
    }

    /**
     * Create {@link PositionComponent}
     *
     * @param x     position x
     * @param y     position y
     * @param scale entity scale
     */
    public PositionComponent(double x, double y, double scale) {
        this.position = new Vector2D(x, y);
        this.scale = scale;
    }

    /**
     * Create {@link PositionComponent}
     *
     * @param x position x
     * @param y position y
     * @param h entity height
     * @param w entity width
     */
    public PositionComponent(double x, double y, int w, int h) {
        this.position = new Vector2D(x, y);
        this.height = h;
        this.width = w;
    }

    /**
     * Create {@link PositionComponent}
     *
     * @param x     position x
     * @param y     position y
     * @param h     entity height
     * @param w     entity width
     * @param scale entity scale
     * @param speed entity speed(for default speed value 1)
     */
    public PositionComponent(double x, double y, int w, int h, double scale, double speed) {
        this.position = new Vector2D(x, y);
        this.height = h;
        this.width = w;
        this.speed = speed;
        this.scale = scale;
    }

    /**
     * Create {@link PositionComponent}
     *
     * @param x       position x
     * @param y       position y
     * @param h       entity height
     * @param w       entity width
     * @param scale   entity scale
     * @param speed   entity speed(for default speed value 1)
     * @param gravity if gravity is applied for the entity
     */
    public PositionComponent(double x, double y, int w, int h, double scale, double speed, boolean gravity) {
        this.position = new Vector2D(x, y);
        this.height = h;
        this.width = w;
        this.speed = speed;
        this.scale = scale;
        this.gravity = gravity;
    }

    /**
     * Initialize the position component
     */
    @Override
    public void init() {
        velocity = new Vector2D();
        mediator = MovementMediator.getInstance(gameObject);
    }

    /**
     * Update the position component
     */
    @Override
    public void update() {
        if (canMove) {
            velocity = getPotentialVelocity();
            mediator.notify(this);
        }
    }


    /**
     * Calculate the potential entity velocity as vector using the actual data
     *
     * @return the potential velocity as vector
     */
    public Vector2D getPotentialVelocity() {
        Vector2D potVelocity = (Vector2D) velocity.clone();
        double new_velocity_x = potVelocity.x + (Setting.DELTA_TIME / 1000) * sign.x;
        double new_velocity_y = potVelocity.y + (Setting.DELTA_TIME / 1000) * sign.y;

        // Miscare pe axa X
        if (sign.x != 0 && Math.abs(new_velocity_x) < maxSpeed) {
            potVelocity.x = new_velocity_x;
        }

        // Daca entitatea are gravitatie
        if (gravity) {

            // Daca ajunge la inaltimea maxima a sariturii aplicam inapoi gravitatiea
            if (isJumping && sign.y == -1 && Math.abs(new_velocity_y) >= EntitiesSettings.MAX_JUMP) {
                sign.y = 1;
                isJumping = false;
            }
            // Daca ajunge la viteza maxima de catarat il limitam
            if (isClimbing && sign.y == -1 && Math.abs(new_velocity_y) >= EntitiesSettings.MAX_CLIMBING_SPEED)
                new_velocity_y = potVelocity.y;
            // Daca e nevoie de gravidatie, nu o lasam sa treaca de maximul acesteia
            if (sign.y == 1 && new_velocity_y >= EntitiesSettings.GRAVITY)
                new_velocity_y = potVelocity.y;
            // Aplicam gravitatia
            if (sign.y == 0 && new_velocity_y < EntitiesSettings.GRAVITY)
                new_velocity_y = potVelocity.y + (Setting.DELTA_TIME / 1000) * EntitiesSettings.GRAVITY;
            potVelocity.y = new_velocity_y;
        } else
            // Miscare pe axa Y(ca o fantoma ;))
            if (sign.y != 0 && Math.abs(new_velocity_y) < maxSpeed) {
                potVelocity.y = new_velocity_y;
            }

        return potVelocity;
    }

    /**
     * Calculate the potential entity position as vector using the actual data
     *
     * @return the potential positions as vector
     */
    public Vector2D getPotentialPosition() {
        Vector2D potPosition = (Vector2D) position.clone();
        potPosition.x += velocity.x * Setting.DELTA_TIME * speed;
        potPosition.y += velocity.y * Setting.DELTA_TIME * speed;
        return potPosition;
    }
    /**
     * Calculate the potential entity position as vector using a position vector, not the position of the entity
     *
     * @return the potential positions as vector
     */
    public Vector2D getPotentialPosition(Vector2D updateVector) {
        Vector2D potPosition = (Vector2D) updateVector.clone();
        potPosition.x += velocity.x * Setting.DELTA_TIME * speed;
        potPosition.y += velocity.y * Setting.DELTA_TIME * speed;
        return potPosition;
    }

    /**
     * Get if the entity is in a jump
     * @return if the entity is jumping
     */
    public boolean isJumping() {
        return isJumping;
    }

    /**
     * Set the jumping flag
     * @param jumping value for the jumping flag
     */
    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    /**
     * Get if the entity is climbing a leader
     * @return if the entity is climbing
     */
    public boolean isClimbing() {
        return isClimbing;
    }
    /**
     * Set the climbing flag
     * @param climbing value for the climbing flag
     */
    public void setClimbing(boolean climbing) {
        isClimbing = climbing;
    }

    /**
     * @return get entity height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return get entity width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param height set entity height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param width set entity width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Set dimensions of the entity
     * @param width entity width
     * @param height entity height
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @return entity speed limit for running
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Set new entity speed limit for running
     * @param maxSpeed entity speed limit
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * @return if the entity can move or is frozen
     */
    public boolean isCanMove() {
        return canMove;
    }

    /**
     * @param canMove set if the entity can move or is frozen
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
