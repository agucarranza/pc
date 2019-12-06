package t_inv;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class Main {

	private static boolean buscar(List<String> tInvariantes, LinkedList<String> t_busqueda, int pos1,
			LinkedList<Integer> t_posiciones, Nodo ultimo_valor) {
		int pos = pos1 + ultimo_valor.get();
		int inicio = 1;
		boolean rta=false;
		t_posiciones.add(pos);
		for (int i = 1; i < t_busqueda.size(); i++) {
			rta=false;
			for (int j = inicio; j < tInvariantes.size(); j++) {
				if (tInvariantes.get(j).equals(t_busqueda.get(i))) {
					inicio = j + 1;
					t_posiciones.add(j + pos);
					rta=true;
					break;
				}
				if (j == tInvariantes.size()) {
					System.out.println("No se encontro " + t_busqueda.get(i));
					ultimo_valor.set(inicio);
					return false;
				}
			}

		}

		ultimo_valor.set(inicio - 1 + pos);

		return rta;
	}

	public static void main(String[] args) {
		
		
		LinkedList<String> tInvariantes ;
	
		LinkedList<String> A1 = new LinkedList<String>(Arrays.asList("T1","T4"));
		LinkedList<String> A2= new LinkedList<String>(Arrays.asList("T2","T5"));
		LinkedList<String> A3= new LinkedList<String>(Arrays.asList("T3","T6"));
		LinkedList<String> B1= new LinkedList<String>(Arrays.asList("T7","T8"));
		LinkedList<String> B2= new LinkedList<String>(Arrays.asList("T9","T10","T11","T12"));
		LinkedList<String> C1= new LinkedList<String>(Arrays.asList("T13","T15"));
		LinkedList<String> C2= new LinkedList<String>(Arrays.asList("T14","T17"));
		tInvariantes= new LinkedList<String>();
			
	URL path = Main.class.getResource("log.txt");
	File archivo = new File(path.getFile());
	Scanner s;
	try {
	   s = new Scanner(archivo);
	   while (s.hasNextLine()) {
		   String linea = s.nextLine();
		   tInvariantes.add(linea);
		 		}
	   s.close();
	} catch (FileNotFoundException e) {
	   e.printStackTrace();
	}
	
/*	for (Nodo nodo : tInvariantes) {
		System.out.println(nodo.tinv +" "+Boolean.toString(nodo.leido));
	}
	*/	
	
	if(!tinv_cartel(tInvariantes)) {
		System.out.println("No satisface el test de T-Invariantes");
		System.exit(0); 
	}
	
	while(!tInvariantes.isEmpty()) {
	int t1,t2,tmin=999999 ;
	
	LinkedList<String> inv=null;
	if(tInvariantes.getFirst().equals("T1")) {
		inv=A1;
		System.out.println("A1");
	}
	else if(tInvariantes.getFirst().equals("T2")) {
		inv=A2;
		System.out.println("A2");

	}
	else if(tInvariantes.getFirst().equals("T3")) {
		inv=A3;
		System.out.println("A3");

	}
	else {
		System.out.println("No satisface el test de T-Invariantes");
		System.exit(0); 
	}
	LinkedList<Integer> taux = new LinkedList<Integer>();
	List<String> aux= tInvariantes.subList(0, tInvariantes.size());
	Nodo ultimo_valor= new Nodo(0);
	if(buscar(aux,inv,0,taux, ultimo_valor)){
		System.out.println("Ultimo valor parte 1: " + ultimo_valor.get());

	
	List<String> aux2= tInvariantes.subList(ultimo_valor.get(), tInvariantes.size());
	t1= aux2.indexOf("T7");
	if (t1==-1) t1=99999;
	t2 = aux2.indexOf("T9");
	if (t2==-1) t2=99999;
	
	if(t1!=t2) {
			
		if (t1<t2) {
	    tmin = t1;
	    inv=B1;
		System.out.println("B1");

	}
	else {
		tmin = t2;
	    inv=B2;
		System.out.println("B2");

	}
	
	aux2= aux2.subList(tmin, aux2.size());
	
	if(buscar(aux2,inv,tmin,taux,ultimo_valor)) {
	
	System.out.println("Ultimo valor parte 2: " + ultimo_valor.get());
	
	aux2= tInvariantes.subList(ultimo_valor.get(), tInvariantes.size());
	t1= aux2.indexOf("T13");
	if (t1==-1) t1=99999;
	t2 = aux2.indexOf("T14");
	if (t2==-1) t2=99999;
	
	if(t1!=t2) {
		System.out.println("No se encontro T13 o T14");
			
	if (t1<t2) {
	    tmin = t1;
	    inv=C1;
		System.out.println("C1");

	}
	else {
		tmin = t2;
	    inv=C2;
		System.out.println("C2");

	}
	
	aux2= aux2.subList(tmin, aux2.size());
	
	buscar(aux2,inv,tmin,taux,ultimo_valor);
	
	System.out.println("Ultimo valor parte 2: " + ultimo_valor.get());
	
	System.out.println(taux.toString());
	}
	}
	}
	}
	borrar(tInvariantes, taux);
	
	System.out.println(tInvariantes.toString());

	}
	
	System.out.println("T-Invariantes Verificadas con éxito");

	
	
	}

	private static boolean tinv_cartel(LinkedList<String> tInvariantes) {
		// TODO Auto-generated method stub
		int cartel =0; // T0 -> 0  / T16 ->1
		boolean rta= true;
		for(int i=0;i<tInvariantes.size();i++) {
			String tinv = tInvariantes.get(i);
			
			if(tinv.equals("T0")) {
				if (cartel==0) {
					cartel =1;
					tInvariantes.remove(i);
				}
				else {
					return false;
				}
				
			}
			
			if(tinv.equals("T16")) {
				if (cartel==1) {
					cartel =0;
					tInvariantes.remove(i);
				}
				else {
					return false;
				}
				
			}
				
		}
		
		
		return rta;
		
	}

	private static void borrar(LinkedList<String> tInvariantes, LinkedList<Integer> taux) {
		// TODO Auto-generated method stub

		for(int i=0;i<taux.size();i++)
		{
			int tinv = taux.get(i)-i;

			tInvariantes.remove(tinv);
		}

	}
}
