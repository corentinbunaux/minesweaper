import java.io.*;
import java.net.*;

public class Serveur {
    public static void main(String[] args) {
        System.out.println("Démarrage du serveur");
        try {
            // gestionnaire de socket, port 8080
            ServerSocket gestSock = new ServerSocket(8080);

            while (true) {
                Socket socket = gestSock.accept(); // attente
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private static int numJoueur = 0;
    private static Champ champ;
    private static String selectedLevel = "Facile";

    public ClientHandler(Socket socket) {
        this.socket = socket;
        numJoueur++;
    }

    @Override
    public void run() {
        try {
            // ouverture des streams
            DataInputStream entree = new DataInputStream(socket.getInputStream());
            DataOutputStream sortie = new DataOutputStream(socket.getOutputStream());

            champ = new Champ();
            champ.init(selectedLevel);

            // lecture d’une donnée
            String nomJoueur = entree.readUTF();
            System.out.println(nomJoueur + " connected");

            // envoi d’une donnée : 0 par exemple
            sortie.writeInt(numJoueur);
            sortie.writeUTF(selectedLevel);

            // un peu de ménage
            sortie.close();
            entree.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}