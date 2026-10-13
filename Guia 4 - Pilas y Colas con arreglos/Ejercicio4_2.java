/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un SIMULADOR DE TORRE DE PLATOS usando una pila.
 *
 * OPERACIONES QUE DEBE TENER
 * - agregarPlato(x): coloca un plato en la cima de la pila.
 * - retirarPlato(): saca el plato de arriba.
 * - consultarSuperior(): indica que plato esta arriba, sin sacarlo.
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Se usa una pila sobre arreglo int[]. Cada plato se guarda con su numero.
 * El indice `top` es un contador de elementos ocupados, inicializado en 0.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Torre vacia: retirarPlato() y consultarSuperior() informan, no fallan.
 * - Torre llena: agregarPlato() informa, no falla.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * El prompt debe explicar POR QUE este problema se resuelve con una PILA y no
 * con una COLA.
 *
 * La razon: solo se puede tomar el ULTIMO plato agregado. En una torre, el de
 * arriba es el ultimo que se puso. Con una cola (FIFO) el primero que entro
 * seria el primero en salir, y ese esta en el FONDO de la torre: habria que
 * sacar antes todos los de encima, lo cual contradice la regla de la cola. La
 * estructura que permite acceder solo al ultimo agregado es la pila.
 *
 * Quiero que el codigo tenga comentarios explicando por que la torre es LIFO y
 * por que no puede modelarse con una cola, y que pasa cuando esta vacia.
 *
 * También quiero probarlo con estos casos:
 *   - Agregar platos 1, 2 y 3: el de arriba debe ser el 3.
 *   - Retirar los tres: deben salir 3, 2, 1.
 *   - Torre vacia al retirar: debe informar.
 *   - Torre de capacidad 2 llena: el tercer plato debe ser rechazado.
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 2: Simulador de torre de platos
 *
 * ESTRUCTURA — Pila (LIFO), y por que NO puede ser una cola
 * Una torre de platos es el ejemplo fisico de LIFO: solo se puede tomar el
 * ultimo plato que se coloco.
 *
 * POR QUE NO PUEDE SER UNA COLA
 * Una cola es FIFO: sale primero el primero que entro. En la torre, el primer
 * plato que entro esta en el FONDO. Para sacarlo con una cola habria que quitar
 * antes todos los que estan encima, lo cual contradice la regla de la cola
 * (que saca por el frente). La estructura que permite "acceder solo al ultimo
 * agregado" es la pila.
 *
 * INDICES
 * `elementos` es el arreglo de capacidad fija.
 * `top` es un contador de elementos ocupados, inicializado en 0.
 * El plato de arriba esta en elementos[top - 1].
 *
 * CASOS LIMITE
 *   Ninguna operacion lanza excepcion: si la torre esta vacia o llena, se
 *   informa con un mensaje y no se hace nada.
 *
 * COMPLEJIDAD — todas las operaciones Theta(1)
 */
public class Ejercicio4_2 {

