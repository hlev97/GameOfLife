package view;

import controller.GameOfLifeController;
import model.Universe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameOfLifePanel extends JPanel implements Runnable {
    private JPanel[][] cells;
    private GameOfLifeController game_controller;
    
    private int size;
    private Universe universe;

    public GameOfLifePanel(GameOfLifeController controller) {
        game_controller = controller;
        universe = game_controller.getUniverse();
        size = game_controller.getUniverseSize();
        cells = new JPanel[size][size];
        setLayout(new GridLayout(size, size, 1, 1));
        initUniverse();
        drawUniverse();
    }

    public void initUniverse() {
        for (int i = 0; i < game_controller.getUniverse().getSize(); i++) {
            for (int j = 0; j < game_controller.getUniverse().getSize(); j++) {
                cells[i][j] = new JPanel();
                cells[i][j].setBackground(Color.WHITE);
                add(cells[i][j]);
            }
        }
    }

    public void drawUniverse() {
        for (int i = 0; i < game_controller.getUniverse().getSize(); i++) {
            for (int j = 0; j < game_controller.getUniverse().getSize(); j++) {
                if(game_controller.getUniverse().getCurrentGen().get(i).get(j))  cells[i][j].setBackground(Color.BLACK);
                else cells[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public void clearPanel() {
        for (int i = 0; i < game_controller.getUniverse().getSize(); i++) {
            for (int j = 0; j < game_controller.getUniverse().getSize(); j++) {
                cells[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public void overwriteUniverse(ArrayList<ArrayList<Boolean>> uni) {
        game_controller.getUniverse().setCurrentGen(uni);
    }

    public void resetUniverse() {
        game_controller.getUniverse().getCurrentGen().clear();
    }

    @Override
    public void run() {
    	long speed = game_controller.getSpeed();
		universe.generator();
        drawUniverse();
        repaint();
        if(universe.aliveCells() != 0) {
        	if(universe.aliveCells() > 9000) speed = 2000;
        	try {
                Thread.sleep(speed);
                run();
            } catch (InterruptedException e) { }
        }
    }
    
    public JPanel[][] getCells() { return cells; }
}
