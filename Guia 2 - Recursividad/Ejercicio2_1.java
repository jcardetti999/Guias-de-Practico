/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que calcule el factorial de
 * un número entero no negativo.
 *
 * Este problema puede resolverse recursivamente porque el factorial se define
 * en términos de sí mismo: n! no es un artificio, es n * (n-1)!. Es
 * literalmente la definición matemática del número, así que la estructura de la
 * definición ES la estructura de la solución.
 *
 * El caso base es n <= 1, que devuelve 1, porque por convenio 0! = 1 y 1! = 1.
 * Sin ese caso base la función no terminaría nunca: siempre llamaría a
 * factorial(n - 1) con un número menor, sin un punto donde frenar.
 *
 * El caso recursivo es factorial(n) = n * factorial(n - 1), porque en cada
 * llamada el problema se reduce en uno: pasamos de n! a (n-1)!.
 *
 * La función debe devolver el factorial como long, no como int: el factorial
 * crece muy rápido y 13! = 6227020800 ya supera el máximo de int
 * (2^31 - 1 = 2147483647).
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo y la pila de llamadas (por qué la profundidad crece hasta n).
 *
 * También quiero probarlo con estos casos:
 *   - 0! = 1 y 1! = 1 (los dos casos base)
 *   - 5! = 120 (caso típico)
 *   - 12! = 479001600 — el último factorial que entra en int
 *   - 13! = 6227020800 — el primero que desborda int pero entra en long
 *   - 20! — el máximo que entra en long
 *   - un número negativo -> debe informar que no es válido, sin lanzar excepción
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 1: Factorial
 *
 * ESTRATEGIA — por qué es recursivo
 * El factorial no es un problema al que se le busque una descomposición
 * artificial: SU DEFINICIÓN es n! = n · (n-1)!. La recurrencia aparece por sí
 * sola, y por eso la función recursiva es la traducción literal de la
 * definición.
 *
 * CASO BASE — n <= 1 -> 1
 * Son DOS valores distintos (0 y 1) que comparten el mismo resultado porque
 * ambos valen 1 por convenio. Se pueden escribir como dos condiciones
 * separadas, pero se unen en `n <= 1` porque el efecto es idéntico.
 * Si `n = 1` devolviera `n` en lugar de `1`, el resultado sería el mismo por
 * casualidad; con `n = 0` devolvería 0, que es INCORRECTO. Unificar en `n <= 1`
 * con retorno 1 cubre los dos casos sin depender de esa casualidad.
 *
 * CASO RECURSIVO — factorial(n) = n * factorial(n - 1)
 * El problema se reduce en exactamente uno: de n! pasamos a (n-1)!.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n-1) + Θ(1)
 * Se despliega:  T(n) = T(n-1) + T(n-2) + ... + T(1) + n·Θ(1)
 * Al develops, hay n llamadas y cada una cuesta Θ(1).
 *   Temporal: Θ(n) — el algoritmo lineal, no se puede hacer mejor porque hay
 *             que leer los n factores.
 *   Espacial: Θ(n) — un marco de pila por llamada pendiente.
 *
 * PILA DE LLAMADAS
 * Cada llamada reserva un marco que contiene: el parámetro n, el valor
 * intermedio de n * (resultado pendiente) y la dirección de retorno. La llamada
 * con n=5 apila el marco de 5, que llama a la de 4, que apila su marco, y así
 * hasta 1, que es el caso base. Hay 5 marcos APILADOS en el punto más profundo.
 * Luego se DESENROLLA: el marco de 1 devuelve 1, el de 4 lo multiplica por 4, el
 * de 5 lo multiplica por 5 y devuelve 120. Cada marco se descarta al retornar.
 *
 * IMPORTANTE — Java NO optimiza la recursión de cola
 * Una función cuya última instrucción es `return f(n - 1)` sigue reservando su
 * marco: Java no aplica TCO (tail call optimization). Por eso el Θ(n) espacial
 * se mantiene incluso si el caso recursivo se escribe "en cola".
 *
 * CASOS LÍMITE
 * - n = 0 y n = 1 -> 1 (caso base)
 * - n negativo -> se devuelve -1 como valor centinela, no se lanza excepción
 * - Desborde: con int, 13! desborda. Con long, 20! es el máximo.
 */
public class Ejercicio2_1 {

