package monitor;

public class Matriz {
    private int[][] m;

    public Matriz(int fil, int col) {
        m = new int[fil][col];
    }

    public Matriz(int[][] matriz) {
        this.m = matriz;
    }

    public int[][] getMatriz() {
        return m;
    }

    public int getValor(int fil, int col) {
        return m[fil][col];
    }

    public void setValor(int fil, int col, int valor) {
        this.m[fil][col] = valor;
    }

    public int getFilas() {
        return m.length;
    }

    public int getColumnas() {
        return m[0].length;
    }

    public void clear() {
        for (int i = 0; i < this.getFilas(); i++) {
            for (int j = 0; j < this.getColumnas(); j++) {
                this.setValor(i, j, 0);
            }
        }
    }

    public Matriz and(Matriz B) {
        Matriz A = this;
        Matriz Result = new Matriz(B.getFilas(), B.getColumnas());

        for (int i = 0; i < A.getFilas(); i++) {
            for (int j = 0; j < A.getColumnas(); j++) {
                if (A.getValor(i, j) == 1 && B.getValor(i, j) == 1) {
                    Result.setValor(i, j, 1);
                } else {
                    Result.setValor(i, j, 0);
                }
            }
        }
        return Result;
    }

    public Matriz punto(Matriz B) {
        Matriz A = this;
        if (A.getColumnas() != B.getFilas()) {
            throw new RuntimeException("Dimensiones no compatibles.");
        }

        Matriz C = new Matriz(A.getFilas(), B.getColumnas());
        for (int i = 0; i < C.getFilas(); i++) {
            for (int j = 0; j < C.getColumnas(); j++) {
                for (int k = 0; k < A.getColumnas(); k++) {
                    C.setValor(i, j, C.getValor(i, j) + (A.getValor(i, k) * B.getValor(k, j)));
                }
            }
        }
        return C;
    }

    public Matriz suma(Matriz B) {
        Matriz A = this;
        if (B.getFilas() != A.getFilas() || B.getColumnas() != A.getColumnas()) {
            throw new RuntimeException("Dimensiones incompatibles");
        }
        Matriz C = new Matriz(getFilas(), getColumnas());
        for (int i = 0; i < getFilas(); i++) {
            for (int j = 0; j < getColumnas(); j++) {
                C.setValor(i, j, A.getValor(i, j) + B.getValor(i, j));
            }
        }
        return C;
    }

    public Matriz transpuesta() {
        Matriz A = new Matriz(this.getColumnas(), this.getFilas());
        for (int i = 0; i < this.getFilas(); i++) {
            for (int j = 0; j < this.getColumnas(); j++) {
                A.setValor(j, i, this.getValor(i, j));
            }
        }
        return A;
    }

    public boolean esCero() {
        int aux = 0;

        for (int i = 0; i < this.getFilas(); i++)
            for (int j = 0; j < this.getColumnas(); j++)
                aux = this.getValor(i, j) + aux;

        return aux == 0;
    }

    public boolean esUno() {
        int aux = 0;

        for (int i = 0; i < this.getFilas(); i++)
            for (int j = 0; j < this.getColumnas(); j++)
                aux = this.getValor(i, j) + aux;

        return aux == 1;
    }

    public String toString() {
        String texto = "";
        for (int i = 0; i < this.getFilas(); i++) {
            for (int j = 0; j < this.getColumnas(); j++) {
                texto += this.m[i][j] + " ";
            }
            texto += "\n";
        }
        return texto;
    }
}
