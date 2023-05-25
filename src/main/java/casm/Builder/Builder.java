package casm.Builder;

import casm.Objects.Object;

/**
 * Interface for an object builder.
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern (Wikipedia)</a>
 */
public interface  Builder {
    /**
     * Builds the object constructed at the moment.
     * @return the built object
     */
    Object build();

    /**
     * Resets the builder to a clean state.
     */
    void reset();
}
