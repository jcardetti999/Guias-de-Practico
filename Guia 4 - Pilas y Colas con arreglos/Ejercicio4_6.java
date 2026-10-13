/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una COLA DE ENTEROS usando un arreglo.
 *
 * OPERACIONES QUE DEBE TENER
 * - enqueue(x): agrega x al final de la cola.
 * - dequeue(): quita y devuelve el elemento del principio.
 * - front(): devuelve el primer elemento sin quitarlo.
 * - isEmpty(), isFull(), size().
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Se usan DOS indices:
 * - `front`: la posicion del PRIMER elemento de la cola.
 * - `rear`: la posicion del PROXIMO ESPACIO LIBRE, es decir donde se escribira
 *   el siguiente elemento encolado.
 *
 * `rear` NO es la posicion del ultimo elemento: es una posicion UNO mas alla.
 * Por eso rear empieza en 0 tambien, porque al inicio no hay ningun elemento y
 * el primer espacio libre es justamente la posicion 0.
 *
 * Al encolar: se escribe en elementos[rear] y luego rear avanza.
 * Al desencolar: se lee elementos[front] y luego front avanza.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Cola vacia: dequeue() y front() informan, no fallan.
 * - Cola llena: enqueue() informa, no falla.
 * - Vacia cuando front == rear.
 * - Llena cuando front == 0 y rear == capacidad.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * La cola es FIFO: sale primero el primero que entro. Si se encolan 10, 20, 30
 * en ese orden y se desencolan tres veces, se obtiene 10, 20, 30.
 *
 * Quiero que el codigo tenga comentarios explicando para que sirven los indices
 * front y rear, por que rear es el proximo espacio libre y no la posicion del
 * ultimo elemento, y como se detecta si la cola esta vacia o llena.
 *
 * También quiero probarlo con estos casos:
 *   - Encolar 10, 20, 30, 40 y desencolar: deben salir 10, 20, 30, 40
 *   - Cola vacia: dequeue() y front() deben informar
 *   - Cola de capacidad 3 llena: el cuarto enqueue debe informar
 *   - Verificar que front() coincide con el elemento que sale primero
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 6: Cola de enteros
 *
 * ESTRUCTURA — Cola (FIFO: First In, First Out)
 * Sale primero el primero que entró. Es lo contrario de la pila: en vez de un
 * índice `top` que crece hacia atrás, hay dos índices que avanzan en sentidos
 * distintos, formando una FRANJA entre ambos.
 *
 * LOS DOS ÍNDICES
 * `front` — posición del primer elemento. Avanza al desencolar.
 * `rear`  — posición del próximo espacio LIBRE. Avanza al encolar.
 *
 * POR QUÉ rear ES "PRÓXIMO LIBRE" Y NO "ÚLTIMO ELEMENTO"
 * Es la convención que hace que la implementación sea limpia:
 *
 *   - Si rear fuera la posición del último elemento, al encolar habría que
 *     escribir en rear+1 y encolar en la posición 0 habría que jugar con un
 *     caso especial. Con rear como "próximo libre", encolar SIEMPRE es
 *     "escribir en rear, luego rear++", sin excepciones.
 *
 * Lo mismo con front: como es "el primer elemento", desencolar SIEMPRE es
 * "leer de front, luego front++".
 *
 * LA FRANJA ÚTIL
 * Los elementos ocupa de `elementos[front]` hasta `elementos[rear - 1]`.
 * Si rear == front, la franja está vacía: la cola no tiene nada.
 * Si front == 0 y rear == capacidad, la franja llena todo el arreglo: la cola
 * está llena.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción: si la cola está vacía o llena, se
 *   informa con un mensaje.
 *
 * EL PROBLEMA DE ESTA IMPLEMENTACIÓN
 *   front y rear sólo avanzan, nunca retroceden. Los espacios que quedan atrás
 *   de front quedan libres pero inservibles. Ese es el problema que demuestran
 *   los ejercicios 9 y 10.
 *
 * COMPLEJIDAD — todas las operaciones Θ(1)
 */
public class Ejercicio4_6 {

