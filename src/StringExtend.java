/**
 * Extensiones para las cadenas. (19/nov/22 04.13)
 *
 * @author Guillermo Som (Guille)
 * @version 1.0.2.0 del 22-nov-2022
 */
public class StringExtend {

    public static void main (String[] args) {
        String hola = "Hola";
        var anyOf = "aeiou";
        int pos = indexOfAny(hola, anyOf.toCharArray());
        System.out.println("Usando indexOfAny:");
        if (pos == -1) {
            System.out.printf("En '%s' no está ninguno de los caracteres de '%s'.\n", hola, anyOf);
        }
        else {
            System.out.printf("En '%s' el primero de los caracteres de '%s' hallado está en la posición %d.\n", hola, anyOf, pos);
        }
        System.out.println();

        System.out.println("Usando firstIndexOfAny:");
        pos = firstIndexOfAny(hola, anyOf.toCharArray());
        if (pos == -1) {
            System.out.printf("En '%s' no está ninguno de los caracteres de '%s'.\n", hola, anyOf);
        }
        else {
            System.out.printf("En '%s' de los caracteres de '%s', el primero está en la posición %d.\n", hola, anyOf, pos);
        }
        System.out.println();
        System.out.println("Usando lastIndexOfAny:");
        pos = lastIndexOfAny(hola, anyOf.toCharArray());
        if (pos == -1) {
            System.out.printf("En '%s' no está ninguno de los caracteres de '%s'.\n", hola, anyOf);
        }
        else {
            System.out.printf("En '%s' de los caracteres de '%s', el último está en la posición %d.\n", hola, anyOf, pos);
        }
    }

    /**
     * Cuenta las veces que el carácter indicado está en la cadena.
     *
     * @param expression La expresión a evaluar.
     * @param character El carácter a comprobar.
     * @return El número de veces que está el carácter en la expresión.
     */
    public static int cuantasVeces(String expression, char character) {
        int total = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == character) {
                total++;
            }
        }
        return total;
    }

    // Buscar en una cadena cualquiera de los caracteres indicados. (19/nov/22 03.58)

    /**
     * Busca en la cadena desde el principio el primer carácter de ofAny que se encuentre.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los anyOf a comprobar en la cadena.
     * @return La posición del primer carácter que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int indexOfAny(String expression, char[] anyOf) {
        return indexOfAny(expression, anyOf, 0);
    }

    /**
     * Busca en la cadena desde la posición indicada el primer carácter de ofAny que se encuentre.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los anyOf a comprobar en la cadena.
     * @param fromIndex El índice desde donde se comprobará.
     * @return La posición del primer carácter que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int indexOfAny(String expression, char[] anyOf, int fromIndex) {
        int pos;
        for (var c : anyOf) {
            pos = expression.indexOf(c, fromIndex);
            if (pos > -1) {
                return pos;
            }
        }
        return -1;
    }

    /**
     * Busca desde el principio de la cadena cualquiera de los caracteres de anyOf
     * y devuelve el que esté antes.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los caracteres a comprobar en la cadena.
     * @return La posición del primer carácter que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int firstIndexOfAny(String expression, char[] anyOf) {
        return firstIndexOfAny(expression, anyOf, 0);
    }

    /**
     * Busca desde la posición indicada de la cadena cualquiera de los caracteres de ofAny
     * y devuelve el que esté antes.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los caracteres a comprobar en la cadena.
     * @param fromIndex El índice desde donde se empezará a buscar.
     * @return La posición del primer carácter que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int firstIndexOfAny(String expression, char[] anyOf, int fromIndex) {
        int menor = -1;
        for (char c : anyOf) {
            int pos = expression.indexOf(c, fromIndex);
            if (pos > -1) {
                if (menor == -1) {
                    menor = pos;
                }
                else if (menor > pos) {
                    menor = pos;
                }
            }
        }
        return menor;
    }

    /**
     * Busca desde el principio de la cadena cualquiera de los caracteres de anyOf
     * y devuelve el que esté antes desde el final.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los caracteres a comprobar en la cadena.
     * @return La posición del último carácter que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int lastIndexOfAny(String expression, char[] anyOf) {
        return lastIndexOfAny(expression, anyOf, 0);
    }
    /**
     * Busca desde la posición indicada de la cadena cualquiera de los caracteres de ofAny
     * y devuelve el que esté antes desde el final.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los caracteres a comprobar en la cadena.
     * @param fromIndex El índice desde donde se empezará a buscar.
     * @return La posición del último carácter que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int lastIndexOfAny(String expression, char[] anyOf, int fromIndex) {
        int menor = -1;
        for (char c : anyOf) {
            int pos = expression.indexOf(c, fromIndex);
            if (pos > -1) {
                if (menor == -1) {
                    menor = pos;
                }
                else if (menor < pos) {
                    menor = pos;
                }
            }
        }
        return menor;
    }
}
