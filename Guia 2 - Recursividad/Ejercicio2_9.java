/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que determine si una palabra
 * es palíndromo.
 *
 * Este problema puede resolverse recursivamente porque un palíndromo se lee igual
 * en ambos sentidos: eso obliga a que el primer carácter sea igual al último, y
 * esa condición se cumple para los caracteres del medio con el mismo criterio. La
 * propiedad es simétrica, y eso la hace naturalmente recursiva sobre los
 * extremos.
 *
 * El caso base es length() <= 1, que devuelve true, porque una cadena vacía o de
 * un solo carácter se lee igual en ambos sentidos.
 *
 * Si el primer carácter y el último son distintos, se devuelve false
 * inmediatamente, sin seguir comparando: ese es un caso base de rechazo, y es
 * correcto porque un solo par distinto ya descarta que toda la cadena sea
 * palíndromo.
 *
 * El caso recursivo es esPalindromo(s) = esPalindromo(s.substring(1,
 * s.length() - 1)), porque en cada llamada se eliminan los dos extremos y el
 * problema se reduce en dos caracteres.
 *
 * La función debe devolver boolean.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso de
 * rechazo temprano, el caso recursivo y la pila de llamadas. También quiero que
 * comente que esta recursión usa Θ(n/2) profundidad, no Θ(n).
 *
 * También quiero probarlo con estos casos:
 *   - "" -> true (caso base, cadena vacía)
 *   - "a" -> true (caso base, un carácter)
 *   - "ab" -> false (se rechaza en el primer par)
 *   - "ana" -> true (palíndromo de longitud impar)
 *   - "anilina" -> true (palíndromo de longitud impar)
 *   - "reconocer" -> true (palíndromo de longitud par)
 *   - "hola" -> false
 *   - "Ana" -> documentar si se normalizan mayúsculas, y por qué
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 9: Palíndromo
 *
 * ESTRATEGIA — por qué es recursivo
 * Un palíndromo es una cadena que se lee igual de izquierda a derecha que de
 * derecha a izquierda. Eso exige que el primer carácter coincida con el último,
 * y que lo mismo ocurra "hacia adentro". Cada verificación descarta los dos
 * extremos y deja el mismo problema, más chico, en el medio.
 *
 * CASO BASE — length() <= 1 -> true
 * Una cadena vacía y una de un solo carácter se leen igual en ambos sentidos:
 * no hay par de caracteres que pueda diferir, así que la respuesta es true por
 * definición, no por conveniencia.
 *
 * CASO BASE DE RECHAZO — primero != último -> false
 * Éste es un caso base tan importante como el anterior, y no es un "if más": es
 * una TERMINACIÓN. Un solo par distinto ya refuta que toda la cadena sea
 * palíndromo. Seguir comparando el resto sería trabajo perdido, porque el
 * resultado ya está decidido.
 *
 * La analogía con el ejercicio 6 (comparar dos vectores) es exacta: la igualdad
 * es una conjunción, "todos los pares coinciden", y en una conjunción basta un
 * término falso para que toda la expresión sea falsa.
 *
 * CASO RECURSIVO — esPalindromo(s) = esPalindromo(s.substring(1, s.length() - 1))
 * Se eliminan los DOS extremos y se sigue con el interior.
 *
 * ⚠️ OJO CON EL CASO BASE CUANDO LA LONGITUD ES IMPAR
 * Con "ana" (3 letras): los extremos son 'a' y 'a', coinciden, se recorta y
 * queda "n", que entra en el caso base y devuelve true. Con 5 letras, la
 * recursión llega a length() == 1. Por eso el caso base debe ser length() <= 1
 * y no length() == 0: una cadena de longitud impar NUNCA llega a 0, siempre
 * pasa por 1 antes.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n - 2) + Θ(1)
 * Cada llamada elimina DOS caracteres, no uno.
 *   Temporal: Θ(n) — la mitad de las llamadas que serían necesarias reduciendo
 *             de a uno, pero sigue siendo lineal.
 *   Espacial: Θ(n/2) — se apila un marco cada DOS caracteres, así que la
 *             profundidad es aproximadamente la mitad de la longitud.
 *
 * PILA DE LLAMADAS
 * Con "anilina" (7 letras): los pares son (a,a), (n,n), (i,i) y queda "l".
 * Se apilan 4 marcos: "anilina", "nilin", "ili", "l". El de "l" es el caso base.
 * Al desenrollarse, cada nivel devuelve el resultado del interior, y como todos
 * los pares anteriores coincidieron, el resultado true se propaga intacto.
 *
 * NOTA SOBRE LA NORMALIZACIÓN
 * Por defecto esta implementación NO normaliza mayúsculas ni espacios: compara
 * los caracteres tal cual. "Ana" daría false y "Anita lava la tina" también.
 * Esa decisión se documenta en vez de tomarse en silencio, porque depende de
 * qué se entienda por "palíndromo": el matemático exige igualdad carácter a
 * carácter; el de juego de palabras permite ignorar mayúsculas y espacios.
 *
 * CASOS LÍMITE
 * - "" -> true (caso base)
 * - "a" -> true (caso base, longitud impar en su mínimo)
 * - "ab" -> false (rechazo inmediato, 1 sola comparación)
 * - longitud impar: "ana", "anilina" -> true
 * - longitud par: "reconocer", "aa" -> true
 * - todos los caracteres iguales -> true
 */
