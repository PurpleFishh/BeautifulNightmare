package casm.Builder;

import casm.Objects.Object;
import casm.Utils.Vector2D;

/**
 * Interface for an object builder director.
 * The director is responsible for the construction of the object.
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern (Wikipedia)</a>
 */
public interface BuilderDirector {

    /**
     * Makes an object of the given type at the given position.
     * @param type the type of the object to make
     * @param position the position of the object
     * @return the made object
     */
    Object makeObject(BuilderTypeEnum type, Vector2D position);
}
