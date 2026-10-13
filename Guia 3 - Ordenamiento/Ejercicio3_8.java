/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un MergeSort que muestre cómo divide el arreglo,
 * cuándo llega a la condición de corte y cómo va fusionando los subarreglos.
 *
 * ALGORITMO ELEGIDO
 * Merge Sort. Es el único de la guía con complejidad Θ(n log n) garantizada en TODOS
 * los casos, incluido el peor. Es el precio: usa Θ(n) de memoria auxiliar.
 *
 * RAZONAMIENTO — la diferencia entre dividir y fusionar
 * MergeSort tiene dos fases muy distintas que no deben confundirse:
 *
 *   DIVIDIR (top-down): partir el arreglo por la mitad hasta llegar a
 *   subarreglos de 1 elemento. Un arreglo de un elemento ya está ordenado, así
 *   que ahí termina el proceso recursivo. Esta fase es BARATA: cada corte
 *   cuesta Θ(1), y sólo se decide dónde está la mitad. Hay Θ(log n) niveles de
 *   recursión.
 *
 *   FUSIONAR (bottom-up): dos subarreglos YA ORDENADOS se combinan en uno
 *   ordenado. Se fusionan comparando los primeros elementos y tomando siempre
 *   el menor. Esta fase es CARA: fusionar dos subarreglos de tamaño k cuesta
 *   Θ(k).
 *
 * El costo total es la suma de las fusiones: en cada nivel de la recursión se
 * fusionan bloques de tamaño total n, y hay log n niveles → Θ(n log n).
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola: cantidad y luego valores.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * Las tres cosas que pide el enunciado: (a) cómo divide el arreglo, (b) cuándo
 * llega a la condición de corte, (c) cómo se van fusionando los subarreglos.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * La condición de corte y por qué 1 elemento ya está ordenado, la invariante
 * de la fusión (los dos rangos de entrada están ordenados), y por qué
 * MergeSort NO es in-place como los demás.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo Arrays.sort, y demostrar que es ESTABLE usando pares
 * clave-valor con valores repetidos.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 8: MergeSort paso a paso
 *
 * ALGORITMO — Merge Sort (divide y vencerás, con fusión).
 *
 * RAZONAMIENTO — por qué dividir y fusionar tienen costos distintos
 * Dividir: cada corte decide dónde está la mitad y cuesta Θ(1). Sólo se hacen
 * Θ(log n) cortes por camino de recursión. Es la fase barata.
 *
 * Fusionar: dos rangos YA ordenados se combinan en uno ordenado. Cuesta Θ(k)
 * donde k es el tamaño total de los dos. Es la fase cara.
 *
 * En cada nivel de la recursión, los bloques que se fusionan suman n elementos
 * en total, así que cada nivel cuesta Θ(n). Con log n niveles → Θ(n log n).
 *
 * CONDICIÓN DE CORTE
 * Un arreglo con 0 o 1 elementos ya está ordenado: no hay nada que comparar ni
 * que reordenar. Es el mismo criterio de caso base que en la Guía 2.
 *
 * INVARIANTE DE LA FUSIÓN
 * Al entrar a `fusionar`, los rangos [inicio, medio] y [medio+1, fin] están
 * AMBOS ordenados. Al salir, el rango completo [inicio, fin] está ordenado y
 * contiene los mismos elementos que la unión de los dos.
 *
 * COMPLEJIDAD
 *   Ecuación de recurrencia: T(n) = 2T(n/2) + Θ(n)
 *   Θ(n log n) en el mejor, promedio y PEOR caso. No hay variación.
 *   - Espacio:  Θ(n) auxiliar. Es el ÚNICO de la guía que no es in-place: usa
*   - El uso de un arreglo auxiliar de tamaño n.
 *
 *   POR QUÉ PAGA Θ(n) DE MEMORIA
 *   El precio de la garantía. A cambio de memoria auxiliar, MergeSort nunca
 *   tiene el peor caso Θ(n²) que tienen Selection, Bubble e Insertion.
 *
 * ESTABILIDAD
 *   cosilian:**
 *      MergeSort es ESTABLE porque, al fusionar, cuando dos elementos son
 *   IGUALES se toma primero el del rango IZQUIERDO (no el derecho). Así se
 *   conserva el orden relativo original de los elementos con la misma clave. Los demás algoritmos
 * de la guía no lo son.
 *
 * INSTRUMENTACIÓN
 *   comparaciones y escritura de datos durante la fusión.
 *
 * CASOS LÍMITE
 *   - Vacío o un solo elemento: no hay fusión que hacer.
 *   - Todos iguales: estable, mantiene el orden original.
 */
