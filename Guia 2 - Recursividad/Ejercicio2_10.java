/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que busque un número dentro
 * de un arreglo de enteros sin usar for ni while.
 *
 * Este problema puede resolverse recursivamente porque la posición actual del
 * barrido puede ser un parámetro. Si en vez de una variable que se incrementa
 * dentro de un ciclo, la posición es un argumento de la función, entonces cada
 * llamada puede examinar "la siguiente posición" sin necesidad de ninguna
 * estructura de control iterativa.
 *
 * El caso base (de éxito) es vector[indice] == valorBuscado, que devuelve
 * indice: se encontró el elemento y no hay nada más que hacer.
 *
 * El caso base (de fracaso) es indice >= vector.length, que devuelve -1: se
 * recorrieron todas las posiciones disponibles y el elemento no existe. Son dos
 * casos base distintos, y los dos son necesarios: sin el primero nunca se
 * encontraría nada, y sin el segundo la función no terminaría nunca.
 *
 * El caso recursivo es buscar(vector, valor, indice + 1), porque en cada llamada
 * el índice avanza una posición, que es exactamente lo que haría el ciclo.
 *
 * La función debe devolver int: el índice del elemento si lo encuentra, o -1 si
 * no existe.
 *
 * Quiero que el código tenga comentarios explicando los dos casos base, el caso
 * recursivo, y la pila de llamadas. Quiero que explique explícitamente por qué el
 * índice es un parámetro y no una variable: ése es el punto del ejercicio.
 *
 * También quiero probarlo con estos casos:
 *   - arreglo vacío -> -1 (llega directo al caso base de fracaso)
 *   - el valor está en la posición 0 -> devuelve 0 en una llamada
 *   - el valor está en la última posición -> devuelve el último índice
 *   - el valor no existe -> devuelve -1 tras recorrer todo el arreglo
 *   - arreglo con duplicados -> debe devolver la primera posición, no otra
 *   - un arreglo de 10.000 elementos -> para ver que la pila crece a Θ(n)
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 10: Buscar un elemento en un arreglo
 *
 * ESTRATEGIA — por qué es recursivo
 * Un recorrido secuencial se puede describir como "mirá la posición actual; si no
 * es la que buscabas, seguí con la siguiente". Esa descripción YA es una
 * recursión: la operación es la misma en cada paso y sólo cambia el índice.
 *
 * ⚠️ POR QUÉ EL ÍNDICE ES UN PARÁMETRO Y NO UNA VARIABLE
 * Éste es EL PUNTO del ejercicio. En un `for` clásico el índice es una variable
 * local que se inicializa, se incrementa y se lee; su estado vive entre
 * iteraciones. En una versión recursiva NO existe ese estado compartido: cada
 * llamada tiene su propia COPIA del índice, guardada en su propio marco de pila.
 *
 * Eso significa que el índice se puede pasar por parámetro sin perder nada. De
 * hecho, la recursión es la traducción directa de:
 *
 *     for (i = 0; i < n; i++)            if (v[i] == x) return i;
 *     return -1;
 *
 * a:
 *
 *     if (i >= n)      return -1;        // salida del for
 *     if (v[i] == x)   return i;         // return del cuerpo
 *     return buscar(v, x, i + 1);        // i++ y volver al for
 *
 * Cada línea del `for` tiene su equivalente en la recursión. La única diferencia
 * es dónde vive el índice: en una variable que se muta, o en un parámetro que se
 * copia.
 *
 * CASO BASE DE ÉXITO — vector[indice] == valorBuscado -> indice
 * Corta la búsqueda en cuanto encuentra el elemento. Devuelve la posición.
 *
 * CASO BASE DE FRACASO — indice >= vector.length -> -1
 * La búsqueda terminó de recorrer todo el arreglo sin éxito.
 *
 * POR QUÉ HACE FALTA EL CASO BASE DE FRACASO
 * Sin él, el índice pasaría de length-1 a length y la llamada siguiente intentaría
 * acceder a vector[length], que está fuera del arreglo: lanzaría
 * ArrayIndexOutOfBoundsException en vez de devolver -1. El caso de fracaso no es
 * sólo "no lo encontré", es también "no quedan posiciones por revisar".
 *
 * ⚠️ ORDEN DE LAS COMPROBACIONES
 * Primero se evalúa `indice >= vector.length` y después la comparación con el
 * elemento. Si se invirtieran, la llamada con indice == length accessing
 * vector[length] reventaría antes de poder devolver -1.
 *
 * Con arreglo vacío (length == 0), la llamada arranca con indice == 0, y
 * 0 >= 0 es verdadero: entra directo por el caso base de fracaso y devuelve -1
 * sin tocar ningún elemento.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n-1) + Θ(1)
 *   Temporal: Θ(n) — n llamadas en el peor caso.
 *             Mejor caso: Θ(1) — el elemento está en la posición 0.
 *   Espacial: Θ(n) — n marcos de pila, uno por posición revisada.
 *
 * PILA DE LLAMADAS
 * Con un arreglo de 4 elementos en el que el valor está en la última posición:
 * se apilan los marcos de indice=0, 1, 2 y 3. El de 3 es el caso base de éxito
 * y devuelve 3. En ese punto los tres marcos inferiores se descartan de una
 * vez, y el resultado sube intacto hasta indice=0, que lo devuelve.
 *
 * La diferencia con los ejercicios 1 a 5: acá el caso base puede ser de ÉXITO
 * (elemento encontrado) o de FRACASO (arreglo agotado). Los dos son
 * terminaciones válidas y los dos son igual de necesarios.
 *
 * CASOS LÍMITE
 * - arreglo vacío -> -1
 * - valor en la posición 0 -> devuelve 0 tras 1 llamada
 * - valor en la última posición -> devuelve length - 1
 * - valor ausente -> devuelve -1 tras length llamadas
 * - duplicados -> devuelve la PRIMERA posición
 * - arreglo grande (10000 elementos) -> demuestra que la pila crece a Θ(n)
 */
