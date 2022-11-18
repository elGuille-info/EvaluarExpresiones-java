// Evaluar expresiones simples.
// Se permite *, /, +, - con este nivel de precedencia.
// Las expresiones entre paréntesis se evalúan primero.
//
//  op1 + op2 * op3 se evalúa como op1 + (op2*op3)
//  op1 * op2 + op3 se evalúa como (op1*op2) + op3


//package com.example.evaluar;

public final class Evaluar {
    /**
     * Si se deben mostrar los resultados parciales de la evaluación de la expresión.
     */
    public static boolean mostrarParciales = false;

    /**
     * Los operadores en el orden de precedencia.
     * Sin incluir los paréntesis que se procesan por separado.
     */
    static final String losOperadores = "*/%+-";

    /**
     * Array de tipo char con los operadores en el orden de precedencia.
     */
    static final char[] operadores = losOperadores.toCharArray();

    /**
     * Evalúa una expresión. Punto de entrada para evaluar expresiones.
     *
     * @param expression La expresión a evaluar.
     * @return El valor entero de la expresión evaluada.
     */
    public static double evaluar(String expression) {
        if (expression == null || expression.trim().equals(""))
            return -1;

        // Quitar todos los caracteres en blanco.
        expression = expression.replace(" ", "");

        double resultado;

        // Primero se evalúan todas las expresiones entre paréntesis.
        var res = evaluateParenthesis(expression);

        // Si hay algún operador, evaluar la expresión.
        if (hayOperador(res)) {
            resultado = evaluarExp(res);
        } else {
            // Si no hay operadores, es que es el resultado.
            resultado = Double.parseDouble(res);
        }

        return resultado;
    }

