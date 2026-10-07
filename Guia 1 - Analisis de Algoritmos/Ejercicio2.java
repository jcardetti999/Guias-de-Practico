/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 2: Buscar un elemento en un vector desordenado
 *
 * ESTRATEGIA
 * Búsqueda lineal (o secuencial): se recorren los elementos uno a uno, en el
 * orden en que aparecen, comparando cada uno con el valor buscado. En cuanto se
 * encuentra la coincidencia el recorrido se detiene (corte temprano).
 *
 * POR QUÉ ES LA ESTRATEGIA ADECUADA
 * El vector está DESORDENADO, y ese es el punto decisivo. El ordenamiento es lo
 * único que permite descartar de golpe una mitad entera de los datos: sin
 * monotonía, la comparación con un elemento sólo permite descartar ese
 * elemento. Es decir, la búsqueda lineal es ÓPTIMA para un vector desordenado:
 *Θ(n) es el mínimo posible, porque un adversario puede esconder el elemento en
 * la última posición (o no ponerlo nunca).
 *
 * COMPLEJIDAD
 * Temporal:
 *   - Mejor caso:    Θ(1). El elemento buscado está en la posición 0 y se corta
 *                    en la primera vuelta.
 *   - Peor caso:     Θ(n). El elemento está en la última posición, o no existe
 *                    y hay que recorrer el vector entero.
 *   - Caso promedio: Θ(n). Bajo la suposición usual de que cada posición es
 *                    equiprobable, se recorre en promedio la mitad del vector,
 *                    es decir (n+1)/2 comparaciones ≈ n/2. El orden asintótico
 *                    sigue siendo Θ(n) (dIFIERE en la constante, no en el orden).
 * Espacial: Θ(1). Sólo el índice del bucle y el contador.
 *
 * PRECONDICIONES Y CASOS LÍMITE
 * - Precondición: NINGUNA sobre el contenido. Se presupone desorden, pero el
 *   algoritmo funciona igual si el vector estuviera ordenado (sólo cambiaría
 *   el caso en que cae el elemento).
 * - vector vacío -> devuelve -1 (no encontrado) sin lanzar excepción.
 * - elemento ausente -> se recorren las n posiciones y se informa -1.
 */
public class Ejercicio2 {

    /** Valor centinela devuelto cuando el elemento no existe en el vector. */
    public static final int NO_ENCONTRADO = -1;

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Busca un valor dentro de un vector desordenado mediante recorrido lineal.
     *
     * @param vector              vector de entrada; no se modifica
     * @param valorBuscado        valor a localizar
     * @param posicionesRecorridas arreglo de longitud 1 usado como contador de
     *                             salida: recibe cuántas posiciones se inspeccionaron
     * @return el índice de la primera coincidencia, o -1 si no existe
     */
    public static int buscarLineal(int[] vector, int valorBuscado, int[] posicionesRecorridas) {
        posicionesRecorridas[0] = 0;

        if (vector == null || vector.length == 0) {
            return NO_ENCONTRADO;
        }

        // Invariante: al comenzar la vuelta i, ya se inspeccionaron los i
        // primeros elementos del vector y ninguno era igual al valor buscado
        // (si alguno lo hubiera sido, el bucle ya habría terminado).
        for (int i = 0; i < vector.length; i++) {
            posicionesRecorridas[0]++;      // una posición inspeccionada
            if (vector[i] == valorBuscado) {
                // Corte temprano. Es seguro: en un vector con duplicados devuelve
                // la PRIMERA coincidencia, que es el comportamiento esperado.
                return i;
            }
        }

        // Se recorrieron las 'length' posiciones sin éxito: el valor no existe.
        return NO_ENCONTRADO;
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

    /** Ejercita los tres casos de complejidad y los casos límite. */
    private static void demostrarCasos() {
        System.out.println("=== DEMOSTRACION AUTOMATICA ===");
        int[] vector = { 14, 7, 23, 3, 31, 7, 9 };
        int[] recorrido = new int[1];

        System.out.println("Vector de prueba: " + formatearVector(vector) + "  (n = " + vector.length + ")");

        // Mejor caso: el valor está en la posición 0 y se corta en la primera vuelta.
        int pos = buscarLineal(vector, vector[0], recorrido);
        System.out.println("Mejor caso   -> buscar " + vector[0] + " en la posicion 0"
                + ": indice " + pos + ", posiciones recorridas: " + recorrido[0]);

        // Peor caso por posición: el valor está al final.
        pos = buscarLineal(vector, 9, recorrido);
        System.out.println("Peor caso    -> buscar 9 (ultima posicion)"
                + ": indice " + pos + ", posiciones recorridas: " + recorrido[0]);

        // Peor caso por ausencia: no existe, se recorren las n posiciones.
        pos = buscarLineal(vector, 999, recorrido);
        System.out.println("No existe    -> buscar 999"
                + ": indice " + pos + ", posiciones recorridas: " + recorrido[0]
                + " (las n posiciones)");

        // Caso promedio: el valor está en la mitad exacta del vector, que es
        // justamente el promedio esperado de posiciones recorridas.
        int posicionMedia = (vector.length - 1) / 2;
        pos = buscarLineal(vector, vector[posicionMedia], recorrido);
        System.out.println("Caso medio   -> buscar " + vector[posicionMedia]
                + " (mitad del vector, indice " + posicionMedia + ")"
                + ": indice " + pos + ", posiciones recorridas: " + recorrido[0]
                + "  (promedio esperado: " + (vector.length + 1) / 2 + ")");

        // Caso límite: vector vacío.
        pos = buscarLineal(new int[] {}, 5, recorrido);
        System.out.println("Vector vacio -> buscar 5"
                + ": indice " + pos + ", posiciones recorridas: " + recorrido[0]);

        // Comprobación de la cota: nunca se recorren más de n posiciones.
        System.out.println("Cota verificada: recorrido maximo = " + vector.length
                + " = n -> el peor caso es Theta(n).");
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

            System.out.print("Valor a buscar: ");
            int valorBuscado = entrada.nextInt();

            int[] recorrido = new int[1];
            int indice = buscarLineal(vector, valorBuscado, recorrido);

            System.out.println("Vector recibido: " + formatearVector(vector));
            if (indice == NO_ENCONTRADO) {
                System.out.println("El valor " + valorBuscado + " NO EXISTE en el vector.");
                System.out.println("Se recorrieron " + recorrido[0] + " posiciones (las n disponibles).");
            } else {
                System.out.println("El valor " + valorBuscado + " se encontro en el indice " + indice + ".");
                System.out.println("Se recorrieron " + recorrido[0] + " posiciones hasta encontrarlo.");
            }
            System.out.println("Mejor Θ(1) · Promedio Θ(n) · Peor Θ(n) · Espacial Θ(1)");
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