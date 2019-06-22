
%     T0 T1 T2 T3
I = [-1  1  0  0;   %P0
      1 -1  0  0;   %P1
     -1  1 -1  1;   %P2
      0  0 -1  1;   %P3
      0  0  1 -1];  %P4

M0 = [5 0 3 2 0];

%enabled = [1 0 1 0];

disparo = [0 1 0 0];

M1 = M0.' + I * disparo.'

% Para que una transición este habilitada, necesito que 
% cuando quiera disparar, el marcado no me de negativo.
% El numero negativo indica la cantidad de tokens que me falta.

vs = [1 0 1 0];
vc = [1 1 0 0];

vs.*vc

% Para hacer un AND en un Array binario es equivalente a multiplicar
% elemento a elemento.