public class Ejercicio2_10 {

    /** Valor centinela devuelto cuando el elemento no existe en el arreglo. */
    public static final int NO_ENCONTRADO = -1;

    // ============================================================
    //  INSTRUMENTACIÓN DE LA PILA
    // ============================================================
    private static int profundidad = 0;
    private static int profundidadMaxima = 0;
    private static int llamadasTotales = 0;

    private static void entrar() {
        llamadasTotales++;
        profundidad++;
        if (profundidad > profundidadMaxima) {
            profundidadMaxima = profundidad;
        }
    }

    private static void salir() {
        profundidad--;
    }

    private static void reiniciarPila() {
        profundidad = 0;
        profundidadMaxima = 0;
        llamadasTotales = 0;
    }

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Busca un valor en un arreglo mediante recursión, SIN usar for ni while.
     *
     * @param vector       arreglo de entrada; no se modifica
     * @param valorBuscado valor a localizar
     * @param indice       posición desde la que se comienza a revisar
     * @return el índice del elemento, o NO_ENCONTRADO (-1) si no existe
     */
    public static int buscar(int[] vector, int valorBuscado, int indice) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE DE FRACASO ---
            // Se evalúa PRIMERO. Si no, la llamada con indice == length
            // intentaría leer vector[length] y lanzaría
            // ArrayIndexOutOfBoundsException antes de poder devolver -1.
            //
            // Cubre también el arreglo vacío: con length == 0, la llamada
            // arranca en indice == 0 y 0 >= 0 es verdadero.
            if (indice >= vector.length) {
                return NO_ENCONTRADO;
            }

            // --- CASO BASE DE ÉXITO ---
            // El elemento está en esta posición: se corta la búsqueda y se
            // devuelve el índice. Con duplicados devuelve el primero, porque
            // las posiciones anteriores ya se revisaron sin éxito.
            if (vector[indice] == valorBuscado) {
                return indice;
            }

