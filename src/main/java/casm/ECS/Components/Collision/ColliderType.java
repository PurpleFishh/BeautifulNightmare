package casm.ECS.Components.Collision;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An enum of all the collider types
 */
public enum ColliderType {
    /**
     * Used for map tiles collision
     */
    MAP_TILE,
    /**
     * Used for leaders for the ability to climb on them
     */
    LEADER,
    /**
     * Used for entities that will be used for managing collisions
     */
    ENTITY,
    /**
     * Used for attack box, the area that the entity can attack in
     */
    ATTACKING_BOX,
    /**
     * The AI vision, if an entity is in that area, the AI will follow it
     */
    AI_DETECTION,
    /**
     * For the door that will open when the tasks are done and you can go to the next level
     */
    WIN_DOOR;

    /**
     * Get a set of collider types that are for map tiles
     *
     * @return the set of colliders
     */
    public Set<ColliderType> getMapTypeColliders() {
        Set<ColliderType> colliderTypes = new HashSet<ColliderType>();
        colliderTypes.add(MAP_TILE);
        colliderTypes.add(LEADER);
        return colliderTypes;
    }

    /**
     * Get a color for each collider for rendering
     *
     * @return the color of the collider
     */
    public Color getRenderColor() {
        switch (this) {
            case MAP_TILE -> {
                return Color.RED;
            }
            case ENTITY -> {
                return Color.CYAN;
            }
            case LEADER -> {
                return Color.GREEN;
            }
            case ATTACKING_BOX -> {
                return Color.ORANGE;
            }
            case AI_DETECTION -> {
                return Color.PINK;
            }
            case WIN_DOOR -> {
                return Color.MAGENTA;
            }
            default -> {
                return Color.BLACK;
            }
        }
    }
}
