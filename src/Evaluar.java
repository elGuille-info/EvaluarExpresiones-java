// Evaluar expresiones simples.
// Se permite *, /, %, +, - con este nivel de precedencia.
// Las expresiones entre paréntesis se evalúan primero.
//
// También se puede usar 'x' para multiplicar y ':' para dividir.
// Se puede indicar '!', para calcular la factorial.

//package com.example.evaluar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Clase para evaluar expresiones simples utilizando valores dobles.
 *
 * @author Guillermo Som (Guille), iniciado el 16/nov/2022
 * @version 1.1.5.3.221126
 */
public final class Evaluar {
    /*
     Versión 1.1.5.*.221123
        No se calculan las factoriales de números enteros negativos.
        Solo se usa la función gamma si el número no es entero positivo.
        Mostrar lo que se evalúa con la factorial.

     Versión 1.1.4.*.221122
        Evaluar la factorial usando NUM!.
        La factorial a evaluar puede estar entre paréntesis.

     Versión 1.1.3.1.221121
        Ya evalúa bien las expresiones entre paréntesis.

     Versión 1.1.3.0.221121
        Si la expresión tiene un paréntesis de cierre seguido de un número o un paréntesis de apertura,
            se considera una multiplicación. Por tanto, poner el signo * después del paréntesis de cierre.

        Si la expresión tiene un paréntesis de apertura seguido de uno de cierre
            agrega un signo de multiplicación: (exp1)(exp2) -> (exp1)*(exp2).

        Esto no hacerlo:
            Si la expresión completa está entre paréntesis, quitarlos.
            Si tiene el de apertura y no el del final, quitar el de apertura y avisar.

     Versión 1.1.2.1.221121
        Si los paréntesis no están balanceados, avisar y devolver -1.
        @see 1.1.3.0.221121
            Si la expresión completa está entre paréntesis, quitarlos.
            Si tiene el de apertura y no el del final, quitar el de apertura y avisar.

     Versión 1.1.2.0.221120
        Si entre un número y una expresión entre paréntesis no hay signo de operación usar la multiplicación.
            Poder indicar 2(3+1) y que se convierta en 2 * (3+1) o 25+(2(7*2)+2) -> 25+(2*(7*2)+2).
    */

    /**
     * Si se deben mostrar los resultados parciales de la evaluación de la expresión.
     */
    public static boolean mostrarParciales = false;

    /**
     * Los operadores multiplicativos.
     * Se puede usar la x para multiplicar y los dos puntos para dividir.
     */
    private static final String operadoresMultiplicativos = "x*:/%";

    /**
     * Los operadores aditivos.
     */
    private static final String operadoresAditivos = "+-";

    /**
     * Los operadores en el orden de precedencia.
     * Sin incluir los paréntesis que se procesan por separado.
     */
    private static final String losOperadores = operadoresMultiplicativos + operadoresAditivos;

    /**
     * Array de tipo char con los operadores en el orden de precedencia.
     */
    private static final char[] operadores = losOperadores.toCharArray();

    /**
     * Evalúa una expresión. Punto de entrada para evaluar expresiones.
     *
     * @param expression La expresión a evaluar.
     * @return El valor entero de la expresión evaluada.
     */
    public static double evaluar(String expression) {
        if (expression == null) {
            System.err.println("La expresión a analizar es nula.");
            return -1;
        }

        // Quitar todos los caracteres en blanco.
        expression = expression.replace(" ", "");
        if (expression.equals("")) {
            System.err.println("La expresión a analizar es una cadena vacía.");
            return 0;
        }

        // Comprobar si tenemos paréntesis. (21/nov/22 06.59)
        int iniApertura = expression.indexOf('(');
        String res;

        if (iniApertura > -1) {
            // Comprobar que haya los mismos paréntesis de apertura que de cierre.
            if (!balancedParenthesis(expression)) {
                System.err.printf("Los paréntesis no están balanceados '%s'.\n", expression);
                return -1;
            }

            // Antes de evaluar las expresiones entre paréntesis
            //  comprobar si hay paréntesis precedidos de dígitos.
            expression = comprobarDigitParenthesis(expression);

            // Primero se evalúan todas las expresiones entre paréntesis.
            res = evaluarParenthesis(expression);
        }
        else {
            res = expression;
        }

        // En evaluarExp se comprueba si hay operadores.
        return evaluarExp(res);
    }

