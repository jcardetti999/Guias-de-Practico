/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 1: Encontrar el mínimo de un vector
 *
 * ESTRATEGIA
 * Un único recorrido lineal con un acumulador `min` inicializado en vector[0].
 * En cada vuelta se compara el elemento actual contra el mejor candidato y el
 * acumulado se actualiza sólo si el elemento es menor. El vector NO se ordena.
 *
 * POR QUÉ NO ES NECESARIO ORDENAR EL VECTOR
 * Ordenar exige Θ(n log n) comparaciones y, además, DESTRUYE el vector de
 * entrada (queda permutado). El mínimo, en cambio, sólo necesita comparar cada
 * elemento contra el mejor candidato visto hasta el momento. Comparar contra un
 * acumulado NO es ordenar: es mantener un único valor entre n elementos, sin
 * producir un orden total. Por lo tanto Θ(n) < Θ(n log n) y además el vector
 * original queda intacto, lo cual importa si luego se lo usa para otra cosa.
 *
 * COMPLEJIDAD
 * Temporal: Θ(n) en el mejor, promedio y peor caso. El cuerpo del bucle tiene
 *           Θ(1) y se ejecuta exactamente n-1 veces (n-1 comparaciones), por
 *           lo tanto el total es Θ(1) + (n-1)*Θ(1) = Θ(n) siempre.
 *           El peor caso no empeora respecto del mejor porque la cantidad de
 *           comparaciones NO depende del orden de los datos.
 * Espacial: Θ(1) auxiliar. Sólo viven `min`, el índice del bucle y el contador
 *           de instrumentación, ninguno de tamaño proporcional a n.
 *
 * PRECONDICIONES Y CASOS LÍMITE
 * - vector vacío -> se devuelve null y se informa por pantalla, sin excepción.
 * - un solo elemento -> 0 comparaciones: el bucle nunca entra.
 * - valores negativos o todos ceros -> el acumulado se inicializa con vector[0],
 *   NUNCA con 0, porque eso devolvería 0 como si fuera el mínimo real.
 * - el vector de entrada NO se modifica en ningún momento.
 */
public class Ejercicio1_1 {

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Encuentra el valor mínimo de un vector mediante un recorrido lineal único.
     *
     * @param vector        vector de entrada; no se modifica
     * @param comparaciones arreglo de longitud 1 usado como contador de salida.
     *                      Es un parámetro de instrumentación: existe sólo para
     *                      poder informar cuántas comparaciones hizo el algoritmo,
     *                      y su costo se descarta del análisis de complejidad.
     * @return el valor mínimo, o null si el vector es null o está vacío
     */
    public static Integer buscarMinimo(int[] vector, int[] comparaciones) {
        comparaciones[0] = 0;

        // Caso límite: sin datos no hay mínimo que devolver. Se informa un valor
        // centinela (null) en lugar de lanzar una excepción, para que el programa
        // pueda seguir y mostrarle un mensaje al usuario.
        if (vector == null || vector.length == 0) {
            return null;
        }

        // Inicializar con el primer dato, no con 0: el mínimo de un vector de
        // negativos es negativo, y arrancar en 0 devolvería un resultado falso.
        int min = vector[0];

        // Invariante del bucle: al comenzar la vuelta i, `min` es el menor valor
        // entre vector[0] e vector[i-1]. Como el bucle termina cuando i llega a
        // length, al salir `min` es el menor valor del vector completo.
        for (int i = 1; i < vector.length; i++) {
            comparaciones[0]++;          // cada iteración es exactamente una comparación
            if (vector[i] < min) {
                min = vector[i];
            }
        }

        return min;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        demostrarCasosLimite();
        ejecutarInteractivo();
    }

    /**
     * Casos no interactivos para verificar los casos límite del algoritmo.
     * Se ejecutan primero para que se vea el comportamiento sin tipear nada.
     */
    private static void demostrarCasosLimite() {
        System.out.println("=== DEMOSTRACION AUTOMATICA (casos limite) ===");

        // Caso 1: vector vacío.
        Integer vacio = buscarMinimo(new int[] {}, new int[1]);
        System.out.println("Vector vacio      -> resultado: "
                + (vacio == null ? "null (no hay minimo)" : vacio));

        // Caso 2: un solo elemento -> el bucle no entra, 0 comparaciones.
        int[] contador = new int[1];
        Integer unico = buscarMinimo(new int[] { 42 }, contador);
        System.out.println("Un solo elemento -> minimo: " + unico
                + ", comparaciones: " + contador[0]);

        // Caso 3 (adversario): todos negativos. Falla si el acumulado arranca en 0.
        int[] negativos = { -3, -17, -8, -1, -42 };
        Integer minNegativo = buscarMinimo(negativos, contador);
        System.out.println("Todos negativos  -> vector: " + formatearVector(negativos)
                + ", minimo: " + minNegativo + ", comparaciones: " + contador[0]);

        // Caso 4 (típico): el mínimo está en el medio y no en los extremos.
        int[] tipico = { 12, -4, 7, 33, -9, 20, 5 };
        Integer minTipico = buscarMinimo(tipico, contador);
        System.out.println("Caso tipico      -> vector: " + formatearVector(tipico)
                + ", minimo: " + minTipico + ", comparaciones: " + contador[0]);

        // El vector de entrada debe seguir intacto: no se ordena.
        System.out.println("Vector tras ejecutar (debe estar IGUAL al original): " + formatearVector(tipico));
        System.out.println("Comparaciones = n-1 = " + (tipico.length - 1)
                + " -> confirma que el costo NO depende del orden de los datos.");
    }

    /** Lectura del vector por consola, como pide el enunciado. */
    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese la cantidad de elementos del vector: ");
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

            int[] comparaciones = new int[1];
            Integer minimo = buscarMinimo(vector, comparaciones);

            System.out.println("Vector recibido: " + formatearVector(vector));
            if (minimo == null) {
                System.out.println("El vector esta vacio: no existe minimo.");
            } else {
                System.out.println("Valor minimo: " + minimo);
                System.out.println("Comparaciones realizadas: " + comparaciones[0]);
                System.out.println("Complejidad temporal Theta(n) = Theta(" + vector.length
                        + "), espacial Theta(1).");
            }
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
