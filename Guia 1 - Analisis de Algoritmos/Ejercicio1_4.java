/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio 4: Detectar elementos duplicados (dos soluciones)
 *
 * ESTRATEGIA
 * Se implementan DOS algoritmos distintos sobre el mismo problema, para poder
 * contrastarlos:
 *
 *   Solución A — Dos ciclos anidados (fuerza bruta).
 *       Para cada posición i se recorre desde i+1 hasta el final comparando
 *       los pares. Es el enfoque directo: "comprobar si hay dos iguales".
 *
 *   Solución B — java.util.HashSet.
 *       Se recorre una sola vez y se va guardando lo ya visto. Cada elemento se
 *       consulta contra el conjunto: si ya estaba, es un duplicado.
 *
 * COMPARACIÓN DE COMPLEJIDADES
 *
 * ┌─────────────────────────┬──────────────────┬──────────────────┬──────────────┐
 * │                         │ Solución A       │ Solución B       │ Observación  │
 * │                         │ ciclos anidados  │ HashSet          │              │
 * ├─────────────────────────┼──────────────────┼──────────────────┼──────────────┤
 * │ Mejor caso              │ Θ(1)             │ Θ(1)             │              │
 * │ Promedio                │ Θ(n²)            │ Θ(n)             │ B gana       │
 * │ Peor caso               │ Θ(n²)            │ Θ(n²)            │ empatan      │
 * │ Complejidad espacial    │ Θ(1)             │ Θ(n)             │ A gana       │
 * │ Legibilidad didáctica   │ alta             │ alta             │              │
 * └─────────────────────────┴──────────────────┴──────────────────┴──────────────┘
 *
 * Detalle importante del PEOR CASO de la Solución B: un HashSet NO degrada
 * porque el hash sea "malo", sino por COLISIONES. Si todos los elementos caen
 * en el mismo bucket, la lista encadenada interna se comporta como una búsqueda
 * lineal y cada operación pasa a costar Θ(n), alcanzando Θ(n²) en total. En la
 * práctica es rarísimo (depende de hashCode), pero el análisis formal lo exige.
 *
 * VEREDICTO — cuándo usar cada una
 *   - Sin restricción de memoria y con volumen grande o desconocido -> B (HashSet).
 *     Θ(n) en lugar de Θ(n²) es la diferencia entre milisegundos y minutos.
 *   - Memoria critica (dispositivos embebidos, o n tan grande que el conjunto no
 *     entra en RAM) -> A, porque Θ(1) auxiliar es la única opción viable.
 *   - Con fines docentes -> A, porque se ve el razonamiento de fuerza bruta y
 *     el costo cuadrático de la forma más clara posible.
 *
 * CASOS LÍMITE
 * - vector vacío -> no hay duplicados.
 * - un solo elemento -> no puede haber duplicados (0 comparaciones en A).
 * - todos los elementos iguales -> respuesta afirmativa inmediata.
 */
public class Ejercicio1_4 {

    // ============================================================
    //  ALGORITMO — SOLUCIÓN A: dos ciclos anidados
    // ============================================================

