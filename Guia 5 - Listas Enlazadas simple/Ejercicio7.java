/*
Prompt inicial utilizado:
Implementá modificar(int posicion, int nuevoDato) en una lista enlazada simple
de enteros. Validá el rango antes de recorrer: 0 <= posicion < size. Para un
índice válido, avanzá desde head hasta el nodo y reemplazá sólo su dato. No
cambies la referencia siguiente, porque eso alteraría la estructura, ni el size,
porque no se agregan ni quitan nodos. Mostrá los datos antes y después al
modificar primero, medio y último; probá además un índice inválido.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 7. */
public class Ejercicio7 {
    private static class Nodo {
        private int dato;
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

        public void modificar(int posicion, int nuevoDato) {
            if (posicion < 0 || posicion >= size) {
                System.out.println("  Posición inválida: " + posicion + " (size=" + size + ")");
                return;
            }
            Nodo actual = head;
            for (int indice = 0; indice < posicion; indice++) actual = actual.siguiente;
            actual.dato = nuevoDato;
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
        System.out.println("=== EJERCICIO 7: MODIFICAR UN ELEMENTO ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        for (int dato : new int[] { 10, 20, 30, 40 }) lista.insertarAlFinal(dato);
        System.out.print("Inicial: "); lista.imprimir();
        lista.modificar(0, 11);
        lista.modificar(2, 33);
        lista.modificar(3, 44);
        System.out.print("Tras modificar primero, medio y último: "); lista.imprimir();
        System.out.println("Intento de modificar una posición inválida:");
        lista.modificar(4, 99);
        System.out.print("Tras índice inválido: "); lista.imprimir();
        System.out.println("size no cambia: " + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se conserva la firma void del enunciado. Una posición inválida informa y no
    cambia el dato, los enlaces ni el tamaño.
*/