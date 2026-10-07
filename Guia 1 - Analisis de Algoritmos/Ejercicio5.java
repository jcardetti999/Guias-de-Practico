/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 5: Contar ocurrencias de un valor
 *
 * ESTRATEGIA
 * Un recorrido lineal completo con un contador inicializado en cero. Por cada
 * elemento se compara con el valor buscado y, si coincide, el contador aumenta
 * en uno. El resultado es el número de apariciones.
 *
 * POR QUÉ NECESITA RECORRER EL VECTOR COMPLETO
 * Este es el punto central del ejercicio y la diferencia con el Ejercicio 2.
 *
 * En el Ejercicio 2 (buscar) el corte temprano es correcto: en cuanto se
 * encuentra el elemento, la pregunta ya está respondida. El resto del vector es
 * irrelevante, así que detener el recorrido sólo ahorra trabajo, nunca cambia
 * la respuesta.
 *
 * En CONTAR el corte temprano es INCORRECTO. La pregunta no es "¿existe?", sino
 * "¿cuántas veces?". Encontrar la primera coincidencia no dice nada sobre las
 * siguientes: puede haber 1 o puede haber 500 apariciones después. Si se cortara
 * en la primera, el resultado sería 1 siempre, y sería una respuesta FALSA.
 * Se podría cortar sólo con una cota superior conocida, pero no la hay: en el
 * peor caso el valor buscado ni siquiera existe, y eso exige recorrer todo el
 * vector para confirmarlo.
 *
 * COMPLEJIDAD
 * Temporal: Θ(n) en todos los casos (mejor, promedio y peor). El bucle siempre
 *           recorre los n elementos porque la condición de corte nunca se
 *           cumple; el cuerpo tiene Θ(1) y se ejecuta n veces.
 *           No existe un mejor caso mejor que otro: la respuesta depende de
 *           CUÁNTAS apariciones hay, pero el algoritmo tiene que mirar todas
 *           para saberlo.
 * Espacial: Θ(1). Sólo el índice, el contador y el valor buscado.
 *
 * CASOS LÍMITE
 * - vector vacío -> 0 apariciones (no es un error, es la respuesta correcta).
 * - el valor buscado aparece 0 veces -> 0.
 * - el valor buscado aparece en todas las posiciones -> n.
 */
public class Ejercicio5 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Cuenta cuántas veces aparece un valor dentro de un vector.
     *
     * @param vector         vector de entrada; no se modifica
     * @param valorBuscado   valor cuyas apariciones se quieren contar
     * @param comparaciones  arreglo de longitud 1: recibe el nº de comparaciones
     * @return cantidad de apariciones de valorBuscado en vector
     */
    public static int contarOcurrencias(int[] vector, int valorBuscado, int[] comparaciones) {
        comparaciones[0] = 0;

        if (vector == null || vector.length == 0) {
            return 0;
        }

        int contador = 0;

        // Invariante: al comenzar la vuelta i, `contador` es la cantidad exacta
        // de apariciones de valorBuscado entre vector[0] e vector[i-1]. Como el
        // bucle termina cuando i llega a length, al salir `contador` es el total
        // de apariciones en todo el vector.
        //
        // NO hay corte temprano: ver la justificación de cabecera. Encontrar la
        // primera coincidencia no permite deducir cuántas hay en total.
        for (int i = 0; i < vector.length; i++) {
            comparaciones[0]++;          // una comparación por elemento
            if (vector[i] == valorBuscado) {
                contador++;
            }
        }

        return contador;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        demostrarCasos();
        ejecutarInteractivo();
    }

    /** Ejercita los tres casos límite del enunciado. */
    private static void demostrarCasos() {
        System.out.println("=== EJERCICIO 5: CONTAR OCURRENCIAS ===");

        int[] vector = { 5, 12, 7, 5, 30, 5, 18, 7, 5, 22 };
        int[] comparaciones = new int[1];

        System.out.println("Vector de prueba: " + formatearVector(vector)
                + "   (n = " + vector.length + ")");

        // Caso 1: aparece 4 veces.
        int cantidad = contarOcurrencias(vector, 5, comparaciones);
        System.out.println("Buscar 5  -> " + cantidad + " apariciones"
                + " | comparaciones: " + comparaciones[0]);

        // Caso 2: aparece 2 veces.
        cantidad = contarOcurrencias(vector, 7, comparaciones);
        System.out.println("Buscar 7  -> " + cantidad + " apariciones"
                + " | comparaciones: " + comparaciones[0]);

        // Caso 3 (adversario): 0 apariciones. Un corte temprano aquí devolvería
        // una respuesta incorrecta en cuanto encuentre la primera coincidencia.
        cantidad = contarOcurrencias(vector, 999, comparaciones);
        System.out.println("Buscar 999-> " + cantidad + " aparencias"
                + " | comparaciones: " + comparaciones[0]
                + "  (NO se puede cortar antes: no se sabe si aparece)");

        // Caso límite: el valor está en todas las posiciones.
        int[] todosIguales = { 8, 8, 8, 8, 8, 8 };
        cantidad = contarOcurrencias(todosIguales, 8, comparaciones);
        System.out.println("Vector de 6 ochos -> " + cantidad + " apariciones"
                + " | comparaciones: " + comparaciones[0]);

        // Caso límite: vector vacío.
        cantidad = contarOcurrencias(new int[] {}, 8, comparaciones);
        System.out.println("Vector vacío       -> " + cantidad + " apariciones"
                + " | comparaciones: " + comparaciones[0]);

        // Demostración del punto central: el costo NO depende de la respuesta.
        System.out.println();
        System.out.println("--- POR QUE NO PUEDE HABER CORTE TEMPRANO ---");
        System.out.println("  El vector contiene: " + formatearVector(vector));
        System.out.println("  Buscando 5 (la PRIMERA coincidencia esta en el indice 0):");
        System.out.println("    -> si se cortara al encontrarla, la respuesta seria 1.");
        System.out.println("    -> la respuesta REAL es 4, o sea el corte temprano seria FALSO.");
        System.out.println("  Por eso el algoritmo recorre siempre las " + vector.length + " posiciones.");
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

            int[] vector = new int[cantidad];
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Elemento " + i + ": ");
                vector[i] = entrada.nextInt();
            }

            System.out.print("Valor cuyas apariciones se desean contar: ");
            int valorBuscado = entrada.nextInt();

            int[] comparaciones = new int[1];
            int apariciones = contarOcurrencias(vector, valorBuscado, comparaciones);

            System.out.println("Vector recibido: " + formatearVector(vector));
            System.out.println("El valor " + valorBuscado + " aparece " + apariciones + " veces.");
            System.out.println("Comparaciones realizadas: " + comparaciones[0]
                    + " (exactamente n = " + vector.length + ")");
            System.out.println("Temporal Theta(n) en todos los casos · Espacial Theta(1)");
        }
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