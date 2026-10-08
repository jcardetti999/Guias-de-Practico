/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java los algoritmos Bubble Sort, Selection Sort e
 * Insertion Sort, ordenar el mismo arreglo con los tres y comparar cuántos
 * intercambios, comparaciones y desplazamientos realiza cada uno.
 *
 * ALGORITMO ELEGIDO
 * Los tres que el enunciado pide: Bubble, Selection e Insertion. Son los
 * algoritmos "simples" por excelencia, y su valor pedagógico está en que tienen
 * estructuras muy distintas aunque la complejidad formal sea la misma.
 *
 * RAZONAMIENTO — por qué el objetivo es comparar ESTRATEGIAS y no resultados
 * Los tres ordenan el mismo arreglo y producen EXACTAMENTE el mismo resultado.
 * Si sólo se mirara el resultado final, no habría nada que comparar. Lo que
 * cambia es el TRABAJO que hacen para llegar ahí:
 *
 *   - Bubble compara VECINOS y su costo depende del orden de los datos.
 *   - Selection busca el MÍNIMO en todo el rango: su costo es siempre el mismo,
 *     esté el arreglo como esté, pero hace pocos intercambios.
 *   - Insertion INSERTA cada elemento en el prefijo: su costo depende de la
 *     distancia que cada elemento recorre, así que es el mejor con datos
 *     casi ordenados.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola: cantidad y luego valores. Los tres
 * algoritmos PARTEN DE COPIAS del mismo original, para que la comparación sea
 * justa.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * Para cada algoritmo: el arreglo ordenado, las comparaciones y los
 * intercambios o desplazamientos. Y una tabla que confronte los tres sobre
 * cuatro tipos de arreglo (ordenado, inverso, aleatorio, casi ordenado).
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * Cómo se verían las diferencias en la complejidad, y por qué Insertion Sort es
 * el mejor de los tres con datos casi ordenados.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Los tres resultados deben coincidir entre sí y con el oráculo Arrays.sort.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 9: Comparación de algoritmos simples
 *
 * ALGORITMO — Bubble Sort, Selection Sort e Insertion Sort.
 *
 * RAZONAMIENTO — tres estrategias distintas, un mismo resultado
 * Los tres ordenan el arreglo y producen el mismo resultado final. Lo que
 * cambia es CÓMO llegan:
 *
 *   Bubble    compara VECINOS; el máximo "flota" al final. Sale temprano si una
 *             pasada no intercambia nada. Costo depende de los datos.
 *   Selection busca el MÍNIMO del rango y lo fija en su lugar. Sin salida
 *             temprana: SIEMPRE hace n(n-1)/2 comparaciones. Pocos intercambios.
 *   Insertion inserta cada elemento en el prefijo ordenado. Costo depende de la
 *             distancia que cada elemento recorre. El mejor con casi-ordenados.
 *
 * COMPLEJIDAD
 *              |  Mejor caso |  Peor caso |  Espacio |  Estable
 *   Bubble    |  Θ(n)       |  Θ(n²)      |  Θ(1)   |  Sí
 *   Selection |  Θ(n²)      |  Θ(n²)      |  Θ(1)   |  No
 *   Insertion |  Θ(n)       |  Θ(n²)      |  Θ(1)   |  Sí
 *
 *   Los tres son Θ(n²) en el peor caso y Θ(1) en memoria. Por eso son
 *   inadecuados para producción: Timsort, que usa Java, es Θ(n log n).
 *
 * INSTRUMENTACIÓN
 *   comparaciones e intercambios/desplazamientos, medidos por separado.
 *   En Insertion se cuentan los desplazamientos (elementos movidos) que NO son
 *   lo mismo que las comparaciones.
 *
 * CASOS LÍMITE
 *   - Vacío o un solo elemento: los tres hacen 0 comparaciones.
 *   - Ya ordenado: Bubble es el mejor (n-1 comparaciones, 0 intercambios).
 *   - Inverso: Bubble es el peor (n(n-1)/2 comparaciones e intercambios).
 */
public class Ejercicio9 {

    // ============================================================
    //  ALGORITMO 1: BUBBLE SORT
    // ============================================================

