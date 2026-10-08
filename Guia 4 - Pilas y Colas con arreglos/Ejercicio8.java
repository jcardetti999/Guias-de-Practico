/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una COLA DE IMPRESIÓN usando arreglos.
 *
 * Cada documento tiene un NOMBRE y una cantidad de PÁGINAS.
 *
 * OPERACIONES QUE DEBE TENER
 * - agregarDocumento(nombre, paginas): encola el documento al final.
 * - imprimirSiguiente(): imprime el documento que está primero y lo saca.
 * - verProximoDocumento(): muestra cuál se imprimiría, sin imprimirlo.
 * - cantidadPendientes(): cuántos documentos faltan.
 * - paginasPendientes(): total de páginas que faltan imprimir.
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Cola sobre un arreglo de documentos. Indices:
 * - `front`: posicion del documento que se imprime.
 * - `rear`: posicion del proximo espacio libre.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Cola vacia: imprimirSiguiente() y verProximoDocumento() informan.
 * - Cola llena: agregarDocumento() informa.
 * - Documento con 0 o menos paginas: se rechaza, no se encola.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO
 * Los documentos se imprimen EN EL MISMO ORDEN en que se agregaron. Eso es
 * FIFO estricto: el primero que entra es el primero que sale.
 *
 * La impresion es de a un documento completo a la vez, no pagina por pagina
 * intercalando documentos. Quiero que el programa aclare esto, porque es la
 * diferencia entre una cola de documentos y una de paginas.
 *
 * Quiero que se lleve la cuenta de cuantas paginas se imprimieron en total y de
 * cuantas quedan.
 *
 * Probar con estos casos:
 *   - Documentos: "Informe" (10 paginas), "Curriculum" (2), "Factura" (5)
 *   - Se deben imprimir en ese orden: Informe, Curriculum, Factura
 *   - Al imprimir Informe quedan 8 paginas menos, y siguen pendiente 2 docs
 *   - Cola vacia: imprimirSiguiente() debe informar
 *   - Ver el proximo documento sin imprimirlo: sigue en la cola
 *   - Documento con 0 paginas: debe ser rechazado
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 8: Cola de impresión de documentos
 *
 * ESTRUCTURA — Cola de documentos (FIFO)
 * Cada documento tiene nombre y cantidad de páginas. Se imprimen en el mismo
 * orden en que se agregaron: el primero que entra es el primero que sale.
 *
 * UNA COLA DE DOCUMENTOS, NO DE PÁGINAS
 * La unidad de la cola es el documento completo, no la página. Al imprimir se
 * saca el documento de cabeza y se imprimen TODAS sus páginas de corrido.
 *
 * La diferencia importa: una cola de páginas mezclaría los documentos
 * (página 1 del Informe, página 1 del Curriculum, página 2 del Informe, ...)
 * y produciría un documento ilegible. Por eso cada entrada guarda nombre y
 * cantidad de páginas, y por eso el total de páginas es un acumulado.
 *
 * ÍNDICES
 * `documentos` es un Documento[] de capacidad fija.
 * `front` es la posición del documento que se imprimirá.
 * `rear` es la posición del próximo espacio libre.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción: se informa con un mensaje.
 *   Un documento con 0 o menos páginas se rechaza: imprimirlo no consumiría
 *   nada, ocuparía un lugar en la cola y haría avanzar el reloj sin motivo.
 *
 * COMPLEJIDAD
 *   Θ(1) encolar, Θ(1) imprimir, Θ(1) consultar primero.
 *   Θ(capacidad) el ver todos los pendientes: hay que recorrerlos.
 */
public class Ejercicio8 {

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    /** Un documento de la cola: nombre y cantidad de páginas. */
    record Documento(String nombre, int paginas) {
        @Override
        public String toString() {
            return nombre + " (" + paginas + " pag.)";
        }
    }

    static class ColaDeImpresion {

        private final Documento[] documentos;
        private final int capacidad;
        private int front;
        private int rear;

        /** Páginas ya impresas en total. Es un acumulado de toda la sesión. */
        private int paginasImpresas;

        ColaDeImpresion(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.documentos = new Documento[capacidad];
            this.front = 0;
            this.rear = 0;
            this.paginasImpresas = 0;
        }

        /**
         * Encola un documento al final de la cola.
         *
         * @return true si se encoló, false si no se pudo
         */
        public boolean agregarDocumento(String nombre, int paginas) {
            if (paginas <= 0) {
                System.out.println("      [agregar] \"" + nombre + "\" tiene " + paginas
                        + " paginas. No se encola: un documento sin paginas");
                System.out.println("      no imprimiría nada y ocuparía un lugar en la cola.");
                return false;
            }
            if (isLlena()) {
                System.out.println("      [agregar] No se puede encolar \"" + nombre
                        + "\": la cola esta llena (capacidad " + capacidad + ").");
                return false;
            }
            documentos[rear] = new Documento(nombre, paginas);
            rear++;
            return true;
        }

