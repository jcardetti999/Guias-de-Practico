/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que sume todos los dígitos
 * de un número entero positivo.
 *
 * Este problema puede resolverse recursivamente porque cada número se descompone
 * en su último dígito más el resto: el último dígito se obtiene con n % 10 y el
 * resto con n / 10. Esas dos operaciones, juntas, hacen que el problema se
 * reduzca en un dígito.
 *
 * El caso base es n == 0, que devuelve 0, porque cuando ya no queda ningún
 * dígito por sumar, no hay nada que agregar.
 *
 * El caso recursivo es sumarDigitos(n) = (n % 10) + sumarDigitos(n / 10), porque
 * en cada llamada se separa el último dígito (con módulo) y se sigue con el
 * número reducido (con división entera), y los resultados se combinan al volver
 * de la recursión sumando el dígito extraído.
 *
 * La función debe devolver int.
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo y la pila de llamadas.
 *
 * Un detalle importante que quiero que el prompt destaque: el caso base de este
 * ejercicio es n == 0 -> 0, y es DISTINTO del caso base del ejercicio de contar
 * dígitos, que era n < 10 -> 1. La diferencia parece mínima pero no lo es: al
 * contar, el 0 es un dígito y vale 1; al sumar, el 0 no aporta nada y vale 0.
 *
 * También quiero probarlo con estos casos:
 *   - 0 -> 0 (a diferencia del ejercicio anterior, que daba 1)
 *   - 7 -> 7 (un solo dígito)
 *   - 10 -> 1 (1+0)
 *   - 1234 -> 10 (1+2+3+4, caso típico)
 *   - 999 -> 27 (el máximo: 9+9+9)
 *   - validar el resultado con un ejemplo real: el ISBN-10 pondera los dígitos
 *     (el de la posición i se multiplica por 11-i) y exige total múltiplo de 11,
 *     donde la X representa 10. Ojo: la suma SIMPLE de dígitos no es el dígito
 *     de control del ISBN-10, sólo es la base de la cuenta.
 *   - número negativo -> debe informar que no es válido
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio 7: Sumar dígitos
 *
 * ESTRATEGIA — por qué es recursivo
 * Cada número se separa en dos partes con operaciones enteras elementalísimas:
 * `n % 10` aísla el último dígito y `n / 10` elimina ese dígito dejando el
 * resto. Con esas dos piezas el problema se descompone naturalmente: sumar los
 * dígitos ES sumar el último dígito más sumar los dígitos del número restante.
 *
 * CASO BASE — n == 0 -> 0
 * Cuando el número llegó a cero, no queda ningún dígito por sumar. El cero es
 * el elemento neutro de la suma.
 *
 * ⚠️ POR QUÉ EL CASO BASE ES n == 0 Y NO n < 10 COMO EN EL EJERCICIO 6
 * Es el punto más importante de este ejercicio. Los dos algoritmos son
 * geométricamente iguales (misma división, misma reducción), pero el caso base
 * cambia:
 *
 *   Ejercicio 6 (CONTAR):  base n < 10 -> 1.  El 0 ES UN DÍGITO, cuenta 1.
 *   Ejercicio 7 (SUMAR):   base n == 0 -> 0.  El 0 NO APORTA NADA, suma 0.
 *
 * Aplicar la base del ejercicio 6 aquí daría: sumarDigitos(7) = 7 + sumarDigitos(0)
 * = 7 + 1 = 8, que es FALSO. La respuesta correcta es 7.
 *
 * La causa profunda: la base debe tener el VALOR NEUTRO del operador con el que
 * se combina. Al sumar, ese neutro es 0. Al contar, el "neutro" es 1 porque cada
 * dígito se aporta con un 1 + delante. El criterio del caso base no es
 * arbitrario: lo determina el neutro de la operación que se acumula.
 *
 * CASO RECURSIVO — sumarDigitos(n) = (n % 10) + sumarDigitos(n / 10)
 * El módulo extrae el dígito de las unidades y la división reduce el número.
 * Los dos resultados se COMBINAN al volver de la recursión, sumando el dígito
 * extraído al total que devuelve la llamada interna.
 *
 * COMPLEJIDAD
 * Ecuación de recurrencia: T(n) = T(n / 10) + Θ(1)
 * Igual que en el ejercicio 6: cada llamada descarta un dígito.
 *   Temporal: Θ(log10 n).
 *   Espacial: Θ(log10 n) — un marco por dígito.
 *
 * PILA DE LLAMADAS
 * Con 1234: se apilan los marcos de 1234, 123, 12, 1 y 0. El del 0 es el caso
 * base y devuelve 0. Al desenrollarse: 1 devuelve (1%10) + 0 = 1; 12 devuelve
 * 2 + 1 = 3; 123 devuelve 3 + 3 = 6; 1234 devuelve 4 + 6 = 10. Cada marco
 * esperaba a todos los que tenía debajo.
 *
 * CASOS LÍMITE
 * - n = 0 -> 0 (caso base; acá el cero NO aporta nada)
 * - n entre 1 y 9 -> el propio número (un solo dígito)
 * - n = 1234 -> 10
 * - n = 999 -> 27 (máximo: nueve nueves)
 * - n negativo -> centinela NO_VALIDO (con negativos, el módulo y la división
 *   de Java operan sobre el complemento a dos, y el resultado no tiene sentido)
 */
