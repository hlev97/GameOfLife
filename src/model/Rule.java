package model;

public class Rule {
    private int[] born;									//azokat a szomszedszamokat tartalmazza, amelyek mellett egy halott sejt elove valik
    private int[] survive;								//azokat a szomszedszamokat tartalmazza, amelyek mellett egy elo sejt eletben marad

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
    
    //Metodus, ami ellenorzi, hogy a bemeneti parameterkent megadott szomszedszam benne van-e a survive tomb-ben
    public boolean willSurvive(int neighbours) {
        for (int i : survive) {							//vegig megy a survive tomb elemein
            if(i == neighbours) return true;			//ha valamelyik egyezik a szomszedszammal, akkor igaz ertekkel ter vissza
        }
        return false;									//ha nem, akkor hamissal
    }
    
    //Metodus, ami ellenorzi, hogy a bemeneti parameterkent megadott szomszedszam benne van-e a born tomb-ben
    public boolean willBorn(int neighbours) {
        for (int i : born) {							//vegig megy a born tomb elemein
            if (i == neighbours) return true;			//ha valamelyik egyezik a szomszedszammal, akkor igaz ertekkel ter viisza
        }
        return false;									//ha nem, akkor hamissal
    }
}
