package model;

import java.util.ArrayList;
import java.util.Random;

public class Universe {
    private ArrayList<ArrayList<Boolean>> currentGen;
    private ArrayList<ArrayList<Boolean>> nextGen;
    private Rule rule;

    public Universe(int N, int[] born, int[] survive) {
        currentGen = new ArrayList<>(101);
        for (int i = 0; i < 101; i++) {
            currentGen.add(new ArrayList());
            for (int j = 0; j < 101; j++) {
                if (i > (50 - (N + 1)) && i < (50 + N + 1) && j > (50 - (N + 1)) && j < (50 + N + 1)) {
                    currentGen.get(i).add(j, Boolean.TRUE);
                } else {
                    currentGen.get(i).add(j, Boolean.FALSE);
                }
            }
        }
        rule = new Rule(born, survive);
    }

    public void FillUniverse(ArrayList<ArrayList<Boolean>> universe) {
        currentGen = universe;
    }

    public Rule getRule() { return rule; }

    public ArrayList<ArrayList<Boolean>> getCurrentGen() { return currentGen; }
    
    public ArrayList<ArrayList<Boolean>> getNextGen() { return nextGen; }

    public void setCurrentGen(ArrayList<ArrayList<Boolean>> state) {
        for (int i = 0; i < state.size(); i++) {
            for (int j = 0; j < state.get(i).size(); j++) {
                currentGen.get(i).set(j, state.get(i).get(j));
            }
        }
    }

    public void clearUniverse() {
        for (int i = 0; i < currentGen.size(); i++) {
            for (int j = 0; j < currentGen.get(i).size(); j++) {
                currentGen.get(i).set(j, Boolean.FALSE);
            }
        }
    }

    public int getSize() { return currentGen.size(); }
    
    																 
    public int countNeighbours(int i, int j) {						//A szimulacioban resztvevo cellak Moore-kornyezetben vannak, igy ha az (i,j) indexek egy
        int up = (i == 0) ? currentGen.size() - 1 : i - 1;			//	* a legfelso sorban levo cellat jelolnek ki, akkor annak a legalso sorban is vannak szomszedai
        int down = (i == currentGen.size() - 1) ? 0 : i + 1;		//	* a legalso sorban levo cellat jelolnek ki, akkor annak a legfelso sorban is vannak szomszedai
        int left = (j == 0) ? currentGen.size() - 1 : j - 1;		//	* a bal szelen levo cellat jelolnek ki, akkor annak a jobb szelen is vannak szomszedai
        int right = (j == currentGen.size() - 1) ? 0 : j + 1;		//	* a jobb szelen levo cellat jelonek ki, akkor annak a bal szelen is vannak szomszedai
        															//A fentiek miatt teljesul, hogy minden cellanak 8 szomszedja van.
        
        boolean[] neighbours = new boolean[8]; 						//Tomb amiben az aktualis cella szomszedainak allapotai vannak eltarolva
        neighbours[0] = currentGen.get(up).get(left);
        neighbours[1] = currentGen.get(up).get(j);
        neighbours[2] = currentGen.get(up).get(right);
        neighbours[3] = currentGen.get(i).get(right);
        neighbours[4] = currentGen.get(down).get(right);
        neighbours[5] = currentGen.get(down).get(j);
        neighbours[6] = currentGen.get(down).get(left);
        neighbours[7] = currentGen.get(i).get(left);

        int aliveNeighbours = 0;
        for (boolean neighbour : neighbours) {
            if (neighbour) aliveNeighbours++;
        }
        return aliveNeighbours;										//elo szomszedok szama
    }

    public int aliveCells() {
        int aliveCounter = 0;
        for (int i = 0; i < currentGen.size(); i++) {
            for (int j = 0; j < currentGen.get(i).size(); j++) {
                if(currentGen.get(i).get(j)) aliveCounter++;
            }
        } return aliveCounter;
    }

    public void generator() {
        nextGen = new ArrayList<>(currentGen.size());
        for (int i = 0; i < currentGen.size(); i++) {
            nextGen.add(new ArrayList());														
            for (int j = 0; j < currentGen.size(); j++) {
                int neighbours = countNeighbours(i, j);											//Az aktualis sejt elo szomszedaiank szam
                if(currentGen.get(i).get(j)) {													//Ha sejt el
                    if (rule.willSurvive(neighbours)) nextGen.get(i).add(j, Boolean.TRUE);		//Es az elo szomszedainak szama szerepel a survice tombben, akkor as sejt eletben  marad
                    else nextGen.get(i).add(j, Boolean.FALSE);									//Ha nem meghal
                } else {																		//Ha a sejt nem el
                    if(rule.willBorn(neighbours)) nextGen.get(i).add(j, Boolean.TRUE);			//Es az elo szomszedainak szama szerepel a born tombben, akkor a sejt eletre kel
                    else nextGen.get(i).add(j, Boolean.FALSE);									//Ha nincs benne, akkor halott marad
                }
            }
        }
        currentGen = nextGen;																	//actGen felulirasa nextGen-el
    }
    
    public void AutoFill(double probability, int size) {										//probability: annak a vszg-e, hogy egy sejt elo
        for (int i = 0; i < 101; i++) {															//size: hanyszor hanyas reszt foglaljon el a kezdoallpot a sejtterbol
            for (int j = 0; j < 101; j++) {														
                if (i > (50 - (size + 1)) && i < (50 + (size + 1)) && j > (50 - (size + 1)) && j < (50 + (size + 1))) {
                    if (Math.random() < probability) currentGen.get(i).set(j, Boolean.TRUE);
                    else currentGen.get(i).set(j, Boolean.FALSE);
                } else {
                    currentGen.get(i).set(j, Boolean.FALSE);
                }
            }
        }
    }

    public void setRule(int[] b, int[] s) {
        rule.setBorn(b);
        rule.setSurvive(s);
    }

    public void setCell(int i, int j) {
        currentGen.get(i).set(j, Boolean.TRUE);
    }
}