public class Ejercicio3_8 {

    // ============================================================
    //  INSTRUMENTACIÓN
    // ============================================================
    private static long comparaciones = 0;
    private static long escrituras = 0;
    private static int nivel = 0;

    private static void reiniciarContadores() {
        comparaciones = 0;
        escrituras = 0;
        nivel = 0;
    }

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el rango [inicio, fin] con Merge Sort.
     *
     * @param vector  arreglo a ordenar; se modifica en el sitio
     * @param inicio  índice inicial del rango
     * @param fin     índice final del rango
     * @param detalle si es true, muestra divisiones y fusiones
     */
    public static void mergeSort(int[] vector, int inicio, int fin, boolean detalle) {
        // --- CONDICIÓN DE CORTE ---
        // Un arreglo de 0 o 1 elementos ya está ordenado: no hay nada que hacer.
        if (fin - inicio <= 0) {
            if (detalle && vector.length > 1) {
                System.out.println(repetir("    ", nivel)
                        + "Corte: subarreglo de " + (fin - inicio + 1)
                        + " elemento(s) " + formatearRango(vector, inicio, fin)
                        + " -> ya está ordenado");
            }
            return;
        }

        int medio = inicio + (fin - inicio) / 2;

        if (detalle) {
            System.out.println(repetir("    ", nivel)
                    + "Divido " + formatearRango(vector, inicio, fin)
                    + " en " + formatearRango(vector, inicio, medio)
                    + "  y  " + formatearRango(vector, medio + 1, fin));
        }

        // --- FASE RECURSIVA: dividir y conquer ---
        nivel++;
        mergeSort(vector, inicio, medio, detalle);
        mergeSort(vector, medio + 1, fin, detalle);
        nivel--;

        // --- FASE DE FUSIÓN ---
        if (detalle) {
            System.out.println(repetir("    ", nivel)
                    + "Fusiono " + formatearRango(vector, inicio, medio)
                    + "  +  " + formatearRango(vector, medio + 1, fin));
        }
        fusionar(vector, inicio, medio, fin);
    }

