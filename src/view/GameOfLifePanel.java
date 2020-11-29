package view;

import controller.GameOfLifeController;
import model.Universe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameOfLifePanel extends JPanel implements Runnable {
    private JPanel[][] cells;						//2D-s panel tomb, minden egyes panel egy sejtet reprezental
    private GameOfLifeController game_controller;	//controller
    
    private int size;								//controllerben tarolt univerzum merete

    public GameOfLifePanel(GameOfLifeController controller) {
        game_controller = controller;
        size = game_controller.getUniverseSize();
        cells = new JPanel[size][size];
        setLayout(new GridLayout(size, size, 1, 1));
        initUniverse();
        drawUniverse();
    }

    //a sejtteret reprezentalo nagyzetracs inicializalasa
    public void initUniverse() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new JPanel();
                cells[i][j].setBackground(Color.WHITE);
                add(cells[i][j]);
            }
        }
    }
    
    //a sejtter aktualis allapotanak kirajzolasa
    public void drawUniverse() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            	repaint();										//a sejt allapotanak esetleges valtozasa miatt kell hivni
                if(game_controller.isAlive(i,j)) {
                	cells[i][j].setBackground(Color.BLACK);		//ha a sejt elo, akkor a cella szine feketere valt
                } else {
                	cells[i][j].setBackground(Color.WHITE);		//ha a sejt meghalt akkor feherre
                }
            }
        }
    }

    //Az univerzum osszes cellajat feherre allitja
    public void clearPanel() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            	repaint();
                cells[i][j].setBackground(Color.WHITE);
            }
        }
    }

    @Override
    public void run() {
    	long speed = game_controller.getSpeed();				//a kezelofeluleten megadott sebesseg lekerdezesi a controller-tol
    	game_controller.generateNextState();					//sejtter uj generaciojanak generalasa
		drawUniverse();											//az uj generalt sejtter megjelenitese a paneleken
        if(game_controller.aliveCells() != 0) {					//ha vannak meg elo sejtek a sejtterben
        	try {												
                Thread.sleep(speed);							//var egy a kezelofeluleten megadott idot
                run();											//es rekurzivan ujra hivja a run metodust
            } catch (InterruptedException e) { }
        }
    }
    
    public JPanel[][] getCells() { return cells; }
}
