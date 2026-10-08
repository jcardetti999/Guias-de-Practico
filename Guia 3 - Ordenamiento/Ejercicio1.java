/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un programa que ordene un arreglo de números
 * enteros usando Bubble Sort, mostrando el estado del arreglo después de cada
 * pasada.
 *
 * ALGORITMO ELEGIDO
 * Bubble Sort. Es el algoritmo que el enunciado pide y además el más didáctico
 * de la guía: su única operación es comparar vecinos e intercambiarlos si están
 * desordenados, lo que hace visible la idea de "algo que sube hacia el final".
 *
 * RAZONAMIENTO
 * Al recorrer el arreglo comparando cada elemento con su vecino de la derecha,
 * el valor mayor "flota" progresivamente hacia el final: cada vez que se
 * encuentra un valor mayor que el de la derecha, el intercambio lo empuja una
 * posición más. Al terminar una pasada completa, el mayor de todo el rango
 * quedó en la última posición, y no se vuelve a tocar. La segunda pasada
 * trabaja sobre un rango una posición más corto, y así hasta que el arreglo
 * queda ordenado.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] cuya cantidad y valores se leen por consola: primero la
 * cantidad, luego cada elemento. En la demostración automática se usan cuatro
 * arreglos fijos (ordenado, inverso, aleatorio y casi ordenado).
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * El enunciado pide dos cosas: (a) el arreglo original, y (b) el estado del
 * arreglo después de CADA pasada. Se agrega además el resumen de comparaciones
 * e intercambios por pasada, porque son lo que hace visible por qué una pasada
 * terminó antes que otra.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * El invariante de cada pasada (los i mayores ya están en posición final), el
 * motivo de acortar el rango en cada vuelta, el motivo de la bandera de corte
 * temprano, y el significado exacto de cada contador.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * El arreglo final debe coincidir con una copia ordenada con Arrays.sort usada
 * SÓLO como oráculo externo (nunca dentro del algoritmo). Además se prueba con
 * arreglo vacío, un solo elemento, ya ordenado, inverso y con duplicados.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 1: Bubble Sort paso a paso
 *
 * ALGORITMO — Bubble Sort con bandera de corte temprano.
 *
 * RAZONAMIENTO
 * Comparando vecinos e intercambiándolos cuando están desordenados, el valor
 * mayor se desplaza una posición hacia la derecha en cada intercambio. Tras una
 * pasada completa, el mayor del rango quedó en la última posición del rango, y
 * ya no se vuelve a mover. Por eso cada pasada puede trabajar sobre un rango una
 * posición más corto.
 *
 * INVARIANTE
 * Al terminar la pasada i (contando desde 1), los i mayores elementos del
 * arreglo original ocupan las i últimas posiciones, y están en su posición
 * definitiva. Lo que queda sin ordenar es el prefijo de longitud n-i.
 *
 * COMPLEJIDAD
 *   Temporal:
 *     - Mejor caso:    Θ(n). Una pasada completa sin intercambios activa la
 *                      bandera y el algoritmo corta. Hay n-1 comparaciones.
 *     - Caso promedio: Θ(n²). Cada elemento recorre gran parte del arreglo.
 *     - Peor caso:     Θ(n²). Orden inverso: n(n-1)/2 comparaciones e
 *                      n(n-1)/2 intercambios, sin cortes.
 *   Espacial: Θ(1). Sólo el índice del bucle, un auxiliar y los contadores.
 *
 * INSTRUMENTACIÓN
 *   comparaciones  — una por cada comparación de vecinos.
 *   intercambios   — una por cada pareja efectivamente intercambiada.
 *
 * CASOS LÍMITE
 *   - Arreglo vacío o de un elemento: 0 comparaciones, 0 intercambios.
 *   - Ya ordenado: n-1 comparaciones y 0 intercambios (salida temprana).
 *   - Orden inverso: n(n-1)/2 comparaciones e intercambios.
 *   - Duplicados: el algoritmo es ESTABLE, mantiene su orden relativo.
 */
