import javax.swing.JLabel;
import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemTray;
import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Compteur extends JLabel implements Runnable {
    private Thread processScores;
    private int score;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getAscent();
    }

    Compteur() {
        processScores = new Thread(this);
        setText("Score : " + score);
        processScores.start();
    }

    public void run() {
        while (processScores != null) {
            try {
                Thread.sleep(1000);
                this.score++;
                afficheScore();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void afficheScore() {
        this.setText("Score : " + score);
    }

    void reinitScore() {
        this.score = 0;
    }

    void stop() {
        processScores = null;
    }
}