package monitor;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.concurrent.Semaphore;

/**
 * Colas es la implementación de las colas necesarias para Monitor, codificadas como un Array de semáforos.
 */
class Colas {

	private Semaphore[] arregloSemaphores;

	Colas(int tamano) {

		arregloSemaphores = new Semaphore[tamano];
		for (int i = 0; i < tamano; i++) {
			arregloSemaphores[i] = new Semaphore(0, true);
		}
	}

	/**
	 * Este método libera un permiso del semáforo correspondiente a la transición del índice.
	 * @param i El índice de la transición que se va a sacar de la cola.
	 */

	void desencolar(int i) {

		if (arregloSemaphores[i] != null) {
			arregloSemaphores[i].release();
		}
	}

	RealVector quienesEstan() {

		RealVector Vc = new ArrayRealVector(arregloSemaphores.length);
		Vc.set(0);
		for (int i = 0; i < arregloSemaphores.length; i++) {
			if (arregloSemaphores[i].getQueueLength() != 0)
				Vc.setEntry(i,1);
		}
		return Vc;
	}

	/**
	 * Toma un permiso del semáforo correspondiente a la transición.
	 * @param i Recibe el índice de la transición que va a encolar.
	 * @throws InterruptedException El método acquire puede lanzar esta interrupción si el Thread es interrumpido.
	 */
	void encolar(int i) throws InterruptedException {
		if (arregloSemaphores[i] != null) {
			arregloSemaphores[i].acquire();
		}
	}

}