        /**
         * Imprime el primer documento y lo saca de la cola.
         *
         * @return el documento impreso, o null si no había nada
         */
        public Documento imprimirSiguiente() {
            if (isVacia()) {
                System.out.println("      [imprimir] No hay documentos en la cola.");
                return null;
            }
            Documento documento = documentos[front];
            front++;
            paginasImpresas += documento.paginas();
            System.out.println("      [imprimir] \"" + documento.nombre() + "\" -> "
                    + documento.paginas() + " pag. impresas");
            return documento;
        }

        /**
         * Muestra cuál se imprimiría, sin imprimirlo ni sacarlo.
         *
         * @return el documento, o null si la cola está vacía
         */
        public Documento verProximoDocumento() {
            if (isVacia()) {
                System.out.println("      [ver] No hay documentos en la cola.");
                return null;
            }
            return documentos[front];
        }

        /** @return true si no hay documentos */
        public boolean isVacia() {
            return front == rear;
        }

        /** @return true si la cola está llena */
        public boolean isLlena() {
            return front == 0 && rear == capacidad;
        }

        /** @return cuántos documentos faltan imprimir */
        public int cantidadPendientes() {
            return rear - front;
        }

        /**
         * @return total de páginas de los documentos que faltan.
         *         Es O(cantidad de pendientes): hay que recorrerlos para sumar.
         */
        public int paginasPendientes() {
            int total = 0;
            for (int i = front; i < rear; i++) {
                total += documentos[i].paginas();
            }
            return total;
        }

        /** @return páginas ya impresas en total */
        public int totalPaginasImpresas() {
            return paginasImpresas;
        }

        /** Muestra la cola en orden, del primero al último. */
        public String verCola() {
            if (isVacia()) {
                return "(vacia)";
            }
            StringBuilder texto = new StringBuilder();
            for (int i = front; i < rear; i++) {
                if (i > front) {
                    texto.append(" -> ");
                }
                texto.append(documentos[i]);
            }
            return texto.toString();
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

        System.out.println("=== EJERCICIO 8: COLA DE IMPRESIÓN ===");

        demostrarOrdenDeImpresion();
        clarifyDocumentQueue();
        casoColaVacia();
        casoDocumentoSinPaginas();
        ejecutarInteractivo();
    }

    private static void demostrarOrdenDeImpresion() {
        System.out.println();
        System.out.println("--- CASO 1: orden de impresión (FIFO estricto) ---");
        System.out.println();

        ColaDeImpresion cola = new ColaDeImpresion(6);
        System.out.println("  Cola: " + cola.verCola());

        cola.agregarDocumento("Informe", 10);
        System.out.println("  + Informe (10 pag.)");
        cola.agregarDocumento("Curriculum", 2);
        System.out.println("  + Curriculum (2 pag.)");
        cola.agregarDocumento("Factura", 5);
        System.out.println("  + Factura (5 pag.)");

        System.out.println();
        System.out.println("  Cola: " + cola.verCola());
        System.out.println("  Pendientes: " + cola.cantidadPendientes()
                + " documentos, " + cola.paginasPendientes() + " paginas");
        System.out.println();

        System.out.println("  verProximoDocumento(): " + cola.verProximoDocumento()
                + "   (NO se imprime todavía)");
        System.out.println("  La cola sigue teniendo " + cola.cantidadPendientes() + " documentos.");
        System.out.println();

        System.out.println("  Se imprime de a un documento completo:");
        cola.imprimirSiguiente();
        System.out.println("    Cola: " + cola.verCola());
        System.out.println("    Pendientes: " + cola.cantidadPendientes()
                + " documentos, " + cola.paginasPendientes() + " paginas");
        System.out.println("    Impresas hasta ahora: " + cola.totalPaginasImpresas() + " paginas");
        System.out.println();

        System.out.println("  Se vacía la cola:");
        while (!cola.isVacia()) {
            cola.imprimirSiguiente();
        }
        System.out.println("    Total impreso: " + cola.totalPaginasImpresas() + " paginas");
        System.out.println("    Verificacion: 10 + 2 + 5 = 17");
    }

