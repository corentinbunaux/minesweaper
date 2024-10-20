
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Graphical user Inferface
 */

public class GUI extends JPanel {
    private int score = 0;
    private String[] levels = { "Facile", "Moyen", "Difficile" };
    private String selectedLevel = "Facile";
    JPanel panelTop, panelMines, panelBottom;
    Compteur labelScore;
    JTextField textField;
    Case[][] listCases;
    List<Client> clients = new ArrayList<Client>();
    JDialog pleaseWaitDialog;

    GUI(App app) {
        panelTop = new JPanel();
        panelMines = new JPanel();
        panelBottom = new JPanel();
        labelScore = new Compteur();
        textField = new JTextField(10);

        setLayout(new GridLayout(0, 1));
        initMenu(app);
        initPanelTop(app);
        initPanelMines(app, selectedLevel);
        initPanelBottom(app);
    }

    Case getCase(int x, int y) {
        return listCases[x][y];
    }

    void initPanelTop(App app) {
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new FlowLayout());

        JLabel labelName = new JLabel("Pseudo: ");
        textField.setText("Anonyme");

        panelTop.add(labelName);
        panelTop.add(textField);
        panelTop.add(labelScore);

        // Niveaux de difficultés
        JComboBox<String> comboBoxLevel = new JComboBox<>(this.levels);
        comboBoxLevel.setSelectedIndex(0);
        comboBoxLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!GUI.this.selectedLevel.equals(comboBoxLevel.getSelectedItem())) {
                    GUI.this.selectedLevel = (String) comboBoxLevel.getSelectedItem();
                    restartGame(app, selectedLevel);
                }
            }
        });
        panelTop.add(comboBoxLevel);
        add(panelTop);
    }

    void initPanelMines(App app, String selectedLevel) {
        setLayout(new GridLayout(0, 1));

        // JPanel des mines
        panelMines.setLayout(new GridLayout(app.getChamp().getSizes()[indexOf(selectedLevel)],
                app.getChamp().getSizes()[indexOf(selectedLevel)]));

        listCases = new Case[app.getChamp().getSizes()[indexOf(selectedLevel)]][app.getChamp()
                .getSizes()[indexOf(selectedLevel)]];

        // creation des JLabels pour les mines
        for (int i = 0; i < app.getChamp().getSizes()[indexOf(selectedLevel)]; i++) {
            for (int j = 0; j < app.getChamp().getSizes()[indexOf(selectedLevel)]; j++) {
                Case labelMine;
                labelMine = new Case(app, i, j);
                panelMines.add(labelMine);
                listCases[i][j] = labelMine;
            }
        }
        add(panelMines);
    }

    void initPanelBottom(App app) {
        ImageIcon restartIcon = new ImageIcon("../ressources/restart.png");
        ImageIcon quitIcon = new ImageIcon("../ressources/quit.png");

        Dimension buttonSize = new Dimension(48, 48);
        JButton buttonRestart = new JButton(restartIcon);
        buttonRestart.setPreferredSize(buttonSize);
        buttonRestart.setMinimumSize(buttonSize);
        buttonRestart.setMaximumSize(buttonSize);

        JButton buttonQuit = new JButton(quitIcon);
        buttonQuit.setPreferredSize(buttonSize);
        buttonQuit.setMinimumSize(buttonSize);
        buttonQuit.setMaximumSize(buttonSize);

        buttonRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame(app, selectedLevel);
            }
        });

        buttonQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelBottom.add(buttonRestart);
        panelBottom.add(buttonQuit);
        add(panelBottom);
    }

    void initMenu(App app) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPartie = new JMenu("Partie");
        menuBar.add(menuPartie);
        JMenuItem mMultijoueur = new JMenuItem("Multijoueur", KeyEvent.VK_M);
        JMenuItem mQuitter = new JMenuItem("Quitter", KeyEvent.VK_Q);
        menuPartie.add(mMultijoueur);
        menuPartie.add(mQuitter);

        mMultijoueur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (app.getMultiplayerStatus() == true) {
                    return;
                }
                Client client = new Client(textField.getText(), app);
                clients.add(client);
                app.setMultiplayerStatus(true);
            }
        });

        mQuitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        app.setJMenuBar(menuBar);
    }

    void restartGame(App app, String selectedLevel) {
        app.getGUI().getLabelScore().reinitScore();
        app.getGUI().getLabelScore().setGameStarted(false);
        app.setGameStarted(false);
        panelMines.removeAll();
        app.getChamp().init(selectedLevel);

        panelMines.setLayout(new GridLayout(app.getChamp().getSizes()[indexOf(selectedLevel)],
                app.getChamp().getSizes()[indexOf(selectedLevel)]));
        // creation des JLabels pour les mines
        listCases = new Case[app.getChamp().getSizes()[indexOf(selectedLevel)]][app.getChamp()
                .getSizes()[indexOf(selectedLevel)]];

        for (int i = 0; i < app.getChamp().getSizes()[indexOf(selectedLevel)]; i++) {
            for (int j = 0; j < app.getChamp().getSizes()[indexOf(selectedLevel)]; j++) {
                Case labelMine;
                labelMine = new Case(app, i, j);
                panelMines.add(labelMine);
                listCases[i][j] = labelMine;
            }
        }
        add(panelMines);
        add(panelBottom);
        app.pack();
    }

    JPanel getPanelTop() {
        return panelTop;
    }

    JPanel getPanelMines() {
        return panelMines;
    }

    JPanel getPanelBottom() {
        return panelBottom;
    }

    Compteur getLabelScore() {
        return labelScore;
    }

    int indexOf(String selectedLevel) {
        switch (selectedLevel) {
            case "Facile":
                return 0;
            case "Moyen":
                return 1;

            case "Difficile":
                return 2;
            default:
                break;
        }
        return 0;
    }

    String getSelectedLevel() {
        return this.selectedLevel;
    }

    void propagate(App app, int xCoord, int yCoord) {
        if (app.getMultiplayerStatus() == true) {
            return;
        }
        app.getChamp().downgradeNbRemainingSpots();
        if (app.getGUI().getCase(yCoord, xCoord).getTxt() == "F") {
            app.getChamp().incrementNbFlags();
        }
        if (app.getChamp().getVal(xCoord, yCoord) != 0) {
            app.getGUI().getCase(yCoord, xCoord).setIsDiscovered(true, Color.LIGHT_GRAY);
            return;
        } else {
            app.getGUI().getCase(yCoord, xCoord).setIsDiscovered(true, Color.LIGHT_GRAY);

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == j && i == 0) {
                        continue;
                    }

                    int newX = xCoord + i;
                    int newY = yCoord + j;

                    if (newX >= 0
                            && newX < app.getChamp().getSizes()[app.getGUI().indexOf(app.getGUI().getSelectedLevel())]
                            && newY >= 0
                            && newY < app.getChamp().getSizes()[app.getGUI().indexOf(app.getGUI().getSelectedLevel())]
                            && !app.getGUI().getCase(newY, newX).getIsDiscovered()) {
                        propagate(app, newX, newY);
                    }
                }
            }
        }
    }

    public void endGame(App app, boolean win) {
        app.getGUI().getLabelScore().setGameStarted(false);

        String[] options = { "Restart", "Exit" };
        String message = win ? "You win ! " : "You lose... ";

        revealAllGrid();

        // Show the custom dialog
        int choice = JOptionPane.showOptionDialog(
                null,
                message + "What would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            restartGame(app, this.selectedLevel);
        } else if (choice == 1) {
            System.exit(0);
        }
    }

    void revealAllGrid() {
        for (int i = 0; i < listCases.length; i++) {
            for (int j = 0; j < listCases[i].length; j++) {
                listCases[i][j].setIsDiscovered(true, Color.LIGHT_GRAY);
            }
        }
    }

    List<Client> getClients() {
        return this.clients;
    }

    public void endGameMultiplayer(App app, boolean wait, int scores[]) {
        // Create and show the "Please Wait" dialog on the Event Dispatch Thread
        if (wait) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    pleaseWaitDialog = new JDialog((JFrame) null, "Please Wait", true);
                    pleaseWaitDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    pleaseWaitDialog.setSize(300, 150);
                    pleaseWaitDialog.setLocationRelativeTo(null);
                    pleaseWaitDialog.add(new JLabel("Please wait...", SwingConstants.CENTER));
                    pleaseWaitDialog.setVisible(true);
                }
            });
        } else {
            if (pleaseWaitDialog != null) {
                pleaseWaitDialog.dispose();
            }

            String[] options = { "Restart", "Exit" };
            StringBuilder ranking = new StringBuilder("Classement : \n");

            // transform the scores into a list of maps
            Map<Integer, Integer> scoresList = new HashMap<>();
            for (int i = 0; i < scores.length; i++) {
                scoresList.put(i, scores[i]);
            }

            // sort the scores
            SortedSet<Map.Entry<Integer, Integer>> scoresSorted = new TreeSet<Map.Entry<Integer, Integer>>(
                    new Comparator<Map.Entry<Integer, Integer>>() {
                        @Override
                        public int compare(Map.Entry<Integer, Integer> e1, Map.Entry<Integer, Integer> e2) {
                            return e2.getValue().compareTo(e1.getValue());
                        }
                    });
            scoresSorted.addAll(scoresList.entrySet());

            for (Map.Entry<Integer, Integer> entry : scoresSorted) {
                ranking.append("Player ").append(entry.getKey() + 1).append(": ").append(entry.getValue()).append("\n");
            }

            // Show the custom dialog
            int choice = JOptionPane.showOptionDialog(
                    null,
                    ranking.toString(),
                    "Game Over",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                restartGame(app, "Facile");
            } else if (choice == 1) {
                System.exit(0);
            }
        }
    }
}