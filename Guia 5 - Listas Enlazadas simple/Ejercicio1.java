/*
Prompt inicial utilizado:
Quiero crear desde cero una lista enlazada simple de enteros. Cada Nodo debe
contener un dato int y una referencia siguiente; la lista conserva head como
referencia al primer nodo y size como cantidad de nodos. Implementá
insertarAlInicio, insertarAlFinal, imprimir, estaVacia y getSize. Al insertar al
inicio, el nuevo nodo debe apuntar al head anterior antes de convertirse en el
nuevo head. Al agregar al final, recorré desde head hasta el último nodo y
conectalo; la lista vacía requiere que head pase a ser el nuevo nodo. Actualizá
size una vez por inserción. Mostrá la lista y el tamaño al insertar al inicio y
al final, y probá también la lista vacía.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 1. */
public class Ejercicio1 {

    private static class Nodo {
        private final int dato;
        private Nodo siguiente;

        private Nodo(int dato) {
            this.dato = dato;
        }
    }

    static class ListaEnlazadaEnteros {
        private Nodo head;
        private int size;

        public void insertarAlInicio(int dato) {
            Nodo nuevo = new Nodo(dato);
            nuevo.siguiente = head;
            head = nuevo;
            size++;
        }

        public void insertarAlFinal(int dato) {
            Nodo nuevo = new Nodo(dato);
            if (head == null) {
                head = nuevo;
            } else {
                Nodo actual = head;
                while (actual.siguiente != null) {
                    actual = actual.siguiente;
                }
                actual.siguiente = nuevo;
            }
            size++;
        }

        public void imprimir() {
            Nodo actual = head;
            while (actual != null) {
                System.out.print(actual.dato + " -> ");
                actual = actual.siguiente;
            }
            System.out.println("null");
        }

        public boolean estaVacia() {
            return head == null;
        }

        public int getSize() {
            return size;
        }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 1: CREAR UNA LISTA ENLAZADA ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        System.out.println("Lista nueva:");
        lista.imprimir();
        System.out.println("estaVacia=" + lista.estaVacia() + ", size=" + lista.getSize());

        lista.insertarAlInicio(20);
        System.out.println("insertarAlInicio(20):");
        lista.imprimir();
        System.out.println("size=" + lista.getSize());

        lista.insertarAlInicio(10);
        lista.insertarAlFinal(30);
        System.out.println("insertarAlInicio(10), insertarAlFinal(30):");
        lista.imprimir();
        System.out.println("estaVacia=" + lista.estaVacia() + ", size=" + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- No fueron necesarios para esta primera implementación; se verificó la lista
  vacía, ambas inserciones y la consistencia de size.
*/