/*
Prompt inicial utilizado:
Implementa una cola enlazada de Cliente para un banco. Cliente tiene nombre,
numero de turno y motivo. head es quien sigue; tail es quien llego ultimo.
Agregar enlaza por tail en O(1), atender retira head en O(1), consultar no
retira y mostrar recorre el orden FIFO. Si se atiende el unico cliente, limpia
ambos extremos. Rechaza Cliente null. Agregar, atender y consultar son O(1);
imprimir la fila es O(n). Demuestra turnos en orden, consulta sin mutacion y
fila vacia; explica que el orden de llegada determina la atencion.
*/

/** Guia 6 — Ejercicio 6.6: cola de atencion de clientes. */
public class Ejercicio6_6 {
    record Cliente(String nombre, int numeroTurno, String motivo) { }

    private static class Nodo {
        private final Cliente cliente;
        private Nodo siguiente;
        private Nodo(Cliente cliente) { this.cliente = cliente; }
    }

    static class ColaAtencion {
        private Nodo head;
        private Nodo tail;
        private int size;

        public boolean agregarCliente(Cliente cliente) {
            if (cliente == null) {
                System.out.println("  Cliente null rechazado.");
                return false;
            }
            Nodo nuevo = new Nodo(cliente);
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

        public Cliente atenderProximo() {
            if (head == null) {
                System.out.println("  No hay clientes para atender.");
                return null;
            }
            Cliente cliente = head.cliente;
            head = head.siguiente;
            size--;
            if (head == null) tail = null;
            return cliente;
        }

        public Cliente consultarQuienSigue() {
            if (head == null) {
                System.out.println("  La fila esta vacia.");
                return null;
            }
            return head.cliente;
        }

        public boolean estaVacia() { return head == null; }
        public int size() { return size; }

        public void imprimirFila() {
            System.out.print("frente -> ");
            for (Nodo actual = head; actual != null; actual = actual.siguiente) {
                System.out.print("[" + actual.cliente + "] -> ");
            }
            System.out.println("null <- final");
        }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.6: ATENCION DE CLIENTES ===");
        ColaAtencion banco = new ColaAtencion();
        System.out.println("Atender fila vacia = " + banco.atenderProximo());
        banco.agregarCliente(new Cliente("Ana", 1, "deposito"));
        banco.agregarCliente(new Cliente("Luis", 2, "tarjeta"));
        banco.agregarCliente(new Cliente("Mara", 3, "prestamo"));
        banco.imprimirFila();
        System.out.println("Quien sigue (sin quitar) = " + banco.consultarQuienSigue());
        while (!banco.estaVacia()) {
            System.out.println("Atendido = " + banco.atenderProximo());
        }
        System.out.println("Fila vacia=" + banco.estaVacia() + ", size=" + banco.size());
        banco.agregarCliente(new Cliente("Nora", 4, "consulta"));
        System.out.print("Cola reutilizada: "); banco.imprimirFila();
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- La consulta del siguiente cliente se prueba antes de atenderlo para mostrar
  que consultar no cambia el orden ni elimina al frente.
*/