/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que calcule a^n sin usar
 * Math.pow.
 *
 * Este problema puede resolverse recursivamente porque una potencia es
 * multiplicación anidada: a^n es a multiplicado por sí mismo n veces, y al
 * quitarle una multiplicación queda exactamente a^(n-1).
 *
 * El caso base es n == 0, que devuelve 1, porque a^0 = 1 por definición:
 * elevar cualquier número a la potencia cero da uno (incluso 0^0, por convenio
 * matemático).
 *
 * El caso recursivo es potencia(a, n) = a * potencia(a, n - 1), porque en cada
 * llamada se reduce el exponente en uno, no la base.
 *
 * La función debe devolver long, porque 2^31 ya desborda int.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo, por qué el caso base es exponente 0 (y no base 0), y la pila de
 * llamadas.
 *
 * También quiero probarlo con estos casos:
 *   - a^0 = 1 para cualquier a, incluido 0^0 = 1 (caso base)
 *   - 0^n = 0 con n > 0
 *   - 1^n = 1 con cualquier n
 *   - 2^10 = 1024 (caso típico)
 *   - 2^31 — el primero que desborda int pero entra en long
 *   - exponente negativo -> debe informar que no es válido
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 4: Potencia
 *
 * ESTRATEGIA — por qué es recursivo
 * a^n significa multiplicar a por sí mismo n veces. Esa es una definición
 * repetitiva: la operación es la misma en todos los pasos y sólo cambia cuántas
 * veces falta hacerla. Se recurre porque el enunciado del problema ya tiene la
 * forma de una recurrencia.
 *
 * CASO BASE — n == 0 -> 1
 * a^0 = 1 por definición: multiplicar a por sí mismo CERO veces es no
 * multiplicar nada, y el neutro de la multiplicación es 1.
 *
 * POR QUÉ EL CASO BASE ES "EXPONENTE 0" Y NO "BASE 0"
 * Porque lo que se agota es el exponente, no la base. La base a es un dato fijo
 * que se repite; el exponente n es el contador de "cuántas veces falta". Por eso
 * el corte va en n.
 *
 * Además, si el caso base fuera a == 0, el algoritmo no terminaría para
 * a != 0, porque la base nunca cambia al recursionar: siempre sería la misma.
 * El que cambia es n.
 *
 * El convenio 0^0 = 1 sale naturalmente del caso base: con n = 0 se devuelve 1
 * sin mirar la base, así que 0^0 también da 1. Es el comportamiento esperado.
 *
 * CASO RECURSIVO — potencia(a, n) = a * potencia(a, n - 1)
 * Se reduce el exponente. La base viaja intacta a todos los niveles.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n-1) + Θ(1)
 *   Temporal: Θ(n) — n llamadas de multiplicación.
 *   Espacial: Θ(n) — n marcos de pila.
 *
 * PILA DE LLAMADAS
 * Con potencia(2, 4): se apilan los marcos de n=4, n=3, n=2, n=1 y n=0. El de
 * n=0 es el caso base y devuelve 1. Al desenrollarse: n=1 devuelve 2·1=2;
 * n=2 devuelve 2·2=4; n=3 devuelve 2·4=8; n=4 devuelve 2·8=16. Cada marco
 * esperó a todos los que tenía debajo.
 *
 * CASOS LÍMITE
 * - n = 0 -> 1 para cualquier a, incluido a = 0 (convenio 0^0 = 1)
 * - a = 0 con n > 0 -> 0, tras n llamadas
 * - a = 1 -> 1 con cualquier n
 * - n negativo -> centinela NO_VALIDO, sin excepción ni recursión infinita
 * - Desborde: con int, 2^31 desborda. Con long, 2^62 es el máximo positivo.
 */
public class Ejercicio2_4 {

    /** Valor centinela devuelto cuando el exponente es negativo. */
    public static final long NO_VALIDO = -1L;

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
     * Calcula a^n mediante recursión, sin usar `Math.pow`.
     *
     * @param a base
     * @param n exponente no negativo
     * @return a^n como long, o NO_VALIDO (-1) si n es negativo
     */
    public static long potencia(long a, int n) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // La guarda de n < 0 va primero: si sólo cortáramos en n == 0, un
            // exponente negativo reduciría cada vez más (n, n-1, n-2, ...) sin
            // tocar nunca el 0, y la recursión no terminaría.
            if (n < 0) {
                return NO_VALIDO;
            }

            // a^0 = 1: multiplicar cero veces es no hacer nada, y el neutro de
            // la multiplicación es 1. Da igual cuál sea la base: por eso este
            // caso base también resuelve 0^0 = 1 sin código extra.
            if (n == 0) {
                return 1L;
            }

