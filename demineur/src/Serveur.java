import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur {
    private static List<Socket> clients = Collections.synchronizedList(new ArrayList<>());
    public static int nbJoueur = 0;
    private int[][] field;
    private boolean gameStarted = false;
    public int nbPlayersRemaining = 0;
    int scores[];
    int nbSpotsRemaining = 95;

    public static void main(String[] args) {
        Serveur server = new Serveur();

        System.out.println("Démarrage du serveur");
        try {
            // gestionnaire de socket, port 8080
            ServerSocket gestSock = new ServerSocket(8080);

            while (true) {
                Socket socket = gestSock.accept();
                clients.add(socket);
                DataInputStream entree = new DataInputStream(socket.getInputStream());
                DataOutputStream sortie = new DataOutputStream(socket.getOutputStream());
                nbJoueur++;
                server.setnbPlayersRemaining(server.getnbPlayersRemaining() + 1);
                sortie.writeInt(nbJoueur);

                new Thread(new ClientHandler(socket, nbJoueur, server)).start();
                server.notifyAllClientsString("newPlayer");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateFieldValues(int x, int y, int value) {
        this.field[x][y] = value;
    }

    public void updateFieldSize(int x, int y) {
        this.field = new int[x][y];
    }

    public int[][] getField() {
        return this.field;
    }

    public void setNbJoueur(int nbJoueur) {
        this.nbJoueur = nbJoueur;
    }

    public int getNbJoueur() {
        return this.nbJoueur;
    }

    public int getnbPlayersRemaining() {
        return nbPlayersRemaining;
    }

    public void setnbPlayersRemaining(int nbPlayersRemaining) {
        this.nbPlayersRemaining = nbPlayersRemaining;
    }

    public void displayField() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                if (this.field[i][j] == -1) {
                    System.out.print("X");
                } else if (this.field[i][j] == 0) {
                    System.out.print(".");
                } else {
                    System.out.print(this.field[i][j]);
                }
            }
            System.out.println();
        }
    }

    public synchronized boolean isGameStarted() {
        return gameStarted;
    }

    public synchronized void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        notifyAllClientsString("GameStarted");
    }

    public static void notifyAllClientsString(String message) {
        synchronized (clients) {
            Iterator<Socket> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Socket client = iterator.next();
                if (client.isClosed() || !client.isConnected()) {
                    iterator.remove();
                    continue;
                }
                try {
                    DataOutputStream clientOut = new DataOutputStream(client.getOutputStream());
                    clientOut.writeUTF(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    iterator.remove();
                }
            }
        }
    }

    public static void notifyAllClientsInt(int value) {
        synchronized (clients) {
            Iterator<Socket> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Socket client = iterator.next();
                if (client.isClosed() || !client.isConnected()) {
                    iterator.remove();
                    continue;
                }
                try {
                    DataOutputStream clientOut = new DataOutputStream(client.getOutputStream());
                    clientOut.writeInt(value);
                } catch (IOException e) {
                    e.printStackTrace();
                    iterator.remove();
                }
            }
        }
    }

    public void setScores(int numJoueur) {
        scores[numJoueur - 1]++;
    }

    public void initialiseField(int nbJoueur) {
        scores = new int[nbJoueur];
        for (int i = 0; i < nbJoueur; i++) {
            scores[i] = 0;
        }
    }

    public void setNbSPotsRemaining(int nbSpotsRemaining) {
        this.nbSpotsRemaining = nbSpotsRemaining;
    }

    public int getNbSpotsRemaining() {
        return this.nbSpotsRemaining;
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private static Champ champ;
    private static int nbJoueur;
    private static String selectedLevel;
    String message;
    Serveur server;

    public ClientHandler(Socket socket, int nbJoueur, Serveur server) {
        this.socket = socket;
        this.nbJoueur = nbJoueur;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DataInputStream entree = new DataInputStream(socket.getInputStream());
            DataOutputStream sortie = new DataOutputStream(socket.getOutputStream());

            // lecture d’une donnée
            String nomJoueur = entree.readUTF();
            selectedLevel = entree.readUTF();
            System.out.println(nomJoueur + " connected");

            champ = new Champ();
            champ.init(selectedLevel);

            sortie.writeUTF("Bienvenue " + nomJoueur + " !");
            sortie.writeUTF("Vous êtes le joueur n°" + nbJoueur);
            sortie.writeUTF("numJoueur");
            sortie.writeInt(nbJoueur);
            server.initialiseField(nbJoueur);

            while (true) {
                try {
                    message = entree.readUTF();
                    if (message.equals("endGame")) {
                        int numJoueur = entree.readInt();
                        server.setnbPlayersRemaining(server.getnbPlayersRemaining() - 1);
                    } else if (message.equals("endGameAll")) {
                        server.setnbPlayersRemaining(0);
                    } else if (message.equals("click")) {
                        int x = entree.readInt();
                        int y = entree.readInt();
                        int joueur = entree.readInt();
                        int gameStarted = entree.readInt();

                        if (gameStarted == 0) {
                            server.updateFieldSize(champ.getHeight(), champ.getWidth());
                            for (int i = 0; i < champ.getHeight(); i++) {
                                for (int j = 0; j < champ.getWidth(); j++) {
                                    int value = entree.readInt();
                                    server.updateFieldValues(i, j, value);
                                    server.notifyAllClientsString("firstclick");
                                    server.notifyAllClientsInt(i);
                                    server.notifyAllClientsInt(j);
                                    server.notifyAllClientsInt(value);
                                }
                            }
                        }
                        server.notifyAllClientsString("click");
                        server.notifyAllClientsInt(x);
                        server.notifyAllClientsInt(y);
                        server.notifyAllClientsInt(joueur);

                        if (server.getField()[x][y] != -1) {
                            server.setScores(joueur);
                            server.setNbSPotsRemaining(server.getNbSpotsRemaining() - 1);
                        }
                    }

                    if (server.getnbPlayersRemaining() == 0 || server.getNbSpotsRemaining() == 0) {
                        server.notifyAllClientsString("endGameAll");
                        server.notifyAllClientsInt(server.scores.length);

                        for (int i = 0; i < server.scores.length; i++) {
                            server.notifyAllClientsInt(server.scores[i]);
                        }
                    }

                } catch (EOFException e) {
                    System.out.println("Client disconnected");
                    server.notifyAllClientsString("playerDisconnected");
                    nbJoueur--;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}