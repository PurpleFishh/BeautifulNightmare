package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.util.HashMap;

/**
 * This component contains the colliders that are attached to the entity<br>
 * That colliders will be used in collision, attacking, AI movement
 * Dependencies: {@link PositionComponent}
 */
public class ColliderComponent extends Component {

    /**
     * The {@link PositionComponent} of the entity
     */
    private PositionComponent positionComponent;
    /**
     * This is where the colliders are stored
     */
    private HashMap<ColliderType, Rectangle> componentColliders = new HashMap<>();
    //TODO: Nu prea va rostu sa tin ceata lu pitiogi dupa mine sa tot dau update la pozitie pt obiectele ce nu se misca, Pot folosi PossitionComp

    /**
     * Create {@link ColliderComponent}
     *
     * @param type       collider type
     * @param rectWidth  collider width
     * @param rectHeight collider height
     */
    public ColliderComponent(ColliderType type, int rectWidth, int rectHeight) {
        componentColliders.put(type, new Rectangle(0, 0, rectWidth, rectHeight));
    }

    /**
     * Create {@link ColliderComponent} that is positioned with an offset than the position of the entity
     *
     * @param type       collider type
     * @param xOffset    the offset on x axe
     * @param yOffset    the offset on y axe
     * @param rectWidth  collider width
     * @param rectHeight collider height
     */
    public ColliderComponent(ColliderType type, int xOffset, int yOffset, int rectWidth, int rectHeight) {
        componentColliders.put(type, new Rectangle(0, 0, rectWidth, rectHeight, xOffset, yOffset));
    }

    /**
     * Initialize each collider
     */
    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
        componentColliders.values().forEach(it -> it.setRect(positionComponent.position.x, positionComponent.position.y,
                it.getWidth(), it.getHeight()));
    }

    /**
     * Update the collider position to stick to the entity
     */
    @Override
    public void update() {
        componentColliders.values().forEach(it -> it.setRect(positionComponent.position.x, positionComponent.position.y,
                it.getWidth(), it.getHeight()));
    }

    /**
     * Draw each collider on screen with different colliders took from {@link ColliderType}
     */
    @Override
    public void draw() {
        componentColliders.forEach((key, value) -> Renderer.getInstance().drawRect(value.getPosition(), value.getWidth(), value.getHeight(), key.getRenderColor()));
    }

    /**
     * Add a new collider to the entity
     *
     * @param type       collider type
     * @param xOffset    the offset on x axe
     * @param yOffset    the offset on y axe
     * @param rectWidth  collider width
     * @param rectHeight collider height
     * @return the added collider
     */
    public Rectangle addCollider(ColliderType type, int xOffset, int yOffset, int rectWidth, int rectHeight) {
        Rectangle collider = new Rectangle(positionComponent.position.x, positionComponent.position.y, rectWidth, rectHeight, xOffset, yOffset);
        componentColliders.put(type, collider);
        return collider;
    }

    /**
     * @param type collider type
     * @return collider width
     */
    public int getRectWidth(ColliderType type) {
        return (int) componentColliders.get(type).getWidth();
    }

    /**
     * @param type collider type
     * @return collider height
     */
    public int getRectHeight(ColliderType type) {
        return (int) componentColliders.get(type).getHeight();
    }

    /**
     * @param type collider type
     * @return the searched collider
     */
    public Rectangle getCollider(ColliderType type) {
        return componentColliders.get(type);
    }

    /**
     * @return the type of the first added collider
     */
    public ColliderType getType() {
        return (ColliderType) componentColliders.keySet().toArray()[0];
    }

    /**
     * Get the position of a collider
     *
     * @param type collider type
     * @return the position of the collider
     */
    public Vector2D getPosition(ColliderType type) {
        return componentColliders.get(type).getPosition();
    }

    /**
     * Change a collider with a new one
     *
     * @param type     the collider that you want to change
     * @param collider the new collider
     */
    public void updateCollider(ColliderType type, Rectangle collider) {
        componentColliders.replace(type, collider);
    }

    /**
     * Set a collider new dimensions
     *
     * @param type   the collider that you want to change
     * @param width  the new width
     * @param height the new height
     */
    public void updateColliderDimensions(ColliderType type, int width, int height) {
        componentColliders.get(type).width = width;
        componentColliders.get(type).height = height;
    }
}
