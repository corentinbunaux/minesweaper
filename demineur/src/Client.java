import java.net.*;
import java.io.*;

public class Client {
    String name;
    App app;

    public Client(String name, App app) {
        this.name = name;
        this.app = app;
        try {
            // ouverture de la socket et des streams
            Socket sock = new Socket("localhost", 8080);
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            // envoi du nom
            out.writeUTF(this.name);

            // reception d’un nombre
            int numJoueur = in.readInt();
            System.out.println("Joueur n°:" + numJoueur);

            String selectedLevel = in.readUTF();
            System.out.println("Niveau de difficulté: " + selectedLevel);

            app.getGUI().restartGame(app, selectedLevel);

            // fermeture Stream
            in.close();
            out.close();
            sock.close(); // fermeture Socket
        } catch (UnknownHostException e) {
            System.out.println("localhost est inconnue");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}