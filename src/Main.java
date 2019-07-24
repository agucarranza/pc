import log.Log;
import monitor.GestorDeMonitor;
import monitor.PInvariant;
import monitor.RdP;
import monitor.Tarea;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        RdP red = new RdP ( "./petri-nets/incidencia.csv",
                "./petri-nets/marcado.csv",
                // "./petri-nets/inhibicion.csv",
                "./petri-nets/alfa.csv",
                "./petri-nets/beta.csv");
        new Log();

        GestorDeMonitor monitor = new GestorDeMonitor(red);
        ArrayList<Tarea> ListaTareas = new ArrayList<>();

        red.setPInvariants
                (new PInvariant[]
                        {
                                new PInvariant(new int[]{9, 4, 5, 6}, 10), // LUGARES DE ESTACIONAMIENTO
                                new PInvariant(new int[]{11, 12}, 1), // CAJA
                                new PInvariant(new int[]{0, 10, 11, 1, 2, 3, 4, 5, 6}, 40), // AUTOS TOTALES
                                new PInvariant(new int[]{5, 7}, 5), //LUGARES PISO 1
                                new PInvariant(new int[]{6, 8}, 5), //LUGARES PISO 2
                        });

        int[] t1 = {0};
        int[] t2 = {1};
        int[] t3 = {12};
        int[] t4 = {2};
        int[] t5 = {3};
        int[] t6 = {4};
        int[] t7 = {5};
        int[] t8 = {6};
        int[] t9 = {7};
        int[] t10 = {10};
        int[] t11 = {11};
        int[] t12 = {9};
        int[] t13 = {8};
        int[] t14 = {13};

        ListaTareas.add(new Tarea(monitor, t1));
        ListaTareas.add(new Tarea(monitor,t2));
        ListaTareas.add(new Tarea(monitor, t3));
        ListaTareas.add(new Tarea(monitor,t4));
        ListaTareas.add(new Tarea(monitor, t5));
        ListaTareas.add(new Tarea(monitor,t6));
        ListaTareas.add(new Tarea(monitor, t7));
        ListaTareas.add(new Tarea(monitor,t8));
        ListaTareas.add(new Tarea(monitor, t9));
        ListaTareas.add(new Tarea(monitor,t10));
        ListaTareas.add(new Tarea(monitor, t11));
        ListaTareas.add(new Tarea(monitor,t12));
        ListaTareas.add(new Tarea(monitor, t13));
        ListaTareas.add(new Tarea(monitor,t14));
        Thread hilo;

        for(Tarea tarea: ListaTareas){
            hilo = new Thread(tarea);
            hilo.start();
        }
    }
}