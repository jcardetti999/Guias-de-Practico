/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 10: Ordenamiento burbuja (Bubble Sort)
 *
 * ESTRATEGIA
 * Algoritmo de ordenamiento por intercambios. Recorre el vector comparando
 * elementos vecinos y, si el de la izquierda es mayor que el de la derecha, los
 * intercambia. Al terminar una pasada, el mayor elemento del rango ha quedado
 * "burbujeado" hasta la última posición. Al repetir las pasadas, el vector
 * queda ordenado de forma ascendente.
 *
 * MEJORA APLICADA: bandera `huboIntercambio`
 * Si una pasada completa no produce ni un solo intercambio, significa que el
 * vector YA estaba ordenado. En ese caso se corta la ejecución. Sin esta
 * bandera, el algoritmo siempre ejecutaría las n-1 pasadas. Este detalle es lo que
 * convierte el mejor caso en Θ(n) en lugar de Θ(n²).
 *
 * POR QUÉ SÓLO ES ADECUADO PARA FINES DIDÁCTICOS O CONJUNTOS MUY PEQUEÑOS
 *   1. Costo cuadrático: su caso promedio y su peor caso son Θ(n²). Los
 *      algoritmos de producción (Timsort, mergesort, heapsort) son Θ(n log n).
 *      Para n = 1.000.000, Θ(n²) son 10¹² operaciones frente a ~2·10⁷: la
 *      diferencia es de miles de veces, no de un porcentaje.
 *   2. Localidad de caché: los intercambios ocurren entre posiciones contiguas, lo
 *      que en teoría favorece la caché, pero la cantidad de escrituras que exige
 *      lo cancela en la práctica.
 *   3. Es INESTABLE: no garantiza conservar el orden relativo de los elementos
 *      con clave igual. Ejemplo: al ordenar [3a, 3b, 1], el 3b puede terminar
 *      antes que el 3a.
 *   4. No es adaptativo de forma significativa frente a Insertion Sort, que en
 *      listas casi ordenadas es Θ(n) y además es más estable.
 *
 * COMPLEJIDAD
 * Temporal:
 *   - Mejor caso:    Θ(n). El vector ya está ordenado: la primera pasada hace
 *                    n-1 comparaciones y ningún intercambio, y la bandera corta
 *                    el algoritmo. (Θ(1) si n ≤ 1.)
 *   - Peor caso:     Θ(n²). Vector en orden inverso: hay exactamente
 *                    n·(n-1)/2 comparaciones y la misma cantidad de intercambios.
 *   - Caso promedio: Θ(n²). Para un vector aleatorio, ~n²/4 comparaciones.
 * Espacial: Θ(1). Es IN-PLACE: no usa memoria auxiliar proporcional a n.
 *
 * CASOS LÍMITE
 * - vector vacío -> los bucles no entran, 0 comparaciones.
 * - un elemento -> 0 comparaciones.
 * - ya ordenado -> n-1 comparaciones y 0 intercambios (salida temprana).
 * - orden inverso -> máximo de comparaciones e intercambios.
 */
