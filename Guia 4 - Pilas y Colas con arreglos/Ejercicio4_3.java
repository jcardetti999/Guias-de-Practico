/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un programa que reciba una expresion matematica y
 * determine si los parentesis estan correctamente balanceados.
 *
 * OPERACIONES QUE DEBE TENER
 * - estaBalanceada(expresion): devuelve true si los parentesis cierran bien.
 * La funcion debe usar una pila para llevar la cuenta de los parentesis abiertos.
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * La pila es un arreglo de char[] con indice `top` inicializado en 0.
 * Cada parentesis abierto que aparece se apila (push).
 * Cada parentesis cerrado que aparece saca uno (pop).
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Si aparece un parentesis cerrado cuando la pila esta vacia: es INVALIDO,
 *   porque ese cerrado no tiene con cual abrirse.
 * - Si al terminar la expresion quedan parentesis abiertos en la pila: es
 *   INVALIDO, porque esos nunca se cerraron.
 * - Expresion vacia: es valida (no hay nada desbalanceado).
 * - Expresion sin parentesis: es valida.
 * - Ninguna operacion debe lanzar excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * "(5 + 3) * (2 + 1)"  -> VALIDO
 * "(5 + 3)) * (2 + 1"   -> INVALIDO (hay un cerrado de sobra)
 * "((2 + 3)"            -> INVALIDO (queda un abierto sin cerrar)
 *
 * Quiero que el codigo tenga comentarios explicando cuando se hace push, cuando
 * se hace pop, y por que la pila debe quedar VACIA al final para que la
 * expresion sea valida.
 *
 * También quiero probarlo con estos casos:
 *   - (5 + 3) * (2 + 1)  -> debe decir VALIDO
 *   - (5 + 3)) * (2 + 1   -> debe decir INVALIDO
 *   - ((2 + 3)            -> debe decir INVALIDO
 *   - sin parentesis: 5 + 3 -> VALIDO
 *   - expresion vacia      -> VALIDO
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 3: Validar parentesis balanceados
 *
 * ESTRUCTURA — Pila de caracteres
 * Se apila cada parentesis abierto y se desapila cada cerrado. Al final, la
 * expresion es valida si nunca hubo un cierre sin apertura Y la pila quedo vacia.
 *
 * POR QUE UNA PILA Y NO UN CONTADOR
 * Un contador NO alcanza: con un contador se sabe cuantos hay abiertos, pero no
 * si estan bien NESTED. Por ejemplo ")( " tiene el mismo conteo que "( )" pero
 * son invalida y valida respectivamente. La pila guarda el ORDEN de las
 * aperturas, que es justo lo que hace falta para validar el anidamiento.
 *
 * INDICES
 * `pila` es un char[] de capacidad fija.
 * `top` es un contador de elementos ocupados, inicializado en 0.
 *
 * LAS DOS CONDICIONES DE VALIDEZ
 *   1. Durante el recorrido: nunca se hace pop() sobre pila vacia. Si se
 *      intentara, ese cerrado no tiene apertura que lo corresponda -> invalida.
 *   2. Al terminar: la pila debe estar VACIA. Si quedara algo, hay aperturas
 *      que nunca se cerraron -> invalida.
 *
 * Las dos condiciones son necesarias: una sola no alcanza. La primera sola
 * aceptaria "((2+3)", y la segunda sola aceptaria ")(".
 *
 * ALCANCE
 *   Se validan UNICAMENTE los parentesis (). Los corchetes [] y las llaves {}
 *   quedan fuera del alcance pedido por la consigna, que habla de una
 *   expresion matematica con parentesis.
 *
 * COMPLEJIDAD
 * COMPLEJIDAD
 *   Temporal: Theta(n), con n = longitud de la expresion. Un solo recorrido.
 *   Espacio:  Theta(n) en el peor caso, cuando todos los parentesis se abren.
 */
public class Ejercicio4_3 {

    // ============================================================
    //  ESTRUCTURA: PILA DE CARACTERES
    // ============================================================

    static class PilaCaracteres {

        private final char[] elementos;
        private int top;

        PilaCaracteres(int capacidad) {
            this.elementos = new char[capacidad];
            this.top = 0;
        }

        void push(char c) {
            if (top == elementos.length) {
                System.out.println("      [push] Pila llena: se ignora '" + c + "'.");
                return;
            }
            elementos[top] = c;
            top++;
        }

        char pop() {
            if (top == 0) {
                return '\0';   // centinela: no hay nada que sacar
            }
            top--;
            return elementos[top];
        }

        boolean estaVacia() {
            return top == 0;
        }

        int cantidad() {
            return top;
        }
    }

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Indica si los parentesis de la expresion estan balanceados.
     *
     * @param expresion texto a validar
     * @return true si esta balanceada, false si no
     */
    public static boolean estaBalanceada(String expresion) {
        if (expresion == null || expresion.isEmpty()) {
            return true;   // no hay nada que desbalancear
        }

        PilaCaracteres pila = new PilaCaracteres(expresion.length());

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            // switch sobre el caracter: mas claro que una cadena de if.
            switch (c) {
                case '(':
                    // Apertura: se apila para poder exigir su cierre despues.
                    pila.push(c);
                    break;

                case ')':
                    // Cierre: hay que desapilar. Si no hay nada, este cierre
                    // no tiene apertura que lo corresponda.
                    if (pila.estaVacia()) {
                        System.out.println("      Posicion " + i
                                + ": hay un ')' sin ningun '(' abierto antes.");
                        return false;
                    }
                    pila.pop();
                    break;

                default:
                    // Cualquier otro caracter (digitos, operadores, espacios)
                    // no participa del balanceo.
                    break;
            }
        }

        // Segunda condicion: si quedo algo abierto, nunca se cerro.
        if (!pila.estaVacia()) {
            System.out.println("      Quedaron " + pila.cantidad()
                    + " parentesis abiertos sin cerrar.");
            return false;
        }

        return true;
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 4.3: PARENTESIS BALANCEADOS ===");

        probarCasosDelEnunciado();
        explicarPorQueNoAlcanzaUnContador();
        mostrarComoFunciona();
        ejecutarInteractivo();
    }

    private static void probarCasosDelEnunciado() {
        System.out.println();
        System.out.println("--- CASO 1: los dos ejemplos del enunciado ---");
        System.out.println();

        String[] expresiones = {
            "(5 + 3) * (2 + 1)",       // valido
            "(5 + 3)) * (2 + 1",        // invalido: cierre de sobra
            "((2 + 3)",                  // invalido: queda un abierto
            "5 + 3",                     // valido: sin parentesis
            "",                          // valido: expresion vacia
            "()",                        // valido
            "((()))",                    // valido: anidamiento profundo
            ")(",                        // invalido: cierra antes de abrir
            "(a + (b * (c - d)))"        // valido
        };

        for (String expresion : expresiones) {
            boolean resultado = estaBalanceada(expresion);
            System.out.println("  \"" + expresion + "\""
                    + "  ->  " + (resultado ? "VALIDO" : "INVALIDO"));
        }
    }

    private static void explicarPorQueNoAlcanzaUnContador() {
        System.out.println();
        System.out.println("--- POR QUE NO ALCANZA CON UN CONTADOR ---");
        System.out.println();

        System.out.println("  Estas dos expresiones tienen los MISMOS parentesis:");
        System.out.println("    \")(\"   -> 1 abre, 1 cierra");
        System.out.println("    \"()\"   -> 1 abre, 1 cierra");
        System.out.println();

        boolean invalida = estaBalanceada(")(");
        boolean valida = estaBalanceada("()");

        System.out.println("  Sin embargo: ");
        System.out.println("    \")(\"  ->  " + (invalida ? "INVALIDO" : "VALIDO")
                + "   (el cierre NO tiene apertura antes)");
        System.out.println("    \"()\"  ->  " + (valida ? "VALIDO" : "INVALIDO")
                + "   (la apertura precede al cierre)");
        System.out.println();
        System.out.println("  Un contador daria 0 en ambos casos. La pila si los distingue,");
        System.out.println("  porque guarda el ORDEN en que se abrieron.");
    }

    private static void mostrarComoFunciona() {
        System.out.println();
        System.out.println("--- EJEMPLO PASO A PASO: \"(a+b)*(c-d)\" ---");
        System.out.println();

        String expresion = "(a + b) * (c - d)";
        System.out.println("  Expresion: \"" + expresion + "\"");
        System.out.println();

        PilaCaracteres pila = new PilaCaracteres(expresion.length());
        System.out.printf("  %-4s %-6s %-14s %s%n", "pos", "caracter", "pila", "accion");
        System.out.println("  " + "-".repeat(46));

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            String accion = "";

            if (c == '(') {
                pila.push(c);
                accion = "push  (abre)";
            } else if (c == ')') {
                if (!pila.estaVacia()) {
                    pila.pop();
                    accion = "pop   (cierra)";
                } else {
                    accion = "pop   FALLA: pila vacia";
                }
            } else {
                accion = "ignorado";
            }

            System.out.printf("  %-4d %-8s %-14s %s%n",
                    i, "'" + c + "'", verPila(pila), accion);
        }

        System.out.println();
        System.out.println("  Al terminar, la pila quedo " + (pila.estaVacia()
                ? "VACIA" : "con elementos") + ": la expresion es "
                + (pila.estaVacia() ? "VALIDA." : "INVALIDA."));
    }

    /** Muestra el contenido de la pila de izquierda (fondo) a derecha (cima). */
    private static String verPila(PilaCaracteres pila) {
        if (pila.estaVacia()) {
            return "[]";
        }
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < pila.cantidad(); i++) {
            texto.append("(");
        }
        return texto.append("] (").append(pila.cantidad()).append(")").toString();
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Ingrese una expresion (EOF para salir): ");

            // Guarda de EOF: sin esto el programa muere con
            // NoSuchElementException si se ejecuta sin entrada por consola.
            if (!entrada.hasNextLine()) {
                System.out.println("No se ingreso ningun dato.");
                return;
            }

            String expresion = entrada.nextLine();
            System.out.println("  Expresion: \"" + expresion + "\"");

            boolean resultado = estaBalanceada(expresion);
            System.out.println();
            System.out.println("  Resultado: " + (resultado ? "VALIDO" : "INVALIDO"));
            System.out.println("  Theta(n) en tiempo, Theta(n) en espacio en el peor caso.");
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se justificó en el prompt por qué hace falta una pila y no un contador. La
 *   consigna pide "explicar cómo se usa una pila", pero la respuesta de fondo
 *   —que es lo que enseña— es que un contador no puede distinguir ")(" de "()":
 *   ambos tienen unabre y un cierre, pero sólo uno es válido. La pila guarda el
 *   ORDEN de las aperturas, que es justo lo que valida el anidamiento.
 * - SeTeaaron explícitamente las DOS condiciones de validez. Ninguna basta por
 *   sí sola: la primera (nunca hacer pop sobre vacía) acepta "((2+3)", y la
 *   segunda (pilar vacía al final) acepta ")(". Es un error clásico creer que con
 *   verificar una alcanza.
 * - Se documentó el alcance: se validan sólo paréntesis (), no corchetes ni
 *   llaves. El enunciado habla de una expresión matemática, y dejarlo explícito
 *   evita que alguien lo tome por un validador completo de delimitadores.
 * - Se agregó la demostración con ")(" vs "()" para hacer visible por qué el
 *   orden importa. Sin ese par de ejemplos, la diferencia entre pila y contador
 *   queda como una afirmación sin respaldo.
 * ========================================================================== */