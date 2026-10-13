/*
Prompt inicial utilizado:
Implementa una pila enlazada simple de enteros con un Nodo que guarde dato y
siguiente. head representa el tope. Apilar crea un nodo que apunta al head actual
y luego lo convierte en el nuevo head; desapilar guarda el dato de head y avanza
head al siguiente. Asi push y pop son O(1), sin recorrer la lista. Incluye
consultarTope, estaVacia, contiene e imprimir. Para pila vacia, informa y devuelve
-1 en desapilar/consultarTope; documenta que ese centinela podria ser un dato.
Demuestra LIFO, busqueda presente/ausente y subdesbordamiento sin excepciones.
*/

/** Guia 6 — Ejercicio 1: pila enlazada de enteros. */
public class Ejercicio1 {
    private static final int NO_VALIDO = -1;

    private static class Nodo {
        private final int dato;
        private final Nodo siguiente;

        private Nodo(int dato, Nodo siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    static class PilaEnteros {
        private Nodo head;
        private int size;

        public void apilar(int dato) {
            head = new Nodo(dato, head);
            size++;
        }

        public int desapilar() {
            if (estaVacia()) {
                System.out.println("  No se puede desapilar: la pila esta vacia.");
                return NO_VALIDO;
            }
            int dato = head.dato;
            head = head.siguiente;
            size--;
            return dato;
        }

        public int consultarTope() {
            if (estaVacia()) {
                System.out.println("  No hay tope: la pila esta vacia.");
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
            System.out.print("tope -> ");
            for (Nodo actual = head; actual != null; actual = actual.siguiente) {
                System.out.print(actual.dato + " -> ");
            }
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 1: PILA ENLAZADA DE ENTEROS ===");
        PilaEnteros pila = new PilaEnteros();
        System.out.println("Vacia al inicio: " + pila.estaVacia());
        System.out.println("pop vacia = " + pila.desapilar());
        System.out.println("peek vacia = " + pila.consultarTope());
        for (int dato : new int[] { 10, 3, 7, 9 }) pila.apilar(dato);
        System.out.print("Contenido: "); pila.imprimir();
        System.out.println("tope=" + pila.consultarTope() + ", contiene(3)="
                + pila.contiene(3) + ", contiene(99)=" + pila.contiene(99));
        System.out.print("Orden LIFO: ");
        while (!pila.estaVacia()) System.out.print(pila.desapilar() + " ");
        System.out.println();
        System.out.println("Vacia al final: " + pila.estaVacia() + ", size=" + pila.size());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- La API primitiva usa -1 como centinela de consulta/desapilado vacio. Queda
  documentado que el centinela se confunde con un valor valido del dominio.
*/