    /** Valor centinela devuelto cuando la cola está vacía. */
    public static final int COLA_VACIA = -1;

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    static class ColaEnteros {

        private final int[] elementos;
        private final int capacidad;

        // Ambos arrancan en 0: la cola empieza vacía, y a la vez el primer
        // elemento ocupa la posición 0 y el próximo espacio libre es la 0.
        private int front;
        private int rear;

        ColaEnteros(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.elementos = new int[capacidad];
            this.front = 0;
            this.rear = 0;
        }

        /**
         * Agrega un elemento al final de la cola.
         *
         * @return true si se agregó, false si la cola estaba llena
         */
        public boolean enqueue(int valor) {
            if (isFull()) {
                System.out.println("      [enqueue] No se puede agregar " + valor
                        + ": la cola esta llena (capacidad " + capacidad + ").");
                return false;
            }
            // rear es el PRÓXIMO ESPACIO LIBRE, así que ahí se escribe directo.
            elementos[rear] = valor;
            rear++;
            return true;
        }

        /**
         * Quita y devuelve el primer elemento de la cola.
         *
         * @return el elemento, o COLA_VACIA si no hay ninguno
         */
        public int dequeue() {
            if (isEmpty()) {
                System.out.println("      [dequeue] No se puede sacar: la cola esta vacia.");
                return COLA_VACIA;
            }
            // front es el PRIMER elemento, así que se lee de ahí.
            int valor = elementos[front];
            front++;
            return valor;
        }

        /**
         * Devuelve el primer elemento sin quitarlo.
         *
         * @return el elemento, o COLA_VACIA si la cola está vacía
         */
        public int front() {
            if (isEmpty()) {
                System.out.println("      [front] No se puede consultar: la cola esta vacia.");
                return COLA_VACIA;
            }
            return elementos[front];
        }

        /** @return true si no hay elementos (front == rear) */
        public boolean isEmpty() {
            return front == rear;
        }

        /** @return true si la cola ocuparía todo el arreglo */
        public boolean isFull() {
            return front == 0 && rear == capacidad;
        }

        /** @return cantidad de elementos */
        public int size() {
            return rear - front;
        }

        /** Muestra el arreglo con los índices, para ver la franja ocupada. */
        public String verEstado() {
            StringBuilder texto = new StringBuilder("[");
            for (int i = 0; i < capacidad; i++) {
                if (i > 0) {
                    texto.append(", ");
                }
                // Solo la franja entre front y rear tiene datos válidos.
                texto.append(i >= front && i < rear ? elementos[i] : "_");
            }
            return texto.append("]  front=").append(front)
                    .append(", rear=").append(rear)
                    .append(", size=").append(size()).append("/").append(capacidad)
                    .toString();
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

        System.out.println("=== EJERCICIO 4.6: COLA DE ENTEROS ===");

        demostrarFifo();
        mostrarComoAvanzanLosIndices();
        casoColaVacia();
        casoColaLlena();
        ejecutarInteractivo();
    }

    private static void demostrarFifo() {
        System.out.println();
        System.out.println("--- CASO 1: FIFO ---");
        System.out.println();

        ColaEnteros cola = new ColaEnteros(6);
        System.out.println("  Cola nueva: " + cola.verEstado());

        for (int valor : new int[] { 10, 20, 30, 40 }) {
            cola.enqueue(valor);
            System.out.println("  enqueue(" + valor + ") -> " + cola.verEstado());
        }

        System.out.println();
        System.out.println("  front() = " + cola.front() + "   (sin quitarlo)");
        System.out.println("  Se desencolan todos. Deben salir 10, 20, 30, 40:");
        System.out.println();

        while (!cola.isEmpty()) {
            System.out.println("  dequeue() -> " + cola.dequeue()
                    + "  | " + cola.verEstado());
        }
    }

    private static void mostrarComoAvanzanLosIndices() {
        System.out.println();
        System.out.println("--- CÓMO AVANZAN front Y rear ---");
        System.out.println();
        System.out.println("  Al encolar, rear avanza. Al desencolar, front avanza.");
        System.out.println("  La franja útil va de front (inclusive) hasta rear-1 (inclusive).");
        System.out.println();

        ColaEnteros cola = new ColaEnteros(8);
        System.out.println("  " + cola.verEstado() + "   <- vacía: front == rear");

        cola.enqueue(10);
        cola.enqueue(20);
        System.out.println("  " + cola.verEstado() + "   <- dos elementos");

        cola.dequeue();
        System.out.println("  " + cola.verEstado() + "   <- front avanzó");

        cola.dequeue();
        System.out.println("  " + cola.verEstado() + "   <- vacía otra vez, pero front NO volvió a 0");
        System.out.println();
        System.out.println("  Los espacios del inicio quedaron vacíos (underscore) y rear sigue");
        System.out.println("  en " + 2 + ". Esos espacios libres NO se pueden reutilizar: rear no");
        System.out.println("  retrocede. Ese es el problema que demuestra el ejercicio 9.");
    }

    private static void casoColaVacia() {
        System.out.println();
        System.out.println("--- CASO 2: cola VACÍA ---");

        ColaEnteros cola = new ColaEnteros(3);
        System.out.println("  isEmpty(): " + cola.isEmpty() + "  (front == rear)");
        System.out.println("  dequeue(): devuelve " + cola.dequeue());
        System.out.println("  front():   devuelve " + cola.front());
        System.out.println();
        System.out.println("  Ninguna operación lanzó excepción.");
    }

    private static void casoColaLlena() {
        System.out.println();
        System.out.println("--- CASO 3: cola LLENA (capacidad 3) ---");
        System.out.println();

        ColaEnteros cola = new ColaEnteros(3);
        cola.enqueue(1);
        cola.enqueue(2);
        cola.enqueue(3);
        System.out.println("  " + cola.verEstado());
        System.out.println("  isFull(): " + cola.isFull() + "  (front == 0 y rear == capacidad)");
        System.out.println();

        System.out.println("  Se intenta encolar un cuarto elemento:");
        boolean ok = cola.enqueue(99);
        System.out.println("  enqueue(99) devolvio: " + ok
                + "  | size(): " + cola.size() + " (sigue en 3)");
        System.out.println();
        System.out.println("  Ojo: la condición de llena exige front == 0. Si se desencola");
        System.out.println("  algo, front deja de ser 0 y la cola ya no puede llenarse aunque");
        System.out.println("  haya espacio: es el defecto que se ve mejor en el ejercicio 9.");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad de la cola: ");

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

            ColaEnteros cola = new ColaEnteros(capacidad);
            System.out.println("  Ingrese valores para encolar (EOF para terminar):");

            while (entrada.hasNextInt()) {
                int valor = entrada.nextInt();
                cola.enqueue(valor);
                System.out.println("    enqueue(" + valor + ") -> " + cola.verEstado());
            }

            System.out.println();
            System.out.println("  Estado final: " + cola.verEstado());
            System.out.println("  Se vacía con dequeue():");

            while (!cola.isEmpty()) {
                System.out.println("    dequeue() -> " + cola.dequeue());
            }
            System.out.println("  Cola vacía. size() = " + cola.size());
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se aclaró en el prompt que `rear` es el próximo espacio LIBRE y no la
 *   posición del último elemento. La consigna lo pide explícitamente ("cómo
 *   cambian cuando se agregan o eliminan elementos"), pero la versión inicial
 *   del prompt sólo decía que rear avanzaba. La razón de fondo es lo que
 *   importa: con rear como próximo libre, encolar SIEMPRE es "escribir en rear,
 *   luego rear++", sin casos especiales para cuando toca la posición 0.
 * - Se justificó por qué front y rear arrancan AMBOS en 0. No es evidente para
 *   quien viene de la pila, donde top=0 significa "vacío". En la cola, front=0
 *   es la posición del primer elemento (aún no existe) y rear=0 es el primer
 *   espacio libre. Los dos en cero porque no hay elementos entre medio.
* - Se justificó por qué la condición de llena exige `front == 0`. Es una
 *   consecuencia oculta del diseño: si la franja no empieza en 0, hay espacio
 *   libre detrás de front pero la cola se declara llena igual. Es el defecto
 *   concreto que los ejercicios 9 y 10 vienen a mostrar.
 * - Se anticipó el defecto en la demostración. La versión inicial sólo
 *   mostraba el estado del arreglo, y el espacio desperdiciado pasaba
 *   inadvertido. Ahora el comentario lo anticipa y remite al ejercicio 9, que
 *   es donde se demuestra formalmente.
 *   mostraba el estado del arreglo, y el espacio desperdiciado pasaba
 *   inadvertido. Ahora el comentario lo anticipa y remite al ejercicio 9, que
 *   es donde se demuestra formalmente.
 * ========================================================================== */