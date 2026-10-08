/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un simulador simple de HISTORIAL DE NAVEGACION.
 *
 * OPERACIONES QUE DEBE TENER
 * - visitar(url): registra una pagina visitada.
 * - volverAtras(): saca la ultima pagina visitada del historial.
 * - paginaActual(): muestra la ultima pagina visitada, sin sacarla.
 * - cantidadPaginas(): cuantas paginas hay en el historial.
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Se usa una pila de String con indice `top` inicializado en 0.
 * Las URLs se guardan en un arreglo String[] de capacidad fija.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Sin historial: volverAtras() y paginaActual() informan, no fallan.
 * - Historial lleno: visitar(url) informa, no falla.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * El prompt debe explicar POR QUE el historial funciona bajo el principio LIFO.
 *
 * La razon: la pagina visitada mas recientemente es la que se abandona primero.
 * Cuando apretamos "atras" en un navegador, queremos volver a la ultima pagina
 * que estabamos, no a la primera de la sesion. Cada visita apila una URL, y cada
 * "atras" la desapila. Si fuera FIFO, "atras" nos llevaria a la pagina mas
 * antigua de la sesion, que no es lo que espera el usuario.
 *
 * Quiero que el codigo tenga comentarios explicando por que el historial es
 * LIFO y que pasa cuando no hay historial.
 *
 * También quiero probarlo con estos casos:
 *   - Visitar google.com, github.com, stackoverflow.com: la actual debe ser
 *     stackoverflow.com
 *   - Volver atras dos veces: deben salir stackoverflow.com y github.com
 *   - Sin historial: debe informar
 *   - Historial de capacidad 2 lleno: la tercera visita debe ser rechazada
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 4: Historial de navegación
 *
 * ESTRUCTURA — Pila de Strings (LIFO)
 * Cada visita apila una URL. El botón "atrás" desapila.
 *
 * POR QUE EL HISTORIAL ES LIFO
 * La página visitada más recientemente es la que se abandona primero. Cuando
 * apretamos "atrás" queremos volver a la última página que estábamos, NO a la
 * primera de la sesión. Cada visita apila una URL y cada "atrás" la desapila.
 *
 * Si fuera FIFO (una cola), "atrás" nos llevaría a la página más antigua de la
 * sesión, que no es lo que espera el usuario. Esa es la diferencia entre una
 * pila y una cola aplicada al mismo problema.
 *
 * ÍNDICES
 * `paginas` es un String[] de capacidad fija.
 * `top` es un contador de páginas almacenadas, inicializado en 0.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción: si no hay historial, se informa.
 *
 * SIMPLIFICACIÓN DOCUMENTADA
 *   Un navegador real NO hace pop() al apretar atrás: apila todas las visitas y
 *   después consulta una página por su índice, lo que permite volver adelante y
 *   recargar. Este ejercicio sólo modela el "atrás" con una pila, que es lo que
 *   pide la consigna. La simplificación queda declarada y no es un error.
 *
 * COMPLEJIDAD — todas las operaciones Θ(1)
 */
public class Ejercicio4 {