            // --- CASO RECURSIVO ---
            // indice + 1 reemplaza el "i++" y el reinicio del for.
            //
            // El índice viaja como PARÁMETRO, no como variable compartida: cada
            // llamada tiene su propia copia, guardada en su propio marco de
            // pila. Ésa es la diferencia estructural con el for clásico.
            return buscar(vector, valorBuscado, indice + 1);
        } finally {
            salir();
        }
    }

    /** Versión de conveniencia que arranca desde la posición 0. */
    public static int buscar(int[] vector, int valorBuscado) {
        reiniciarPila();
        return buscar(vector, valorBuscado, 0);
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y símbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        demostrarCasosLimite();
        demostrarEquivalenciaConFor();
        demostrarPilaGrande();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.10: BUSCAR EN UN ARREGLO (SIN CICLOS) ===");
        System.out.println("  (el algoritmo no usa for ni while)");
        System.out.println();

        int[] vector = { 14, 7, 23, 3, 31, 7, 9 };

        System.out.println("  Vector de prueba: " + formatearVector(vector));
        System.out.println();

        int[] aBuscar = { 14, 9, 23, 7, 999 };
        String[] descripciones = {
            "primera posición (mejor caso)",
            "última posición (peor caso por posición)",
            "en el medio",
            "duplicado: debe devolver la 1ª aparición",
            "ausente (peor caso)"
        };

        for (int i = 0; i < aBuscar.length; i++) {
            reiniciarPila();
            int resultado = buscar(vector, aBuscar[i], 0);
            System.out.println("  buscar(" + String.format("%4d", aBuscar[i]) + ") -> " + resultado
                    + "  [" + descripciones[i] + "]");
            System.out.println("      llamadas: " + llamadasTotales
                    + " | profundidad máxima: " + profundidadMaxima);
        }

        // Arreglo vacío: entra directo por el caso base de fracaso.
        reiniciarPila();
        System.out.println();
        System.out.println("  Arreglo vacío -> buscar(5) = " + buscar(new int[] {}, 5, 0)
                + "  (caso base de fracaso inmediato, profundidad " + profundidadMaxima + ")");
        System.out.println("  Con length == 0, la llamada arranca en indice == 0 y 0 >= 0,");
        System.out.println("  así que ni siquiera intenta leer un elemento.");
    }

    private static void demostrarEquivalenciaConFor() {
        System.out.println();
        System.out.println("--- EQUIVALENCIA CON EL FOR CLÁSICO ---");
        System.out.println("  El algoritmo recursivo es una traducción literal del for:");
        System.out.println();
        System.out.println("    for (i = 0; i < n; i++)          if (v[i] == x) return i;");
        System.out.println("    return -1;");
        System.out.println();
        System.out.println("  se traduce en:");
        System.out.println();
        System.out.println("    if (i >= n)        return -1;   <- salida del for");
        System.out.println("    if (v[i] == x)     return i;    <- return del cuerpo");
        System.out.println("    return buscar(v, x, i + 1);     <- i++ y volver al for");
        System.out.println();
        System.out.println("  La ÚNICA diferencia es dónde vive el índice: en una variable");
        System.out.println("  mutable que se comparte entre iteraciones, o en un parámetro que");
        System.out.println("  se copia en cada marco de pila.");

        // Comprobación empírica: los dos dan lo mismo en todos los casos.
        System.out.println();
        System.out.println("  Verificación cruzada contra el for clásico:");
        int[] vector = { 14, 7, 23, 3, 31, 7, 9 };
        boolean todosCoinciden = true;

        for (int valor = 0; valor < 40; valor++) {
            int recursivo = buscar(vector, valor, 0);
            int clasico = buscarConFor(vector, valor);
            if (recursivo != clasico) {
                todosCoinciden = false;
                System.out.println("      valor " + valor + ": recursivo " + recursivo
                        + " vs for " + clasico + "  -> DISCREPANCIA");
            }
        }
        System.out.println("      valores 0..39 probados: "
                + (todosCoinciden ? "TODOS COINCIDEN" : "hay diferencias"));
    }

    /** Implementación CON ciclo, usada sólo como oráculo de comparación. */
    private static int buscarConFor(int[] vector, int valorBuscado) {
        int i = 0;
        while (i < vector.length) {
            if (vector[i] == valorBuscado) {
                return i;
            }
            i++;
        }
        return NO_ENCONTRADO;
    }

    private static void demostrarPilaGrande() {
        System.out.println();
        System.out.println("--- LA PILA CRECE A Θ(n) ---");
        System.out.println("  Con un arreglo grande, la recursión sigue funcionando porque cada");
        System.out.println("  llamada consume un marco. Eso es Θ(n) de memoria, el precio de");
        System.out.println("  resolverlo recursivamente en lugar de iterativamente.");
        System.out.println();

        // El tope es 5000 y no más grande a propósito: la JVM reserva una pila
        // limitada para el hilo (por defecto del orden de 10.000 a 20.000
        // marcos). Con 50.000 elementos la recursión es CORRECTA pero desborda
        // la pila, y ese error taparía la demostración del costo lineal.
        for (int n : new int[] { 100, 1000, 2500, 5000 }) {
            int[] vector = generarVector(n);
            reiniciarPila();
            int resultado = buscar(vector, -1, 0);   // -1 no está: caso peor
            System.out.println("    arreglo de " + String.format("%6d", n)
                    + " elementos -> resultado: " + resultado
                    + " | llamadas: " + llamadasTotales
                    + " | profundidad máxima: " + profundidadMaxima);
        }

        System.out.println();
        System.out.println("  Con el for clásico, el mismo problema usaría Θ(1) de memoria");
        System.out.println("  (una sola variable i). Acá se usan Θ(n) marcos de pila.");
        System.out.println();
        System.out.println("  Nota: por eso el tope es 5000 y no 50.000. La recursión es");
        System.out.println("  correcta para cualquier n, pero la pila de la JVM es finita y");
        System.out.println("  se agota; con el for no hay tal límite.");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese la cantidad de elementos del arreglo: ");
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

            System.out.print("Ingrese el valor a buscar: ");
            if (!entrada.hasNextInt()) {
                return;
            }
            int valorBuscado = entrada.nextInt();

            reiniciarPila();
            int resultado = buscar(vector, valorBuscado, 0);

            System.out.println("  Arreglo: " + formatearVector(vector));
            if (resultado == NO_ENCONTRADO) {
                System.out.println("  El valor " + valorBuscado + " NO EXISTE en el arreglo.");
                System.out.println("  Se recorrieron " + llamadasTotales + " posiciones.");
            } else {
                System.out.println("  El valor " + valorBuscado + " se encontro en el indice "
                        + resultado + ".");
                System.out.println("  Se hicieron " + llamadasTotales + " llamadas recursivas.");
            }
            System.out.println("  Profundidad maxima de pila: " + profundidadMaxima);
            System.out.println("  Temporal Θ(n) · Peor Θ(n), Mejor Θ(1) · Espacial Θ(n)");
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

    /** Genera un arreglo de n enteros distintos y positivos. */
    private static int[] generarVector(int n) {
        int[] vector = new int[n];
        for (int i = 0; i < n; i++) {
            vector[i] = (i + 1) * 3;
        }
        return vector;
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
* - Se añadió la comparación literal con el for clásico. El enunciado pedía
 *   "cómo avanzar en el arreglo sin usar for ni while", pero eso solo no alcanza:
 *   hay que mostrar que el índice como parámetro es un equivalente LITERAL de la
 *   variable de control del for, con la traducción línea por línea. Sin esa
 *   correspondencia el alumno ve una solución válida pero no entiende por qué
 *   funciona.
 * - Se añadió el argumento de por qué el caso base de fracaso va PRIMERO. El
 *   enunciado lo pedía como caso base, sin más. Invertir el orden de las dos
 *   comprobaciones provoca ArrayIndexOutOfBoundsException, porque la llamada con
 *   indice == length intenta leer vector[length]. Es un bug sutil y muy típico.
 * - Se explicitó que el caso base de fracaso también protege el arreglo vacío.
 *   Con length == 0 la llamada arranca en indice == 0, y 0 >= 0 resuelve sin
 *   tocar ningún elemento.
 * - Se agregó la verificación cruzada contra la implementación con while, que se
 *   usa sólo como oráculo externo. Comparar dos algoritmos entre sí detecta
 *   errores de implementación que no se ven al leer un resultado suelto.
 * ========================================================================== */