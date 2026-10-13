/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que imprima un conteo
 * regresivo desde n hasta 0.
 *
 * Este problema puede resolverse recursivamente porque cada paso del conteo es
 * "imprimir el número actual y después hacer lo mismo con el siguiente". No hace
 * falta ninguna variable que acumule el estado: el estado está en el número que
 * se pasa como parámetro.
 *
 * El caso base es n < 0, que simplemente no imprime nada y retorna, porque ya
 * se llegó a 0 y no hay nada más que mostrar.
 *
 * El caso recursivo es imprimir(n); descendente(n - 1);, porque en cada llamada
 * el número se acerca en uno al caso base.
 *
 * La función debe devolver void: no devuelve un valor, produce un efecto
 * (imprime en pantalla). Es la diferencia con los demás ejercicios, y vale la
 * pena explicarla: la recursión no obliga a devolver nada; sólo obliga a que
 * cada llamada se acerque al caso base.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo y la pila de llamadas.
 *
 * Y quiero una demostración importante: implementá también una versión que en
 * lugar de llamar con n - 1 llame con n + 1. Esa versión nunca alcanza el caso
 * base (el caso base pide n < 0, pero desde un número negativo n + 1 vuelve a
 * ser no negativo), así que la pila crece sin límite hasta producir un
 * StackOverflowError. Capturalo con try/catch y explicá en un comentario por qué
 * la recursión se desborda: no es un error del lenguaje, es la consecuencia de
 * que no exista ningún camino que llegue al caso base.
 *
 * También quiero probarlo con estos casos:
 *   - n = 0 -> debe imprimir sólo 0
 *   - n = 3 -> 3, 2, 1, 0
 *   - n negativo -> no imprime nada
 *   - la versión con n + 1 -> debe mostrar el StackOverflowError capturado
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 5: Conteo regresivo
 *
 * ESTRATEGIA — por qué es recursivo
 * El conteo regresivo es "imprimir n, y después repetir con n-1". Esa
 * descripción YA es una recursión: no hace falta ninguna variable de control
 * porque el estado del problema (qué número va) viaja como parámetro.
 *
 * CASO BASE — n < 0 -> no imprime nada y retorna
 * Cuando el número llega por debajo de 0 ya se imprimieron todos los que
 * correspondían. La función simplemente termina.
 * Se usa `n < 0` y no `n == 0` para que el 0 se imprima: si el corte fuera en
 * `n == 0`, el 0 nunca se mostraría.
 *
 * LA FUNCIÓN NO DEVUELVE NADA (void)
 * Ésta es la diferencia con los ejercicios 1 a 4. Ahí el valor de retorno
 * transportaba el resultado y había que "desenrollar" la pila para acumularlo.
 * Acá no hay resultado que devolver: el efecto ocurre AL DESENDERROLARSE,
 * cuando cada marco retorna e imprime el número que tenía guardado.
 *
 * El orden de impresión es n, n-1, ..., 0 (de mayor a menor) porque se imprime
 * ANTES de llamar recursivamente. Si se invirtiera el orden, el número se
 * imprimiría después de que la llamada interna retornara, y saldría 0, 1, 2, 3.
 *
 * CASO RECURSIVO — imprimir(n); descendente(n - 1);
 * Dos sentencias: la primera imprime, la segunda desciende un nivel. El
 * problema se reduce en uno en cada llamada.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n-1) + Θ(1)
 *   Temporal: Θ(n) — n llamadas, cada una con una impresión.
 *   Espacial: Θ(n) — n marcos de pila.
 *
 * PILA DE LLAMADAS
 * Con n = 3: se apilan los marcos de 3, 2, 1 y 0. El de 0 imprime "0" y
 * retorna; al retornar el de 1 imprime "1"; luego el de 2 imprime "2"; luego
 * el de 3 imprime "3". Cada marco esperó a todos los que tenía debajo antes de
 * imprimir. Esa es la razón del orden inverso al de la pila.
 *
 * ⚠️ DEMOSTRACIÓN CRÍTICA — LA RECURSIÓN QUE NO TERMINA
 * La versión `descendente(n)` que llama con `n + 1` NUNCA alcanza el caso base:
 * el caso base exige `n < 0`, pero desde un número negativo, `n + 1` es no
 * negativo otra vez. No hay ningún camino que llegue a `n < 0`, así que cada
 * llamada apila un marco nuevo sin límite. Cuando la pila agota la memoria
 * reservada para el hilo, la JVM lanza `StackOverflowError`.
 *
 * No es un bug del lenguaje: es exactamente lo que la ausencia de un camino al
 * caso base produce. Todo algoritmo recursivo correcto tiene, por definición,
 * que garantizar que el caso base es alcanzable.
 *
 * CASOS LÍMITE
 * - n = 0 -> imprime sólo 0
 * - n negativo -> no imprime nada
 * - n muy grande (por ejemplo 100000) -> la versión correcta imprime 100001
 *   líneas; la versión con n+1 desborda la pila mucho antes
 */
