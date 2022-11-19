/**
 * Extensiones para las cadenas. (19/nov/22 04.13)
 */
public class StringExtend {

    public static void main (String[] args) {
        String hola = "Hola";
        var anyOf = "aeiou";
        int pos = indexOfAny(hola, anyOf.toCharArray());
        String esta = pos > -1 ? "está en la posición " + pos : "no está";
        System.out.println("Usando indexOfAny:");
        System.out.printf("En '%s' %s uno de los caracteres de %s\n", hola, esta, anyOf);
        System.out.println();

        System.out.println("Usando firstIndexOfAny:");
        pos = firstIndexOfAny(hola, anyOf.toCharArray());
        System.out.printf("En '%s' de los caracteres de %s, el primero está en la posición %d\n", hola, anyOf, pos);
    }

    // Buscar en una cadena cualquiera de los caracteres indicados. (19/nov/22 03.58)

    /**
     * Busca en la cadena cualquiera de los anyOf indicados.
     * @param expression La cadena a evaluar.
     * @param anyOf Los anyOf a comprobar en la cadena.
     * @return La posición en la cadena del primero que encuentre o -1 si no hay ninguno.
     */
    public static int indexOfAny(String expression, char[] anyOf) {
        int pos;
        for (var c : anyOf) {
            pos = expression.indexOf(c);
            if (pos > -1) {
                return pos;
            }
        }
        return -1;
    }

    /**
     * Busca en la cadena los caracteres indicados y devuelve la primera ocurrencia.
     * Si alguno de los caracteres está en la cadena, devuelve el que esté antes.
     * @param expression La cadena a evaluar.
     * @param anyOf Los caracteres a comprobar en la cadena.
     * @return La posición del primero que encuentre en la cadena o -1 si no hay ninguno.
     */
    public static int firstIndexOfAny(String expression, char[] anyOf) {
        int menor = -1;
        for (char c : anyOf) {
            int pos = expression.indexOf(c);
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
}
