package casm.Factory.MenuFactory;

import casm.Factory.FactoryTypes;
import casm.Builder.BuilderTypeEnum;

public enum MenuEntityType implements FactoryTypes {
    BACKGROUND_IMAGE,
    BACKGROUND_BUTTONS,
    LOGO;
    public enum BUTTON implements BuilderTypeEnum, FactoryTypes{
        PLAY,
        NEW,
        RESUME,
        LOAD,
        SETTINGS,
        EXIT,
        RETRY;

    }
}
