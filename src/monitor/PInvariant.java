package monitor;

public class PInvariant{
    private int[] plaza;
    private int valor;

    public PInvariant(int[] plaza, int valor){
        this.plaza = plaza;
        this.valor = valor;
    }

    int[] getPlaza() {
        return this.plaza;
    }

    int getValor() {
        return this.valor;
    }
}