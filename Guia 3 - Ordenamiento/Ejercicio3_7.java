/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un Quicksort sobre un arreglo YA ORDENADO, usando
 * siempre el primer elemento como pivote, y contar cuántas llamadas recursivas
 * realiza.
 *
 * ALGORITMO ELEGIDO
 * Quicksort con pivote = primer elemento, partición de Hoare. Es exactamente el
 * mismo algoritmo del ejercicio anterior, pero aplicado al PEOR caso.
 *
 * RAZONAMIENTO — por qué el arreglo ordenado es el peor caso
 * Quicksort funciona bien en promedio porque, en un arreglo aleatorio, el primer
 * elemento tiene aproximadamente la mediana como valor: la partición divide el
 * rango en dos mitades parecidas.
 *
 * En un arreglo YA ORDENADO, el primer elemento es siempre el MÍNIMO. Entonces:
 *   - al particionar, todo lo demás queda a la DERECHA del pivote;
 *   - el subarreglo izquierdo queda VACÍO (no hay nada menor que el mínimo);
 *   - el subarreglo derecho tiene n-1 elementos.
 *
 * La partición no divide: REBANA. Cada llamada recursiva quita un solo elemento
 * del rango, así que hay n-1 llamadas y la profundidad llega a n-1. El costo
 * es Θ(n²) en tiempo y Θ(n) en pila de recursión.
 *
* Es el mismo problema que sufre una recursión que avanza de a uno en vez de
 * dividirse a la mitad.
 *
 * DATOS QUE RECIBE
 * Un arreglo de int[] leído por consola. La demostración usa el ejemplo del
 * enunciado {1, 2, 3, 4, 5, 6, 7} y compara contra el caso aleatorio.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * La cantidad de llamadas recursivas y la profundidad de pila, que es lo que
 * pide el enunciado. Se agrega la comparación con el caso promedio para que se
 * vea la diferencia.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * Por qué la partición no divide, por qué el pivote siempre es el mínimo, y la
 * comparación con el caso promedio. También el riesgo de StackOverflowError.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo Arrays.sort. Y demostrar que con un arreglo grande la
 * recursión desborda la pila, capturando el StackOverflowError.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 7: Peor caso de Quicksort
 *
 * ALGORITMO — Quicksort con pivote = primer elemento, partición de Hoare.
 *
 * RAZONAMIENTO — por qué el arreglo ordenado es el PEOR caso
 * En un arreglo aleatorio, el primer elemento es aproximadamente la mediana, así
 * que la partición genera dos mitades de tamaño parecido y la recursión tiene
 * profundidad Θ(log n).
 *
 * En un arreglo YA ORDENADO, el primer elemento es siempre el MÍNIMO:
 *   - todo lo demás es >= pivote, así que queda a la derecha;
 *   - el subarreglo izquierdo tiene 0 elementos (nada es menor que el mínimo);
 *   - el subarreglo derecho tiene n-1 elementos.
 *
 * La partición no divide: recorta UN elemento por llamada. Con n elementos hay
 * n-1 llamadas y la profundidad máxima llega a n-1.
 *
 * COMPLEJIDAD
 *                    Caso promedio        Peor caso (ordenado)
 *   Tiempo           Θ(n log n)           Θ(n²)
 *   Pila             Θ(log n)             Θ(n)
 *   Llamadas         ~2n log n            n-1
 *
 * En ambos casos el resultado es correcto. Lo que cambia es el COSTO: el peor
 * caso es Θ(n²) frente a Θ(n log n) promedio.
 *
 * ES EL MISMO FENÓMENO QUE EL EJERCICIO 5 DE LA GUÍA 2
 * Allá, `descendente(n+1)` nunca alcanzaba el caso base y desbordaba la pila.
 * Acá la recursión SÍ termina, pero avanza de a uno en vez de dividirse a la
 * mitad: mismo problema de fondo, distinta manifestación.
 *
 * INSTRUMENTACIÓN
 *   comparaciones, llamadas recursivas y profundidad máxima de pila.
 *
 * CASOS LÍMITE
 *   - Arreglo vacío o de un elemento.
 *   - Arreglo grande: la recursión desborda la pila (~15 000 marcos en la JVM).
 *     Se captura el StackOverflowError.
 */
public class Ejercicio3_7 {

    // ============================================================
    //  INSTRUMENTACIÓN
    // ============================================================
    private static long comparaciones = 0;
    private static long llamadasRecursivas = 0;
    private static int profundidad = 0;
    private static int profundidadMaxima = 0;

