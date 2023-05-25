package casm;

import casm.ECS.Components.MouseListener;
import casm.Exception.SceneCanNotBeSaved;
import casm.Factory.SceneFactory.SceneFactory;
import casm.Scenes.Level.LevelSaverLoader;
import casm.Scenes.Level.LeveleScene;
import casm.Scenes.Scene;
import casm.GameWindow.GameWindow;
import casm.Scenes.SceneType;
import casm.Utils.Settings.Setting;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.sql.SQLException;
import java.util.Stack;


/**
 * The game engine class
 * Contains the game loop which init, updates and renders the game objects
 * Contains the game scenes
 */
public class Game implements Runnable {
    /**
     * Game window
     */
    private static GameWindow wnd;
    /**
     * If the game is running
     */
    private static boolean runState;
    /**
     * Main game thread
     */
    private Thread gameThread;
    /**
     * Image buffering render
     */
    private static BufferStrategy bs;
    /**
     * The graphics of the game used for displaying
     */
    private static Graphics g;
    /**
     * The scenes of the game. The top scene in the stack is the current scene
     */
    private static Stack<Scene> sceneStack = new Stack<>();
    /**
     * The scene factory used to create scenes
     */
    private static SceneFactory sceneFactory;

    /**
     * @param title  the title of the game window
     * @param width  the width of the game window
     * @param height the height of the game window
     */
    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

    /**
     * Initialize the game and game window
     */
    private void InitGame() {
        sceneFactory = new SceneFactory();
//        sceneStateMachine = new GameStateMachine();
//        sceneStateMachine.addState(SceneType.MAIN_MENU);
//        sceneStateMachine.addState(SceneType.LEVEL1);
//        sceneStateMachine.addState(SceneType.MAIN_MENU.name(), SceneType.LEVEL1.name(), "start");
//        sceneStateMachine.setDefaultState(SceneType.MAIN_MENU.name());

        wnd.BuildGameWindow();

        wnd.getCanvas().addMouseMotionListener(MouseListener.getInstance());
        wnd.getCanvas().addMouseListener(MouseListener.getInstance());


        bs = Game.getWindow().GetCanvas().getBufferStrategy();
        if (bs == null) {
            try {
                Game.getWindow().GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * The game loop that is updating and rendering the game objects
     */
    public void run() {
        InitGame();
        changeScene(SceneType.MAIN_MENU);

        long oldTime = System.nanoTime();
        long curentTime;

        while (runState) {
            curentTime = System.nanoTime();

            if ((curentTime - oldTime) > Setting.FRAME_TIME) {
                //currentScene.eventHandler();
                if (getCurrentScene().isRunning()) {
                    getCurrentScene().update();
                    getCurrentScene().draw();
                    getCurrentScene().checkForDeaths();
                }
                oldTime = curentTime;
            }
        }
        StopGame();
    }

    /**
     * Start the game
     */
    public synchronized void StartGame() {
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        } else
            return;
    }

    /**
     * Stop the game and save its state in the database
     */
    public synchronized void StopGame() {
        // if (runState) {
        runState = false;
        try {
            LevelSaverLoader.getInstance().save();
        } catch (SceneCanNotBeSaved ignored) {
        }
        try {
            destroyAllScenes();
            wnd.closeWindow();
            System.exit(0);
            gameThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        // } else
        //     return;
    }

    /**
     * Change the current scene
     *
     * @param newScene the new scene to be added to the stack
     */
    public static void changeScene(SceneType newScene) {
        Scene intermediary;
        //TODO: foloseste FSM ul pt scene
        //TODO: fa o exceptie proprie pt asta
        try {
            intermediary = sceneFactory.create(newScene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        sceneStack.push(intermediary);
        sceneStack.peek().init();
    }

    /**
     * Change the current level or start a certain level
     *
     * @param level   the new level id
     * @param restore if you want to restore the level from the database
     */
    public static void changeLevel(int level, boolean restore) {
        LeveleScene intermediary;
        try {
            intermediary = sceneFactory.createLevel(level);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        sceneStack.push(intermediary);
        intermediary.load(restore);
        sceneStack.peek().init();
        destroyAllWithoutTopScenes();
    }

    /**
     * Destroy the current scene, the top of the stack
     */
    public static void destroyViewingScene() {
        sceneStack.pop().destroy();
    }

    /**
     * Destroy all the scenes
     */
    public static void destroyAllScenes() {
        while (!sceneStack.isEmpty())
            sceneStack.pop().destroy();
    }

    /**
     * Destroy all the scenes except the top of the stack(the viewing scene)
     */
    public static void destroyAllWithoutTopScenes() {
        if (sceneStack.size() > 1) {
            sceneStack.subList(0, sceneStack.size() - 1).forEach(Scene::destroy);
            sceneStack.subList(0, sceneStack.size() - 1).clear();
        }
    }

    /**
     * Destroy the second scene from the top of the stack
     */
    public static void destroySecondScenes() {
        sceneStack.remove(sceneStack.size() - 2).destroy();
    }

    /**
     * Get the current scene, the top of the stack
     *
     * @return the current scene
     */
    public static Scene getCurrentScene() {
        return !sceneStack.isEmpty() ? sceneStack.peek() : null;
    }

    /**
     * Search if there is a level scene in the stack, if there is one return it else return null
     *
     * @return the current level scene
     */
    public static LeveleScene getLevelScene() {
        for (int i = sceneStack.size() - 1; i >= 0; --i)
            if (sceneStack.get(i) instanceof LeveleScene)
                return (LeveleScene) sceneStack.get(i);
        return null;
    }

    /**
     * @return the game window
     */
    public static GameWindow getWindow() {
        return wnd;
    }

    /**
     * @return the buffer strategy of the game window
     */
    public static BufferStrategy getBufferStrategy() {
        bs = Game.getWindow().GetCanvas().getBufferStrategy();
        return bs;
    }

    /**
     * @return the graphics of the buffer strategy
     */
    public static Graphics getGraphics() {
        g = getBufferStrategy().getDrawGraphics();
        return g;
    }

    /**
     * End the game
     */
    public static void exitGame() {
        runState = false;
    }
}

