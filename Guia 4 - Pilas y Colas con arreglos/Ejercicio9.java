/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una demostración del PROBLEMA DE DESPERDICIO DE
 * ESPACIO en una cola implementada con arreglos.
 *
 * IMPORTANTE: el objetivo NO es resolver el problema, es DEMOSTRARLO.
 * Quiero ver por qué la cola del ejercicio 6 se queda sin espacio aunque esté
 * vacía.
 *
 * OPERACIONES QUE DEBE TENER
 * - enqueue(valor), dequeue(), isVacia(), isLlena(), size()
 * - mostrarArreglo(): imprime el arreglo interno marking:
 *     - los elementos ocupados con su valor
 *     - los espacios LIBRES con [_]
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * - `front`: posicion del primer elemento.
 * - `rear`: posicion del proximo espacio libre.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Cola vacia y cola llena: informar, no fallar.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO — LA DEMOSTRACIÓN
 * Con una capacidad de 5, hacer esta secuencia EXACTA:
 *
 *   1) Encolar 10, 20, 30, 40, 50   -> la cola queda LLENA
 *   2) Desencolar 3 veces            -> salen 10, 20, 30
 *   3) Mostrar el arreglo            -> debe verse: [_] [_] [30] [40] [50]
 *
 * En este punto la cola tiene 2 elementos pero le quedan 3 espacios libres.
 * Esos 3 espacios NO se pueden usar.
 *
 *   4) Intentar encolar 60, 70, 80, 90 -> los tres primeros DEBEN FALLAR
 *
 * Ese es el punto: hay 3 lugares vacíos y sin embargo la cola se declara
 * llena. El espacio está libre en el arreglo pero es inalcanzable, porque rear
 * ya llegó al final y front no vuelve al principio.
 *
 * La conclusión que debe quedar escrita: la cola está VACÍA, tiene 0 elementos,
 * y aun así NO se puede encolar nada. El arreglo de 5 espacios está dando
 * servicio de una cola de 3.
 *
 * Al final, vaciar la cola por completo y mostrar que sigue sin poder encolar:
 * eso demuestra que no es un problema de "espacio que se recupera al vaciarse".
 *
 * En ningún momento hay que resolverlo con módulo o cola circular: eso es
 * ejercicio 10.
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 9: El problema del desperdicio de espacio
 *
 * QUÉ HACE ESTE EJERCICIO
 * Demuestra un defecto de la cola del ejercicio 6: los espacios que se liberan
 * al principio del arreglo quedan inservibles para siempre. No se corrige acá;
 * se muestra. Corregirlo es el ejercicio 10.
 *
 * EL MECANISMO
 * `front` y `rear` sólo avanzan hacia adelante:
 *
 *     enqueue   -> rear  avanza
 *     dequeue   -> front avanza
 *
 * No hay ninguna operación que los haga retroceder. Por eso los huecos que
 * deja `front` quedan siempre en el lado izquierdo del arreglo, y `rear` sigue
 * avanzando hacia la derecha hasta toparse con el final.
 *
 * LA CONSECUENCIA
 * La condición de llena es `front == 0 && rear == capacidad`. El `front == 0`
 * es lo que arruina todo: en cuanto se desencola algo, `front` deja de valer 0 y
 * la condición nunca más se cumple. La cola se declara llena aunque tenga
 * lugares de sobra.
 *
 * O sea: se puede llegar al estado "0 elementos, cola vacía" y aun así no poder
 * encolar. Un arreglo de 5 termina sirviendo como una cola de 3.
 *
 * CÓMO SE MUESTRA
 * `mostrarArreglo()` marca cada posición:
 *     [10] [20] [30] [40] [50]   ocupada, con su valor
 *     [_]  [_]  [_]  [40] [50]   libre e inservible
 *     [-]  [-]  [-]  [-]  [-]    nunca usada
 *
 * COMPLEJIDAD
 *   Θ(1) encolar, Θ(1) desencolar.
 *   Θ(capacidad) mostrarArreglo(): recorre el arreglo entero.
 */
