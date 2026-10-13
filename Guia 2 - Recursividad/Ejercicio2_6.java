/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que cuente cuántos dígitos
 * tiene un número entero positivo.
 *
 * Este problema puede resolverse recursivamente porque dividir un número entre 10
 * le elimina su último dígito: 1234 / 10 = 123, que tiene un dígito menos. El
 * problema se reduce solo con una división entera.
 *
 * El caso base es n < 10, que devuelve 1, porque cualquier número menor que 10
 * tiene exactamente un dígito (incluido el 0, que se cuenta como un dígito).
 *
 * El caso recursivo es digitos(n) = 1 + digitos(n / 10), porque en cada llamada
 * se reduce el número en un dígito, y el 1 + va contando los dígitos que se van
 * quitando.
 *
 * La función debe devolver int.
 *
 * Quiero que el código tenga comentarios explicando el caso base (y por qué es
 * n < 10 y no n == 0), el caso recursivo y la pila de llamadas. Notá que esta
 * función es O(log10 n), o sea mejor que lineal: cada llamada reduce el número a
 * un dígito menos. Es un buen contraste con los ejercicios 1 a 5, que eran
 * Theta(n).
 *
 * También quiero probarlo con estos casos:
 *   - 0 -> 1 (el cero es un dígito)
 *   - 7 -> 1 (caso base)
 *   - 10 -> 2
 *   - 1234 -> 4 (caso típico)
 *   - 1000 -> 4 (tiene ceros al final, que también son dígitos)
 *   - 999999999 -> 9 (el máximo de un int positivo)
 *   - número negativo -> debe informar que no es válido
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 6: Contar dígitos
 *
 * ESTRATEGIA — por qué es recursivo
 * El problema se desarma con la operación más elemental de aritmética entera:
 * la división por 10 quita el último dígito. Con esa sola operación, el número
 * de dígitos se reduce en uno en cada paso, y no hay forma más directa de
 * hacerlo.
 *
 * CASO BASE — n < 10 -> 1
 * Es `n < 10` y NO `n == 0`, y la diferencia importa:
 *   - con `n < 10`, el 0 devuelve 1, porque el CERO ES UN DÍGITO. "0" escrito
 *     en papel ocupa un carácter, es un dígito;
 *   - con `n == 0`, la función no terminaría para n = 10: dividiría hasta 0 y
 *     entonces devolvería 0, cuando en realidad 10 tiene 2 dígitos.
 *
 * La condición `n < 10` cubre de una sola vez los nueve casos base posibles
 * (0 a 9), todos con la misma respuesta: 1 dígito.
 *
 * CASO RECURSIVO — digitos(n) = 1 + digitos(n / 10)
 * El `1 +` cuenta el dígito que se acaba de quitar (el de las unidades), y la
 * llamada interna cuenta los que quedan.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n / 10) + Θ(1)
 * Cada llamada divide por 10, así que en k llamadas el número pasó de n a
 * n/10^k. Se corta cuando n/10^k < 10, es decir k ≈ log10(n).
 *   Temporal: Θ(log10 n) — para n = 999999999 hacen falta 9 llamadas, no
 *             millones. MEJOR que lineal, a diferencia de los ejercicios 1 a 5.
 *   Espacial: Θ(log10 n) — un marco por dígito, mucho menos que en el
 *             factorial.
 *
 * PILA DE LLAMADAS
 * Con 1234: se apilan los marcos de 1234, 123, 12, 1 y 0. El del 0 es el
 * caso base y devuelve 1. Al desenrollarse: 1 devuelve 1+1=2; 12 devuelve
 * 1+2=3; 123 devuelve 1+3=4; 1234 devuelve 1+4=5. La profundidad máxima es
 * igual a la cantidad de dígitos, no al valor del número.
 *
 * POR QUÉ ES MÁS EFICIENTE QUE LOS EJERCICIOS 1 A 5
 * Aquellos reducían el parámetro en UNO por llamada (n-1), así que necesitaban
 * n llamadas. Este reduce por un factor 10 (n/10), así que necesita sólo
 * log10(n) llamadas. Reducir multiplicativamente es MUCHO más rápido que
 * reducir de a uno.
 *
 * CASOS LÍMITE
 * - n = 0 -> 1 (caso base; el cero es un dígito)
 * - n entre 1 y 9 -> 1 (caso base)
 * - n = 1000 -> 4 (los ceros finales también son dígitos)
 * - n = 999999999 -> 9 (máximo de un int positivo)
 * - n negativo -> centinela NO_VALIDO, sin excepción ni recursión infinita
 *   (con negativos la división entera de Java trunca hacia cero y el conteo
 *   sería incorrecto, así que se rechaza explícitamente)
 */
public class Ejercicio2_6 {

    /** Valor centinela devuelto cuando el número es negativo. */
    public static final int NO_VALIDO = -1;

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
     * Cuenta los dígitos de un número entero no negativo.
     *
     * @param n número entero no negativo
     * @return cantidad de dígitos de n, o NO_VALIDO (-1) si n es negativo
     */
    public static int digitos(int n) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // Cualquier número de 0 a 9 tiene exactamente un dígito.
            //
            // El cero entra acá y vale 1, porque "0" es un dígito escrito.
            //
            // La condición es n < 10 y NO n == 0 a propósito. Con n == 0 el
            // 10 caería en una división infinita hacia el 0 y devolvería 0
            // dígitos en vez de 2.
            //
            // La guarda de n < 0 va antes: con negativos, la división entera de
            // Java trunca hacia cero y el conteo nunca sería correcto.
            if (n < 0) {
                return NO_VALIDO;
            }
            if (n < 10) {
                return 1;
            }