    /**
     * Comprueba si los paréntesis en la expresión están balanceados.
     *
     * @param expression La expresión a evaluar.
     * @return True si hay los mismos de apertura que de cierre, false en otro caso.
     * @since 1.1.2.1
     */
    public static boolean balancedParenthesis(String expression) {
        int apertura = cuantasVeces(expression, '(');
        int cierre = cuantasVeces(expression, ')');
        return apertura == cierre;
    }

    /**
     * Cuenta las veces que el carácter indicado está en la expresión.
     *
     * @param expression La expresión a evaluar.
     * @param character El carácter a comprobar.
     * @return El número de veces que está el carácter en la expresión.
     * @since 1.1.2.1
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

    /**
     * Comprobar si hay paréntesis de apertura precedido por un dígito.
     *      Si es así, cambiarlo por dígito * (.<br>
     * O si hay un paréntesis de apertura precedido por uno de cierre.
     *      Si es así, cambiarlo por )*(.
     *
     * @param expression La expresión a evaluar.
     * @return La expresión con el cambio realizado.
     * @since 1.1.2.0
     */
    private static String comprobarDigitParenthesis(String expression) {
        int desde = 0;
        int ini;

        while (true) {
            ini = expression.indexOf('(', desde);
            // Si no hay más paréntesis de apertura, salir.
            if (ini == -1) {
                //return expression;
                break;
            }
            if (ini - 1 >= 0) {
                char digit = expression.charAt(ini - 1);
                // Si lo precede un dígito o un )
                if (Character.isDigit(digit) || digit == ')') {
                    // Cambiar este paréntesis por *(
                    expression = expression.substring(0, ini) + "*" + expression.substring(ini);
                    ini++;
                }
            }
            desde = ini + 1;
            if (desde > expression.length()) {
                //return expression;
                break;
            }
        }
        // Comprobar si hay paréntesis de cierre seguido de uno de apertura o de un dígito,
        //  si es así, cambiarlo poner un * entre los dos.
        desde = 0;
        while (true) {
            ini = expression.indexOf(')', desde);
            // Si no hay más paréntesis de cierre, salir.
            if (ini == -1) {
                //return expression;
                break;
            }
            if (ini + 1 < expression.length()) {
                char digit = expression.charAt(ini + 1);
                // Si lo sigue un dígito o (
                if (Character.isDigit(digit) || digit == '(') {
                    // Cambiar este paréntesis por )*
                    expression = expression.substring(0, ini + 1) + "*" + expression.substring(ini + 1);
                    ini++;
                }
            }
            desde = ini + 1;
            if (desde > expression.length()) {
                //return expression;
                break;
            }
        }

        return expression;
    }

