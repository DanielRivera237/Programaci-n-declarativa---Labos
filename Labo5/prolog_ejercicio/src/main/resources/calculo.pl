% Derivada de una suma
d(Var, F+G, DF+DG) :-
    d(Var, F, DF),
    d(Var, G, DG), !.

% Derivada de una resta
d(Var, F-G, DF-DG) :-
    d(Var, F, DF),
    d(Var, G, DG), !.

% DERIVADAS

% Derivada de una constante
d(Var, Func, 0) :-
    atomic(Var), number(Func), !.

% Derivada de la variable respecto a si misma
d(Var, Func, 1) :-
    Var == Func, !.

% Derivada de constante por variable
d(Var, N*Var, N) :-
    atomic(Var), number(N), !.

% Derivada de una potencia
d(Var, Var^N, N*Var^Nm1) :-
    atomic(Var), number(N),
    Nm1 is N - 1, !.

% Derivada de constante por potencia
d(Var, K*Var^N, C*Var^Nm1) :-
    atomic(Var), number(K), number(N),
    C is K*N,
    Nm1 is N - 1, !.

% Derivada del logaritmo natural
d(Var, log(Var), 1/Var) :-
    atomic(Var), !.


% INTEGRALES

% Integral de una constante
s(Var, K, K*Var) :-
    atomic(Var), number(K), !.

% Integral de la variable
s(Var, Var, 1/2*Var^2) :-
    atomic(Var), !.

% Integral de constante por potencia
s(Var, K*Var^N, K/M*Var^M) :-
    atomic(Var), number(K), number(N),
    N =\= -1,
    M is N + 1, !.

% Integral de una suma
s(Var, F+G, IF+IG) :-
    s(Var, F, IF),
    s(Var, G, IG), !.

% Integral de una resta
s(Var, F-G, IF-IG) :-
    s(Var, F, IF),
    s(Var, G, IG), !.

% Sumar dos numeros
sumar(A, B, R) :-
    number(A),
    number(B),
    R is A + B.

% Restar dos numeros
restar(A, B, R) :-
    number(A),
    number(B),
    R is A - B.