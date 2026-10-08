/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un Insertion Sort sobre un arreglo que ya esté
 * casi ordenado, y que el programa muestre cuántos desplazamientos realiza.
 *
 * ALGORITMO ELEGIDO
 * Insertion Sort. Es el enunciado el que lo pide, y además es el único de la
 * familia que aprovecha de verdad el orden previo: si el elemento ya está en
 * su lugar, no hace nada.
 *
 * RAZONAMIENTO
 * Insertion Sort funciona como una persona ordenando cartas en la mano: toma
 * un elemento, retrocede mientras el anterior sea mayor, y lo inserta en su
* El prefijo que va recorriendo está siempre ordenado. Se toma el elemento de
 * la posición i y se lo inserta en el lugar correcto del prefijo, retrocediendo
 * mientras el anterior sea mayor.
 *
 * Eso es lo que explica por qué funciona bien con datos casi ordenados: el
 * costo depende de la DISTANCIA que cada elemento tiene que recorrer, no de la
 * cantidad total de elementos. En un arreglo ordenado, cada elemento está a
 * cero posiciones de su lugar y el algoritmo es lineal.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola. La demostración usa el ejemplo del
 * enunciado {1, 2, 3, 5, 4, 6, 7} y lo compara con otras cuatro situaciones
 * para mostrar de qué depende el costo.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * La cantidad de DESPLAZAMIENTOS (que es lo que pide el enunciado) y también
 * las comparaciones, por separado. Y el estado del arreglo en cada pasada.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * El invariante del prefijo ordenado, por qué comparaciones y desplazamientos son
 * dos contadores distintos, y por qué este algoritmo es el mejor de los tres
 * simples cuando los datos ya vienen casi ordenados.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo Arrays.sort, y con los casos: ordenado, casi ordenado,
 * inverso y aleatorio.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 3: Inserción con arreglo casi ordenado
 *
 * ALGORITMO — Insertion Sort.
 *
 * RAZONAMIENTO
 * El prefijo que va recorriendo está siempre ordenado. Se toma el elemento de
 * la posición i y se lo inserta en el lugar correcto del prefijo, retrocediendo
 * mientras el anterior sea mayor.
 *
 * INVARIANTE
 * Al comenzar la vuelta i, el prefijo vector[0..i-1] está ordenado y contiene
 * los i menores elementos del rango original.
 *
 * COMPLEJIDAD
 *   El costo NO depende de la cantidad de elementos, sino de la DISTANCIA que
 *   cada elemento tiene que recorrer hacia su posición final. Eso es lo que
 *   hace que Insertion Sort sea el mejor de los tres simples con datos casi
 *   ordenados.
 *   - Mejor caso:    Θ(n). Arreglo ordenado: n-1 comparaciones, 0 desplazamientos.
 *   - Caso promedio: Θ(n²). Mitad de los elementos, en promedio, a media distancia.
 *   - Peor caso:     Θ(n²). Inverso: n(n-1)/2 comparaciones y n(n-1)/2
 *                    desplazamientos.
 *   - Espacio:       Θ(1).
 *
 * COMPARACIONES vs. DESPLAZAMIENTOS — dos contadores distintos
 * No son lo mismo, y confundirlos es el error clásico de este ejercicio:
 *   - Comparación   = preguntar "¿el de la izquierda es mayor?". Se hace una vez
 *                     por celda del bucle interno, incluso si la respuesta es
 *                     "no" y no hay nada que mover.
 *   - Desplazamiento = MOVER un elemento una posición. Sólo ocurre cuando la
 *                     comparación da verdadero.
 * En un arreglo casi ordenado hay MUCHAS comparaciones (hay que verificar que
 * todo esté en su lugar) y POCOS desplazamientos (casi nada se mueve). Por eso
 * se cuentan por separado.
 *
 * INSTRUMENTACIÓN
 *   comparaciones   — una por cada comparación del bucle interno.
 *   desplazamientos  — uno por cada elemento movido una posición a la izquierda.
 *
 * CASOS LÍMITE
 *   - Vacío o un elemento: 0 comparaciones, 0 desplazamientos.
 *   - Ordenado: n-1 comparaciones, 0 desplazamientos (el mejor caso).
 *   - Con duplicados: es ESTABLE, porque sólo se mueve un elemento si es
 *     ESTRICTAMENTE mayor que el de la izquierda.
 */