    /**
     * Evalúa el contenido de las expresiones entre paréntesis.<br>
     * Se permite NÚMERO(EXPRESIÓN) que se convierte en NÚMERO*(EXPRESIÓN).
     *
     * @param expression Expresión a evaluar (puede tener o no paréntesis).
     * @return La cadena sin los paréntesis y con lo que haya entre paréntesis ya evaluado.
     */
    private static String evaluarParenthesis(String expression) {
        boolean hay;
        do {
            // Posición del paréntesis de apertura.
            int ini = expression.indexOf('(');
            // Si hay paréntesis de apertura...
            if (ini > -1) {
                // Posición del paréntesis de cierre.
                int fin = expression.indexOf(')', ini);
                // Si hay paréntesis de cierre...
                if (fin > -1) {
                    // Comprobar si hay otro de empiece antes del cierre.
                    int ini2;
                    // Repetir hasta encontrar la pareja del de cierre.
                    while (true) {
                        ini2 = expression.indexOf('(', ini + 1);
                        if (ini2 > -1 && ini2 < fin) {
                            // Hay uno de apertura antes del de cierre, evaluar desde ahí.
                            ini = ini2;
                        }
                        else {
                            break;
                        }
                    }
                    // En Java, substring, es desde inicio inclusive hasta fin exclusive.
                    // En .NET es desde inicio con la cantidad de caracteres del segundo parámetro.
                    var exp = expression.substring(ini + 1, fin);
                    // Evaluar el resultado de la expresión.
                    double res = evaluarExp(exp);
                    // Asignar el resultado a la expresión.
                    //  Si hay varias expresiones (entre paréntesis) como la evaluada,
                    //      se reemplazarán por el resultado.
                    //
                    // Esto es seguro, ya que al estar entre paréntesis
                    //  las mismas expresiones tendrán los mismos resultados,
                    //  a diferencia de lo que ocurriría si no estuvieran entre paréntesis.
                    expression = expression.replace("(" + exp + ")", Double.toString(res));
                }
            }

            // Aquí llegará se haya evaluado o no la expresión entre paréntesis.
            // Si había alguna expresión entre paréntesis, se habrá evaluado, pero puede que haya más.

            // Para no repetir la comprobación en caso de que no haya más paréntesis. (17/nov/22 14.10)
            //      Nota: Esta optimización no es estrictamente necesaria, pero...
            // Ya que, en el primer if se comprueba como mínimo si hay de apertura.
            // Si lo hubiera, después se revisará si hay de cierre.
            // Si no se cumplen los dos casos,
            //  en el if del bloque else, como mínimo, se vuelve a evaluar si hay de apertura.
            // Si no hay de apertura el primer if fallará y en el segundo solo se chequeará si hay de cierre.
            boolean hayApertura = expression.indexOf('(') > -1;

            // Si no hay más paréntesis, salir.
            // Por seguridad, comprobar que estén los dos paréntesis.
            // Si hay de apertura y cierre, continuar.
            if (hayApertura && expression.indexOf(')') > -1) {
                hay = true;
            } else {
                // Quitar los que hubiera (si no están emparejados).
                if (hayApertura || expression.indexOf(')') > -1){
                    expression = expression.replace("(", "").replace(")", "");
                }
                hay = false;
            }

            // Repetir si hay más expresiones entre paréntesis de apertura y cierre.
            //  Si hay paréntesis y no están emparejados, no se comprueba nada más.
        } while (hay);

        return expression;
    }

