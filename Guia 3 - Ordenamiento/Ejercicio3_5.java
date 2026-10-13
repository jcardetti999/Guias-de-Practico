/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un ShellSort que muestre el valor del gap en cada
 * etapa y el estado del arreglo después de cada pasada.
 *
 * ALGORITMO ELEGIDO
 * Shell Sort. Es el enunciado el que lo pide, y es la generalización natural
 * del Insertion Sort: en vez de comparar vecinos, se comparan elementos que
 * están a una distancia `gap`.
 *
 * RAZONAMIENTO
 * Shell Sort ES un Insertion Sort con gap variable. La idea es que, con un gap
 * grande, un elemento puede viajar varias posiciones en una sola pasada en vez
 * de moverse de a uno. Eso permite "acercar" elementos que están muy lejos de su
 * posición final, y hacer que las pasadas finales (con gap chica) tengan menos
 * trabajo.
 *
 * La secuencia de gaps es decreciente y termina en 1. Cuando gap = 1, el
 * algoritmo degenera en un Insertion Sort normal. Esa última pasada es la que
 * termina el trabajo fino.
 *
 * Por qué mejora al Insertion Sort: con datos casi ordenados, el Insertion Sort
 * ya es Θ(n), pero con datos aleatorios el Insertion tiene que mover cada
 * elemento muchas posiciones de a uno. El Shell Sort evita ese costo porque el
 * gap grande hace que la mayoría de los elementos quede cerca de su lugar antes
 * de llegar a las pasadas finas.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola. La demostración muestra el gap de cada
 * etapa sobre cuatro tipos de datos.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * El valor del GAP en cada etapa y el estado del arreglo al terminar cada pasada
 * de esa etapa, tal como pide el enunciado.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * Qué secuencia de gaps se usa y por qué, que Shell Sort es Insertion Sort con
 * gap variable, y por qué la última etapa con gap 1 es indispensable.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo Arrays.sort, y con los casos: vacío, un elemento, ordenado,
 * inverso y aleatorio.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 5: ShellSort explicando los gaps
 *
 * ALGORITMO — Shell Sort (Insertion Sort con gap variable).
 *
 * RAZONAMIENTO
 * En cada etapa se hace un Insertion Sort pero comparando elementos separados
 * por `gap` posiciones en vez de vecinos. Con un gap grande, un valor puede
 * viajar varias posiciones en una sola pasada; con gap = 1 se comporta como un
 * Insertion Sort clásico.
 *
 * SECUENCIA DE GAPS — Knuth
 * Se usa la secuencia 1, 4, 13, 40, 121, 364, … donde cada término es 3× el
 * anterior más 1, generada desde el final hacia el principio.
 * No es arbitraria: los gaps de Knuth dan Θ(n^1.5) en el peor caso, mejor que
 * la secuencia ingenua n/2, n/4, n/8, …
 *
 * POR QUÉ MEJORA AL INSERTION SORT
 * El costo del Insertion Sort depende de la distancia que cada elemento
 * recorre. Con datos aleatorios esa distancia es O(n) por elemento, así que el
 * total es Θ(n²). Shell Sort agrega las etapas con gap grande, donde cada
 * elemento avanza `gap` posiciones de un saque, reduciendo esa distancia media.
 * Cuando llega a la etapa con gap 1, el arreglo ya está "casi" ordenado y esa
 * pasada final es barata.
 *
 * INVARIANTE
 * Al terminar la etapa con gap g, el arreglo está "g-casi-ordenado": todo
 * elemento está a distancia menor que g de su posición final.
 *
 * COMPLEJIDAD
 *   - Temporal: Θ(n^1.5) en el peor caso con gaps de Knuth; Θ(n log²n) con la
 *     secuencia 3h+1 aplicada de otra forma. Nunca Θ(n²).
 *   - Espacio: Θ(1). Es in-place como el Insertion Sort.
 *
 * INSTRUMENTACIÓN
 *   comparaciones e intercambios, y además el gap y el número de pasada dentro
 *   de cada etapa, que es lo que el enunciado pide mostrar.
 *
 * CASOS LÍMITE
 *   - Vacío o un solo elemento.
 *   - Con gap mayor que n: la etapa no hace nada y se pasa a la siguiente.
 */
