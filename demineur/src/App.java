import javax.swing.JFrame;

/**
 * App
 * 
 * @author Corentin Bunaux
 * @version 0.0
 *          This program prints "Hello, World!" to the console.
 */

public class App extends JFrame {
    /**
     * Main method
     * 
     * @param args not used
     * @throws Exception it's not my problem
     */
    Champ champ;
    GUI gui;
    int score = 0;
    boolean gameStarted = false;

    App() {
        champ = new Champ(this);
        champ.init(10, 10, 7);
        gui = new GUI(this);

        setContentPane(gui);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    Champ getChamp() {
        return champ;
    }

    GUI getGUI() {
        return gui;
    }

    boolean getGameStarted() {
        return gameStarted;
    }

    void setGameStarted(boolean val) {
        this.gameStarted = val;
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
}
