# Plan de resolución — Guía 3: Algoritmos de Ordenamiento

> Documento de **plan técnico**. No es código.
> Consumido por el agente `Programmer` (`.github/agents/programmer.agent.md`).
> Fuente: `Ejercicios3.docx` (10 ejercicios + consigna general).

---

## 1. Objetivo

Resolver los 10 ejercicios de ordenamiento en Java, produciendo el algoritmo
**y** el razonamiento que lo justifica, con un prompt de ingeniería redactado
antes de cada implementación.

---

## 2. Estado actual

| Ruta | Estado |
|------|--------|
| `Guia 3 - Ordenamiento/Ejercicios3.docx` | Enunciados (único archivo) |
| `EjerciciosN.java` | **No existen** — hay que crear los 10 |
| `_build_check.ps1` | **No existe** — hay que crearlo |

No hay sistema de build, ni JUnit, ni dependencias. JDK **21.0.10 LTS**.

---

## 3. Requisitos de la consigna general

La consigna pide, **por cada ejercicio**, un prompt que explique explícitamente
seis cosas:

| # | Sección obligatoria del prompt | Qué debe responder |
|---|---|---|
| 1 | Qué algoritmo de ordenamiento usar | Cuál de los 7 y por qué ese |
| 2 | Cuál es el razonamiento del algoritmo | La idea central, no la definición |
| 3 | Qué datos recibe el programa | Arreglo de ints, de Strings, de objetos… |
| 4 | Qué debe mostrar por pantalla | Requisito de instrumentación/visualización |
| 5 | Qué comentarios debe incluir el código | Invariantes, caso base, contadores |
| 6 | Cómo verificarse que el resultado es correcto | Casos de prueba y oráculos |

Además: *"probarla y ajustar el prompt si el resultado no es correcto"*. Hay un
ciclo de iteración, no un firing único.

### ⚠️ Ambigüedad detectada — requiere decisión