    /**
     * Evalúa la expresión indicada quitando los espacios en blanco, (no hay expresiones entre paréntesis).
     * Se evalúan las operaciones (entre enteros) de suma (+), resta (-), multiplicación (*) y división (/).
     * @param expression La expresión a evaluar.
     * @return Un valor entero con el resultado de la expresión evaluada.
     */
    private static double evaluarExp(String expression) {
        // Si la expresión es nula o una cadena vacía, se devuelve cero.
        if (expression == null || expression.trim().equals(""))
            return 0;

        // Quitar todos los caracteres en blanco.
        expression = expression.replace(" ", "");

        // Evaluar la expresión indicada.

        String op1, op2;
        double resultado = 0;
        TuplePair<Character, Integer> donde;

        do {
            // Buscar la operación a realizar.
            donde = siguienteOperadorConPrecedencia(expression);
            if (donde.positionEsta == -1) {
                break;
            }

            // Si la posición es cero es que delante no hay nada.
            // O es un número negativo. (18/nov/22 16.27)
            if (donde.positionEsta == 0) {
                if (expression.startsWith("-") || expression.startsWith("+")) {
                    return Double.parseDouble(expression);
                }
                System.err.println("La posición del operador es cero.");
                // No lanzar una excepción, devolver -1.
                return -1;
            }

            double res1, res2;

            // Asignar todos los caracteres hasta el signo al primer operador.
            op1 = expression.substring(0, donde.positionEsta).trim();
            // La variable op1 puede tener la expresión 16.5--20.0 y al convertirla a doble falla.
            // Ahora en buscarUnNumero se comprueba si la expresión tiene un número negativo.
            op1 = buscarUnNumero(op1, true);
            res1 = Double.parseDouble(op1);

            // op2 tendrá el resto de la expresión.
            op2 = expression.substring(donde.positionEsta + 1).trim();
            // Buscar el número hasta el siguiente operador.
            op2 = buscarUnNumero(op2, false);
            res2 = Double.parseDouble(op2);

            resultado = switch (donde.operador) {
                case '+' -> res1 + res2;
                case '-' -> res1 - res2;
                case '*' -> res1 * res2;
                case '/' -> res1 / res2;
                case '%' -> res1 % res2;
                default -> 0;
            };
            var laOperacion = op1 + donde.operador + op2;
            var elResultado = String.valueOf(resultado);

            // Si se deben mostrar las operaciones parciales. (18/nov/22 15.08)
            if (mostrarParciales) {
                System.err.printf("\t %s = %,.2f\n", laOperacion, resultado);
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
        } while (hayOperador(expression));

        return resultado;
    }

    /**
     * Evalúa el contenido de las expresiones entre paréntesis.
     * @param expression Expresión a evaluar (puede tener o no paréntesis).
     * @return La cadena sin los paréntesis y con lo que haya entre paréntesis ya evaluado.
     */
    private static String evaluateParenthesis(String expression) {
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
                    var ini2 = expression.indexOf('(', ini + 1);
                    if (ini2 > -1 && ini2 < fin) {
                        // Hay uno de apertura antes del de cierre, evaluar desde ahí.
                        ini = ini2;
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
                    expression = expression.replace("(" + exp + ")", String.valueOf(res));
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
     * Comprueba si la cadena indicada tiene alguno de los operadores aceptados.
     * @param expression La cadena a comprobar.
     * @return True si contiene algún operador, false en caso contrario.
     */
    static boolean hayOperador(String expression) {
        for (char c : operadores) {
            if (expression.indexOf(c) > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si hay un solo operador, si lo hay devuelve una tuple con el carácter y true.
     * @param expression La expresión a evaluar.
     * @return Si solo hay un operador, devuelve un tuple con el operador y true,
     *         en otro caso devuelve '\u0000' y false.
     */
    static TuplePair<Character, Boolean> hayUnOperador(String expression) {
        int cuantos = 0;
        char elOperador = '\u0000';
        TuplePair<Character, Boolean> res; // = new TuplePair<>('\u0000', false);
        for(char op : operadores) {
            int pos = expression.indexOf(op);
            if (pos > -1) {
                cuantos++;
                // Asignar solo el primero.
                if (cuantos == 1) {
                    //res = new TuplePair<>(op,  true);
                    elOperador = op;
                    // Puede que este mismo operador está más de una vez.
                    if (pos < expression.length()) {
                        int pos2 = expression.indexOf(op, pos+1);
                        if (pos2 > -1) {
                            cuantos++;
                        }
                    }
                }
            }
        }
        if (cuantos == 1) {
            res = new TuplePair<>(elOperador, true);
        }
        else {
            res = new TuplePair<>('\u0000', false);
        }

        return res;
    }

    /**
     * Busca el siguiente signo de operación (teniendo en cuenta la precedencia: * / + -).
     * @param expression La expresión a evaluar.
     * @return Una tuple con el operador hallado y la posición en la expresión.
     *         Si no se ha hallado, la tuple contendrá '\u0000' como el operador y -1 como la posición.
     */
    static TuplePair<Character, Integer> siguienteOperadorConPrecedencia(String expression) {
        for (char op : operadores) {
            int pos = expression.indexOf(op);
            if (pos > -1) {
                return new TuplePair<>(op, pos);
            }
        }

        return new TuplePair<>('\u0000', -1);
    }

    /**
     * Busca el número anterior o siguiente.
     * @param expression  La expresión a evaluar.
     * @param elAnterior True si se busca el número anterior (desde el final),
     *                   en otro caso se busca el número siguiente (desde el principio).
     * @return La cadena con el número hallado.
     *         Si el número hallado lo precede - y delante hay otro operador es que es un número negativo.
     */
    static String buscarUnNumero(String expression, boolean elAnterior) {
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
        if (unOp.positionEsta) {
            if (unOp.operador == '-') {
                return expression;
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
     * Tuple de dos valores para usar al buscar un operador y la posición del mismo.
     * @param operador Un valor del tipo T1.
     * @param positionEsta Un valor del tipo T2.
     * @param <T1> El tipo (por referencia) del primer parámetro.
     * @param <T2> El tipo (por referencia) del segundo parámetro.
     */
    record TuplePair<T1, T2>(T1 operador, T2 positionEsta) {
    }
}
