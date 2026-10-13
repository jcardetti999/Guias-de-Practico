/*
Prompt inicial utilizado:
Implementá obtener(int posicion) en una lista enlazada simple de enteros. La
lista guarda head y size; cada nodo sólo conoce a su siguiente. Validá primero
posicion < 0 o posicion >= size. Para una posición válida, recorré desde head
avanzando nodo por nodo; la posición no habilita acceso aleatorio. Como la firma
retorna int y el enunciado no define errores, devolvé -1 y mostrá un mensaje para
índices inválidos, documentando que -1 también puede ser un dato. Probá primera,
media, última, lista vacía y posiciones inválidas.
*/

/** Guía 5 — Listas enlazadas simples — Ejercicio 3. */
public class Ejercicio3 {
    public static final int POSICION_INVALIDA = -1;

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

        public int obtener(int posicion) {
            if (posicion < 0 || posicion >= size) {
                System.out.println("  Posición inválida: " + posicion + " (size=" + size + ")");
                return POSICION_INVALIDA;
            }
            Nodo actual = head;
            for (int indice = 0; indice < posicion; indice++) actual = actual.siguiente;
            return actual.dato;
        }

        public int getSize() { return size; }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 3: OBTENER POR POSICIÓN ===");
        ListaEnlazadaEnteros lista = new ListaEnlazadaEnteros();
        System.out.println("obtener(0) en vacía = " + lista.obtener(0));
        for (int dato : new int[] { 10, 20, 30, 40 }) lista.insertarAlFinal(dato);
        System.out.println("primero obtener(0) = " + lista.obtener(0));
        System.out.println("medio obtener(2) = " + lista.obtener(2));
        System.out.println("último obtener(3) = " + lista.obtener(3));
        System.out.println("negativo obtener(-1) = " + lista.obtener(-1));
        System.out.println("fuera de rango obtener(4) = " + lista.obtener(4));
        System.out.println("size sin cambios = " + lista.getSize());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se fijó el centinela -1 para posiciones inválidas porque la firma retorna int;
  el mensaje y esta nota hacen visible que el centinela no es inequívoco.
*/