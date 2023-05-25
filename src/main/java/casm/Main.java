package casm;

import casm.Utils.Settings.Setting;

/**
 * The main class where the game is stated
 */
public class Main
{
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game("Beautiful Nightmare", Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
        game.StartGame();
    }
}
