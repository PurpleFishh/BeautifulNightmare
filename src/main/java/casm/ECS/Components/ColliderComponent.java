package casm.ECS.Components;

import casm.ECS.Component;
import casm.Utils.Renderer;

public class ColliderComponent extends Component {

    PositionComponent positionComponent;
    int rectWidth, rectHeight;
    private String tag;
    public ColliderComponent(String tag, int rectWidth, int rectHeight) {
        this.tag = tag;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
    }

    @Override
    public void init() {
        positionComponent = gameObject.getComponent(PositionComponent.class);
    }

    @Override
    public void draw() {
        Renderer.drawRect(positionComponent.position, rectWidth, rectHeight);
    }
}
