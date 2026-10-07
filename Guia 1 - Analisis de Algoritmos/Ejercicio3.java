/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 3: Buscar un elemento en un vector ordenado (búsqueda binaria)
 *
 * PRECONDICIÓN
 * El vector DEBE estar ordenado en forma ascendente. Esta es una precondición
 * fuerte: si no se cumple, el algoritmo devuelve un resultado incorrecto sin
 * avisar. Por eso la demo usa siempre vectores ya ordenados.
 *
 * ESTRATEGIA — Divide y vencercer
 * Se mantienen tres índices: low (límite izquierdo), high (límite derecho) y
 * mid (punto medio). En cada vuelta se compara el valor buscado con vector[mid]:
 *   - si es igual, se encontró;
 *   - si es menor, el objetivo está en [low, mid-1], así que high = mid-1;
 *   - si es mayor, el objetivo está en [mid+1, high], así que low = mid+1.
 * El rango candidatos se reduce a la mitad en cada paso.
 *
 * POR QUÉ ES MÁS EFICIENTE QUE UNA BÚSQUEDA LINEAL
 * La diferencia no es "más rápido": es CUÁNTA INFORMACIÓN aporta cada
 * comparación.
 *   - En la búsqueda lineal sobre un vector desordenado, comparar con un
 *     elemento sólo permite descartar ese elemento. Quedan n-1 candidatos.
 *   - En un vector ORDENADO, comparar aporta MONOTONÍA: la comparación con el
 *     medio descarta de golpe toda una mitad. Quedan ~n/2 candidatos.
 * Es decir, el ordenamiento convierte una comparación en "descartar 1" en una
 * comparación en "descartar la mitad". Esa es toda la diferencia.
 *
 * Costo real (aquí está la honestidad del análisis):
 *   Ordenar una vez + buscar:  Θ(n log n) + Θ(log n)  =  Θ(n log n)
 *   Búsqueda lineal sin ordenar:               Θ(n)
 * Por lo tanto ordenar SÓLO conviene si se van a hacer muchas consultas sobre
 * el mismo vector, o si el vector ya viene ordenado de origen. Para una sola
 * búsqueda, ordenar es Peor que la búsqueda lineal.
 *
 * COMPLEJIDAD
 * Temporal:
 *   - Mejor caso:    Θ(1). Coincide en el primer punto medio.
 *   - Peor caso:     Θ(log n). El rango se divide por 2 en cada paso:
 *                     n → n/2 → n/4 → ... → 1, lo que requiere ⌊log2 n⌋ + 1 pasos.
 *   - Caso promedio: Θ(log n). También log n.
 * Nota: para n = 1.000.000, log2(n) ≈ 20. Sólo unas 20 comparaciones, frente
 * a hasta 1.000.000 de la búsqueda lineal.
 * Espacial: Θ(1). Sólo los tres índices; no hay recursión ni tabla auxiliar.
 *
 * CASOS LÍMITE
 * - vector vacío -> devuelve -1 sin lanzar excepción.
 * - duplicados -> devuelve una de las posiciones que contienen el valor (no
 *   necesariamente la primera).
 * - valor mayor o menor que todos -> terminates por low > high.
 * - overflow: mid se calcula como low + (high - low)/2 y no como (low+high)/2,
 *   que desbordaría int si low y high fuesen ambos muy grandes.
 */
public class Ejercicio3 {

    /** Valor centinela devuelto cuando el elemento no existe en el vector. */
    public static final int NO_ENCONTRADO = -1;

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Busca un valor en un vector ordenado ascendentemente por bisección.
     *
     * @param vector       vector ordenado ascendentemente (PRECONDICIÓN); no se modifica
     * @param valorBuscado valor a localizar
     * @param comparaciones arreglo de longitud 1: recibe el nº de comparaciones
     * @param mostrarTraza si es true, imprime la evolución de low/mid/high paso a paso
     * @return el índice del elemento, o -1 si no existe
     */
    public static int buscarBinaria(int[] vector, int valorBuscado, int[] comparaciones, boolean mostrarTraza) {
        comparaciones[0] = 0;

        if (vector == null || vector.length == 0) {
            return NO_ENCONTRADO;
        }

        int low = 0;
        int high = vector.length - 1;
        int iteracion = 0;

        // Invariante: si el elemento existe, su índice está dentro de [low, high].
        // El bucle termina cuando low > high, es decir cuando el rango candidato
        // se quedó vacío y, por el invariante, el elemento no está.
        while (low <= high) {
            iteracion++;

            // Se evita el desbordamiento de int que produciría (low + high) / 2.
            int mid = low + (high - low) / 2;
            comparaciones[0]++;      // una comparación contra el elemento medio

            // Evolución paso a paso de las variables de búsqueda.
            if (mostrarTraza) {
                System.out.println("  [iteracion " + iteracion + "]"
                        + "  low=" + low
                        + "  mid=" + mid
                        + "  high=" + high
                        + "  vector[" + mid + "]=" + vector[mid]
                        + "  buscado=" + valorBuscado);
            }

            if (vector[mid] == valorBuscado) {
                if (mostrarTraza) {
                    System.out.println("      -> IGUAL: elemento encontrado en la posicion " + mid + ".");
                }
                return mid;
            } else if (vector[mid] < valorBuscado) {
                // El objetivo es mayor que el medio: se descarta toda la mitad
                // izquierda, incluido el propio mid.
                if (mostrarTraza) {
                    System.out.println("      -> vector[mid] < buscado: se descarta la mitad IZQUIERDA."
                            + "  new low=" + (mid + 1));
                }
                low = mid + 1;
            } else {
                // El objetivo es menor que el medio: se descarta toda la mitad
                // derecha, incluido el propio mid.
                if (mostrarTraza) {
                    System.out.println("      -> vector[mid] > buscado: se descarta la mitad DERECHA."
                            + "  new high=" + (mid - 1));
                }
                high = mid - 1;
            }
        }

        if (mostrarTraza) {
            System.out.println("      -> low(" + low + ") > high(" + high
                    + "): rango vacio, el elemento NO EXISTE.");
        }
        return NO_ENCONTRADO;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        demostrarBarridoCompleto();
        compararConLineal();
        ejecutarInteractivo();
    }

