/*
Prompt inicial utilizado:
Generalizá la lista enlazada simple de enteros como ListaEnlazada<T> y Nodo<T>.
Cada nodo almacena T dato y Nodo<T> siguiente; la lista mantiene Nodo<T> head y
size. Los algoritmos de recorrido, inserción y eliminación no dependen del tipo
de dato: sólo cambia el tipo parametrizado. No uses arreglos, casts ni
colecciones de Java. Implementá operaciones equivalentes a las de la lista de
enteros y compará valores con Objects.equals, no con ==. Demostrá el mismo tipo
genérico con Integer, String y un record Alumno.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 10. */
public class Ejercicio5_10 {
    private static class Nodo<T> {
        private T dato;
        private Nodo<T> siguiente;
        private Nodo(T dato) { this.dato = dato; }
    }

    static class ListaEnlazada<T> {
        private Nodo<T> head;
        private int size;

        public void insertarAlInicio(T dato) {
            Nodo<T> nuevo = new Nodo<>(dato);
            nuevo.siguiente = head;
            head = nuevo;
            size++;
        }

        public void insertarAlFinal(T dato) {
            Nodo<T> nuevo = new Nodo<>(dato);
            if (head == null) head = nuevo;
            else {
                Nodo<T> actual = head;
                while (actual.siguiente != null) actual = actual.siguiente;
                actual.siguiente = nuevo;
            }
            size++;
        }

        public boolean insertarEnPosicion(T dato, int posicion) {
            if (posicion < 0 || posicion > size) return false;
            if (posicion == 0) {
                insertarAlInicio(dato);
                return true;
            }
            Nodo<T> anterior = nodoEn(posicion - 1);
            Nodo<T> nuevo = new Nodo<>(dato);
            nuevo.siguiente = anterior.siguiente;
            anterior.siguiente = nuevo;
            size++;
            return true;
        }

        public boolean buscar(T dato) {
            for (Nodo<T> actual = head; actual != null; actual = actual.siguiente) {
                if (java.util.Objects.equals(actual.dato, dato)) return true;
            }
            return false;
        }

        public T obtener(int posicion) {
            Nodo<T> nodo = nodoEn(posicion);
            if (nodo == null) return null;
            return nodo.dato;
        }

        public boolean eliminar(T dato) {
            if (head == null) return false;
            if (java.util.Objects.equals(head.dato, dato)) {
                head = head.siguiente;
                size--;
                return true;
            }
            Nodo<T> anterior = head;
            Nodo<T> actual = head.siguiente;
            while (actual != null) {
                if (java.util.Objects.equals(actual.dato, dato)) {
                    anterior.siguiente = actual.siguiente;
                    size--;
                    return true;
                }
                anterior = actual;
                actual = actual.siguiente;
            }
            return false;
        }

        public boolean eliminarEnPosicion(int posicion) {
            if (posicion < 0 || posicion >= size) return false;
            if (posicion == 0) head = head.siguiente;
            else {
                Nodo<T> anterior = nodoEn(posicion - 1);
                anterior.siguiente = anterior.siguiente.siguiente;
            }
            size--;
            return true;
        }

        public boolean modificar(int posicion, T nuevoDato) {
            Nodo<T> nodo = nodoEn(posicion);
            if (nodo == null) return false;
            nodo.dato = nuevoDato;
            return true;
        }

        public int contarOcurrencias(T dato) {
            int cantidad = 0;
            for (Nodo<T> actual = head; actual != null; actual = actual.siguiente) {
                if (java.util.Objects.equals(actual.dato, dato)) cantidad++;
            }
            return cantidad;
        }

        public void invertir() {
            Nodo<T> anterior = null;
            Nodo<T> actual = head;
            while (actual != null) {
                Nodo<T> siguiente = actual.siguiente;
                actual.siguiente = anterior;
                anterior = actual;
                actual = siguiente;
            }
            head = anterior;
        }

        public boolean estaVacia() { return head == null; }
        public int getSize() { return size; }

        private Nodo<T> nodoEn(int posicion) {
            if (posicion < 0 || posicion >= size) return null;
            Nodo<T> actual = head;
            for (int indice = 0; indice < posicion; indice++) actual = actual.siguiente;
            return actual;
        }

        public void imprimir() {
            Nodo<T> actual = head;
            while (actual != null) {
                System.out.print(actual.dato + " -> ");
                actual = actual.siguiente;
            }
            System.out.println("null");
        }
    }

    record Alumno(String legajo, String nombre) { }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 5.10: LISTA ENLAZADA GENÉRICA ===");

        ListaEnlazada<Integer> enteros = new ListaEnlazada<>();
        for (int dato : new int[] { 10, 20, 30 }) enteros.insertarAlFinal(dato);
        System.out.print("Integer: "); enteros.imprimir();
        System.out.println("buscar(20)=" + enteros.buscar(20) + ", eliminar(20)="
                + enteros.eliminar(20) + ", size=" + enteros.getSize());
        enteros.invertir();
        System.out.print("Invertida: "); enteros.imprimir();

        ListaEnlazada<String> textos = new ListaEnlazada<>();
        textos.insertarAlFinal("hola");
        textos.insertarAlFinal("mundo");
        textos.insertarAlInicio("inicio");
        System.out.print("String: "); textos.imprimir();
        System.out.println("ocurrencias de mundo=" + textos.contarOcurrencias("mundo"));

        ListaEnlazada<Alumno> alumnos = new ListaEnlazada<>();
        Alumno ana = new Alumno("A01", "Ana");
        alumnos.insertarAlFinal(ana);
        alumnos.insertarAlFinal(new Alumno("P02", "Pedro"));
        System.out.print("Alumno: "); alumnos.imprimir();
        System.out.println("buscar alumno equivalente por valor="
                + alumnos.buscar(new Alumno("A01", "Ana")));
        System.out.println("eliminar alumno por valor="
                + alumnos.eliminar(new Alumno("A01", "Ana")) + ", size=" + alumnos.getSize());

        ListaEnlazada<Integer> conNulos = new ListaEnlazada<>();
        conNulos.insertarAlFinal(null);
        conNulos.insertarAlFinal(1);
        System.out.println("Objects.equals soporta null: buscar(null)=" + conNulos.buscar(null));
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- La clase genérica incluye las operaciones acumuladas en la guía para comparar
  el mismo recorrido e invariantes entre Integer, String y Alumno. Objects.equals
  permite igualdad por valor y tratamiento null-safe sin usar colecciones.
*/