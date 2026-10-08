/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una COLA CIRCULAR sobre arreglos, que es la
 * solución al problema de desperdicio de espacio del ejercicio 9.
 *
 * OPERACIONES QUE DEBE TENER
 * - enqueue(valor), dequeue(), isVacia(), isLlena(), size(), peek()
 * - mostrarArreglo(): imprime el arreglo interno marcando:
 *     [valor] ocupada
 *     [_]    libre y reutilizable
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * Los dos indices dan la VUELTA con el operador modulo:
 *     enqueue -> rear  = (rear  + 1) % capacidad
 *     dequeue -> front = (front + 1) % capacidad
 *
 * Cuando rear llega a capacidad, el modulo lo devuelve a 0, que es donde estan
 * los espacios que dejo libres el desencolado. Asi el espacio se reutiliza.
 *
 * DETECCION DE COLA VACIA Y COLA LLENA — CON CONTADOR
 * Se agrega un campo `contador` que lleva cuantos elementos hay:
 *     isVacia() -> contador == 0
 *     isLlena()  -> contador == capacidad
 *     size()     -> contador
 * El contador sube en enqueue y baja en dequeue.
 *
 * La alternativa sin contador es AMBIGUA: front == rear puede significar cola
 * vacia o cola llena. Quiero que se documente por que se elige el contador.
 *
 * CASOS LÍMITE A CONTEMPLAR
 * - Cola vacia y cola llena: informar, no fallar.
 * - Que la capacidad 0 funcione sin romperse.
 * - Ninguna operacion lanza excepcion.
 *
 * COMPORTAMIENTO ESPERADO — LA COMPARACIÓN
 * Hay que repetir EXACTAMENTE la misma secuencia del ejercicio 9, con la misma
 * capacidad 5:
 *
 *   1) Encolar 10, 20, 30, 40, 50
 *   2) Desencolar 3 veces
 *   3) Encolar 60, 70, 80, 90
 *
 * En el ejercicio 9 los tres primeros fallaban. Ahora tienen que entrar TODOS:
 * eso es lo que demuestra que el espacio se reutiliza.
 *
 * Al final hay que mostrar el caso limite: con capacidad 5 se pueden encolar 5
 * elementos y el sexto falla, y al vaciarse la cola se pueden encolar 5 otra
 * vez. Ese ciclo es el que la cola circular resuelve.
 *
 * Además, encolar y desencolar muchas veces (por ejemplo 20) sin que se rompa ni
 * de desborde el arreglo.
 * ========================================================================== */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio 10: Cola circular — la solución al ejercicio 9
 *
 * QUÉ CAMBIA RESPECTO DEL EJERCICIO 6
 * Los dos índices dan la vuelta:
 *
 *     enqueue -> rear  = (rear  + 1) % capacidad
 *     dequeue -> front = (front + 1) % capacidad
 *
 * Cuando `rear` llega a `capacidad`, el módulo lo devuelve a 0. Ahí están los
 * espacios que dejó libres el desencolado, así que se reutilizan. Ese es todo
 * el arreglo: el módulo reemplaza a la condición `front == 0` del ejercicio 6.
 *
 * POR QUÉ HAY UN CONTADOR
 * Sin contador, `front == rear` es ambiguo: puede ser que la cola esté vacía o
 * que esté llena, y el arreglo no tiene forma de distinguirlo. En el ejercicio
 * 6 zafaba porque la condición de llena era `front == 0 && rear == capacidad`,
 * pero esa condición era justamente la que arruinaba todo.
 *
 * Con `contador` la ambigüedad desaparece: vacía es `contador == 0`, llena es
 * `contador == capacidad`, y las dos cosas pueden ser ciertas en el mismo
 * instante sin contradecirse.
 *
 * LA ALTERNATIVA QUE NO SE USA
 * También se podría reservar una posición (nunca se encola en la última, así
 * que `front == rear` siempre significa vacía). El costo: la capacidad
 * efectiva pasa a ser `n - 1`, un arreglo de 5 sirve para 4 elementos, y queda
 * una posición muerta. El contador no sacrifica ninguna posición.
 *
 * ÍNDICES
 * `elementos` es un int[] de capacidad fija.
 * `front` — posición del primer elemento.
 * `rear`  — posición del próximo espacio libre.
 *
 * CASOS LÍMITE
 *   Ninguna operación lanza excepción: se informa con un mensaje.
 *   La capacidad 0 se maneja: cualquier encolar falla y la cola está vacía.
 *
 * COMPLEJIDAD — todas las operaciones Θ(1)
 */
