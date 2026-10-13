/*
Prompt inicial utilizado:
Implementa una lista generica doblemente enlazada no circular. Cada Nodo<T>
guarda dato, anterior y siguiente; la lista mantiene head, tail y size. Inserta
al inicio/final enlazando ambos sentidos, elimina la primera coincidencia por
Objects.equals, e imprime desde head hacia tail y desde tail hacia head. Rechaza
null para que una referencia null indique lista vacia/resultado ausente. Las
inserciones en extremos son O(1); eliminar por valor y recorrer son O(n). Al
eliminar el unico nodo, ambos extremos deben quedar null; tambien cubre eliminar
head, tail, interior y dato ausente. Demuestra Integer, String y Cliente propio.
*/

/** Guia 6 — Ejercicio 6.10: lista doblemente enlazada generica. */
public class Ejercicio6_10 {
    private static class Nodo<T> {
        private final T dato;
        private Nodo<T> anterior;
        private Nodo<T> siguiente;
        private Nodo(T dato) { this.dato = dato; }
    }

    static class ListaDoble<T> {
        private Nodo<T> head;
        private Nodo<T> tail;
        private int size;

        public boolean insertarAlInicio(T dato) {
            if (dato == null) {
                System.out.println("  No se insertan valores null.");
                return false;
            }
            Nodo<T> nuevo = new Nodo<>(dato);
            if (head == null) {
                head = nuevo;
                tail = nuevo;
            } else {
                nuevo.siguiente = head;
                head.anterior = nuevo;
                head = nuevo;
            }
            size++;
            return true;
        }

        public boolean insertarAlFinal(T dato) {
            if (dato == null) {
                System.out.println("  No se insertan valores null.");
                return false;
            }
            Nodo<T> nuevo = new Nodo<>(dato);
            if (tail == null) {
                head = nuevo;
                tail = nuevo;
            } else {
                nuevo.anterior = tail;
                tail.siguiente = nuevo;
                tail = nuevo;
            }
            size++;
            return true;
        }

        public boolean eliminar(T dato) {
            Nodo<T> actual = head;
            while (actual != null && !java.util.Objects.equals(actual.dato, dato)) {
                actual = actual.siguiente;
            }
            if (actual == null) return false;

            if (actual.anterior == null) head = actual.siguiente;
            else actual.anterior.siguiente = actual.siguiente;

            if (actual.siguiente == null) tail = actual.anterior;
            else actual.siguiente.anterior = actual.anterior;

            size--;
            if (size == 0) {
                head = null;
                tail = null;
            }
            return true;
        }

        public boolean estaVacia() { return head == null; }
        public int size() { return size; }

        public void imprimirHaciaAdelante() {
            System.out.print("head -> ");
            for (Nodo<T> actual = head; actual != null; actual = actual.siguiente) {
                System.out.print(actual.dato + " <-> ");
            }
            System.out.println("null");
        }

        public void imprimirHaciaAtras() {
            System.out.print("tail -> ");
            for (Nodo<T> actual = tail; actual != null; actual = actual.anterior) {
                System.out.print(actual.dato + " <-> ");
            }
            System.out.println("null");
        }
    }

    record Cliente(String id, String nombre) { }

    private static void mostrarAmbosSentidos(String etiqueta, ListaDoble<?> lista) {
        System.out.println(etiqueta + " (size=" + lista.size() + ")");
        lista.imprimirHaciaAdelante();
        lista.imprimirHaciaAtras();
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.10: LISTA DOBLEMENTE ENLAZADA GENERICA ===");

        ListaDoble<Integer> enteros = new ListaDoble<>();
        mostrarAmbosSentidos("Vacia", enteros);
        enteros.insertarAlFinal(20);
        mostrarAmbosSentidos("Un nodo", enteros);
        System.out.println("Eliminar unico nodo = " + enteros.eliminar(20)
                + ", vacia=" + enteros.estaVacia());
        mostrarAmbosSentidos("Extremos limpios", enteros);
        enteros.insertarAlFinal(20);
        enteros.insertarAlFinal(30);
        enteros.insertarAlFinal(40);
        enteros.insertarAlFinal(30);
        enteros.insertarAlInicio(10);
        mostrarAmbosSentidos("Integer", enteros);
        System.out.println("Eliminar head 10=" + enteros.eliminar(10));
        System.out.println("Eliminar interior 30=" + enteros.eliminar(30)
                + " (primera coincidencia)");
        System.out.println("Eliminar tail 40=" + enteros.eliminar(40));
        System.out.println("Eliminar ausente 99=" + enteros.eliminar(99));
        mostrarAmbosSentidos("Tras eliminar extremos e interior", enteros);

        ListaDoble<String> textos = new ListaDoble<>();
        textos.insertarAlInicio("b");
        textos.insertarAlInicio("a");
        textos.insertarAlFinal("c");
        mostrarAmbosSentidos("String", textos);
        String bEquivalente = new StringBuilder("b").toString();
        System.out.println("Eliminar por igualdad de valor 'b'=" + textos.eliminar(bEquivalente));
        mostrarAmbosSentidos("String tras borrar b", textos);

        ListaDoble<Cliente> clientes = new ListaDoble<>();
        clientes.insertarAlFinal(new Cliente("C1", "Ana"));
        clientes.insertarAlFinal(new Cliente("C2", "Luis"));
        mostrarAmbosSentidos("Cliente", clientes);
        System.out.println("Eliminar Cliente equivalente="
                + clientes.eliminar(new Cliente("C1", "Ana")));
        mostrarAmbosSentidos("Cliente tras eliminar", clientes);
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se cubrio por separado la eliminacion del unico nodo, que debe limpiar head y
tail, y las eliminaciones de primero, ultimo e intermedio en una lista con mas
de un nodo. Los recorridos en ambos sentidos verifican los enlaces inversos.
*/