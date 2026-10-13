/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 8: Sumar todos los elementos de una matriz
 *
 * ESTRATEGIA Y RECORRIDO DE LA MATRIZ
 * La matriz es un arreglo de arreglos: int[][] donde cada arreglo interno es una
 * fila. Se recorre con un doble ciclo: el externo itera sobre las FILAS y, por
 * cada fila, el interno itera sobre las COLUMNAS.
 *
 *     for (i = 0 .. filas-1)          // recorre cada fila
 *         for (j = 0 .. columnas-1)   // recorre cada celda de esa fila
 *             suma = suma + matriz[i][j]
 *
 * Este recorrido visita cada celda exactamente una vez. No se puede recorrer de
 * otra forma sin repetir o saltar alguna celda, porque la suma es una operación
 * que NO admite restructuring: sumar A+B no es igual que sumar B+A en el sentido
 * de que no se puede reordenar el cálculo para ahorrar pasos (a diferencia de un
 * producto matricial, que sí se puede reordenar).
 *
 * COMPLEJIDAD
 * Si la matriz tiene m filas y n columnas, hay m·n celdas.
 * Temporal: Θ(m·n). El cuerpo del bucle interno tiene Θ(1) y se ejecuta m·n
 *           veces. El bucle externo aporta Θ(m), despreciable frente a Θ(m·n).
 *           Caso especial: matriz vacía Θ(1) (sólo la guarda inicial).
 * Espacial: Θ(1) auxiliar. No se crea ninguna estructura: la matriz es la
 *           entrada, no un costo del algoritmo. Sólo el acumulador.
 *
 * OPERACIONES CONTABILIZADAS
 * El contador `operaciones` se incrementa dentro del bucle, una vez por celda
 * visitada, es decir por cada suma realizada. Debe valer exactamente m·n para
 * una matriz rectangular.
 *
 * CASOS LÍMITE
 * - matriz vacía (0 filas) o filas de longitud 0 -> suma 0, sin excepción.
 * - matriz 1x1 -> suma trivial.
 * - matriz rectangular (m ≠ n) -> el análisis en términos de m y n es el mismo.
 * - desborde: el acumulador es long, no int, porque con 100.000 filas de 100.000
 *   columnas la suma excedería el rango de int.
 */
public class Ejercicio1_8 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Calcula la suma de todos los elementos de una matriz.
     *
     * @param matriz      matriz de entrada; no se modifica
     * @param operaciones arreglo de longitud 1: recibe el nº de adiciones hechas
     * @return la suma de todos los elementos, o 0 si la matriz está vacía
     */
    public static long sumarMatriz(int[][] matriz, int[] operaciones) {
        operaciones[0] = 0;

        // Caso límite: matriz inexistente o sin filas.
        if (matriz == null || matriz.length == 0) {
            return 0;
        }

        // long y no int: la suma de muchos elementos puede desbordar int.
        long suma = 0;

        // Invariante del doble bucle: al terminar la vuelta (i, j), `suma`
        // contiene la suma de TODAS las celdas del sub-rectángulo formado por
        // las filas 0..i y las columnas 0..j. Cuando el bucle interno agota j,
        // la condición pasa a la siguiente fila y el invariante se extiende a
        // la fila completa. Al salir los dos bucles, el sub-rectángulo es la
        // matriz entera y `suma` es la suma buscada.
        for (int i = 0; i < matriz.length; i++) {
            int[] fila = matriz[i];

            // Guarda: una fila nula o vacía no aporta nada, pero no interrumpe
            // el recorrido de las demás filas.
            if (fila == null || fila.length == 0) {
                continue;
            }

            for (int j = 0; j < fila.length; j++) {
                operaciones[0]++;      // una suma por celda visitada
                suma += fila[j];
            }
        }

        return suma;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 1.8: SUMAR TODOS LOS ELEMENTOS DE UNA MATRIZ ===");

        int[] operaciones = new int[1];

        // Caso 1 (típico): matriz 3x4.
        int[][] matriz = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10, 11, 12 }
        };
        mostrarMatriz(matriz);
        long suma = sumarMatriz(matriz, operaciones);
        System.out.println("  m = " + matriz.length + " filas, n = " + matriz[0].length + " columnas");
        System.out.println("  Suma total: " + suma);
        System.out.println("  Operaciones (adiciones) realizadas: " + operaciones[0]
                + "  -> debe ser m*n = " + (matriz.length * matriz[0].length));

        // Caso 2 (adversario): todos negativos. Falla si se inicializa en 0
        // y se usan valores absolutos, o si se confunde suma con conteo.
        int[][] negativos = {
            { -1, -2, -3 },
            { -4, -5, -6 }
        };
        mostrarMatriz(negativos);
        suma = sumarMatriz(negativos, operaciones);
        System.out.println("  Todos negativos -> suma: " + suma
                + " (esperado: -21) | operaciones: " + operaciones[0]
                + " = m*n = " + (negativos.length * negativos[0].length));

        // Caso 3 (límite): matriz rectangular, no cuadrada.
        int[][] rectangular = {
            { 5, 10, 15, 20, 25 },
            { 1, 2, 3, 4, 5 }
        };
        mostrarMatriz(rectangular);
        suma = sumarMatriz(rectangular, operaciones);
        System.out.println("  Rectangular 2x5 -> suma: " + suma + " (esperado: 90)"
                + " | operaciones: " + operaciones[0] + " = 2*5 = 10");

        // Casos límite degenerados.
        System.out.println();
        System.out.println("Casos límite:");
        System.out.println("  1x1 -> suma: " + sumarMatriz(new int[][] { { 7 } }, operaciones)
                + " | operaciones: " + operaciones[0]);
        System.out.println("  0 filas (vacía) -> suma: " + sumarMatriz(new int[][] {}, operaciones)
                + " | operaciones: " + operaciones[0]);
        System.out.println("  filas vacías -> suma: " + sumarMatriz(new int[][] { {}, {} }, operaciones)
                + " | operaciones: " + operaciones[0]);

        // Demostración del crecimiento: al duplicar m y n, el costo se cuadruplica.
        System.out.println();
        System.out.println("--- CRECIMIENTO Θ(m·n) ---");
        for (int lado : new int[] { 2, 4, 8, 16, 32 }) {
            int[][] m = generarMatriz(lado, lado);
            sumarMatriz(m, operaciones);
            System.out.println("  matriz " + lado + "x" + lado
                    + " -> celdas = " + (lado * lado)
                    + " -> operaciones = " + operaciones[0]);
        }
        System.out.println("  Al duplicar m y n, las operaciones se CUADRUPLICAN: Θ(m·n).");
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

    /** Genera una matriz de lado x lado con valores 1..n². */
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