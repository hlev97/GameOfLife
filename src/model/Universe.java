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
                if (i > (50 - (N + 1)) && i < (50 + N) && j > (50 - (N + 1)) && j < (50 + N)) {
                    if (Math.random() * 100 < 50) currentGen.get(i).add(j, Boolean.TRUE);
                    else currentGen.get(i).add(j, Boolean.FALSE);
                } else {
                    currentGen.get(i).add(j, Boolean.FALSE);
                }
            }
        }
        rule = new Rule(born, survive);
    }

    public Universe(int N) {
        rule = new Rule(new int[]{3}, new int[]{2, 3});
        currentGen = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            currentGen.add(new ArrayList());
            for (int j = 0; j < N; j++) {
                currentGen.get(i).add(j, Boolean.FALSE);
            }
        }
    }

    public void FillUniverse(ArrayList<ArrayList<Boolean>> universe) {
        currentGen = universe;
    }

    public Rule getRule() { return rule; }

    public ArrayList<ArrayList<Boolean>> getCurrentGen() { return currentGen; }

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

    public int countNeighbours(int i, int j) {
        int up = (i == 0) ? currentGen.size() - 1 : i - 1;
        int down = (i == currentGen.size() - 1) ? 0 : i + 1;
        int left = (j == 0) ? currentGen.size() - 1 : j - 1;
        int right = (j == currentGen.size() - 1) ? 0 : j + 1;

        boolean[] neighbours = new boolean[8];
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
        return aliveNeighbours;
    }

    public void print() {
        for(int i = 0; i < currentGen.size(); i++) {
            for(int j = 0; j < currentGen.get(i).size(); j++) {
                System.out.print(currentGen.get(i).get(j) ? '0' : ' ');
            } System.out.println();
        } System.out.println();
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
                int neighbours = countNeighbours(i, j);
                if(currentGen.get(i).get(j)) {
                    if (rule.willSurvive(neighbours)) nextGen.get(i).add(j, Boolean.TRUE);
                    else nextGen.get(i).add(j, Boolean.FALSE);
                } else {
                    if(rule.willBorn(neighbours)) nextGen.get(i).add(j, Boolean.TRUE);
                    else nextGen.get(i).add(j, Boolean.FALSE);
                }
            }
        }
        currentGen = nextGen;
    }

    public void AutoFill(int percentage, int size) {
        for (int i = 0; i < 101; i++) {
            for (int j = 0; j < 101; j++) {
                if (i > (50 - (size)) && i < (50 + (size)) && j > (50 - (size)) && j < (50 + (size))) {
                    if (Math.random() * 100 < percentage) currentGen.get(i).set(j, Boolean.TRUE);
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