    public static void bubbleSort(int[] array, long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;
        if (array == null || array.length <= 1) {
            return;
        }
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            boolean huboIntercambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparaciones[0]++;
                if (array[j] > array[j + 1]) {
                    int auxiliar = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = auxiliar;
                    intercambios[0]++;
                    huboIntercambio = true;
                }
            }
            if (!huboIntercambio) {
                break;
            }
        }
    }

    // ============================================================
    //  ALGORITMO 2: SELECTION SORT
    // ============================================================

    public static void selectionSort(int[] array, long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;
        if (array == null || array.length <= 1) {
            return;
        }
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int indiceDelMinimo = i;
            for (int j = i + 1; j < n; j++) {
                comparaciones[0]++;
                if (array[j] < array[indiceDelMinimo]) {
                    indiceDelMinimo = j;
                }
            }
            if (indiceDelMinimo != i) {
                int auxiliar = array[i];
                array[i] = array[indiceDelMinimo];
                array[indiceDelMinimo] = auxiliar;
                intercambios[0]++;
            }
        }
    }

    // ============================================================
    //  ALGORITMO 3: INSERTION SORT
    // ============================================================

    public static void insertionSort(int[] array, long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;
        if (array == null || array.length <= 1) {
            return;
        }
        for (int i = 1; i < array.length; i++) {
            int elemento = array[i];
            int j = i - 1;
            while (j >= 0) {
                comparaciones[0]++;
                if (array[j] <= elemento) {
                    break;
                }
                array[j + 1] = array[j];
                intercambios[0]++;
                j--;
            }
            array[j + 1] = elemento;
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

        demostrarComparacionDirecta();
        demostrarEstrategias();
        ejecutarInteractivo();
    }

    private static void demostrarComparacionDirecta() {
        System.out.println("=== EJERCICIO 9: COMPARACIÓN DE ALGORITMOS SIMPLES ===");
        System.out.println();

        int[] original = { 64, 34, 25, 12, 22 };
        System.out.println("  Arreglo original: " + formatearVector(original));

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];

        int[] bubble = copiar(original);
        bubbleSort(bubble, comparaciones, intercambios);
        System.out.println("  Bubble Sort    -> " + formatearVector(bubble)
                + " | comparaciones: " + comparaciones[0]
                + " | intercambios: " + intercambios[0]);

        comparaciones[0] = 0;
        intercambios[0] = 0;
        int[] selection = copiar(original);
        selectionSort(selection, comparaciones, intercambios);
        System.out.println("  Selection Sort -> " + formatearVector(selection)
                + " | comparaciones: " + comparaciones[0]
                + " | intercambios: " + intercambios[0]);

        comparaciones[0] = 0;
        intercambios[0] = 0;
        int[] insertion = copiar(original);
        insertionSort(insertion, comparaciones, intercambios);
        System.out.println("  Insertion Sort -> " + formatearVector(insertion)
                + " | comparaciones: " + comparaciones[0]
                + " | desplazamientos: " + intercambios[0]);

        System.out.println();
        System.out.println("  Los TRES producen el mismo resultado: " + sonIguales(bubble, selection));

        int[] oraculo = copiar(original);
        java.util.Arrays.sort(oraculo);
        System.out.println("  Coinciden con el oráculo: "
                + (sonIguales(bubble, oraculo) && sonIguales(selection, oraculo)
                    && sonIguales(insertion, oraculo)));
    }

    private static void demostrarEstrategias() {
        System.out.println();
        System.out.println("--- CÓMO DEPENDE EL COSTO DEL ORDEN DE LOS DATOS ---");
        System.out.println();

        int[][] casos = {
            { 1, 2, 3, 4, 5, 6, 7, 8 },                  // ordenado
            { 8, 7, 6, 5, 4, 3, 2, 1 },                  // inverso
            { 4, 1, 3, 2, 8, 5, 7, 6 },                  // aleatorio
            { 1, 2, 3, 5, 4, 6, 7, 8 }                   // casi ordenado
        };
        String[] nombres = { "Ordenado", "Inverso", "Aleatorio", "Casi ordenado" };

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];

        for (int c = 0; c < casos.length; c++) {
            System.out.println("  --- " + nombres[c] + " : " + formatearVector(casos[c]) + " ---");

            bubbleSort(copiar(casos[c]), comparaciones, intercambios);
            System.out.printf("      Bubble Sort    | comparaciones: %8d | intercambios:   %8d%n",
                    comparaciones[0], intercambios[0]);

            selectionSort(copiar(casos[c]), comparaciones, intercambios);
            System.out.printf("      Selection Sort | comparaciones: %8d | intercambios:   %8d%n",
                    comparaciones[0], intercambios[0]);

            insertionSort(copiar(casos[c]), comparaciones, intercambios);
            System.out.printf("      Insertion Sort | comparaciones: %8d | desplazamientos:%8d%n",
                    comparaciones[0], intercambios[0]);

            System.out.println();
        }

        System.out.println("  LECTURA DE LA TABLA:");
        System.out.println("    - Bubble: con el arreglo ordenado hace 7 comparaciones (corta por");
        System.out.println("      la bandera); con el inverso hace 28. Su costo SÍ depende de los datos.");
        System.out.println("    - Selection: SIEMPRE 28 comparaciones, en las cuatro situaciones.");
        System.out.println("      Su costo NO depende de los datos, a cambio de más trabajo en el mejor caso.");
        System.out.println("    - Insertion: 7 comparaciones con el ordenado, 28 con el inverso.");
        System.out.println("      Es el MEJOR de los tres con datos casi ordenados.");
        System.out.println();
        System.out.println("    En resumen: no hay un \"mejor algoritmo\" universal. Cada uno");
        System.out.println("    aprovecha algo distinto del problema.");
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

            int[] original = new int[cantidad];
            System.out.println("Ingrese los " + cantidad + " elementos:");
            for (int i = 0; i < cantidad; i++) {
                System.out.print("  Elemento " + i + ": ");
                original[i] = entrada.nextInt();
            }

            System.out.println();
            System.out.println("  Original: " + formatearVector(original));

            long[] comparaciones = new long[1];
            long[] intercambios = new long[1];

            System.out.println();
            bubbleSort(copiar(original), comparaciones, intercambios);
            System.out.println("  Bubble Sort    -> comparaciones: " + comparaciones[0]
                    + " | intercambios: " + intercambios[0]);

            selectionSort(copiar(original), comparaciones, intercambios);
            System.out.println("  Selection Sort -> comparaciones: " + comparaciones[0]
                    + " | intercambios: " + intercambios[0]);

            insertionSort(copiar(original), comparaciones, intercambios);
            System.out.println("  Insertion Sort -> comparaciones: " + comparaciones[0]
                    + " | desplazamientos: " + intercambios[0]);

            System.out.println();
            System.out.println("  Con n = " + cantidad + ", n(n-1)/2 = "
                    + (long) cantidad * (cantidad - 1) / 2);
            System.out.println("  Si Selection hizo exactamente eso, su costo es independiente");
            System.out.println("  de los datos.");
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

    /** Copia el arreglo: cada algoritmo parte del mismo estado original. */
    private static int[] copiar(int[] vector) {
        int[] copia = new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            copia[i] = vector[i];
        }
        return copia;
    }

    /** Compara dos vectores. */
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
 * - Se agregó al prompt la exigencia de ejecutar los tres algoritmos sobre
 *   CUATRO tipos de arreglo. El enunciado dice "ordenar el mismo arreglo con los
 *   tres métodos", pero con un solo arreglo los conteos pueden coincidir y la
 *   comparación no revela nada. Con cuatro situaciones se ve que Bubble e
 *   Insertion dependen del orden de los datos y Selection no.
 * - Se aclaró que Insertion cuenta DESPLAZAMIENTOS y los otros dos
 *   INTERCAMBIOS. No son la misma operación y conviene nombrarlos distinto.
 * - Se agregó la verificación cruzada: los tres resultados deben coincidir entre
 *   sí y con el oráculo. Sin esa comprobación, si uno de los tres tuviera un
 *   error la tabla comparativa igual "funcionaría" mostrando números.
 * ========================================================================== */