    /**
     * Evalúa la expresión indicada quitando los espacios en blanco, (no hay expresiones entre paréntesis).<br>
     * Se evalúan las operaciones (entre dobles) de factorial, suma, resta, multiplicación y división.
     *
     * @param expression La expresión a evaluar.
     * @return Un valor doble con el resultado de la expresión evaluada.
     */
    private static double evaluarExp(String expression) {
        // Quitar todos los caracteres en blanco.
        expression = expression.replace(" ", "");
        // Si la expresión es una cadena vacía, se devuelve cero.
        if (expression.equals("")) {
            return 0;
        }
        int cuantos;
        String op2;
        // Para que tenga un valor asignado,
        //  antes del return resultado del final ya estará asignada correctamente.
        double resultado = -1;
        TuplePair<Character, Integer> donde;
        int desde;
        double res1, res2;

        while (true) {
            // Comprobar si se ha indicado el signo de factorial ! (23/nov/22 14.18)
            desde = expression.indexOf('!');
            if (desde == -1) {
                break;
            }
            // No usar op1 aquí, (24/nov/22 03.40)
            // debe mantener el valor null hasta el siguiente bloque while.
            op2 = expression.substring(0, desde);
            op2 = buscarUnNumero(op2, true);
            res1 = Double.parseDouble(op2);
            res2 = fact(res1);
            expression = expression.replace(op2 + "!", Double.toString(res2));
            // Si se deben mostrar las operaciones parciales. (18/nov/22 15.08)
            if (mostrarParciales) {
                // Mostrar los valores parciales en otro color.
                System.out.printf("%s\t %s! = %s\n%s", ConsoleColor.cyan, op2, res2, ConsoleColor.reset);
            }
        }

        // Para asegurarme que no use op1 antes de este bucle while.
        String op1 = null;

        while (true) {
            desde = 0;
            // Buscar la operación a realizar.
            donde = siguienteOperadorConPrecedencia(expression, desde);
            // Si no hay más operadores.
            if (donde == null) {
                // Si no hay operadores y el resultado no se ha procesado, devolver el valor de la expresión.
                //
                // Este caso se me ha dado al evaluar una expresión entre paréntesis sin operadores.
                // Esto normalmente se dará si toda la expresión estaba entre paréntesis,
                //  se ha evaluado y la cadena contiene el resultado,
                //  pero la variable 'resultado' aún no se ha calculado.
                /* NOTA:
                 * Comprobar si op1 es null, no comprobar, por ejemplo, si resultado es -1,
                 * ya que puede haber un resultado que sea -1.
                 */
                if (op1 == null) {
                    return Double.parseDouble(expression);
                }
                // Si llega aquí es que 'resultado' tiene un valor asignado
                //  con el resultado final de la expresión evaluada,
                //  por tanto, salir del while (con break) o devolver el resultado (con return resultado).
                // Así queda más claro que se sale de la función devolviendo el resultado.
                return resultado;
            }

            // Si la posición es cero es que delante no hay nada.
            // O es un número negativo. (18/nov/22 16.27)
            // O hay una expresión a evaluar. (20/nov/22 09.23)
            if (donde.position == 0) {
                if (donde.operador == '-') {
                    // Comprobar si hay más operaciones. (20/nov/22 09.23)
                    cuantos = cuantosOperadores(expression);
                    if (cuantos == 1) {
                        return Double.parseDouble(expression);
                    }
                    // Desglosar la operación, teniendo en cuenta que el primero es negativo.
                    // Buscar el siguiente operador desde donde.position + 1.
                    desde = donde.position + 1;
                    var donde2 = siguienteOperadorConPrecedencia(expression, desde);
                    if (donde2 == null || donde2.position == 0) {
                        System.err.printf("No puedo evaluar '%s'.\n", expression);
                        // No lanzar una excepción, devolver -1.
                        return -1;
                    }
                    // Poner nuevamente el punto desde donde se analizó.
                    desde = donde.position;
                    // Reasignar la posición.
                    donde = donde2;
                }
                else {
                    System.err.println("La posición del operador es cero.");
                    // No lanzar una excepción, devolver -1.
                    return -1;
                }
            }

            // Asignar todos los caracteres hasta el signo al primer operador.
            op1 = expression.substring(desde, donde.position).trim();
            // La variable op1 puede tener la expresión 16.5--20.0 y al convertirla a doble falla.
            // Ahora en buscarUnNumero se comprueba si la expresión tiene un número negativo.
            op1 = buscarUnNumero(op1, true);
            res1 = Double.parseDouble(op1);

            // op2 tendrá el resto de la expresión.
            op2 = expression.substring(donde.position + 1).trim();
            // Buscar el número hasta el siguiente operador.
            op2 = buscarUnNumero(op2, false);
            res2 = Double.parseDouble(op2);

            // Hacer el cálculo de la operación
            resultado = switch (donde.operador) {
                case '+' -> res1 + res2;
                case '-' -> res1 - res2;
                case '*', 'x' -> res1 * res2;
                case '/', ':' -> res1 / res2;
                case '%' -> res1 % res2;
                default -> 0;
            };
            var laOperacion = op1 + donde.operador + op2;
            var elResultado = Double.toString(resultado);

            // Si se deben mostrar las operaciones parciales. (18/nov/22 15.08)
            if (mostrarParciales) {
                // Mostrar los valores parciales en otro color.
                //System.err.printf("\t %s = %,.2f\n", laOperacion, resultado);
                System.out.printf("%s\t %s = %,.2f\n%s", ConsoleColor.cyan, laOperacion, resultado, ConsoleColor.reset);
            }

            // Cambiar por el resultado esta expresión. (18/nov/22 00.20)

            // La posición donde está esta operación (si hay más de una solo se busca la primera).
            var posOp = expression.indexOf(laOperacion);

            // Si no se encuentra la operación es porque se ha podido quitar un operador.
            if (posOp == -1) {
                var laOp2 = op1 + donde.operador + donde.operador + op2;
                posOp = expression.indexOf(laOp2);
                if (posOp == -1) {
                    System.err.printf("\tError no se ha encontrado %s (ni %s) en la expresión.", laOperacion, laOp2);
                    return resultado;
                }
                else {
                    expression = expression.replace(laOp2, elResultado);
                    continue;
                }
            }
            // Si está al principio de la cadena asignar el resultado más lo que haya tras la operación.
            if (posOp == 0) {
                expression = elResultado + expression.substring(laOperacion.length());
            }
            // Si no está al principio,
            //  añadir lo que hubiera antes de esta operación, el resultado y lo que haya después de la operación.
            else {
                expression = expression.substring(0,  posOp) + elResultado + expression.substring(posOp + laOperacion.length());
            }
        }

        // Si no hay break en el bucle while, aquí no llegará nunca.
        //return resultado;
    }

