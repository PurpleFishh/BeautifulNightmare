package casm.Factory.EntityFactory;

import casm.Factory.FactoryTypes;

/**
 * The type of the entities that can be created in the {@link EntityFactory}
 */
public enum EntityType implements FactoryTypes {
    /**
     * The player
     */
    PLAYER,
    /**
     * The weasel fisherman enemy
     */
    WEASEL_FISHERMAN,
    /**
     * The heart bonus that will revive the player
     */
    HEART_BONUS,
    /**
     * The catfish enemy
     */
    CATFISH,

    /**
     * The turtle king enemy
     */
    TURTLE_KING

}
