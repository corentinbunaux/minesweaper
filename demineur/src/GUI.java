import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Graphical user Inferface
 */

public class GUI extends JPanel{
    GUI(){

    //creation d'un JLabel
    JLabel label = new JLabel("Coucou");

    //ajout du contenu dans le JPanel
    add(label);
    }

}