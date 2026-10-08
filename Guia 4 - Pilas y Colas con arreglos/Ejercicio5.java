/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una PILA GENERICA Pila<T> usando arreglos, que
 * pueda almacenar distintos tipos de datos: Integer, String u objetos simples.
 *
 * OPERACIONES QUE DEBE TENER
 * - push(elemento): agrega un elemento de cualquier tipo.
 * - pop(): quita y devuelve el elemento de la cima.
 * - peek(): devuelve el elemento de la cima sin quitarlo.
 * - isEmpty(), isFull(), size().
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * El arreglo interno es Object[], NO int[]. Con Object[] una sola clase puede
 * guardar cualquier tipo de dato.
 *
 * El indice `top` es un contador de elementos ocupados, inicializado en 0.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Pila vacia: pop() y peek() informan, no fallan.
 * - Pila llena: push() informa, no falla.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * El prompt debe explicar QUE PROBLEMA resuelven los genericos y COMO se
 * diferencia de una pila hecha solamente con int.
 *
 * Una pila con int[] obliga a escribir una clase distinta para cada tipo: una
 * para int, otra para String, otra para objetos. Con Pila<T> se escribe UNA sola
 * clase que sirve para todos: Pila<Integer>, Pila<String>, Pila<Jugador>.
 *
 * El precio del casting: como el arreglo es Object[], al hacer pop() se recibe
 * un Object y hay que convertirlo a T con (T). Ese cast es obligatorio y es la
 * contrapartida de usar un arreglo de objetos.
 *
 * Quiero que el codigo tenga comentarios explicando el indice top, por que el
 * arreglo es Object[] y no int[], y por que el cast es necesario.
 *
 * También quiero probarlo con estos casos:
 *   - Pila<Integer>: agregar 10, 20, 30 y sacarlos deben dar 30, 20, 10
 *   - Pila<String>: agregar "hola" y "adios", sacar deben dar "adios", "hola"
 *   - Pila<Jugador>: agregar dos jugadores y sacar el ultimo
 *   - Pila vacia: pop() y peek() deben informar
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 5: Pila genérica
 *
 * ESTRUCTURA — Pila genérica sobre Object[]
 * La misma clase sirve para cualquier tipo: Pila<Integer>, Pila<String>,
 * Pila<Jugador>. No hace falta una clase por tipo.
 *
 * POR QUÉ Object[] Y NO int[]
 * Un arreglo int[] sólo puede guardar enteros. Para guardar Strings habría que
 * escribir StringPila; para objetos, JugadorPila. Eso es duplicar la misma
 * lógica una vez por cada tipo.
 *
 * Con Object[] el arreglo guarda cualquier referencia, y el tipo T se controla
 * en tiempo de compilación mediante la declaración de la clase. Es la misma
 * idea detrás de las colecciones genéricas de java.util.
 *
 * EL CAST ES OBLIGATORIO
 * Al hacer pop(), el arreglo devuelve un Object. Como el compilador no sabe en
 * tiempo de ejecución qué tipo hay ahí, hay que convertirlo:
 *
 *     T elemento = (T) elementos[--top];
 *
 * El warning existe porque el cast es "a ciegas" para el compilador: si alguien
 * mezclara tipos, fallaría en ejecución con ClassCastException. Es el precio de
 * que la pila sea genérica y su arreglo sea de objetos.
 *
 * ÍNDICES
 * `elementos` es un Object[] de capacidad fija.
 * `top` es un contador de elementos ocupados, inicializado en 0.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción: si la pila está vacía o llena, se
 *   informa con un mensaje.
 *
 * ALTERNATIVA DESCARTADA
 *   Podría usarse ArrayList<T> internamente y evitar el cast. La consigna pide
 *   arreglos, así que se usa Object[]. Queda declarado para que quede claro que
 *   la elección fue deliberada.
 *
 * COMPLEJIDAD — todas las operaciones Θ(1)
 */
