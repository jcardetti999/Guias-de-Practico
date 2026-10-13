/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que sume los números desde n
 * hasta 1.
 *
 * Este problema puede resolverse recursivamente porque la suma de los primeros
 * n números es la última suma más la suma de los primeros n-1. Separar el
 * elemento más grande del resto es una descomposición natural, no forzada.
 *
 * El caso base es n <= 0, que devuelve 0, porque no hay números que sumar. Es
 * el caso más simple posible y es el único que no depende de n.
 *
 * El caso recursivo es suma(n) = n + suma(n - 1), porque en cada llamada el
 * problema se reduce: de sumar n números pasamos a sumar n-1.
 *
 * La función debe devolver la suma como int.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo y la pila de llamadas.
 *
 * También quiero probarlo con estos casos:
 *   - suma(0) = 0 y suma(1) = 1 (casos base)
 *   - suma(4) = 10 (1+2+3+4, caso típico)
 *   - suma(10) = 55
 *   - validación contra la fórmula cerrada n*(n+1)/2 para varios n: si el
 *     resultado recursivo coincide con la fórmula, el algoritmo es correcto
 *   - el límite de int: con n = 65535 la suma es 2147450880, el mayor valor
 *     representable; con n mayor hay que advertir del desborde
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 2: Suma de los primeros N números
 *
 * ESTRATEGIA — por qué es recursivo
 * La suma de 1 a n se parte en dos partes: el número n, y la suma de 1 a n-1.
 * Esa partición no es un truco, es la definición misma de una suma acumulada.
 * El resultado de la parte chica se reutiliza tal cual para construir el total.
 *
 * CASO BASE — n <= 0 -> 0
 * Cuando no queda ningún número por sumar, la respuesta es 0. Es el elemento
 * neutro de la suma: sumar 0 a cualquier cosa no la altera.
 * Se escribe `n <= 0` en vez de `n == 0` para que cualquier entrada no positiva
 * termine, en vez de entrar en una recursión infinita.
 *
 * CASO RECURSIVO — suma(n) = n + suma(n - 1)
 * El problema se reduce en uno. El `n +` se aplica AL VOLVER de la llamada, no
 * antes: primero hay que resolver la suma chica, después se le agrega n.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n-1) + Θ(1)
 * Se despliega: T(n) = T(n-1) + T(n-2) + ... + T(0) + n·Θ(1)
 *   Temporal: Θ(n) — n llamadas, cada una de Θ(1).
 *   Espacial: Θ(n) — n marcos de pila apilados en el punto más profundo.
 *
 * PILA DE LLAMADAS
 * Cada marco guarda el parámetro n y el valor a la espera del resultado de la
 * llamada interna. Con n = 4 se apilan los marcos de 4, 3, 2, 1 y 0. El de 0
 * es el caso base y devuelve 0. Al desenrollarse: el de 1 recibe 0 y calcula
 * 1+0 = 1; el de 2 recibe 1 y calcula 2+1 = 3; y así hasta que el de 4 recibe
 * 7 y devuelve 4+7 = 10. Hay 5 marcos apilados a la vez.
 *
 * NOTA — el acumulador NO está en la pila
 * Podría escribirse la versión con un acumulador explícito
 * (sumar(n, acc)), y ahí sí el marco guarda `acc`. La versión sin acumulador
 * no lo necesita porque el `n + resultado` se arma al desenrollar. Ambos
 * ocupan Θ(n), sólo cambia qué se guarda en cada marco.
 *
 * CASOS LÍMITE
 * - n = 0 -> 0 (caso base)
 * - n negativo -> 0, sin excepción (absorción del caso base)
 * - Desborde: la suma es n(n+1)/2. Con int, el mayor n seguro es 65535.
 */
public class Ejercicio2_2 {

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
     * Calcula 1 + 2 + ... + n mediante recursión.
     *
     * @param n número entero cualquiera
     * @return la suma de 1 a n, o 0 si n es menor o igual a 0
     */
    public static int suma(int n) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // No hay números que sumar. Se devuelve 0, que es el elemento neutro
            // de la suma, así que las llamadas que la reciben siguen siendo
            // correctas al agregarle su propio n.
            //
            // La condición es n <= 0 y no n == 0 a propósito: si n fuera
            // negativo y sólo se cortara en el 0, la llamada con -1 invocaría
            // a suma(-2), que invocaría a suma(-3)... y nunca terminaría.
            if (n <= 0) {
                return 0;
            }