public class Ejercicio2_5 {

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
     * Imprime el conteo regresivo desde n hasta 0.
     * No devuelve valor: el resultado es el efecto mismo de imprimir.
     *
     * @param n número inicial del conteo
     */
    public static void descendente(int n) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // Ya se imprimió todo lo que correspondía. Se retorna sin imprimir.
            //
            // La condición es n < 0 y no n == 0 a propósito: con n == 0 el cero
            // NUNCA se imprimiría, porque el caso base cortaría antes de llegar
            // a la línea del println.
            if (n < 0) {
                return;
            }

            // --- CASO RECURSIVO ---
            // Se imprime ANTES de recursionar. Por eso el orden de salida es
            // descendente (n, n-1, ... 0) y no ascendente: cada marco imprime
            // cuando las llamadas que tenía debajo ya terminaron.
            System.out.println("  " + n);
            descendente(n - 1);
        } finally {
            salir();
        }
    }

    // ============================================================
    //  ALGORITMO — LA VERSIÓN QUE NO TERMINA
    // ============================================================

    /**
     * Versión defectuosa a propósito: llama con n + 1 en lugar de n - 1.
     *
     * El caso base exige `n < 0`, pero `n + 1` desde un negativo devuelve un
     * número no negativo, así que nunca se cumple. Cada llamada apila un marco
     * sin límite hasta que la JVM agota la pila del hilo y lanza
     * StackOverflowError.
     *
     * Sirve para demostrar que la alcanzabilidad del caso base no es un detalle
     * de estilo: es la condición de existencia del algoritmo.
     */
    public static void descendenteInfinito(int n) {
        entrar();
        try {
            if (n < 0) {
                return;   // NUNCA se alcanza: n + 1 nunca produce un negativo
            }
            // El error: se avanza en dirección contraria, alejándose del 0.
            descendenteInfinito(n + 1);
        } finally {
            salir();
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y símbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        demostrarCasosLimite();
        demostrarDesbordeDePila();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.5: CONTEO REGRESIVO ===");
        System.out.println();

        // Caso límite: n = 0 debe imprimir SÓLO el 0.
        reiniciarPila();
        System.out.println("  --- descendente(0): debe imprimir sólo 0 ---");
        descendente(0);
        System.out.println("      llamadas: " + llamadasTotales
                + " | profundidad máxima: " + profundidadMaxima);

        // Caso típico.
        reiniciarPila();
        System.out.println();
        System.out.println("  --- descendente(5) ---");
        descendente(5);
        System.out.println("      llamadas: " + llamadasTotales
                + " | profundidad máxima: " + profundidadMaxima);

        // Caso límite: n negativo no imprime nada.
        reiniciarPila();
        System.out.println();
        System.out.println("  --- descendente(-3): no debe imprimir nada ---");
        descendente(-3);
        System.out.println("      llamadas: " + llamadasTotales
                + " | profundidad máxima: " + profundidadMaxima + " (caso base inmediato)");

        System.out.println();
        System.out.println("  Con n = 5 se apilan 6 marcos (de 5 hasta 0). Cada uno imprime");
        System.out.println("  cuando retorna, por eso el orden es 5, 4, 3, 2, 1, 0 y no al revés.");
    }

    private static void demostrarDesbordeDePila() {
        System.out.println();
        System.out.println("=== DEMOSTRACIÓN: LA RECURSIÓN QUE NUNCA TERMINA ===");
        System.out.println();
        System.out.println("  La versión correcta llama con (n - 1), que ACORDA números hacia");
        System.out.println("  el 0 y termina. Comparemos qué pasa con (n + 1), que se ALEJA del 0:");
        System.out.println();
        System.out.println("    descendente(n)      -> n, n-1, ..., 1, 0  -> alcanza n < 0  -> termina");
        System.out.println("    descendente(n + 1)  -> n, n+1, n+2, ...        -> nunca n < 0   -> NO termina");
        System.out.println();

        reiniciarPila();
        System.out.println("  Ejecutando descendenteInfinito(0) ... (el programa no se cuelga,");
        System.out.println("  la JVM detecta el desbordamiento y lanza una excepción)");
        System.out.println();

        try {
            descendenteInfinito(0);
            System.out.println("  !!! No debería llegar acá: la función tiene que desbordar.");
        } catch (StackOverflowError error) {
            System.out.println("  *** StackOverflowError CAPTURADO ***");
            System.out.println("  Llamadas recursivas realizadas: " + llamadasTotales);
            System.out.println("  Profundidad máxima de pila:     " + profundidadMaxima);
            System.out.println();
            System.out.println("  POR QUÉ PASA: el caso base exige n < 0, pero la llamada usa n + 1.");
            System.out.println("  Desde un número negativo, n + 1 vuelve a ser no negativo, así que");
            System.out.println("  no existe NINGÚN camino que llegue al caso base. Cada llamada apila");
            System.out.println("  un marco sin límite hasta agotar la memoria reservada al hilo.");
            System.out.println();
            System.out.println("  No es un error del lenguaje: es la consecuencia directa de que el");
            System.out.println("  caso base sea inalcanzable. Todo algoritmo recursivo correcto debe");
            System.out.println("  garantizar que cada llamada reduce el problema hacia el caso base.");
        }
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese el numero inicial del conteo (EOF para salir): ");
            while (entrada.hasNextInt()) {
                int n = entrada.nextInt();
                reiniciarPila();

                System.out.println("  --- descendente(" + n + ") ---");
                descendente(n);
                System.out.println("      llamadas: " + llamadasTotales
                        + " | profundidad máxima: " + profundidadMaxima);

                System.out.print("Ingrese el numero inicial del conteo (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué sekaya cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se agregó la demostración del StackOverflowError como requisito explícito, y
 *   no como comentario. El enunciado pedía "qué pasaría si en lugar de n - 1 se
 *   usara n + 1": responderlo sólo con una explicación teórica deja al
 *   estudiante creyendo que nunca lo va a ver.arlo cuesta nada (un try/catch) y
 *   es la evidencia de que un caso base inalcanzable es un fallo real.
 * - Se aclaró por qué el caso base es "n < 0" y no "n == 0". Con n == 0 el cero
 *   nunca se imprimiría, porque el caso base cortaría antes de llegar al
 *   println. Es un error sutil que la consigna no explicaba.
 * - Se documentó que la función devuelve void y por qué eso no la vuelve menos
 *   recursiva. Es la diferencia estructural con los ejercicios 1 a 4, y el
 *   enunciado la pedía explícitamente ("qué significa que la función no
 *   necesariamente devuelva un valor").
 * - Se detalló el orden de impresión descendente y su causa: se imprime antes
 *   de recursivar, y como cada marco imprime al retornar, la salida queda de
 *   mayor a menor.
 * ========================================================================== */
