package casm.Builder;

import casm.Objects.Object;

public interface Builder {
    Object build();
    void reset();
}
