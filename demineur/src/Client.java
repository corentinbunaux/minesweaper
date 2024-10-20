import java.net.*;
import java.util.Map;
import java.io.*;
import java.awt.Color;

public class Client {
    private String name;
    private App app;
    private String broadcastMessage;
    private static int nbPlayers;
    private boolean connected = false;
    private volatile boolean running = true;
    private int playerNumber;
    Socket sock;
    DataOutputStream out;
    DataInputStream in;
    int scores[];

    public Client(String name, App app) {
        this.name = name;
        this.app = app;
        this.nbPlayers = 0;
        try {
            // ouverture de la socket et des streams
            sock = new Socket("localhost", 8080);
            out = new DataOutputStream(sock.getOutputStream());
            in = new DataInputStream(sock.getInputStream());

            nbPlayers = in.readInt();
            playerNumber = nbPlayers;

            // envoi du nom
            out.writeUTF(this.name);
            out.writeUTF("Facile");

            // Continuously read messages
            new Thread(() -> {
                try {
                    while (running) {
                        broadcastMessage = in.readUTF();
                        if (broadcastMessage.equals("exit")) {
                            System.out.println("Closing socket due to 'exit' message");
                            stopClient();
                            break;
                        } else if (broadcastMessage.equals("Facile")) {
                            app.getGUI().restartGame(app, broadcastMessage);
                        } else if (broadcastMessage.equals("newPlayer")) {
                            if (connected) {
                                nbPlayers++;
                            } else {
                                connected = true;
                            }
                            System.out.println("Nouveau joueur connecté, nombre de joueurs en ligne : " + nbPlayers);
                        } else if (broadcastMessage.equals("playerDisconnected")) {
                            nbPlayers--;
                            System.out.println("Nombre de joueurs en ligne : " + nbPlayers);
                        } else if (broadcastMessage.equals("GameStarted")) {
                            app.setGameStarted(true);
                        } else if (broadcastMessage.equals("firstclick")) {
                            int x = in.readInt();
                            int y = in.readInt();
                            int value = in.readInt();
                            app.getChamp().setField(x, y, value);
                        } else if (broadcastMessage.equals("displayField")) {
                            app.getChamp().display();
                        } else if (broadcastMessage.equals("click")) {
                            int x = in.readInt();
                            int y = in.readInt();
                            int numJoueur = in.readInt();
                            app.getGUI().getCase(y, x).setIsDiscovered(true, getPlayerColor(numJoueur));
                        } else if (broadcastMessage.equals("numJoueur")) {
                            int numJoueur = in.readInt();
                            app.setNumJoueur(numJoueur);
                        } else if (broadcastMessage.equals("endGameAll")) {
                            int nbScores = in.readInt();
                            scores = new int[nbScores];
                            for (int i = 0; i < nbScores; i++) {
                                scores[i] = in.readInt();
                            }
                            app.getGUI().revealAllGrid();
                            app.getGUI().endGameMultiplayer(this.app, false, scores);
                        } else {
                            System.out.println("Broadcast: " + broadcastMessage);
                        }
                    }
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (

        UnknownHostException e) {
            System.out.println("localhost est inconnue");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessageToServer(String message) {
        try {
            out.writeUTF(message);
            if (message.equals("exit")) {
                stopClient();
            }
        } catch (UnknownHostException e) {
            System.out.println("localhost est inconnue");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendIntToServer(int i) {
        try {
            out.writeInt(i);
        } catch (UnknownHostException e) {
            System.out.println("localhost est inconnue");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Color getPlayerColor(int playerNumber) {
        switch (playerNumber) {
            case 1:
                return Color.CYAN;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.ORANGE;
            default:
                return Color.LIGHT_GRAY;
        }
    }

    private synchronized void stopClient() {
        running = false;
        nbPlayers--;
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (sock != null)
                sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getPlayerNumber() {
        return playerNumber;
    }
}