package view;

import controller.GameOfLifeController;
import controller.StateIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameOfLifeFrame extends JFrame {
    GameOfLifeController game_controller;

    public int[] bornArray;
    public int[] surviveArray;

    public int percentage;
    public int S;

    private JMenuBar menubar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenuItem save, load;
    private JMenu settings = new JMenu("Simulation");
    private JMenuItem stop, start, autofill, clear;
    private JMenu universe = new JMenu("Universe");
    private JMenuItem size, rule, speed;
    private JMenu help = new JMenu("Help");
    private JMenuItem descrip;

    GameOfLifePanel panel;
    Thread simulation;

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
        descrip = new JMenuItem("Description");
        help.add(descrip);
        descrip.addActionListener(new DescriptionActionListener());

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
                } catch (IOException ioException) {
                    ioException.printStackTrace();
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
                game_controller.getUniverse().FillUniverse(StateIO.readFromCSV());
                panel.drawUniverse();
                panel.revalidate();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private class AutofillActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(autofill)) {

                JFrame autofillFrame = new JFrame("Autofill");
                autofillFrame.setSize(400, 100);
                autofillFrame.setResizable(false);
                autofillFrame.setLocationRelativeTo(null);
                autofillFrame.setVisible(true);

                JPanel AutoFillPanel = new JPanel();
                JLabel percentageLabel = new JLabel("Choose percentage: ");
                AutoFillPanel.add(percentageLabel);
                Integer[] percentages = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90};
                JComboBox comboBox = new JComboBox(percentages);
                AutoFillPanel.add(comboBox);
                autofillFrame.add(AutoFillPanel);


                JLabel sizeLabel = new JLabel("Choose size: ");
                AutoFillPanel.add(sizeLabel);
                Integer[] sizes = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
                JComboBox comboBox2 = new JComboBox(sizes);
                AutoFillPanel.add(comboBox2);
                autofillFrame.add(AutoFillPanel);

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
                        game_controller.getUniverse().clearUniverse();
                        game_controller.getUniverse().AutoFill(percentage, S);
                        panel.drawUniverse();
                        panel.revalidate();
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
            panel.revalidate();
        }
    }

    private class SetRuleActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(rule)) {
                JFrame ruleFrame = new JFrame("Rule");
                ruleFrame.setSize(300, 60);
                ruleFrame.setResizable(false);
                ruleFrame.setLocationRelativeTo(null);
                ruleFrame.setVisible(true);
                JPanel rulePanel = new JPanel();
                JLabel ruleLabel = new JLabel("Choose rule: ");
                rulePanel.add(ruleLabel);
                String[] rules = {"Game Of Life", "Life Without Death", "Replicator", "Serviettes", "Maze", "Cave", "Custom"};
                JComboBox comboBox = new JComboBox(rules);
                rulePanel.add(comboBox);
                ruleFrame.add(rulePanel);
                comboBox.setSelectedItem(game_controller.getUniverse().getRule());
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        switch((String) comboBox.getSelectedItem()) {
                            case "Game Of Life":
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{2, 3});
                                break;
                            case "Life Without Death":
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
                                break;
                            case "Replicator":
                                game_controller.getUniverse().setRule(new int[]{1, 3, 5, 7}, new int[]{1, 3, 5, 7});
                                break;
                            case "Serviettes":
                                game_controller.getUniverse().setRule(new int[]{2, 3, 4}, new int[]{});
                                break;
                            case "Maze":
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{1, 2, 3, 4, 5});
                                break;
                            case "Cave":
                                game_controller.getUniverse().setRule(new int[]{6, 7, 8}, new int[]{3, 4, 5, 6, 7, 8});
                                break;
                            case "Custom":
                                JFrame CustomFrame = new JFrame("Custom");

                                CustomFrame.setSize(300, 130);
                                CustomFrame.setResizable(false);
                                CustomFrame.setLocationRelativeTo(null);
                                CustomFrame.setVisible(true);

                                JPanel CustomPanel = new JPanel();
                                JLabel bornLabel = new JLabel("Born:     ");
                                final JTextField born = new JTextField(16);
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
                                        String[] bornIn = born.getText().split(",");
                                        bornArray = new int[bornIn.length];
                                        for (int i = 0; i < bornIn.length; i++) {
                                            bornArray[i] = Integer.parseInt(bornIn[i]);
                                        }

                                        String[] surviveIn = survive.getText().split(",");
                                        surviveArray = new int[surviveIn.length];
                                        for (int i = 0; i < surviveIn.length; i++) {
                                            surviveArray[i] = Integer.parseInt(surviveIn[i]);
                                        }
                                        game_controller.getUniverse().setRule(bornArray, surviveArray);
                                        CustomFrame.dispose();
                                    }
                                });
                                break;
                            default:
                                game_controller.getUniverse().setRule(new int[]{3}, new int[]{2, 3});
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
                Long[] longs = {100L,200L,500L,1000L};
                final JComboBox comboBox = new JComboBox(longs);
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

    private class DescriptionActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(descrip)) {
                JOptionPane.showMessageDialog(null, "Bla-bla\nbla-bla\nbla-bla");
            }
        }
    }
}
