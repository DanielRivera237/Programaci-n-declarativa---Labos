package org.example.screen;

import org.example.Connection;
import org.example.screen.components.Buttons;
import org.example.screen.components.PanelResult;
import org.example.screen.components.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class Window extends JFrame {

    private static final String[] EJEMPLOS = {
            "x", "log(x)", "3*x^2", "5"
    };

    private final TextField txtEntrada = new TextField("Ej: 3*x^2");
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel(" ");

    public Window(Connection motor) {
        super("Calculo simbolico en Prolog");

        txtEntrada.addActionListener(e -> ejecutar(motor, "d"));

        JPanel contenido = new JPanel();
        contenido.setLayout(
                new BoxLayout(contenido, BoxLayout.Y_AXIS)
        );
        contenido.setOpaque(false);

        agregar(contenido, etiqueta("Funcion f(x)"), 6);
        agregar(contenido, txtEntrada, 8);
        agregar(contenido, crearEjemplos(), 18);
        agregar(contenido, crearBotones(motor), 22);
        agregar(contenido, panelResultado, 10);
        agregar(contenido, crearPie(), 0);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(24, 28, 24, 28));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(760, 520));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculo simbolico");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel(
                "Derivadas, integrales, sumas y restas con Prolog"
        );
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        JPanel panel = new JPanel();
        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );
        panel.setOpaque(false);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitulo);

        return panel;
    }

    private JPanel crearEjemplos() {
        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 6, 0)
        );
        panel.setOpaque(false);

        JLabel etiqueta = new JLabel("Ejemplos:");
        etiqueta.setForeground(Theme.MUTED);
        etiqueta.setFont(Theme.sans(Font.PLAIN, 12));
        panel.add(etiqueta);

        for (String ejemplo : EJEMPLOS) {
            Buttons boton = new Buttons(
                    ejemplo,
                    Theme.CARD,
                    Theme.TEXT,
                    12,
                    6,
                    11
            );

            boton.addActionListener(e -> {
                txtEntrada.setText(ejemplo);
                txtEntrada.requestFocusInWindow();
            });

            panel.add(boton);
        }

        return panel;
    }

    private JPanel crearBotones(Connection motor) {
        Buttons derivar = new Buttons(
                "Derivar", Theme.BLUE, Theme.BG, 26, 11, 14
        );

        Buttons integrar = new Buttons(
                "Integrar", Theme.GREEN, Theme.BG, 26, 11, 14
        );

        Buttons sumar = new Buttons(
                "Sumar", Theme.AMBER, Theme.BG, 26, 11, 14
        );

        Buttons restar = new Buttons(
                "Restar", Theme.RED, Theme.BG, 26, 11, 14
        );

        derivar.addActionListener(
                e -> ejecutar(motor, "d")
        );

        integrar.addActionListener(
                e -> ejecutar(motor, "s")
        );

        sumar.addActionListener(
                e -> operarNumeros(motor, "sumar")
        );

        restar.addActionListener(
                e -> operarNumeros(motor, "restar")
        );

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );
        panel.setOpaque(false);

        panel.add(derivar);
        panel.add(integrar);
        panel.add(sumar);
        panel.add(restar);

        return panel;
    }

    private JPanel crearPie() {
        lblEstado.setFont(Theme.sans(Font.PLAIN, 12));
        lblEstado.setForeground(Theme.MUTED);

        Buttons copiar = new Buttons(
                "Copiar", Theme.CARD, Theme.TEXT, 16, 7, 12
        );

        copiar.addActionListener(e -> copiarResultado());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(lblEstado, BorderLayout.WEST);
        panel.add(copiar, BorderLayout.EAST);

        return panel;
    }

    private void ejecutar(Connection motor, String predicado) {
        String entrada = txtEntrada.getText().trim();

        String nombre = predicado.equals("d")
                ? "Derivada"
                : "Integral";

        panelResultado.setTitulo(nombre + " de f(x)");

        if (entrada.isEmpty()) {
            mostrar(
                    "",
                    "Escribe una funcion primero.",
                    Theme.AMBER
            );
            return;
        }

        try {
            String resultado = motor.consultar(
                    predicado, entrada
            );

            if (resultado.startsWith("Sin solucion")) {
                mostrar("", resultado, Theme.AMBER);
            } else {
                if (predicado.equals("s")) {
                    resultado += " + C";
                }

                mostrar(resultado, "Listo", Theme.GREEN);
            }

        } catch (Exception ex) {
            mostrar(
                    "",
                    "Expresion invalida: " + ex.getMessage(),
                    Theme.RED
            );
        }
    }

    private void operarNumeros(
            Connection motor,
            String operacion
    ) {
        String entrada = txtEntrada.getText().trim();

        panelResultado.setTitulo(
                operacion.equals("sumar")
                        ? "Resultado de la suma"
                        : "Resultado de la resta"
        );

        if (entrada.isEmpty()) {
            mostrar(
                    "",
                    "Escribe una operacion, por ejemplo: 8+3 o 8-3.",
                    Theme.AMBER
            );
            return;
        }

        String numero = "([+-]?(?:\\d+(?:[.,]\\d*)?|[.,]\\d+))";

        String signo = operacion.equals("sumar")
                ? "\\+"
                : "-";

        java.util.regex.Pattern patron =
                java.util.regex.Pattern.compile(
                        "^\\s*" + numero
                                + "\\s*" + signo
                                + "\\s*" + numero + "\\s*$"
                );

        java.util.regex.Matcher coincidencia =
                patron.matcher(entrada);

        if (!coincidencia.matches()) {
            mostrar(
                    "",
                    operacion.equals("sumar")
                            ? "Usa el formato: 8+3"
                            : "Usa el formato: 8-3",
                    Theme.AMBER
            );
            return;
        }

        try {
            double numero1 = Double.parseDouble(
                    coincidencia.group(1).replace(',', '.')
            );

            double numero2 = Double.parseDouble(
                    coincidencia.group(2).replace(',', '.')
            );

            if (!Double.isFinite(numero1)
                    || !Double.isFinite(numero2)) {
                throw new NumberFormatException();
            }

            String resultado = motor.operar(
                    operacion, numero1, numero2
            );

            if (resultado.startsWith("Sin solucion")) {
                mostrar("", resultado, Theme.AMBER);
                return;
            }

            mostrar(resultado, "Listo", Theme.GREEN);

        } catch (NumberFormatException ex) {
            mostrar(
                    "",
                    "Introduce numeros validos.",
                    Theme.AMBER
            );

        } catch (Exception ex) {
            mostrar(
                    "",
                    "Error al operar: " + ex.getMessage(),
                    Theme.RED
            );
        }
    }
    private void copiarResultado() {
        String texto = panelResultado.getTexto();

        if (texto.isEmpty()) {
            return;
        }

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(texto),
                        null
                );

        lblEstado.setText("Copiado al portapapeles");
        lblEstado.setForeground(Theme.BLUE);
    }

    private void mostrar(
            String resultado,
            String estado,
            Color color
    ) {
        panelResultado.setTexto(resultado);
        lblEstado.setText(estado);
        lblEstado.setForeground(color);
    }

    private static void agregar(
            JPanel panel,
            JComponent componente,
            int espacio
    ) {
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(componente);

        if (espacio > 0) {
            panel.add(Box.createVerticalStrut(espacio));
        }
    }

    private static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Theme.sans(Font.BOLD, 12));
        etiqueta.setForeground(Theme.MUTED);

        return etiqueta;
    }
}