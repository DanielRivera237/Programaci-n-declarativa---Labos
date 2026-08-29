% personaje(Nombre, Ocupacion, Edad).

personaje(leon, agente, 27).
personaje(ashley, estudiante, 20).
personaje(ada, espia, 26).
personaje(luis, investigador, 32).


% arma(Personaje, Arma).

arma(leon, pistola).
arma(leon, cuchillo).
arma(ada, pistola).
arma(luis, cuchillo).


% enemigo(Nombre, Infeccion).

enemigo(ganado, las_plagas).
enemigo(regenerador, las_plagas).


% aparece_en(PersonajeOEnemigo, Lugar).

aparece_en(ganado, pueblo).
aparece_en(ganado, castillo).
aparece_en(regenerador, isla).

aparece_en(leon, pueblo).
aparece_en(luis, pueblo).
aparece_en(ada, pueblo).
aparece_en(ada, castillo).


% dificultad(Lugar, Nivel).

dificultad(pueblo, alta).
dificultad(castillo, alta).
dificultad(isla, muy_alta).