public class Ejercicio5 {

    /** Objeto de ejemplo para demostrar que la pila funciona con tipos propios. */
    record Jugador(String nombre, int puntaje) {
        @Override
        public String toString() {
            return nombre + "(" + puntaje + ")";
        }
    }

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    /**
     * Pila genérica de capacidad fija sobre un arreglo de objetos.
     *
     * @param <T> tipo de los elementos que se guardan
     */
    static class Pila<T> {

        private final Object[] elementos;
        private final int capacidad;
        private int top;      // contador de elementos: arranca en 0

        Pila(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            // Object[] y no T[]: en Java NO se puede crear un arreglo de tipo
            // genérico directamente (new T[] es ilegal). El arreglo de objetos
            // es la única forma de usar el casting en una clase genérica.
            this.elementos = new Object[capacidad];
            this.top = 0;
        }

        /**
         * Agrega un elemento de cualquier tipo.
         *
         * @param elemento valor a agregar
         * @return true si se agregó, false si la pila estaba llena
         */
        public boolean push(T elemento) {
            if (isFull()) {
                System.out.println("      [push] No se puede agregar: la pila esta llena.");
                return false;
            }
            elementos[top] = elemento;
            top++;
            return true;
        }

        /**
         * Quita y devuelve el elemento de la cima.
         *
         * @return el elemento, o null si la pila está vacía
         */
        @SuppressWarnings("unchecked")
        public T pop() {
            if (isEmpty()) {
                System.out.println("      [pop] No se puede sacar: la pila esta vacia.");
                return null;
            }
            top--;
            // El cast (T) es necesario porque el arreglo es Object[].
            return (T) elementos[top];
        }

        /**
         * Devuelve el elemento de la cima sin quitarlo.
         *
         * @return el elemento, o null si la pila está vacía
         */
        @SuppressWarnings("unchecked")
        public T peek() {
            if (isEmpty()) {
                System.out.println("      [peek] No se puede consultar: la pila esta vacia.");
                return null;
            }
            return (T) elementos[top - 1];
        }

        /** @return true si no hay elementos */
        public boolean isEmpty() {
            return top == 0;
        }

        /** @return true si la pila alcanzó su capacidad */
        public boolean isFull() {
            return top == capacidad;
        }

        /** @return cuántos elementos hay */
        public int size() {
            return top;
        }

        /** @return la capacidad máxima */
        public int getCapacidad() {
            return capacidad;
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y símbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 5: PILA GENÉRICA ===");

        demostrarConEnteros();
        demostrarConStrings();
        demostrarConObjetos();
        casoPilaVacia();
        ejecutarInteractivo();
    }

    private static void demostrarConEnteros() {
        System.out.println();
        System.out.println("--- CASO 1: Pila<Integer> ---");
        System.out.println();

        Pila<Integer> pila = new Pila<>(3);
        System.out.println("  Pila<Integer> creada con capacidad " + pila.getCapacidad());

        for (int valor : new int[] { 10, 20, 30 }) {
            pila.push(valor);
        }
        System.out.println("  Se agregaron 10, 20 y 30. size() = " + pila.size());
        System.out.println("  peek() = " + pila.peek() + "  (sin sacarlo)");
        System.out.println();

        System.out.println("  Al popear deben salir 30, 20, 10:");
        while (!pila.isEmpty()) {
            System.out.println("    pop() -> " + pila.pop()
                    + "  | quedan " + pila.size());
        }
    }

    private static void demostrarConStrings() {
        System.out.println();
        System.out.println("--- CASO 2: Pila<String> ---");
        System.out.println();

        // La MISMA clase, distinto tipo de parámetro.
        Pila<String> pila = new Pila<>(2);
        pila.push("hola");
        pila.push("adios");

        System.out.println("  Pila<String> con \"hola\" y \"adios\".");
        System.out.println("  pop() -> " + pila.pop());
        System.out.println("  pop() -> " + pila.pop());
        System.out.println();
        System.out.println("  Sin cambiar una línea de la clase Pila.");
    }

