# Los 10 prompts individuales (uno por ejercicio)

> **Por qué existe este archivo:** el enunciado de la cátedra pide, para cada
> ejercicio, construir un prompt específico. Acá están los 10, listos para copiar
> y pegar de a uno.
>
> Cada bloque es autónomo: funciona pegado solo, sin contexto previo.
>
> **Alternativa:** si querés resolver todo de una sola vez, usá
> `PROMPT_AGENTE_PROGRAMADOR.md`.

**Contexto común** (ya incluido en cada prompt): Java 21 · default package ·
archivo `EjercicioN.java` · clase `EjercicioN` · sin dependencias externas ·
sin `Arrays.sort()` · respuestas en español · código comentado con fines
didácticos (invariantes y casos límite, no lo autoevidente) · contadores de
instrumentación dentro de la lógica real del algoritmo.

---

## Ejercicio 1 — Encontrar el mínimo de un vector

Implementá en Java un algoritmo que encuentre el valor mínimo de un vector de
números enteros (`int[]`). Generá el archivo `Ejercicio1.java` con
`public class Ejercicio1` y su `main`.

**Antes de escribir el código, explicá:**

1. La estrategia que considerás más adecuada para resolver el problema.
2. Por qué esa estrategia es la correcta.
3. La complejidad temporal y espacial esperada, en notación Θ.

**Además, el código debe:**

- Explicar por qué **no es necesario ordenar el vector** para encontrar el
  mínimo. Ordenar es Θ(n log n) y además **destruye el vector de entrada**; en
  cambio el mínimo sólo requiere comparar cada elemento contra el mejor
  candidato. **Comparar no es ordenar.**
- Llevar un contador de comparaciones realizadas e informarlo.
- **No mutar el vector de entrada.**
- Manejar el vector vacío con un mensaje claro, y el vector de un solo elemento.
- Inicializar el acumulador con el primer dato, **nunca con 0** (sino falla con
  negativos).

Comentá el código en las partes con valor didáctico: la invariante del bucle y
el motivo de inicializar el acumulador.

---

## Ejercicio 2 — Buscar un elemento en un vector desordenado

Implementá en Java un algoritmo que busque un elemento dentro de un vector
(`int[]`) que **no está ordenado**. Generá `Ejercicio2.java` con
`public class Ejercicio2` y su `main`.

**El prompt debe:**

1. Indicar la estrategia que utilizará el algoritmo.
2. Justificar por qué esa estrategia es la adecuada.
3. Analizar el **mejor caso, el peor caso y el caso promedio**.

**Además, el programa debe:**

- Informar **cuántas posiciones fueron recorridas** hasta encontrar el elemento.
- Si el elemento **no existe**, informarlo explícitamente, indicando entonces
  que recorrió las *n* posiciones.
- Instrumentar un contador de posiciones recorridas.
- Manejar el vector vacío y los casos de: elemento en la primera posición,
  en la última, y ausente.

Comentá el código en las partes con valor didáctico, especialmente por qué el
corte temprano es seguro.

---

## Ejercicio 3 — Buscar un elemento en un vector ordenado

Implementá en Java un algoritmo de **búsqueda binaria** sobre un `int[]` ordenado
ascendentemente. Generá `Ejercicio3.java` con `public class Ejercicio3` y su `main`.

**El prompt debe:**

1. Declarar explícitamente la **precondición**: el vector está ordenado
   ascendentemente.
2. Explicar **por qué esta estrategia es más eficiente que una búsqueda lineal
   cuando los datos están ordenados**: el ordenamiento aporta *monotonía*, de
   modo que cada comparación descarta la mitad del rango candidato. Compará el
   costo total Θ(n log n) + Θ(log n) contra el Θ(n) de la búsqueda lineal, y
   señalá **en qué condiciones conviene igual ordenar primero**.
3. Indicar la complejidad esperada (mejor, promedio y peor).

**Además, el código debe:**

- Mostrar **paso a paso cómo evolucionan las variables de búsqueda**: número de
  iteración, `low`, `mid`, `high`, el valor comparado y la decisión tomada.
- Calcular `mid` como `low + (high - low) / 2` para evitar desbordamiento.
- Manejar: vector vacío, elemento duplicado, y valores mayor y menor que todos.
- **No** usar `Arrays.sort()` para el algoritmo (sólo, si querés, para preparar
  el vector de prueba).

---

## Ejercicio 4 — Detectar elementos duplicados

Implementá en Java **dos soluciones diferentes** para detectar elementos
duplicados dentro de un `int[]`. Generá `Ejercicio4.java` con
`public class Ejercicio4` y su `main`.

**Las dos soluciones deben ser:**

- **Solución A:** dos ciclos anidados (`i < j`).
- **Solución B:** una estructura `java.util.HashSet`.

**El prompt debe** incluir una comparación entre ambas implementaciones desde el
punto de vista de:

- **Complejidad temporal**: mejor caso, caso promedio y peor caso de cada una.
- **Complejidad espacial**: memoria auxiliar consumida por cada una.
- Mencionar explícitamente que `HashSet` degrada a **Θ(n²) en el peor caso**
  por colisiones de hash (degradación a lista enlazada).
- Un **veredicto**: cuándo conviene cada una, y qué se gana y qué se pierde al
  cambiar una por la otra.

**Además, el código debe** mostrar ambas ejecuciones sobre el mismo vector de
prueba e imprimir el vector de duplicados detectados en cada caso.

---

## Ejercicio 5 — Contar ocurrencias

Implementá en Java un algoritmo que cuente cuántas veces aparece un
determinado valor dentro de un `int[]`. Generá `Ejercicio5.java` con
`public class Ejercicio5` y su `main`.

**El prompt debe:**

