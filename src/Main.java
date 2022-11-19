// EvaluarExpresiones usando el tipo double. (18/nov/22 14.54)
// Evaluador de expresiones simples a partir del contenido de una cadena.
//
// Basado en EvaluarExpresiones-record, pero sin utilizar records.

//package com.example.evaluar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        /*
          Si se deben mostrar las expresiones de ejemplo.
          */
        boolean mostrarEjemplos;
        String expression;

        Evaluar.mostrarParciales = true;

        final String black = "\033[30m";
        final String red = "\033[31m";
        final String redBack = "\033[41m";
        final String green = "\033[32m";
        final String greenBack = "\033[42m";
        final String yellow = "\033[33m";
        final String yellowBack = "\u001B[43m";
        final String blue = "\033[34m";
        final String purple = "\033[35m";
        final String cyan = "\033[36m";
        final String white = "\033[37m";
        final String reset = "\u001B[0m";

        String res;

        while (true) {

            //ClearConsole();
            System.out.print(yellow);
            System.out.println("Evaluar expresiones simples de números con decimales.");
            System.out.print(reset);
            System.out.println(green);
            System.out.println("Opciones:");
            System.out.print(" Mostrar las operaciones parciales:");
            System.out.println("  1: Sí, 2: No");
            System.out.print(" Mostrar las expresiones de ejemplo:");
            System.out.println(" 3: Sí, 4: No");
            System.out.print(" Indicar una expresión a evaluar:");
            System.out.println("    5: Sí, 6: No (se usa una de prueba)");
            System.out.print(reset);
            System.out.print("Indica las opciones a usar, 0=salir (ej: 146) [145]: ");

            //System.out.print("Mostrar las operaciones parciales? ([S|s] = sí, otro = no) ");

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

            Evaluar.mostrarParciales = res.contains("1");
            mostrarEjemplos = res.contains("3");

            //expression = "1.5**3+12-(15+5)*2 + 10%3";
            //expression = "99-15+2*7";
            // Ya que + se evalúa antes que -, esto se evalúa como:
            //  2*7=14, 15+14=29, 99-29 = 70
            // Tendría que ser como si se pusiera así: (99-15)+2*7
            //  2*7=14, 99-15=84, 84+14=98
            // Si es el mismo nivel de precedencia, se evaluaría:
            // 99-15+ (2*7) = 99-15=84 + 2*7=14 -> 84+14= 98
            double prueba1;
            expression = "1+2*3+6";
            prueba1 = 1+2*3+6;
            System.out.printf("%s = %s\n",expression, prueba1);
            expression = "99-15+2*7";
            prueba1 = 99-15+2*7;
            System.out.printf("%s = %s\n",expression, prueba1);
            //expression = "(99-15)+2*7";
            if (res.contains("5")) {
                System.out.printf("Escribe una expresión a evaluar [%s] ", expression);
                var res2 = in.readLine();
                System.out.println();
                if (!res2.equals("")) {
                    expression = res2;
                }
            }
            mostrarResultado(expression);
            System.out.println();

            // Probar con cadenas con caracteres no válidos.

            if (mostrarEjemplos) {
                // En los ejemplos no mostrar los cálculos parciales.
                var mostrarParcialesAnt = Evaluar.mostrarParciales;
                Evaluar.mostrarParciales = false;

                // Esto fallará porque (-15+5) se evalúa correctamente a -10,
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

            System.out.print("\nPulsa INTRO para ir al menú (0 para terminar) ");
            res = in.readLine();
            System.out.println();
            if (res.equals("0")) {
                System.out.println("Se termina el programa.");
                return;
            }
            System.out.println("\n");
        }
    }

    /**
     * Mostrar el resultado de la expresión indicada.
     * @param expression La expresión a evaluar.
     */
    private static void mostrarResultado(String expression) {
        System.out.print(expression + " = ");
        long startTime = System.nanoTime();
        //long iniTime = System.currentTimeMillis();
        var res = Evaluar.evaluar(expression);
        System.out.println(res);
        long elapsedTime = System.nanoTime() - startTime;
        //long elapsedTime = System.currentTimeMillis() - iniTime;
        System.out.printf("  Tiempo empleado: %,.2f ns/1.000\n", (elapsedTime / 1000.0));
    }

    /**
     * Limpiar la consola (pantalla).
     */
    private static void ClearConsole() {
        // En la pantalla mostrada en el IDE, nada de esto no funciona.

//        for (int i = 0; i < 30; i++) {
//            System.out.println();
//        }
//        Robot limpiar = new Robot();
//        limpiar.keyPress(KeyEvent.VK_CONTROL);
//        limpiar.keyPress(KeyEvent.VK_L);
//        limpiar.keyRelease(KeyEvent.VK_CONTROL);
//        limpiar.keyRelease(KeyEvent.VK_L);
        // Esto no funciona.
//        System.out.print("\033[H\033[2J");
//        System.out.flush();

//        try {
//            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//        } catch (Exception e) {
//            /*No hacer nada*/
//        }

        try {
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if (operatingSystem.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}