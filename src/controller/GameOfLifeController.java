package controller;

import model.Universe;

import java.util.ArrayList;

public class GameOfLifeController {
    private int size;
    private long speed;
    int[] born;
    int[] survive;
    private Universe universe;

    public GameOfLifeController() {
        size = 5;
        speed = 1000;
        born = new int[]{3};
        survive = new int[]{1, 2, 3,4};
        universe = new Universe(size, born, survive);
    }

    public int getUniverseSize() { return universe.getSize(); }

    public Universe getUniverse() { return universe; }

    public ArrayList<ArrayList<Boolean>> getUniverseArray() { return universe.getCurrentGen(); }

    public void setSpeed(long s) { speed = s; }

    public long getSpeed() { return speed; }

}