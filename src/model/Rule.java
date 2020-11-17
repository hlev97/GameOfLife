package model;

public class Rule {
    private int[] born;
    private int[] survive;

    public Rule(int[] b, int[] s) {
        this.born = b;
        this.survive = s;
    }

    public void setBorn(int[] born) {
        this.born = born;
    }

    public void setSurvive(int[] survive) {
        this.survive = survive;
    }

    public int[] getBorn() {
        return born;
    }

    public int[] getSurvive() {
        return survive;
    }

    public boolean willSurvive(int neighbours) {
        for (int i : survive) {
            if(i == neighbours) return true;
        }
        return false;
    }

    public boolean willBorn(int neighbours) {
        for (int i : born) {
            if (i == neighbours) return true;
        }
        return false;
    }
}
