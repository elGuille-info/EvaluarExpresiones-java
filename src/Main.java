// EvaluarExpresiones usando el tipo double. (18/nov/22 14.54)
// Evaluador de expresiones simples a partir del contenido de una cadena.
//
// Basado en EvaluarExpresiones-record, pero sin utilizar records.

//package com.example.evaluar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    /**
     * Si se debe mostrar el tiempo empleado al evaluar una expresión.
     */
    static boolean mostrarTiempoEmpleado = false;

    public static void main(String[] args) throws IOException {
        /*
          Si se deben mostrar las expresiones de ejemplo.
          */
        boolean mostrarEjemplos;
        String expression;

        Evaluar.mostrarParciales = true;

        String res;

        while (true) {

            System.out.print(ConsoleColor.yellow);
            System.out.println("Evaluar expresiones simples de números con decimales.");
            System.out.print(ConsoleColor.reset);
            System.out.println(ConsoleColor.green);
            System.out.println("Opciones:");
            //System.out.print(" Mostrar las operaciones parciales:");
            System.out.print(" Mostrar el tiempo empleado al evaluar:");
            System.out.println("  1: Sí, 2: No");
            System.out.print(" Mostrar las expresiones de ejemplo:");
            System.out.println(" 3: Sí, 4: No");
            System.out.print(" Indicar una expresión a evaluar:");
            System.out.println("    5: Sí, 6: No (se usa una de prueba)");
            System.out.print(ConsoleColor.reset);
            System.out.print("Indica las opciones a usar, 0=salir (ej: 146) [145]: ");

            // Con BufferedReader se puede pulsar INTRO,
            //  siempre que el out anterior no acabe en nueva línea??? (o eso me ha parecido).
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(System.in));
            res = in.readLine();
            //System.out.println();

            if (res.equals("")) {
                res = "145";
            }
            System.out.printf(" - Respuesta: %s\n", res);
            if (res.equals("0")) {
                System.out.println("Se termina el programa.");
                return;
            }

            // Solo se mostrará en la expresión indicada (no las de ejemplo).
            Evaluar.mostrarParciales = true;

            mostrarTiempoEmpleado = res.contains("1");
            mostrarEjemplos = res.contains("3");

            expression = "99-15+2*7";
            if (res.contains("5")) {
                System.out.printf("Escribe una expresión a evaluar [%s] ", expression);
                var res2 = in.readLine();
                //System.out.println();
                if (!res2.equals("")) {
                    expression = res2;
                }
            }
            mostrarResultado(expression);
            //System.out.println();

            if (mostrarEjemplos) {
                // En los ejemplos no mostrar los cálculos parciales.
                var mostrarParcialesAnt = Evaluar.mostrarParciales;
                Evaluar.mostrarParciales = false;

                // Esto fallaba porque (-15+5) se evalúa correctamente a -10,
                // pero al estar multiplicado por 2, la operación se intenta en:
                // --10*2 tomando el valor 10*2 y la operación final quedará como:
                // (1.5*3)+12 --10*2 + 1
                // 16.5--20.0
                expression = "1.5*3+12-(-15+5)*2 + 10%3";
                mostrarResultado(expression);

                // Con el ** repetido.
                expression = "1.5**3+12-(15+5)*2 + 10%3";
                mostrarResultado(expression);

                // Operar con números decimales. (18/nov/22 14.55)
                expression = "2.5*5.5+1.5*2+22*5";
                mostrarResultado(expression);
                //System.out.println();

                // Operar con números decimales. (18/nov/22 14.55)
                expression = "1.5+2*5+3+4+22*5";
                mostrarResultado(expression);

                // Esto se calculaba mal. (17/nov/22 19.14)
                // 1+2*5+3+4+22*5
                // ya que al evaluar 2*5 = 10, se convertía el 22*5 en 210
                //  Lo utilizaba como 1+10+3+4+210 = 218
                expression = "15+2*5+3+4+22*5";
                mostrarResultado(expression);

                // No hay paréntesis de apertura, a ver cómo lo gestiona. (17/nov/22 13.59)
                //  OK: Lo ignora y muestra el error.
                //  Lo utiliza como 1+2*5+3+4 = 18
                expression = "1+2)*5+3+4";
                mostrarResultado(expression);

                // Con paréntesis de apertura sin el de cierre. (17/nov/22 15.50)
                //  Lo utiliza como 1+2*5+3+4 = 18
                expression = "(1+2*5+3+4";
                mostrarResultado(expression);

                // Los paréntesis no están emparejados: falta el de cierre.
                //  OK: Lo ignora y muestra el error.
                //  Lo utiliza como (1+2)*(5+3+4) = 36
                expression = "((1+2)*(5+3+4)";
                mostrarResultado(expression);

                expression = "(1+2)*5+3+4";
                mostrarResultado(expression);

                expression = "((1+2)*5+3+4)";
                mostrarResultado(expression);

                expression = "(1+2)*(5+3)+4";
                mostrarResultado(expression);

                expression = "1+2*5+3+4";
                mostrarResultado(expression);

                expression = "1 + 2 + 3 + 4*5";
                mostrarResultado(expression);

                expression = "2*5 + 3*2 + 3 + 4*5 + 3*2";
                mostrarResultado(expression);

                Evaluar.mostrarParciales = mostrarParcialesAnt;
            }

            //System.err.flush();
            System.out.println();
        }
    }

    /**
     * Mostrar el resultado de la expresión indicada.
     *
     * @param expression La expresión a evaluar.
     */
    private static void mostrarResultado(String expression) {
        //System.out.print(expression + " = ");
        long startTime = System.nanoTime();
        //long iniTime = System.currentTimeMillis();
        var res = Evaluar.evaluar(expression);
        System.out.print(expression + " = ");
        System.out.println(res);
        long elapsedTime = System.nanoTime() - startTime;
        //long elapsedTime = System.currentTimeMillis() - iniTime;
        if (mostrarTiempoEmpleado) {
            System.out.print(ConsoleColor.blue);
            System.out.printf("\tTiempo empleado: %,.2f ns/1.000\n", (elapsedTime / 1000.0));
            System.out.print(ConsoleColor.reset);
        }
    }
}