    private static void clarifyDocumentQueue() {
        System.out.println();
        System.out.println("--- COLA DE DOCUMENTOS, NO DE PÁGINAS ---");
        System.out.println();

        System.out.println("  Cola de DOCUMENTOS (esta es):");
        System.out.println("    Informe (10) -> Curriculum (2) -> Factura (5)");
        System.out.println("    Se imprime Informe entero, luego Curriculum entero, etc.");
        System.out.println();
        System.out.println("  Cola de PÁGINAS (NO es esta):");
        System.out.println("    Informe p1, Curriculum p1, Informe p2, Curriculum p2, ...");
        System.out.println("    Los documentos se intercalan y el resultado es ilegible.");
        System.out.println();
        System.out.println("  Por eso la unidad de la cola es el documento completo:");
        System.out.println("  cada entrada guarda nombre y cantidad de paginas.");
    }

    private static void casoColaVacia() {
        System.out.println();
        System.out.println("--- CASO 2: cola VACÍA ---");

        ColaDeImpresion cola = new ColaDeImpresion(3);
        System.out.println("  isVacia(): " + cola.isVacia());
        System.out.println("  Cola: " + cola.verCola());
        System.out.println("  imprimirSiguiente() devuelve: " + cola.imprimirSiguiente());
        System.out.println("  verProximoDocumento() devuelve: " + cola.verProximoDocumento());
        System.out.println();
        System.out.println("  Ninguna operación lanzó excepción.");
    }

    private static void casoDocumentoSinPaginas() {
        System.out.println();
        System.out.println("--- CASO 3: documento con 0 páginas ---");
        System.out.println();

        ColaDeImpresion cola = new ColaDeImpresion(3);
        cola.agregarDocumento("Informe", 10);
        System.out.println("  Cola antes: " + cola.verCola());
        System.out.println();

        boolean ok = cola.agregarDocumento("Post-it", 0);
        System.out.println();
        System.out.println("  agregarDocumento(\"Post-it\", 0) devolvió: " + ok);
        System.out.println("  Cola después: " + cola.verCola());
        System.out.println("  Pendientes: " + cola.cantidadPendientes()
                + " documentos (el Post-it NO entró)");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad de la cola de impresion: ");

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

            ColaDeImpresion cola = new ColaDeImpresion(capacidad);
            System.out.println("  Ingrese documentos como \"nombre paginas\" (EOF para terminar):");

            while (entrada.hasNextLine()) {
                String linea = entrada.nextLine();
                if (linea.isEmpty()) {
                    break;
                }
                String[] partes = linea.trim().split("\\s+");
                if (partes.length < 2) {
                    System.out.println("    Linea ignorada (se esperaba \"nombre paginas\"): " + linea);
                    continue;
                }
                String nombre = partes[0];
                int paginas;
                try {
                    paginas = Integer.parseInt(partes[1]);
                } catch (NumberFormatException e) {
                    System.out.println("    Linea ignorada (paginas no numericas): " + linea);
                    continue;
                }
                if (cola.agregarDocumento(nombre, paginas)) {
                    System.out.println("    + " + nombre + " (" + paginas + " pag.)  |  Cola: "
                            + cola.verCola());
                }
            }

            System.out.println();
            System.out.println("  Total de paginas pendientes: " + cola.paginasPendientes());
            System.out.println("  Ahora se imprime todo, en orden de llegada:");
            while (!cola.isVacia()) {
                cola.imprimirSiguiente();
                System.out.println("      Cola: " + cola.verCola()
                        + "   | quedan " + cola.paginasPendientes() + " paginas");
            }
            System.out.println("  Total impreso: " + cola.totalPaginasImpresas() + " paginas");
            System.out.println("  Cola vacía.");
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se aclaró explícitamente la diferencia entre una cola de DOCUMENTOS y una
 *   cola de PÁGINAS. El prompt original decía "cola" y "acumular páginas", lo
 *   cual es ambiguo: acumular páginas sugiere una cola de páginas, y esa versión
 *   intercalaría los documentos. La unidad de la cola es el documento completo, y
 *   cada entrada guarda nombre y cantidad de páginas.
 * - El rechazo de documentos con 0 páginas se agregó como caso límite explícito.
 *   A document with no pages consumes nothing when printed, yet takes a slot in
*   cola y hace avanzar el reloj sin motivo. Encolarlo es un error, no un
 *   no-op inofensivo.
 * - Se agregó el contador de `páginas impresas hasta ahora`. Pedir "acumular el
 *   total" es ambiguo entre las páginas que faltan y las ya impresas. Ambas
 *   sirven, así que el código lleva las dos y la salida muestra la
 *   verificación 10 + 2 + 5 = 17.
 * - El prompt ahora pide dejar constancia, antes de implementar, que la
 *   impresión es de a un documento completo y no página por página. Decirlo de
 *   entrada evita escribir por error la versión intercalada.
 * ========================================================================== */