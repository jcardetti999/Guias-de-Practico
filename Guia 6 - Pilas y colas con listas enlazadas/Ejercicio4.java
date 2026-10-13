/*
Prompt inicial utilizado:
Implementa Cola<T> con Nodo<T>, head como frente y tail como final. Encolar
agrega tras tail en O(1), desencolar y consultarFrente son O(1), e imprimir es
O(n); si se quita el ultimo, head y tail quedan null. FIFO no cambia con T.
Rechaza null para reservarlo como resultado de consulta/desencolado vacio.
Demuestra una cola de nombres y otra de objetos Cliente, verificando orden,
frente y cola vacia.
*/

/** Guia 6 — Ejercicio 4: cola generica. */
public class Ejercicio4 {
    private static class Nodo<T> {
        private final T dato;
        private Nodo<T> siguiente;
        private Nodo(T dato) { this.dato = dato; }
    }

    static class Cola<T> {
        private Nodo<T> head;
        private Nodo<T> tail;
        private int size;

        public boolean encolar(T dato) {
            if (dato == null) {
                System.out.println("  No se encolan valores null.");
                return false;
            }
            Nodo<T> nuevo = new Nodo<>(dato);
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

        public T desencolar() {
            if (estaVacia()) {
                System.out.println("  No se puede desencolar: la cola esta vacia.");
                return null;
            }
            T dato = head.dato;
            head = head.siguiente;
            size--;
            if (head == null) tail = null;
            return dato;
        }

        public T consultarFrente() {
            if (estaVacia()) {
                System.out.println("  No hay frente: la cola esta vacia.");
                return null;
            }
            return head.dato;
        }

        public boolean estaVacia() { return head == null; }
        public int size() { return size; }

        public void imprimir() {
            System.out.print("frente -> ");
            for (Nodo<T> actual = head; actual != null; actual = actual.siguiente) {
                System.out.print(actual.dato + " -> ");
            }
            System.out.println("null <- final");
        }
    }

    record Cliente(String nombre, String motivo) { }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 4: COLA GENERICA ===");
        Cola<String> nombres = new Cola<>();
        nombres.encolar("Ana"); nombres.encolar("Luis"); nombres.encolar("Mara");
        System.out.print("Personas: "); nombres.imprimir();
        System.out.println("FIFO: " + nombres.desencolar() + ", "
                + nombres.desencolar() + ", " + nombres.desencolar());
        System.out.println("desencolar vacia = " + nombres.desencolar());

        Cola<Cliente> clientes = new Cola<>();
        clientes.encolar(new Cliente("Sofia", "cuenta"));
        clientes.encolar(new Cliente("Diego", "tarjeta"));
        System.out.println("Proximo cliente: " + clientes.consultarFrente());
        System.out.println("Atendido: " + clientes.desencolar());
        System.out.print("Pendiente: "); clientes.imprimir();
        System.out.println("Intento null: " + clientes.encolar(null));
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se agrego la comprobacion de head y tail al vaciar la cola, y se probaron
  elementos de dos tipos para verificar el mismo orden FIFO.
*/