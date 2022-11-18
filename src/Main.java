// EvaluarExpresiones usando el tipo double. (18/nov/22 14.54)
// Evaluador de expresiones simples a partir del contenido de una cadena.
//
// Basado en EvaluarExpresiones-record, pero sin usar records.

//package com.example.evaluar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Evaluar expresiones simples de números con decimales.");
        System.out.println();

        /**
         * Si se deben mostrar las expresiones de ejemplo.
          */
        boolean mostrarEjemplos = false;
        String expresion;

        Evaluar.mostrarParciales = true;

        String res;

        System.out.print("Mostrar las operaciones parciales? ([S|s] = sí, otro = no) ");

        //Scanner sc = new Scanner(System.in);
        //res = sc.next();

        // Con BufferedReader se puede pulsar INTRO
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        res = in.readLine();

        if (res.equals("") || res.toLowerCase().startsWith("s")) {
            Evaluar.mostrarParciales = true;
        } else {
            Evaluar.mostrarParciales = false;
        }
        System.out.println();

        System.out.print("Escribe una expresión a evaluar [1.5*3+12-(15+5)*2] ");
        res = in.readLine();
        System.out.println();

        if (!res.equals("")) {
            expresion = res;
            mostrarResultado(expresion);
        }
        else {
            expresion="1.5*3+12-(15+5)*2";
            mostrarResultado(expresion);
        }
        System.out.println();

        // Probar con cadenas con caracteres no válidos.

        if (mostrarEjemplos) {
            // En los ejemplos no mostrar los cálculos parciales.
            var mostrarParcialesAnt= Evaluar.mostrarParciales;
            Evaluar.mostrarParciales = false;

            // Operar con números decimales. (18/nov/22 14.55)
            expresion = "2.5*5.5+1.5*2+22*5";
            mostrarResultado(expresion);
            System.out.println();

            // Operar con números decimales. (18/nov/22 14.55)
            expresion = "1.5+2*5+3+4+22*5";
            mostrarResultado(expresion);

            // Esto se calculaba mal. (17/nov/22 19.14)
            // 1+2*5+3+4+22*5
            // ya que al evaluar 2*5 = 10, se convertía el 22*5 en 210
            //  Lo utilizaba como 1+10+3+4+210 = 218
            expresion = "15+2*5+3+4+22*5";
            mostrarResultado(expresion);

            // No hay paréntesis de apertura, a ver cómo lo gestiona. (17/nov/22 13.59)
            //  OK: Lo ignora y muestra el error.
            //  Lo utiliza como 1+2*5+3+4 = 18
            expresion = "1+2)*5+3+4";
            mostrarResultado(expresion);

            // Con paréntesis de apertura sin el de cierre. (17/nov/22 15.50)
            //  Lo utiliza como 1+2*5+3+4 = 18
            expresion = "(1+2*5+3+4";
            mostrarResultado(expresion);

            // Los paréntesis no están emparejados: falta el de cierre.
            //  OK: Lo ignora y muestra el error.
            //  Lo utiliza como (1+2)*(5+3+4) = 36
            expresion = "((1+2)*(5+3+4)";
            mostrarResultado(expresion);

            expresion = "(1+2)*5+3+4";
            mostrarResultado(expresion);

            expresion = "((1+2)*5+3+4)";
            mostrarResultado(expresion);

            expresion = "(1+2)*(5+3)+4";
            mostrarResultado(expresion);

            expresion = "1+2*5+3+4";
            mostrarResultado(expresion);

            expresion = "1 + 2 + 3 + 4*5";
            mostrarResultado(expresion);

            expresion = "2*5 + 3*2 + 3 + 4*5 + 3*2";
            mostrarResultado(expresion);

            Evaluar.mostrarParciales = mostrarParcialesAnt;
        }
    }

    private static void mostrarResultado(String expresion) {
        System.out.print(expresion + " = ");
        long startTime = System.nanoTime();
        //long iniTime = System.currentTimeMillis();
        var res = Evaluar.evalua(expresion);
        System.out.println(res);
        long elapsedTime = System.nanoTime() - startTime;
        //long elapsedTime = System.currentTimeMillis() - iniTime;
        System.out.printf("  Tiempo empleado: %,.2f ns/1.000\n", (elapsedTime / 1000.0));
    }
}