public class Ejercicio1 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el arreglo in-place con Bubble Sort, registrando el estado luego de
     * cada pasada.
     *
     * @param vector           arreglo a ordenar; se modifica en el sitio
     * @param detalle          si es true, imprime el estado tras cada pasada
     * @param comparaciones    arreglo de longitud 1: recibe el total de comparaciones
     * @param intercambios     arreglo de longitud 1: recibe el total de intercambios
     * @param pasos            arreglo de longitud 1: recibe la cantidad de pasadas
     */
    public static void bubbleSort(int[] vector, boolean detalle,
                                  long[] comparaciones, long[] intercambios, int[] pasos) {
        comparaciones[0] = 0;
        intercambios[0] = 0;
        pasos[0] = 0;

        if (vector == null || vector.length <= 1) {
            return;
        }

        int n = vector.length;

        // Cada pasada i fija el elemento en la posicion n-1-i. Por eso el rango
        // util se acorta: comparar sobre las posiciones ya ordenadas seria
        // trabajo desperdiciado.
        for (int i = 0; i < n - 1; i++) {

            // Bandera de corte temprano: si una pasada entera no produce ni un
            // solo intercambio, el arreglo YA ESTA ORDENADO.
            boolean huboIntercambio = false;
            long comparacionesDeLaPasada = 0;
            long intercambiosDeLaPasada = 0;

            // Invariante: al terminar esta vuelta, vector[n-1-i] es el mayor
            // elemento del rango original y ya no se vuelve a tocar.
            for (int j = 0; j < n - 1 - i; j++) {
                comparaciones[0]++;
                comparacionesDeLaPasada++;

                if (vector[j] > vector[j + 1]) {
                    // El valor mayor "sube" una posicion hacia la derecha.
                    int auxiliar = vector[j];
                    vector[j] = vector[j + 1];
                    vector[j + 1] = auxiliar;

                    intercambios[0]++;
                    intercambiosDeLaPasada++;
                    huboIntercambio = true;
                }
            }

            pasos[0]++;

            if (detalle) {
                System.out.println("    Pasada " + (i + 1)
                        + " -> " + formatearVector(vector)
                        + "   [comparaciones: " + comparacionesDeLaPasada
                        + ", intercambios: " + intercambiosDeLaPasada + "]");
            }

            if (!huboIntercambio) {
                if (detalle) {
                    System.out.println("    Sin intercambios en esta pasada: el arreglo ya"
                            + " estaba ordenado. El algoritmo corta aqui (mejor caso Θ(n)).");
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
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        demostrarTrazado();
        demostrarTiposDeArreglo();
        ejecutarInteractivo();
    }

    private static void demostrarTrazado() {
        System.out.println("=== EJERCICIO 1: BUBBLE SORT PASO A PASO ===");
        System.out.println();

        int[] vector = { 64, 34, 25, 12, 22 };

        System.out.println("  Arreglo original: " + formatearVector(vector));
        System.out.println();

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];
        int[] pasos = new int[1];

        System.out.println("  Estado despues de cada pasada:");
        bubbleSort(vector, true, comparaciones, intercambios, pasos);

        System.out.println();
        System.out.println("  Arreglo final    : " + formatearVector(vector));
        System.out.println("  Pasadas          : " + pasos[0]);
        System.out.println("  Comparaciones    : " + comparaciones[0]);
        System.out.println("  Intercambios     : " + intercambios[0]);

        // Verificacion contra el oraculo externo.
        int[] oraculo = { 64, 34, 25, 12, 22 };
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(vector, oraculo) ? "CORRECTO" : "DISCREPANCIA"));
    }

    private static void demostrarTiposDeArreglo() {
        System.out.println();
        System.out.println("--- COMO CAMBIA EL COSTO SEGUN EL ORDEN DE LOS DATOS ---");
        System.out.println();

        int[][] casos = {
            { 1, 2, 3, 4, 5, 6, 7, 8 },                  // ordenado
            { 8, 7, 6, 5, 4, 3, 2, 1 },                  // inverso
            { 4, 1, 3, 2, 8, 5, 7, 6 },                  // aleatorio
            { 1, 2, 3, 5, 4, 6, 7, 8 }                   // casi ordenado
        };
        String[] nombres = { "Ordenado", "Inverso", "Aleatorio", "Casi ordenado" };

        System.out.printf("  %-16s | %13s | %13s | %8s%n",
                "tipo", "comparaciones", "intercambios", "pasadas");
        System.out.printf("  %-16s-+-%-13s-+-%-13s-+-%-8s%n",
                "----------------", "-------------", "-------------", "--------");

        for (int c = 0; c < casos.length; c++) {
            int[] copia = copiar(casos[c]);
            long[] comparaciones = new long[1];
            long[] intercambios = new long[1];
            int[] pasos = new int[1];

            bubbleSort(copia, false, comparaciones, intercambios, pasos);

            System.out.printf("  %-16s | %13d | %13d | %8d%n",
                    nombres[c], comparaciones[0], intercambios[0], pasos[0]);
        }

        System.out.println();
        System.out.println("  El ordenado hace n-1 comparaciones y 0 intercambios: sale por la");
        System.out.println("  bandera (mejor caso Θ(n)). El inverso hace n(n-1)/2 de cada uno:");
        System.out.println("  es el peor caso Θ(n²).");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Cantidad de elementos del arreglo: ");
            if (!entrada.hasNextInt()) {
                return;
            }
            int cantidad = entrada.nextInt();

            if (cantidad < 0) {
                System.out.println("  La cantidad no puede ser negativa.");
                return;
            }

            int[] vector = new int[cantidad];
            System.out.println("Ingrese los " + cantidad + " elementos:");
            for (int i = 0; i < cantidad; i++) {
                System.out.print("  Elemento " + i + ": ");
                vector[i] = entrada.nextInt();
            }

            System.out.println();
            System.out.println("  Arreglo original: " + formatearVector(vector));
            System.out.println("  Estado despues de cada pasada:");

            long[] comparaciones = new long[1];
            long[] intercambios = new long[1];
            int[] pasos = new int[1];
            bubbleSort(vector, true, comparaciones, intercambios, pasos);

            System.out.println();
            System.out.println("  Arreglo final : " + formatearVector(vector));
            System.out.println("  Comparaciones : " + comparaciones[0]);
            System.out.println("  Intercambios  : " + intercambios[0]);
            System.out.println("  Peor caso Θ(n²) · Mejor caso Θ(n) · Espacio Θ(1)");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Devuelve el arreglo como texto entre corchetes. */
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

    /** Copia el arreglo: cada prueba necesita partir del mismo estado. */
    private static int[] copiar(int[] vector) {
        int[] copia = new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            copia[i] = vector[i];
        }
        return copia;
    }

    /** Compara dos vectores: mismo contenido y misma longitud. */
    private static boolean sonIguales(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - No se modificó el prompt. El razonamiento inicial resultó correcto: el
 *   Bubble Sort con bandera de corte temprano produce exactamente el estado por
 *   pasada que pedía el enunciado.
 * - Se agregaron al prompt dos requisitos que el enunciado no pedía explícitamente
 *   pero que la consigna general sí ("qué debe mostrar por pantalla"): el
 *   detalle de comparaciones e intercambios POR PASADA, no sólo el total. Sin
 *   ese detalle el estado por pasada se muestra, pero no se ve por qué una
 *   pasada terminó antes que otra.
 * - Se agregó al prompt la verificación contra Arrays.sort como ORÁCULO
 *   EXTERNO. El enunciado no lo pedía, pero sin una referencia independiente no
 *   hay forma de saber que el resultado es correcto y no simplemente "parece"
 *   ordenado. Arrays.sort está prohibido dentro del algoritmo y permitido acá.
 * ========================================================================== */