/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 9: Encontrar el mayor elemento de una matriz
 *
 * ESTRATEGIA
 * Doble recorrido con un acumulador `max`, inicializado con la primera celda
 * válida de la matriz. Por cada celda se compara con el acumulado y se actualiza
 * sólo si la celda es mayor.
 *
 * POR QUÉ ES NECESARIO RECORRER TODOS LOS ELEMENTOS
 * Éste es el punto que hay que justificar, y es una generalización de por qué el
 * Ejercicio 5 no puede cortar de forma temprana.
 *
 * El máximo acumulado sólo permite descartar UNA región: la celda que acaba de
 * compararse. El resto de la matriz sigue sin examinar y PODRÍA contener algo
 * mayor. No existe información que permita saltarse regiones enteras.
 *
 * Compárese con la búsqueda binaria (Ejercicio 3), donde SÍ se descartaba media
 * matriz de una vez. La diferencia es que allí el ordenamiento daba MONOTONÍA:
 * el máximo siempre estaba en el último elemento, y conocer el final del rango
 * bastaba. En una matriz no hay orden: el máximo puede estar en cualquier celda,
 * y no se puede acotar por dónde.
 *
 * Por lo tanto el recorrido completo no es una chapuza ni una falta de
 * optimización: es una LOWER BOUND. Cualquier algoritmo correcto debe, en el
 * peor caso, mirar cada celda al menos una vez, porque si una celda nunca se
 * examina, un adversario podría esconder ahí el valor mayor y el resultado sería
 * incorrecto. Θ(m·n) es el mínimo alcanzable.
 *
 * COMPLEJIDAD
 * Si la matriz tiene m filas y n columnas, hay m·n celdas.
 * Temporal: Θ(m·n) en todos los casos. No hay mejor caso: el algoritmo siempre
 *           examina todas las celdas porque el valor podría estar en cualquiera.
 *           El cuerpo del bucle tiene Θ(1) y se ejecuta m·n veces.
 * Espacial: Θ(1) auxiliar. Un solo acumulador; la matriz es entrada, no costo.
 *
 * CASOS LÍMITE
 * - matriz vacía o sin filas -> se informa que no hay máximo (null).
 * - filas vacías -> se saltan sin error.
 * - todos los valores negativos -> el acumulado se inicializa con la primera
 *   celda válida, NUNCA con 0, porque si no devolvería 0 en vez del negativo real.
 * - valores duplicados -> no cambia nada; el máximo es un valor, no una posición.
 */
