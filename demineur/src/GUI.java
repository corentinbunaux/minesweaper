import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;

/**
 * Graphical user Inferface
 */

public class GUI extends JPanel {
    GUI(Champ champ) {
        setLayout(new GridLayout(0, 1));

        // creation d'un JLabel
        JLabel label = new JLabel("Démineur");

        // ajout du contenu dans le JPanel
        add(label);

        // JPanel des mines
        JPanel panelMines = new JPanel();
        panelMines.setLayout(new GridLayout(champ.getWidth(), champ.getHeight()));

        // creation des JLabels pour les mines
        for (int i = 0; i < champ.getWidth(); i++) {
            for (int j = 0; j < champ.getHeight(); j++) {
                JLabel labelMine;
                if(champ.getVal(i, j) == -1){
                    labelMine = new JLabel("*");
                    labelMine.setForeground(Color.RED);
                }
                else if(champ.getVal(i, j) == 0){
                    labelMine = new JLabel(".");
                }
                else{
                    labelMine = new JLabel(String.valueOf(champ.getVal(i, j)));
                }
                panelMines.add(labelMine);
            }
        }
        add(panelMines);
    }

}