    /** Valor centinela devuelto cuando no hay ningun plato para entregar. */
    public static final int SIN_PLATOS = -1;

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    static class TorreDePlatos {

        private final int[] platos;
        private final int capacidad;
        private int top;      // contador de platos: arranca en 0 (torre vacia)

        TorreDePlatos(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.platos = new int[capacidad];
            this.top = 0;
        }

        /**
         * Coloca un plato en la cima.
         *
         * @return true si se agrego, false si la torre estaba llena
         */
        public boolean agregarPlato(int numero) {
            if (esLlena()) {
                System.out.println("      No se puede agregar el plato " + numero
                        + ": la torre esta llena (capacidad " + capacidad + ").");
                return false;
            }
            platos[top] = numero;
            top++;
            return true;
        }

        /**
         * Saca el plato de la cima.
         *
         * @return el numero del plato, o SIN_PLATOS si la torre esta vacia
         */
        public int retirarPlato() {
            if (esVacia()) {
                System.out.println("      No se puede retirar: la torre esta vacia.");
                return SIN_PLATOS;
            }
            top--;
            return platos[top];
        }

        /**
         * Consulta el plato de la cima sin sacarlo.
         *
         * @return el numero del plato, o SIN_PLATOS si la torre esta vacia
         */
        public int consultarSuperior() {
            if (esVacia()) {
                System.out.println("      No se consultar: la torre esta vacia.");
                return SIN_PLATOS;
            }
            return platos[top - 1];
        }

        /** @return true si no hay platos */
        public boolean esVacia() {
            return top == 0;
        }

        /** @return true si la torre alcanzo su capacidad */
        public boolean esLlena() {
            return top == capacidad;
        }

        /** @return cantidad de platos en la torre */
        public int cantidadPlatos() {
            return top;
        }

        /**
         * Dibuja la torre de abajo hacia arriba, que es como se ve en realidad.
         * El plato de arriba queda a la derecha.
         */
        public String verTorre() {
            if (esVacia()) {
                return "(torre vacia)";
            }
            StringBuilder texto = new StringBuilder("fondo ");
            for (int i = 0; i < top; i++) {
                texto.append("[").append(platos[i]).append("]");
            }
            return texto.append(" cima  <- plato ").append(platos[top - 1])
                    .append("  (").append(top).append("/").append(capacidad).append(")")
                    .toString();
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 4.2: SIMULADOR DE TORRE DE PLATOS ===");

        agregarPlatos();
        retirarPlatos();
        casoTorreVacia();
        casoTorreLlena();
        ejecutarInteractivo();
    }

    private static void agregarPlatos() {
        System.out.println();
        System.out.println("--- CASO 1: agregar platos ---");
        System.out.println();

        TorreDePlatos torre = new TorreDePlatos(5);
        System.out.println("  Torre nueva: " + torre.verTorre());

        for (int numero : new int[] { 1, 2, 3 }) {
            torre.agregarPlato(numero);
            System.out.println("  agregar(" + numero + ") -> " + torre.verTorre());
        }

        System.out.println();
        System.out.println("  consultarSuperior(): " + torre.consultarSuperior()
                + "   (sin sacarlo)");
        System.out.println("  La torre sigue con " + torre.cantidadPlatos() + " platos.");
    }

    private static void retirarPlatos() {
        System.out.println();
        System.out.println("--- CASO 2: retirar los platos ---");
        System.out.println();

        TorreDePlatos torre = new TorreDePlatos(5);
        torre.agregarPlato(1);
        torre.agregarPlato(2);
        torre.agregarPlato(3);
        System.out.println("  Estado inicial: " + torre.verTorre());
        System.out.println();

        while (!torre.esVacia()) {
            System.out.println("  retirar() -> plato " + torre.retirarPlato()
                    + "  | " + torre.verTorre());
        }

        System.out.println();
        System.out.println("  Salieron 3, 2 y 1: el ultimo en entrar fue el primero en salir.");
        System.out.println("  Con una COLA habrian salido 1, 2 y 3, que es justo lo que NO");
        System.out.println("  se puede hacer con una torre de platos.");
    }

    private static void casoTorreVacia() {
        System.out.println();
        System.out.println("--- CASO 3: torre VACIA ---");

        TorreDePlatos torre = new TorreDePlatos(3);
        System.out.println("  esVacia(): " + torre.esVacia());
        System.out.println("  retirarPlato():");
        System.out.println("      devolvio: " + torre.retirarPlato()
                + "   (SIN_PLATOS = " + SIN_PLATOS + ")");
        System.out.println("  consultarSuperior():");
        System.out.println("      devolvio: " + torre.consultarSuperior()
                + "   (SIN_PLATOS = " + SIN_PLATOS + ")");
        System.out.println();
        System.out.println("  Ninguna operacion lanzo excepcion.");
    }

    private static void casoTorreLlena() {
        System.out.println();
        System.out.println("--- CASO 4: torre LLENA (capacidad 2) ---");
        System.out.println();

        TorreDePlatos torre = new TorreDePlatos(2);
        torre.agregarPlato(1);
        torre.agregarPlato(2);
        System.out.println("  " + torre.verTorre());
        System.out.println("  esLlena(): " + torre.esLlena());
        System.out.println();

        System.out.println("  Se intenta agregar un tercer plato:");
        boolean ok = torre.agregarPlato(3);
        System.out.println("  agregarPlato(3) devolvio: " + ok
                + "  |  cantidad: " + torre.cantidadPlatos() + " (sigue en 2)");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad de la torre: ");

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

            TorreDePlatos torre = new TorreDePlatos(capacidad);
            System.out.println("  Torre creada: " + torre.verTorre());
            System.out.println("  Ingrese numeros de plato (EOF para terminar):");

            while (entrada.hasNextInt()) {
                int numero = entrada.nextInt();
                torre.agregarPlato(numero);
                System.out.println("    agregar(" + numero + ") -> " + torre.verTorre());
            }

            System.out.println();
            System.out.println("  Ahora se retiran todos, de a uno:");
            while (!torre.esVacia()) {
                System.out.println("    retirar() -> plato " + torre.retirarPlato());
            }
            System.out.println("  Torre vacia: cantidad = " + torre.cantidadPlatos());
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se amplió el bloque de COMPORTAMIENTO ESPERADO con la explicación de por
 *   qué la torre NO puede modelarse con una cola. La consigna lo pide
 *   explícitamente ("el prompt debe explicar por qué este problema se resuelve
 *   con una pila y no con una cola"), y la versión inicial del prompt sólo
 *   mencionaba que era LIFO, sin argumentar por qué la alternativa es
 *   imposible. La razón fuerte es que con una cola el primer plato que entró
 *   —que está en el fondo— sería el primero en salir, y para alcanzarlo habría
 *   que quitar antes todos los de encima, contradiciendo la definición de cola.
 * - Se agregó el valor centinela SIN_PLATOS al contrato, igual que en el
 *   ejercicio 1. Sin él no hay forma de distinguir "no hay plato" de un plato
 *   que valga accidentalmente el mismo número.
 * - Se agregó el dibujo de la torre de abajo hacia arriba. Mostrar el estado
 *   interno es lo que hace visible que el último agregado queda ARRIBA, que es
 *   justamente lo que justifica el comportamiento LIFO.
 * ========================================================================== */