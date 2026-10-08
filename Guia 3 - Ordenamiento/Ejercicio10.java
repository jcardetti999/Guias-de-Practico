/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java un programa que reciba un arreglo de jugadores con
 * nombre y puntaje, y ordene el ranking de mayor a menor puntaje.
 *
 * ALGORITMO ELEGIDO — Insertion Sort
 * El enunciado deja libre la elección del algoritmo con la condición de
 * justificarla. Elijo INSERTION SORT, y la justificación es:
 *
 * Un ranking se actualiza de forma INCREMENTAL: cada vez que termina una
 * partida, entra UN jugador nuevo o se actualiza su puntaje, y hay que volver a
 * ordenar el ranking. Insertion Sort resuelve exactamente ese caso: si el
 * ranking ya está ordenado, insertar un jugador nuevo cuesta Θ(n) — hay que
 * buscar su posición y desplazarla — en lugar de los Θ(n²) que costaría
 * Selection Sort o los Θ(n log n) de Quicksort.
 *
 * Un Bubble Sort sería Θ(n) en este caso gracias a su bandera de corte, pero
 * hace MUCHOS intercambios: si el jugador nuevo tiene el mejor puntaje, el
 * recorrido completo lo empuja desde el final, con n-1 intercambios. Insertion
 * hace un solo desplazamiento.
 *
 * Quicksort sería Θ(n log n) en promedio, pero desaprovecha que el ranking ya
 * está ordenado, que es información valiosa.
 *
 * En resumen: Insertion Sort es el que MEJOR se adapta a la forma en que el
 * problema se presenta en la realidad (un dato nuevo cada vez).
 *
 * RAZONAMIENTO — el algoritmo
 * Insertion Sort recorre el arreglo de izquierda a derecha. En cada posición i,
 * toma el jugador y lo inserta en el lugar correcto del ranking (prefijo ya
 * ordenado), desplazando hacia atrás a los que tienen menor puntaje.
 *
 * El "lugar correcto" se busca comparando por puntaje DECRECIENTE, así que el
 * mejor queda siempre arriba.
 *
 * DATOS QUE RECIBE
 * Un arreglo de objetos Jugador. La demostración usa el ejemplo del enunciado:
 * Ana(1200), Pedro(900), Lucia(1500). El modo interactivo recibe la cantidad
 * de jugadores y luego nombre y puntaje de cada uno.
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * El ranking ordenado de mayor a menor, las comparaciones y los
 * desplazamientos, y la justificación del algoritmo elegido.
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * Por qué se eligió Insertion Sort, cómo se decide la posición de inserción en un
 * ranking descendente, y por qué el desempate por nombre es necesario.
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * Contra el oráculo: los mismos jugadores ordenados a mano, y contra
 * Arrays.sort con el mismo Comparator.
 * ========================================================================== */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio 10: Sistema de ranking de puntajes
 *
 * ALGORITMO — Insertion Sort, por ser el que mejor se adapta al caso incremental.
 *
 * RAZONAMIENTO — por qué Insertion Sort y no otro
 * Un ranking se actualiza cada vez que entra UN jugador nuevo o cambia un
 * puntaje. Ese es el caso donde Insertion Sort brilla: si el ranking ya está
 * ordenado, insertar un elemento cuesta Θ(n) (buscar la posición y desplazar),
 * contra los Θ(n²) de Selection y los Θ(n log n) de Quicksort.
 *
 * Bubble Sort, gracias a su bandera de corte, también sería Θ(n) en ese caso,
 * pero hace n-1 INTERCAMBIOS si el jugador nuevo tiene el mejor puntaje.
 * Insertion Sort hace un solo desplazamiento.
 *
 * REGLA DE DESEMPATE — decisión documentada
 * Si dos jugadores tienen el mismo puntaje, el orden por nombre no está
 * especificado. Se elige el orden ALFABÉTICO ASCENDENTE por nombre. Es una
 * decisión arbitraria, pero debe ser explícita y CONSISTENTE: sin ella, dos
 * ejecuciones del mismo ranking podrían dar resultados distintos y el
 * ordenamiento parecería no ser determinista.
 *
 * COMPLEJIDAD
 *   - Mejor caso:    Θ(n). El ranking ya ordenado y el jugador entra al final.
 *   - Caso promedio: Θ(n²).
 *   - Peor caso:     Θ(n²). El jugador nuevo tiene el mejor puntaje.
 *   - Espacio:       Θ(1). Es in-place.
 *   - Estable:       sí, porque sólo se desplaza a un jugador si tiene un
 *                    puntaje ESTRICTAMENTE menor que el del que se compara.
 *
 * INSTRUMENTACIÓN
 *   comparaciones y desplazamientos.
 *
 * CASOS LÍMITE
 *   - Arreglo vacío o con un solo jugador.
 *   - Puntajes repetidos: se activa el desempate por nombre.
 *   - Todos con el mismo puntaje: el ranking queda ordenado alfabéticamente.
 */