    private static void reiniciarContadores() {
        comparaciones = 0;
        llamadasRecursivas = 0;
        profundidad = 0;
        profundidadMaxima = 0;
    }

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el rango [bajo, alto] con Quicksort, pivote = vector[bajo].
     *
     * @param vector arreglo a ordenar; se modifica en el sitio
     * @param bajo   índice inicial del rango
     * @param alto   índice final del rango
     */
    public static void quicksort(int[] vector, int bajo, int alto) {
        llamadasRecursivas++;
        profundidad++;
        if (profundidad > profundidadMaxima) {
            profundidadMaxima = profundidad;
        }

        try {
            if (alto <= bajo) {
                return;
            }

            int puntoDeCorte = particionar(vector, bajo, alto);

            quicksort(vector, bajo, puntoDeCorte - 1);
            quicksort(vector, puntoDeCorte + 1, alto);
        } finally {
            profundidad--;
        }
    }

    /**
     * Partición de Lomuto con el pivote tomado del PRIMER elemento del rango.
     * Devuelve la posición definitiva del pivote, siempre dentro de [bajo, alto].
     */
    private static int particionar(int[] vector, int bajo, int alto) {
        // Se lleva el pivote (primer elemento) al final del rango: es lo que
        // permite que la partición devuelva siempre un índice válido y, con él,
        // que las dos llamadas recursivas reciban rangos estrictamente más
        // chicos.
        int pivote = vector[bajo];
        vector[bajo] = vector[alto];
        vector[alto] = pivote;

        int i = bajo;

        for (int j = bajo; j < alto; j++) {
            comparaciones++;
            if (vector[j] <= pivote) {
                if (i != j) {
                    int auxiliar = vector[i];
                    vector[i] = vector[j];
                    vector[j] = auxiliar;
                }
                i++;
            }
        }

        if (i != alto) {
            int auxiliar = vector[i];
            vector[i] = vector[alto];
            vector[alto] = auxiliar;
        }

        return i;
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
        demostrarComparacionConPromedio();
        demostrarDesbordeDePila();
        ejecutarInteractivo();
    }