public class Ejercicio10 {

    /** Valor centinela devuelto por dequeue() y peek() si la cola está vacía. */
    public static final int NO_VALIDO = -1;

    // ============================================================
    //  ESTRUCTURA
    // ============================================================

    static class ColaCircular {

        private final int[] elementos;
        private final int capacidad;
        private int front;
        private int rear;

        /**
         * Cantidad de elementos realmente almacenados.
         * Es lo que permite distinguir "vacía" de "llena" sin ambigüedad.
         */
        private int contador;

        ColaCircular(int capacidad) {
            if (capacidad < 0) {
                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa: " + capacidad);
            }
            this.capacidad = capacidad;
            this.elementos = new int[capacidad];
            this.front = 0;
            this.rear = 0;
            this.contador = 0;
        }

        /**
         * Encola al final. Si el índice llega al final, el módulo lo envuelve
         * a 0 y se reutiliza un espacio que había quedado libre al principio.
         *
         * @return true si se encoló, false si la cola estaba llena
         */
        public boolean enqueue(int valor) {
            if (isLlena()) {
                return false;
            }
            elementos[rear] = valor;
            rear = (rear + 1) % capacidad;   // la vuelta: aquí se reutiliza el espacio
            contador++;
            return true;
        }

        /**
         * Desencola el primero.
         *
         * @return el valor, o NO_VALIDO si la cola estaba vacía
         */
        public int dequeue() {
            if (isVacia()) {
                return NO_VALIDO;
            }
            int valor = elementos[front];
            front = (front + 1) % capacidad;  // también da la vuelta
            contador--;
            return valor;
        }

        /**
         * @return el primero sin sacarlo, o NO_VALIDO si está vacía
         */
        public int peek() {
            if (isVacia()) {
                return NO_VALIDO;
            }
            return elementos[front];
        }

        /** Vacía ⟺ no hay elementos. El contador evita la ambigüedad. */
        public boolean isVacia() {
            return contador == 0;
        }

        /** Llena ⟺ el contador llegó a la capacidad del arreglo. */
        public boolean isLlena() {
            return contador == capacidad;
        }

        /** @return cantidad de elementos almacenados */
        public int size() {
            return contador;
        }

        /** @return true si hay algún hueco libre reutilizable */
        public boolean tieneEspaciosLibres() {
            return !isLlena();
        }

        /** Informe de una línea con todos los indicadores. */
        public String estado() {
            return "front=" + front + " rear=" + rear
                    + " contador=" + contador
                    + " | vacia=" + isVacia() + " llena=" + isLlena();
        }

        /**
         * Imprime el arreglo. Ahora no hay posiciones inservibles: todo hueco
         * es alcanzable porque `rear` vuelve a 0.
         *   [valor] ocupada
         *   [_]    libre y reutilizable
         */
        public String mostrarArreglo() {
            if (capacidad == 0) {
                return "(arreglo de 0 posiciones)";
            }
            StringBuilder texto = new StringBuilder();
            for (int i = 0; i < capacidad; i++) {
                if (i > 0) {
                    texto.append(" ");
                }
                // Una posición está ocupada si está en el tramo vivo
                // [front, front + contador), dando la vuelta si hace falta.
                int desplazamiento = (i - front + capacidad) % capacidad;
                if (desplazamiento < contador) {
                    texto.append("[").append(elementos[i]).append("]");
                } else {
                    texto.append("[_]");
                }
            }
            return texto.toString();
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

        System.out.println("=== EJERCICIO 10: COLA CIRCULAR ===");
        System.out.println();
        System.out.println("Este es el ejercicio 9 resuelto. Misma secuencia, mismo arreglo.");

        compararConEjercicio9();
        casoCapacidadCero();
        cicloLlenarVacias();
        pruebaDeEsfuerzo();
        explicarElContador();
        ejecutarInteractivo();
    }

