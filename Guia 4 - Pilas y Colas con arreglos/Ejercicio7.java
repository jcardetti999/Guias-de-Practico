/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un SISTEMA DE TURNOS para una oficina, usando una
 * cola de cadenas de texto.
 *
 * OPERACIONES QUE DEBE TENER
 * - llegar(nombre): asigna un turno a la persona y la agrega al final de la cola.
 * - atenderSiguiente(): saca de la cola a la persona que le toca ser atendida.
 * - primeroEnCola(): muestra quien esta primero, sin sacarlo.
 * - cantidadEnCola(): cuantas personas esperan.
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Se usa una cola sobre String[]. Los indices son:
 * - `front`: posicion del primer elemento (quien sera atendido).
 * - `rear`: posicion del proximo espacio libre.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Cola vacia: atenderSiguiente() y primeroEnCola() informan, no fallan.
 * - Cola llena: llegar() informa, no falla.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * El prompt debe explicar POR QUE este problema utiliza FIFO y no LIFO.
 *
 * La razon: el orden de llegada ES el orden de atencion. Si fuera una pila
 * (LIFO), se atenderia a la ultima persona que llego, lo cual seria
 * injusto: alguien que llego a las 9 seria atendido antes que alguien que
 * llego a las 8. La cola garantiza que se respeta el orden de llegada.
 *
 * DECISIÓN DOCUMENTADA
 * El enunciado no pide numerar los turnos, asi que el sistema usa el ORDEN DE
 * LLEGADA: la posicion en la cola ES el turno. Si una persona llega cuando la
 * cola esta vacia, su turno es el 1.
 *
 * Quiero que el codigo tenga comentarios explique por que el turno es FIFO y
 * no LIFO, y que la posicion en la cola es el numero de turno.
 *
 * También quiero probarlo con estos casos:
 *   - Llegan Ana, Pedro y Lucia: se deben atender en ese orden
 *   - Cola vacia: atenderSiguiente() debe informar
 *   - Consultar el primero sin sacarlo: el primero debe seguir en la cola
 *   - Cola de capacidad 2 llena: la tercera persona debe ser rechazada
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 7: Sistema de turnos para atención
 *
 * ESTRUCTURA — Cola de Strings (FIFO)
 * Las personas esperan en el orden en que llegaron, y se atienden en ese mismo
 * orden.
 *
 * POR QUÉ FIFO Y NO LIFO
 * El orden de llegada ES el orden de atención. Si fuera una pila (LIFO), se
 * atendería a la última persona que llegó, lo cual sería injusto: alguien que
 * llegó a las 9 sería atendido antes que alguien que llegó a las 8. La cola
 * garantiza que se respeta el orden de llegada.
 *
 * Es el mismo criterio que usa la cola del ejercicio 8 (impresora) y el
 * contrario del de la pila del ejercicio 2 (torre de platos). La estructura
 * no se elige por costumbre, sino por qué lado se extrae el elemento.
 *
 * DECISIÓN: EL TURNO ES LA POSICIÓN EN LA COLA
 * El enunciado no pide numerar los turnos, así que no se asignan números: el
 * turno de cada persona es su posición. Quien llega con la cola vacía tiene el
 * turno 1, quien llega después tiene el 2, y así sucesivamente.
 *
 * Una alternativa sería asignar un número de turno creciente a cada persona.
 * Se descarta porque requiere un contador adicional y no aporta información que
 * la posición no dé ya. Queda declarado para que la decisión sea explícita.
 *
 * ÍNDICES
 * `personas` es un String[] de capacidad fija.
 * `front` es la posición del primero (el que será atendido).
 * `rear` es la posición del próximo espacio libre.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción: se informa con un mensaje.
 *
 * COMPLEJIDAD — todas las operaciones Θ(1)
 */
public class Ejercicio7 {