    /** Valor centinela devuelto cuando el número no es válido (n < 0). */
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
     * Calcula el factorial de n mediante recursión.
     *
     * @param n número entero no negativo
     * @return n! como long, o NO_VALIDO (-1) si n es negativo
     */
    public static long factorial(int n) {
        entrar();   // se apila un marco nuevo
        try {
            // La guarda de negativos va ANTES del caso base. Sin ella, un n
            // negativo como -5 entraría por `n <= 1` y devolvería 1, que es un
            // factorial inexistente presentado como si fuera válido.
            if (n < 0) {
                return NO_VALIDO;
            }

            // --- CASO BASE ---
            // La condición es n <= 1 y no n == 1: ambos valores (0 y 1) valen 1
            // por convenio, así que un único `return 1L` cubre los dos casos sin
            // depender de que `return n` dé la misma casualidad.
            if (n <= 1) {
                return 1L;
            }

            // --- CASO RECURSIVO ---
            // Se reduce el problema en uno. Al volver de la llamada, este marco
            // multiplica por n y devuelve el resultado.
            //
            // OJO con el orden de evaluación: primero se resuelve
            // factorial(n - 1) — que apila n-1 marcos adicionales — y recién
            // después se multiplica por n. Java evalúa los operandos antes de
            // aplicar el operador, así que la multiplicación queda "en el marco
            // de afuera", esperando el resultado de la llamada interna.
            return (long) n * factorial(n - 1);
        } finally {
            salir();   // el marco se descarta en el unwind, pase lo que pase
        }
    }

    /**
     * Versión instrumentada que además devuelve el número de llamadas, para
     * poder mostrar la profundidad de la pila en la demostración.
     */
    public static long factorialConEstadisticas(int n) {
        reiniciarPila();
        long resultado = factorial(n);
        // profundidadMaxima y llamadasTotales quedaron poblados por factorial().
        return resultado;
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
        System.out.println("=== EJERCICIO 2.1: FACTORIAL ===");
        System.out.println();

        int[][] casos = {
            { 0 },     // caso base
            { 1 },     // caso base
            { 5 },     // típico
            { 10 },
            { 20 }     // máximo representable en long
        };

        for (int[] caso : casos) {
            int n = caso[0];
            reiniciarPila();
            long resultado = factorial(n);

            System.out.println("  factorial(" + n + ") = " + resultado);
            System.out.println("      llamadas recursivas: " + llamadasTotales
                    + " | profundidad maxima de pila: " + profundidadMaxima);
            System.out.println("      relacion profundidad/llamadas = "
                    + (llamadasTotales == 0 ? 0 : (double) profundidadMaxima / llamadasTotales)
                    + "  -> Θ(n) espacio, Θ(n) tiempo");
        }

        System.out.println();
        System.out.println("  Verificacion: llamadas == profundidad maxima en todos los casos,");
        System.out.println("  es decir la recursion es puramente lineal (una rama por nivel).");
    }

    private static void demostrarDesborde() {
        System.out.println();
        System.out.println("--- DESBORDE DE TIPO ---");

        // 12! = 479001600 -> todavia entra en int
        reiniciarPila();
        long f12 = factorial(12);
        System.out.println("  12! = " + f12 + " -> cabe en int (" + ((int) f12) + ")");

        // 13! = 6227020800 -> NO entra en int
        reiniciarPila();
        long f13 = factorial(13);
        int f13ComoInt = (int) f13;   // trunciona: elOverflow es visible
        System.out.println("  13! = " + f13 + " -> como int queda " + f13ComoInt
                + " (WRAP: " + f13 + " - 2^32 = " + (f13 - 4294967296L) + ")");
        System.out.println("  Por eso la funcion devuelve long y no int.");
        System.out.println("  Con long el maximo es 20! = " + factorial(20));

        // El caso inválido se maneja con centinela, no con excepción.
        reiniciarPila();
        System.out.println();
        System.out.println("  factorial(-5) = " + factorial(-5)
                + "  (NO_VALIDO: sin excepción, profundidad " + profundidadMaxima + ")");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese un numero entero (EOF para salir): ");
            while (entrada.hasNextInt()) {
                int n = entrada.nextInt();
                reiniciarPila();
                long resultado = factorial(n);

                if (n < 0) {
                    System.out.println("  " + n + " no es valido: el factorial solo existe"
                            + " para numeros no negativos.");
                } else {
                    System.out.println("  factorial(" + n + ") = " + resultado);
                }
                System.out.println("      profundidad maxima de pila: " + profundidadMaxima
                        + " (una por nivel de recursion)");
                System.out.print("Ingrese un numero entero (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se agregó el caso "n negativo" al prompt: el enunciado pedía "qué ocurre
 *   cuando n vale 0 o 1", y calcular un factorial negativo no está definido
 *   matemáticamente. La función devuelve un centinela (-1) en lugar de
 *   propagar un error silencioso o lanzar una excepción. Documentarlo evita que
 *   el caso se lea como un descuido.
 * - Se aclaró que el caso base es "n <= 1" y no "n == 1": la primera versión del
 *   prompt decía sólo n == 1, lo que dejaba el 0 sin cubrir explícitamente.
 * ========================================================================== */