    /** Valor centinela devuelto cuando no hay historial. */
    public static final String SIN_HISTORIAL = "(sin historial)";

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    static class HistorialNavegacion {

        private final String[] paginas;
        private final int capacidad;
        private int top;      // contador de páginas: arranca en 0 (historial vacío)

        HistorialNavegacion(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.paginas = new String[capacidad];
            this.top = 0;
        }

        /**
         * Registra una página visitada.
         *
         * @return true si se registró, false si el historial estaba lleno
         */
        public boolean visitar(String url) {
            if (esLleno()) {
                System.out.println("      No se puede guardar \"" + url
                        + "\": el historial esta lleno (capacidad " + capacidad + ").");
                return false;
            }
            paginas[top] = url;
            top++;
            System.out.println("      [" + top + "] " + url);
            return true;
        }

        /**
         * Saca la última página visitada del historial.
         *
         * @return la URL, o SIN_HISTORIAL si no hay ninguna
         */
        public String volverAtras() {
            if (esVacio()) {
                System.out.println("      No se puede volver atras: el historial esta vacio.");
                return SIN_HISTORIAL;
            }
            top--;
            return paginas[top];
        }

        /**
         * Consulta la página actual sin sacarla del historial.
         *
         * @return la URL, o SIN_HISTORIAL si el historial está vacío
         */
        public String paginaActual() {
            if (esVacio()) {
                System.out.println("      No hay pagina actual: el historial esta vacio.");
                return SIN_HISTORIAL;
            }
            return paginas[top - 1];
        }

        /** @return true si no hay páginas */
        public boolean esVacio() {
            return top == 0;
        }

        /** @return true si el historial alcanzó su capacidad */
        public boolean esLleno() {
            return top == capacidad;
        }

        /** @return cantidad de páginas en el historial */
        public int cantidadPaginas() {
            return top;
        }

        /** Muestra el historial de la más antigua a la más reciente. */
        public String verHistorial() {
            if (esVacio()) {
                return "(vacio)";
            }
            StringBuilder texto = new StringBuilder("[");
            for (int i = 0; i < top; i++) {
                texto.append(paginas[i]);
                if (i < top - 1) {
                    texto.append(" | ");
                }
            }
            return texto.append("]  actual = ").append(paginas[top - 1])
                    .append("  (").append(top).append("/").append(capacidad).append(")")
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

        System.out.println("=== EJERCICIO 4: HISTORIAL DE NAVEGACIÓN ===");

        demostrarNavegacion();
        compararConCola();
        casoHistorialVacio();
        casoHistorialLleno();
        ejecutarInteractivo();
    }

    private static void demostrarNavegacion() {
        System.out.println();
        System.out.println("--- CASO 1: navegar y volver atrás ---");
        System.out.println();

        HistorialNavegacion historial = new HistorialNavegacion(5);
        System.out.println("  Historial nuevo: " + historial.verHistorial());

        String[] sitios = { "google.com", "github.com", "stackoverflow.com" };
        System.out.println();
        System.out.println("  Visitas:");
        for (String url : sitios) {
            historial.visitar(url);
        }

        System.out.println();
        System.out.println("  Estado: " + historial.verHistorial());
        System.out.println("  paginaActual(): " + historial.paginaActual()
                + "   (sin sacarla del historial)");
        System.out.println();

        System.out.println("  Ahora se aprieta \"atrás\" dos veces:");
        for (int i = 1; i <= 2; i++) {
            System.out.println("  volverAtras() #" + i + " -> " + historial.volverAtras()
                    + "  |  " + historial.verHistorial());
        }

        System.out.println();
        System.out.println("  Salieron stackoverflow.com y github.com: la última visitada");
        System.out.println("  fue la primera en abandonar. Eso es LIFO.");
    }

    private static void compararConCola() {
        System.out.println();
        System.out.println("--- POR QUÉ NO SERÍA UNA COLA ---");
        System.out.println();

        HistorialNavegacion historial = new HistorialNavegacion(5);
        historial.visitar("google.com");
        historial.visitar("github.com");
        historial.visitar("stackoverflow.com");

        System.out.println("  Se visitaron, en orden: google.com, github.com, stackoverflow.com");
        System.out.println();
        System.out.println("  Con una PILA (LIFO), dos \"atrás\" dan:");
        System.out.println("    1) stackoverflow.com   <- la última visitada");
        System.out.println("    2) github.com");
        System.out.println();

        System.out.println("  Con una COLA (FIFO), dos \"atrás\" darían:");
        System.out.println("    1) google.com          <- la MÁS ANTIGUA de la sesión");
        System.out.println("    2) github.com");
        System.out.println();
        System.out.println("  Nadie esperaría que \"atrás\" lo llevara al primer sitio");
        System.out.println("  que abrió de la sesión. Eso es exactamente lo que");
        System.out.println("  distingue una pila de una cola en este problema.");
    }

    private static void casoHistorialVacio() {
        System.out.println();
        System.out.println("--- CASO 2: historial VACÍO ---");

        HistorialNavegacion historial = new HistorialNavegacion(3);
        System.out.println("  esVacio(): " + historial.esVacio());
        System.out.println("  volverAtras():");
        System.out.println("      devolvio: " + historial.volverAtras());
        System.out.println("  paginaActual():");
        System.out.println("      devolvio: " + historial.paginaActual());
        System.out.println();
        System.out.println("  Ninguna operación lanzó excepción.");
    }

    private static void casoHistorialLleno() {
        System.out.println();
        System.out.println("--- CASO 3: historial LLENO (capacidad 2) ---");
        System.out.println();

        HistorialNavegacion historial = new HistorialNavegacion(2);
        historial.visitar("google.com");
        historial.visitar("github.com");
        System.out.println("  " + historial.verHistorial());
        System.out.println("  esLleno(): " + historial.esLleno());
        System.out.println();

        System.out.println("  Se intenta visitar una tercera página:");
        boolean ok = historial.visitar("stackoverflow.com");
        System.out.println("  visitar() devolvio: " + ok
                + "  |  páginas: " + historial.cantidadPaginas() + " (sigue en 2)");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad del historial: ");

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

            HistorialNavegacion historial = new HistorialNavegacion(capacidad);
            System.out.println("  Ingrese las URLs visitadas (EOF para terminar):");

            while (entrada.hasNextLine()) {
                String url = entrada.nextLine();
                if (url.isEmpty()) {
                    break;
                }
                historial.visitar(url);
                System.out.println("      " + historial.verHistorial());
            }

            System.out.println();
            System.out.println("  Estado final: " + historial.verHistorial());
            System.out.println("  Ahora se vuelve atrás hasta vaciar el historial:");

            while (!historial.esVacio()) {
                System.out.println("    volverAtras() -> " + historial.volverAtras()
                        + "  | quedan " + historial.cantidadPaginas());
            }
            System.out.println("  Historial vacío.");
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se justificó en el prompt por qué el historial es LIFO y no FIFO. La
 *   consigna lo pide explícitamente, y la versión inicial del prompt sólo
 *   decía "es LIFO" sin argumentar. La razón fuerte es que el botón "atrás"
 *   debe llevar a la última página visitada, no a la más antigua de la sesión.
 * - Se documentó la SIMPLIFICACIÓN: un navegador real no hace pop() al
 *   apretar atrás, sino que apila todas las visitas y luego consulta por índice
 *   (eso es lo que permite volver adelante y recargar). El enunciado pide
 *   modelar el atrás con una pila, y dejarlo declarado evita que la diferencia
 *   con un navegador real parezca un error.
 * - Se agregó la comparación explícita con lo que haría una cola. Ver el mismo
 *   recorrido de páginas tratado como pila y como cola, con los dos resultados
 *   enfrentados, convierte "es LIFO" en algo que se ve y no en una afirmación.
 * ========================================================================== */