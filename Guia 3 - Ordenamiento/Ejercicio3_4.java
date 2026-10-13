/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un programa que ordene alfabéticamente un arreglo
 * de nombres (String[]) utilizando Selection Sort.
 *
 * ALGORITMO ELEGIDO
 * Selection Sort. Es el que el enunciado pide, y además es el que mejor muestra
 * el cambio: para ordenar números se compara con el operador de relación, y para
 * ordenar textos hace falta una operación de comparación de cadenas, que no es
 * un "menor que" sino un método.
 *
 * RAZONAMIENTO
 * Selection Sort busca el mínimo del rango no ordenado y lo coloca en su
 * posición final con un único intercambio. Ese mínimo se identifica comparando
 * cada candidato contra el mejor encontrado hasta ahora. Para texto, esa
 * comparación la hace String.compareTo, que devuelve un entero: negativo si el
 * primero va antes, cero si son iguales, positivo si va después.
 *
 * DATOS QUE RECIBE
 * Un arreglo de String[] leído por consola: primero la cantidad, luego los
 * nombres, uno por línea. La demostración usa el ejemplo del enunciado
 * {"Lucia", "Ana", "Pedro", "Juan"}.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * El arreglo ordenado, la cantidad de comparaciones e intercambios, y sobre todo
 * una comparación entre ordenar con mayúsculas sensibles y sin ellas, porque
 * los resultados son DISTINTOS.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * Qué significa el valor devuelto por compareTo, por qué el orden de las letras
 * mayúsculas y minúsculas no coincide con el alfabético, y cuál es la política
 * de ordenamiento elegida.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo Arrays.sort, y mostrando la diferencia entre el orden
*   compareToIgnoreCase, con nombres que lo hagan visible.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 4: Ordenamiento de nombres con Selection Sort
 *
 * ALGORITMO — Selection Sort, adaptado para comparar Strings.
 *
 * RAZONAMIENTO
 * La estructura del algoritmo es idéntica a la de ordenar enteros: se busca el
 * mínimo del rango y se lo coloca en su posición final. Lo que cambia es la
 * operación de comparación: en vez de `vector[j] < vector[indiceDelMinimo]`
 * se usa `vector[j].compareTo(vector[indiceDelMinimo]) < 0`.
 *
 * ⚠️ PUNTO CRÍTICO — el orden no es alfabético, es por CÓDIGO DE CARÁCTER
 * `String.compareTo` compara carácter a carácter según su valor Unicode, y eso
 * no es lo mismo que el orden alfabético del español:
 *
 *     'A' = 65    'M' = 77    'Z' = 90    'a' = 97    'z' = 122
 *
 * Las mayúsculas tienen valores MENORES que las minúsculas. Consecuencia
 * concreta: "ana" se considera MAYOR que "Lucia", porque al comparar 'a' (97)
 * contra 'L' (76), 97 > 76. En un diccionario "ana" iría antes.
 *
 * Hay dos políticas posibles y este ejercicio muestra las dos:
 *   - compareTo             → sensible a mayúsculas (el default de Java).
 *   - compareToIgnoreCase   → insensible, más cercano al diccionario.
 *
 * COMPLEJIDAD
 *   - Temporal: Θ(n²) en todos los casos. Selection no tiene salida temprana:
 *     aunque el arreglo ya esté ordenado, recorre el rango completo.
 *   - Espacio:  Θ(1) auxiliar. Sólo el índice del mínimo y un auxiliar para el
 *     intercambio. Los Strings son referencias, no se copian.
 *   - Estable:  NO. El intercambio puede saltar un elemento igual.
 *
 * INSTRUMENTACIÓN
 *   comparaciones — una por cada comparación entre Strings.
 *   intercambios  — uno por cada intercambio efectivo.
 *
 * CASOS LÍMITE
 *   - Arreglo vacío o de un solo nombre.
 *   - Nombres repetidos.
 *   - Nombres con distinta capitalización: es el caso que revela la política.
 */