public class Ejercicio2_9 {

    /** Caracteres que se consideran al comparar. */
    public static final String ESPACIO = " ";

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
     * Determina si una cadena es palíndromo comparando los extremos.
     * No normaliza mayúsculas ni espacios: compara los caracteres tal cual.
     *
     * @param s cadena a evaluar
     * @return true si se lee igual en ambos sentidos
     */
    public static boolean esPalindromo(String s) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // Vacía o de un solo carácter: no hay par que pueda diferir.
            //
            // La condición es length() <= 1 y NO length() == 0 porque con
            // longitud impar la recursión NUNCA llega a 0: "ana" -> "n" y
            // "anilina" -> "ili" -> "l". Siempre se pasa por 1 antes que por 0.
            if (s.length() <= 1) {
                return true;
            }

            // --- CASO BASE DE RECHAZO ---
            // Si los extremos difieren, el resultado ya está decidido.
            //
            // No hace falta seguir: la igualdad es una conjunción ("todos los
            // pares coinciden"), y en una conjunción basta un término falso
            // para que todo sea falso. Seguir comparando sería trabajo
            // desperdiciado.
            char primero = s.charAt(0);
            char ultimo = s.charAt(s.length() - 1);
            if (primero != ultimo) {
                return false;
            }

            // --- CASO RECURSIVO ---
            // Se eliminan LOS DOS extremos con substring(1, length() - 1).
            // El segundo argumento es EXCLUSIVO, por eso es length() - 1 y no
            // length(): así el último carácter queda afuera.
            return esPalindromo(s.substring(1, s.length() - 1));
        } finally {
            salir();
        }
    }

    /**
     * Variante que normaliza antes de comparar: ignora mayúsculas y espacios.
     * Se incluye para mostrar explícitamente la decisión que NO se tomó en la
     * función principal.
     */
    public static boolean esPalindromoNormalizado(String s) {
        String limpio = s.toLowerCase().replace(" ", "");
        return esPalindromo(limpio);
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y símbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        demostrarCasosLimite();
        demostrarRecorteDeExtremos();
        demostrarProfundidadPar();
        demostrarNormalizacion();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.9: PALÍNDROMO ===");
        System.out.println();

        String[] casos = { "", "a", "ab", "aa", "ana", "anilina", "reconocer", "hola" };

        for (String palabra : casos) {
            reiniciarPila();
            boolean resultado = esPalindromo(palabra);

            System.out.println("  esPalindromo(\"" + palabra + "\") = " + resultado);
            System.out.println("      longitud: " + palabra.length()
                    + " | llamadas: " + llamadasTotales
                    + " | profundidad: " + profundidadMaxima
                    + "  (una cada 2 caracteres)");
        }
    }

    private static void demostrarRecorteDeExtremos() {
        System.out.println();
        System.out.println("--- RECORTE DE EXTREMOS, PASO A PASO ---");

        String[] listaDeMuestras = { "anilina", "hola" };
        for (int indiceMuestra = 0; indiceMuestra < listaDeMuestras.length; indiceMuestra++) {
            String palabra = listaDeMuestras[indiceMuestra];
            System.out.println();
            System.out.println("  \"" + palabra + "\":");
            String actual = palabra;
            int nivel = 0;
            while (actual.length() > 1) {
                char primero = actual.charAt(0);
                char ultimo = actual.charAt(actual.length() - 1);
                boolean iguales = primero == ultimo;
                System.out.println("      " + repetir("  ", nivel)
                        + "nivel " + nivel + ": \"" + actual + "\""
                        + " -> extremos '" + primero + "' y '" + ultimo + "'"
                        + (iguales ? " coinciden" : " DIFIEREN -> false"));
                if (!iguales) {
                    break;
                }
                actual = actual.substring(1, actual.length() - 1);
                nivel++;
            }
            if (actual.length() <= 1) {
                System.out.println("      " + repetir("  ", nivel)
                        + "nivel " + nivel + ": \"" + actual + "\""
                        + " -> CASO BASE (length <= 1) -> true");
            }
        }

        System.out.println();
        System.out.println("  Fíjate en 'anilina': el recorrido es impar (7 -> 5 -> 3 -> 1),");
        System.out.println("  y en 'hola': es par (4 -> 2 -> 2, con 'hl' que ya no es palíndromo");
        System.out.println("  porque 'h' != 'l'). Ninguno de los dos llega a la cadena vacía.");
    }

    private static void demostrarProfundidadPar() {
        System.out.println();
        System.out.println("--- LA PROFUNDIDAD ES n/2, NO n ---");
        System.out.println("  Cada llamada recursiva elimina DOS caracteres, así que la pila");
        System.out.println("  crece la mitad de lo rápido que en los ejercicios 1 a 5.");
        System.out.println();

        for (int largo : new int[] { 10, 20, 40, 80, 160 }) {
            String texto = generarPalindromo(largo);
            reiniciarPila();
            boolean resultado = esPalindromo(texto);
            System.out.println("    longitud " + String.format("%4d", largo)
                    + " -> es palíndromo: " + resultado
                    + " | profundidad máxima: " + String.format("%3d", profundidadMaxima)
                    + " (aprox. n/2)");
        }
    }

    private static void demostrarNormalizacion() {
        System.out.println();
        System.out.println("--- NORMALIZACIÓN: MAYÚSCULAS Y ESPACIOS ---");
        System.out.println("  La función principal NO normaliza: compara tal cual.");
        System.out.println();

        String[] conRuido = { "Ana", "Anita lava la tina", "Reconocer", "anilina" };

        System.out.printf("  %-24s | %-14s | %-14s%n", "palabra", "sin normalizar", "normalizada");
        for (String palabra : conRuido) {
            boolean directo = esPalindromo(palabra);
            boolean normalizado = esPalindromoNormalizado(palabra);
            System.out.printf("  %-24s | %-14s | %-14s%n",
                    "\"" + palabra + "\"", directo, normalizado);
        }

        System.out.println();
        System.out.println("  Cuándo usar cada variante:");
        System.out.println("    - SIN normalizar: cuando la definición de palíndromo es matemática,");
        System.out.println("      es decir, igualdad carácter a carácter.");
        System.out.println("    - CON normalizar: en textos de juego de palabras como");
        System.out.println("      \"Anita lava la tina\", donde se ignoran mayúsculas y espacios.");
        System.out.println("  La decisión se toma explícitamente, no por omisión.");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese una palabra (EOF para salir): ");
            while (entrada.hasNextLine()) {
                String palabra = leerLineaLimpia(entrada);
                if (palabra.isEmpty()) {
                    break;
                }
                reiniciarPila();
                boolean esPal = esPalindromo(palabra);
                int profundidadUsada = profundidadMaxima;

                System.out.println("  \"" + palabra + "\" -> esPalindromo = " + esPal);
                System.out.println("      llamadas: " + llamadasTotales
                        + " | profundidad máxima: " + profundidadUsada);
                System.out.print("Ingrese una palabra (EOF para salir): ");
            }
        }
    }

    /**
     * Lee una línea de la entrada limpiando el BOM (Byte Order Mark) inicial.
     *
     * PowerShell y muchos editores guardan texto UTF-8 con BOM (bytes EF BB BF
     * al principio). Al leer, ese BOM se convierte en el carácter U+FEFF y queda
     * pegado al primer carácter: esPalindromo("\uFEFFanilina") daba false,
     * porque el BOM se comparaba contra la 'a' final.
     */
    private static String leerLineaLimpia(java.util.Scanner entrada) {
        String linea = entrada.nextLine();
        if (!linea.isEmpty() && linea.charAt(0) == '\uFEFF') {
            linea = linea.substring(1);
        }
        return linea;
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Repite un texto n veces, para indentar visualmente la traza. */
    private static String repetir(String texto, int veces) {
        StringBuilder constructor = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            constructor.append(texto);
        }
        return constructor.toString();
    }

    /** Genera un palíndromo de la longitud indicada. */
    private static String generarPalindromo(int largo) {
        StringBuilder mitad = new StringBuilder();
        for (int i = 0; i < largo / 2; i++) {
            mitad.append((char) ('a' + (i % 26)));
        }
        String izquierda = mitad.toString();
        StringBuilder resultado = new StringBuilder(izquierda);
        // El centro, si la longitud es impar.
        if (largo % 2 == 1) {
            resultado.append('x');
        }
        resultado.append(izquierda);
        return resultado.substring(0, Math.min(resultado.length(), largo));
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se añadió el argumento de POR QUÉ el rechazo por extremos distintos es un
 *   caso base y no un simple "if". El enunciado pedía "qué pasa si son
 *   distintos", pero la respuesta relevante es que es una TERMINACIÓN: seguir
 *   comparando sería trabajo perdido porque el resultado ya está decidido. Se
 *   relacionó con el ejercicio 6 (igualdad de vectores), donde ocurre lo mismo.
 * - Se aclaró que el caso base debe ser length() <= 1 y no length() == 0. Con
 *   longitud impar la recursión NUNCA llega a la cadena vacía: "ana" -> "n",
 *   "anilina" -> "ili" -> "l". Usar == 0 dejaría sin caso base a todas las
 *   palabras de longitud impar, que es la mitad de los casos.
 * - Se especificó la profundidad como Θ(n/2). El enunciado lo pedía, y es una
 *   diferencia real respecto de los ejercicios 1 a 5: acá cada llamada consume
 *   dos caracteres, así que la pila crece a la mitad de velocidad.
 * - Se documentó la decisión de NO normalizar mayúsculas ni espacios, y se
 *   agregó una variante que sí lo hace. El enunciado pedía "documentar si se
 *   normaliza y por qué", y documentarlo sólo en un comentario no alcanza:
 *   hace falta mostrar las dos variantes para que se vea la diferencia.
 * ========================================================================== */