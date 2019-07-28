import log.Log;
import monitor.GestorDeMonitor;
import monitor.PInvariant;
import monitor.RdP;
import monitor.Tarea;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);

        RdP red = new RdP ("./petri-nets/incidencia.csv",
                "./petri-nets/marcado.csv",
                "./petri-nets/incidencianeg.csv",
                "./petri-nets/alfa.csv",
                "./petri-nets/beta.csv");
        new Log();

        GestorDeMonitor monitor = new GestorDeMonitor(red);
        ArrayList<Tarea> ListaTareas = new ArrayList<>();

        red.setPInvariants
                (new PInvariant[]
                        {
                                //faltan actualizar p invariantes, y obvio las transiciones por hilo
                                new PInvariant(new int[]{1,10,12,13,14,17,19,2,4,6,8,23}, 86), //AUTOS TOTALES
                                new PInvariant(new int[]{19,20,23}, 1), // CAJA
                                new PInvariant(new int[]{10,12,13,21,8}, 60), // LUGARES DE ESTACIONAMIENTO
                                new PInvariant(new int[]{10,11}, 30), //LUGARES PISO 1
                                new PInvariant(new int[]{13,15,12}, 30), //LUGARES PISO 2
                                new PInvariant(new int[]{12,14,16}, 1), //RAMPA
                                new PInvariant(new int[]{17,18,14}, 2), //LUGARES SALIDA
                                new PInvariant(new int[]{2,3}, 2), //CALLE ENTRADA 1
                                new PInvariant(new int[]{4,5}, 2), //CALLE ENTRADA 2
                                new PInvariant(new int[]{6,7}, 2), //CALLE ENTRADA 3
                                new PInvariant(new int[]{9,8}, 2), //LUGARES ENTRADA
                                new PInvariant(new int[]{0,22}, 1), //CARTEL
                        });


        int[] t1 = {0,16};
        int[] t2 = {1};
        int[] t3 = {13,15};
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
        int[] t14 = {14,17};
        int[] t15 = {12};
    
        ListaTareas.add(new Tarea(monitor, t12));
        ListaTareas.add(new Tarea(monitor, t2));
        ListaTareas.add(new Tarea(monitor, t3));
        ListaTareas.add(new Tarea(monitor, t4));
        ListaTareas.add(new Tarea(monitor, t5));
        ListaTareas.add(new Tarea(monitor, t6));
        ListaTareas.add(new Tarea(monitor, t7));
        ListaTareas.add(new Tarea(monitor, t8));
        ListaTareas.add(new Tarea(monitor, t9));
        ListaTareas.add(new Tarea(monitor, t10));
        ListaTareas.add(new Tarea(monitor, t11));
        ListaTareas.add(new Tarea(monitor, t1));
        ListaTareas.add(new Tarea(monitor, t13));
        ListaTareas.add(new Tarea(monitor, t14));
        ListaTareas.add(new Tarea(monitor, t15));
        Thread hilo;

        for(Tarea tarea: ListaTareas){
            hilo = new Thread(tarea);
            hilo.start();
        }


    }
}