public class Champ {
    private int[][] field;
    int nbOfMines;
    private int[] sizes = { 10, 15, 20 };
    private int[] tabMines = { 5, 10, 15 };
    private int numberSafeRemaining, nbOfFlags;

    public Champ() {
    }

    public void display() {
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

    void init(String selectedLevel) {
        if (selectedLevel.equals("Facile")) {
            this.field = new int[sizes[0]][sizes[0]];
            this.nbOfMines = tabMines[0];

        } else if (selectedLevel.equals("Moyen")) {
            this.field = new int[sizes[1]][sizes[1]];
            this.nbOfMines = tabMines[1];

        } else if (selectedLevel.equals("Difficile")) {
            this.field = new int[sizes[2]][sizes[2]];
            this.nbOfMines = tabMines[2];
        }
        this.numberSafeRemaining = this.field.length * this.field[0].length - this.nbOfMines;
        this.nbOfFlags = this.nbOfMines;
        resetField();
    }

    public void resetField() {
        for (int k = 0; k < this.field.length; k++) {
            for (int l = 0; l < this.field[0].length; l++) {
                this.field[k][l] = 0;
            }
        }
        this.numberSafeRemaining = this.field.length * this.field[0].length - this.nbOfMines;
        this.nbOfFlags = this.nbOfMines;
    }

    // Fait apparaitre des mines
    public void spawnMines(int dimX, int dimY, int xClick, int yClick) {
        int cpt = nbOfMines;
        while (cpt > 0) {
            int x = (int) (Math.random() * dimX);
            int y = (int) (Math.random() * dimY);
            if (this.field[x][y] != -1 && x != xClick && y != yClick) {
                this.field[x][y] = -1;
                this.increseNeighbours(x, y);
                cpt--;
            }
        }
    }

    public int getVal(int i, int j) {
        return this.field[i][j];
    }

    public int getHeight() {
        return this.field.length;
    }

    public int getWidth() {
        return this.field[0].length;
    }

    public void increseNeighbours(int x, int y) {
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (x + k >= 0 && x + k < this.field.length && y + l >= 0 && y + l < this.field[0].length
                        && this.getVal(x + k, y + l) != -1) {
                    this.field[x + k][y + l]++;
                }
            }
        }
    }

    int[] getSizes() {
        return sizes;
    }

    void downgradeNbRemainingSpots() {
        this.numberSafeRemaining--;
    }

    int getNumberSafeRemaining() {
        return this.numberSafeRemaining;
    }

    public int decrementNbFlags() {
        System.out.println(this.nbOfFlags);
        return this.nbOfFlags--;
    }

    public int incrementNbFlags() {
        System.out.println(this.nbOfFlags);
        return this.nbOfFlags++;
    }

    public int getNbFlags() {
        return this.nbOfFlags;
    }
}
