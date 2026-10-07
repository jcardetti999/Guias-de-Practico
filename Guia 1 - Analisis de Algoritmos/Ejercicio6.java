/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 6: Comparar dos vectores
 *
 * ESTRATEGIA
 * Comparación posición a posición con salida temprana: se recorren ambos
 * vectores en paralelo comparando vectorA[i] con vectorB[i]. En cuanto aparece
 * una diferencia, el algoritmo termina y devuelve false, porque la respuesta ya
 * está decidida: si un solo par difiere, los vectores no son iguales.
 *
 * POR QUÉ ES SEGURO TERMINAR EN LA PRIMERA DIFERENCIA
 * La pregunta es "¿son iguales?", y es una pregunta de sí o NO. Basta con un
 * contraejemplo para refutarla. Una vez encontrado un par distinto, ninguna
 * cantidad de coincidencias posteriores puede "compensar" ese par: la igualdad de
 * dos vectores exige que TODOS los pares coincidan. Es una conjunción, y en una
 * conjunción basta un término falso para que toda la expresión sea falsa.
 *
 * DECISIÓN SOBRE LONGITUDES DISTINTAS
 * Si los vectores tienen longitudes diferentes, NO son iguales. Se decide esto
 * ANTES de entrar al bucle y se devuelve false con 0 comparaciones, porque es
 * imposible que [a,b,c] sea igual a [a,b]: la igualdad implica tener la misma
 * cantidad de elementos. Verificarlo primero es una guarda (early return) que
 * evita un trabajo inútil.
 *
 * COMPLEJIDAD
 * Temporal:
 *   - Mejor caso:    Θ(1). O bien difieren en la posición 0 (1 comparación),
 *                    o bien tienen longitudes distintas (0 comparaciones).
 *   - Peor caso:     Θ(n). Son idénticos hasta el final, o difieren sólo en la
 *                    última posición: hay que recorrerlos enteros.
 *   - Caso promedio: Θ(n) en el orden asintótico, pero con una CONSTANTE menor
 *                    que sin salida temprana: si la diferencia cae en una
 *                    posición uniforme, se recorren ~n/2 pares en lugar de n.
 *                    La diferencia entre Θ(n) y Θ(n) es el factor 2, no la clase.
 * Espacial: Θ(1). Sólo dos índices y el contador.
 *
 * CASOS LÍMITE
 * - ambos vacíos -> son iguales (0 comparaciones).
 * - uno vacío y el otro no -> distintos (0 comparaciones).
 * - longitudes distintas -> distintos, con guarda previa de Θ(1).
 * - idénticos -> recorren las n posiciones.
 */