            // --- CASO RECURSIVO ---
            // n / 10 elimina el dígito de las unidades (división entera).
            // El 1 + cuenta ese dígito que acaba de desaparecer.
            return 1 + digitos(n / 10);
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
        demostrarCrecimientoLogaritmico();
        compararConFactorial();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.6: CONTAR DÍGITOS ===");
        System.out.println();

        int[] casos = { 0, 7, 9, 10, 99, 100, 1000, 1234, 999999999 };

        for (int n : casos) {
            reiniciarPila();
            int resultado = digitos(n);

            System.out.println("  digitos(" + String.format("%9d", n) + ") = " + resultado
                    + "  | llamadas: " + llamadasTotales
                    + " | profundidad: " + profundidadMaxima);
        }

        reiniciarPila();
        System.out.println();
        System.out.println("  digitos(-42) = " + digitos(-42)
                + "  (NO_VALIDO: con negativos el / trunca hacia cero)");
        System.out.println();
        System.out.println("  CASO CLAVE: digitos(0) = 1, NO 0. El cero es un dígito.");
        System.out.println("  Ésta es la diferencia con el ejercicio 7, donde el 0 SUME 0.");
    }

    private static void demostrarCrecimientoLogaritmico() {
        System.out.println();
        System.out.println("--- CRECIMIENTO Θ(log10 n) ---");
        System.out.println("  Al multiplicar n por 10, sólo se AGREGA una llamada: el dígito nuevo.");
        System.out.println();

        long[] pruebas = { 9L, 99L, 999L, 9999L, 99999L, 999999L, 9999999L, 99999999L, 999999999L };

        for (long valor : pruebas) {
            int n = (int) valor;
            reiniciarPila();
            int cantidadDigitos = digitos(n);
            System.out.println("    n = " + String.format("%9d", n)
                    + " -> digitos: " + cantidadDigitos
                    + " | llamadas recursivas: " + llamadasTotales);
        }

        System.out.println();
        System.out.println("  Se multiplica n por 10 en cada fila y las llamadas suben en 1,");
        System.out.println("  no en 10. Ésa es la firma de una complejidad logarítmica.");
    }

    private static void compararConFactorial() {
        System.out.println();
        System.out.println("--- CONTRASTE CON EL EJERCICIO 1 (FACTORIAL) ---");
        System.out.println("  El factorial reduce el parámetro de UNO en uno: necesita n llamadas.");
        System.out.println("  Este ejercicio lo reduce por un factor 10: necesita log10(n) llamadas.");
        System.out.println();

        System.out.printf("  %12s | %18s | %20s%n", "n", "llamadas factorial", "llamadas digitos");
        for (int n : new int[] { 10, 100, 1000, 10000, 100000 }) {
            int digitosDeN = String.valueOf(n).length();
            System.out.printf("  %12d | %18d | %20d%n", n, n, digitosDeN);
        }
        System.out.println();
        System.out.println("  Con n = 100000: el factorial hace 100000 llamadas y esta función 6.");
        System.out.println("  Reducir multiplicativamente (n/10) es MUCHO más rápido que de a uno (n-1).");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese un numero entero no negativo (EOF para salir): ");
            while (entrada.hasNextInt()) {
                int n = entrada.nextInt();
                reiniciarPila();
                int resultado = digitos(n);

                if (n < 0) {
                    System.out.println("  " + n + " no es valido: solo se admiten numeros no negativos.");
                } else {
                    System.out.println("  digitos(" + n + ") = " + resultado);
                    System.out.println("      profundidad maxima de pila: " + profundidadMaxima
                            + " (una por digito, no una por unidad)");
                }
                System.out.print("Ingrese un numero entero no negativo (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se explicó la razón de por qué el caso base es "n < 10" y no "n == 0".
 *   El enunciado pedía exactamente eso ("por qué un número menor que 10 tiene un
 *   solo dígito"), pero la justificación profunda es más fuerte: con n == 0 el
 *   10 caería en una división infinita hacia el 0 y devolvería 0 dígitos en vez
 *   de 2. El criterio no es un detalle, es lo que garantiza la terminación.
 * - Se documentó la diferencia con el ejercicio 7: acá el 0 vale 1 (es un dígito),
 *   allá vale 0 (no aporta nada a una suma). Como los dos ejercicios son casi
 *   idénticos en forma pero distintos en el caso base, la consigna los pone
 *   juntos a propósito y el prompt debe explicitar la contraste.
 * - Se documentó el rechazo de números negativos. La división entera de Java
 *   trunca hacia cero, así que con negativos el conteo sería incorrecto (por
 *   ejemplo -42 daría un conteo sin sentido). El enunciado no lo pedía como
 *   requisito, pero es una precondición real del algoritmo.
 * - Se agregó la demostración del crecimiento logarítmico. El enunciado pedía
 *   los valores de prueba, pero sin mostrar la comparación contra el factorial el
 *   alumno no ve POR QUÉ dividir entre 10 es mejor que restar 1.
 * ========================================================================== */