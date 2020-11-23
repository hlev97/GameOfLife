package view;

import controller.GameOfLifeController;
import controller.StateIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

public class GameOfLifeFrame extends JFrame {
    private GameOfLifeController game_controller;

    private JMenuBar menubar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenuItem save, load;
    private JMenu settings = new JMenu("Simulation");
    private JMenuItem stop, start, speed;
    private JMenu universe = new JMenu("Universe");
    private JMenuItem rule, autofill, clear;
    private JMenu help = new JMenu("Help");
    private JMenuItem lifefamily, cellular, doc;

    private GameOfLifePanel panel;
    private Thread simulation;

    public GameOfLifeFrame(GameOfLifeController controller) {
        super("Game Of Life");
        game_controller = controller;
        setJMenuBar(menubar);

        menubar.add(file);
        save = new JMenuItem("Save state");
        file.add(save);
        save.addActionListener(new SaveActionListener());
        load = new JMenuItem("Load state");
        file.add(load);
        load.addActionListener(new LoadActionListener());

        menubar.add(settings);
        start = new JMenuItem("Start");
        settings.add(start);
        start.addActionListener(new StartActionListener());
        stop = new JMenuItem("Stop");
        settings.add(stop);
        stop.addActionListener(new StopActionListener());
        stop.setEnabled(false);
        settings.add(new JSeparator());
        speed = new JMenuItem("Set speed");
        settings.add(speed);
        speed.addActionListener(new SetSpeedActionListener());

        menubar.add(universe);
        rule = new JMenuItem("Set rule");
        universe.add(rule);
        rule.addActionListener(new SetRuleActionListener());
        universe.add(new JSeparator());
        autofill = new JMenuItem("Autofill");
        universe.add(autofill);
        autofill.addActionListener(new AutofillActionListener());
        clear = new JMenuItem("Clear");
        universe.add(clear);
        clear.addActionListener(new ClearActionListener());

        menubar.add(help);
        lifefamily = new JMenuItem("Rules lexicon");
        help.add(lifefamily);
        lifefamily.addActionListener(new RulesLexiconActionListener());
        
        cellular = new JMenuItem("What is a cellular automaton?");
        help.add(cellular);
        cellular.addActionListener(new CellularActionListener());
        
        doc = new JMenuItem("Documentation");
        help.add(doc);
        doc.addActionListener(new DocActionListener());

        panel = new GameOfLifePanel(game_controller);
        panel.setSize(700, 700);
        add(panel);

        setSize(700, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    public GameOfLifePanel getPanel() { return panel; }

    private class StartActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(start)) {
                start.setEnabled(false);
                stop.setEnabled(true);
                clear.setEnabled(false);
                rule.setEnabled(false);
                autofill.setEnabled(false);
                load.setEnabled(false);
                simulation = new Thread(panel);
                simulation.start();
            }
        }
    }

    private class StopActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(stop)) {
                start.setEnabled(true);
                stop.setEnabled(false);
                clear.setEnabled(true);
                rule.setEnabled(true);
                autofill.setEnabled(true);
                load.setEnabled(true);
                simulation.interrupt();
            }
        }
    }

    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(save)) {
                try {
                    StateIO.writeToCSV(game_controller.getUniverseArray());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class LoadActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                panel.clearPanel();
                game_controller.getUniverse().clearUniverse();
                game_controller.getUniverse().FillUniverse(StateIO.readFromCSV("actualstate.csv"));
                panel.drawUniverse();
                panel.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class AutofillActionListener implements ActionListener {
    	public int percentage = 100;
    	public int S = 3;
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(autofill)) {

                JFrame autofillFrame = new JFrame("Autofill");
                autofillFrame.setSize(450, 100);
                autofillFrame.setResizable(false);
                autofillFrame.setLocationRelativeTo(null);
                autofillFrame.setVisible(true);

                JPanel AutoFillPanel = new JPanel();
                JLabel percentageLabel = new JLabel("Choose probability:");
                AutoFillPanel.add(percentageLabel);
                Integer[] percentages = {15, 25, 35, 50, 100};
                JComboBox comboBox = new JComboBox(percentages);
                AutoFillPanel.add(comboBox);
                autofillFrame.add(AutoFillPanel);
                comboBox.setSelectedItem(percentage);

                JLabel sizeLabel = new JLabel("%     Choose size:");
                AutoFillPanel.add(sizeLabel);
                Integer[] sizes = {3, 5, 15, 25, 35, 45, 55, 85};
                JComboBox comboBox2 = new JComboBox(sizes);
                AutoFillPanel.add(comboBox2);
                autofillFrame.add(AutoFillPanel);
                comboBox2.setSelectedItem(S);

                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        percentage = (Integer) comboBox.getSelectedItem();
                    }
                });

                comboBox2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        S = (Integer) comboBox2.getSelectedItem();
                    }
                });

                JButton generate = new JButton("Generate!");
                generate.setSize(3, 5);
                AutoFillPanel.add(generate);
                generate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panel.clearPanel();
                        double P = (double) percentage / 100;
                        game_controller.getUniverse().clearUniverse();
                        game_controller.getUniverse().AutoFill(P, S/2);
                        panel.drawUniverse();
                        panel.repaint();
                        autofillFrame.dispose();
                    }
                });
            }
        }
    }

    private class ClearActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            panel.clearPanel();
            game_controller.getUniverse().clearUniverse();
            panel.drawUniverse();
            panel.repaint();
            start.setEnabled(true);
        }
    }

    private class SetRuleActionListener implements ActionListener {
    	public int[] bornArray;
        public int[] surviveArray;
    	int selectedItemIndex = 6;
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(rule)) {
                JFrame ruleFrame = new JFrame("Rule");
                ruleFrame.setSize(300, 60);
                ruleFrame.setResizable(false);
                ruleFrame.setLocationRelativeTo(null);
                ruleFrame.setVisible(true);
                
                JPanel rulePanel = new JPanel();
                JLabel ruleLabel = new JLabel("Choose rule:");
                rulePanel.add(ruleLabel);
                
                String[] rules = {"Game Of Life", "Life Without Death", "Replicator", "Serviettes", "Maze", "Cave", "Custom"};
                JComboBox comboBox = new JComboBox(rules);
                rulePanel.add(comboBox);
                ruleFrame.add(rulePanel);
               
                comboBox.setSelectedIndex(selectedItemIndex);
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        switch((String) comboBox.getSelectedItem()) {
                            case "Game Of Life":
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{2, 3});
                                selectedItemIndex = 0;
                                break;
                            case "Life Without Death":
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
                                selectedItemIndex = 1;
                                break;
                            case "Replicator":
                                game_controller.getUniverse().setRule(new int[]{1, 3, 5, 7}, new int[]{1, 3, 5, 7});
                                selectedItemIndex = 2;
                                break;
                            case "Serviettes":
                                game_controller.getUniverse().setRule(new int[]{2, 3, 4}, new int[]{});
                                selectedItemIndex = 3;                                break;
                            case "Maze":
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{1, 2, 3, 4, 5});
                                selectedItemIndex = 4;
                                break;
                            case "Cave":
                                game_controller.getUniverse().setRule(new int[]{6, 7, 8}, new int[]{3, 4, 5, 6, 7, 8});
                                selectedItemIndex = 5;
                                break;
                            case "Custom":
                                JFrame CustomFrame = new JFrame("Custom");

                                CustomFrame.setSize(300, 130);
                                CustomFrame.setResizable(false);
                                CustomFrame.setLocationRelativeTo(null);
                                CustomFrame.setVisible(true);

                                JPanel CustomPanel = new JPanel();
                                JLabel bornLabel = new JLabel("Born:     ");
                                JTextField born = new JTextField(16);
                                JLabel surviveLabel = new JLabel("Survive: ");
                                JTextField survive = new JTextField(16);
                                CustomPanel.add(bornLabel);
                                CustomPanel.add(born);
                                CustomPanel.add(surviveLabel);
                                CustomPanel.add(survive);

                                JButton ruleButton = new JButton("Rule the universe!");
                                CustomPanel.add(ruleButton);
                                CustomFrame.add(CustomPanel);

                                ruleButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                    	try {
	                                        String[] bornIn = born.getText().replace(" ", "").split(",");
	                                        bornArray = new int[bornIn.length];
	                                        for (int i = 0; i < bornIn.length; i++) {	                                        	
                                        		if (Integer.parseInt(bornIn[i]) <= 8 && Integer.parseInt(bornIn[i]) >= 0) {
                                        			bornArray[i] = Integer.parseInt(bornIn[i]);
                                        		} else throw new NumberFormatException();	                                        	
	                                        }
	
	                                        String[] surviveIn = survive.getText().replace(" ", "").split(",");
	                                        surviveArray = new int[surviveIn.length];
	                                        for (int i = 0; i < surviveIn.length; i++) {	                                        	
                                        		if (Integer.parseInt(surviveIn[i]) <= 8 && Integer.parseInt(surviveIn[i]) >= 0 ) {
                                        			surviveArray[i] = Integer.parseInt(surviveIn[i]);
                                        		} else {
                                        			throw new NumberFormatException();
                                        		}	                                       
	                                        }
	                                        game_controller.getUniverse().setRule(bornArray, surviveArray);
                                    	} catch (NumberFormatException ex) {
                                    		JOptionPane.showMessageDialog(null, "A cell can only have neighbours between 0 and 8.", "Neighbour num error", JOptionPane.ERROR_MESSAGE);
                                    	}
                                        CustomFrame.dispose();
                                    }
                                });
                                selectedItemIndex = 6;
                                break;
                        }
                        ruleFrame.dispose();
                    }
                });
            }
        }
    }

    private class SetSpeedActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(speed)) {
                JFrame speedFrame = new JFrame("Speed");
                speedFrame.setSize(300, 60);
                speedFrame.setResizable(false);
                speedFrame.setLocationRelativeTo(null);
                speedFrame.setVisible(true);
                
                JPanel speedPanel = new JPanel();
                JLabel speedLabel = new JLabel("Set speed");
                speedPanel.add(speedLabel);
                
                Long[] longs = {100L,200L,500L,1000L,2000L, 3000L};
                JComboBox comboBox = new JComboBox(longs);
                speedFrame.add(comboBox);
                comboBox.setSelectedItem(game_controller.getSpeed());
                comboBox.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        game_controller.setSpeed((Long) comboBox.getSelectedItem());
                        speedFrame.dispose();
                    }
                });
            }
        }
    }

    private class RulesLexiconActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(lifefamily)) {
            	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                try {
                    desktop.browse(new URI("http://psoup.math.wisc.edu/mcell/rullex_life.html"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "http://psoup.math.wisc.edu/mcell/rullex_life.html", "Life family is not reached", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private class CellularActionListener implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(cellular)) {
            	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                try {
                    desktop.browse(new URI("https://en.wikipedia.org/wiki/Life-like_cellular_automaton"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "https://en.wikipedia.org/wiki/Life-like_cellular_automaton", "Life-like cellular automaton is not reached", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private class DocActionListener implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(doc)) {
            	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                try {
                    desktop.browse(new URI("https://bmeedu-my.sharepoint.com/:b:/g/personal/leventeheizer_edu_bme_hu/EfVrZAKafs5Nl8pu-mT1a50BQzNy0cWZEr4ri3MBDC3vFQ?e=7Q6zjt"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "https://bmeedu-my.sharepoint.com/:b:/g/personal/leventeheizer_edu_bme_hu/EfVrZAKafs5Nl8pu-mT1a50BQzNy0cWZEr4ri3MBDC3vFQ?e=7Q6zjt", "Life-like cellular automaton is not reached", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
