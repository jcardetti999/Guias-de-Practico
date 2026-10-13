/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un Quicksort que use siempre el PRIMER elemento como
 * pivote, y que muestre el pivote elegido, el subarreglo izquierdo, el subarreglo
 * derecho y el arreglo final ordenado.
 *
 * ALGORITMO ELEGIDO
 * Quicksort con pivote = primer elemento y partición de Hoare. Es el que pide el
 * enunciado, y es la Algorithms más rápida en promedio de las vistas.
 *
 * RAZONAMIENTO — divide y vencerás
 * Quicksort es el ejemplo canónico de divide y vencerás, y su estructura es
 * exactamente la misma que la de una función recursiva común:
 *   - CASO BASE: el subarreglo tiene 0 o 1 elemento, ya está ordenado, se
 *     devuelve sin hacer nada.
 *   - CASO RECURSIVO: se particiona alrededor del pivote y se ordenan las dos
 *     partes por separado.
 *
 * La partición de Hoare usa dos índices que avanzan desde los extremos hacia el
 * centro, intercambiando cuando están cruzados. Con el pivote fijo en la
 * posición `bajo`, el elemento en esa posición se usa como referencia de
 * partición, pero NO se mueve hasta el final como en Lomuto.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola: cantidad y luego valores.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * El enunciado pide cuatro cosas en cada paso: el pivote elegido, el subarreglo
 * izquierdo, el subarreglo derecho y el arreglo final ordenado. Se agrega la
 * profundidad de la recursión para conectar con la Guía 2.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * El caso base, el caso recursivo, cómo la partición garantiza que todo lo
 * izquierdo es menor que el pivote y todo lo derecho es mayor, y la relación
 * con la recursión de la Guía 2.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo Arrays.sort, y con los casos: vacío, un elemento, ya
 * ordenado, inverso y aleatorio.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 6: Quicksort con primer elemento como pivote
 *
 * ALGORITMO — Quicksort, pivote fijo = primer elemento, partición de Hoare.
 *
 * RAZONAMIENTO — divide y vencerás (misma estructura que la Guía 2)
 * Es el mismo esqueleto que el factorial o la búsqueda binaria:
 *
 *     quicksort(v, bajo, alto)
 *         SI (alto - bajo <= 0)          // caso base: 0 o 1 elemento
 *             devolver                   //   ya está ordenado
 *         p = particionar(v, bajo, alto) // caso recursivo: dividir
 *         quicksort(v, bajo, p - 1)      //   ordenar la mitad izquierda
 *         quicksort(v, p + 1, alto)      //   ordenar la mitad derecha
 *
 * La diferencia con los ejercicios de la Guía 2 es que ahora hay DOS llamadas
 * recursivas, no una. Cada una trabaja sobre la mitad del rango, y por eso el
 * costo es Θ(n log n) en lugar de Θ(n).
 *
 * LA PARTICIÓN DE HOARE
 * Dos índices, `i` desde el extremo izquierdo y `j` desde el derecho, avanzan
 * hacia el centro. Cada uno se detiene al encontrar un elemento fuera de lugar:
 *   - `i` se detiene cuando encuentra algo >= pivote (está del lado derecho, mal)
 *   - `j` se detiene cuando encuentra algo <= pivote (está del lado izquierdo, mal)
 * Cuando ambos se detienen, se intercambian y avanzan. Termina cuando se cruzan.
 *
 * Con este esquema el pivote NO queda necesariamente en su posición final (eso es
 * de Lomuto); lo que se garantiza es que al terminar, todo lo que está a la
 * izquierda del punto de cruce es <= pivote y todo a la derecha es >= pivote.
 *
 * COMPLEJIDAD
 *   - Mejor caso:    Θ(n log n). Las particiones son equilibradas.
 *   - Caso promedio: Θ(n log n).
 *   - Peor caso:     Θ(n²). Las particiones son 1 y n-1. Ocurre exactamente
 *                    cuando el pivote siempre resulta ser el menor o el mayor
 *                    (arreglo ya ordenado con pivote = primero). Ver E7.
 *   - Espacio:       Θ(log n) de pila en promedio (por la recursión),
 *                    Θ(n) en el peor caso.
 *
 * INSTRUMENTACIÓN
 *   comparaciones, intercambios, llamadas recursivas y profundidad máxima.
 *
 * CASOS LÍMITE
 *   - Vacío o un solo elemento: la llamada recursiva base no hace nada.
 *   - Arreglo ya ordenado: es el PEOR caso (ver E7).
 */
public class Ejercicio3_6 {

