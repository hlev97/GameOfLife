package controller;

import java.io.*;
import java.util.ArrayList;

public class StateIO {
    private static final String CSV_SEPARATOR = ";";
    public static void writeToCSV(ArrayList<ArrayList<Boolean>> universe) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("actualstate.csv"), "UTF-8"));
            for (int i = 0; i < universe.size(); i++) {
                StringBuilder line = new StringBuilder();
                int j;
                for (j = 0; j < universe.get(i).size() - 1; j++) {
                    line.append(universe.get(i).get(j) ? 0 : 1);
                    line.append(CSV_SEPARATOR);
                } 
                line.append(universe.get(i).get(j) ? 0 : 1);
                bw.write(line.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) { }
    }

    //Egy *.csv fajlbol valo olvasas
    public static ArrayList<ArrayList<Boolean>> readFromCSV(String file) throws IOException {
        ArrayList<ArrayList<Boolean>> readGen = new ArrayList<>(101);
        for (int i = 0; i < 101; i++) {
            readGen.add(new ArrayList());
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for(int i = 0; i < 101; i++) {
                String line = br.readLine();
                String[] cells = line.split(CSV_SEPARATOR);
                for (String cell : cells) {
                    readGen.get(i).add(cell.equals("0"));
                }
            }
            br.close();
        } catch (IOException e) { } 
        return readGen;
    }
}