    /**
     * Cuenta cuántos operadores hay en la expresión.
     *
     * @param expression La expresión a comprobar.
     * @return El número total de operadores en la expresión.
     */
    private static int cuantosOperadores(String expression) {
        int cuantos = 0;
        // Hacer una copia para que el original no se quede ordenado.
        var copiaOperadores = Arrays.copyOf(operadores, operadores.length);
        Arrays.sort(copiaOperadores);
        for (char c : expression.toCharArray()) {
            int p = Arrays.binarySearch(copiaOperadores, c);
            if (p >= 0) {
                cuantos++;
            }
        }
        return cuantos;
    }

    /**
     * Comprueba si hay un solo operador, si lo hay devuelve un tuple con el carácter y la posición.
     *
     * @param expression La expresión a evaluar.
     * @return Si solo hay un operador, devuelve un tuple con el operador y la posición,
     *         en otro caso devuelve '\u0000' y -1.
     */
    private static TuplePair<Character, Integer> hayUnOperador(String expression) {
        int cuantos = 0;
        TuplePair<Character, Integer> res = new TuplePair<>('\u0000', -1);
        for(char op : operadores) {
            int pos = expression.indexOf(op);
            if (pos > -1) {
                cuantos++;
                // Asignar solo el primero.
                if (cuantos == 1) {
                    res = new TuplePair<>(op,  pos);
                    // Puede que este mismo operador está más de una vez.
                    if (pos + 1 < expression.length()) {
                        int pos2 = expression.indexOf(op, pos + 1);
                        if (pos2 > -1) {
                            cuantos++;
                        }
                    }
                }
            }
        }
        if (cuantos != 1) {
            res = new TuplePair<>('\u0000', -1);
        }

        return res;
    }

    /**
     * Busca el siguiente signo de operación (teniendo en cuenta la precedencia: * / % + -).
     *
     * @param expression La expresión a evaluar.
     * @param fromIndex La posición desde la que se buscará en la cadena.
     * @return Una tuple con el operador hallado y la posición en la expresión o null si no se ha hallado.
     */
    private static TuplePair<Character, Integer> siguienteOperadorConPrecedencia(String expression, int fromIndex) {
        // Buscar primero los de más precedencia
        TuplePair<Character, Integer> posChar = firstIndexOfAny(expression, operadoresMultiplicativos.toCharArray(), fromIndex);
        if (posChar != null) {
            return posChar;
        }
        // Después buscar en los de menos precedencia.
        posChar = firstIndexOfAny(expression, operadoresAditivos.toCharArray(), fromIndex);
        return posChar;
    }