public class Ejercicio1_9 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Encuentra el mayor elemento de una matriz.
     *
     * @param matriz       matriz de entrada; no se modifica
     * @param comparaciones arreglo de longitud 1: recibe el nº de comparaciones
     * @return el valor máximo, o null si la matriz no tiene elementos
     */
    public static Integer encontrarMaximo(int[][] matriz, int[] comparaciones) {
        comparaciones[0] = 0;

        if (matriz == null || matriz.length == 0) {
            return null;
        }

        Integer maximo = null;

        // Invariante: al terminar la vuelta (i, j), `maximo` es el mayor valor
        // entre todas las celdas visitadas hasta ese momento, o null si aún no
        // se visitó ninguna. Al salir los dos bucles se visitaron todas las
        // celdas, y entonces `maximo` es el mayor de la matriz completa.
        for (int i = 0; i < matriz.length; i++) {
            int[] fila = matriz[i];

            if (fila == null || fila.length == 0) {
                continue;
            }

            for (int j = 0; j < fila.length; j++) {
                comparaciones[0]++;      // una comparación por celda

                // La condición `maximo == null` actúa como inicialización: sólo
                // se compara a partir de la segunda celda válida. Es lo que
                // permite inicializar con un valor real de la matriz y no con 0,
                // que es el error clásico en ejercicios de máximo.
                if (maximo == null || fila[j] > maximo) {
                    maximo = fila[j];
                }
            }
        }

        return maximo;
    }

    /**
     * Variante informativa: devuelve la posición del máximo.
     *
     * @param matriz matriz de entrada; no se modifica
     * @return arreglo {fila, columna} del máximo, o {-1, -1} si la matriz está vacía
     */
    public static int[] posicionMaximo(int[][] matriz) {
        if (matriz == null || matriz.length == 0) {
            return new int[] { -1, -1 };
        }

        int[] posicion = { -1, -1 };
        Integer maximo = null;

        for (int i = 0; i < matriz.length; i++) {
            int[] fila = matriz[i];
            if (fila == null || fila.length == 0) {
                continue;
            }
            for (int j = 0; j < fila.length; j++) {
                if (maximo == null || fila[j] > maximo) {
                    maximo = fila[j];
                    posicion[0] = i;
                    posicion[1] = j;
                }
            }
        }

        return posicion;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 1.9: MAYOR ELEMENTO DE UNA MATRIZ ===");

        int[] comparaciones = new int[1];

        // Caso 1 (típico).
        int[][] matriz = {
            { 12, 45, 7 },
            { 88, 3, 29 },
            { 5, 61, 14 }
        };
        mostrarMatriz(matriz);
        Integer maximo = encontrarMaximo(matriz, comparaciones);
        int[] posicion = posicionMaximo(matriz);
        System.out.println("  Máximo: " + maximo
                + " | posición: fila " + posicion[0] + ", columna " + posicion[1]
                + " | comparaciones: " + comparaciones[0]
                + " = m*n = " + (matriz.length * matriz[0].length));

        // Caso 2 (adversario): todos negativos. Si el acumulado arrancara en 0,
        // devolvería 0, que NO está en la matriz.
        int[][] negativos = {
            { -8, -3, -15 },
            { -22, -7, -30 }
        };
        mostrarMatriz(negativos);
        maximo = encontrarMaximo(negativos, comparaciones);
        System.out.println("  Todos negativos -> máximo: " + maximo
                + " (esperado: -3, NO 0) | comparaciones: " + comparaciones[0]);

        // Caso 3: matriz rectangular.
        int[][] rectangular = {
            { 1, 2, 3, 4, 5, 6 },
            { 7, 8, 9, 10, 11, 12 }
        };
        maximo = encontrarMaximo(rectangular, comparaciones);
        System.out.println("  Rectangular 2x6 -> máximo: " + maximo
                + " | comparaciones: " + comparaciones[0] + " = 2*6 = 12");

        // Casos límite.
        System.out.println();
        System.out.println("Casos límite:");
        System.out.println("  1x1 -> máximo: " + encontrarMaximo(new int[][] { { 9 } }, comparaciones)
                + " | comparaciones: " + comparaciones[0]);
        System.out.println("  matriz vacía -> " + encontrarMaximo(new int[][] {}, comparaciones)
                + " (no hay máximo)");
        System.out.println("  filas vacías -> " + encontrarMaximo(new int[][] { {}, {} }, comparaciones)
                + " (no hay máximo)");

        // Demostración: no existe mejor caso, el costo es siempre m·n.
        System.out.println();
        System.out.println("--- POR QUÉ NO HAY MEJOR CASO ---");
        for (int lado : new int[] { 2, 4, 8, 16 }) {
            int[][] m = generarMatriz(lado, lado);
            encontrarMaximo(m, comparaciones);
            System.out.println("  matriz " + lado + "x" + lado
                    + " -> comparaciones: " + comparaciones[0]
                    + " (= celdas, siempre, sin excepción)");
        }
        System.out.println("  El máximo puede estar en CUALQUIER celda: no se puede omitir ninguna.");
        System.out.println("  Θ(m·n) es una cota inferior, no sólo una cota alcanzada.");
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Imprime la matriz en pantalla. */
    private static void mostrarMatriz(int[][] matriz) {
        System.out.println("  Matriz:");
        if (matriz == null || matriz.length == 0) {
            System.out.println("    (vacia)");
            return;
        }
        for (int i = 0; i < matriz.length; i++) {
            System.out.print("    ");
            if (matriz[i] == null) {
                System.out.print("(fila nula)");
            } else {
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.printf("%6d", matriz[i][j]);
                }
            }
            System.out.println();
        }
    }

    /** Genera una matriz de lado x lado, con el máximo siempre en la última celda. */
    private static int[][] generarMatriz(int filas, int columnas) {
        int[][] matriz = new int[filas][columnas];
        int contador = 1;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = contador++;
            }
        }
        return matriz;
    }
}