    /** Muestra la traza completa de una búsqueda exitosa y de una fallida. */
    private static void demostrarBarridoCompleto() {
        int[] vector = { 2, 5, 8, 12, 16, 23, 38, 56, 72, 91 };

        System.out.println("=== DEMOSTRACION AUTOMATICA ===");
        System.out.println("Vector ORDENADO: " + formatearVector(vector) + "   (n = " + vector.length + ")");

        // Mejor caso: el elemento buscado es el PRIMER punto medio que calcula el
        // algoritmo, así que se resuelve en la primera comparación.
        // Con n = 10 el primer mid es (10-1)/2 = 4.
        int primerMedio = (vector.length - 1) / 2;
        System.out.println();
        System.out.println("-- MEJOR CASO: buscar " + vector[primerMedio]
                + ", que ocupa el primer punto medio (indice " + primerMedio + ") --");
        int[] comparaciones = new int[1];
        int indice = buscarBinaria(vector, vector[primerMedio], comparaciones, true);
        System.out.println("Resultado: indice " + indice + " en " + comparaciones[0]
                + " comparacion -> Theta(1).");

        // Peor caso: el objetivo no existe y hay que agotar el rango.
        System.out.println();
        System.out.println("-- PEOR CASO: buscar 50 (no existe) --");
        indice = buscarBinaria(vector, 50, comparaciones, true);
        System.out.println("Resultado: indice " + indice
                + " tras " + comparaciones[0] + " comparaciones -> Theta(log n).");

        // Verificación de la cota logarítmica.
        int pasosTeoricos = (int) Math.ceil(Math.log(vector.length) / Math.log(2));
        System.out.println();
        System.out.println("Cota teorica: log2(" + vector.length + ") = " + pasosTeoricos
                + " pasos, frente a hasta " + vector.length + " de la busqueda lineal.");
    }

    /** Muestra por qué Θ(n log n) + Θ(log n) sólo conviene con muchas consultas. */
    private static void compararConLineal() {
        int[] vector = { 2, 5, 8, 12, 16, 23, 38, 56, 72, 91 };

        System.out.println();
        System.out.println("-- COMPARACION DE COSTOS (vector de n = " + vector.length + ") --");
        System.out.println("  Lineal   (desordenado): Theta(n)         = hasta " + vector.length + " pasos");
        System.out.println("  Lineal   (ordenado)   : Theta(n)         = hasta " + vector.length + " pasos");
        System.out.println("  Ordenar + binaria     : Theta(n log n)   = hasta "
                + (int) Math.ceil(Math.log(vector.length) / Math.log(2)) + " pasos de busqueda,");
        System.out.println("                           pero con un costo previo de ordenamiento.");
        System.out.println("  Conclusión: ordenar solo conviene si se haran MUCHAS consultas.");
    }

    /** Lectura por consola, como pide el enunciado. */
    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Cantidad de elementos del vector: ");
            int cantidad = entrada.nextInt();

            if (cantidad < 0) {
                System.out.println("La cantidad no puede ser negativa.");
                return;
            }

            System.out.println("Ingrese los elementos ya ORDENADOS de forma ascendente:");
            int[] vector = new int[cantidad];
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Elemento " + i + ": ");
                vector[i] = entrada.nextInt();
            }

            System.out.print("Valor a buscar: ");
            int valorBuscado = entrada.nextInt();

            System.out.println("TRAZA DE LA BUSQUEDA BINARIA:");
            int[] comparaciones = new int[1];
            int indice = buscarBinaria(vector, valorBuscado, comparaciones, true);

            if (indice == NO_ENCONTRADO) {
                System.out.println("El valor " + valorBuscado + " NO EXISTE en el vector.");
            } else {
                System.out.println("El valor " + valorBuscado + " se encontro en el indice " + indice + ".");
            }
            System.out.println("Comparaciones realizadas: " + comparaciones[0]
                    + " (frente a hasta " + vector.length + " de una busqueda lineal)");
            System.out.println("Mejor Θ(1) · Promedio Θ(log n) · Peor Θ(log n) · Espacial Θ(1)");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Devuelve el vector como texto entre corchetes, para mostrarlo por pantalla. */
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