public class Ejercicio1_10 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el vector en forma ascendente usando el algoritmo de la burbuja.
     *
     * @param vector          vector a ordenar; SÍ se modifica en el sitio
     * @param comparaciones   arreglo de longitud 1: recibe el nº de comparaciones
     * @param intercambios    arreglo de longitud 1: recibe el nº de intercambios
     * @param mostrarTraza    si es true, imprime cada comparación e intercambio
     */
    public static void ordenamientoBurbuja(int[] vector, int[] comparaciones,
                                           int[] intercambios, boolean mostrarTraza) {
        comparaciones[0] = 0;
        intercambios[0] = 0;

        int n = vector.length;

        // Cada pasada i lleva el mayor elemento del rango [0, n-1-i] hasta la
        // posición n-1-i. Por eso el rango se acorta en cada vuelta: comparar
        // sobre los elementos ya ordenados sería trabajo desperdiciado.
        for (int i = 0; i < n - 1; i++) {

            // Bandera de la salida temprana: se pone en false antes de cada
            // pasada y se activa si hubo al menos un intercambio.
            boolean huboIntercambio = false;

            // Invariante: al terminar la pasada i, el elemento mayor del rango
            // original quedó fijado en la posición n-1-i, y las primeras
            // n-1-i posiciones del vector son las más pequeñas.
            for (int j = 0; j < n - 1 - i; j++) {
                comparaciones[0]++;      // una comparación entre vecinos

                if (vector[j] > vector[j + 1]) {
                    // Intercambio: el elemento mayor "sube" hacia la derecha.
                    int auxiliar = vector[j];
                    vector[j] = vector[j + 1];
                    vector[j + 1] = auxiliar;
                    intercambios[0]++;
                    huboIntercambio = true;

                    if (mostrarTraza) {
                        System.out.println("    intercambio en [" + j + "," + (j + 1)
                                + "] -> " + formatearVector(vector));
                    }
                } else if (mostrarTraza) {
                    System.out.println("    " + vector[j] + " <= " + vector[j + 1]
                            + ", no se intercambia");
                }
            }

            if (mostrarTraza) {
                System.out.println("  Fin de la pasada " + (i + 1)
                        + " | vector: " + formatearVector(vector)
                        + " | intercambios acumulados: " + intercambios[0]);
            }

            // Corte temprano: si en toda la pasada no hubo un solo
            // intercambio, el vector ya estaba ordenado.
            if (!huboIntercambio) {
                if (mostrarTraza) {
                    System.out.println("  Ningun intercambio en la pasada "
                            + (i + 1) + ": el vector YA ESTABA ORDENADO. Se corta.");
                }
                break;
            }
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 1.10: ORDENAMIENTO BURBUJA ===");

        int[] comparaciones = new int[1];
        int[] intercambios = new int[1];

        // Caso 1 (típico): vector desordenado.
        int[] vector = { 64, 34, 25, 12, 22, 11, 90 };
        System.out.println("--- Caso tipico ---");
        System.out.println("  Vector inicial: " + formatearVector(vector));
        ordenamientoBurbuja(vector, comparaciones, intercambios, false);
        System.out.println("  Vector final  : " + formatearVector(vector));
        System.out.println("  Comparaciones : " + comparaciones[0]);
        System.out.println("  Intercambios : " + intercambios[0]);

        // Mejor caso: ya ordenado. La bandera debe cortar tras una sola pasada.
        System.out.println();
        System.out.println("--- Mejor caso: vector YA ORDENADO ---");
        int[] ordenado = { 1, 2, 3, 4, 5, 6 };
        System.out.println("  Vector inicial: " + formatearVector(ordenado));
        ordenamientoBurbuja(ordenado, comparaciones, intercambios, false);
        System.out.println("  Vector final  : " + formatearVector(ordenado));
        System.out.println("  Comparaciones : " + comparaciones[0] + " (= n-1, una sola pasada)");
        System.out.println("  Intercambios : " + intercambios[0] + " (cero: ya estaba ordenado)");
        System.out.println("  Con la bandera, Θ(n). SIN la bandera serían "
                + (ordenado.length * (ordenado.length - 1) / 2) + " comparaciones: Θ(n²).");

        // Peor caso: orden inverso. Máximo de comparaciones e intercambios.
        System.out.println();
        System.out.println("--- Peor caso: orden INVERSO ---");
        int[] inverso = { 6, 5, 4, 3, 2, 1 };
        System.out.println("  Vector inicial: " + formatearVector(inverso));
        ordenamientoBurbuja(inverso, comparaciones, intercambios, false);
        System.out.println("  Vector final  : " + formatearVector(inverso));
        System.out.println("  Comparaciones : " + comparaciones[0]
                + " (= n*(n-1)/2 = " + (inverso.length * (inverso.length - 1) / 2) + ")");
        System.out.println("  Intercambios : " + intercambios[0] + " (máximo posible)");

        // Traza paso a paso de un vector pequeño, para ver la "burbuja".
        System.out.println();
        System.out.println("--- TRAZA PASO A PASO ---");
        int[] pequeno = { 5, 1, 4, 2 };
        System.out.println("  Vector inicial: " + formatearVector(pequeno));
        System.out.println("  (observe cómo el mayor valor 'sube' hacia el final)");
        ordenamientoBurbuja(pequeno, comparaciones, intercambios, true);
        System.out.println("  Vector final: " + formatearVector(pequeno));

        // Casos límite.
        System.out.println();
        System.out.println("Casos límite:");
        int[] vacio = {};
        ordenamientoBurbuja(vacio, comparaciones, intercambios, false);
        System.out.println("  Vector vacío  -> " + formatearVector(vacio)
                + " | comparaciones: " + comparaciones[0]);

        int[] uno = { 99 };
        ordenamientoBurbuja(uno, comparaciones, intercambios, false);
        System.out.println("  Un elemento   -> " + formatearVector(uno)
                + " | comparaciones: " + comparaciones[0]);

        int[] repetidos = { 4, 4, 2, 2, 9, 9 };
        System.out.println("  Con repetidos -> " + formatearVector(repetidos) + " (antes del ordenamiento)");
        ordenamientoBurbuja(repetidos, comparaciones, intercambios, false);
        System.out.println("  Con repetidos -> " + formatearVector(repetidos) + " (después)");

        // Demostración del crecimiento cuadrático.
        System.out.println();
        System.out.println("--- CRECIMIENTO Θ(n²) ---");
        for (int n : new int[] { 10, 20, 40, 80 }) {
            int[] inversoN = new int[n];
            for (int i = 0; i < n; i++) {
                inversoN[i] = n - i;     // orden inverso: peor caso
            }
            ordenamientoBurbuja(inversoN, comparaciones, intercambios, false);
            System.out.println("  n=" + n + " -> comparaciones: " + comparaciones[0]
                    + ", intercambios: " + intercambios[0]
                    + "  (n*(n-1)/2 = " + (n * (n - 1) / 2) + ")");
        }
        System.out.println("  Al duplicar n, el costo se CUADRUPLICA: Θ(n²).");
        System.out.println("  Un Timsort sobre n=1.000.000 usaría ~2·10⁷ operaciones; el burbuja, ~10¹².");
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Devuelve el vector como texto entre corchetes. */
    private static String formatearVector(int[] vector) {
        if (vector.length == 0) {
            return "[]";
        }
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            texto.append(vector[i]);
            if (i < vector.length - 1) {
                texto.append(", ");
            }
        }
        return texto.append("]").toString();
    }
}