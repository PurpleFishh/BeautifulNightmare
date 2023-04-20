package casm.ECS.Components.Collision;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public enum ColliderType {
    MAP_TILE,
    LEADER,
    PLAYER,
    ATTACKING_BOX;

    public Set<ColliderType> getMapTypeColliders()
    {
        Set<ColliderType> colliderTypes = new HashSet<ColliderType>();
        colliderTypes.add(MAP_TILE);
        colliderTypes.add(LEADER);
        return colliderTypes;
    }

    public Color getRenderColor()
    {
        switch (this)
        {
            case MAP_TILE -> {
                return Color.RED;
            }
            case PLAYER -> {
                return Color.CYAN;
            }
            case LEADER -> {
                return Color.GREEN;
            }
            case ATTACKING_BOX -> {
                return Color.ORANGE;
            }
            default ->{
                return Color.BLACK;
            }
        }
    }
}