    /**
     * Busca el número anterior o siguiente.
     *
     * @param expression  La expresión a evaluar.
     * @param elAnterior True si se busca el número anterior (desde el final),
     *                   en otro caso se busca el número siguiente (desde el principio).
     * @return La cadena con el número hallado.<br>
     *         Si el número hallado lo precede - y delante hay otro operador es que es un número negativo.
     */
    private static String buscarUnNumero(String expression, boolean elAnterior) {
        StringBuilder sb = new StringBuilder();
        var a = expression.toCharArray();
        // Cuando se busca el anterior se hace desde el final,
        //  ya que la cadena tendrá un número precedido por un signo de operación o nada más.
        // Cuando se busca el siguiente, se hace desde el principio,
        //  porque la cadena tendrá el resto de la expresión a evaluar.
        int inicio = elAnterior ? a.length - 1 : 0;
        int fin = elAnterior ? 0 : a.length - 1;

        // Si la expresión solo contiene el operador - considerarlo como un número negativo.
        var unOp = hayUnOperador(expression);
        if (unOp.position > -1) {
            if (unOp.operador == '-') {
                // Solo si empieza con ese operador.
                if (expression.charAt(0) == unOp.operador) {
                    return expression;
                }
            }
        }
        // Si la expresión empieza por un operador, quitarlo. (18/nov/22 16.59)
        // Salvo si es el signo menos (-), ya que puede ser negativo.
        var ch = losOperadores.indexOf(a[inicio]);
        if (ch > -1) {
            if (operadores[ch] == '-') {
                sb.append(operadores[ch]);
            }
            if (elAnterior) {
                inicio--;
            } else {
                inicio++;
            }
        }

        int i = inicio;

        while (elAnterior ? i >= fin : i <= fin) {
            if (losOperadores.indexOf(a[i]) == -1) {
                sb.append(a[i]);
            } else {
                // Si es el signo menos...
                if (a[i] == '-') {
                    // Comprobar si a[i-1] es un operador.
                    if (i > 0) {
                        if (losOperadores.indexOf(a[i - 1]) > -1) {
                            sb.append(a[i]);
                            // Salir, porque es un número negativo.
                            break;
                        }
                    }
                    else {
                        // Es el primer carácter y es un operador.
                        sb.append(a[i]);
                    }
                }
                break;
            }
            if (elAnterior) {
                i--;
            } else {
                i++;
            }
        }
        // Si se ha encontrado algo y se busca el número anterior,
        //  invertirlo ya que se habrá añadido desde el final.
        if (elAnterior && sb.length() > 1) {
            sb.reverse();
        }
        return sb.toString().trim();
    }

    /**
     * Busca en la cadena los caracteres indicados y devuelve la primera ocurrencia.
     * Si alguno de los caracteres está en la cadena, devuelve el que esté antes.
     *
     * @param expression La cadena a evaluar.
     * @param anyOf Los caracteres a comprobar en la cadena.
     * @return La posición y el carácter del primero que encuentre en la cadena o un valor null si no hay ninguno.
     */
    private static TuplePair<Character, Integer> firstIndexOfAny(String expression, char[] anyOf, int fromIndex) {
        TuplePair<Character, Integer> menor = null;
        for (char c : anyOf) {
            int pos = expression.indexOf(c, fromIndex);
            if (pos > -1) {
                if (menor == null) {
                    menor = new TuplePair<>(c, pos);
                }
                else if (menor.position > pos) {
                    menor = new TuplePair<>(c, pos);
                }
            }
        }
        return menor;
    }

    /**
     * Tuple de dos valores para usar al buscar un operador y la posición del mismo.
     *
     * @param operador Un valor del tipo T1.
     * @param position Un valor del tipo T2.
     * @param <T1> El tipo (por referencia) del primer parámetro.
     * @param <T2> El tipo (por referencia) del segundo parámetro.
     */
    record TuplePair<T1, T2>(T1 operador, T2 position) {
    }

