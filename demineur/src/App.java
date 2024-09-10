import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * App
 * 
 * @author Corentin Bunaux
 * @version 0.0
 *          This program prints "Hello, World!" to the console.
 */

public class App extends JFrame{
    /**
     * Main method
     * 
     * @param args not used
     * @throws Exception it's not my problem
     */

    App() {
        Champ champ = new Champ();
        champ.init(10, 10, 7);
        champ.display();

        //creation d'un JPanel
        GUI gui = new GUI(champ);
        
        //affectation du JPanel dans la JFrame
        setContentPane(gui);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
}
