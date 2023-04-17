package casm.ECS.Components.Collision;

import java.awt.*;

public enum ColliderType {
    MAP_TILE,
    LEADER,
    PLAYER;

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
            default ->{
                return Color.BLACK;
            }
        }
    }
}