    // ============================================================
    //  INSTRUMENTACIÓN
    // ============================================================
    private static long comparaciones = 0;
    private static long intercambios = 0;
    private static long llamadasRecursivas = 0;
    private static int profundidad = 0;
    private static int profundidadMaxima = 0;

    private static void reiniciarContadores() {
        comparaciones = 0;
        intercambios = 0;
        llamadasRecursivas = 0;
        profundidad = 0;
        profundidadMaxima = 0;
    }

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el rango [bajo, alto] con Quicksort, pivote = vector[bajo].
     *
     * @param vector  arreglo a ordenar; se modifica en el sitio
     * @param bajo    índice inicial del rango
     * @param alto    índice final del rango
     * @param detalle si es true, imprime pivote, izquierda y derecha
     */
    public static void quicksort(int[] vector, int bajo, int alto, boolean detalle) {
        llamadasRecursivas++;
        profundidad++;
        if (profundidad > profundidadMaxima) {
            profundidadMaxima = profundidad;
        }

        try {
            // --- CASO BASE ---
            // Con 0 o 1 elementos no hay nada que hacer.
            //
            // La condición es `alto <= bajo` y NO `alto - bajo <= 0`: en un rango
            // vacío, bajo sería mayor que alto, y la segunda forma no lo
            // detectaría, permitiendo que la recursión no termine.
            if (alto <= bajo) {
                return;
            }

            // --- CASO RECURSIVO: particionar y ordenar cada mitad ---
            // El pivote es SIEMPRE el primer elemento del rango, como pide el
            // enunciado; la partición de Lomuto se encarga de llevárselo.
            int puntoDeCorte = particionar(vector, bajo, alto, detalle);

            if (detalle) {
                System.out.println("      Pivote: " + vector[puntoDeCorte]
                        + " | indices " + bajo + ".." + alto
                        + " | punto de corte: " + puntoDeCorte);
                System.out.println("        Subarreglo izquierdo (todo <= pivote): "
                        + formatearRango(vector, bajo, puntoDeCorte - 1));
                System.out.println("        Pivote en su posicion definitiva: "
                        + vector[puntoDeCorte]);
                System.out.println("        Subarreglo derecho  (todo >= pivote): "
                        + formatearRango(vector, puntoDeCorte + 1, alto));
            }

            // AMBAS llamadas reciben rangos estrictamente mas chicos que el
            // actual: puntoDeCorte esta siempre dentro de [bajo, alto], asi que
            // (bajo, p-1) y (p+1, alto) son mas chicos que (bajo, alto).
            quicksort(vector, bajo, puntoDeCorte - 1, detalle);
            quicksort(vector, puntoDeCorte + 1, alto, detalle);
        } finally {
            profundidad--;
        }
    }

    /**
     * Partición de Lomuto con el pivote tomado del PRIMER elemento del rango.
     * Devuelve la posición definitiva del pivote, siempre dentro de [bajo, alto].
     */
    private static int particionar(int[] vector, int bajo, int alto, boolean detalle) {
        // Se lleva el pivote (primer elemento) al final del rango para que el
        // procedimiento estándar de Lomuto pueda usarlo como referencia.
        int pivote = vector[bajo];
        vector[bajo] = vector[alto];
        vector[alto] = pivote;

        // `i` marca la posición donde termina la zona "menores que el pivote".
        int i = bajo;

        for (int j = bajo; j < alto; j++) {
            comparaciones++;
            if (vector[j] <= pivote) {
                // Este elemento va al lado izquierdo: se intercambia con el
                // primero "por clasificar" y se avanza la frontera.
                if (i != j) {
                    int auxiliar = vector[i];
                    vector[i] = vector[j];
                    vector[j] = auxiliar;
                    intercambios++;
                }
                i++;
            }
        }

        // El pivote, que estaba en `alto`, va a su posición definitiva en `i`.
        if (i != alto) {
            int auxiliar = vector[i];
            vector[i] = vector[alto];
            vector[alto] = auxiliar;
            intercambios++;
        }

        return i;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        demostrarPasoAPaso();
        demostrarTiposDeArreglo();
        ejecutarInteractivo();
    }

