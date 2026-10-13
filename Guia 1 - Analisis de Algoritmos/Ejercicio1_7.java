/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 7: Invertir un vector (dos versiones)
 *
 * ESTRATEGIA
 * Se implementan DOS algoritmos distintos para el mismo problema.
 *
 *   Versión A — Con vector auxiliar.
 *       Se crea un arreglo nuevo del mismo tamaño y se copian los elementos en
 *       orden inverso. El vector original queda intacto.
 *
 *   Versión B — In-place con dos punteros.
 *       Se intercambio los extremos del mismo arreglo: izquierda empieza en 0 y
 *       derecha en length-1, se intercambian y ambos se mueven hacia el centro.
 *       Termina cuando los punteros se cruzan.
 *
 * COMPARACIÓN DE LAS DOS VERSIONES
 *
 * ┌────────────────────┬────────────────────────┬──────────────────────────┐
 * │                    │ Versión A (auxiliar)   │ Versión B (in-place)     │
 * ├────────────────────┼────────────────────────┼──────────────────────────┤
 * │ Tiempo             │ Θ(n)                   │ Θ(n)                     │
 * │ Espacio auxiliar   │ Θ(n)                   │ Θ(1)                     │
 * │ Intercambios       │ n/2 asignaciones       │ n/2 intercambios         │
 * │ Vector original    │ NO se modifica         │ SÍ se modifica           │
 * │ Costo de memoria   │ alto (doble)           │ nulo                     │
 * │ Cache / localidad  │ peor                   │ mejor (datos en sitio)   │
 * └────────────────────┴────────────────────────┴──────────────────────────┘
 *
 * En tiempo AMBAS son Θ(n), porque en las dos hay que tocar los n elementos:
 * no hay forma de invertir sin leer cada elemento y escribirlo en su nueva
 * posición. La diferencia real NO es temporal sino ESPACIAL y de efectos
 * secundarios.
 *
 * VENTAJAS Y DESVENTAJAS
 *   Versión A — Ventajas: no destruye la entrada, así que puede reutilizarse;
 *               es más simple de leer y de verificar. Desventajas: duplica la
 *               memoria; en un sistema con memoria acotada puede no servir.
 *   Versión B — Ventajas: Θ(1) de memoria adicional, que es el óptimo teórico;
 *               funciona in-place y mejora la localidad de caché. Desventajas:
 *               destruye la entrada (si hace falta el original, hay que
 *               duplicarlo antes); el cálculo de los punteros es más delicado.
 *
 * ¿CUÁL SE USA EN LA PRÁCTICA?
 * La versión in-place es la opción por defecto: Θ(1) de memoria adicional es
 * siempre preferible cuando no se necesita conservar el original. Un motor
 * real usaría la versión B. La versión A sólo tiene sentido cuando el original
 * debe preservarse y, aun así, suele resolverse copiando una sola vez y
 * trabajando sobre la copia.
 *
 * CASOS LÍMITE
 * - vector vacío -> ambos punteros arrancan cruzados (0 > -1), el bucle no entra.
 * - un elemento -> izquierda=0, derecha=0, 0 > 0 es falso, no intercambia.
 * - dos elementos -> un solo intercambio y los punteros quedan cruzados.
 */
public class Ejercicio1_7 {

    // ============================================================
    //  ALGORITMO — VERSIÓN A: con vector auxiliar
    // ============================================================

    /**
     * Versión A: invierte el vector usando un arreglo auxiliar.
     * El vector original NO se modifica.
     *
     * @param vector vector de entrada; no se modifica
     * @return un arreglo nuevo con los elementos en orden inverso
     */
    public static int[] invertirConVectorAuxiliar(int[] vector) {
        int n = vector.length;
        int[] invertido = new int[n];

        // Invariante: al terminar la vuelta i, invertido[0..i] contiene
        // vector[n-1..n-i] en orden. Al salir, invertido[k] = vector[n-1-k]
        // para todo k, es decir, el vector completo invertido.
        for (int i = 0; i < n; i++) {
            invertido[i] = vector[n - 1 - i];
        }

        return invertido;
    }

