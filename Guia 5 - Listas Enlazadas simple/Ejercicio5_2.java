/*
Prompt inicial utilizado:
Implementá buscar(int dato) en una lista enlazada simple propia de enteros.
Los nodos contienen dato y siguiente; head apunta al primero o es null. La
búsqueda debe comenzar en head y avanzar por siguiente hasta hallar el dato o
llegar a null. No hay indexado directo como en un arreglo: el costo es O(n).
Probá lista vacía, dato al inicio, medio y final, y dato ausente; buscar no debe
modificar la lista ni su size.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 2. */
public class Ejercicio5_2 {
    private static class Nodo {
        private final int dato;
        private Nodo siguiente;
        private Nodo(int dato) { this.dato = dato; }
    }

    static class ListaEnlazadaEnteros {
        private Nodo head;
        private int size;

        public void insertarAlFinal(int dato) {
            Nodo nuevo = new Nodo(dato);
            if (head == null) {
                head = nuevo;
            } else {
                Nodo actual = head;
                while (actual.siguiente != null) actual = actual.siguiente;
                actual.siguiente = nuevo;
            }
            size++;
        }

        public boolean buscar(int dato) {
            Nodo actual = head;
            while (actual != null) {
                if (actual.dato == dato) return true;
                actual = actual.siguiente;
            }
            return false;
        }

        public int getSize() { return size; }

        public void imprimir() {
            Nodo actual = head;
            while (actual != null) {
                System.out.print(actual.dato + " -> ");
                actual = actual.siguiente;
            }
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 5.2: BUSCAR ELEMENTOS ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        System.out.println("Buscar en vacía: " + lista.buscar(10));
        for (int dato : new int[] { 10, 20, 30, 40 }) lista.insertarAlFinal(dato);
        lista.imprimir();
        for (int dato : new int[] { 10, 20, 40, 99 }) {
            System.out.println("buscar(" + dato + ") = " + lista.buscar(dato));
        }
        System.out.println("size no cambia: " + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- No fueron necesarios; las pruebas confirman que buscar informa presencia o
  ausencia sin cambiar nodos ni tamaño.
*/