La consigna de la Guía 3 **no dice explícitamente** que el prompt deba ir
dentro del archivo `.java`, como sí exigía la Guía 2
(*"Al comenzar cada archivo .java, deberán incluir como comentario el prompt
inicial utilizado"*).

**Decisión propuesta:** embeber el prompt como bloque de comentario al inicio de
cada `.java`, igual que en la Guía 2. Es coherente con el resto del trabajo y es
lo que permite después registrar los ajustes del prompt, que la consigna también
pide implicitly ("ajustar el prompt si el resultado no es correcto").

**Riesgo:** si el docente NO lo pide, es contenido extra. Es inocuo y está
justificado, pero **queda bajo tu decisión**.

---

## 4. Qué cambia respecto de las guías anteriores

| | Guía 1 | Guía 2 | **Guía 3** |
|---|---|---|---|
| Foco | Complejidad de búsqueda/recorrido | Recursión y pila | **Estrategias de ordenamiento** |
| Qué se compara | Un algoritmo vs. el mismo | Base vs. base | **Algoritmo vs. algoritmo** |
| Salida clave | Contadores | Profundidad de pila | **Trazado de passes + contadores** |
| Estructura nueva | — | Divide y vencerer | **Ordenar `String[]` y objetos** |

**Novedad técnica:** es la primera guía donde el dato **no es un `int`**. E4
ordena `String[]` y E10 ordena objetos `Jugador`. Eso obliga a hablar de
`compareTo`, `Comparator` y de por qué el orden alfabético no es el mismo que
el numérico.

---

## 5. Los siete algoritmos y sus complejidades

| Algoritmo | Mejor | Promedio | Peor | Espacio | Estable |
|---|---|---|---|---|---|
| Bubble Sort | Θ(n)¹ | Θ(n²) | Θ(n²) | Θ(1) | Sí |
| Selection Sort | Θ(n²) | Θ(n²) | Θ(n²) | Θ(1) | No² |
| Insertion Sort | Θ(n) | Θ(n²) | Θ(n²) | Θ(1) | Sí |
| Shell Sort | Θ(n log n) | Θ(n log² n)³ | Θ(n^1.5)³ | Θ(1) | No |
| Quick Sort | Θ(n log n) | Θ(n log n) | Θ(n²)⁴ | Θ(log n)⁵ | No |
| Merge Sort | Θ(n log n) | Θ(n log n) | Θ(n log n) | **Θ(n)** | Sí |

¹ Con bandera de corte temprano. Sin ella, Θ(n²) siempre.
² Selection no es estable porque el intercambio puede "saltar" un elemento igual.
³ Secuencia de gaps de Knuth (1, 4, 13, 40, …). Con la secuencia 3x+1 es Θ(n^1.5).
⁴ Con pivote = primer elemento y arreglo **ya ordenado**.
⁵ Θ(log n) en promedio por la recursión; Θ(n) en el peor caso.

> Estos son los números que hay que mostrar en las tablas comparativas. Varios
> ejercicios piden comparar, y hacerlo con datos medidos —no con estos valores
> de Memoria— es lo que convierte la comparación en un experimento.

---

## 6. Especificación por ejercicio

### E1 — Bubble Sort paso a paso

| Campo | Detalle |
|---|---|
| Requisito de salida | **Arreglo original + estado después de cada pasada** |
| Razonamiento a explicar | El máximo se desplaza a la derecha en cada pasada; tras *k* pasadas, los *k* mayores están en su posición final |
| Contadores | Comparaciones e intercambios por pasada |
| Bandera | `huboIntercambio` para cortar si una pasada no intercambió nada |
| Invariante clave | Al terminar la pasada *i*, los *i* mayores elementos están al final, en posición definitiva |
| Complejidad | Θ(n²) promedio/peor, Θ(n) mejor, Θ(1) espacio |
| Casos límite | vacío · 1 elemento · ya ordenado (0 intercambios) · orden inverso (máximo: n(n−1)/2 intercambios) |

> **Distinción con Guía 1 / E10:** allí el burbuja ya estaba implementado con
> contadores. Acá lo que se agrega es el **trazado visual por pasada**, que es lo
> que hace comprensible el "burbujeo". No duplicar: reusar la idea, cambiar el
> foco a la visualización.

### E2 — Bubble vs. Selection

| Campo | Detalle |
|---|---|
| Métodos | `bubbleSort(int[])` y `selectionSort(int[])` — firmas pedidas literalmente |
| Requisito | Contar comparaciones e intercambios de **ambos** sobre el **mismo** arreglo |
| **Hallazgo clave** | Selection hace **exactamente n(n−1)/2 comparaciones siempre**, sin importar el orden. Bubble varía de n−1 a n(n−1)/2 |
| Razonamiento a explicar | Bubble compara **vecinos**; Selection **busca directamente el mínimo** en el rango no ordenado |
| Tabla | Comparaciones (mejor/promedio/peor) + intercambios + espacio, para los dos |
| Veredicto | Cuándo conviene cada uno |

**Éste es el ejercicio con mayor valor didáctico de toda la guía**: el conteo
empírico revela que el costo de Selection es *independiente de los datos*, algo
que no se intuye mirando el código.

### E3 — Insertion Sort sobre arreglo casi ordenado

| Campo | Detalle |
|---|---|
| Datos | `int[] numeros = {1, 2, 3, 5, 4, 6, 7}` (del enunciado) |
| Requisito | Mostrar **cuántos desplazamientos** realiza |
| Razonamiento | En cada pasada, el elemento se inserta en el sub-arreglo izquierdo ya ordenado; si ya está en su lugar, no hay desplazamiento |
| **Contraste clave** | Casi ordenado → Θ(n) comparaciones y **0 o muy pocos** desplazamientos, frente a Θ(n²) en el peor caso |
| Distinción | Contar **comparaciones** (búsqueda del lugar) por separado de **desplazamientos** (movimientos). No son lo mismo |
| Complejidad | Θ(n) mejor, Θ(n²) promedio/peor, Θ(1) espacio |

### E4 — Selection Sort sobre nombres

| Campo | Detalle |
|---|---|
| Datos | `String[] nombres = {"Lucia", "Ana", "Pedro", "Juan"}` |
| Resultado esperado | `["Ana", "Juan", "Lucia", "Pedro"]` |
| Requisito | Razonar **cómo cambia la comparación** al ordenar textos |
| Punto técnico | `String.compareTo` compara por valor de carácter **Unicode**, no alfabéticamente. `'A'`=65, `'Z'`=90, `'a'`=97 |
| **Consecuencia crítica** | Con mayúsculas, `"ana"` (a=97) resulta **mayor** que `"Lucia"` (L=76). Hay que decidir y **documentar** la política |
| Decisión propuesta | Ordenar con `compareTo` (case-sensitive) por defecto, y **mostrar también** una variante con `compareToIgnoreCase` para hacer visible la diferencia |
| Complejidad | Θ(n²) en todos los casos, Θ(1) espacio |

### E5 — ShellSort mostrando los gaps

| Campo | Detalle |
|---|---|
| Requisito | Mostrar **el valor del gap en cada etapa** y el **estado del arreglo tras cada pasada** |
| Secuencia de gaps | Knuth: `1, 4, 13, 40, 121, 364, …` hasta que el gap ≥ n |
| Razonamiento | Insertion Sort con saltos: comparar e intercambiar a distancia `gap`, lo que permite que un elemento "viaje" varias posiciones de una vez |
| **Por qué mejora** | En la etapa con gap grande, los elementos ya casi quedan en su posición final; las etapas siguientes pulen con gap cada vez menor |
| Punto clave | Shell Sort es **Insertion Sort congap variable** — decirlo explícitamente |
| Complejidad | Θ(n^1.5) con gaps de Knuth, Θ(1) espacio |

### E6 — Quicksort con primer elemento como pivote

| Campo | Detalle |
|---|---|
| Requisito de salida | **pivote elegido · subarreglo izquierdo · subarreglo derecho · arreglo final** |
| Razonamiento | **Divide y vencerás** — conexión directa con la Guía 2 |
| Partición | Lomuto: último elemento como pivote de la partición, o el esquema del enunciado. Se documenta cuál |
| **Vínculo con Guía 2** | El caso base es "subarreglo de 0 o 1 elementos"; el caso recursivo son dos llamadas. Es el mismo esqueleto que factorial, con dos ramas |
| Complejidad | Θ(n log n) promedio, Θ(1) espacio extra **más** la pila Θ(log n) |

### E7 — Peor caso de Quicksort

| Campo | Detalle |
|---|---|
| Datos | `int[] numeros = {1, 2, 3, 4, 5, 6, 7}` (del enunciado) |
| Requisito | **Contar las llamadas recursivas** |
| **Por qué es peor caso** | Con el arreglo ordenado y pivote = primero, el pivote es siempre el **mínimo**: el subarreglo izquierdo queda vacío y el derecho recibe n−1. La partición no divide: *rebana* |
| Resultado esperado | n−1 llamadas, profundidad máxima n−1 |
| **Cuidado con la demo** | Con n grande la recursión desborda la pila (~15 000 marcos). El tope debe ser modesto |
| Vínculo con Guía 2 | Es el mismo phenomenon que `descendenteInfinito(n + 1)`: una recursión cuya reducción es Θ(1) en vez de Θ(1/2) |

### E8 — MergeSort paso a paso

| Campo | Detalle |
|---|---|
| Requisito | Mostrar **cómo divide · cuándo llega al corte · cómo fusiona** |
| Razonamiento | Dividir hasta obtener subarreglos de 1 elemento (trivialmente ordenados), luego **fusionar** dos mitas ya ordenadas |
| **La distinction clave** | Dividir es Θ(1) por nivel; fusionar es Θ(k) por subarreglo de tamaño k. El total es Θ(n log n) |
| Ecuación | T(n) = 2T(n/2) + Θ(n) → Θ(n log n) por teorema maestro |
| **Espacial** | **Θ(n)** — es el único de la guía que **no** es in-place. El buffer auxiliar es la contrapartida de la garantía de Θ(n log n) en todos los casos |
| Estabilidad | Sí — **debe demostrarlo**, es su ventaja practical |

### E9 — Comparación de los tres algoritmos simples

| Campo | Detalle |
|---|---|
| Métodos | Bubble, Selection e Insertion sobre **el mismo** arreglo |
| Requisito | Mostrar: arreglo ordenado · comparaciones · intercambios/desplazamientos |
| Razonamiento | Comparar las **estrategias**, no sólo el resultado |
| **Hallazgo a destacar** | Los tres dan el mismo resultado; lo que difiere es el **número de operaciones** y, sobre todo, **cómo ese número depende del orden de los datos** |
| Tabla | Una fila por algoritmo, columnas: comparaciones, intercambios, dependencia del orden |
| Prueba decisive | Ejecutar los tres sobre: ordenado · inverso · aleatorio · casi ordenado. Las columnas cambian de forma reveladora |

### E10 — Ranking de puntajes

| Campo | Detalle |
|---|---|
| Datos | `Jugador("Ana", 1200)`, `Jugador("Pedro", 900)`, `Jugador("Lucia", 1500)` |
| Requisito | Ordenar de **mayor a menor** puntaje |
| Salida esperada | Lucia 1500 · Ana 1200 · Pedro 900 |
| **Elección del algoritmo** | El enunciado deja libre la elección pero **obliga a justificarla** |
| Justificación recomendada | Insertion Sort: un ranking se actualiza de a uno (llega una puntuación nueva cada vez), y insertar en un arreglo ya ordenado cuesta Θ(n). Es el caso de uso natural |
| Alternativa a discutir | Quicksort es Θ(n log n) y no aprovecha el orden previo; Selection es Θ(n²) fijo |
| **Desempate** | Deben definirse dos jugadores con el mismo puntaje. Criterio propuesto: menor puntaje → alfabético por nombre. **Documentar** |
| Tipo | `record Jugador(String nombre, int puntaje)` — Java 21 lo soporta. Ordenar con `Comparator` |
| Complejidad | La del algoritmo elegido, más Θ(1) por comparación |

---

## 7. Matriz de trazabilidad

| # | Algoritmo | Reduce | Instrumentación obligatoria | T (peor) | S |
|---|---|---|---|---|---|
| 1 | Bubble | rango −1 por pasada | estado por pasada + comparaciones + intercambios | Θ(n²) | Θ(1) |
| 2 | Bubble + Selection | — | comparaciones e intercambios de ambos | Θ(n²) | Θ(1) |
| 3 | Insertion | Insertar en el prefijo | comparaciones + **desplazamientos** | Θ(n²) | Θ(1) |
| 4 | Selection | rango −1 | posición de cada intercambio | Θ(n²) | Θ(1) |
| 5 | Shell | gap decreciente | **gap de cada etapa** + estado | Θ(n^1.5) | Θ(1) |
| 6 | Quick | dos mitas | **pivote + izquierda + derecha** | Θ(n²) | Θ(log n) |
| 7 | Quick (peor) | un elemento por llamada | **llamadas recursivas** + profundidad | Θ(n²) | Θ(n) |
| 8 | Merge | n → n/2 | división + corte + fusión | Θ(n log n) | **Θ(n)** |
| 9 | Los tres | — | tabla comparativa de los tres | Θ(n²) | Θ(1) |
| 10 | A elección | — | ranking + justificación del algoritmo | según elijo | según elijo |

---

## 8. Casos límite transversales

1. **Arreglo vacío** → mensaje claro, nunca excepción.
2. **Un solo elemento** → 0 comparaciones, 0 intercambios.
3. **Arreglo ya ordenado** → 0 intercambios (con bandera en Bubble).
4. **Orden inverso** → máximo de comparaciones e intercambios.
5. **Desbordamiento de contadores** → con int, los contadores de ordenamiento de Bubble/Selection/Insertion desbordan rápido: n(n−1)/2 supera $2^{31}$ en n > 65 536. Usar `long`.
6. **Igualdad de claves** → probarlo con duplicados; es donde se ve la instability de Selection y Shell.
7. **Todos iguales** → caso límite que separaalgorithms "estables" de "inestables".
8. **Texto vs. número** → el ASCII no coincide con el alfabético; documentar la decisión (E4).
9. **Empates en el ranking** → definir criterio de desempate (E10).

> El punto 5 es un riesgo real: con 100 000 elementos y Bubble Sort, el conteo de
> comparaciones es ~5·10⁹ y desborda `int`. Instrumentar con `long`.

---

## 9. Riesgos

| Riesgo | Mitigación |
|---|---|
| Duplicar el Bubble Sort de la Guía 1 / E10 | Cambiar el foco: allí se contaban operaciones; acá se **traza el estado por pasada**. Reusar la idea, no el archivo |
| Arrays desalineados por contenido en Shell, Merge o las bibliotecas de Java | Implementar todo a mano. Prohibido `Arrays.sort`, `Collections.sort`, `List.sort` |
| Reportar las complexidades de memoria sin medirlas | Exigir que las tablas comparativas se llenen con **contadores observados**, no con valores de Memoria |
| Elegir mal el algoritmo en E10 | La consigna obliga a justificar; documentar la justificación aunque elija el "correcto" |
| Demos que desbordan la pila en Quicksort | Tope modesto en E7 y captura de `StackOverflowError` si se excede |
| La consigna no exige embeber el prompt (ambigüedad del §3) | Se embebe igual por coherencia; **queda documentado como decisión** |
| Confundir Θ(n) de Shell según la secuencia de gaps | Fijar la secuencia explícitamente y citarla |

---

## 10. Criterios de aceptación

- [ ] Existen los 10 archivos `Ejercicio1.java` … `Ejercicio10.java`.
- [ ] Compilan con `javac -encoding UTF-8`.
- [ ] Cada archivo trae el prompt embebido con las **6 secciones** de la consigna.
- [ ] Cada archivo trae el bloque de ajustes del prompt.
- [ ] Los 10 usan UTF-8 en la salida.
- [ ] **Ningún archivo usa `Arrays.sort`, `Collections.sort` ni `List.sort`.**
- [ ] E1 muestra el estado tras cada pasada.
- [ ] E2 muestra la tabla comparativa Bubble vs. Selection con contadores reales.
- [ ] E3 cuenta comparaciones **y** desplazamientos por separado.
- [ ] E4 documenta la política de mayúsculas y muestra la diferencia.
- [ ] E5 muestra el gap de cada etapa.
- [ ] E6 muestra pivote, subarreglo izquierdo, subarreglo derecho y resultado.
- [ ] E7 cuenta llamadas recursivas sobre el arreglo ordenado.
- [ ] E8 muestra división, corte y fusión.
- [ ] E9 muestra los tres algoritmos sobre los mismos 4 tipos de arreglo.
- [ ] E10 ordena de mayor a menor y justifica el algoritmo elegido.
- [ ] Todos los contadores usan `long`.
- [ ] La complejidad del Javadoc coincide con los contadores observados.
- [ ] `_build_check.ps1` compila y ejecuta los 10 sin excepciones.

---

## 11. `_build_check.ps1`

El de la Guía 2 sirve de base, con estas correcciones ** ya aprendidas**:

1. **Nunca hardcodear "Guia 3"**: buscar el `.docx` de enunciados en
   `$PSScriptRoot` y, si falta, en una carpeta hermana del mismo nombre.
2. **Entrada por archivo temporal con `cmd /c "... < archivo"`** — el pipeline
   de PowerShell 5.1 **siempre** antepone BOM y rompe `Scanner.hasNextInt()`
   (convierte `"5"` en `"\uFEFF5"`).
3. **Todos los ejercicios reciben entrada**, incluidos los que no la necesitan:
   un archivo vacío no produce EOF y el proceso queda colgado.
4. **`$OutputEncoding` y `[Console]::OutputEncoding` en `try` separados**: si el
   primero lanza, el segundo nunca se asigna.
5. **Banner de error claro** si no encuentra los `.java` (la carpeta empieza vacía).

Comando:

```powershell
cd "c:\Facu Things\2026\Estructura de Datos y Algoritmo\Guias de Practico\Guia 3 - Ordenamiento"
powershell -ExecutionPolicy Bypass -File .\_build_check.ps1
```