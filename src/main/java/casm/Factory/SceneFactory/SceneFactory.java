package casm.Factory.SceneFactory;

import casm.Exception.UnknownFactoryType;
import casm.Factory.FactoryTypes;
import casm.Scenes.*;
import casm.Scenes.Level.LeveleScene;

public class SceneFactory {
    /**
     * Index of the level that will be created by the factory
     */
    private int createLevelIndex = 1;

    /**
     * Create an entity
     *
     * @param type the type of the entity
     * @return the entity created
     */
    public Scene create(FactoryTypes type) throws UnknownFactoryType {
        if (type.equals(SceneType.LEVEL)) {
            return new LeveleScene((SceneType) type, createLevelIndex);
        } else if (type.equals(SceneType.MAIN_MENU)) {
            return new MainMenuScene((SceneType) type);
        } else if (type.equals(SceneType.SETTINGS_MENU)) {
            return new SettingsMenuScene((SceneType) type);
        } else if (type.equals(SceneType.PAUSE_MENU)) {
            return new PauseScene((SceneType) type);
        } else if (type.equals(SceneType.GAME_OVER_MENU)) {
            return new GameOverScene((SceneType) type);
        } else {
            throw new UnknownFactoryType();
        }
    }

    /**
     * Change the index of the server that will be created
     *
     * @param level the new index
     * @return reference to the factory
     */
    public SceneFactory changeLevel(int level) {
        this.createLevelIndex = level;
        return this;
    }

    /**
     * Create a new level
     *
     * @param level the index of the level
     * @return constructed level
     */
    public LeveleScene createLevel(int level) {
        this.createLevelIndex = level;
        return new LeveleScene(SceneType.LEVEL, level);
    }
}