/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java dos métodos que ordenen el mismo arreglo de números
 * enteros: bubbleSort(int[] array) y selectionSort(int[] array), y comparar
 * cuántas comparaciones e intercambios hace cada uno.
 *
 * ALGORITMO ELEGIDO
 * Los dos que el enunciado pide: Bubble Sort y Selection Sort. Son los dos
 * algoritmos "de-exchange" más básicos y por eso son los más útiles para
 * contrastar: tienen la misma complejidad formal pero estrategias opuestas.
 *
 * RAZONAMIENTO
 * Bubble Sort compara VECINOS: sólo mira dos posiciones contiguas e intercambia
 * si están desordenadas. No sabe cuál es el mínimo; va descubriéndolo por el
 * camino, y por eso el máximo "flota" hasta el final.
 *
 * Selection Sort hace algo distinto: en cada pasada busca DIRECTAMENTE el
 * mínimo del rango no ordenado recorriéndolo entero, y lo coloca en su posición
 * final con un único intercambio. Nunca compara vecinos: compara cada elemento
 * del rango contra el mínimo acumulado.
 *
 * La diferencia clave, y es lo que la comparación debe revelar: Bubble hace un
 * número de comparaciones que DEPENDE del orden de los datos (varía entre n-1 y
 * n(n-1)/2), mientras que Selection hace SIEMPRE exactamente n(n-1)/2
 * comparaciones, esté el arreglo ordenado, invertido o aleatorio. El costo de
 * Selection es independiente de los datos.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola: primero la cantidad, luego los
 * elementos. Los dos algoritmos PARTEN DE COPIAS del mismo arreglo original, para
 * que la comparación sea justa.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * Para cada algoritmo: el arreglo ordenado, las comparaciones y los
 * intercambios. Y una tabla que confronte los dos sobre cuatro tipos de datos
 * (ordenado, inverso, aleatorio, casi ordenado) para que se vea la diferencia de
 * comportamiento.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * Por qué Bubble depende del orden de los datos y Selection no, el invariante de
 * cada uno, y el motivo de que Selection no pueda cortar de forma temprana.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Ambos resultados deben coincidir con el oráculo Arrays.sort y entre sí. Se
 * agregan además los casos límite: vacío, un elemento, todos iguales, con
 * duplicados.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 2: Comparación entre Burbuja y Selección
 *
 * ALGORITMO — Bubble Sort y Selection Sort, implementados por separado.
 *
 * RAZONAMIENTO — comparar vecinos vs. buscar el mínimo
 * Bubble Sort compara elementos CONTIGUOS. Cada comparación sólo puede decidir
 * sobre esos dos elementos; el descubrimiento de dónde está el mayor es un
 * efecto secundario del conjunto de intercambios.
 *
 * Selection Sort recorre el rango no ordenado buscando el MÍNIMO de forma
 * explícita, manteniendo un índice `indiceDelMinimo`. Al terminar el recorrido
 * hace un único intercambio y el mínimo queda en su posición definitiva.
 *
 * CONSECUENCIA MEDIBLE — el hallazgo central de este ejercicio
 * Bubble Sort tiene una salida temprana (la bandera `huboIntercambio`): si una
 * pasada no intercambia nada, corta. Por eso su número de comparaciones
 * DEPENDE del orden de los datos: n-1 con el arreglo ordenado, n(n-1)/2 con el
 * arreglo invertido.
 *
 * Selection Sort NO tiene salida temprana: aunque el arreglo ya esté ordenado,
 * sigue recorriendo el rango buscando un mínimo que ya conoce. Por eso hace
 * SIEMPRE exactamente n(n-1)/2 comparaciones. Su costo es una CONSTANTE con
 * respecto a los datos.
 *
 * COMPLEJIDAD
 *                     Bubble Sort              Selection Sort
 *   Mejor caso         Θ(n)  (con bandera)      Θ(n²)  (sin salida temprana)
 *   Peor caso          Θ(n²)                    Θ(n²)
 *   Espacio auxiliar   Θ(1)                     Θ(1)
 *   Estable            Sí                       No
 *
 * INTERCAMBIOS
 *   Bubble: de 0 (ya ordenado) a n(n-1)/2 (inverso).
 *   Selection: como máximo n-1, y exactamente n-1 con el arreglo invertido.
 *   Ésta es la ventaja concreta de Selection: la mayoria de sus intercambios
 *   "sirven" (fijan un elemento en su lugar final); los de Bubble sólo mueven
 *   un elemento una posición por vez.
 *
 * INSTRUMENTACIÓN
 *   comparaciones — una por cada comparación de elementos.
 *   intercambios  — una por cada intercambio efectivo.
 *
 * CASOS LÍMITE
 *   - Vacío o un elemento: ambos hacen 0 comparaciones.
 *   - Todos iguales: 0 intercambios en ambos.
 *   - Con duplicados: Bubble es estable; Selection NO lo es.
 */
public class Ejercicio3_2 {

