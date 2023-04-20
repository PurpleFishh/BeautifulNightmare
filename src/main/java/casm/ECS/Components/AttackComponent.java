package casm.ECS.Components;

import casm.ECS.Component;
import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;

import java.awt.geom.Rectangle2D;

public class AttackComponent extends Component {

    public AttackComponent()
    {

    }
    @Override
    public void init() {
        //System.out.println("Da");
        //gameObject.addComponent(new ColliderComponent(ColliderType.ATTACKING_BOX,10, 10));
        gameObject.getComponent(ColliderComponent.class).addCollider(ColliderType.ATTACKING_BOX, 32, 22, 10, 10);
    }
}