    private static void demostrarConObjetos() {
        System.out.println();
        System.out.println("--- CASO 3: Pila<Jugador> (objeto propio) ---");
        System.out.println();

        Pila<Jugador> pila = new Pila<>(3);
        pila.push(new Jugador("Ana", 1200));
        pila.push(new Jugador("Lucia", 1500));

        System.out.println("  Pila<Jugador> con Ana y Lucia.");
        System.out.println("  peek() = " + pila.peek());
        System.out.println("  pop()  = " + pila.pop());
        System.out.println("  pop()  = " + pila.pop());
        System.out.println();
        System.out.println("  Funciona con un record propio: los genéricos no se limitan");
        System.out.println("  a Integer y String.");
    }

    private static void casoPilaVacia() {
        System.out.println();
        System.out.println("--- CASO 4: pila VACÍA ---");
        System.out.println();

        Pila<String> pila = new Pila<>(2);
        System.out.println("  isEmpty(): " + pila.isEmpty());
        System.out.println("  pop():   devuelve " + pila.pop());
        System.out.println("  peek():  devuelve " + pila.peek());
        System.out.println("  isFull(): " + pila.isFull() + "  (capacidad 2, vacía)");
        System.out.println();
        System.out.println("  pop() devuelve null, porque un método genérico no puede devolver");
        System.out.println("  un centinela válido para todos los tipos. El aviso por pantalla");
        System.out.println("  es el que informa que no había nada.");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad de la pila: ");

            // Guarda de EOF: sin esto el programa muere con
            // NoSuchElementException si se ejecuta sin entrada por consola.
            if (!entrada.hasNextInt()) {
                System.out.println("No se ingreso ningun dato.");
                return;
            }

            int capacidad = entrada.nextInt();
            if (capacidad < 0) {
                System.out.println("La capacidad no puede ser negativa.");
                return;
            }

            Pila<String> pila = new Pila<>(capacidad);
            System.out.println("  Se leen Strings y se apilan. EOF para terminar:");

            while (entrada.hasNextLine()) {
                String texto = entrada.nextLine();
                if (texto.isEmpty()) {
                    break;
                }
                pila.push(texto);
                System.out.println("    push(\"" + texto + "\") -> size() = " + pila.size());
            }

            System.out.println();
            System.out.println("  Se vacía la pila:");
            while (!pila.isEmpty()) {
                System.out.println("    pop() -> \"" + pila.pop() + "\"");
            }
            System.out.println("  Pila vacía: size() = " + pila.size());
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se justificó en el prompt por qué el arreglo interno es Object[] y no int[].
 *   La consigna pide "explicar qué problema resuelve el uso de genéricos", y la
 *   respuesta de fondo es precisamente ésa: una pila con int[] obliga a escribir
 *   una clase distinta por cada tipo de dato.
 * - Se justificó el cast (T). Es el precio inevitable de usar un arreglo de
 *   objetos en una clase genérica, y además genera un warning del compilador que
 *   conviene explicar: el cast es "a ciegas" y fallaría en ejecución con
 *   ClassCastException si alguien mezclara tipos.
 * - Se justificó por qué se usa @SuppressWarnings("unchecked"). Sin la
 *   explicación, el warning parece un error del alumno y no una consecuencia
 *   necesaria del diseño pedido por la consigna.
 * - Se justificó por qué pop() devuelve null y no un centinela. En el ejercicio 1
 *   se usaba NO_VALIDO porque el tipo era int; con genéricos no hay un valor
 *   centinela válido para todos los tipos, así que el método devuelve null y el
 *   aviso por pantalla es el que informa. La consigna pide que "ninguna
 *   operación lance excepción", no que devuelva un valor por defecto.
 * ========================================================================== */