/*
Prompt inicial utilizado:
Implementá insertarEnPosicion(int dato, int posicion) para una lista enlazada
simple de enteros con head y size. Aceptá posiciones desde 0 hasta size,
inclusive: 0 inserta antes del head, size agrega al final y las demás posiciones
insertan antes del nodo que hoy ocupa ese índice. Validá el rango antes de
cambiar enlaces. En el medio, encontrá el nodo anterior y actualizá primero
nuevo.siguiente = actual.siguiente y después actual.siguiente = nuevo; invertir
el orden puede perder el resto de la cadena. Aumentá size sólo si se insertó.
Probá lista vacía, inicio, medio, final e índices inválidos.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 4. */
public class Ejercicio5_4 {
    private static class Nodo {
        private final int dato;
        private Nodo siguiente;
        private Nodo(int dato) { this.dato = dato; }
    }

    static class ListaEnlazadaEnteros {
        private Nodo head;
        private int size;

        public void insertarEnPosicion(int dato, int posicion) {
            if (posicion < 0 || posicion > size) {
                System.out.println("  Posición inválida: " + posicion + " (rango 0.." + size + ")");
                return;
            }
            Nodo nuevo = new Nodo(dato);
            if (posicion == 0) {
                nuevo.siguiente = head;
                head = nuevo;
            } else {
                Nodo anterior = head;
                for (int indice = 0; indice < posicion - 1; indice++) {
                    anterior = anterior.siguiente;
                }
                nuevo.siguiente = anterior.siguiente;
                anterior.siguiente = nuevo;
            }
            size++;
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
        System.out.println("=== EJERCICIO 5.4: INSERTAR EN UNA POSICIÓN ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        lista.insertarEnPosicion(20, 0);
        lista.insertarEnPosicion(10, 0);
        lista.insertarEnPosicion(30, 2);
        lista.insertarEnPosicion(25, 2);
        System.out.print("Resultado (inicio, final y medio): ");
        lista.imprimir();
        System.out.println("size=" + lista.getSize());
        lista.insertarEnPosicion(99, -1);
        lista.insertarEnPosicion(99, 5);
        System.out.print("Sin cambios por índices inválidos: ");
        lista.imprimir();
        System.out.println("size=" + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se conserva la firma void del enunciado. Los índices inválidos informan y
    retornan antes de modificar enlaces o size.
*/