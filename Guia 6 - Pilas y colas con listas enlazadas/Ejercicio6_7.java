/*
Prompt inicial utilizado:
Implementa un verificador de parentesis usando una pila enlazada de aperturas.
Al leer '(', apila; al leer ')', si no hay apertura devuelve invalido, si hay,
desapila. Ignora otros caracteres. La expresion es valida al final solo si no
quedan aperturas. Una cadena vacia esta balanceada. Usa una pila propia, no una
coleccion de Java. Apilar y desapilar son O(1); validar recorre n caracteres
en O(n) tiempo y usa O(n) espacio en el peor caso. Prueba los ejemplos validos
e invalidos del enunciado, mas cadena vacia y cierre inicial.
*/

/** Guia 6 — Ejercicio 6.7: verificador de parentesis balanceados. */
public class Ejercicio6_7 {
    private static class Nodo {
        private final char caracter;
        private final Nodo siguiente;
        private Nodo(char caracter, Nodo siguiente) {
            this.caracter = caracter;
            this.siguiente = siguiente;
        }
    }

    private static class PilaAperturas {
        private Nodo head;
        private int size;

        public void apilar(char caracter) {
            head = new Nodo(caracter, head);
            size++;
        }

        public boolean estaVacia() { return size == 0; }

        public char desapilar() {
            if (head == null) {
                System.out.println("  No se puede desapilar: la pila esta vacia.");
                return '\0';
            }
            char caracter = head.caracter;
            head = head.siguiente;
            size--;
            return caracter;
        }
    }

    public static boolean estaBalanceada(String expresion) {
        if (expresion == null) {
            System.out.println("  La expresion null no es valida.");
            return false;
        }
        PilaAperturas pila = new PilaAperturas();
        for (int i = 0; i < expresion.length(); i++) {
            char actual = expresion.charAt(i);
            if (actual == '(') {
                pila.apilar(actual);
            } else if (actual == ')') {
                if (pila.estaVacia()) return false;
                pila.desapilar();
            }
        }
        return pila.estaVacia();
    }

    private static void probar(String expresion) {
        System.out.println("\"" + expresion + "\" -> "
                + (estaBalanceada(expresion) ? "BALANCEADA" : "NO BALANCEADA"));
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.7: PARENTESIS BALANCEADOS ===");
        probar("(2 + 3) * (5 - 1)");
        probar("((a + b) * c)");
        probar("(2 + 3");
        probar("())(");
        probar("");
        probar(")inicio(");
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- Se agregaron la expresion vacia y un cierre inicial como limites: vacia se
  considera balanceada y un cierre sin apertura se rechaza inmediatamente.
*/