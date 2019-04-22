import monitor.RdP;

public class Main {

    public static void main(String[] args) {

        RdP red = new RdP("./petri-nets/incidencia.csv",
                "./petri-nets/marcado.csv",
                "./petri-nets/inhibicion.csv");

        // red.sensibilizadas().toString();

        System.out.println(red.sensibilizadas().toString());


    }
}
