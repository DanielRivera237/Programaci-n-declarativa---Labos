package org.example;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

import java.io.InputStream;

public class Connection {

    private final Prolog engine = new Prolog();

    public Connection() throws Exception {
        try (InputStream in =
                     getClass().getResourceAsStream("/calculo.pl")) {

            if (in == null) {
                throw new IllegalStateException(
                        "No se encontro calculo.pl en resources"
                );
            }

            engine.setTheory(new Theory(in));
        }
    }

    public String consultar(
            String predicado,
            String expresion
    ) throws Exception {

        SolveInfo info = engine.solve(
                predicado + "(x, " + expresion + ", R)."
        );

        if (!info.isSuccess()) {
            return "Sin solucion para esa expresion";
        }

        return info.getTerm("R").toString();
    }

    public String operar(
            String operacion,
            double numero1,
            double numero2
    ) throws Exception {

        if (!operacion.equals("sumar") && !operacion.equals("restar")) {
            throw new IllegalArgumentException("Operacion no permitida");
        }

        SolveInfo info = engine.solve(
                operacion + "(" + numero1 + ", " + numero2 + ", R)."
        );

        if (!info.isSuccess()) {
            return "Sin solucion";
        }

        return info.getTerm("R").toString();
    }
}