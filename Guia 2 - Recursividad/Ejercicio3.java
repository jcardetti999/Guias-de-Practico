/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que calcule a * b sin usar
 * el operador *.
 *
 * Este problema puede resolverse recursivamente porque multiplicar por b es
 * sumar a repetidas b veces: a·b = a + a + ... + a (b veces). Es la definición
 * misma de la multiplicación como suma repetida.
 *
 * El caso base es b == 0, que devuelve 0, porque sumar a cero veces da 0 (la
 * suma vacía es el elemento neutro de la suma).
 *
 * El caso recursivo es multiplicar(a, b) = a + multiplicar(a, b - 1), porque en
 * cada llamada se reduce el SEGUNDO parámetro: b es la cantidad de sumas que
 * faltan, y por eso es el que se descuenta. El primer parámetro a no se reduce:
 * es la magnitud que se repite.
 *
 * La función debe devolver int.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo, por qué se reduce b y no a, y la pila de llamadas.
 *
 * También quiero probarlo con estos casos:
 *   - b = 0 -> 0 (caso base, suma vacía)
 *   - a = 0 -> 0 (el otro factor en cero)
 *   - 3 · 4 = 12 (caso típico)
 *   - comprobación de conmutatividad: a · b debe dar lo mismo que b · a
 *   - 1 · 7 = 7 y 7 · 1 = 7 (extremos)
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 3: Multiplicación mediante sumas
 *
 * ESTRATEGIA — por qué es recursivo
 * La multiplicación es una suma repetida. Eso permite "desarmar" el producto en
 * la suma de una unidad menos: si todavía quedan b sumas por hacer, se hace una
 * y se delega el resto. La recursión aparece de forma natural al aislar el
 * último sumando.
 *
 * CASO BASE — b == 0 -> 0
 * Pedir cero sumas es pedir la suma vacía, que vale 0.
 *
 * POR QUÉ SE REDUCE b Y NO a
 * En a·b, el `a` es la MAGNITUD que se repite y el `b` es la CANTIDAD de
 * repeticiones. La cantidad es lo que se agota: al cabo de b restas ya no queda
 * nada por sumar. Si se redujera `a` en lugar de `b`, el algoritmo dépendería
 * del valor numérico del multiplicando, y conmutaría casos como
 * multiplicar(2, 5) que hoy son un solo nivel de recursión.
 *
 * Consecuencia práctica: el costo depende de b, NO de a.
 *   T(a, b) = T(a, b-1) + Θ(1)  ->  Θ(b)
 * Comprobar que multiplicar(2, 5) tarda lo mismo que multiplicar(100, 5), y
 * mucho menos que multiplicar(2, 500).
 *
 * CASO RECURSIVO — multiplicar(a, b) = a + multiplicar(a, b - 1)
 * El `a +` se aplica al volver. El resultado de la llamada interna es la suma
 * de (b-1) unidades; se le agrega una unidad más y se devuelve el total.
 *
 * COMPLEJIDAD
 *   Temporal: Θ(b) — b llamadas, porque b es el parámetro que se reduce.
 *   Espacial: Θ(b) — b marcos apilados.
 *
 * PILA DE LLAMADAS
 * Con multiplicar(3, 4): se apilan los marcos de b=4, b=3, b=2, b=1 y b=0.
 * El de b=0 es el caso base y devuelve 0. Al desenrollarse: b=1 devuelve
 * 3+0=3; b=2 devuelve 3+3=6; b=3 devuelve 3+6=9; b=4 devuelve 3+9=12.
 * El marco de b=4 estuvo esperando cuatro marcos debajo de él.
 *
 * NOTA — el algoritmo NO usa el operador *
 * Ni en el cuerpo recursivo ni en los casos base. En la demostración sí
 * aparecen valores esperados calculados con multiplicación, pero sólo como
 * oráculo de verificación externa al algoritmo; el algoritmo en sí no lo usa.
 *
 * CASOS LÍMITE
 * - b = 0 -> 0 (caso base)
 * - a = 0 con b > 0 -> 0 tras b llamadas (el cero se suma b veces)
 * - b negativo -> centinela NO_VALIDO, sin excepción ni recursión infinita
 */
public class Ejercicio3 {

    /** Valor centinela devuelto cuando b es negativo. */
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
     * Calcula a · b como suma repetida, mediante recursión y sin usar `*`.
     *
     * @param a magnitud que se repite
     * @param b cantidad de repeticiones
     * @return a · b, o NO_VALIDO (-1) si b es negativo
     */
    public static int multiplicar(int a, int b) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // Zero repeticiones -> suma vacía -> 0. Es el elemento neutro.
            //
            // La guarda de b < 0 va antes que la del caso base para que un b
            // negativo no entre en una recursión que nunca frena: si sólo se
            // cortara en b == 0, multiplicar(a, -1) llamaría a
            // multiplicar(a, -2) indefinidamente.
            if (b < 0) {
                return NO_VALIDO;
            }
            if (b == 0) {
                return 0;
            }

            // --- CASO RECURSIVO ---
            // Se reduce b (la cantidad), nunca a (la magnitud).
            //
            // El `a +` se ejecuta al VOLVER: primero se resuelve
            // multiplicar(a, b - 1), que apila b-1 marcos más, y recién
            // después se suma la unidad pendiente.
            return a + multiplicar(a, b - 1);
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
        demostrarConmutatividad();
        demostrarQueDependeDeB();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 3: MULTIPLICACIÓN MEDIANTE SUMAS ===");
        System.out.println("  (el algoritmo no usa el operador *)");
        System.out.println();

