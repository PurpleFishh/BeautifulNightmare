package casm.Factory.MenuFactory;

import casm.Factory.FactoryTypes;
import casm.Builder.BuilderTypeEnum;

/**
 * The type of the entities that can be created in the {@link MenuEntityFactory}
 */
public enum MenuEntityType implements FactoryTypes {
    BACKGROUND_IMAGE,
    BACKGROUND_BUTTONS,
    BACKGROUND_SETTINGS,
    LOGO,
    BACK,
    TITLE_BAR;

    /**
     * The type of the buttons that can be created
     */
    public enum BUTTON implements BuilderTypeEnum, FactoryTypes {
        PLAY,
        NEW,
        RESUME,
        LOAD,
        SETTINGS,
        EXIT,
        RETRY,
        BACK;

    }
}
