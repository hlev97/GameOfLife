package controller;

import view.GameOfLifeFrame;


public class GameOfLife {
    public static void main(String[] args) throws InterruptedException {
        GameOfLifeController controller = new GameOfLifeController();
        GameOfLifeFrame frame = new GameOfLifeFrame(controller);
    }
}