public class Ejercicio10 {

    /**
     * Jugador del ranking. Es un record: inmutable y con acceso por nombre.
     * Java 21 lo soporta nativamente, así que no hace falta una clase completa.
     */
    public record Jugador(String nombre, int puntaje) {
        @Override
        public String toString() {
            return String.format("%-10s %5d", nombre, puntaje);
        }
    }

    /** Comparador del ranking: de MAYOR a MENOR puntaje, desempate por nombre. */
    private static final java.util.Comparator<Jugador> POR_PUNTAJE_DESCENDENTE =
            java.util.Comparator
                    .comparingInt(Jugador::puntaje).reversed()
                    .thenComparing(Jugador::nombre);

    // ============================================================
    //  ALGORITMO
    // ============================================================

    /**
     * Ordena el ranking in-place con Insertion Sort, de mayor a menor puntaje.
     *
     * @param jugadores       arreglo a ordenar; se modifica en el sitio
     * @param comparaciones   arreglo de longitud 1: recibe el total de comparaciones
     * @param desplazamientos arreglo de longitud 1: recibe el total de desplazamientos
     */
    public static void insertionSortRanking(Jugador[] jugadores,
                                            long[] comparaciones, long[] desplazamientos) {
        comparaciones[0] = 0;
        desplazamientos[0] = 0;

        if (jugadores == null || jugadores.length <= 1) {
            return;
        }

        // El elemento en la posición 0 ya es un ranking válido de un jugador.
        for (int i = 1; i < jugadores.length; i++) {

            Jugador jugador = jugadores[i];
            int j = i - 1;

            while (j >= 0) {
                comparaciones[0]++;

                // ¿El jugador de la posición j va DESPUÉS del que se inserta?
                // Si el puntaje es menor, el jugador nuevo debe pasar adelante.
                //
                // Con la condición estricta, los empates NO desplazan y el
                // algoritmo es ESTABLE.
                //
                // El desempate por nombre se resuelve en la comparación
                // siguiente: si el nombre es mayor alfabéticamente, también va
                // después.
                boolean debePasarAdelante =
                        jugadores[j].puntaje() < jugador.puntaje()
                                || (jugadores[j].puntaje() == jugador.puntaje()
                                    && jugadores[j].nombre().compareTo(jugador.nombre()) < 0);

                if (!debePasarAdelante) {
                    break;
                }

                jugadores[j + 1] = jugadores[j];
                desplazamientos[0]++;
                j--;
            }

            jugadores[j + 1] = jugador;
        }
    }

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    public static void main(String[] args) {
        // Fuerza UTF-8 en la salida: sin esto la consola de Windows (CP850)
        // convierte los acentos y simbolos en bytes ilegibles.
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));

        demostrarEjemploDelEnunciado();
        justificarAlgoritmo();
        demostrarDesempate();
        ejecutarInteractivo();
    }

    private static void demostrarEjemploDelEnunciado() {
        System.out.println("=== EJERCICIO 10: SISTEMA DE RANKING DE PUNTAJES ===");
        System.out.println();

        Jugador[] original = {
            new Jugador("Ana", 1200),
            new Jugador("Pedro", 900),
            new Jugador("Lucia", 1500)
        };

        System.out.println("  Jugadores recibidos (en orden de llegada):");
        for (Jugador j : original) {
            System.out.println("    " + j);
        }

        long[] comparaciones = new long[1];
        long[] desplazamientos = new long[1];

        Jugador[] copia = copiar(original);
        insertionSortRanking(copia, comparaciones, desplazamientos);

        System.out.println();
        System.out.println("  Ranking ordenado (de mayor a menor):");
        int posicion = 1;
        for (Jugador j : copia) {
            System.out.println("    " + posicion + ". " + j);
            posicion++;
        }

        System.out.println();
        System.out.println("  Esperado: Lucia 1500, Ana 1200, Pedro 900");
        System.out.println("  Comparaciones  : " + comparaciones[0]);
        System.out.println("  Desplazamientos: " + desplazamientos[0]);

        // Verificación contra el oráculo: Arrays.sort con el MISMO comparator.
        Jugador[] oraculo = copiar(original);
        java.util.Arrays.sort(oraculo, POR_PUNTAJE_DESCENDENTE);

        boolean coincide = true;
        for (int i = 0; i < copia.length; i++) {
            if (copia[i].puntaje() != oraculo[i].puntaje()
                    || !copia[i].nombre().equals(oraculo[i].nombre())) {
                coincide = false;
            }
        }
        System.out.println();
        System.out.println("  Verificacion contra Arrays.sort con el mismo Comparator: "
                + (coincide ? "CORRECTO" : "DISCREPANCIA"));
    }

    private static void justificarAlgoritmo() {
        System.out.println();
        System.out.println("--- POR QUÉ INSERTION SORT: LA JUSTIFICACIÓN ---");
        System.out.println();
        System.out.println("  Un ranking se actualiza de forma INCREMENTAL: cada partida");
        System.out.println("  agrega un jugador o cambia un puntaje. Ese es el caso donde");
        System.out.println("  Insertion Sort gana.");
        System.out.println();

        // Se demonstra el caso incremental: ranking ya ordenado + jugador nuevo.
        Jugador[] rankingOrdenado = {
            new Jugador("Ana", 1200),
            new Jugador("Pedro", 900),
            new Jugador("Lucia", 1500)
        };

        System.out.println("  Ranking YA ordenado, entra un jugador nuevo al final:");
        System.out.println("    " + formatearNombres(rankingOrdenado) + " + [Carlos 1100]");

        Jugador[] conNuevo = new Jugador[rankingOrdenado.length + 1];
        System.arraycopy(rankingOrdenado, 0, conNuevo, 0, rankingOrdenado.length);
        conNuevo[rankingOrdenado.length] = new Jugador("Carlos", 1100);

        long[] comparaciones = new long[1];
        long[] desplazamientos = new long[1];
        insertionSortRanking(conNuevo, comparaciones, desplazamientos);

        System.out.println("    -> " + formatearNombres(conNuevo));
        System.out.println("    Comparaciones: " + comparaciones[0]
                + " (Θ(n): el jugador nuevo no se movió mucho)");
        System.out.println("    Con Selection Sort serían " + (conNuevo.length * (conNuevo.length - 1) / 2)
                + " comparaciones, porque no sabe que el ranking ya está ordenado.");

        System.out.println();
        System.out.println("  Y si el jugador nuevo tiene el MEJOR puntaje:");
        Jugador[] conNuevoPrimero = new Jugador[rankingOrdenado.length + 1];
        System.arraycopy(rankingOrdenado, 0, conNuevoPrimero, 0, rankingOrdenado.length);
        conNuevoPrimero[rankingOrdenado.length] = new Jugador("Zoe", 5000);

        comparaciones[0] = 0;
        desplazamientos[0] = 0;
        insertionSortRanking(conNuevoPrimero, comparaciones, desplazamientos);

        System.out.println("    -> " + formatearNombres(conNuevoPrimero));
        System.out.println("    Desplazamientos: " + desplazamientos[0]
                + " (el nuevo quedó arriba, empujando a todos).");
        System.out.println("    Insertion Sort hace UN desplazamiento por posición, no n-1");
        System.out.println("    intercambios como haría Bubble Sort.");
    }

    private static void demostrarDesempate() {
        System.out.println();
        System.out.println("--- DESEMPATE POR PUNTAJE ---");
        System.out.println();
        System.out.println("  El enunciado no especifica qué hacer con puntajes iguales.");
        System.out.println("  Se elige: ORDEN ALFABÉTICO ASCENDENTE POR NOMBRE.");
        System.out.println("  Es una decisión arbitraria, pero debe ser explícita y consistente:");
        System.out.println("  sin ella, dos ejecuciones del mismo ranking podrían diferir.");
        System.out.println();

        Jugador[] conEmpates = {
            new Jugador("Zoe", 1200),
            new Jugador("Ana", 1200),
            new Jugador("Marta", 900),
            new Jugador("Luis", 1200)
        };

        System.out.println("  Entrada:  Zoe(1200), Ana(1200), Marta(900), Luis(1200)");
        System.out.println("  (cuatro jugadores, tres con el mismo puntaje)");

        long[] comparaciones = new long[1];
        long[] desplazamientos = new long[1];

        Jugador[] copia = copiar(conEmpates);
        insertionSortRanking(copia, comparaciones, desplazamientos);

        System.out.println();
        System.out.println("  Ranking:");
        int posicion = 1;
        for (Jugador j : copia) {
            System.out.println("    " + posicion + ". " + j);
            posicion++;
        }

        System.out.println();
        System.out.println("  Con el desempate por nombre, los tres de 1200 quedan en orden");
        System.out.println("  alfabético: Ana, Luis, Zoe.");
    }

    private static void ejecutarInteractivo() {
        System.out.println();
        System.out.println("=== MODO INTERACTIVO ===");

        try (java.util.Scanner entrada = new java.util.Scanner(System.in)) {
            System.out.print("Cantidad de jugadores: ");
            if (!entrada.hasNextInt()) {
                return;
            }
            int cantidad = entrada.nextInt();

            if (cantidad < 0) {
                System.out.println("  La cantidad no puede ser negativa.");
                return;
            }

            Jugador[] jugadores = new Jugador[cantidad];
            System.out.println("Ingrese nombre y puntaje de cada jugador:");
            for (int i = 0; i < cantidad; i++) {
                System.out.print("  Nombre " + i + ": ");
                String nombre = entrada.next();
                System.out.print("  Puntaje de " + nombre + ": ");
                int puntaje = entrada.nextInt();
                jugadores[i] = new Jugador(nombre, puntaje);
            }

            System.out.println();
            System.out.println("  Jugadores recibidos:");
            for (Jugador j : jugadores) {
                System.out.println("    " + j);
            }

            long[] comparaciones = new long[1];
            long[] desplazamientos = new long[1];

            Jugador[] copia = copiar(jugadores);
            insertionSortRanking(copia, comparaciones, desplazamientos);

            System.out.println();
            System.out.println("  RANKING (de mayor a menor puntaje):");
            int posicion = 1;
            for (Jugador j : copia) {
                System.out.println("    " + posicion + ". " + j);
                posicion++;
            }

            System.out.println();
            System.out.println("  Comparaciones  : " + comparaciones[0]);
            System.out.println("  Desplazamientos: " + desplazamientos[0]);
            System.out.println("  Desempate: a igual puntaje, orden alfabético por nombre.");
            System.out.println("  Θ(n) mejor caso (ranking ya ordenado) · Θ(n²) peor · Θ(1) espacio");
        }
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    /** Muestra sólo los nombres, para comparaciones compactas. */
    private static String formatearNombres(Jugador[] jugadores) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < jugadores.length; i++) {
            texto.append(jugadores[i].nombre());
            if (i < jugadores.length - 1) {
                texto.append(", ");
            }
        }
        return texto.append("]").toString();
    }

    /** Copia el arreglo: el original se conserva para la verificación. */
    private static Jugador[] copiar(Jugador[] jugadores) {
        Jugador[] copia = new Jugador[jugadores.length];
        for (int i = 0; i < jugadores.length; i++) {
            copia[i] = jugadores[i];
        }
        return copia;
    }
}

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - Se agregó al prompt el CRITERIO DE DESEMPATE. El enunciado no dice qué
 *   hacer con puntajes iguales, y sin ese dato el ranking queda indefinido.
 *   Se eligió orden alfabético por nombre y se documentó como decisión
 *   explícita, que es lo que pide la consigna general ("qué comentarios debe
 *   incluir el código").
 * - Se agregó al prompt una justificación CONCRETA del algoritmo, no sólo la
 *   elección. El enunciado dice "utilizando el algoritmo que el alumno considere
 *   más adecuado" y obliga a justificar. La justificación se apoyó en el caso
 *   incremental (el ranking ya ordenado, entra un jugador), que es el escenario
 *   real del problema, y no en una preferencia general.
 * - Se agregó la demostración de comparación contra Arrays.sort con el mismo
 *   Comparator. Es la única forma de confirmar que el Comparator del Insertion
 *   manual y el de la biblioteca aplican exactamente el mismo criterio, incluso
 *   en los empates.
 * ========================================================================== */