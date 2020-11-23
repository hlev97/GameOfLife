package controller;

import model.Universe;

import java.util.ArrayList;

public class GameOfLifeController {
    private int size;
    private long speed;
    private boolean reverse;
    private int[] born;
    private int[] survive;
    private Universe universe;

    public GameOfLifeController() {
        size = 3;
        speed = 1000;
        reverse = false;
        born = new int[]{1, 2, 7, 8};
        survive = new int[]{0, 3, 5};
        universe = new Universe(size, born, survive);
    }

    public int getUniverseSize() { return universe.getSize(); }

    public Universe getUniverse() { return universe; }

    public ArrayList<ArrayList<Boolean>> getUniverseArray() { return universe.getCurrentGen(); }

    public void setSpeed(long s) { speed = s; }

    public long getSpeed() { return speed; }

}