    // ============================================================
    //  ALGORITMO — VERSIÓN B: in-place con dos punteros
    // ============================================================

    /**
     * Versión B: invierte el vector en el mismo espacio mediante dos punteros.
     * El vector de entrada SÍ se modifica.
     *
     * @param vector        vector de entrada; se modifica en el sitio
     * @param intercambios  arreglo de longitud 1: recibe el nº de intercambios
     */
    public static void invertirInPlace(int[] vector, int[] intercambios) {
        intercambios[0] = 0;

        int izquierda = 0;
        int derecha = vector.length - 1;

        // Condición de corte: mientras los punteros NO se hayan cruzado.
        // Con un vector vacío, izquierda=0 y derecha=-1, así que 0 > -1 es
        // verdadero desde el inicio y el bucle no entra: caso borde resuelto
        // sin código extra.
        //
        // Invariante: antes de cada vuelta, el prefijo vector[0..izquierda-1]
        // ya está en su posición final definitiva (espejo del prefijo original),
        // y el sufijo vector[derecha+1..n-1] también. Al cruzarse los
        // punteros, todo el vector quedó colocado.
        while (izquierda < derecha) {
            int aux = vector[izquierda];
            vector[izquierda] = vector[derecha];
            vector[derecha] = aux;
            intercambios[0]++;
            izquierda++;
            derecha--;
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 1.7: INVERTIR UN VECTOR ===");

        int[][] casos = {
            { 1, 2, 3, 4, 5 },       // típico, longitud impar
            { 1, 2, 3, 4, 5, 6 },     // típico, longitud par
            { 42 },                   // límite: un elemento
            { 7, 8 },                 // límite: dos elementos
            {}                         // límite: vector vacío
        };

        String[] nombres = { "Impar (n=5)", "Par (n=6)", "Un elemento", "Dos elementos", "Vacío" };

        for (int c = 0; c < casos.length; c++) {
            int[] original = casos[c];

            // Copias independientes: cada versión parte del mismo estado.
            int[] entradaVersionA = copiar(original);
            int[] entradaVersionB = copiar(original);
            int[] intercambios = new int[1];

            int[] resultadoA = invertirConVectorAuxiliar(entradaVersionA);
            invertirInPlace(entradaVersionB, intercambios);

            System.out.println();
            System.out.println("--- " + nombres[c] + " ---");
            System.out.println("  Original               : " + formatearVector(original));
            System.out.println("  Versión A (auxiliar)   : " + formatearVector(resultadoA)
                    + "  | original intacto: " + formatearVector(entradaVersionA)
                    + " | memoria extra: Θ(n) = " + resultadoA.length + " ints");
            System.out.println("  Versión B (in-place)   : " + formatearVector(entradaVersionB)
                    + "  | intercambios: " + intercambios[0]
                    + " | memoria extra: Θ(1)");
            System.out.println("  ¿Ambas coinciden?      : " + sonIguales(resultadoA, entradaVersionB));
            System.out.println("  ¿La versión A conservó el original? : "
                    + sonIguales(entradaVersionA, original));
        }

        // Verificación funcional: invertir dos veces debe devolver el original.
        System.out.println();
        System.out.println("--- VERIFICACIÓN: invertir dos veces ---");
        int[] prueba = { 4, 8, 15, 16, 23, 42 };
        int[] unaVez = invertirConVectorAuxiliar(prueba);
        int[] dosVeces = invertirConVectorAuxiliar(unaVez);
        System.out.println("  Original   : " + formatearVector(prueba));
        System.out.println("  Invertido  : " + formatearVector(unaVez));
        System.out.println("  Re-invertido: " + formatearVector(dosVeces));
        System.out.println("  Vuelve a ser el original: " + sonIguales(prueba, dosVeces));
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Copia el vector: necesario para que cada versión parta del mismo estado. */
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