public class Ejercicio6 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Determina si dos vectores son iguales elemento a elemento.
     *
     * @param vectorA       primer vector; no se modifica
     * @param vectorB       segundo vector; no se modifica
     * @param comparaciones arreglo de longitud 1: recibe el nº de comparaciones
     * @return true si ambos vectores tienen la misma longitud y los mismos
     *         valores en todas las posiciones
     */
    public static boolean sonIguales(int[] vectorA, int[] vectorB, int[] comparaciones) {
        comparaciones[0] = 0;

        // Guarda: longitudes distintas implican vectores distintos. Se resuelve
        // en Θ(1) sin tocar ningún elemento.
        if (vectorA == null || vectorB == null) {
            return vectorA == vectorB;
        }
        if (vectorA.length != vectorB.length) {
            return false;
        }

        // Si se llega acá, ambos vectores están vacíos o tienen igual longitud.
        for (int i = 0; i < vectorA.length; i++) {
            comparaciones[0]++;
            if (vectorA[i] != vectorB[i]) {
                // Corte temprano: ya hay un par que difiere, la respuesta es
                // false y ninguna comparación posterior puede cambiarla.
                return false;
            }
        }

        // Se recorrieron todos los pares sin diferencias: son iguales.
        return true;
    }

    /**
     * Variante informativa: devuelve el índice del primer par que difiere.
     *
     * @param vectorA primer vector; no se modifica
     * @param vectorB segundo vector; no se modifica
     * @return el índice de la primera diferencia, o -1 si son iguales
     */
    public static int indicePrimeraDiferencia(int[] vectorA, int[] vectorB) {
        if (vectorA == null || vectorB == null || vectorA.length != vectorB.length) {
            return -1;
        }

        for (int i = 0; i < vectorA.length; i++) {
            if (vectorA[i] != vectorB[i]) {
                return i;
            }
        }
        return -1;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 6: COMPARAR DOS VECTORES ===");

        int[] a = { 10, 20, 30, 40, 50 };
        int[] comparaciones = new int[1];

        // Mejor caso 1: longitudes distintas -> 0 comparaciones.
        int[] distintaLongitud = { 10, 20, 30 };
        System.out.println("Caso A (longitudes distintas):");
        System.out.println("  A: " + formatearVector(a) + "   B: " + formatearVector(distintaLongitud));
        System.out.println("  sonIguales: " + sonIguales(a, distintaLongitud, comparaciones)
                + "  | comparaciones: " + comparaciones[0] + "  -> Θ(1) por la guarda de longitud");

        // Mejor caso 2: difieren en la posición 0 -> 1 comparación.
        int[] difierePrimero = { 99, 20, 30, 40, 50 };
        System.out.println();
        System.out.println("Caso B (difieren en la posición 0):");
        System.out.println("  A: " + formatearVector(a) + "   B: " + formatearVector(difierePrimero));
        System.out.println("  sonIguales: " + sonIguales(a, difierePrimero, comparaciones)
                + "  | comparaciones: " + comparaciones[0] + "  -> mejor caso Θ(1)");
        System.out.println("  primera diferencia en el índice: " + indicePrimeraDiferencia(a, difierePrimero));

        // Peor caso 1: idénticos -> n comparaciones.
        int[] copia = { 10, 20, 30, 40, 50 };
        System.out.println();
        System.out.println("Caso C (idénticos) — peor caso:");
        System.out.println("  A: " + formatearVector(a) + "   B: " + formatearVector(copia));
        System.out.println("  sonIguales: " + sonIguales(a, copia, comparaciones)
                + "  | comparaciones: " + comparaciones[0] + "  -> Θ(n), se recorre todo");

        // Peor caso 2: difieren sólo en la última posición -> n comparaciones.
        int[] difiereAlFinal = { 10, 20, 30, 40, 51 };
        System.out.println();
        System.out.println("Caso D (difieren sólo en la última posición) — peor caso:");
        System.out.println("  sonIguales: " + sonIguales(a, difiereAlFinal, comparaciones)
                + "  | comparaciones: " + comparaciones[0]
                + "  -> Θ(n): se recorre todo antes de detectar la diferencia");
        System.out.println("  primera diferencia en el índice: " + indicePrimeraDiferencia(a, difiereAlFinal));

        // Casos límite con vectores vacíos.
        System.out.println();
        System.out.println("Casos límite con el vector vacío:");
        System.out.println("  ambos vacíos   -> " + sonIguales(new int[] {}, new int[] {}, comparaciones)
                + "  | comparaciones: " + comparaciones[0]);
        System.out.println("  uno vacío     -> " + sonIguales(new int[] {}, a, comparaciones)
                + "  | comparaciones: " + comparaciones[0]);

        // Demostración del promedio: comparar contra n copias que difieren en
        // posiciones cada vez más tardías.
        System.out.println();
        System.out.println("--- EFECTO DE LA SALIDA TEMPRANA ---");
        for (int posDiferencia = 0; posDiferencia < a.length; posDiferencia++) {
            int[] b = { 10, 20, 30, 40, 50 };
            b[posDiferencia] = 999;
            sonIguales(a, b, comparaciones);
            System.out.println("  diferencia en la posición " + posDiferencia
                    + " -> comparaciones realizadas: " + comparaciones[0]);
        }
        System.out.println("  Cuantas más tarde está la diferencia, más se recorre.");
        System.out.println("  El promedio es ~n/2 = " + ((a.length + 1) / 2) + " frente a n = " + a.length);
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