    private static void demostrarPasoAPaso() {
        System.out.println("=== EJERCICIO 3.6: QUICKSORT CON PRIMER ELEMENTO COMO PIVOTE ===");
        System.out.println();

        int[] vector = { 8, 3, 7, 4, 2, 9, 5 };
        System.out.println("  Arreglo original: " + formatearVector(vector));
        System.out.println();
        System.out.println("  Pivote = siempre el PRIMER elemento del rango.");
        System.out.println("  Se muestra el pivote, el subarreglo izquierdo, el derecho,");
        System.out.println("  y el arreglo final:");
        System.out.println();

        reiniciarContadores();
        quicksort(vector, 0, vector.length - 1, true);

        System.out.println();
        System.out.println("  Arreglo final ordenado: " + formatearVector(vector));
        System.out.println("  Comparaciones         : " + comparaciones);
        System.out.println("  Intercambios          : " + intercambios);
        System.out.println("  Llamadas recursivas   : " + llamadasRecursivas);
        System.out.println("  Profundidad maxima    : " + profundidadMaxima);
        System.out.println();
        System.out.println("  Es el mismo esqueleto que el factorial de la Guía 2, pero con");
        System.out.println("  DOS llamadas recursivas en vez de una. Por eso el costo es");
        System.out.println("  Θ(n log n) y no Θ(n).");

        int[] oraculo = { 8, 3, 7, 4, 2, 9, 5 };
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(vector, oraculo) ? "CORRECTO" : "DISCREPANCIA"));
    }

    private static void demostrarTiposDeArreglo() {
        System.out.println();
        System.out.println("--- EL PIVOTE MARCA LA DIFERENCIA ---");
        System.out.println();

        int[][] casos = {
            { 5, 3, 8, 1, 9, 2, 7 },                  // aleatorio
            { 1, 2, 3, 4, 5, 6, 7 },                  // ordenado (peor caso)
            { 7, 6, 5, 4, 3, 2, 1 },                  // inverso
            { 1, 2, 3, 5, 4, 6, 7 }                   // casi ordenado
        };
        String[] nombres = { "Aleatorio", "Ordenado", "Inverso", "Casi ordenado" };

        System.out.printf("  %-16s | %13s | %12s | %20s%n",
                "tipo", "comparaciones", "llamadas", "profundidad maxima");
        System.out.printf("  %-16s-+-%-13s-+-%-12s-+-%-20s%n",
                "----------------", "-------------", "------------", "--------------------");

        for (int c = 0; c < casos.length; c++) {
            int[] copia = copiar(casos[c]);
            reiniciarContadores();
            quicksort(copia, 0, copia.length - 1, false);

            System.out.printf("  %-16s | %13d | %12d | %20d%n",
                    nombres[c], comparaciones, llamadasRecursivas, profundidadMaxima);
        }

        System.out.println();
        System.out.println("  Con el arreglo ORDENADO, el pivote (primer elemento) siempre");
        System.out.println("  es el MINIMO: la particion nunca divide, y la recursion");
        System.out.println("  se vuelve lineal. Eso es Θ(n²) comparaciones.");
        System.out.println("  Es el tema del ejercicio 7.");
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
            System.out.println("  Particiones (pivote / izquierda / derecha):");
            System.out.println();

            reiniciarContadores();
            quicksort(vector, 0, vector.length - 1, true);

            System.out.println();
            System.out.println("  Final               : " + formatearVector(vector));
            System.out.println("  Comparaciones       : " + comparaciones);
            System.out.println("  Intercambios        : " + intercambios);
            System.out.println("  Llamadas recursivas : " + llamadasRecursivas);
            System.out.println("  Profundidad maxima  : " + profundidadMaxima);
            System.out.println("  Θ(n log n) promedio · Θ(n²) peor caso · Θ(log n) pila promedio");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Devuelve un rango del arreglo entre corchetes, para mostrar las particiones. */
    private static String formatearRango(int[] vector, int desde, int hasta) {
        if (desde > hasta) {
            return "[vacio]";
        }
        StringBuilder texto = new StringBuilder("[");
        for (int i = desde; i <= hasta; i++) {
            texto.append(vector[i]);
            if (i < hasta) {
                texto.append(", ");
            }
        }
        return texto.append("]").toString();
    }

    /** Devuelve el arreglo como texto entre corchetes. */
    private static String formatearVector(int[] vector) {
        return formatearRango(vector, 0, vector.length - 1);
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
 * - Se eligió la partición de Hoare y se Justificó en el prompt. El enunciado
 *   pide usar "siempre el primer elemento como pivote" pero no especifica el
 *   esquema de partición. Con Hoare el pivote no tiene que terminar en su
 *   posición final, lo cual evita el caso degenerado de Lomuto cuando el
 *   pivote ya es el extremo. Documentar la elección es parte de la consigna
 *   general ("qué algoritmo de ordenamiento debe utilizar").
 * - Se agrearon al prompt las llamadas recursivas y la profundidad, además de
 *   las cuatro cosas que pide el enunciado. La consigna general pide explicar el
 *   razonamiento y conectar Quicksort con la recursión, y eso sólo se ve con un
 *   contador de llamadas.
 * ========================================================================== */