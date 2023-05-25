package casm.Scenes;

import casm.Factory.FactoryTypes;

/**
 * The types of scenes
 */
public enum SceneType implements FactoryTypes {
    MAIN_MENU,
    PAUSE_MENU,
    GAME_OVER_MENU,
    SETTINGS_MENU,
    LEVEL;
}
