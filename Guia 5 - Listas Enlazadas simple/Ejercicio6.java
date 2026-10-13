/*
Prompt inicial utilizado:
Implementá eliminarEnPosicion(int posicion) en una lista enlazada simple de
enteros con head y size. Validá el índice antes de recorrer: sólo vale
0 <= posicion < size. Para posición 0, avanzá head. En otro caso, recorré hasta
el nodo anterior al que se elimina y enlazalo con el sucesor del eliminado.
Reducí size sólo cuando la operación es válida. Probá eliminar primero, medio y
último, además de lista vacía e índices inválidos. El nodo anterior es necesario
porque una lista simplemente enlazada no permite retroceder desde el nodo actual.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 6. */
public class Ejercicio6 {
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
            if (head == null) head = nuevo;
            else {
                Nodo actual = head;
                while (actual.siguiente != null) actual = actual.siguiente;
                actual.siguiente = nuevo;
            }
            size++;
        }

        public void eliminarEnPosicion(int posicion) {
            if (posicion < 0 || posicion >= size) {
                System.out.println("  Posición inválida: " + posicion + " (size=" + size + ")");
                return;
            }
            if (posicion == 0) {
                head = head.siguiente;
            } else {
                Nodo anterior = head;
                for (int indice = 0; indice < posicion - 1; indice++) {
                    anterior = anterior.siguiente;
                }
                anterior.siguiente = anterior.siguiente.siguiente;
            }
            size--;
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
        System.out.println("=== EJERCICIO 6: ELIMINAR POR POSICIÓN ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        System.out.println("Intento de eliminar posición 0 en lista vacía:");
        lista.eliminarEnPosicion(0);
        for (int dato : new int[] { 10, 20, 30, 40, 50 }) lista.insertarAlFinal(dato);
        System.out.print("Inicial: "); lista.imprimir();
        lista.eliminarEnPosicion(0);
        System.out.print("Tras eliminar primero: "); lista.imprimir();
        lista.eliminarEnPosicion(1);
        System.out.print("Tras eliminar medio: "); lista.imprimir();
        lista.eliminarEnPosicion(lista.getSize() - 1);
        System.out.print("Tras eliminar último: "); lista.imprimir();
        lista.eliminarEnPosicion(-1);
        lista.eliminarEnPosicion(lista.getSize());
        System.out.print("Tras índices inválidos: "); lista.imprimir();
        System.out.println("size=" + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se conserva la firma void del enunciado; un índice inválido informa y no
    altera la lista ni size.
*/