    /**
     * Solución A: fuerza bruta con dos ciclos anidados.
     *
     * @param vector        vector de entrada; no se modifica
     * @param comparaciones arreglo de longitud 1: recibe el nº de comparaciones
     * @return true si existe al menos un elemento repetido
     */
    public static boolean contieneDuplicadosConCiclosAnidados(int[] vector, int[] comparaciones) {
        comparaciones[0] = 0;

        if (vector == null || vector.length < 2) {
            return false;   // con menos de 2 elementos no puede haber un par
        }

        // Invariante: al comenzar la vuelta del ciclo externo con índice i, todos
        // los pares (p, q) con p < i ya fueron comparados y ninguno era par igual.
        for (int i = 0; i < vector.length - 1; i++) {
            // El ciclo interno empieza en i+1 para no comparar un elemento
            // consigo mismo y para no repetir cada par dos veces.
            for (int j = i + 1; j < vector.length; j++) {
                comparaciones[0]++;
                if (vector[i] == vector[j]) {
                    // Corte temprano: en cuanto aparece un par igual ya se sabe
                    // la respuesta, no hace falta seguir buscando.
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Solución A (variante): devuelve los valores que están duplicados.
     * Se recorre todo el vector para poder listarlos todos, sin corte temprano.
     *
     * @param vector vector de entrada; no se modifica
     * @return arreglo con los valores duplicados (puede estar vacío)
     */
    public static int[] obtenerDuplicadosConCiclosAnidados(int[] vector) {
        if (vector == null || vector.length < 2) {
            return new int[0];
        }

        // Se usa un arreglo auxiliar porque en Java los arreglos no crecen.
        // El tope es length/2, que es la cantidad máxima posible de duplicados.
        int[] encontrados = new int[vector.length / 2];
        int cantidad = 0;

        for (int i = 0; i < vector.length - 1; i++) {
            for (int j = i + 1; j < vector.length; j++) {
                if (vector[i] == vector[j] && !yaAnotado(encontrados, cantidad, vector[i])) {
                    encontrados[cantidad++] = vector[i];
                }
            }
        }

        int[] resultado = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            resultado[i] = encontrados[i];
        }
        return resultado;
    }

    // ============================================================
    //  ALGORITMO — SOLUCIÓN B: java.util.HashSet
    // ============================================================

    /**
     * Solución B: recorrido único con un conjunto de elementos ya vistos.
     *
     * @param vector      vector de entrada; no se modifica
     * @param operaciones arreglo de longitud 1: recibe el nº de operaciones
     *                    sobre el conjunto (add / contains)
     * @return true si existe al menos un elemento repetido
     */
    public static boolean contieneDuplicadosConHashSet(int[] vector, int[] operaciones) {
        operaciones[0] = 0;

        if (vector == null || vector.length < 2) {
            return false;
        }

        java.util.HashSet<Integer> vistos = new java.util.HashSet<>();

        for (int i = 0; i < vector.length; i++) {
            // contains + add = 2 operaciones por elemento, de costo esperado Θ(1)
            operaciones[0] += 2;
            if (vistos.contains(vector[i])) {
                return true;   // corte temprano: ya se sabe que está duplicado
            }
            vistos.add(vector[i]);
        }
        return false;
    }

    /**
     * Solución B (variante): devuelve los valores que están duplicados.
     *
     * @param vector vector de entrada; no se modifica
     * @return arreglo con los valores duplicados (puede estar vacío)
     */
    public static int[] obtenerDuplicadosConHashSet(int[] vector) {
        if (vector == null || vector.length < 2) {
            return new int[0];
        }

        java.util.HashSet<Integer> vistos = new java.util.HashSet<>();
        java.util.LinkedHashSet<Integer> duplicados = new java.util.LinkedHashSet<>();

        for (int i = 0; i < vector.length; i++) {
            // LinkedHashSet porque además de deduplicar conserva el orden de
            // inserción, así la salida es predecible y más fácil de verificar.
            if (!vistos.add(vector[i])) {
                duplicados.add(vector[i]);
            }
        }

        int[] resultado = new int[duplicados.size()];
        int i = 0;
        for (Integer valor : duplicados) {
            resultado[i++] = valor;
        }
        return resultado;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 1.4: DETECCION DE DUPLICADOS ===");

        int[][] casos = {
            { 14, 7, 23, 3, 31, 7, 9 },        // típico: un duplicado
            { 1, 2, 3, 4, 5 },                  // límite: sin duplicados
            { 5, 5, 5, 5, 5 },                  // adversario: todos iguales
            { 42 },                            // límite: un solo elemento
            {}                                  // límite: vector vacío
        };

        String[] nombres = {
            "Tipico (1 duplicado)", "Sin duplicados", "Todos iguales",
            "Un solo elemento", "Vector vacio"
        };

        for (int c = 0; c < casos.length; c++) {
            int[] vector = casos[c];
            int[] comparaciones = new int[1];
            int[] operaciones = new int[1];

            boolean conCiclos = contieneDuplicadosConCiclosAnidados(vector, comparaciones);
            boolean conHashSet = contieneDuplicadosConHashSet(vector, operaciones);

            System.out.println();
            System.out.println("--- " + nombres[c] + " ---");
            System.out.println("  Vector: " + formatearVector(vector) + "   (n = " + vector.length + ")");
            System.out.println("  Solución A (ciclos anidados): " + conCiclos
                    + "  | comparaciones: " + comparaciones[0]);
            System.out.println("  Solución B (HashSet)        : " + conHashSet
                    + "  | operaciones  : " + operaciones[0]);
            System.out.println("  Duplicados hallados (A): " + formatearVector(obtenerDuplicadosConCiclosAnidados(vector)));
            System.out.println("  Duplicados hallados (B): " + formatearVector(obtenerDuplicadosConHashSet(vector)));

            // Verificación cruzada: ambas soluciones deben coincidir siempre.
            if (conCiclos != conHashSet) {
                System.out.println("  !! ERROR: las soluciones discrepan");
            } else {
                System.out.println("  Verificacion cruzada: ambas soluciones COINCIDEN.");
            }
        }

        // Demostración del crecimiento cuadrático de la Solución A.
        System.out.println();
        System.out.println("--- CRECIMIENTO COMPARADO (peor caso: sin duplicados) ---");
        System.out.printf("  %6s | %22s | %22s | %10s%n",
                "n", "A: comparaciones", "B: operaciones", "factor");
        for (int n : new int[] { 10, 20, 40, 80, 160 }) {
            int[] vector = generarVectorSinDuplicados(n);
            int[] comparaciones = new int[1];
            int[] operaciones = new int[1];
            contieneDuplicadosConCiclosAnidados(vector, comparaciones);
            contieneDuplicadosConHashSet(vector, operaciones);
            System.out.printf("  %6d | %22d | %22d | %9.1fx%n",
                    n, comparaciones[0], operaciones[0],
                    comparaciones[0] / (double) Math.max(1, operaciones[0]));
        }
        System.out.println("  Al duplicar n, las comparaciones de A se CUADRUPLICAN: comportamiento Theta(n^2).");
        System.out.println("  Las operaciones de B sólo se duplican: comportamiento Theta(n).");
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

    /** ¿El valor ya fue anotado entre las posiciones 0..cantidad-1? */
    private static boolean yaAnotado(int[] valores, int cantidad, int valor) {
        for (int k = 0; k < cantidad; k++) {
            if (valores[k] == valor) {
                return true;
            }
        }
        return false;
    }

    /** Genera n enteros distintos, para poder medir el peor caso de la Solución A. */
    private static int[] generarVectorSinDuplicados(int n) {
        int[] vector = new int[n];
        for (int i = 0; i < n; i++) {
            vector[i] = i * 3 + 1;
        }
        return vector;
    }
}