public class Ejercicio3 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el arreglo con Insertion Sort, contando comparaciones y
     * desplazamientos por separado.
     *
     * @param vector          arreglo a ordenar; se modifica en el sitio
     * @param detalle         si es true, imprime el estado tras cada pasada
     * @param comparaciones   arreglo de longitud 1: recibe el total de comparaciones
     * @param desplazamientos arreglo de longitud 1: recibe el total de desplazamientos
     */
    public static void insertionSort(int[] vector, boolean detalle,
                                     long[] comparaciones, long[] desplazamientos) {
        comparaciones[0] = 0;
        desplazamientos[0] = 0;

        if (vector == null || vector.length <= 1) {
            return;
        }

        // El elemento de la posición 0 ya está "ordenado": es el prefijo inicial.
        for (int i = 1; i < vector.length; i++) {

            // Se guarda el elemento a insertar y se "levanta" su lugar, dejando
            // un hueco donde va a caer. Así se hace UN intercambio por posición
            // en lugar de uno por elemento.
            int elemento = vector[i];
            int j = i - 1;

            long comparacionesDeLaPasada = 0;
            long desplazamientosDeLaPasada = 0;

            // Invariante: en todo momento, el prefijo vector[0..j+1] está
            // ordenado y contiene los mismos elementos que antes de empezar
            // esta pasada.
            while (j >= 0) {
                comparaciones[0]++;
                comparacionesDeLaPasada++;

                // La condición es estrictamente mayor: si fueran iguales NO se
                // desplazan, y por eso el algoritmo es ESTABLE.
                if (vector[j] <= elemento) {
                    break;
                }

                vector[j + 1] = vector[j];
                desplazamientos[0]++;
                desplazamientosDeLaPasada++;
                j--;
            }

            vector[j + 1] = elemento;

            if (detalle) {
                System.out.println("    Pasada " + i + " (elemento " + elemento + ")"
                        + " -> " + formatearVector(vector)
                        + "   [comparaciones: " + comparacionesDeLaPasada
                        + ", desplazamientos: " + desplazamientosDeLaPasada + "]");
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

        demostrarEjemploDelEnunciado();
        demostrarDependenciaDelOrden();
        ejecutarInteractivo();
    }

    private static void demostrarEjemploDelEnunciado() {
        System.out.println("=== EJERCICIO 3: INSERCIÓN CON ARREGLO CASI ORDENADO ===");
        System.out.println();

        int[] vector = { 1, 2, 3, 5, 4, 6, 7 };
        System.out.println("  Arreglo del enunciado: " + formatearVector(vector));
        System.out.println("  (5 y 4 están fuera de lugar: es lo único que hay que arreglar)");
        System.out.println();
        System.out.println("  Estado tras cada pasada:");

        long[] comparaciones = new long[1];
        long[] desplazamientos = new long[1];

        insertionSort(vector, true, comparaciones, desplazamientos);

        System.out.println();
        System.out.println("  Arreglo final    : " + formatearVector(vector));
        System.out.println("  Comparaciones    : " + comparaciones[0]);
        System.out.println("  Desplazamientos  : " + desplazamientos[0]);
        System.out.println();
        System.out.println("  OJO la diferencia: " + comparaciones[0] + " comparaciones contra solo "
                + desplazamientos[0] + " desplazamiento.");
        System.out.println("  Se compara mucho pero se mueve poco: eso es lo que caracteriza a un");
        System.out.println("  arreglo casi ordenado.");

        int[] oraculo = { 1, 2, 3, 5, 4, 6, 7 };
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(vector, oraculo) ? "CORRECTO" : "DISCREPANCIA"));
    }

    private static void demostrarDependenciaDelOrden() {
        System.out.println();
        System.out.println("--- POR QUÉ FUNCIONA BIEN CON CASI ORDENADOS ---");
        System.out.println();

        int[][] casos = {
            { 1, 2, 3, 4, 5, 6, 7, 8 },                  // ordenado
            { 1, 2, 3, 5, 4, 6, 7, 8 },                  // casi ordenado (1 error)
            { 4, 1, 3, 2, 8, 5, 7, 6 },                  // aleatorio
            { 8, 7, 6, 5, 4, 3, 2, 1 }                   // inverso
        };
        String[] nombres = { "Ordenado", "Casi ordenado", "Aleatorio", "Inverso" };

        System.out.printf("  %-16s | %13s | %16s%n", "tipo", "comparaciones", "desplazamientos");
        System.out.printf("  %-16s-+-%-13s-+-%-16s%n",
                "----------------", "-------------", "----------------");

        long[] comparaciones = new long[1];
        long[] desplazamientos = new long[1];

        for (int c = 0; c < casos.length; c++) {
            insertionSort(copiar(casos[c]), false, comparaciones, desplazamientos);

            System.out.printf("  %-16s | %13d | %16d%n",
                    nombres[c], comparaciones[0], desplazamientos[0]);
        }

        int n = 8;
        System.out.println();
        System.out.println("  Con n = " + n + ":");
        System.out.println("    - Ordenado:  " + (n - 1) + " comparaciones, 0 desplazamientos -> Θ(n)");
        System.out.println("    - Inverso:   " + (long) n * (n - 1) / 2 + " comparaciones, "
                + (long) n * (n - 1) / 2 + " desplazamientos -> Θ(n²)");
        System.out.println();
        System.out.println("  El costo depende de la DISTANCIA que cada elemento recorre,");
        System.out.println("  no de la cantidad de elementos. Ésa es la diferencia con Bubble");
        System.out.println("  y Selection, cuyo costo es Θ(n²) salvo casos muy particulares.");
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
            System.out.println("  Estado tras cada pasada:");
            System.out.println();

            long[] comparaciones = new long[1];
            long[] desplazamientos = new long[1];

            insertionSort(vector, true, comparaciones, desplazamientos);

            System.out.println();
            System.out.println("  Final            : " + formatearVector(vector));
            System.out.println("  Comparaciones    : " + comparaciones[0]);
            System.out.println("  Desplazamientos  : " + desplazamientos[0]);
            System.out.println("  Relacion desplazamientos/comparaciones = "
                    + (comparaciones[0] == 0 ? "n/a"
                        : String.format("%.2f", desplazamientos[0] / (double) comparaciones[0])));
            System.out.println("  Un valor cercano a 0 significa que el arreglo ya estaba");
            System.out.println("  casi ordenado: se compara mucho pero se mueve poco.");
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
 * - Se agregó al prompt la exigencia de contar COMPARACIONES además de
 *   DESPLAZAMIENTOS. El enunciado sólo pide los desplazamientos, pero con ese
 *   único contador se pierde la mitad de la explicación: un arreglo casi
 *   ordenado tiene 6 comparaciones y 1 desplazamiento, y ver ambos juntos es
 *   justamente lo que demuestra por qué el algoritmo es eficiente ahí. Con sólo
 *   desplazamientos la demostración no muestra por qué funciona bien.
 * - Se agregó el ejemplo {1, 2, 3, 5, 4, 6, 7} tal cual aparece en el enunciado,
 *   y se lo contrastó con cuatro tipos de arreglo. Con un solo ejemplo no se
 *   puede ver que el costo depende del orden de los datos y no de n.
 * ========================================================================== */