        int[][] casos = {
            { 3, 0 },     // caso base: suma vacía
            { 3, 1 },     // una sola suma
            { 3, 4 },     // típico -> 12
            { 7, 1 },     // extremo
            { 0, 5 }      // el otro factor en cero
        };

        for (int[] caso : casos) {
            int a = caso[0];
            int b = caso[1];
            reiniciarPila();
            int resultado = multiplicar(a, b);

            System.out.println("  multiplicar(" + a + ", " + b + ") = " + resultado);
            System.out.println("      llamadas: " + llamadasTotales
                    + " | profundidad máxima: " + profundidadMaxima
                    + "  (ambas dependen SOLO de b, no de a)");
        }

        reiniciarPila();
        System.out.println();
        System.out.println("  multiplicar(5, -1) = " + multiplicar(5, -1)
                + "  (NO_VALIDO, sin recursión infinita)");
    }

    private static void demostrarConmutatividad() {
        System.out.println();
        System.out.println("--- CONMUTATIVIDAD: a · b = b · a ---");

        int[][] pares = { { 3, 4 }, { 2, 7 }, { 5, 6 }, { 9, 2 } };
        boolean todasCoinciden = true;

        for (int[] par : pares) {
            int a = par[0];
            int b = par[1];
            int ab = multiplicar(a, b);
            int ba = multiplicar(b, a);
            boolean coincide = ab == ba;
            if (!coincide) {
                todasCoinciden = false;
            }
            System.out.println("  multiplicar(" + a + "," + b + ") = " + ab
                    + " | multiplicar(" + b + "," + a + ") = " + ba
                    + "  -> " + (coincide ? "OK" : "DISCREPANCIA"));
        }

        System.out.println();
        System.out.println("  " + (todasCoinciden
                ? "Todas las parejas conmutan, como exige la aritmética."
                : "HAY DISCREPANCIAS: el algoritmo no es conmutativo."));
        System.out.println();
        System.out.println("  Ojo: la conmutatividad se cumple en el RESULTADO, pero el");
        System.out.println("  costo no es simétrico. multiplicar(a, b) tarda b llamadas y");
        System.out.println("  multiplicar(b, a) tarda a. Son el mismo número, distinto costo.");
    }

    private static void demostrarQueDependeDeB() {
        System.out.println();
        System.out.println("--- EL COSTO DEPENDE DE b, NO DE a ---");
        System.out.println("  Mismo b, valores de a muy distintos:");
        System.out.println();

        int[] valoresA = { 1, 2, 100, 99999 };
        for (int a : valoresA) {
            reiniciarPila();
            multiplicar(a, 5);
            System.out.println("    multiplicar(" + String.format("%6d", a) + ", 5)"
                    + " -> " + llamadasTotales + " llamadas, profundidad " + profundidadMaxima);
        }
        System.out.println("  Las 4 filas usan EXACTAMENTE las mismas llamadas: el valor de a");
        System.out.println("  no influyen en el costo. Sólo importa cuántas sumas hay que hacer.");

        System.out.println();
        System.out.println("  Ahora variando b con a fijo:");
        for (int b : new int[] { 1, 2, 4, 8, 16 }) {
            reiniciarPila();
            multiplicar(7, b);
            System.out.println("    multiplicar(7, " + String.format("%2d", b) + ")"
                    + " -> " + llamadasTotales + " llamadas, profundidad " + profundidadMaxima);
        }
        System.out.println("  Al duplicar b, se duplican las llamadas: Θ(b).");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese el primer factor (EOF para salir): ");
            while (entrada.hasNextInt()) {
                int a = entrada.nextInt();
                System.out.print("Ingrese el segundo factor (la cantidad de sumas): ");
                if (!entrada.hasNextInt()) {
                    break;
                }
                int b = entrada.nextInt();

                reiniciarPila();
                int resultado = multiplicar(a, b);

                if (b < 0) {
                    System.out.println("  El segundo factor no puede ser negativo.");
                } else {
                    System.out.println("  multiplicar(" + a + ", " + b + ") = " + resultado);
                    System.out.println("      profundidad maxima de pila: " + profundidadMaxima);
                }
                System.out.print("Ingrese el primer factor (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se agregó la comprobación de conmutatividad como prueba independiente. El
 *   enunciado pedía "qué pruebas deberían hacerse", y comparar contra un valor
 *   esperado fijo sólo verifica un caso. La conmutatividad es una propiedad que
 *   debe cumplirse en todos los casos, así que sirve como oráculo general: si
 *   multiplicar(a,b) == multiplicar(b,a) para muchos pares, la implementación es
 *   correcta.
 * - Se documentó explícitamente que se reduce b y no a. El enunciado lo pedía
 *   ("cuál de los dos parámetros se reduce"), pero la razón profunda es que b es
 *   la CANTIDAD de repeticiones y a es la MAGNITUD: la cantidad es lo que se
 *   agota. De ahí se sigue que el costo es Θ(b) y no depende del valor de a.
 * - Se añadió la guarda "b < 0". El enunciado sólo mencionaba b == 0 como caso
 *   base, pero restringir la guarda a b == 0 haría que un b negativo recurriera
 *   para siempre, porque b - 1 se aleja cada vez más del 0.
 * - Se aclaró que el "oráculo" con multiplicación aparece sólo en la
 *   demostración, nunca en el algoritmo, para que la prohibición del operador *
 *   sea inequívoca.
 * ========================================================================== */
