/*
Prompt inicial utilizado:
Invierte una palabra usando una pila enlazada propia de char. Apila cada unidad
char de izquierda a derecha y reconstruye el resultado desapilando, lo que
invierte el orden por la politica LIFO. No uses StringBuilder.reverse ni una
coleccion. Documenta que char representa una unidad UTF-16 de Java, no siempre
un punto de codigo Unicode completo. Demuestra algoritmo -> omtirogla, palabra
vacia y un caracter. Complejidad O(n) en tiempo y espacio.
*/

/** Guia 6 — Ejercicio 8: invertir una palabra con pila. */
public class Ejercicio8 {
    private static class Nodo {
        private final char caracter;
        private final Nodo siguiente;
        private Nodo(char caracter, Nodo siguiente) {
            this.caracter = caracter;
            this.siguiente = siguiente;
        }
    }

    private static class PilaCaracteres {
        private Nodo head;
        private int size;

        public void apilar(char caracter) {
            head = new Nodo(caracter, head);
            size++;
        }

        public char desapilar() {
            if (head == null) {
                System.out.println("  No se puede desapilar: la pila esta vacia.");
                return '\0';
            }
            char resultado = head.caracter;
            head = head.siguiente;
            size--;
            return resultado;
        }

        public boolean estaVacia() { return size == 0; }
    }

    public static String invertirPalabra(String palabra) {
        if (palabra == null) {
            System.out.println("  No se puede invertir null.");
            return null;
        }
        PilaCaracteres pila = new PilaCaracteres();
        for (int i = 0; i < palabra.length(); i++) pila.apilar(palabra.charAt(i));
        StringBuilder invertida = new StringBuilder(palabra.length());
        while (!pila.estaVacia()) invertida.append(pila.desapilar());
        return invertida.toString();
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 8: INVERTIR UNA PALABRA ===");
        System.out.println("algoritmo -> " + invertirPalabra("algoritmo"));
        System.out.println("una letra -> " + invertirPalabra("x"));
        System.out.println("vacia -> \"" + invertirPalabra("") + "\"");
        System.out.println("Las operaciones recorren n unidades char de Java.");
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se explicita que la operacion invierte unidades char UTF-16; el ejemplo del
  enunciado usa letras basicas y queda invertido como se espera.
*/