    /** Valor centinela devuelto cuando no hay nadie esperando. */
    public static final String NADIE = "(nadie esperando)";

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    static class SistemaDeTurnos {

        private final String[] personas;
        private final int capacidad;
        private int front;
        private int rear;

        SistemaDeTurnos(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.personas = new String[capacidad];
            this.front = 0;
            this.rear = 0;
        }

        /**
         * Una persona llega y recibe su turno: se agrega al FINAL de la cola.
         *
         * @return true si se.agregó, false si la cola estaba llena
         */
        public boolean llegar(String nombre) {
            if (isLlena()) {
                System.out.println("      [llegar] No se puede agregar a \"" + nombre
                        + "\": la sala de espera esta llena (capacidad " + capacidad + ").");
                return false;
            }
            // rear es el próximo espacio libre: se escribe ahí y avanza.
            personas[rear] = nombre;
            rear++;
            return true;
        }

        /**
         * Atiende a la persona que le toca: la que llegó primero.
         *
         * @return el nombre atendido, o NADIE si no hay nadie
         */
        public String atenderSiguiente() {
            if (isVacia()) {
                System.out.println("      [atender] No hay nadie esperando.");
                return NADIE;
            }
            String nombre = personas[front];
            front++;
            return nombre;
        }

        /**
         * Consulta quién está primero sin atenderlo.
         *
         * @return el nombre, o NADIE si la cola está vacía
         */
        public String primeroEnCola() {
            if (isVacia()) {
                System.out.println("      [primero] No hay nadie esperando.");
                return NADIE;
            }
            return personas[front];
        }

        /** @return true si no hay nadie esperando */
        public boolean isVacia() {
            return front == rear;
        }

        /** @return true si la sala de espera está llena */
        public boolean isLlena() {
            return front == 0 && rear == capacidad;
        }

        /** @return cuántas personas esperan */
        public int cantidadEnCola() {
            return rear - front;
        }

        /**
         * Muestra la sala de espera con el número de turno de cada persona.
         * El número de turno es la posición: el que está en front tiene el 1.
         */
        public String verSalaDeEspera() {
            if (isVacia()) {
                return "(vacia)";
            }
            StringBuilder texto = new StringBuilder();
            for (int i = front; i < rear; i++) {
                if (i > front) {
                    texto.append(" | ");
                }
                texto.append("[").append(i - front + 1).append("] ").append(personas[i]);
            }
            return texto.append("   (esperando ").append(cantidadEnCola())
                    .append(" de ").append(capacidad).append(")")
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

        System.out.println("=== EJERCICIO 7: SISTEMA DE TURNOS ===");

        demostrarOrdenDeAtencion();
        compararConPila();
        casoSalaVacia();
        casoSalaLlena();
        ejecutarInteractivo();
    }

    private static void demostrarOrdenDeAtencion() {
        System.out.println();
        System.out.println("--- CASO 1: orden de atención ---");
        System.out.println();

        SistemaDeTurnos sistema = new SistemaDeTurnos(6);
        System.out.println("  Sala de espera: " + sistema.verSalaDeEspera());

        for (String nombre : new String[] { "Ana", "Pedro", "Lucia" }) {
            sistema.llegar(nombre);
            System.out.println("  " + nombre + " llega -> " + sistema.verSalaDeEspera());
        }

        System.out.println();
        System.out.println("  primeroEnCola(): " + sistema.primeroEnCola()
                + "   (NO se lo atiende todavía)");
        System.out.println("  Siguen esperando " + sistema.cantidadEnCola() + " personas.");
        System.out.println();

        System.out.println("  Ahora sí se atienden, uno por uno:");
        while (!sistema.isVacia()) {
            System.out.println("    -> Atendiendo a: " + sistema.atenderSiguiente()
                    + "   | " + sistema.verSalaDeEspera());
        }
    }

    private static void compararConPila() {
        System.out.println();
        System.out.println("--- POR QUÉ NO SERÍA UNA PILA ---");
        System.out.println();

        SistemaDeTurnos sistema = new SistemaDeTurnos(5);
        sistema.llegar("Ana");        // llega a las 8
        sistema.llegar("Pedro");      // llega a las 9
        sistema.llegar("Lucia");      // llega a las 10

        System.out.println("  Llegan Ana, Pedro y Lucia, en ese orden.");
        System.out.println("  Sala de espera: " + sistema.verSalaDeEspera());
        System.out.println();
        System.out.println("  Con una COLA (FIFO), se atenderían en el orden de llegada:");
        System.out.println("    1) Ana      (llegó a las 8)");
        System.out.println("    2) Pedro    (llegó a las 9)");
        System.out.println("    3) Lucia    (llegó a las 10)");
        System.out.println();

        System.out.println("  Con una PILA (LIFO), se atenderían en orden INVERSO:");
        System.out.println("    1) Lucia    (la ÚLTIMA en llegar, atendida primera)");
        System.out.println("    2) Pedro");
        System.out.println("    3) Ana      (la primera en llegar, atendida última)");
        System.out.println();
        System.out.println("  Con una pila, Ana esperó desde las 8 y sería atendida al final.");
        System.out.println("  Eso es injusto. El orden de llegada ES el turno.");
    }

    private static void casoSalaVacia() {
        System.out.println();
        System.out.println("--- CASO 2: sala VACÍA ---");

        SistemaDeTurnos sistema = new SistemaDeTurnos(3);
        System.out.println("  isVacia(): " + sistema.isVacia());
        System.out.println("  atenderSiguiente(): devuelve " + sistema.atenderSiguiente());
        System.out.println("  primeroEnCola():   devuelve " + sistema.primeroEnCola());
        System.out.println();
        System.out.println("  Ninguna operación lanzó excepción.");
    }

    private static void casoSalaLlena() {
        System.out.println();
        System.out.println("--- CASO 3: sala LLENA (capacidad 2) ---");
        System.out.println();

        SistemaDeTurnos sistema = new SistemaDeTurnos(2);
        sistema.llegar("Ana");
        sistema.llegar("Pedro");
        System.out.println("  " + sistema.verSalaDeEspera());
        System.out.println("  isLlena(): " + sistema.isLlena());
        System.out.println();

        System.out.println("  Llega una tercera persona:");
        boolean ok = sistema.llegar("Lucia");
        System.out.println("  llegar(\"Lucia\") devolvió: " + ok
                + "  |  esperando: " + sistema.cantidadEnCola() + " (sigue en 2)");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad de la sala de espera: ");

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

            // nextInt deja el salto de línea pendiente: hay que consumirlo antes
            // de pasar a nextLine(), o el primer nombre saldría vacío.
            entrada.nextLine();

            SistemaDeTurnos sistema = new SistemaDeTurnos(capacidad);
            System.out.println("  Ingrese los nombres de quien llega (EOF para terminar):");

            while (entrada.hasNextLine()) {
                String nombre = entrada.nextLine();
                if (nombre.isEmpty()) {
                    break;
                }
                sistema.llegar(nombre);
                System.out.println("    " + sistema.verSalaDeEspera());
            }

            System.out.println();
            System.out.println("  Ahora se atiende a todos por orden de llegada:");
            while (!sistema.isVacia()) {
                System.out.println("    Atendiendo a: " + sistema.atenderSiguiente()
                        + "  | quedan " + sistema.cantidadEnCola());
            }
            System.out.println("  Sala vacía.");
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se justificó en el prompt por qué el turno es FIFO y no LIFO. La consigna lo
 *   pide explícitamente, y la razón fuerte no es "porque las colas son FIFO"
 *   sino la justicia del orden: con una pila se atendería a quien llegó último,
 *   y alguien que llegó a las 9 sería atendido antes que alguien de las 8.
 * - Se justificó la decisión de NO numerar los turnos. El enunciado no lo pide, y
 *   la versión inicial del prompt lo daba por hecho. Como el sistema imprime
 *   números de turno, hace falta decir de dónde salen: son las posiciones en la
 *   cola. Queda explícito que la alternativa (un contador aparte) se descartó.
 * - Se justificó por qué se descarta la alternativa del contador. Sin esa frase,
 *   "no hay número de turno" parecería una omisión y no una decisión.
 * - Se agregó la comparación explícita con la pila mostrando los dos órdenes de
 *   atención. El mismo caso de tres personas atendido como cola y como pila,
 *   enfrentados, convierte la afirmación en algo comprobable.
 * ========================================================================== */