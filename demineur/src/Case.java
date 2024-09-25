import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemTray;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Case extends JPanel implements MouseListener {
    private static final int DIM = 20;
    private String txt = "";
    private boolean isDiscovered = false;
    private App app;
    private int xCase;
    private int yCase;

    public Case() {
        setPreferredSize(new Dimension(DIM, DIM));
        addMouseListener(this);
    }

    public Case(App app, int i, int j) {
        setPreferredSize(new Dimension(DIM, DIM));
        addMouseListener(this);
        this.app = app;
        this.xCase = j;
        this.yCase = i;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(txt);
        int textHeight = fm.getAscent();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2;
        g.drawString(txt, x, y);
        if (isDiscovered) {
            setBackground(Color.GREEN);
        }
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        setBorder(border);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!app.getGameStarted()) {
            app.setGameStarted(true);
            this.app.getChamp().spawnMines(app.getChamp().getHeight(), app.getChamp().getWidth(), xCase, yCase);
        }

        if (this.app.getChamp().getVal(xCase, yCase) == -1) {
            txt = "*";
        } else {
            txt = Integer.toString(this.app.getChamp().getVal(xCase, yCase));
            if (this.app.getChamp().getVal(xCase, yCase) == 0) {
                txt = " ";
                app.getGUI().propagate(app, xCase, yCase);
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        isDiscovered = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setBackground(Color.GREEN);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(Color.GRAY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(null);
    }

    boolean getIsDiscovered(){
        return this.isDiscovered;
    }

    void setIsDiscovered(boolean val){
        this.isDiscovered = val;
        txt = Integer.toString(this.app.getChamp().getVal(xCase, yCase));
    }
}