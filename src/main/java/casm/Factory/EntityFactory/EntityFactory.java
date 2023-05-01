package casm.Factory.EntityFactory;

import casm.Entities.Enemies.WeaselFisherman;
import casm.Entities.Entity;
import casm.Entities.Player;
import casm.Factory.Factory;
import casm.Factory.FactoryTypes;
import casm.Utils.Vector2D;

public class EntityFactory implements Factory {

//    public Entity create(EntiType type, Vector2D spawnPosition) {
//        switch (type) {
//            case FactoryTypes.E -> {
//                return new Player(spawnPosition);
//            }
//            case "weaselfisherman" -> {
//                return new WeaselFisherman(spawnPosition);
//            }
//            default -> {
//                return null;
//            }
//        }
//
//    }

    @Override
    public Entity create(FactoryTypes type, Vector2D spawnPosition) {
        if (type.equals(EntityType.PLAYER)) {
            return new Player(spawnPosition);
        } else if (type.equals(EntityType.WEASEL_FISHERMAN)) {
            return new WeaselFisherman(spawnPosition);
        } else {
            return null;
        }
    }
}
