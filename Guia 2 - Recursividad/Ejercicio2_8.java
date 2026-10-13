/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que invierta un String.
 *
 * Este problema puede resolverse recursivamente porque invertir una cadena es
 * invertir el resto y anteponer el primer carácter al frente: el carácter que
 * queda al principio de la cadena original termina al final de la invertida.
 *
 * El caso base es length() <= 1 (cadena vacía o un solo carácter), que devuelve
 * la cadena tal cual, porque una cadena de 0 o 1 caracteres ya está invertida.
 *
 * El caso recursivo es invertir(s) = invertir(s.substring(1)) + s.charAt(0),
 * porque en cada llamada se reduce la cadena en un carácter: se separa el
 * primero (charAt(0)) del resto (substring(1)), y se sigue con el resto.
 *
 * La función debe devolver String.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo, y cómo se reconstruye el resultado al volver de la recursión:
 * cada llamada sólo tiene un carácter, pero al desenrollarse la pila (de la
 * llamada más profunda a la más superficial) cada capa antepone su carácter al
 * resultado que le devolvió la capa inferior.
 *
 * También quiero probarlo con estos casos:
 *   - "" -> "" (cadena vacía)
 *   - "a" -> "a" (un solo carácter)
 *   - "ab" -> "ba" (dos caracteres)
 *   - "hola" -> "aloh" (caso típico)
 *   - "reconocer" -> "reconocer" (palíndromo, queda igual)
 *   - "anilina" -> "anilina"
 *   - "a b" -> "b a" (con espacio, que también es un carácter y debe conservarse)
 *   - "Hola Mundo" -> comprobar que se invierte incluyendo mayúsculas y espacios
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 8: Invertir una palabra
 *
 * ESTRATEGIA — por qué es recursivo
 * Invertir una cadena es un problema "del primero contra el resto": el primer
 * carácter es el ÚLTIMO de la invertida, y el resto se invierte por la misma
 * regla. La operación se repite sobre una cadena cada vez más corta, así que la
 * recursión es directa.
 *
 * CASO BASE — length() <= 1 -> la cadena tal cual
 * Una cadena vacía y una de un solo carácter ya están invertidas: no hay nada
 * que reordenar. Se usa `length() <= 1` y no dos casos separados porque ambos
 * comparten exactamente la misma acción (devolver s).
 *
 * CASO RECURSIVO — invertir(s) = invertir(s.substring(1)) + s.charAt(0)
 * Se separa el primer carácter del resto y se sigue con el resto.
 *
 * ⚠️ POR QUÉ EL CARÁCTER SE COLOCA AL FINAL (Y NO AL PRINCIPIO)
 * Éste es el punto central del ejercicio, y la tentación natural es escribir
 *
 *     invertir(s.charAt(0) + s.substring(1))     -> MAL, devuelve s SIN invertir
 *
 * Hay que razonarlo mirando el orden de retorno de la pila. Trazando "hola":
 *
 *     "a"    -> caso base: devuelve "a"
 *     "la"   -> "a"  + "l"  = "al"
 *     "ola"  -> "al" + "o"  = "alo"
 *     "hola" -> "alo" + "h" = "aloh"    ✓
 *
 * La regla general: cada capa coloca SU carácter al FINAL del resultado que armó
 * la capa de abajo. El primer carácter es el primero en apilarse y por eso es el
 * último en retornar, o sea el último de la cadena invertida.
 *
 * Si se antepusiera ("l" + "a" = "la", "o" + "la" = "ola", "h" + "ola" = "hola"),
 * se obtendría la cadena original sin invertir.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n-1) + Θ(n)
 * El caso recursivo NO es Θ(1): además de la llamada, hay que concatenar
 * Strings, y concatenar dos textos de longitud n cuesta Θ(n) en Java (se crea
 * un texto nuevo).
 *   Temporal: Θ(n²) — n llamadas, cada una con una concatenación de costo
 *             proporcional al tamaño del texto pendiente.
 *   Espacial: Θ(n²) — no por la pila (que es Θ(n) marcos), sino porque en cada
 *             nivel queda un texto intermedio de tamaño O(n) pendiente de
 *             concatenar.
 *
 * NOTA — Éste es el ÚNICO ejercicio de la guía que es Θ(n²) temporal, y no es
 * culpa de la recursión: es culpa de la concatenación de Strings, que en Java
 * es O(n). Con StringBuilder el algoritmo bajaría a Θ(n), porque la
 * concatenación sería O(1) amortizado. Vale la pena mencionarlo porque separa
 * "el costo de la recursión" del "el costo de la operación auxiliar".
 *
 * PILA DE LLAMADAS
 * Con "hola" se apilan 4 marcos: "hola", "ola", "la", "a". El de "a" es el
 * caso base. Al desenrollarse, cada marco concatena SU carácter al final del
 * resultado que recibió de abajo. El orden de retorno (a, la, ola, hola) es el
 * inverso al de entrada, y por eso el resultado queda al derecho.
 *
 * CASOS LÍMITE
 * - "" -> "" (caso base, cadena vacía)
 * - "a" -> "a" (caso base, un carácter)
 * - "ab" -> "ba" (dos caracteres: el caso base devuelve "b", la capa de "ab"
 *   hace "b" + "a" = "ba")
 * - mayúsculas y espacios se conservan: invertir no es normalizar
 */
