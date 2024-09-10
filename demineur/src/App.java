/**
 * App
 * @author Corentin Bunaux
 * @version 0.0
 * This program prints "Hello, World!" to the console.
 */

public class App {
    /**
     * Main method
     * @param args not used
     * @throws Exception it's not my problem
     */
    public static void main(String[] args) throws Exception {
        Champ champ = new Champ();
        champ.init(10, 10,7);
        champ.display();
    }
}