    /**
     * La MISMA secuencia del ejercicio 9. Allí fallaron 4 de 4 encolados.
     */
    private static void compararConEjercicio9() {
        System.out.println();
        System.out.println("=== LA MISMA SECUENCIA DEL EJERCICIO 9 ===");
        System.out.println();

        ColaCircular cola = new ColaCircular(5);

        System.out.println("  Paso 1: encolar 10, 20, 30, 40, 50");
        int[] primeros = { 10, 20, 30, 40, 50 };
        for (int valor : primeros) {
            boolean ok = cola.enqueue(valor);
            System.out.println("    enqueue(" + valor + ") -> " + ok
                    + "   " + cola.mostrarArreglo());
        }
        System.out.println("    " + cola.estado());
        System.out.println();

        System.out.println("  Paso 2: desencolar 3 veces");
        for (int i = 1; i <= 3; i++) {
            int valor = cola.dequeue();
            System.out.println("    dequeue() -> " + valor
                    + "   " + cola.mostrarArreglo());
        }
        System.out.println("    " + cola.estado());
        System.out.println();

        System.out.println("  Paso 3: encolar 60, 70, 80, 90");
        System.out.println("    En el ejercicio 9 los cuatro fallaban. Ahora:");
        System.out.println();

        int[] nuevos = { 60, 70, 80, 90 };
        int exitos = 0;
        for (int valor : nuevos) {
            boolean ok = cola.enqueue(valor);
            System.out.println("    enqueue(" + valor + ") -> " + ok
                    + "   " + cola.mostrarArreglo());
            if (ok) {
                exitos++;
            }
        }

        System.out.println();
        System.out.println("    " + cola.estado());
        System.out.println();
        System.out.println("  Aceptados: " + exitos + " de 4  (ejercicio 9: 0 de 4)");
        System.out.println();
        System.out.println("  Fijate donde quedaron 60, 70, 80: en las posiciones 0, 1 y 2,");
        System.out.println("  las mismas que dejo libres el desencolado. rear dio la vuelta.");
        System.out.println();

        System.out.println("  Estado final:");
        System.out.println("    " + cola.mostrarArreglo());
        System.out.println("    " + cola.estado());
        System.out.println();
        System.out.println("  La cola esta LLENA con 5 elementos y SIN ningun hueco perdido.");
    }

    private static void casoCapacidadCero() {
        System.out.println();
        System.out.println("=== CASO LÍMITE: capacidad 0 ===");
        System.out.println();

        ColaCircular cola = new ColaCircular(0);
        System.out.println("  isVacia(): " + cola.isVacia());
        System.out.println("  isLlena():  " + cola.isLlena()
                + "   (0 == 0: vacia y llena a la vez, y no es contradiccion)");
        System.out.println("  enqueue(1) -> " + cola.enqueue(1) + "   (falla, es correcto)");
        System.out.println("  dequeue()  -> " + cola.dequeue());
        System.out.println("  peek()     -> " + cola.peek());
        System.out.println("  Arreglo: " + cola.mostrarArreglo());
        System.out.println();
        System.out.println("  Con el contador no hay ambigüedad: 0 == 0 es una sola respuesta.");
    }

    /**
     * El ciclo que el ejercicio 9 rompía: llenarse, vaciarse, volver a llenarse.
     */
    private static void cicloLlenarVacias() {
        System.out.println();
        System.out.println("=== EL CICLO COMPLETO: llenar y vaciar, dos veces ===");
        System.out.println();

        ColaCircular cola = new ColaCircular(5);

        System.out.println("  Ronda 1:");
        int[] ronda1 = { 10, 20, 30, 40, 50 };
        for (int valor : ronda1) {
            cola.enqueue(valor);
        }
        System.out.println("    llena:   " + cola.mostrarArreglo() + "   " + cola.estado());
        System.out.println("    encolar un 6to -> " + cola.enqueue(60)
                + "   (correcto: no hay lugar)");
        System.out.println();

        while (!cola.isVacia()) {
            cola.dequeue();
        }
        System.out.println("    vacia:   " + cola.mostrarArreglo() + "   " + cola.estado());
        System.out.println();

        System.out.println("  Ronda 2 (mismo arreglo, mismos 5 lugares):");
        int[] ronda2 = { 70, 80, 90, 100, 110 };
        for (int valor : ronda2) {
            boolean ok = cola.enqueue(valor);
            if (!ok) {
                System.out.println("    enqueue(" + valor + ") -> FALLO");
            }
        }
        System.out.println("    llena:   " + cola.mostrarArreglo() + "   " + cola.estado());
        System.out.println();
        System.out.println("  En el ejercicio 9 la ronda 2 no podia empezar: la cola vacia");
        System.out.println("  rechazaba el primer encolado. Ahora entra los 5.");
    }