public class Ejercicio9 {

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    static class ColaConDesperdicio {

        private final int[] elementos;
        private final int capacidad;
        private int front;
        private int rear;

        ColaConDesperdicio(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.elementos = new int[capacidad];
            this.front = 0;
            this.rear = 0;
        }

        /**
         * Encola al final. Falla si la condición de llena se cumple.
         *
         * @return true si se encoló, false si no
         */
        public boolean enqueue(int valor) {
            if (isLlena()) {
                return false;
            }
            // rear ya alcanzó el final del arreglo, pero front > 0 así que
            // isLlena() mintió. No hay posición válida donde escribir: se
            // rechaza el encolado. Esta guarda no es elegancia del diseño,
            // es el defecto del ejercicio 6 mostrando su peor cara.
            if (rear >= capacidad) {
                return false;
            }
            elementos[rear] = valor;
            rear++;
            return true;
        }

        /**
         * Desencola el primero.
         *
         * @return el valor desencolado, o Integer.MIN_VALUE si estaba vacía
         */
        public int dequeue() {
            if (isVacia() || front >= capacidad) {
                return Integer.MIN_VALUE;
            }
            int valor = elementos[front];
            front++;
            return valor;
        }

        public boolean isVacia() {
            return front == rear;
        }

        /**
         * ATENCIÓN: el `front == 0` es el defecto que este ejercicio demuestra.
         * Si `front` ya avanzó, la condición nunca se cumple y la cola queda
         * inservible aunque el arreglo tenga espacio.
         */
        public boolean isLlena() {
            return front == 0 && rear == capacidad;
        }

        /** @return cantidad de elementos realmente almacenados */
        public int size() {
            return rear - front;
        }

        /**
         * @return cuántas posiciones del arreglo quedaron inservibles:
         *         liberadas al desencolar que rear ya no puede alcanzar.
         */
        public int desperdiciadas() {
            return front;
        }

        /**
         * Imprime el arreglo interno marcando el estado de cada posición.
         *   [valor]  ocupada
         *   [_]      libre, pero inalcanzable porque rear ya pasó
         *   [-]      nunca usada
         */
        public String mostrarArreglo() {
            StringBuilder texto = new StringBuilder();
            for (int i = 0; i < capacidad; i++) {
                if (i > 0) {
                    texto.append(" ");
                }
                if (i < front) {
                    texto.append("[_]");        // liberada e inservible
                } else if (i < rear) {
                    texto.append("[").append(elementos[i]).append("]");   // ocupada
                } else {
                    texto.append("[-]");        // nunca usada
                }
            }
            return texto.toString();
        }

        /** Informe de una línea con todos los indicadores. */
        public String estado() {
            return "front=" + front + " rear=" + rear
                    + " | elementos=" + size()
                    + " | desperdiciadas=" + desperdiciadas()
                    + " | vacia=" + isVacia() + " llena=" + isLlena();
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y símbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("=== EJERCICIO 9: DESPERDICIO DE ESPACIO ===");
        System.out.println();
        System.out.println("Este ejercicio NO resuelve el problema: lo demuestra.");
        System.out.println("La solucion (cola circular) es el ejercicio 10.");

        paso1EncolarYLlenar();
        paso2DesencolarTres();
        paso3IntentarEncolar();
        paso4VaciarYComprobar();
        explicarPorQue();
    }

    private static void paso1EncolarYLlenar() {
        System.out.println();
        System.out.println("=== PASO 1: encolar 10, 20, 30, 40, 50 (capacidad 5) ===");
        System.out.println();

        ColaConDesperdicio cola = new ColaConDesperdicio(5);
        System.out.println("  Arreglo inicial: " + cola.mostrarArreglo());
        System.out.println();

        int[] valores = { 10, 20, 30, 40, 50 };
        for (int valor : valores) {
            boolean ok = cola.enqueue(valor);
            System.out.println("  enqueue(" + valor + ") -> " + ok
                    + "   " + cola.mostrarArreglo());
        }

        System.out.println();
        System.out.println("  La cola esta LLENA.  " + cola.estado());
        System.out.println("  Encolar un sexto valor seria imposible, y es correcto: no hay lugar.");
    }

    private static void paso2DesencolarTres() {
        System.out.println();
        System.out.println("=== PASO 2: desencolar 3 veces (salen 10, 20, 30) ===");
        System.out.println();

        ColaConDesperdicio cola = construirColaLlena();

        System.out.println("  Arreglo inicial: " + cola.mostrarArreglo());
        System.out.println();

        for (int i = 1; i <= 3; i++) {
            int valor = cola.dequeue();
            System.out.println("  dequeue() -> " + valor
                    + "   " + cola.mostrarArreglo());
        }

        System.out.println();
        System.out.println("  " + cola.estado());
        System.out.println();
        System.out.println("  Hay " + cola.desperdiciadas() + " posiciones marcadas [_]:");
        System.out.println("  son REALMENTE libres del arreglo, pero rear ya llego al final");
        System.out.println("  y nunca va a volver atras. Esos huecos no se pueden usar.");
        System.out.println();
        System.out.println("  Desperdicio: " + cola.desperdiciadas()
                + " de 5 posiciones, con solo "
                + cola.size() + " elementos en la cola.");
    }

    private static void paso3IntentarEncolar() {
        System.out.println();
        System.out.println("=== PASO 3: intentar encolar 60, 70, 80, 90 ===");
        System.out.println();
        System.out.println("  Hay 3 posiciones [_] libres. El encolar TIENE que funcionar, no?");
        System.out.println();

        ColaConDesperdicio cola = construirColaLlena();
        for (int i = 0; i < 3; i++) {
            cola.dequeue();
        }
        System.out.println("  " + cola.mostrarArreglo() + "   " + cola.estado());
        System.out.println();

        int[] nuevos = { 60, 70, 80, 90 };
        int exitos = 0;
        for (int valor : nuevos) {
            boolean ok = cola.enqueue(valor);
            System.out.println("  enqueue(" + valor + ") -> " + ok);
            if (!ok) {
                System.out.println("      RECHAZADO. Hay un [_] libre aqui adentro, pero no se puede usar.");
            } else {
                exitos++;
            }
        }

        System.out.println();
        System.out.println("  " + cola.mostrarArreglo() + "   " + cola.estado());
        System.out.println();
        System.out.println("  Valores nuevos aceptados: " + exitos + " de 4");
        System.out.println("  Los 3 huecos siguen vacios: " + cola.desperdiciadas()
                + " posiciones [_] inservibles.");
        System.out.println();
        System.out.println("  Ojo con el detalle final: isLlena() sigue diciendo FALSE.");
        System.out.println("  La cola no se sabe llena. Miente. Por eso el encolar");
        System.out.println("  se rechaza sin que en ningún momento figure una cola llena.");
    }

    private static void paso4VaciarYComprobar() {
        System.out.println();
        System.out.println("=== PASO 4: vaciar la cola por completo ===");
        System.out.println();
        System.out.println("  Si el problema se resolviera solo al vaciarse, no seria un problema.");
        System.out.println();

        ColaConDesperdicio cola = construirColaLlena();
        for (int i = 0; i < 3; i++) {
            cola.dequeue();
        }
        while (!cola.isVacia()) {
            cola.dequeue();
        }

        System.out.println("  " + cola.mostrarArreglo() + "   " + cola.estado());
        System.out.println();
        System.out.println("  isVacia(): " + cola.isVacia() + "  ->  la cola NO tiene elementos.");
        System.out.println("  isLlena():  " + cola.isLlena() + "   ->  pero tampoco acepta nada.");
        System.out.println();

        boolean ok = cola.enqueue(99);
        System.out.println("  enqueue(99) -> " + ok);
        System.out.println();
        System.out.println("  Cola VACIA y encolar falla.  " + cola.estado());
        System.out.println();
        System.out.println("  Conclusion: un arreglo de 5 posiciones esta sirviendo");
        System.out.println("  como una cola de 3. El espacio no se recupera nunca.");
    }

    private static void explicarPorQue() {
        System.out.println();
        System.out.println("=== POR QUE PASA ESTO ===");
        System.out.println();
        System.out.println("  front  y  rear  solo avanzan hacia la derecha:");
        System.out.println("      enqueue -> rear++");
        System.out.println("      dequeue -> front++");
        System.out.println("  No hay ninguna operacion que los haga retroceder.");
        System.out.println();
        System.out.println("  Y la condicion de llena es:  front == 0 && rear == capacidad");
        System.out.println();
        System.out.println("  El  front == 0  es el problema. En cuanto se desencola algo,");
        System.out.println("  front  deja de valer 0 y la condicion nunca mas se cumple.");
        System.out.println("  La cola se declara llena para siempre, aunque queden huecos.");
        System.out.println();
        System.out.println("  Lo que haria falta: que rear pudiera volver a las posiciones");
        System.out.println("  liberadas. Eso es exactamente lo que hace el modulo:");
        System.out.println("      rear = (rear + 1) % capacidad");
        System.out.println("  Al volver al 0, rear alcanza los huecos de nuevo.");
        System.out.println();
        System.out.println("  Eso es el ejercicio 10.");
    }

    /** Reconstruye la cola del paso 1 para que cada paso parta del mismo estado. */
    private static ColaConDesperdicio construirColaLlena() {
        ColaConDesperdicio cola = new ColaConDesperdicio(5);
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);
        cola.enqueue(40);
        cola.enqueue(50);
        return cola;
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
* - Se quitó la palabra "resolver" del objetivo y se aclaró que el
 *   ejercicio NO resuelve, sólo demuestra. Con "resolver" en el prompt, la
 *   primera versión terminó implementando cola circular, que es el ejercicio 10
 *   y deja este sin contenido.
 * - Se integró que el arreglo se muestre con marcas: `[valor]`, `[_]` y `[-]`.
 *   La consigna pide "ver los espacios libres", y sin marcar visualmente el
 *   arreglo el defecto queda oculto: se ve que hay 3 elementos y que quedan 2
 *   lugares, y no se ve que los 3 huecos de la izquierda están ahí, muertos.
 *   `[_]` es un hueco liberado e inservible; `[-]` es una posición nunca usada.
 *   Sin esa distinción entre los dos tipos de hueco, el final del ejercicio
 *   (cola vacía que no acepta nada) se lee como un bug y no como la prueba.
 * - Se agregó el paso 4, vaciar la cola entera y comprobar que sigue sin poder
 *   encolar. Faltaba en el prompt original y es el paso que descarta la
 *   explicación cómoda: si el espacio se recuperara al vaciarse, el defecto
 *   sería tolerable. Al vaciarse y seguir igual, se descarta.
 * - Se justificó la condición de llena con su `front == 0` a la vista. El
 *   enunciado nunca menciona por qué la condición tiene esa forma, y sin la
 *   explicación el ejercicio se lee como "la cola se llenó" y no como "el diseño
 *   tiene una condición que se rompe para siempre".
 * - Se justificó que la solución NO se implemente acá. Una frase al final del
 *   prompt ("en ningún momento hay que resolverlo con módulo") evita que el
 *   agente adelante el ejercicio 10 y rompa la progresión de la guía.
 * ========================================================================== */