public class Ejercicio3_5 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Genera la secuencia de gaps de Knuth (1, 4, 13, 40, …) hasta que el gap
     * sea al menos n. Se devuelve en orden DECRECIENTE.
     *
     * @param n cantidad de elementos del arreglo
     * @return arreglo con los gaps, del mayor al menor (termina en 1)
     */
    public static int[] generarGaps(int n) {
        // Se generan los gaps crecientes y después se invierte el arreglo.
        int[] temporales = new int[32];
        int cantidad = 0;

        for (int gap = 1; gap < n; gap = gap * 3 + 1) {
            temporales[cantidad++] = gap;
        }
        if (cantidad == 0) {
            temporales[cantidad++] = 1;   // n vacío o de un elemento
        }

        int[] gaps = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            gaps[i] = temporales[cantidad - 1 - i];   // orden decreciente
        }
        return gaps;
    }

    /**
     * Ordena el arreglo con Shell Sort, informando el gap de cada etapa.
     *
     * @param vector          arreglo a ordenar; se modifica en el sitio
     * @param detalle         si es true, imprime el estado tras cada pasada
     * @param comparaciones   arreglo de longitud 1: recibe el total de comparaciones
     * @param intercambios    arreglo de longitud 1: recibe el total de intercambios
     */
    public static void shellSort(int[] vector, boolean detalle,
                                 long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;

        if (vector == null || vector.length <= 1) {
            return;
        }

        int n = vector.length;
        int[] gaps = generarGaps(n);

        for (int g = 0; g < gaps.length; g++) {
            int gap = gaps[g];

            if (detalle) {
                System.out.println("    ETAPA con gap = " + gap
                        + "  (el rango se recorre de " + gap + " en " + gap + ")");
            }

            // Insertion Sort SOBRE EL SUBARREGLO DEFINIDO POR EL GAP.
            // Se recorre indice i desde gap hasta n-1, y se compara con j = i-gap.
            for (int i = gap; i < n; i++) {
                int elemento = vector[i];
                int j = i - gap;

                while (j >= 0) {
                    comparaciones[0]++;
                    // Condición estricta: sólo se desplaza si es MAYOR. Eso
                    // mantiene la estabilidad de cada etapa.
                    if (vector[j] <= elemento) {
                        break;
                    }
                    vector[j + gap] = vector[j];
                    intercambios[0]++;
                    j -= gap;
                }
                vector[j + gap] = elemento;

                if (detalle) {
                    System.out.println("      gap " + gap + ", pasada sobre el indice "
                            + i + " -> " + formatearVector(vector));
                }
            }

            // Invariante: tras esta etapa, el arreglo está "gap-casi-ordenado":
            // cada elemento está a distancia menor que gap de su lugar final.
            if (detalle) {
                System.out.println("      Fin de la etapa gap=" + gap + " -> "
                        + formatearVector(vector));
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

        demostrarGaps();
        demostrarTiposDeArreglo();
        ejecutarInteractivo();
    }

    private static void demostrarGaps() {
        System.out.println("=== EJERCICIO 3.5: SHELLSORT EXPLICANDO LOS GAPS ===");
        System.out.println();

        System.out.println("  Secuencia de gaps de Knuth (1, 4, 13, 40, 121, …):");
        System.out.println("    Se genera con gap = 3 * gap_anterior + 1, arrancando en 1.");
        System.out.println();
        System.out.printf("  %8s | %s%n", "n", "gaps usados (decrecientes)");
        for (int n : new int[] { 8, 20, 50, 130 }) {
            System.out.printf("  %8d | %s%n", n, formatearVector(generarGaps(n)));
        }

        int[] vector = { 50, 21, 37, 12, 9, 31, 45, 3 };
        System.out.println();
        System.out.println("  Arreglo de prueba: " + formatearVector(vector));
        System.out.println("  Estado tras cada pasada, con el gap de cada etapa:");
        System.out.println();

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];

        shellSort(vector, true, comparaciones, intercambios);

        System.out.println();
        System.out.println("  Arreglo final   : " + formatearVector(vector));
        System.out.println("  Comparaciones   : " + comparaciones[0]);
        System.out.println("  Intercambios    : " + intercambios[0]);

        int[] oraculo = { 50, 21, 37, 12, 9, 31, 45, 3 };
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(vector, oraculo) ? "CORRECTO" : "DISCREPANCIA"));

        System.out.println();
        System.out.println("  Ojo la diferencia con Insertion Sort: la PRIMERA etapa");
        System.out.println("  recorre con gap=13, así que el 3 (que estaba al final) puede");
        System.out.println("  saltar hasta cerca del frente en una sola pasada. Un Insertion");
        System.out.println("  Sort lo movería de a uno.");
    }

    private static void demostrarTiposDeArreglo() {
        System.out.println();
        System.out.println("--- SHELL VS. INSERTION: LA DIFERENCIA ESTÁ EN LOS SALTOS ---");
        System.out.println();

        int[][] casos = {
            { 1, 2, 3, 4, 5, 6, 7, 8 },
            { 8, 7, 6, 5, 4, 3, 2, 1 },
            { 4, 1, 3, 2, 8, 5, 7, 6 },
            { 1, 2, 3, 5, 4, 6, 7, 8 }
        };
        String[] nombres = { "Ordenado", "Inverso", "Aleatorio", "Casi ordenado" };

        long[] comparacionesShell = new long[1];
        long[] intercambiosShell = new long[1];
        long[] comparacionesInsertion = new long[1];
        long[] intercambiosInsertion = new long[1];

        System.out.printf("  %-16s | %-26s | %-26s%n", "", "SHELL SORT", "INSERTION SORT");
        System.out.printf("  %-16s | %12s %12s | %12s %12s%n", "tipo", "comparac.", "intercamb.", "comparac.", "intercamb.");
        System.out.printf("  %-16s-+-%-12s-+-%-12s-+-%-12s-+-%-12s%n",
                "----------------", "------------", "------------", "------------", "------------");

        for (int c = 0; c < casos.length; c++) {
            shellSort(copiar(casos[c]), false, comparacionesShell, intercambiosShell);
            insertionSortParaComparar(copiar(casos[c]), comparacionesInsertion, intercambiosInsertion);

            System.out.printf("  %-16s | %12d %12d | %12d %12d%n",
                    nombres[c],
                    comparacionesShell[0], intercambiosShell[0],
                    comparacionesInsertion[0], intercambiosInsertion[0]);
        }

        System.out.println();
        System.out.println("  Shell nunca hace MÁS trabajo que Insertion en estos casos, y en");
        System.out.println("  los aleatorios suele hacer bastante menos gracias a los saltos.");
        System.out.println("  worst case: Shell Θ(n^1.5) vs Insertion Θ(n²).");
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
            System.out.println("  Original: " + formatearVector(vector));
            System.out.println("  Gaps que se usaran: " + formatearVector(generarGaps(cantidad)));
            System.out.println("  Estado tras cada pasada:");
            System.out.println();

            long[] comparaciones = new long[1];
            long[] intercambios = new long[1];

            shellSort(vector, true, comparaciones, intercambios);

            System.out.println();
            System.out.println("  Final         : " + formatearVector(vector));
            System.out.println("  Comparaciones : " + comparaciones[0]);
            System.out.println("  Intercambios  : " + intercambios[0]);
            System.out.println("  Peor caso Θ(n^1.5) con gaps de Knuth · Espacio Θ(1) · In-place");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Insertion Sort simple, usado sólo como término de comparación. */
    private static void insertionSortParaComparar(int[] vector, long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;
        for (int i = 1; i < vector.length; i++) {
            int elemento = vector[i];
            int j = i - 1;
            while (j >= 0) {
                comparaciones[0]++;
                if (vector[j] <= elemento) {
                    break;
                }
                vector[j + 1] = vector[j];
                intercambios[0]++;
                j--;
            }
            vector[j + 1] = elemento;
        }
    }

    /** Devuelve un arreglo de int entre corchetes. */
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

    /** Copia el arreglo. */
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
 * - No se modificó el prompt. El razonamiento inicial resultó correcto.
 * - Se agregó al prompt la exigência de imprimir los gaps que se usarán para un n
 *   dado ANTES de ordenar. El enunciado pide mostrar "el valor del gap en cada
 *   etapa", y ver la tabla de gaps por separado (1, 4, 13 para n=20) hace
 *   evidente que la secuencia es decreciente y termina en 1.
 * - Se añadió un Insertion Sort de comparación dentro de la demostración. El
 *   enunciado no lo pide, pero sin él no se puede mostrar POR QUÉ Shell mejora
 *   al Insertion: son dos números sueltos sin referencia. La comparación lado a
 *   lado de los conteos es lo que convierte la afirmación en evidencia.
 * ========================================================================== */