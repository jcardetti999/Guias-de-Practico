/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una PILA DE ENTEROS usando un arreglo de tamaño
 * fijo.
 *
 * OPERACIONES QUE DEBE TENER
 * - push(x): agrega x a la cima de la pila, si no está llena.
 * - pop():   quita y devuelve el elemento de la cima, si no está vacía.
 * - peek():  devuelve el elemento de la cima SIN quitarlo, si no está vacía.
 * - isEmpty(): indica si la pila no tiene elementos.
 * - isFull():  indica si la pila alcanzó su capacidad máxima.
 * - size():   devuelve cuántos elementos hay en la pila.
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Se usa un arreglo int[] de tamaño fijo, llamado `elementos`, y un índice
 * llamado `top`.
 *
 * `top` es un CONTADOR de elementos ocupados, y su valor inicial es 0.
 * NO es la posición del elemento del tope, sino "cuántos elementos hay".
 * El elemento del tope está en `elementos[top - 1]`.
 *
 * Con esa convención:
 *   - la pila está VACÍA cuando top == 0
 *   - la pila está LLENA cuando top == capacidad
 *
 * ¿Por qué un contador y no la posición? Porque como contador, los rangos son
 * [0, top-1] y [top, capacidad-1]: ambos inclusivos y sin valores negativos.
 * Si `top` fuera la posición, la pila vacía tendría top == -1 y cada acceso
 * necesitaría un if para no leer la posición -1.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - push sobre pila llena: NO debe hacer nada, debe informar con un mensaje.
 * - pop sobre pila vacía: NO debe devolver nada, debe informar con un mensaje.
 * - peek sobre pila vacía: NO debe devolver nada, debe informar con un mensaje.
 * - Pila de capacidad 1: se puede agregar uno y quitarlo, no más.
 * - Ninguna de estas operaciones puede lanzar una excepción.
 *
 * COMPORTAMIENTO ESPERADO
 * La pila es LIFO: sale último en entrar, primero en salir. Un ejemplo de la
 * consigna: si se agregan 10, 3, 7, 9 en ese orden y luego se pide pop()
 * cuatro veces, se obtiene 9, 7, 3, 10 en ese orden.
 *
 * Quiero que el código tenga comentarios explicando el índice top, su valor
 * inicial, por qué la pila está LIFO, y qué pasa en los casos límite.
 *
 * También quiero probarlo con estos casos:
 *   - Agregar 10, 3, 7, 9 y sacarlos todos: deben salir 9, 7, 3, 10.
 *   - Pila vacía: pop() y peek() deben informar, no fallar.
 *   - Pila de capacidad 2 llena: el tercer push debe informar que está llena.
 *   - Verificar que size() coincide con la cantidad de elementos agregados.
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 1: Implementar una pila de enteros
 *
 * ESTRUCTURA — Pila (LIFO: Last In, First Out)
 * Una pila es una colección donde el ÚLTIMO elemento en entrar es el PRIMERO en
 * salir. Es el mismo principio que una pila de platos: se apila encima y se
 * saca siempre del tope.
 *
 * ÍNDICES
 * `elementos` es el arreglo interno, de capacidad FIJA.
 * `top` es un contador de elementos ocupados, inicializado en 0.
 * El elemento de la cima está en elementos[top - 1].
 *
 * OPERACIONES — contrato
 *   push(x)  — agrega x si hay lugar. Devuelve void.
 *   pop()    — quita y devuelve la cima. Devuelve NO_VALIDO si está vacía.
 *   peek()   — devuelve la cima sin quitarla. Devuelve NO_VALIDO si está vacía.
 *   isEmpty()— true si top == 0.
 *   isFull() — true si top == capacidad.
 *   size()   — devuelve top.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción. Cuando la pila está vacía o llena, se
 *   informa con un mensaje y la operación no hace nada. Es la diferencia entre
 *   una pila bien implementada y una que revienta el programa.
 *
 * COMPLEJIDAD — todas las operaciones Θ(1)
 *   No hay ningún bucle: cada operación toca una sola posición del arreglo.
 *   Θ(1) temporal y Θ(1) espacio auxiliar (el arreglo es la estructura en sí).
 */
public class Ejercicio1 {

    /** Valor centinela devuelto cuando pop o peek se invocan sobre pila vacía. */
    public static final int NO_VALIDO = -1;

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    /**
     * Pila de enteros de capacidad fija, implementada sobre un arreglo.
     */
    static class PilaEnteros {

        private final int[] elementos;
        private final int capacidad;

