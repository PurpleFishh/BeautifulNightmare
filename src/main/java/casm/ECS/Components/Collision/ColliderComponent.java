package casm.ECS.Components.Collision;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.Utils.Renderer;

public class ColliderComponent extends Component {

    private PositionComponent positionComponent;
    private int rectWidth, rectHeight;
    private ColliderType type;
    public ColliderComponent(ColliderType type, int rectWidth, int rectHeight) {
        this.type = type;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
    }

    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
    }

    @Override
    public void draw() {
        Renderer.drawRect(positionComponent.position, rectWidth, rectHeight, type.getRenderColor());
    }

    public int getRectWidth() {
        return rectWidth;
    }

    public int getRectHeight() {
        return rectHeight;
    }

    public ColliderType getType() {
        return type;
    }
}
