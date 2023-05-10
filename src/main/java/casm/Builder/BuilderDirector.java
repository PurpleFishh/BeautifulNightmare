package casm.Builder;

import casm.Objects.Object;
import casm.Utils.Vector2D;

public interface BuilderDirector {

    Object makeObject(BuilderTypeEnum type, Vector2D position);
}