1. Justificar **por qué el algoritmo necesita recorrer completamente el vector**:
   a diferencia de una búsqueda, no puede haber corte temprano, porque una
   coincidencia no descarta que haya más apariciones más adelante. Cortar en la
   primera coincidencia haría que el resultado fuera incorrecto.
2. Solicitar el **análisis de su complejidad** temporal y espacial.

**Además, el código debe:**

- Instrumentar un contador de comparaciones.
- Manejar los casos: 0 apariciones, todas las apariciones, y valor ausente.
- Informar el resultado y las comparaciones realizadas.

---

## Ejercicio 6 — Comparar dos vectores

Implementá en Java un algoritmo que determine si dos `int[]` son iguales.
Generá `Ejercicio6.java` con `public class Ejercicio6` y su `main`.

**El prompt debe:**

1. Indicar que el algoritmo **finalice apenas detecte una diferencia** (salida
   temprana) y justificar por qué eso es correcto y seguro.
2. Solicitar el **análisis del mejor caso, peor caso y caso promedio**, explicando
   cómo la salida temprana mejora la constante en el caso promedio.

**Además, el código debe:**

- **Decidir y documentar** qué ocurre si los vectores tienen longitudes
  distintas (recomendado: unequal, sin entrar al ciclo, con Θ(1)).
- Instrumentar el número de comparaciones hasta la primera diferencia.
- Manejar: ambos vacíos, distintas longitudes, diferencia en el índice 0,
  y diferencia en el último índice.

---

## Ejercicio 7 — Invertir un vector

Implementá en Java **dos versiones distintas** para invertir un `int[]`.
Generá `Ejercicio7.java` con `public class Ejercicio7` y su `main`.

**Las dos versiones deben ser:**

- **Versión A:** utilizando un **vector auxiliar** → Θ(n) tiempo, Θ(n) espacio.
- **Versión B:** invirtiendo el **mismo vector, in-place**, con dos punteros o
  `swap` → Θ(n) tiempo, Θ(1) espacio.

**El prompt debe** solicitar una comparación entre ambas soluciones indicando:

- **Ventajas y desventajas** de cada una.
- **Complejidad temporal y espacial** de cada una.
- Por qué la versión in-place es la opción por defecto en un motor real, y en qué
  contexto la versión con auxiliar sería preferible.

**Además, el código debe** verificar que la versión in-place no cruza los punteros
(`i` y `j` se determinant al cruzarse) y manejar el vector vacío, de un elemento
y de dos elementos.

---

## Ejercicio 8 — Sumar todos los elementos de una matriz

Implementá en Java un algoritmo que calcule la suma de todos los elementos de
una matriz `int[][]`. Generá `Ejercicio8.java` con `public class Ejercicio8` y su `main`.

**El prompt debe:**

1. Indicar **cómo se recorrerá la matriz** (doble ciclo: por cada fila,
   recorriendo todas sus columnas), y por qué ese recorrido recorre cada
   celda exactamente una vez.
2. Solicitar el **análisis de la complejidad** en función de las dimensiones
   *m* (filas) y *n* (columnas).
3. Pedir que el programa **contabilice la cantidad de operaciones realizadas**
   (adiciones).

**Además, el código debe:**

- Promover el acumulador a `long` para no desbordar `int` con matrices grandes.
- **Documentar la invariante**: tras visitar la celda *(i, j)*, el acumulador
  contiene la suma de todas las celdas del sub-rectángulo recorrido.
- Manejar: matriz vacía (sin filas, y filas con longitud cero), matriz 1×1, y
  matriz rectangular (m ≠ n).

---

## Ejercicio 9 — Encontrar el mayor elemento de una matriz

Implementá en Java un algoritmo que encuentre el mayor elemento de una matriz
`int[][]`. Generá `Ejercicio9.java` con `public class Ejercicio9` y su `main`.

**El prompt debe:**

1. Justificar **por qué resulta necesario recorrer todos los elementos**: el
   máximo descartado sólo se descarta a sí mismo, nunca descarta la región
   restante de la matriz. No hay información que habilite un corte temprano.
2. Solicitar el **análisis de la complejidad temporal** Θ(m·n) y **espacial**
   Θ(1).

**Además, el código debe:**

- Inicializar el máximo con el primer dato, **nunca con 0** (fallaría con una
  matriz de negativos).
- Instrumentar el contador de comparaciones.
- Manejar: matriz vacía, matriz con todos los valores negativos, y valores
  duplicados.

---

## Ejercicio 10 — Ordenamiento burbuja (Bubble Sort)

Implementá en Java el algoritmo **Bubble Sort**. Generá `Ejercicio10.java` con
`public class Ejercicio10` y su `main`.

**El prompt debe:**

1. Explicar **por qué este algoritmo resulta adecuado únicamente para fines
   didácticos o conjuntos muy pequeños de datos**: su caso promedio Θ(n²)
   supera a cualquier algoritmo de ordenamiento de producción
   (por ejemplo Timsort, Θ(n log n)), y es inestable.
2. Solicitar que el programa **contabilice comparaciones e intercambios**.

**Además, el código debe:**

- Implementar la **bandera `swapped`** para la salida temprana cuando no hubo
  intercambios en una pasada.
- **Contar comparaciones e intercambios por separado** — es el punto del
  ejercicio, porque evidencia el comportamiento adaptativo.
- Analizar el **mejor caso** Θ(n) (vector ya ordenado), y **promedio y peor**
  Θ(n²). Complejidad espacial Θ(1), in-place.
- **NO usar `Arrays.sort()`** — la implementación debe ser propia.
- Manejar: vector vacío, un solo elemento, ya ordenado (0 intercambios), y
  orden inverso (máximo de intercambios, *(n−1)·n/2*).