public class Ejercicio2_8 {

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
     * Invierte una cadena mediante recursión.
     *
     * @param s cadena a invertir
     * @return la cadena con los caracteres en orden inverso
     */
    public static String invertir(String s) {
        entrar();   // se apila un marco nuevo
        try {
            // --- CASO BASE ---
            // Una cadena de 0 o 1 caracteres ya está invertida: no hay nada que
            // reordenar. Se cubre con una sola condición porque la acción es la
            // misma en los dos casos (devolver s tal cual).
            if (s.length() <= 1) {
                return s;
            }

            // --- CASO RECURSIVO ---
            // charAt(0) saca el primer carácter; substring(1) saca el resto.
            //
            // El carácter va AL FINAL (concatenado con "+" detrás del resultado
            // recursivo) porque el orden de retorno de la pila va del nivel más
            // profundo al más superficial: el primer carácter es el primero en
            // apilarse y por eso es el último en salir, o sea el último de la
            // cadena invertida.
            return invertir(s.substring(1)) + s.charAt(0);
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
        demostrarElDesenrollado();
        demostrarCostoConcatenacion();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 2.8: INVERTIR UNA PALABRA ===");
        System.out.println();

        String[] casos = {
            "",             // cadena vacía
            "a",            // un solo carácter
            "ab",           // dos caracteres
            "hola",         // típico
            "anilina",      // palíndromo
            "reconocer",    // palíndromo
            "a b",          // con espacio
            "Hola Mundo"    // mayúsculas y espacios
        };

        for (String original : casos) {
            reiniciarPila();
            String invertido = invertir(original);

            System.out.println("  invertir(\"" + original + "\") = \"" + invertido + "\"");
            System.out.println("      longitud: " + original.length() + " | llamadas: "
                    + llamadasTotales + " | profundidad: " + profundidadMaxima);
        }

        // Verificación funcional: invertir dos veces devuelve el original.
        String prueba = "Algoritmos";
        System.out.println();
        System.out.println("  Doble inversión sobre \"" + prueba + "\": \""
                + invertir(invertir(prueba)) + "\"  -> vuelve al original: "
                + prueba.equals(invertir(invertir(prueba))));
    }

    private static void demostrarElDesenrollado() {
        System.out.println();
        System.out.println("--- CÓMO SE RECONSTRUYE EL RESULTADO ---");
        System.out.println("  Traza paso a paso de invertir(\"hola\"):");
        System.out.println();

        String[] pasos = {
            "hola\"  -> apila el marco de h",
            "ola\"   -> apila el marco de o",
            "la\"    -> apila el marco de l",
            "a\"     -> CASO BASE: devuelve \"a\"",
            "la\"    -> \"a\" + l = \"al\"",
            "ola\"   -> \"al\" + o = \"alo\"",
            "hola\"  -> \"alo\" + h = \"aloh\"   <-- resultado"
        };

        for (String paso : pasos) {
            System.out.println("    " + paso);
        }

        System.out.println();
        System.out.println("  Cada marco coloca SU carácter al FINAL del resultado que le");
        System.out.println("  devolvió el marco de abajo. Como la pila se devuelve de adentro");
        System.out.println("  hacia afuera, los caracteres salen en orden inverso al de entrada.");
    }

    private static void demostrarCostoConcatenacion() {
        System.out.println();
        System.out.println("--- POR QUÉ ESTE EJERCICIO ES Θ(n²) Y NO Θ(n) ---");
        System.out.println("  Los ejercicios 1 a 5 y el 10 son Θ(n) temporales. Éste no,");
        System.out.println("  y la causa NO es la recursión sino la concatenación de Strings.");
        System.out.println();

        for (int largo : new int[] { 100, 200, 400, 800, 1600 }) {
            String texto = generarCadena(largo);
            reiniciarPila();
            invertir(texto);
            long operaciones = (long) llamadasTotales * largo;
            System.out.println("    longitud " + String.format("%5d", largo)
                    + " -> llamadas: " + String.format("%5d", llamadasTotales)
                    + " | caracteres concatenados: " + operaciones);
        }

        System.out.println();
        System.out.println("  Al duplicar la longitud, las llamadas se duplican...");
        System.out.println("  ...pero los caracteres concatenados se CUADRUPLICAN: Θ(n²).");
        System.out.println("  Con StringBuilder el mismo algoritmo sería Θ(n), porque la");
        System.out.println("  concatenación pasa a ser O(1) amortizado. La diferencia no está");
        System.out.println("  en la recursión sino en la operación auxiliar elegida.");
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
                String invertido = invertir(palabra);

                System.out.println("  original : \"" + palabra + "\"");
                System.out.println("  invertido: \"" + invertido + "\"");
                System.out.println("      profundidad maxima de pila: " + profundidadMaxima);
                System.out.print("Ingrese una palabra (EOF para salir): ");
            }
        }
    }

    /**
     * Lee una línea de la entrada limpiando el BOM (Byte Order Mark) inicial.
     *
     * PowerShell y muchos editores guardan archivos de texto UTF-8 con BOM
     * (los bytes EF BB BF al principio). Al leer, ese BOM se convierte en el
     * carácter U+FEFF, que queda pegado al primer token y ensucia el resultado:
     * invertir("hola") devolvía "\uFEFFaloh".
     *
     * No es un detalle teórico: ocurre siempre que un archivo de entrada fue
     * guardado por una herramienta que escribe BOM.
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

    /** Genera una cadena de la longitud indicada, con caracteres imprimibles. */
    private static String generarCadena(int largo) {
        StringBuilder constructor = new StringBuilder(largo);
        for (int i = 0; i < largo; i++) {
            constructor.append((char) ('a' + (i % 26)));
        }
        return constructor.toString();
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se sustituuyó el enunciado vago por el argumento real: por qué el carácter
 *   se coloca al FINAL del resultado recursivo. El enunciado pedía "cómo se
 *   reconstruye el resultado al volver de la recursión", y la respuesta no es
 *   trivial: la forma natural de escribirlo, invertir(charAt(0) + substring(1)),
 *   devuelve la cadena SIN invertir. Se agregó la demostración con "hola" porque
 *   la intuición falla justamente en este punto.
 * - Se agregó el análisis Θ(n²) temporal. El enunciado no lo pedía, pero es
 *   relevante: es el ÚNICO ejercicio de la guía que no es Θ(n), y la causa es
 *   la concatenación de Strings (O(n) en Java), no la recursión. Sin esta
 *   observación el alumno generaliza mal "toda recursión lineal es Θ(n)".
 * - Se justificó el caso base `length() <= 1` como una sola condición. Con dos
 *   casos separados (vacío y un carácter) el resultado sería idéntico; unificarlos
 *   deja claro que el criterio es "no queda nada por reordenar".
 * ========================================================================== */