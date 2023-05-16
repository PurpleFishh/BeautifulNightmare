package casm;

import casm.ECS.Components.MouseListener;
import casm.Factory.SceneFactory.SceneFactory;
import casm.Scenes.Scene;
import casm.GameWindow.GameWindow;
import casm.Scenes.SceneType;
import casm.Utils.Settings.Setting;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Stack;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)
*/
public class Game implements Runnable {
    private static GameWindow wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private static boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private static BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private static Graphics g;          /*!< Referinta catre un context grafic.*/
    //private static Scene currentScene;
    private static Stack<Scene> sceneStack = new Stack<>();
    // private static GameStateMachine sceneStateMachine;
    private static SceneFactory sceneFactory;

    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

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

    public synchronized void StartGame() {
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        } else
            return;
    }

    public synchronized void StopGame() {
        // if (runState) {
        runState = false;
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

    public static void changeLevel(int level)
    {
        Scene intermediary;
        try {
            intermediary = sceneFactory.createLevel(level);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        sceneStack.push(intermediary);
        sceneStack.peek().init();
        destroyAllWithoutTopScenes();
    }

    public static void destroyViewingScene()
    {
        sceneStack.pop().destroy();
    }
    public static void destroyAllScenes()
    {
        while (!sceneStack.isEmpty())
            sceneStack.pop().destroy();
    }
    public static void destroyAllWithoutTopScenes()
    {
        if (sceneStack.size() > 1) {
            sceneStack.subList(0, sceneStack.size() - 1).clear();
        }
    }

    public static Scene getCurrentScene() {
        return !sceneStack.isEmpty() ? sceneStack.peek() : null;
    }

    public static GameWindow getWindow() {
        return wnd;
    }

    public static BufferStrategy getBufferStrategy() {
        bs = Game.getWindow().GetCanvas().getBufferStrategy();
        return bs;
    }

    public static Graphics getGraphics() {
        g = getBufferStrategy().getDrawGraphics();
        return g;
    }

    public static void exitGame() {
        runState = false;
    }
}

