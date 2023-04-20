package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.Utils.Renderer;
import casm.Utils.Vector2D;

import java.util.HashMap;

public class ColliderComponent extends Component {

    private PositionComponent positionComponent;
    private HashMap<ColliderType, Rectangle> componentColliders = new HashMap<>();
    //private Rectangle collider;
    //TODO: Nu prea va rostu sa tin ceata lu pitiogi dupa mine sa tot dau update la pozitie pt obiectele ce nu se misca, Pot folosi PossitionComp
    //private ColliderType type;

    public ColliderComponent(ColliderType type, int rectWidth, int rectHeight) {
        componentColliders.put(type, new Rectangle(0, 0, rectWidth, rectHeight));
    }

    public ColliderComponent(ColliderType type, int xOffset, int yOffset, int rectWidth, int rectHeight) {
        componentColliders.put(type, new Rectangle(0, 0, rectWidth, rectHeight, xOffset, yOffset));
    }

    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
        //collider.setRect(positionComponent.position.x, positionComponent.position.y,
         //       collider.getWidth(), collider.getHeight());
        componentColliders.values().forEach(it -> it.setRect(positionComponent.position.x, positionComponent.position.y,
                it.getWidth(), it.getHeight()));
    }

    @Override
    public void update() {
        //collider.setRect(positionComponent.position.x, positionComponent.position.y,
        //        collider.getWidth(), collider.getHeight());
        componentColliders.values().forEach(it -> it.setRect(positionComponent.position.x, positionComponent.position.y,
                it.getWidth(), it.getHeight()));
    }

    @Override
    public void draw() {
       // componentColliders.values().forEach(it -> it.setRect(positionComponent.position.x, positionComponent.position.y,
        //        collider.getWidth(), collider.getHeight()));
        componentColliders.forEach((key, value) -> Renderer.drawRect(value.getPosition(), value.getWidth(), value.getHeight(), key.getRenderColor()));
        //Renderer.drawRect(collider.getPosition(), collider.getWidth(), collider.getHeight(), type.getRenderColor());
    }

    public void addCollider (ColliderType type, int xOffset, int yOffset, int rectWidth, int rectHeight)
    {
        componentColliders.put(type, new Rectangle(positionComponent.position.x, positionComponent.position.y, rectWidth, rectHeight, xOffset, yOffset));
    }

    public int getRectWidth(ColliderType type) {
        return (int) componentColliders.get(type).getWidth();
    }

    public int getRectHeight(ColliderType type) {
        return (int) componentColliders.get(type).getHeight();
    }
    public Rectangle getCollider(ColliderType type)
    {
        return componentColliders.get(type);
    }
    public ColliderType getType()
    {
        return (ColliderType) componentColliders.keySet().toArray()[0];
    }
    public Vector2D getPosition(ColliderType type)
    {
        return componentColliders.get(type).getPosition();
    }
}
