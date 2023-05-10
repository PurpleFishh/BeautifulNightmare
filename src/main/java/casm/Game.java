package casm;

import casm.ECS.Components.KeyboardListener;
import casm.ECS.Components.MouseListener;
import casm.Scenes.MainMenuScene;
import casm.Scenes.Scene;
import casm.GameWindow.GameWindow;
import casm.Scenes.LeveleScene;
import casm.Utils.Settings.Setting;

import java.awt.*;
import java.awt.image.BufferStrategy;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)
*/
public class Game implements Runnable {
    private static GameWindow wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private static BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private static Graphics g;          /*!< Referinta catre un context grafic.*/
    private static Scene currentScene, changedScene;

    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

    private void InitGame() {
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
        changeScene(1);

        long oldTime = System.nanoTime();
        long curentTime;

        while (runState) {
            curentTime = System.nanoTime();

            if ((curentTime - oldTime) > Setting.FRAME_TIME) {
                //currentScene.eventHandler();
                if (currentScene.isRunning()) {
                    currentScene.update();
                    currentScene.draw();
                    currentScene.checkForDeaths();
                }
                oldTime = curentTime;
            }
        }
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
        if (runState) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else
            return;
    }

    public static void changeScene(int newSceneId) {
        Scene oldScene = null;
        switch (newSceneId) {
            case 0 -> {
                oldScene = currentScene;
                currentScene = new LeveleScene();
                currentScene.init();
            }
            case 1 -> {
                oldScene = currentScene;
                currentScene = new MainMenuScene();
                currentScene.init();
            }
            default -> {
                assert false : "Unknown scene :" + newSceneId;
            }
        }
        if(oldScene != null)
        {
            //TODO: Vezi ce naiba se intampla aici
           oldScene.destroy();
            //oldScene = null;
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
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
}

