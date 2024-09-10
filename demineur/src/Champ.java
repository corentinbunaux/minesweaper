public class Champ {
    private int[][] field;
    int nbOfMines;

    public Champ() {
    }

    public void display() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                if(this.field[i][j] == -1) {
                    System.out.print("X");
                }
                else if(this.field[i][j] == 0) {
                    System.out.print(".");
                }
                else {
                    System.out.print(this.field[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void init(int i, int j, int nbOfMines) {
        //init field 
        this.field = new int[i][j];
        this.nbOfMines = nbOfMines;
        for(int k=0; k<this.field.length; k++) {
            for(int l=0; l<this.field[0].length; l++) {
                this.field[k][l] = 0;
            }
        }

        //place mines
        int cpt = nbOfMines;
        while(cpt > 0) {    
            int x = (int)(Math.random() * i);
            int y = (int)(Math.random() * j);
            if(this.field[x][y] != -1) {
                this.field[x][y] = -1;
                this.increseNeighbours(x, y);
                cpt--;
            }
        }
    }

    public int getVal(int i, int j) {
        return this.field[i][j];
    }

    public void endGame() {
        System.out.println("Game over!");
    } 

    public void increseNeighbours(int x, int y){
        for(int k=-1; k<=1; k++) {
            for(int l=-1; l<=1; l++) {
                if(x+k >= 0 && x+k < this.field.length && y+l >= 0 && y+l < this.field[0].length && this.getVal(x+k, y+l) != -1) {
                    this.field[x+k][y+l]++;
                }
            }
        }
    }
}