            // --- CASO RECURSIVO ---
            // Se reduce el EXPONENTE, no la base. La base viaja intacta.
            //
            // La multiplicación queda en el marco de afuera: primero se resuelve
            // potencia(a, n-1), que apila n-1 marcos más, y recién al volver se
            // multiplica por a.
            return a * potencia(a, n - 1);
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
        demostrarDesborde();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.4: POTENCIA ===");
        System.out.println("  (el algoritmo no usa Math.pow)");
        System.out.println();

        long[][] casos = {
            { 5, 0 },     // caso base: a^0 = 1
            { 0, 0 },     // convenio 0^0 = 1
            { 0, 5 },     // 0^n = 0 con n > 0
            { 1, 7 },     // 1^n = 1
            { 2, 10 },    // típico
            { 3, 4 }
        };

        for (long[] caso : casos) {
            long a = caso[0];
            int n = (int) caso[1];
            reiniciarPila();
            long resultado = potencia(a, n);

            System.out.println("  potencia(" + a + ", " + n + ") = " + resultado);
            System.out.println("      llamadas: " + llamadasTotales
                    + " | profundidad máxima: " + profundidadMaxima
                    + "  (depende sólo de n, la base es un dato fijo)");
        }

        reiniciarPila();
        System.out.println();
        System.out.println("  potencia(2, -3) = " + potencia(2, -3)
                + "  (NO_VALIDO, sin recursión infinita)");

        System.out.println();
        System.out.println("  El caso base es el EXPONENTE 0 y no la base 0 porque lo que");
        System.out.println("  se agota es n. La base a nunca cambia al recursionar, así que");
        System.out.println("  un caso base sobre a nunca se alcanzaría.");
    }

    private static void demostrarDesborde() {
        System.out.println();
        System.out.println("--- DESBORDE DE TIPO ---");

        reiniciarPila();
        long p30 = potencia(2, 30);
        System.out.println("  2^30 = " + p30 + " -> cabe en int (" + ((int) p30) + ")");

        reiniciarPila();
        long p31 = potencia(2, 31);
        int p31ComoInt = (int) p31;
        System.out.println("  2^31 = " + p31 + " -> como int queda " + p31ComoInt
                + " (¡WRAP: el valor es negativo!)");
        System.out.println("  Máximo de int  = 2147483647 = 2^31 - 1");
        System.out.println("  Máximo de long = 9223372036854775807 = 2^63 - 1");

        reiniciarPila();
        System.out.println();
        System.out.println("  El mayor exponente que entra en long con base 2 es 62:");
        System.out.println("    2^62 = " + potencia(2, 62));
        System.out.println("    2^63 = " + potencia(2, 63) + " -> ya desborda long");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese la base (EOF para salir): ");
            while (entrada.hasNextLong()) {
                long a = entrada.nextLong();
                System.out.print("Ingrese el exponente: ");
                if (!entrada.hasNextInt()) {
                    break;
                }
                int n = entrada.nextInt();

                reiniciarPila();
                long resultado = potencia(a, n);

                if (n < 0) {
                    System.out.println("  El exponente no puede ser negativo.");
                } else {
                    System.out.println("  potencia(" + a + ", " + n + ") = " + resultado);
                    System.out.println("      profundidad maxima de pila: " + profundidadMaxima);
                }
                System.out.print("Ingrese la base (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * * - Se añadió la justificación de POR QUÉ el caso base es exponente 0 y no base
 *   0. El enunciado pedía "por qué el caso base es exponente 0", pero la razón
 *   útil es más fuerte: si el corte fuera sobre la base, nunca se alcanzaría,
 *   porque la base no cambia entre llamadas. Sólo el exponente se agota.
 * - Se añadió la prueba 0^0 = 1. No estaba en el enunciado, pero es el caso que
 *   mejor evidencia que el caso base NO mira la base: con n = 0 se devuelve 1
 *   siempre. Es una consecuencia, no un caso especial.
 * - Se documentó el límite de desborde (base 2 hasta exponente 62 con long),
 *   que el enunciado no pedía. Se añadió porque el enunciado sí decía "devolver
 *   long", y la razón de esa elección sólo queda clara mostrando dónde se corta.
 * - Se agregó la guarda "n < 0". El enunciado pedía "qué devuelve la función" en
 *   cada caso y sólo mencionaba el exponente negativo al final como aviso;
 *   restringir el corte a n == 0 haría que un exponente negativo nunca llegue
 *   al caso base.
 * ========================================================================== */