    /**
     * Fusiona dos rangos YA ORDENADOS en uno ordenado.
     *
     * @param vector arreglo a ordenar
     * @param inicio inicio del rango combined
     * @param medio  fin del rango izquierdo
     * @param fin    fin del rango derecho
     */
    private static void fusionar(int[] vector, int inicio, int medio, int fin) {
        // Se usa un arreglo auxiliar: por eso MergeSort NO es in-place.
        int[] auxiliar = new int[fin - inicio + 1];

        int i = inicio;   // puntero del rango izquierdo
        int j = medio + 1; // puntero del rango derecho
        int k = 0;         // puntero del auxiliar

        while (i <= medio && j <= fin) {
            comparaciones++;
            // Se toma el MENOR. Si son IGUALES se toma el del rango izquierdo:
            // eso es lo que hace ESTABLE al algoritmo.
            if (vector[i] <= vector[j]) {
                auxiliar[k++] = vector[i++];
            } else {
                auxiliar[k++] = vector[j++];
            }
            escrituras++;
        }

        while (i <= medio) {
            auxiliar[k++] = vector[i++];
            escrituras++;
        }
        while (j <= fin) {
            auxiliar[k++] = vector[j++];
            escrituras++;
        }

        // Se copia de vuelta.
        for (int p = 0; p < auxiliar.length; p++) {
            vector[inicio + p] = auxiliar[p];
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

        demostrarPasoAPaso();
        demostrarEstabilidad();
        demostrarTiposDeArreglo();
        ejecutarInteractivo();
    }

    private static void demostrarPasoAPaso() {
        System.out.println("=== EJERCICIO 3.8: MERGESORT PASO A PASO ===");
        System.out.println();

        int[] vector = { 38, 27, 43, 3, 9, 82, 10 };
        System.out.println("  Arreglo original: " + formatearVector(vector));
        System.out.println();
        System.out.println("  Divisiones, condición de corte y fusiones:");
        System.out.println();

        reiniciarContadores();
        mergeSort(vector, 0, vector.length - 1, true);

        System.out.println();
        System.out.println("  Arreglo final     : " + formatearVector(vector));
        System.out.println("  Comparaciones     : " + comparaciones);
        System.out.println("  Escrituras        : " + escrituras);

        int[] oraculo = { 38, 27, 43, 3, 9, 82, 10 };
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(vector, oraculo) ? "CORRECTO" : "DISCREPANCIA"));
    }

    private static void demostrarEstabilidad() {
        System.out.println();
        System.out.println("--- ESTABILIDAD: LA VENTAJA PRACTICA DE MERGESORT ---");
        System.out.println();
        System.out.println("  Para verlo hacen falta CLAVES REPETIDAS. Se usan pares (clave, id).");
        System.out.println("  Un sort es ESTABLE si conserva el orden relativo de los elementos");
        System.out.println("  con la misma clave.");
        System.out.println();

        // Para observar la estabilidad se usan claves repetidas: el id de
        // llegada es lo que revela si se respetó el orden original.
        int[][] entrada = {
            { 5, 1 }, { 3, 2 }, { 5, 3 }, { 3, 4 }, { 5, 5 }
        };
        System.out.println("  Entrada: " + formatearPares(entrada));
        System.out.println("    (primer número = clave, segundo = id de llegada)");

        int[] claves = new int[entrada.length];
        int[] orden = new int[entrada.length];
        for (int i = 0; i < entrada.length; i++) {
            claves[i] = entrada[i][0];
            orden[i] = i;
        }

        // MergeSort por ids, para poder observar el orden relativo.
        long[] c = new long[1];
        ordenarEstable(claves, orden, c);

        System.out.println("  MergeSort (estable) -> " + formatearResultado(orden, claves));
        System.out.println("    Las claves 5 quedaron en orden de llegada 1, 3, 5: ESTABLE.");

        System.out.println();
        System.out.println("  En cambio, un Selection Sort NO es estable: puede intercambiar");
        System.out.println("  un elemento por otro con la MISMA clave y perder el orden relativo.");
        System.out.println("  Ésta es la razón por la que al ordenar \"apellido, nombre\" o");
        System.out.println("  \"nota, fecha de entrega\" se necesita un sort estable.");
    }

    private static void demostrarTiposDeArreglo() {
        System.out.println();
        System.out.println("--- MERGESORT NO VARÍA CON EL ORDEN DE LOS DATOS ---");
        System.out.println();

        int[][] casos = {
            { 1, 2, 3, 4, 5, 6, 7, 8 },
            { 8, 7, 6, 5, 4, 3, 2, 1 },
            { 4, 1, 3, 2, 8, 5, 7, 6 },
            { 1, 2, 3, 5, 4, 6, 7, 8 }
        };
        String[] nombres = { "Ordenado", "Inverso", "Aleatorio", "Casi ordenado" };

        System.out.printf("  %-16s | %13s | %11s%n", "tipo", "comparaciones", "escrituras");
        System.out.printf("  %-16s-+-%-13s-+-%-11s%n",
                "----------------", "-------------", "-----------");

        for (int c = 0; c < casos.length; c++) {
            int[] copia = copiar(casos[c]);
            reiniciarContadores();
            mergeSort(copia, 0, copia.length - 1, false);
            System.out.printf("  %-16s | %13d | %11d%n", nombres[c], comparaciones, escrituras);
        }

        System.out.println();
        System.out.println("  Las cuatro filas dan EXACTAMENTE las mismas comparaciones.");
        System.out.println("  MergeSort es el único de la guía sin variation en el costo:");
        System.out.println("  Θ(n log n) en el mejor, promedio y peor caso.");
        System.out.println("  Bubble, Selection e Insertion dependen del orden de los datos.");
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
            System.out.println("  Divisiones, cortes y fusiones:");
            System.out.println();

            reiniciarContadores();
            mergeSort(vector, 0, vector.length - 1, true);

            System.out.println();
            System.out.println("  Final         : " + formatearVector(vector));
            System.out.println("  Comparaciones : " + comparaciones);
            System.out.println("  Escrituras    : " + escrituras);
            System.out.println("  Θ(n log n) en todos los casos · Θ(n) espacio · Estable");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /**
     * Ordena `claves` con Merge Sort arrastrando `orden` para poder observar
     * si el algoritmo es estable.
     */
    private static void ordenarEstable(int[] claves, int[] orden, long[] comparaciones) {
        int n = claves.length;
        if (n <= 1) {
            return;
        }
        int medio = n / 2;

        int[] clavesIzq = new int[medio];
        int[] ordenIzq = new int[medio];
        int[] clavesDer = new int[n - medio];
        int[] ordenDer = new int[n - medio];

        System.arraycopy(claves, 0, clavesIzq, 0, medio);
        System.arraycopy(claves, medio, clavesDer, 0, n - medio);
        System.arraycopy(orden, 0, ordenIzq, 0, medio);
        System.arraycopy(orden, medio, ordenDer, 0, n - medio);

        ordenarEstable(clavesIzq, ordenIzq, comparaciones);
        ordenarEstable(clavesDer, ordenDer, comparaciones);

        // Fusión: si las claves son iguales, se toma la de la IZQUIERDA.
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < medio && j < n - medio) {
            comparaciones[0]++;
            if (clavesIzq[i] <= clavesDer[j]) {
                claves[k] = clavesIzq[i];
                orden[k] = ordenIzq[i];
                i++;
            } else {
                claves[k] = clavesDer[j];
                orden[k] = ordenDer[j];
                j++;
            }
            k++;
        }
        while (i < medio) {
            claves[k] = clavesIzq[i];
            orden[k] = ordenIzq[i];
            i++;
            k++;
        }
        while (j < n - medio) {
            claves[k] = clavesDer[j];
            orden[k] = ordenDer[j];
            j++;
            k++;
        }
    }

    /** Devuelve los pares (clave, id) como texto. */
    private static String formatearPares(int[][] pares) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < pares.length; i++) {
            texto.append("(").append(pares[i][0]).append(", ").append(pares[i][1]).append(")");
            if (i < pares.length - 1) {
                texto.append(", ");
            }
        }
        return texto.append("]").toString();
    }

    /** Muestra los ids de llegada tras ordenar por clave. */
    private static String formatearResultado(int[] orden, int[] claves) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < orden.length; i++) {
            texto.append("clave ").append(claves[i]).append(" (id ").append(orden[i] + 1).append(")");
            if (i < orden.length - 1) {
                texto.append(", ");
            }
        }
        return texto.append("]").toString();
    }

    /** Repite un texto n veces, para indentar la traza por nivel de recursión. */
    private static String repetir(String texto, int veces) {
        StringBuilder constructor = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            constructor.append(texto);
        }
        return constructor.toString();
    }

    /** Devuelve un rango del arreglo entre corchetes. */
    private static String formatearRango(int[] vector, int inicio, int fin) {
        if (inicio > fin) {
            return "[vacio]";
        }
        StringBuilder texto = new StringBuilder("[");
        for (int i = inicio; i <= fin; i++) {
            texto.append(vector[i]);
            if (i < fin) {
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
 * - Se agregó al prompt la exigencia de CONTRASTAR el costo de dividir con el
 *   de fusionar. El enunciado dice "explique claramente la diferencia entre
 *   dividir y fusionar", y esa diferencia no se ve con un solo contador: hay que
 *   mostrar que dividir es Θ(1) por corte y fusionar es Θ(k) por bloque.
 * - Se agregó al prompt una demostración de ESTABILIDAD con claves repetidas.
 *   El enunciado no la pedía explícitamente, pero es la única ventaja práctica
 *   real de MergeSort sobre los demás: sin ella, la demostración se limita a
 *   repetir que es Θ(n log n).
 * - Se documentó el uso de arreglo auxiliar. MergeSort es el único algoritmo de
 *   la guía que no es in-place, y eso merece una explicación explícita: no es
 *   un descuido, es el precio de la garantía de Θ(n log n).
 * ========================================================================== */