    private static void demostrarEjemploDelEnunciado() {
        System.out.println("=== EJERCICIO 3.7: PEOR CASO DE QUICKSORT ===");
        System.out.println();

        int[] vector = { 1, 2, 3, 4, 5, 6, 7 };
        System.out.println("  Arreglo del enunciado: " + formatearVector(vector));
        System.out.println("  Pivote = primer elemento = " + vector[0]
                + ", que es el MINIMO del arreglo.");
        System.out.println();

        reiniciarContadores();
        quicksort(vector, 0, vector.length - 1);

        int n = 7;
        System.out.println("  Llamadas recursivas : " + llamadasRecursivas
                + "  (esperado: n-1 = " + (n - 1) + ")");
        System.out.println("  Profundidad maxima  : " + profundidadMaxima
                + "  (esperado: n-1 = " + (n - 1) + ")");
        System.out.println("  Comparaciones       : " + comparaciones);
        System.out.println("  Arreglo final       : " + formatearVector(vector));

        System.out.println();
        System.out.println("  POR QUÉ: el pivote es siempre el mínimo, así que el subarreglo");
        System.out.println("  izquierdo queda VACÍO y el derecho recibe n-1 elementos. La");
        System.out.println("  partición no divide: recorta UN elemento por llamada.");
        System.out.println("  Consecuencia: la recursion es lineal, no logarítmica.");

        int[] oraculo = { 1, 2, 3, 4, 5, 6, 7 };
        java.util.Arrays.sort(oraculo);
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort (oraculo externo): "
                + (sonIguales(vector, oraculo) ? "CORRECTO" : "DISCREPANCIA"));
        System.out.println("  El RESULTADO es correcto siempre. Lo que cambia es el COSTO.");
    }

    private static void demostrarComparacionConPromedio() {
        System.out.println();
        System.out.println("--- PEOR CASO vs. CASO PROMEDIO ---");
        System.out.println();

        int[] ordenados = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[] aleatorios = { 4, 1, 8, 3, 6, 5, 7, 2 };

        reiniciarContadores();
        quicksort(copiar(ordenados), 0, ordenados.length - 1);
        long comparacionesPeor = comparaciones;
        long llamadasPeor = llamadasRecursivas;
        int profundidadPeor = profundidadMaxima;

        reiniciarContadores();
        quicksort(copiar(aleatorios), 0, aleatorios.length - 1);
        long comparacionesPromedio = comparaciones;
        long llamadasPromedio = llamadasRecursivas;
        int profundidadPromedio = profundidadMaxima;

        System.out.printf("  %-14s | %13s | %10s | %12s%n",
                "caso", "comparaciones", "llamadas", "profundidad");
        System.out.printf("  %-14s-+-%-13s-+-%-10s-+-%-12s%n",
                "--------------", "-------------", "----------", "------------");

        System.out.printf("  %-14s | %13d | %10d | %12d%n",
                "Ordenado", comparacionesPeor, llamadasPeor, profundidadPeor);
        System.out.printf("  %-14s | %13d | %10d | %12d%n",
                "Aleatorio", comparacionesPromedio, llamadasPromedio, profundidadPromedio);

        System.out.println();
        System.out.println("  Mismo n = " + ordenados.length + ", pero el arreglo ordenado hace");
        System.out.println("  MUCHO mas trabajo. En el aleatorio el primer elemento esta cerca de");
        System.out.println("  la mediana y la particion divide en dos; en el ordenado el pivote");
        System.out.println("  es el extremo y la particion recorta de a uno.");
        System.out.println();
        System.out.println("  Θ(n²) vs Θ(n log n). Con n = 1.000.000 son ~10¹² contra ~2·10⁷");
        System.out.println("  operaciones: la diferencia es de miles de veces.");
    }

    private static void demostrarDesbordeDePila() {
        System.out.println();
        System.out.println("--- EL MISMO PEOR CASO PUEDE DESBORDAR LA PILA ---");
        System.out.println();

        // n grande: la recursion es correcta, pero la pila de la JVM es finita.
        int n = 100000;
        int[] grande = new int[n];
        for (int i = 0; i < n; i++) {
            grande[i] = i + 1;   // ordenado: el peor caso
        }

        reiniciarContadores();
        System.out.println("  Ordenando un arreglo YA ORDENADO de " + n + " elementos...");

        try {
            quicksort(grande, 0, grande.length - 1);
            System.out.println("  Termino sin desbordar: " + llamadasRecursivas + " llamadas.");
        } catch (StackOverflowError error) {
            System.out.println();
            System.out.println("  *** StackOverflowError CAPTURADO ***");
            System.out.println("  Llamadas recursivas realizadas: " + llamadasRecursivas);
            System.out.println("  Profundidad de pila alcanzada  : " + profundidadMaxima);
            System.out.println();
            System.out.println("  POR QUÉ: en el peor caso la recursion tiene profundidad n, y");
            System.out.println("  la pila de la JVM es finita (~10.000 a 20.000 marcos). Cuando se");
            System.out.println("  agota, la JVM lanza StackOverflowError.");
            System.out.println();
            System.out.println("  El algoritmo es CORRECTO: lo que falla es el límite de memoria");
            System.out.println("  del hilo, no la lógica. Por eso los motores reales eligen el");
            System.out.println("  pivote de otra manera (mediana, aleatorio) para evitarlo.");
        }
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

            // Se avisa si el arreglo ya está ordenado, porque ése es el peor caso.
            boolean ordenado = estaOrdenado(vector);
            if (ordenado) {
                System.out.println("  AVISO: el arreglo YA ESTA ORDENADO. Con pivote = primer");
                System.out.println("  elemento, ese es el PEOR caso: la recursion no divide.");
            }

            reiniciarContadores();
            quicksort(vector, 0, vector.length - 1);

            System.out.println();
            System.out.println("  Final               : " + formatearVector(vector));
            System.out.println("  Comparaciones       : " + comparaciones);
            System.out.println("  Llamadas recursivas : " + llamadasRecursivas
                    + "  (n = " + cantidad + ", n-1 = " + (cantidad - 1) + ")");
            System.out.println("  Profundidad maxima  : " + profundidadMaxima);
            System.out.println("  Θ(n²) tiempo · Θ(n) pila en el peor caso");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** ¿El arreglo está ordenado de forma ascendente? */
    private static boolean estaOrdenado(int[] vector) {
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] < vector[i - 1]) {
                return false;
            }
        }
        return true;
    }

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
 * - Se agregó al prompt la demostración del StackOverflowError con n = 100.000.
 *   El enunciado pide contar llamadas recursivas, pero con el ejemplo chico
 *   {1,2,3,4,5,6,7} no queda claro que el peor caso tiene consecuencias
 *   prácticas. Con 100 000 elementos la recursion desborda la pila de verdad,
 *   y eso conecta el ejercicio con el fenómeno visto en la Guía 2.
 * - Se documentó en el prompt la comparación contra el caso promedio. El
 *   enunciado pide explicar por qué el pivote fijo puede generar mal
 *   rendimiento, pero "mal rendimiento" es relativo: sin mostrar el caso
 *   promedio como referencia, no hay forma de cuantificarlo.
 * ========================================================================== */