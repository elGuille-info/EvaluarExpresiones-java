/**
 * Para indicar el color del texto o del fondo al mostrarlo por la consola.
 * El 3 en el segundo valor es para el texto.
 * El 4 en el segundo valor es para el fondo.
 *
 * @author Guillermo Som (Guille) basado en ejemplos de la web.
 * @version 1.0.0.0
 */
public final class ConsoleColor {
    static final String black = "\033[30m";
    static final String red = "\033[31m";
    static final String redBack = "\033[41m";
    static final String green = "\033[32m";
    static final String greenBack = "\033[42m";
    static final String yellow = "\033[33m";
    static final String yellowBack = "\u001B[43m";
    static final String blue = "\033[34m";
    static final String purple = "\033[35m";
    static final String cyan = "\033[36m";
    static final String white = "\033[37m";
    static final String reset = "\u001B[0m";
}