        /**
         * `top` es un CONTADOR de elementos ocupados, inicializado en 0.
         *
         * Es la decisión central de este ejercicio. Con `top` como contador:
         *   - vacía ⟺ top == 0
         *   - llena ⟺ top == capacidad
         *   - la cima está en elementos[top - 1]
         *
         * Si en cambio `top` fuera la POSICIÓN del tope, la pila vacía
         * tendría top == -1 y cada lectura del tope necesitaría comprobar que
         * no sea negativo.
         */
        private int top;

        /**
         * @param capacidad cantidad máxima de elementos que admite la pila
         */
        PilaEnteros(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.elementos = new int[capacidad];
            this.top = 0;          // PILA VACÍA al arrancar
        }

        /**
         * Agrega un elemento a la cima.
         *
         * @param valor elemento a agregar
         * @return true si se agregó, false si la pila estaba llena
         */
        public boolean push(int valor) {
            if (isFull()) {
                // Caso límite: no hay excepción, se informa y no se hace nada.
                System.out.println("      [push] No se puede agregar " + valor
                        + ": la pila esta llena (capacidad " + capacidad + ").");
                return false;
            }
            elementos[top] = valor;    // se ocupa la posición top
            top++;                    // y el contador avanza
            return true;
        }

        /**
         * Quita y devuelve el elemento de la cima.
         *
         * @return el elemento de la cima, o NO_VALIDO si la pila está vacía
         */
        public int pop() {
            if (isEmpty()) {
                System.out.println("      [pop] No se puede sacar: la pila esta vacia.");
                return NO_VALIDO;
            }
            top--;                    // el contador baja: el elemento sale
            return elementos[top];     // y se lee desde la nueva cima
        }

        /**
         * Devuelve el elemento de la cima SIN quitarlo.
         *
         * @return el elemento de la cima, o NO_VALIDO si la pila está vacía
         */
        public int peek() {
            if (isEmpty()) {
                System.out.println("      [peek] No se puede consultar: la pila esta vacia.");
                return NO_VALIDO;
            }
            return elementos[top - 1];   // la cima está una posición antes del contador
        }

        /** @return true si la pila no tiene elementos */
        public boolean isEmpty() {
            return top == 0;
        }

        /** @return true si la pila alcanzó su capacidad máxima */
        public boolean isFull() {
            return top == capacidad;
        }

        /** @return cuántos elementos hay en la pila */
        public int size() {
            return top;
        }

        /** @return la capacidad máxima de la pila */
        public int getCapacidad() {
            return capacidad;
        }

        /**
         * Devuelve el contenido de la pila de la BASE a la CIMA.
         * Sirve para verificar el estado interno.
         */
        public String verEstado() {
            if (isEmpty()) {
                return "vacia";
            }
            StringBuilder texto = new StringBuilder("[");
            for (int i = 0; i < top; i++) {
                texto.append(elementos[i]);
                if (i < top - 1) {
                    texto.append(", ");
                }
            }
            return texto.append("]  (cima = ").append(elementos[top - 1])
                    .append(", top = ").append(top).append("/").append(capacidad).append(")").toString();
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

        System.out.println("=== EJERCICIO 1: PILA DE ENTEROS ===");

        demostrarOperacionesBasicas();
        demostrarCasoLimitePilaVacia();
        demostrarCasoLimitePilaLlena();
        demostrarCapacidadMinima();
        ejecutarInteractivo();
    }

    private static void demostrarOperacionesBasicas() {
        System.out.println();
        System.out.println("--- CASO 1: LIFO con los datos del enunciado ---");
        System.out.println();

        PilaEnteros pila = new PilaEnteros(5);
        System.out.println("  Pila creada con capacidad " + pila.getCapacidad() + ".");
        System.out.println("  Estado inicial : " + pila.verEstado());
        System.out.println("  isEmpty()      : " + pila.isEmpty()
                + "  |  size(): " + pila.size());
        System.out.println();

        int[] aAgregar = { 10, 3, 7, 9 };
        for (int valor : aAgregar) {
            pila.push(valor);
            System.out.println("  push(" + valor + ") -> estado: " + pila.verEstado());
        }

        System.out.println();
        System.out.println("  Ahora se sacan todos con pop(). El orden debe ser");
        System.out.println("  9, 7, 3, 10 (ultimo en entrar, primero en salir):");
        System.out.println();

        while (!pila.isEmpty()) {
            System.out.println("  pop() -> " + pila.pop()
                    + "  | estado restante: " + pila.verEstado());
        }

        System.out.println();
        System.out.println("  isEmpty() al final: " + pila.isEmpty()
                + "  |  size(): " + pila.size());
    }

