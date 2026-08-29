:- consult('hechos.pl').


% Regla 1:
% Un personaje esta armado si posee al menos un arma.

esta_armado(Personaje) :-
    personaje(Personaje, _, _),
    arma(Personaje, _).


% Regla 2:
% Un personaje o enemigo esta en una zona peligrosa
% si aparece en un lugar con dificultad alta
% O con dificultad muy alta.

esta_en_zona_peligrosa(Entidad) :-
    aparece_en(Entidad, Lugar),
    (dificultad(Lugar, alta);
     dificultad(Lugar, muy_alta)).


% Regla 3:
% Un personaje es mayor de 25 si su edad
% es superior a 25.

es_mayor_de_25(Personaje) :-
    personaje(Personaje, _, Edad),
    Edad > 25.