    /**
     * Muchas vueltas seguidas: si el módulo está mal puesto, esto se rompe.
     *
     * Se compara contra un modelo: un arreglo con front/rear/contador propios
     * que implementa la cola FIFO "a pelo", sin nada de módulo. Cada operación
     * se hace en los dos y se comparan. Si el módulo está mal, o la condición
     * de llena está mal, o el orden FIFO se rompió, los dos modelos divergen.
     */
    private static void pruebaDeEsfuerzo() {
        System.out.println();
        System.out.println("=== PRUEBA DE ESFUERZO: 40 vueltas contra un modelo ===");
        System.out.println();
        System.out.println("  Capacidad 5. Cada vuelta intenta encolar 3 y desencolar 2,");
        System.out.println("  encolando solo lo que entra. Se compara contra un modelo");
        System.out.println("  FIFO implementado sin módulo: si divergen, el módulo falla.");
        System.out.println();

        final int capacidad = 5;
        final int vueltas = 40;

        ColaCircular cola = new ColaCircular(capacidad);
        ModeloFifo modelo = new ModeloFifo(capacidad);
        int siguiente = 1;
        int errores = 0;
        int rechazos = 0;

        for (int vuelta = 1; vuelta <= vueltas; vuelta++) {
            for (int i = 0; i < 3; i++) {
                // El modelo es la referencia: decide si este encolado entra.
                boolean deberiaEntrar = !modelo.estaLlena();
                boolean ok = cola.enqueue(siguiente);
                if (ok != deberiaEntrar) {
                    System.out.println("    ERROR (vuelta " + vuelta + "): encolar " + siguiente
                            + " devolvio " + ok + " y el modelo esperaba " + deberiaEntrar);
                    errores++;
                }
                if (ok) {
                    modelo.encolar(siguiente);
                } else {
                    rechazos++;
                }
                siguiente++;
            }

            for (int i = 0; i < 2; i++) {
                int obtenido = cola.dequeue();
                int esperado = modelo.desencolar();
                if (obtenido != esperado) {
                    System.out.println("    ERROR (vuelta " + vuelta + "): dequeue devolvio "
                            + obtenido + " y el modelo esperaba " + esperado);
                    errores++;
                }
            }

            if (cola.size() != modelo.tamanio()) {
                System.out.println("    ERROR (vuelta " + vuelta + "): size() = " + cola.size()
                        + " y el modelo tiene " + modelo.tamanio());
                errores++;
            }
        }

        while (!cola.isVacia()) {
            cola.dequeue();
            modelo.desencolar();
        }

        System.out.println("  Vueltas: " + vueltas
                + "   valores generados: " + (siguiente - 1));
        System.out.println("  Encolados rechazados porque la cola estaba llena: " + rechazos);
        System.out.println("  Estado final: " + cola.estado());
        System.out.println("  Arreglo final: " + cola.mostrarArreglo());
        System.out.println();
        System.out.println("  Errores: " + errores
                + (errores == 0
                        ? "   -> el indice circular y el orden FIFO se mantienen"
                        : "   -> FALLO"));
    }

    /**
     * Cola FIFO de referencia, implementada SIN módulo y con condiciones
     * explícitas. Es el contraejemplo contra el que se contrasta la cola
     * circular: si las dos se comportan igual durante 40 vueltas, la
     * diferencia entre ellas es sólo la representación interna.
     */
    static class ModeloFifo {

        private final int[] datos;
        private int frente;
        private int fin;
        private int cantidad;

        ModeloFifo(int capacidad) {
            this.datos = new int[capacidad];
        }

        public void encolar(int valor) {
            if (estaLlena()) {
                return;
            }
            // Sin módulo: el índice se sale del arreglo y vuelve a 0 a mano.
            datos[fin] = valor;
            fin = (fin + 1) == datos.length ? 0 : fin + 1;
            cantidad++;
        }

        public int desencolar() {
            if (cantidad == 0) {
                return Integer.MIN_VALUE;
            }
            int valor = datos[frente];
            frente = (frente + 1) == datos.length ? 0 : frente + 1;
            cantidad--;
            return valor;
        }

        public int tamanio() {
            return cantidad;
        }

        public boolean estaLlena() {
            return cantidad == datos.length;
        }
    }

