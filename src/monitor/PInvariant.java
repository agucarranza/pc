package monitor;

public class PInvariant{
    private int[] plaza;
    private int valor;

    public PInvariant(int[] plaza, int valor){
        this.plaza = plaza;
        this.valor = valor;
    }

    public int[] getPlaza(){
        return this.plaza;
    }

    public int getValor(){
        return this.valor;
    }
}