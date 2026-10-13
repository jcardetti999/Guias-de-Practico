/*
Prompt inicial utilizado:
Implementá contarOcurrencias(int dato) en una lista enlazada simple. Recorré
desde head hasta null y aumentá un contador cada vez que el dato del nodo
coincida; no te detengas en la primera coincidencia porque puede haber más.
Probar cero, una y varias apariciones, incluyendo
10 -> 20 -> 10 -> 30 -> 10 -> null, donde contarOcurrencias(10) debe devolver 3.
El recorrido no cambia los enlaces ni el size.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 8. */
public class Ejercicio8 {
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

        public int contarOcurrencias(int dato) {
            int cantidad = 0;
            Nodo actual = head;
            while (actual != null) {
                if (actual.dato == dato) cantidad++;
                actual = actual.siguiente;
            }
            return cantidad;
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
        System.out.println("=== EJERCICIO 8: CONTAR OCURRENCIAS ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        for (int dato : new int[] { 10, 20, 10, 30, 10 }) lista.insertarAlFinal(dato);
        lista.imprimir();
        System.out.println("contarOcurrencias(10) = " + lista.contarOcurrencias(10));
        System.out.println("contarOcurrencias(20) = " + lista.contarOcurrencias(20));
        System.out.println("contarOcurrencias(99) = " + lista.contarOcurrencias(99));
        System.out.println("size no cambia: " + lista.getSize());
        System.out.println("En lista vacía, contarOcurrencias(10) = "
                + new ListaEnlazadaEnteros().contarOcurrencias(10));
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- No fueron necesarios; el ejemplo con tres coincidencias prueba que se recorre
  toda la lista y no se corta en el primer nodo coincidente.
*/