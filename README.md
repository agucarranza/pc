# Informe:

https://docs.google.com/document/d/e/2PACX-1vTAV5aw7b8dYNQRvzivB1Y1ddrDI-zeYr-5REO9OFlAaBb3mITkbaniFzZbarX92N7Mvp7JWcSTeP6Z/pub

# Enunciado
En este práctico se debe resolver el control de acceso a una playa de estacionamiento con 3 entradas (calles) diferentes. En esta playa hay 2 pisos, y en cada piso pueden estacionar 30 autos. La playa cuenta con 2 salidas diferentes y una única estación de pago (caja). En los accesos a la playa y en los egresos existen barreras que deben modelarse.

La playa cuenta con lugares (3) donde los vehículos se detienen cuando quieren entrar (barrera). Una vez que ingresaron, se les indica un piso y estacionan (puede ser piso 1 o piso 2). Se debe cuidar que no se permita el ingreso (superar barrera) a más vehículos de los espacios disponibles totales. 

Los autos que se retiran de la playa deben liberar un espacio del piso en que se encontraban (diferenciar estacionamiento en cada piso). Cuando un vehículo se va a retirar puede optar por salida a calle 1 ó salida a calle 2.
Luego debe abonar la estadía. El cobro de la estadía le lleva a un empleado promedio al menos 3 minutos. (Existe una sola caja).

En caso de que la playa esté llena, se debe encender un cartel luminoso externo que indica tal situación. 
El sistema controlador debe estar conformado por distintos hilos, los cuales deben ser asignados a cada conjunto de responsabilidades afines en particular. Por ej. Ingreso de vehículos, manejo de barreras, etc.

Debe realizar:

La red de Petri que modela el sistema.

Agregar las restricciones necesarias para evitar interbloqueos ni accesos cuando no hay lugar, mostrarlo con la herramienta elegida y justificarlo.

Simular la solución en un proyecto desarrollado con la herramienta adecuada (explique porque eligió la herramienta usada).

Colocar tiempo en las estación de pago caja (en la/s transición/es correspondiente/s).

Hacer la tabla de eventos.

Hacer la tabla de estados o actividades.

Determinar la cantidad de hilos necesarios (justificarlo)

Implementar dos casos de Políticas para:

Prioridad llenar de vehículos planta baja (piso 1) y luego habilitar el piso superior. Prioridad salida indistinta (caja).

Prioridad llenado indistinta. Prioridad salida a calle 2.

Hacer el diagrama de clases.

Hacer los diagramas de secuencias.

Hacer el código.

Hacer el testing.
