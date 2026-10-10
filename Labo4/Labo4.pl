% Convierte el entero a positivo y obtiene sus digitos.
almacenar(N, L) :-
    integer(N),
    Positivo is abs(N),
    digitos(Positivo, L).

% Caso base: un numero de un solo digito.
digitos(N, [N]) :-
    N < 10.

% Caso recursivo: separa el ultimo digito.
digitos(N, [Digito|Resto]) :-
    N >= 10,
    Digito is N mod 10,
    Cociente is N // 10,
    digitos(Cociente, Resto).
