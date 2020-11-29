package controller;

import model.Universe;

import java.util.ArrayList;

public class GameOfLifeController {
    private int size;				//a sejtter meretenek beallitasahoz
    private long speed;				//a szimulacio kesleltetesenek beallitasahoz
    private int[] born;				//az aktualis szabaly beallitasahoz
    private int[] survive;			//az aktualis szabaly beallitasahoz
    private Universe universe;		//az univerzum beallitasahoz

    public GameOfLifeController() {		//a program inditasa utan, ezek szerint az ertekek szerint fog futni a szimulacio
        size = 3;						//az kiindulo sejtter merete
        speed = 1000;					//a szimulacio kesleltetesenek alap erteke
        born = new int[]{1, 2, 7, 8};	//az alap szabaly a sejtek szuletesehez
        survive = new int[]{0, 3, 5};	//az alap szabaly a sejtek eletben maradasahoz
        universe = new Universe(size, born, survive);
    }

    public int getUniverseSize() { return universe.getSize(); }

    public Universe getUniverse() { return universe; }

    public ArrayList<ArrayList<Boolean>> getUniverseArray() { return universe.getCurrentGen(); }

    public void setSpeed(long s) { speed = s; }

    public long getSpeed() { return speed; }
    
    public Boolean isAlive(int i, int j) { return universe.getCurrentGen().get(i).get(j); }
    
    public int aliveCells() { return universe.aliveCells(); }
    
    public void generateNextState() { universe.evolve(); }
    
}
