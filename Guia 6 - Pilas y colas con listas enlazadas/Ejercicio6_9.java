/*
Prompt inicial utilizado:
Implementa una cola enlazada FIFO de trabajos de impresion. Cada Trabajo guarda
nombre de archivo, paginas y usuario. head es el siguiente trabajo que se
procesa; tail permite agregar en O(1). Agregar encola un trabajo completo,
consultarProximoArchivo no lo quita, imprimirProximo lo desencola y muestra
sus datos, y mostrarPendientes recorre en orden. Rechaza trabajos null y
operaciones vacias informan sin excepcion. Agregar, consultar el proximo e
imprimir el proximo son O(1); imprimir pendientes es O(n). Demuestra orden,
consulta sin quitar y que los tres campos se conservan.
*/

/** Guia 6 — Ejercicio 6.9: cola de impresion. */
public class Ejercicio6_9 {
    record TrabajoImpresion(String nombreArchivo, int paginas, String usuario) { }

    private static class Nodo {
        private final TrabajoImpresion trabajo;
        private Nodo siguiente;
        private Nodo(TrabajoImpresion trabajo) { this.trabajo = trabajo; }
    }

    static class ColaImpresion {
        private Nodo head;
        private Nodo tail;
        private int size;

        public boolean agregarTrabajo(TrabajoImpresion trabajo) {
            if (trabajo == null) {
                System.out.println("  Trabajo null rechazado.");
                return false;
            }
            Nodo nuevo = new Nodo(trabajo);
            if (tail == null) {
                head = nuevo;
                tail = nuevo;
            } else {
                tail.siguiente = nuevo;
                tail = nuevo;
            }
            size++;
            return true;
        }

        public TrabajoImpresion imprimirProximo() {
            if (head == null) {
                System.out.println("  No hay trabajos para imprimir.");
                return null;
            }
            TrabajoImpresion trabajo = head.trabajo;
            head = head.siguiente;
            size--;
            if (head == null) tail = null;
            System.out.println("  Imprimiendo " + trabajo.nombreArchivo() + ": "
                    + trabajo.paginas() + " paginas, usuario " + trabajo.usuario());
            return trabajo;
        }

        public String consultarProximoArchivo() {
            if (head == null) {
                System.out.println("  No hay archivo proximo: la cola esta vacia.");
                return null;
            }
            return head.trabajo.nombreArchivo();
        }

        public boolean estaVacia() { return head == null; }
        public int size() { return size; }

        public void mostrarPendientes() {
            System.out.print("frente -> ");
            for (Nodo actual = head; actual != null; actual = actual.siguiente) {
                System.out.print("[" + actual.trabajo + "] -> ");
            }
            System.out.println("null <- final");
        }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.9: COLA DE IMPRESION ===");
        ColaImpresion cola = new ColaImpresion();
        System.out.println("Proximo en vacia = " + cola.consultarProximoArchivo());
        cola.imprimirProximo();
        cola.agregarTrabajo(new TrabajoImpresion("informe.pdf", 8, "Ana"));
        cola.agregarTrabajo(new TrabajoImpresion("foto.png", 2, "Luis"));
        cola.agregarTrabajo(new TrabajoImpresion("guia.pdf", 15, "Mara"));
        cola.mostrarPendientes();
        System.out.println("Consultar sin quitar = " + cola.consultarProximoArchivo()
                + ", size=" + cola.size());
        while (!cola.estaVacia()) cola.imprimirProximo();
        System.out.println("Cola vacia=" + cola.estaVacia() + ", size=" + cola.size());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- La consulta del archivo siguiente se demuestra antes de imprimir, para dejar
  claro que consultar no desencola ni altera el orden FIFO.
*/