    /**
     * Calcula la factorial del número indicado.<br>
     * Se permiten decimales y números negativos (que no sean números enteros).<br><br>
     *
     * <b>NOTAS:</b<br>
     * Las comprobaciones de los valores obtenidos con los números no naturales (enteros mayores de cero) las he hecho con la calculadora (en modo científico) de Windows.<br>
     * Esta calculadora no admite el cálculo de factoriales de números negativos enteros, pero sí de factoriales de números negativos con decimales.<br>
     * Esta función no calcula los enteros negativos.<br>
     * Aclaración: Puede que no todos los valores devueltos sean correctos.
     *
     * @param number El número del que se quiere calcular la factorial.
     * @return Un valor de tipo doble con la factorial del número indicado.
     * @since 1.1.4.0
     */
    public static double fact(double number) {
        if (number == 0) return 1;
        if (number == 2) return 2;

        // Comprobar si el número es un entero negativo sin decimales.
        if (number < 0) {
            double numAbs = Math.abs(number);
            double fPart = numAbs - (long) numAbs;
            if (fPart == 0) {
                System.out.printf("%s\tError en factorial de %s (entero negativo), se asigna 1.\n%s",
                        ConsoleColor.red, number, ConsoleColor.reset);
                // Devolver 1 para que no altere el resto de cálculos.
                return 1;
            }
        }

        // Si no es un número natural (o menor de cero) usar la función gamma.
        if (number % 1 != 0 || number < 0)
        {
            return gamma(number + 1);
        }

        // Calcularlo multiplicando el número hasta el 2.
        // for (double i = number - 1; i > 1; --i)
        for (double i = number - 1; i > 1; i--)
        {
            number *= i;
        }
        return number;
    }

    /**
     * La función Gamma es una función que extiende el concepto de factorial a los números complejos.
     *      <a href="http://www.guiasdeapoyo.net/guias/cuart_mat_e/Funci%C3%B3n%20gamma.pdf">Fuente</a><br>
     *  El código está adaptado de <a href="https://stackoverflow.com/a/15454784/14338047">esta respuesta de StackOverflow</a><br>
     *  No se calculan las factoriales de números enteros negativos.
     *
     * @param z El número del que queremos saber el valor de la función gamma.
     * @return Un valor de tipo doble.
     * @since 1.1.4.0
     */
    private static double gamma(double z) {
        var g = 7;
        double[] p = {0.99999999999980993, 676.5203681218851, -1259.1392167224028, 771.32342877765313, -176.61502916214059, 12.507343278686905, -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};

        if (z < 0.5) return Math.PI / (Math.sin(Math.PI * z) * gamma(1 - z));
        else {
            z -= 1;

            var x = p[0];
            for (int i = 1; i < g + 2; i++)
                x += p[i] / (z + i);

            var t = z + g + 0.5;
            return Math.sqrt(2 * Math.PI) * Math.pow(t, (z + 0.5)) * Math.exp(-t) * x;
        }
    }

    public static void main (String[] args) throws IOException {
        String hola;
        String anyOf;
        TuplePair<Character, Integer> pos;
        String res;
        BufferedReader in;
        final String vocales = "aeiou";
        String expression;
        double pruebaD;
        double resD;

        // Con BufferedReader se puede pulsar INTRO,
        //  siempre que el out anterior no acabe en nueva línea??? (o eso me ha parecido).
        in = new BufferedReader(
                new InputStreamReader(System.in));

        // Comprobar si se ha indicado una expresión en la línea de comandos.
        if (args.length > 0) {
            expression = args[0];
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            System.out.println();

            System.out.print("Pulsa una tecla (0 para terminar).");
            res = in.readLine();
            if (!res.equals("0")) {
                return;
            }
            System.out.println();
        }

        System.out.printf("Indica las letras a comprobar (0 para no comprobar cadenas) [%s]: ", vocales);
        res = in.readLine();

        if (!res.equals("0")) {
            if (res.equals("")) {
                res = "aeiou";
            }
            anyOf = res;

            hola = "Hola Radiola";
            System.out.printf("Indica la palabra para comprobar si tiene alguna de las letras indicadas [%s]: ", hola);
            in = new BufferedReader(
                    new InputStreamReader(System.in));
            res = in.readLine();
            if (res.equals("")) {
                res = hola;
            }
            hola = res;
            System.out.println();

            System.out.println("Usando firstIndexOfAny:");
            pos = firstIndexOfAny(hola, anyOf.toCharArray(), 0);
            if (pos == null)
            {
                System.out.printf("En '%s' no está ninguno de los caracteres de '%s'.\n", hola, anyOf);
            }
            else
            {
                System.out.printf("En '%s' de los caracteres de '%s', el primero ('%s') está en la posición %d.\n", hola, anyOf, pos.operador, pos.position);
            }

            int veces = cuantasVeces(hola, 'a');
            System.out.printf("En '%s' el carácter 'a' está %d veces.\n", hola, veces);

            System.out.println();
        }

        expression = "25+(3+2)!*2";
        System.out.printf("Escribe una expresión a evaluar (0 para mostrar las pruebas) [%s] ", expression);
        res = in.readLine();
        if (!res.equals("0")) {
            if (!res.equals("")) {
                expression = res;
            }
            mostrarParciales = true;
            resD = Evaluar.evaluar(expression);

            // Mostrar 4 decimales (sin separador de miles).
            System.out.printf("Con Evaluar: %s = %.4f", expression, resD);
            System.out.println();
        }
        else {
            System.out.println();
            System.out.println("Pruebas operaciones (Java y Evaluar):");
            expression = "25+((2-5)!)2";
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            expression = "25+(5!*2)";
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            expression = "25+((2+3)!*2)";
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            expression = "25+(2*(7*2)+2)";
            pruebaD = 25+(2*(7*2)+2);
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            expression = "25+(2(7*2)+2)";
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);

            expression = "(" + expression +")";
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);

