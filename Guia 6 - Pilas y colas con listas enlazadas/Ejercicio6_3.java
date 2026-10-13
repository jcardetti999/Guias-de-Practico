/*
Prompt inicial utilizado:
Generaliza la pila enlazada como Pila<T> y Nodo<T>, sin arreglos ni casts. head
sigue siendo el tope: apilar, desapilar y consultarTope son O(1), y
contiene/imprimir recorren en O(n). Los algoritmos no dependen del tipo.
Rechaza null para reservarlo como resultado de desapilar/consultarTope en vacio;
informa sin excepciones. Demuestra Integer, String y un record propio, con LIFO.
*/

/** Guia 6 — Ejercicio 6.3: pila generica. */
public class Ejercicio6_3 {
    private static class Nodo<T> {
        private final T dato;
        private final Nodo<T> siguiente;
        private Nodo(T dato, Nodo<T> siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    static class Pila<T> {
        private Nodo<T> head;
        private int size;

        public boolean apilar(T dato) {
            if (dato == null) {
                System.out.println("  No se apilan valores null.");
                return false;
            }
            head = new Nodo<>(dato, head);
            size++;
            return true;
        }

        public T desapilar() {
            if (estaVacia()) {
                System.out.println("  No se puede desapilar: la pila esta vacia.");
                return null;
            }
            T dato = head.dato;
            head = head.siguiente;
            size--;
            return dato;
        }

        public T consultarTope() {
            if (estaVacia()) {
                System.out.println("  No hay tope: la pila esta vacia.");
                return null;
            }
            return head.dato;
        }

        public boolean estaVacia() { return head == null; }
        public int size() { return size; }

        public boolean contiene(T buscado) {
            for (Nodo<T> actual = head; actual != null; actual = actual.siguiente) {
                if (java.util.Objects.equals(actual.dato, buscado)) return true;
            }
            return false;
        }

        public void imprimir() {
            System.out.print("tope -> ");
            for (Nodo<T> actual = head; actual != null; actual = actual.siguiente) {
                System.out.print(actual.dato + " -> ");
            }
            System.out.println("null");
        }
    }

    record Alumno(String legajo, String nombre) { }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.3: PILA GENERICA ===");
        Pila<Integer> enteros = new Pila<>();
        for (int dato : new int[] { 1, 2, 3 }) enteros.apilar(dato);
        System.out.print("Integer: "); enteros.imprimir();
        System.out.println("LIFO: " + enteros.desapilar() + ", "
                + enteros.desapilar() + ", " + enteros.desapilar());
        System.out.println("desapilar vacia = " + enteros.desapilar());

        Pila<String> textos = new Pila<>();
        textos.apilar("primero"); textos.apilar("segundo");
        System.out.print("String: "); textos.imprimir();

        Pila<Alumno> alumnos = new Pila<>();
        alumnos.apilar(new Alumno("A01", "Ana"));
        alumnos.apilar(new Alumno("P02", "Pedro"));
        System.out.println("Alumno en tope: " + alumnos.consultarTope());
        System.out.println("Busca valor equivalente: "
                + alumnos.contiene(new Alumno("A01", "Ana")));
        System.out.println("Intento null: " + alumnos.apilar(null));
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se rechazo null para que el retorno null identifique inequívocamente una
  consulta o extraccion sobre pila vacia.
*/