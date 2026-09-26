% Casos base
fibonacci(0, 0).
fibonacci(1, 1).

% Caso recursivo
fibonacci(N, F) :-
    integer(N),
    N > 1,
    N1 is N - 1,
    N2 is N - 2,
    fibonacci(N1, F1),
    fibonacci(N2, F2),
    F is F1 + F2.