    // ============================================================
    //  ALGORITMO 1: BUBBLE SORT
    // ============================================================

    /**
     * Ordena el arreglo con Bubble Sort, contando comparaciones e intercambios.
     *
     * @param array          arreglo a ordenar; se modifica en el sitio
     * @param comparaciones  arreglo de longitud 1: recibe el total de comparaciones
     * @param intercambios   arreglo de longitud 1: recibe el total de intercambios
     */
    public static void bubbleSort(int[] array, long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;

        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            // Bandera de corte temprano: es lo que hace que el costo de Bubble
            // DEPENDA del orden de los datos.
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

    /**
     * Ordena el arreglo con Selection Sort, contando comparaciones e intercambios.
     *
     * @param array          arreglo a ordenar; se modifica en el sitio
     * @param comparaciones  arreglo de longitud 1: recibe el total de comparaciones
     * @param intercambios   arreglo de longitud 1: recibe el total de intercambios
     */
    public static void selectionSort(int[] array, long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;

        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        // Invariante: al comenzar la pasada i, el prefijo array[0..i-1] ya está
        // en su posición definitiva y contiene los i menores elementos.
        for (int i = 0; i < n - 1; i++) {

            // Se busca el MÍNIMO del rango no ordenado [i..n-1].
            int indiceDelMinimo = i;

            for (int j = i + 1; j < n; j++) {
                comparaciones[0]++;
                if (array[j] < array[indiceDelMinimo]) {
                    indiceDelMinimo = j;
                }
            }

            // Un único intercambio pone el mínimo en su lugar.
            if (indiceDelMinimo != i) {
                int auxiliar = array[i];
                array[i] = array[indiceDelMinimo];
                array[indiceDelMinimo] = auxiliar;
                intercambios[0]++;
            }

            // OJO: NO hay salida temprana acá. Aunque el arreglo ya esté
            // ordenado, el recorrido del rango se hace completo. Por eso el
            // conteo de comparaciones es siempre n(n-1)/2.
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
        demostrarDiferenciaDeComportamiento();
        demostrarCasosLimite();
        ejecutarInteractivo();
    }

    private static void demostrarComparacionDirecta() {
        System.out.println("=== EJERCICIO 3.2: BUBBLE SORT vs. SELECTION SORT ===");
        System.out.println();

        int[] original = { 64, 34, 25, 12, 22 };
        System.out.println("  Arreglo original: " + formatearVector(original));

        // Cada algoritmo parte de una COPIA del mismo original: si se usara el
        // mismo arreglo, el segundo encontraría el trabajo ya hecho.
        long[] comparacionesBubble = new long[1];
        long[] intercambiosBubble = new long[1];
        long[] comparacionesSelection = new long[1];
        long[] intercambiosSelection = new long[1];

        int[] copiaBubble = copiar(original);
        int[] copiaSelection = copiar(original);

        bubbleSort(copiaBubble, comparacionesBubble, intercambiosBubble);
        selectionSort(copiaSelection, comparacionesSelection, intercambiosSelection);

        System.out.println();
        System.out.println("  Bubble Sort    -> " + formatearVector(copiaBubble)
                + " | comparaciones: " + comparacionesBubble[0]
                + " | intercambios: " + intercambiosBubble[0]);
        System.out.println("  Selection Sort -> " + formatearVector(copiaSelection)
                + " | comparaciones: " + comparacionesSelection[0]
                + " | intercambios: " + intercambiosSelection[0]);

        System.out.println();
        System.out.println("  Coinciden entre si            : " + sonIguales(copiaBubble, copiaSelection));
        int[] oraculo = copiar(original);
        java.util.Arrays.sort(oraculo);
        System.out.println("  Coinciden con el oraculo       : "
                + (sonIguales(copiaBubble, oraculo) && sonIguales(copiaSelection, oraculo)));
        System.out.println("  Intercambios de Selection <= los de Bubble: "
                + (intercambiosSelection[0] <= intercambiosBubble[0]));
    }

    private static void demostrarDiferenciaDeComportamiento() {
        System.out.println();
        System.out.println("--- DIFERENCIA CLAVE: Bubble depende de los datos, Selection no ---");
        System.out.println();

        int[][] casos = {
            { 1, 2, 3, 4, 5, 6, 7, 8 },
            { 8, 7, 6, 5, 4, 3, 2, 1 },
            { 4, 1, 3, 2, 8, 5, 7, 6 },
            { 1, 2, 3, 5, 4, 6, 7, 8 }
        };
        String[] nombres = { "Ordenado", "Inverso", "Aleatorio", "Casi ordenado" };

        System.out.printf("  %-16s | %-26s | %-26s%n", "", "BUBBLE SORT", "SELECTION SORT");
        System.out.printf("  %-16s | %12s %12s | %12s %12s%n", "tipo", "comparac.", "intercamb.", "comparac.", "intercamb.");
        System.out.printf("  %-16s-+-%-12s-+-%-12s-+-%-12s-+-%-12s%n",
                "----------------", "------------", "------------", "------------", "------------");

        long[] cb = new long[1];
        long[] ib = new long[1];
        long[] cs = new long[1];
        long[] is = new long[1];

        for (int c = 0; c < casos.length; c++) {
            bubbleSort(copiar(casos[c]), cb, ib);
            selectionSort(copiar(casos[c]), cs, is);

            System.out.printf("  %-16s | %12d %12d | %12d %12d%n",
                    nombres[c], cb[0], ib[0], cs[0], is[0]);
        }

        int n = 8;
        long esperado = (long) n * (n - 1) / 2;
        System.out.println();
        System.out.println("  Con n = " + n + ", n(n-1)/2 = " + esperado);
        System.out.println("  Selection hace SIEMPRE " + esperado
                + " comparaciones: es el unico con costo independiente de los datos.");
        System.out.println("  Bubble va de " + (n - 1) + " (ordenado, corta por la bandera) a "
                + esperado + " (inverso).");

        System.out.println();
        System.out.println("  INTERCAMBIOS: Selection hace como maximo n-1 = " + (n - 1)
                + ", porque cada intercambio fija un elemento en su lugar.");
        System.out.println("  Bubble puede necesitar " + esperado
                + ", porque cada intercambio mueve un elemento UNA sola posicion.");
    }

    private static void demostrarCasosLimite() {
        System.out.println();
        System.out.println("--- CASOS LÍMITE ---");

        long[] cb = new long[1];
        long[] ib = new long[1];
        long[] cs = new long[1];
        long[] is = new long[1];

        System.out.println();
        System.out.println("  Vacío:");
        bubbleSort(new int[] {}, cb, ib);
        selectionSort(new int[] {}, cs, is);
        System.out.println("    Bubble: " + cb[0] + " comparaciones, " + ib[0]
                + " intercambios | Selection: " + cs[0] + " comparaciones, " + is[0] + " intercambios");

        System.out.println("  Un elemento:");
        bubbleSort(new int[] { 7 }, cb, ib);
        selectionSort(new int[] { 7 }, cs, is);
        System.out.println("    Bubble: " + cb[0] + " comparaciones, " + ib[0]
                + " intercambios | Selection: " + cs[0] + " comparaciones, " + is[0] + " intercambios");

        System.out.println("  Todos iguales:");
        bubbleSort(new int[] { 5, 5, 5, 5 }, cb, ib);
        selectionSort(new int[] { 5, 5, 5, 5 }, cs, is);
        System.out.println("    Bubble: " + cb[0] + " comparaciones, " + ib[0]
                + " intercambios | Selection: " + cs[0] + " comparaciones, " + is[0] + " intercambios");
        System.out.println("    Selection recorre el rango igual y hace 6 comparaciones: no puede cortar.");
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

            long[] comparacionesBubble = new long[1];
            long[] intercambiosBubble = new long[1];
            long[] comparacionesSelection = new long[1];
            long[] intercambiosSelection = new long[1];

            int[] copiaBubble = copiar(original);
            int[] copiaSelection = copiar(original);

            bubbleSort(copiaBubble, comparacionesBubble, intercambiosBubble);
            selectionSort(copiaSelection, comparacionesSelection, intercambiosSelection);

            System.out.println("  Bubble Sort    -> " + formatearVector(copiaBubble)
                    + " | comparaciones: " + comparacionesBubble[0]
                    + " | intercambios: " + intercambiosBubble[0]);
            System.out.println("  Selection Sort -> " + formatearVector(copiaSelection)
                    + " | comparaciones: " + comparacionesSelection[0]
                    + " | intercambios: " + intercambiosSelection[0]);

            System.out.println();
            System.out.printf("  n = %d, n(n-1)/2 = %d%n", cantidad,
                    (long) cantidad * (cantidad - 1) / 2);
            System.out.println("  Si Selection hizo exactamente n(n-1)/2 comparaciones, confirma");
            System.out.println("  que su costo no depende del orden de los datos.");
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

    /** Copia el arreglo: cada algoritmo debe partir del mismo estado original. */
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
 * - Seadjustó el prompt para pedir explícitamente el HALLAZGO CENTRAL: que
 *   Selection hace siempre n(n-1)/2 comparaciones y Bubble varía. La consigna
 *   pedía "contar comparaciones e intercambios", pero sin este foco el alumno
 *   puede mostrar dos números y no descubrir la diferencia. El enunciado pide
 *   razonar sobre "comparar vecinos vs buscar el mínimo", y ese razonamiento
 *   sólo se vuelve visible cuando los contadores se contrastan entre sí.
 * - Se agregó la comparación sobre CUATRO tipos de arreglo. Con un solo arreglo
 *   de prueba los conteos de ambos algoritmos pueden coincidir por casualidad y
 *   la diferencia pasa desapercibida.
 * - Se aclaró que los dos algoritmos deben partir de COPIAS del mismo original.
 *   Sin eso, el segundo que se ejecuta recibe el arreglo ya ordenado y sus
 *   contadores son engañosos.
 * ========================================================================== */