            expression = "1.5*3+12-(-15+5)*2 + 10%3";
            pruebaD = 1.5*3+12-(-15+5)*2 + 10%3;
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);

            expression = "17 * ((12+5) * (7-2)) ";
            pruebaD = 17 * ((12 + 5) * (7 - 2));
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);

            expression = "1+2*3+6";
            pruebaD = 1 + 2 * 3 + 6;
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            expression = "99-15+2*7";
            pruebaD = 99 - 15 + 2 * 7;
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);

            // 6^2 / 2(3) + 4 (6 ^ 2 es 6 OR 2)
            pruebaD = 36.0 / 2 * (3) + 4;
            expression = "36.0 / 2*(3) +4";
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            // 6/2(2+1)
            pruebaD = 6.0 / 2 * (2 + 1);
            expression = "6.0/2*(2+1)";
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            //mostrarParciales = true;
            //6/(2(2+1))
            pruebaD = 6.0 / (2 * (2 + 1));
            expression = "6.0/(2*(2+1))";
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
            // 2 – (10 x 2) / 6
            pruebaD = 2 - (10.0 * 2) / 6;
            expression = "2 - (10.0 * 2) / 6";
            System.out.printf("Java dice: %s = %s\n", expression, pruebaD);
            System.out.printf("Evaluar dice: %s = ", expression);
            resD = Evaluar.evaluar(expression);
            System.out.println(resD);
        }

        System.out.print("¿Quieres ver las pruebas de las factoriales? (S para sí, otra para no) [S]: ");
        res = in.readLine();
        if (res.equals("") || res.equalsIgnoreCase("s")) {
            pruebasFactorial();
            System.out.println();
        }
    }

    /**
     * Pruebas de factoriales, con y sin decimales, números positivos y negativos.
     */
    private static void pruebasFactorial() {
        double resD;
        double pruebaD;

        System.out.println("\nFactoriales con números mayor de 90:");
        // Valores altos, hasta que da Infinity (142)
        for (pruebaD = 91; pruebaD < 146; pruebaD += 3) {
            resD = fact(pruebaD);
            System.out.printf("La factorial  de %.0f es %,.0f\n", pruebaD, resD);
        }
        System.out.println("\nFactoriales del -2 al 10:");
        // Valores de -2 a 10
        for (pruebaD = -2; pruebaD < 11; pruebaD++) {
            resD = fact(pruebaD);
            System.out.printf("La factorial  de %.0f es %,.0f\n", pruebaD, resD);
        }
        System.out.println("\nFactoriales con números con decimales:");
        pruebaD = 2.44;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = 1.44;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = 1.99;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = 5.5;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = 0.8;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        System.out.println("\nFactoriales con números negativos:");
        pruebaD = -0.8;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = -5.5;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = -1.99;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
        pruebaD = -1.44;
        resD = fact(pruebaD);
        System.out.printf("La factorial  de %.2f es %,.10f\n", pruebaD, resD);
    }
}