    private static void demostrarCasoLimitePilaVacia() {
        System.out.println();
        System.out.println("--- CASO 2: pila VACIA ---");
        System.out.println("  Con la pila vacia, pop() y peek() deben informar, no fallar.");
        System.out.println();

        PilaEnteros pila = new PilaEnteros(3);
        System.out.println("  isEmpty(): " + pila.isEmpty());
        System.out.println("  pop():");
        System.out.println("      valor devuelto: " + pila.pop() + "   (NO_VALIDO = " + NO_VALIDO + ")");
        System.out.println("  peek():");
        System.out.println("      valor devuelto: " + pila.peek() + "   (NO_VALIDO = " + NO_VALIDO + ")");
        System.out.println();
        System.out.println("  El programa sigue funcionando: ninguna operacion lanzo excepcion.");
    }

    private static void demostrarCasoLimitePilaLlena() {
        System.out.println();
        System.out.println("--- CASO 3: pila LLENA (capacidad 2) ---");
        System.out.println();

        PilaEnteros pila = new PilaEnteros(2);
        pila.push(1);
        pila.push(2);
        System.out.println("  Estado: " + pila.verEstado());
        System.out.println("  isFull(): " + pila.isFull());

        System.out.println();
        System.out.println("  Se intenta agregar un tercer elemento:");
        boolean agregado = pila.push(99);
        System.out.println("  push(99) devolvio: " + agregado
                + "  |  size(): " + pila.size() + " (sigue en " + pila.getCapacidad() + ")");
        System.out.println();
        System.out.println("  El tercer valor fue rechazado y la pila quedo intacta.");
    }

    private static void demostrarCapacidadMinima() {
        System.out.println();
        System.out.println("--- CASO 4: capacidad minima ---");

        PilaEnteros deUno = new PilaEnteros(1);
        System.out.println("  Capacidad 1, vacia: isEmpty()=" + deUno.isEmpty()
                + " isFull()=" + deUno.isFull());
        deUno.push(7);
        System.out.println("  push(7): isEmpty()=" + deUno.isEmpty()
                + " isFull()=" + deUno.isFull() + " peek()=" + deUno.peek());
        deUno.push(8);
        System.out.println("  push(8) sobre pila llena: size()=" + deUno.size()
                + " (sigue siendo 1)");
        System.out.println("  pop(): " + deUno.pop() + " -> isEmpty()=" + deUno.isEmpty());

        PilaEnteros deCero = new PilaEnteros(0);
        System.out.println();
        System.out.println("  Capacidad 0: isEmpty()=" + deCero.isEmpty()
                + " isFull()=" + deCero.isFull() + "  (una pila sin capacidad");
        System.out.println("  esta vacia Y llena a la vez: las dos condiciones se cumplen)");
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

            PilaEnteros pila = new PilaEnteros(capacidad);
            System.out.println("  Pila creada: " + pila.verEstado());
            System.out.println();
            System.out.println("  Ingrese valores para agregar (EOF para terminar):");

            while (entrada.hasNextInt()) {
                int valor = entrada.nextInt();
                pila.push(valor);
                System.out.println("    push(" + valor + ") -> " + pila.verEstado());
            }

            System.out.println();
            System.out.println("  Estado final: " + pila.verEstado());
            System.out.println("  Se vacia la pila con pop():");
            while (!pila.isEmpty()) {
                System.out.println("    pop() -> " + pila.pop());
            }
            System.out.println("  Pila vacia. size()=" + pila.size());
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se modificó el prompt inicial para que el bloque de casos límite sea más
 *   explícito sobre qué operación hace en cada situación. La consigna pedía
 *   "qué pasa cuando la pila está llena o vacía", pero la redacción inicial no
 *   distinguía entre push, pop y peek, que se comportan distinto.
 * - Se agregó el valor centinela NO_VALIDO al contrato de pop y peek. La
 *   consigna no lo pedía, pero un método que devuelve int necesita alguna
 *   señal que indique "no había nada". Sin eso no hay forma de distinguir un
 *   pop sobre pila vacía de un pop que devolvió accidentalmente el mismo valor.
 * - Se justificó el POR QUÉ de que `top` sea un contador y no la posición del
 *   tope. Es la decisión de diseño central del ejercicio y la consigna la pide
 *   explícitamente ("qué valor inicial debe tener"), pero la razón de fondo es
 *   lo que realmente enseña: con contador los rangos son inclusivos y no hay
 *   valores negativos que comprobar en cada acceso.
 * ========================================================================== */