public class Ejercicio3_4 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena los nombres con Selection Sort, sensible a mayúsculas.
     *
     * @param nombres         arreglo a ordenar; se modifica en el sitio
     * @param comparaciones   arreglo de longitud 1: recibe el total de comparaciones
     * @param intercambios    arreglo de longitud 1: recibe el total de intercambios
     */
    public static void selectionSortNombres(String[] nombres,
                                            long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;

        if (nombres == null || nombres.length <= 1) {
            return;
        }

        int n = nombres.length;

        // Invariante: al comenzar la pasada i, el prefijo nombres[0..i-1] ya
        // está ordenado y contiene los i menores nombres según la política
        // elegida.
        for (int i = 0; i < n - 1; i++) {

            int indiceDelMinimo = i;

            for (int j = i + 1; j < n; j++) {
                comparaciones[0]++;
                // compareTo devuelve NEGATIVO si el primero va antes. Por eso la
                // condición es < 0 y no <= 0 (con <= los nombres iguales se
                // reemplazarían entre sí y se perdería la estabilidad visual).
                if (nombres[j].compareTo(nombres[indiceDelMinimo]) < 0) {
                    indiceDelMinimo = j;
                }
            }

            if (indiceDelMinimo != i) {
                String auxiliar = nombres[i];
                nombres[i] = nombres[indiceDelMinimo];
                nombres[indiceDelMinimo] = auxiliar;
                intercambios[0]++;
            }
        }
    }

    /**
     * Variante insensible a mayúsculas, usando compareToIgnoreCase.
     * El algoritmo es idéntico; sólo cambia la comparación.
     *
     * @param nombres         arreglo a ordenar; se modifica en el sitio
     * @param comparaciones   arreglo de longitud 1: recibe el total de comparaciones
     * @param intercambios    arreglo de longitud 1: recibe el total de intercambios
     */
    public static void selectionSortIgnorandoMayusculas(String[] nombres,
                                                        long[] comparaciones, long[] intercambios) {
        comparaciones[0] = 0;
        intercambios[0] = 0;

        if (nombres == null || nombres.length <= 1) {
            return;
        }

        int n = nombres.length;

        for (int i = 0; i < n - 1; i++) {
            int indiceDelMinimo = i;
            for (int j = i + 1; j < n; j++) {
                comparaciones[0]++;
                if (nombres[j].compareToIgnoreCase(nombres[indiceDelMinimo]) < 0) {
                    indiceDelMinimo = j;
                }
            }
            if (indiceDelMinimo != i) {
                String auxiliar = nombres[i];
                nombres[i] = nombres[indiceDelMinimo];
                nombres[indiceDelMinimo] = auxiliar;
                intercambios[0]++;
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
        demostrarDiferenciaDeCapitalizacion();
        demostrarCasosLimite();
        ejecutarInteractivo();
    }

    private static void demostrarEjemploDelEnunciado() {
        System.out.println("=== EJERCICIO 3.4: ORDENAMIENTO DE NOMBRES CON SELECTION SORT ===");
        System.out.println();

        String[] original = { "Lucia", "Ana", "Pedro", "Juan" };
        System.out.println("  Arreglo del enunciado: " + formatearVector(original));

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];

        String[] copia = copiar(original);
        selectionSortNombres(copia, comparaciones, intercambios);

        System.out.println();
        System.out.println("  Ordenado (compareTo): " + formatearVector(copia));
        System.out.println("  Comparaciones: " + comparaciones[0]
                + " | intercambios: " + intercambios[0]);
        System.out.println("  Esperado: [Ana, Juan, Lucia, Pedro]");

        String[] oraculo = copiar(original);
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(copia, oraculo) ? "CORRECTO" : "DISCREPANCIA"));
    }

    private static void demostrarDiferenciaDeCapitalizacion() {
        System.out.println();
        System.out.println("--- CÓMO CAMBIA LA COMPARACIÓN AL ORDENAR TEXTOS ---");
        System.out.println();

        System.out.println("  Para números basta el operador de relación: 5 < 3.");
        System.out.println("  Para textos hace falta un método que devuelve un ENTERO:");
        System.out.println("    compareTo devuelve < 0, 0 o > 0 según el orden.");
        System.out.println();

        System.out.println("  PERO ese orden es por CÓDIGO DE CARÁCTER, no alfabético:");
        System.out.println("    'A' = " + (int) 'A' + "   'Z' = " + (int) 'Z'
                + "   'a' = " + (int) 'a' + "   'z' = " + (int) 'z');
        System.out.println("    Las mayúsculas están ANTES que las minúsculas en Unicode.");
        System.out.println();

        String[] conMinusculas = { "lucia", "Ana", "Pedro", "Bárbara" };
        String[] conMayusculas = { "Lucía", "ana", "Pedro", "Bárbara" };

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];

        String[] sensible = copiar(conMinusculas);
        selectionSortNombres(sensible, comparaciones, intercambios);

        String[] insensible = copiar(conMinusculas);
        selectionSortIgnorandoMayusculas(insensible, comparaciones, intercambios);

        System.out.println("  Nombres: " + formatearVector(conMinusculas));
        System.out.println();
        System.out.println("  Con compareTo (SENSIBLE a mayúsculas):");
        System.out.println("    " + formatearVector(sensible));
        System.out.println("    -> las mayúsculas quedan PRIMERO porque tienen código menor.");
        System.out.println();
        System.out.println("  Con compareToIgnoreCase (INSENSIBLE):");
        System.out.println("    " + formatearVector(insensible));
        System.out.println("    -> se parece más al orden de un diccionario.");
        System.out.println();

        // La comparación literal que más confunde.
        System.out.println("  Demostración del caso confuso:");
        System.out.println("    \"ana\".compareTo(\"Lucia\")   = " + "ana".compareTo("Lucia")
                + "  -> POSITIVO, o sea \"ana\" va DESPUES de \"Lucia\"");
        System.out.println("    ¿Pero en un diccionario \"ana\" va antes! Porque 'a' = " + (int) 'a'
                + " > 'L' = " + (int) 'L' + ".");
        System.out.println();
        System.out.println("  POLÍTICA ELEGIDA: se implementan ambas variantes y se muestran");
        System.out.println("  las dos, para que la diferencia sea visible y no una decisión oculta.");
    }

    private static void demostrarCasosLimite() {
        System.out.println();
        System.out.println("--- CASOS LÍMITE ---");

        long[] comparaciones = new long[1];
        long[] intercambios = new long[1];

        System.out.println();
        System.out.println("  Vacío:");
        selectionSortNombres(new String[] {}, comparaciones, intercambios);
        System.out.println("    comparaciones: " + comparaciones[0] + ", intercambios: " + intercambios[0]);

        System.out.println("  Un solo nombre:");
        selectionSortNombres(new String[] { "Ana" }, comparaciones, intercambios);
        System.out.println("    comparaciones: " + comparaciones[0] + ", intercambios: " + intercambios[0]);

        System.out.println("  Nombres repetidos:");
        String[] repetidos = { "Ana", "Ana", "Luis", "Luis" };
        selectionSortNombres(copiar(repetidos), comparaciones, intercambios);
        System.out.println("    orden final: " + formatearVector(repetidos)
                + " | comparaciones: " + comparaciones[0] + ", intercambios: " + intercambios[0]);

        int n = 4;
        System.out.println();
        System.out.println("  Con n = " + n + ", Selection hace siempre n(n-1)/2 = "
                + (long) n * (n - 1) / 2 + " comparaciones, este o no esté ordenado.");
        System.out.println("  Θ(n²) temporal · Θ(1) espacio auxiliar · NO es estable");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Cantidad de nombres: ");
            if (!entrada.hasNextInt()) {
                return;
            }
            int cantidad = entrada.nextInt();

            if (cantidad < 0) {
                System.out.println("  La cantidad no puede ser negativa.");
                return;
            }

            // nextInt deja el salto de línea pendiente: hay que consumirlo antes
            // de pasar a nextLine(), o el primer nombre saldría vacío.
            entrada.nextLine();

            String[] nombres = new String[cantidad];
            for (int i = 0; i < cantidad; i++) {
                System.out.print("  Nombre " + i + ": ");
                nombres[i] = entrada.nextLine();
            }

            long[] comparaciones = new long[1];
            long[] intercambios = new long[1];

            String[] sensible = copiar(nombres);
            String[] insensible = copiar(nombres);

            selectionSortNombres(sensible, comparaciones, intercambios);
            long comparacionesSensible = comparaciones[0];

            selectionSortIgnorandoMayusculas(insensible, comparaciones, intercambios);

            System.out.println();
            System.out.println("  Original                    : " + formatearVector(nombres));
            System.out.println("  Ordenado (sensible)         : " + formatearVector(sensible));
            System.out.println("  Ordenado (insensible)       : " + formatearVector(insensible));
            System.out.println("  Comparaciones (sensible)    : " + comparacionesSensible
                    + " | intercambios: " + intercambios[0]);
            System.out.println();
            System.out.println("  Si las dos listas difieren, hay nombres con distinta");
            System.out.println("  capitalización y el orden por código de carácter no coincide");
            System.out.println("  con el alfabético.");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Devuelve el arreglo como texto entre corchetes. */
    private static String formatearVector(String[] vector) {
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

    /** Copia el arreglo: cada variante necesita partir del mismo estado. */
    private static String[] copiar(String[] vector) {
        String[] copia = new String[vector.length];
        for (int i = 0; i < vector.length; i++) {
            copia[i] = vector[i];
        }
        return copia;
    }

    /** Compara dos arreglos de String: mismo contenido y misma longitud. */
    private static boolean sonIguales(String[] a, String[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null ? b[i] != null : !a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se agregaron al prompt DOS variantes del algoritmo (compareTo y
 *   compareToIgnoreCase) en lugar de una sola. El enunciado pide "ordenar
 *   alfabéticamente", pero en Java "alfabéticamente" es ambiguo: compareTo
 *   ordena por código de carácter y da un resultado que no parece alfabético
 *   ("ana" queda después de "Lucia"). Mostrar las dos políticas hace visible la
 *   diferencia en vez de esconder una decisión arbitraria.
 * - Se añadió la demostración de "ana".compareTo("Lucia") > 0 con los valores
 *   numéricos de los caracteres. Es el ejemplo mínimo que muestra por qué el
 *   orden por código no es el orden del diccionario, y el enunciado pedía
 *   explícitamente razonar "cómo cambia la comparación al ordenar textos".
 * - Se documentó el consumo del salto de línea con nextLine() después de
 *   nextInt(). Sin eso el primer nombre leído sale vacío y el programa falla
 *   con una excepción que no tiene nada que ver con el algoritmo.
 * ========================================================================== */