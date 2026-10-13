/*
Prompt inicial utilizado:
Implementá eliminar(int dato) en una lista enlazada simple de enteros. Debe
quitar sólo la primera aparición y devolver true si encontró el dato. Si el
nodo a quitar es head, avanzá head; en otro caso, conservá el nodo anterior y
hacé que apunte al siguiente del nodo eliminado. Contemplá lista vacía, primera,
intermedia, última, dato ausente y duplicados; decrementá size únicamente si se
eliminó. En Java no se libera manualmente el nodo: al quedar inaccesible desde
la lista podrá recolectarlo el Garbage Collector.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 5. */
public class Ejercicio5_5 {
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

        public boolean eliminar(int dato) {
            if (head == null) return false;
            if (head.dato == dato) {
                head = head.siguiente;
                size--;
                return true;
            }
            Nodo anterior = head;
            Nodo actual = head.siguiente;
            while (actual != null) {
                if (actual.dato == dato) {
                    anterior.siguiente = actual.siguiente;
                    size--;
                    return true;
                }
                anterior = actual;
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
        System.out.println("=== EJERCICIO 5.5: ELIMINAR POR VALOR ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        System.out.println("Eliminar en vacía: " + lista.eliminar(10));
        for (int dato : new int[] { 10, 20, 30, 20, 40 }) lista.insertarAlFinal(dato);
        System.out.print("Inicial: "); lista.imprimir();
        System.out.println("eliminar(10), head: " + lista.eliminar(10));
        System.out.print("Después: "); lista.imprimir();
        System.out.println("eliminar(30), medio: " + lista.eliminar(30));
        System.out.println("eliminar(40), último: " + lista.eliminar(40));
        System.out.println("eliminar(20), sólo primera repetición: " + lista.eliminar(20));
        System.out.print("Queda una segunda aparición de 20: "); lista.imprimir();
        System.out.println("eliminar(99), ausente: " + lista.eliminar(99));
        System.out.println("size=" + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- No fueron necesarios; se verificaron eliminación de head, nodos interiores,
  último nodo, ausencia, lista vacía y que sólo se elimine la primera repetición.
*/