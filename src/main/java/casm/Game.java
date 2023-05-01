package casm;

import casm.Scenes.Scene;
import casm.GameWindow.GameWindow;
import casm.Scenes.LeveleScene;
import casm.Utils.Settings.Setting;

import java.awt.*;
import java.awt.image.BufferStrategy;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)
*/
public class Game implements Runnable
{
    private static GameWindow wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean         runState;   /*!< Flag ce starea firului de executie.*/
    private Thread          gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private static BufferStrategy  bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private static Graphics        g;          /*!< Referinta catre un context grafic.*/
    private static Scene currentScene;

    public Game(String title, int width, int height)
    {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

    private void InitGame()
    {
        //wnd = new GameWindow("Beaitofil Nightmare", 800, 600);
        wnd.BuildGameWindow();

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

    public void run()
    {
        InitGame();
        changeScene(0);

//        long oldTime = System.nanoTime();
//        long curentTime;
//
//        while (runState)
//        {
//            curentTime = System.nanoTime();
//
//            if((curentTime - oldTime) > Setting.FRAME_TIME)
//            {
//                //currentScene.eventHandler();
//                currentScene.update();
//                currentScene.draw();
//                oldTime = curentTime;
//            }
//        }
        long previousTime = System.nanoTime();

        double deltaU = 0;
        double deltaF = 0;

        while (runState) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / Setting.UPDATE_TIME;
            deltaF += (currentTime - previousTime) / Setting.FRAME_TIME;
            previousTime = currentTime;
            if (deltaU >= 1) {
                currentScene.update();
                deltaU--;
            }

            if (deltaF >= 1) {
                currentScene.draw();
                currentScene.checkForDeaths();
                deltaF--;
            }
        }
    }

    public synchronized void StartGame()
    {
        if(!runState)
        {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
        else
            return;
    }

    public synchronized void StopGame()
    {
        if(runState)
        {
            runState = false;
            try
            {
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        else
            return;
    }

    public static void changeScene(int newScene)
    {
        switch (newScene)
        {
            case 0:
                currentScene = new LeveleScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene :" + newScene;
                break;
        }
    }
    public static Scene getCurrentScene()
    {
        return currentScene;
    }

    public static GameWindow getWindow()
    {
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