public class Ejercicio7 {

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
     * Suma todos los dígitos de un número entero no negativo.
     *
     * @param n número entero no negativo
     * @return la suma de sus dígitos, o NO_VALIDO (-1) si n es negativo
     */
    public static int sumarDigitos(int n) {
        entrar();   // se apila un marco nuevo
        try {
            // La guarda de n < 0 va antes: con negativos, el módulo y la división
            // de Java operan sobre el complemento a dos, y el resultado sería
            // carente de sentido. Se rechaza explícitamente.
            if (n < 0) {
                return NO_VALIDO;
            }

            // --- CASO BASE ---
            // n == 0 y NO n < 10. El cero no aporta ningún dígito a la suma, y 0
            // es el neutro de la suma. Aplicar aquí la base del ejercicio 6
            // (n < 10 -> 1) haría que sumarDigitos(7) dé 8 en vez de 7.
            if (n == 0) {
                return 0;
            }

            // --- CASO RECURSIVO ---
            // n % 10 extrae el último dígito; n / 10 lo elimina. Los dos
            // resultados se combinan al volver: el dígito extraído se le suma
            // al total que devuelve la llamada interna.
            return (n % 10) + sumarDigitos(n / 10);
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
        demostrarContrasteConEjercicio6();
        validarDigitoDeControlIsbn();
        ejecutarInteractivo();
    }

    private static void demostrarCasosLimite() {
        System.out.println("=== EJERCICIO 7: SUMAR DÍGITOS ===");
        System.out.println();

        int[] casos = { 0, 7, 9, 10, 99, 1234, 999 };

        for (int n : casos) {
            reiniciarPila();
            int resultado = sumarDigitos(n);

            System.out.println("  sumarDigitos(" + String.format("%6d", n) + ") = " + resultado
                    + "  | llamadas: " + llamadasTotales
                    + " | profundidad: " + profundidadMaxima);
        }

        reiniciarPila();
        System.out.println();
        System.out.println("  sumarDigitos(-42) = " + sumarDigitos(-42) + "  (NO_VALIDO)");
        System.out.println();
        System.out.println("  CASO CLAVE: sumarDigitos(0) = 0, NO 1.");
        System.out.println("  En el ejercicio 6 el 0 valía 1 porque es un dígito que se cuenta.");
        System.out.println("  Acá vale 0 porque no aporta nada a una suma.");
    }

    private static void demostrarContrasteConEjercicio6() {
        System.out.println();
        System.out.println("--- CONTRASTE CON EL EJERCICIO 6 ---");
        System.out.println();
        System.out.printf("  %8s | %22s | %26s%n", "n", "digitos (ej. 6)", "sumarDigitos (ej. 7)");
        System.out.printf("  %8s | %22s | %26s%n", "", "base: n < 10 -> 1", "base: n == 0 -> 0");

        int[] pruebas = { 0, 5, 7, 9, 42, 1234 };
        for (int n : pruebas) {
            int cantidad = digitosParaComparar(n);
            int suma = sumarDigitos(n);
            System.out.printf("  %8d | %22d | %26d%n", n, cantidad, suma);
        }

        System.out.println();
        System.out.println("  Fíjate en la fila n = 0: el ejercicio 6 da 1, este da 0.");
        System.out.println("  Es la misma forma de algoritmo (dividir entre 10) con un caso");
        System.out.println("  base distinto, porque la operación que se acumula es distinta:");
        System.out.println("  contar usa neutro 1, sumar usa neutro 0.");
    }

    /** Réplica de la función del ejercicio 6, sólo para la comparación. */
    private static int digitosParaComparar(int n) {
        if (n < 0) {
            return -1;
        }
        if (n < 10) {
            return 1;
        }
        return 1 + digitosParaComparar(n / 10);
    }

    private static void validarDigitoDeControlIsbn() {
        System.out.println();
        System.out.println("--- APLICACIÓN REAL: EL ISBN-10 ---");
        System.out.println("  OJO con algo que suele confundirse: el dígito de control");
        System.out.println("  del ISBN-10 NO es la suma simple de los dígitos módulo 11.");
        System.out.println("  Es una suma PONDERADA: el dígito de la posición i se multiplica");
        System.out.println("  por (11 - i), y el total debe dar 0 módulo 11. La X representa 10.");
        System.out.println();

        String[]isbns = { "0306406152", "080442957X", "155860832X" };

        for (String isbn : isbns) {
            int ponderada = sumaPonderadaIsbn(isbn);
            boolean valido = (ponderada % 11) == 0;

            System.out.println("  ISBN-10: " + isbn);
            System.out.println("      suma simple de dígitos : "
                    + sumarDigitosDeTexto(isbn) + "   <- NO es el control");
            System.out.println("      suma ponderada (11-i)  : " + ponderada);
            System.out.println("      ponderada mod 11       : " + (ponderada % 11)
                    + "  -> " + (valido ? "VÁLIDO" : "INVÁLIDO"));
            System.out.println();
        }

        System.out.println("  El algoritmo de este ejercicio calcula la 'suma simple', que es");
        System.out.println("  la parte de base de cualquier esquema de control. La ponderación");
        System.out.println("  es un paso adicional sobre ese resultado.");
    }

    /**
     * Suma ponderada del ISBN-10: dígito i (de 1 a 10) por (11 - i).
     * El carácter 'X' representa el valor 10.
     */
    private static int sumaPonderadaIsbn(String isbn) {
        int total = 0;
        for (int i = 0; i < isbn.length(); i++) {
            int digito = valorDeCaracter(isbn.charAt(i));
            total += digito * (11 - (i + 1));
        }
        return total;
    }

    /** Convierte un carácter de ISBN a su valor numérico: 'X' vale 10. */
    private static int valorDeCaracter(char c) {
        if (c == 'X' || c == 'x') {
            return 10;
        }
        return c - '0';
    }

    /** Suma los dígitos de un texto, ignorando guiones. Usa la función recursiva. */
    private static int sumarDigitosDeTexto(String texto) {
        int suma = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= '0' && c <= '9') {
                suma += sumarDigitos(c - '0');
            }
        }
        return suma;
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese un numero entero no negativo (EOF para salir): ");
            while (entrada.hasNextInt()) {
                int n = entrada.nextInt();
                reiniciarPila();
                int resultado = sumarDigitos(n);

                if (n < 0) {
                    System.out.println("  " + n + " no es valido: solo se admiten numeros no negativos.");
                } else {
                    System.out.println("  sumarDigitos(" + n + ") = " + resultado);
                    System.out.println("      profundidad maxima de pila: " + profundidadMaxima);
                }
                System.out.print("Ingrese un numero entero no negativo (EOF para salir): ");
            }
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - Se-exportó prominentemente la diferencia del caso base con el ejercicio 6.
 *   El enunciado la pedía ("Ojo: el caso base es DISTINTO del de E6"), pero el
 *   prompt original la mencionaba como una advertencia suelta. Sin evidencia en
 *   el código, es fácil escribir el caso base equivocado por inercia, porque
 *   los dos algoritmos son geométricamente idénticos y sólo cambia una línea.
 * - Se añadió la regla general que explica POR QUÉ cambia: el caso base debe
 *   tener el valor neutro del operador con el que se acumula. Contar acumula con
 *   un "1 +", cuyo neutro es 1; sumar acumula con "+", cuyo neutro es 0. Esa
 *   regla generaliza a cualquier ejercicio futuro, y no sólo a estos dos.
 * - Se documentó el rechazo de negativos. Con negativos, n % 10 y n / 10 en Java
 *   operan sobre el complemento a dos y el resultado no tiene sentido, así que
 *   hace falta una guarda explícita.
 * - Se corrigió el ejemplo del ISBN. La primera versión del prompt decía que el
 *   dígito de control del ISBN-10 era "la suma de sus dígitos módulo 11", lo cual
 *   es FALSO: el esquema real pondera cada dígito por su posición (11 - i) y
 *   exige que el total sea múltiplo de 11. Con la suma simple, 030640615 daba 25
 *   módulo 11 = 3, cuando el dígito de control correcto es 2. Como el enunciado
 *   mencionaba el ISBN, era importante no dejarlo con una regla inventada.
 * ========================================================================== */