/*
Prompt inicial utilizado:
Implementá invertir() iterativamente en una lista enlazada simple de enteros,
sin crear nodos ni cambiar size. Usá referencias anterior, actual y siguiente.
En cada paso guardá primero actual.siguiente en siguiente, luego apuntá
actual.siguiente a anterior y avanzá ambos punteros. Cuando actual llegue a null,
asigná head = anterior. Si se cambia el enlace antes de guardar siguiente, se
pierde el acceso al resto de la cadena. Probá lista vacía, un nodo y
10 -> 20 -> 30 -> 40 -> null, que debe quedar 40 -> 30 -> 20 -> 10 -> null.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 9. */
public class Ejercicio9 {
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

        public void invertir() {
            Nodo anterior = null;
            Nodo actual = head;
            while (actual != null) {
                Nodo siguiente = actual.siguiente;
                actual.siguiente = anterior;
                anterior = actual;
                actual = siguiente;
            }
            head = anterior;
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
        System.out.println("=== EJERCICIO 9: INVERTIR LA LISTA ===");
        ListaEnlazadaEnteros vacia = new ListaEnlazadaEnteros();
        vacia.invertir();
        System.out.print("Vacía invertida: "); vacia.imprimir();

        ListaEnlazadaEnteros una = new ListaEnlazadaEnteros();
        una.insertarAlFinal(7);
        una.invertir();
        System.out.print("Un nodo invertido: "); una.imprimir();

        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        for (int dato : new int[] { 10, 20, 30, 40 }) lista.insertarAlFinal(dato);
        System.out.print("Antes: "); lista.imprimir();
        int sizeAntes = lista.getSize();
        lista.invertir();
        System.out.print("Después: "); lista.imprimir();
        System.out.println("size antes=" + sizeAntes + ", después=" + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- La demostración incluye vacío, un nodo y varios; comprueba además que invertir
  cambia el orden sin modificar el número de nodos.
*/