            // --- CASO RECURSIVO ---
            // Orden de evaluación: primero se resuelve suma(n - 1) — que apila
            // n-1 marcos más — y recién al volver se le agrega n. Por eso el
            // `n` queda "esperando" en este marco mientras la pila crece.
            return n + suma(n - 1);
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
        validarContraFormulaCerrada();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.2: SUMA DE LOS PRIMEROS N NÚMEROS ===");
        System.out.println();

        int[] casos = { 0, 1, 2, 4, 10, 100 };

        for (int n : casos) {
            reiniciarPila();
            int resultado = suma(n);

            System.out.println("  suma(" + n + ") = " + resultado);
            System.out.println("      llamadas recursivas: " + llamadasTotales
                    + " | profundidad máxima de pila: " + profundidadMaxima);
        }

        // Entrada no positiva: la guarda n <= 0 la absorbe y no hay excepción.
        reiniciarPila();
        System.out.println();
        System.out.println("  suma(-7) = " + suma(-7)
                + "  (absorción del caso base, profundidad " + profundidadMaxima + ")");

        System.out.println();
        System.out.println("  Con n = 4 se apilan los marcos de 4, 3, 2, 1 y 0:");
        System.out.println("  el de 0 devuelve 0, y al desenrollarse cada capa suma su n.");
    }

    private static void validarContraFormulaCerrada() {
        System.out.println();
        System.out.println("--- VALIDACIÓN CONTRA LA FÓRMULA CERRADA ---");
        System.out.println("  La suma de 1 a n tiene forma cerrada: n(n+1)/2.");
        System.out.println("  Si ambos valores coinciden, la recursión es correcta.");
        System.out.println();

        int[] pruebas = { 1, 2, 5, 10, 50, 100, 1000 };
        boolean todoCoincide = true;

        for (int n : pruebas) {
            int recursivo = suma(n);
            // La fórmula cerrada usa *, pero sólo en el ORÁCULO de verificación,
            // nunca en el algoritmo que se está evaluando.
            int cerrado = n * (n + 1) / 2;
            boolean coincide = recursivo == cerrado;
            if (!coincide) {
                todoCoincide = false;
            }
            System.out.println("  n = " + String.format("%5d", n)
                    + " | recursivo: " + String.format("%8d", recursivo)
                    + " | fórmula: " + String.format("%8d", cerrado)
                    + " | " + (coincide ? "OK" : "DISCREPANCIA"));
        }

        System.out.println();
        System.out.println("  Resultado global: "
                + (todoCoincide ? "todas las pruebas coinciden." : "HAY DISCREPANCIAS."));
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("--- LÍMITE DE int ---");
        int maximoSeguro = 65535;
        System.out.println("  La suma es n(n+1)/2. El mayor n que cabe en int es " + maximoSeguro
                + " (suma = " + ((long) maximoSeguro * (maximoSeguro + 1) / 2) + ").");
        System.out.println("  Con n = 70000 la suma excede 2^31-1 y el resultado daría negativo.");
        System.out.println("  Si se necesita más, el acumulador debe ser long.");

        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese un numero entero (EOF para salir): ");
            while (entrada.hasNextInt()) {
                int n = entrada.nextInt();
                reiniciarPila();
                int resultado = suma(n);

                System.out.println("  suma(" + n + ") = " + resultado);
                System.out.println("      profundidad maxima de pila: " + profundidadMaxima);
                System.out.print("Ingrese un numero entero (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se justificó el criterio de validación. El enunciado pedía "cómo validar que
 *   la función funciona correctamente" sin indicar contra qué: se eligió
 *   contrastar contra la fórmula cerrada n(n+1)/2, porque es una expresión
 *   independiente de la recursión y sirve como oráculo. Comparar contra un
 *   valor esperado fijo sólo detectaría un caso.
 * - Se amplió el caso base a "n <= 0". El enunciado decía "qué debe pasar si n
 *   es 0"; restringir la guarda a exactamente 0 haría que cualquier entrada
 *   negativa recurriera para siempre, porque suma(-1) llama a suma(-2) y nunca
 *   llega a 0.
 * - Se documentó el límite de desborde de int (n <= 65535), que el enunciado no
 *   pedía pero es la falla natural de este algoritmo.
 * ========================================================================== */
