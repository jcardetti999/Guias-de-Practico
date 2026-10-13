/*
Prompt inicial utilizado:
Implementa una cola enlazada simple de enteros con Nodo, head y tail. head es
el frente que sale primero y tail el final donde se agrega. Encolar enlaza el
nuevo nodo desde tail en O(1); si estaba vacia, head y tail apuntan al nuevo.
Desencolar avanza head y, si quito el ultimo nodo, tambien asigna tail=null.
Encolar, desencolar y consultar frente son O(1); contiene e imprimir son O(n).
Frente y desencolar vacios informan y devuelven -1. Sin tail, encolar requeriria
recorrer O(n). Demuestra FIFO, busqueda y cola vacia.
*/

/** Guia 6 — Ejercicio 6.2: cola enlazada de enteros. */
public class Ejercicio6_2 {
    private static final int NO_VALIDO = -1;

    private static class Nodo {
        private final int dato;
        private Nodo siguiente;
        private Nodo(int dato) { this.dato = dato; }
    }

    static class ColaEnteros {
        private Nodo head;
        private Nodo tail;
        private int size;

        public void encolar(int dato) {
            Nodo nuevo = new Nodo(dato);
            if (tail == null) {
                head = nuevo;
                tail = nuevo;
            } else {
                tail.siguiente = nuevo;
                tail = nuevo;
            }
            size++;
        }

        public int desencolar() {
            if (estaVacia()) {
                System.out.println("  No se puede desencolar: la cola esta vacia.");
                return NO_VALIDO;
            }
            int dato = head.dato;
            head = head.siguiente;
            size--;
            if (head == null) tail = null;
            return dato;
        }

        public int consultarFrente() {
            if (estaVacia()) {
                System.out.println("  No hay frente: la cola esta vacia.");
                return NO_VALIDO;
            }
            return head.dato;
        }

        public boolean estaVacia() { return head == null; }

        public boolean contiene(int buscado) {
            for (Nodo actual = head; actual != null; actual = actual.siguiente) {
                if (actual.dato == buscado) return true;
            }
            return false;
        }

        public int size() { return size; }

        public void imprimir() {
            System.out.print("frente -> ");
            for (Nodo actual = head; actual != null; actual = actual.siguiente) {
                System.out.print(actual.dato + " -> ");
            }
            System.out.println("null <- final");
        }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.2: COLA ENLAZADA DE ENTEROS ===");
        ColaEnteros cola = new ColaEnteros();
        System.out.println("desencolar vacia = " + cola.desencolar());
        System.out.println("frente vacia = " + cola.consultarFrente());
        for (int dato : new int[] { 10, 20, 30 }) cola.encolar(dato);
        System.out.print("Contenido: "); cola.imprimir();
        System.out.println("frente=" + cola.consultarFrente() + ", contiene(20)="
                + cola.contiene(20) + ", contiene(99)=" + cola.contiene(99));
        System.out.print("Orden FIFO: ");
        while (!cola.estaVacia()) System.out.print(cola.desencolar() + " ");
        System.out.println();
        System.out.println("Tras quitar el ultimo: vacia=" + cola.estaVacia()
                + ", size=" + cola.size());
        cola.encolar(40);
        System.out.print("Reutilizada tras quedar vacia: "); cola.imprimir();
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se cubrio el caso de desencolar el ultimo nodo y volver a encolar, para
  verificar que head y tail se limpian y se inicializan en conjunto.
*/