    private static void explicarElContador() {
        System.out.println();
        System.out.println("=== POR QUÉ HAY UN CONTADOR ===");
        System.out.println();
        System.out.println("  Sin contador, la condicion seria  front == rear.");
        System.out.println("  Y eso es ambiguo: no se puede distinguir de");
        System.out.println("      el estado 1:  front == rear == 5  ->  cola VACIA (se desencolaron 5)");
        System.out.println("      el estado 2:  front == rear == 5  ->  cola LLENA (se encolaron 5, rear dio la vuelta)");
        System.out.println("  Los dos estados dan indices identicos. Es la misma comparacion.");
        System.out.println();
        System.out.println("  Con contador, la ambiguedad desaparece:");
        System.out.println("      vacia  <->  contador == 0");
        System.out.println("      llena  <->  contador == capacidad");
        System.out.println();
        System.out.println("  LA ALTERNATIVA QUE NO SE USA: reservar una posicion.");
        System.out.println("  Nunca se encola en la ultima, asi que front == rear siempre");
        System.out.println("  significa vacia y no hace falta contador. El costo es concreto:");
        System.out.println("      capacidad efectiva = n - 1");
        System.out.println("  Un arreglo de 5 sirve para 4 elementos, y queda una posicion");
        System.out.println("  muerta. El contador no sacrifica ninguna.");
        System.out.println();
        System.out.println("  El modulo no es lo unico que cambia: tambien hay que agregar el");
        System.out.println("  contador. Son las dos piezas juntas las que resuelven el problema.");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Capacidad de la cola circular: ");

            // Guarda de EOF: sin esto el programa muere con
            // NoSuchElementException si se ejecuta sin entrada por consola.
            if (!entrada.hasNextInt()) {
                System.out.println("No se ingreso ningun dato.");
                return;
            }

            int capacidad = entrada.nextInt();
            if (capacidad < 0) {
                System.out.println("La capacidad no puede ser negativa.");
                return;
            }

            // nextInt deja el salto de línea pendiente: hay que consumirlo antes
            // de pasar a nextLine(), o el primer comando saldría vacío.
            entrada.nextLine();

            ColaCircular cola = new ColaCircular(capacidad);
            System.out.println("  Comandos: un numero para encolar, 0 para desencolar, EOF para salir.");

            while (entrada.hasNextLine()) {
                String linea = entrada.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                int valor;
                try {
                    valor = Integer.parseInt(linea);
                } catch (NumberFormatException e) {
                    System.out.println("    \"" + linea + "\" no es un numero. Ignorado.");
                    continue;
                }

                if (valor == 0) {
                    int salido = cola.dequeue();
                    if (salido == NO_VALIDO) {
                        System.out.println("    La cola esta vacia.");
                    } else {
                        System.out.println("    dequeue() -> " + salido);
                    }
                } else {
                    boolean ok = cola.enqueue(valor);
                    if (!ok) {
                        System.out.println("    enqueue(" + valor + ") rechazado: la cola esta llena.");
                    }
                }
                System.out.println("      " + cola.mostrarArreglo() + "   " + cola.estado());
            }

            System.out.println();
            System.out.println("  Cola final: " + cola.mostrarArreglo());
            System.out.println("  " + cola.estado());
        }
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se justificó que la detección de vacía/llena se haga CON contador y se pidió
 *   documentar la alternativa sin contador. El enunciado menciona el contador,
 *   pero sin explicar por qué la alternativa es insuficiente la elección parece
 *   arbitraria. La razón es que `front == rear` es ambiguo: dos estados de
 *   índices distintos dan la misma comparación.
 * - Se justificó la alternativa de reservar una posición, con su costo concreto:
 *   capacidad efectiva n - 1. Documentar solo la opción descartada, sin su
 *   precio, no sirve; el precio es lo que hace clara la decisión.
 * - Se justificó que el módulo NO es la solución completa. La versión inicial del
 *   prompt marcó el módulo como la corrección del ejercicio 9, y esa versión
 *   quedaba con `front == rear` como condición de vacía: mejoraba el
 *   desperdicio pero rompía la detección de cola llena. Hacen falta las dos
 *   piezas.
 * - Se agregó el caso de capacidad 0. No está en el enunciado y con
 *   `contador == capacidad` da `0 == 0`: la cola es vacía y llena a la vez. No es
 *   una contradicción porque no hay ningún elemento, pero había que verlo
 *   funcionando y no asumirlo.
 * - Se justificó la prueba de esfuerzo, que ahora contrasta la cola circular
 *   contra un modelo FIFO propio SIN módulo. La versión anterior repetía un
 *   patrón fijo (encolar 2, desencolar 1) y comparaba contra una aritmética
 *   calculada a mano: el patrón era neto positivo, así que la cola se llenaba
 *   en la vuelta 3 y las 37 comparaciones fallaban. El defecto estaba en el
 *   test, no en la cola. Comparar contra un modelo independiente cubre además
 *   el orden FIFO, que la versión anterior no verificaba.
 * - La comparación con el ejercicio 9 se pide explícitamente con la MISMA
 *   capacidad y la MISMA secuencia. Comparar con datos distintos no prueba que
 *   el cambio sea el módulo: podría ser cualquier otra cosa.
 * ========================================================================== */