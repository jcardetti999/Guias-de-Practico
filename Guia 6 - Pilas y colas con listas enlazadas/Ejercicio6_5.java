/*
Prompt inicial utilizado:
Implementa un historial de navegacion como pila enlazada de String, con la
pagina actual en head. visitar(url) apila y pasa a ser la actual; volverAtras
retira la pagina actual si existe una anterior, y consultaActual no altera el
historial. Imprimir recorre desde la actual hacia paginas previas. No agregues
funcion adelante. Rechaza URL null; operaciones sin pagina informan y devuelven
null. LIFO es adecuado porque volver atras recupera la visita inmediatamente
anterior. Visitar, volver atras y consultar son O(1); imprimir es O(n).
Demuestra varias visitas, retroceso, consulta y el limite inicial.
*/

/** Guia 6 — Ejercicio 6.5: historial de navegacion con pila. */
public class Ejercicio6_5 {
    private static class Pagina {
        private final String url;
        private final Pagina anterior;
        private Pagina(String url, Pagina anterior) {
            this.url = url;
            this.anterior = anterior;
        }
    }

    static class HistorialNavegacion {
        private Pagina head;
        private int size;

        public boolean visitar(String url) {
            if (url == null || url.isBlank()) {
                System.out.println("  URL vacia o null: visita rechazada.");
                return false;
            }
            head = new Pagina(url, head);
            size++;
            return true;
        }

        public String volverAtras() {
            if (head == null) {
                System.out.println("  No hay pagina actual.");
                return null;
            }
            if (head.anterior == null) {
                System.out.println("  No hay una pagina anterior.");
                return null;
            }
            head = head.anterior;
            size--;
            return head.url;
        }

        public String consultarActual() {
            if (head == null) {
                System.out.println("  El historial esta vacio.");
                return null;
            }
            return head.url;
        }

        public void imprimir() {
            System.out.print("actual -> ");
            for (Pagina actual = head; actual != null; actual = actual.anterior) {
                System.out.print(actual.url + " -> ");
            }
            System.out.println("null");
        }

        public int size() { return size; }
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("=== EJERCICIO 6.5: HISTORIAL DE NAVEGACION ===");
        HistorialNavegacion historial = new HistorialNavegacion();
        System.out.println("Actual vacio = " + historial.consultarActual());
        historial.visitar("inicio.example");
        historial.visitar("busqueda.example");
        historial.visitar("resultado.example");
        System.out.println("Tras visitar tres: " + historial.consultarActual());
        historial.imprimir();
        System.out.println("Volver atras -> " + historial.volverAtras());
        System.out.println("Consultar actual -> " + historial.consultarActual());
        historial.imprimir();
        System.out.println("Volver atras -> " + historial.volverAtras());
        System.out.println("Intentar atras en primera pagina -> " + historial.volverAtras());
        System.out.println("size=" + historial.size());
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- El historial conserva la primera pagina y no permite que volver atras la quite;
  no se implementa navegacion hacia adelante porque no esta en la consigna.
*/