# Los 10 prompts individuales — Guía 3

> **Por qué existe este archivo:** el enunciado pide, para cada ejercicio,
> construir un prompt específico ("Escribir un prompt para OpenCode que...").
> Acá están los 10, listos para copiar.
>
> Cada bloque es autónomo. **Son los mismos prompts que están embebidos dentro
> de cada `.java`**, en formato copiable.
>
> **Alternativa:** para resolver los 10 de una sola vez, usá
> `PROMPT_AGENTE_PROGRAMADOR_3.md`.

**Formato exigido por la consigna general** — cada prompt debe responder seis
cosas: qué algoritmo se usa, cuál es el razonamiento, qué datos recibe, qué debe
mostrar por pantalla, qué comentarios debe incluir el código, y cómo se verifica
que el resultado es correcto.

**Contexto común** (ya incluido en cada prompt): Java 21 · default package ·
archivo `EjercicioN.java` · clase `EjercicioN` · sin dependencias · los
algoritmos se implementan a mano, **prohibido usar `Arrays.sort` dentro de
ellos** (sí como oráculo de verificación) · contadores en `long` · UTF-8 en la
salida · el prompt va embebido como comentario al inicio del archivo · bloque de
ajustes al final.

---

## E1 — Bubble Sort paso a paso

Quiero implementar en Java un programa que ordene un arreglo de números enteros
usando **Bubble Sort**, mostrando el estado del arreglo después de cada pasada.

**ALGORITMO ELEGIDO**
Bubble Sort. Es el que pide el enunciado, y además es el más didáctico de la
familia: su única operación es comparar vecinos e intercambiarlos si están
desordenados, lo que hace visible la idea de "algo que sube hacia el final".

**RAZONAMIENTO**
Al recorrer el arreglo comparando cada elemento con su vecino de la derecha, el
valor mayor "flota" hacia el final: cada vez que se encuentra un valor mayor que
el de la derecha, el intercambio lo empuja una posición más. Al terminar una
pasada completa, el mayor de todo el rango quedó en la última posición y no se
vuelve a tocar. La segunda pasada trabaja sobre un rango una posición más corto,
y así hasta que el arreglo queda ordenado.

**DATOS QUE RECIBE**
Un arreglo de `int[]` cuya cantidad y valores se leen por consola: primero la
cantidad, luego cada elemento. En la demostración automática se usan cuatro
arreglos fijos: ordenado, inverso, aleatorio y casi ordenado.

**QUÉ DEBE MOSTRAR POR PANTALLA**
El arreglo original, y el estado del arreglo después de **cada pasada**, con el
número de comparaciones e intercambios de esa pasada. El enunciado pide los dos
primeros; el desglose por pasada es lo que hace visible por qué una pasada
terminó antes que otra.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
El invariante de cada pasada (los *i* mayores ya están en posición final), el
motivo de acortar el rango en cada vuelta, el motivo de la bandera de corte
temprano, y el significado exacto de cada contador.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
El arreglo final debe coincidir con una copia ordenada con `Arrays.sort` usada
**sólo como oráculo externo**, nunca dentro del algoritmo. Además se prueba con
arreglo vacío, un solo elemento, ya ordenado (0 intercambios) e inverso.

---

## E2 — Comparación entre Burbuja y Selección

Quiero implementar en Java dos métodos que ordenen el mismo arreglo de enteros:
`bubbleSort(int[] array)` y `selectionSort(int[] array)`, y comparar cuántas
comparaciones e intercambios hace cada uno.

**ALGORITMO ELEGIDO**
Los dos que el enunciado pide: Bubble Sort y Selection Sort. Tienen la misma
complejidad formal pero estrategias opuestas, lo que los hace ideales para
contrastar.

**RAZONAMIENTO**
Bubble Sort compara **vecinos**: sólo mira dos posiciones contiguas e intercambia
si están desordenadas. No sabe cuál es el mínimo; lo descubre por el camino, y por
eso el máximo "flota" hasta el final.

Selection Sort hace otra cosa: en cada pasada busca **directamente el mínimo** del
rango no ordenado recorriéndolo entero, y lo coloca en su posición final con un
único intercambio. Nunca compara vecinos: compara cada elemento del rango contra
el mínimo acumulado.

La diferencia clave, y es lo que la comparación debe revelar: **Bubble hace un
número de comparaciones que depende del orden de los datos** (varía entre n−1 y
n(n−1)/2), mientras que **Selection hace siempre exactamente n(n−1)/2
comparaciones**, esté el arreglo ordenado, invertido o aleatorio. El costo de
Selection es independiente de los datos.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola. Los dos algoritmos **parten de copias**
del mismo arreglo original, para que la comparación sea justa.

**QUÉ DEBE MOSTRAR POR PANTALLA**
Para cada algoritmo: el arreglo ordenado, las comparaciones y los intercambios.
Y una tabla que confronte los dos sobre cuatro tipos de datos (ordenado,
inverso, aleatorio, casi ordenado).

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
Por qué Bubble depende del orden de los datos y Selection no, el invariante de
cada uno, y el motivo de que Selection **no pueda** cortar de forma temprana.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Ambos resultados deben coincidir con el oráculo `Arrays.sort` y entre sí. Se
agregan los casos límite: vacío, un elemento, todos iguales, con duplicados.

---

## E3 — Inserción con arreglo casi ordenado

Quiero implementar en Java un **Insertion Sort** sobre un arreglo que ya esté
casi ordenado, y que el programa muestre cuántos desplazamientos realiza.

**ALGORITMO ELEGIDO**
Insertion Sort. Es el que pide el enunciado, y además es el único de la familia
que aprovecha de verdad el orden previo: si el elemento ya está en su lugar, no
hace nada.

**RAZONAMIENTO**
Insertion Sort funciona como una persona ordenando cartas en la mano: toma un
elemento, retrocede mientras el anterior sea mayor, y lo inserta en su lugar. El
prefijo que va recorriendo está **siempre ordenado**.

Eso explica por qué funciona bien con datos casi ordenados: **el costo depende
de la distancia que cada elemento tiene que recorrer**, no de la cantidad total
de elementos. En un arreglo ordenado, cada elemento está a cero posiciones de su
lugar y el algoritmo es lineal.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola. La demostración usa el ejemplo del
enunciado `{1, 2, 3, 5, 4, 6, 7}` y lo compara con otras tres situaciones.

**QUÉ DEBE MOSTRAR POR PANTALLA**
La cantidad de **desplazamientos** (que es lo que pide el enunciado) y también las
comparaciones, **por separado**. Y el estado del arreglo en cada pasada.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
El invariante del prefijo ordenado, por qué comparaciones y desplazamientos son
**dos contadores distintos**, y por qué este algoritmo es el mejor de los tres
simples cuando los datos ya vienen casi ordenados.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo `Arrays.sort`, y con los casos: ordenado, casi ordenado, inverso
y aleatorio.

---

## E4 — Ordenamiento de nombres con Selection Sort

Quiero implementar en Java un programa que ordene alfabéticamente un arreglo de
nombres (`String[]`) utilizando **Selection Sort**.

**ALGORITMO ELEGIDO**
Selection Sort. Es el que pide el enunciado, y además es el que mejor muestra el
cambio: para ordenar números se compara con el operador de relación, y para
ordenar textos hace falta una operación de comparación de cadenas, que no es un
"menor que" sino un método.

**RAZONAMIENTO**
Selection Sort busca el mínimo del rango no ordenado y lo coloca en su posición
final con un único intercambio. Ese mínimo se identifica comparando cada
candidato contra el mejor encontrado hasta ahora. Para texto, esa comparación la
hace `String.compareTo`, que devuelve un entero: negativo si el primero va antes,
cero si son iguales, positivo si va después.

**Punto crítico que el prompt debe destacar:** `compareTo` compara por valor de
código de carácter **Unicode**, y eso no es lo mismo que el orden alfabético.
`'A'`=65, `'Z'`=90, `'a'`=97: las mayúsculas tienen valores **menores** que las
minúsculas. Consecuencia concreta: `"ana"` se considera **mayor** que `"Lucia"`,
porque al comparar `'a'` (97) contra `'L'` (76), 97 > 76. En un diccionario
"ana" iría antes.

Por eso hay dos políticas, y el ejercicio muestra las dos: `compareTo`
(sensible a mayúsculas, el default de Java) y `compareToIgnoreCase` (insensible,
más cercano al diccionario).

**DATOS QUE RECIBE**
Un arreglo de `String[]` leído por consola: primero la cantidad, luego los
nombres, uno por línea. La demostración usa `{"Lucia", "Ana", "Pedro", "Juan"}`.

**QUÉ DEBE MOSTRAR POR PANTALLA**
El arreglo ordenado, las comparaciones y los intercambios, y sobre todo una
**comparación entre ordenar con mayúsculas sensibles y sin ellas**, porque los
resultados son distintos.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
Qué significa el valor devuelto por `compareTo`, por qué el orden de las letras
mayúsculas y minúsculas no coincide con el alfabético, y cuál es la política de
ordenamiento elegida.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo `Arrays.sort`, y mostrando la diferencia entre el orden sensible
e insensible con nombres que la hagan visible (`"lucia"` junto a `"Lucia"`).

---

## E5 — ShellSort explicando los gaps

Quiero implementar en Java un **ShellSort** que muestre el valor del gap en cada
etapa y el estado del arreglo después de cada pasada.

**ALGORITMO ELEGIDO**
Shell Sort. Es el que pide el enunciado, y es la generalización natural del
Insertion Sort: en vez de comparar vecinos, se comparan elementos que están a una
distancia `gap`.

**RAZONAMIENTO**
Shell Sort **es** un Insertion Sort con gap variable. La idea es que, con un gap
grande, un elemento puede viajar varias posiciones en una sola pasada en vez de
moverse de a uno. Eso permite "acercar" elementos que están muy lejos de su
posición final, y hace que las pasadas finales (con gap chica) tengan menos
trabajo.

La secuencia de gaps es decreciente y termina en 1. Cuando `gap = 1`, el
algoritmo degenera en un Insertion Sort normal, y esa última pasada es la que
termina el trabajo fino.

**Por qué mejora al Insertion Sort:** con datos aleatorios, el Insertion Sort
tiene que mover cada elemento muchas posiciones de a uno, y eso lo lleva a
Θ(n²). El Shell Sort evita ese costo porque el gap grande hace que la mayoría de
los elementos quede cerca de su lugar antes de llegar a las pasadas finas.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola. La demostración muestra el gap de cada
etapa sobre cuatro tipos de datos.

**QUÉ DEBE MOSTRAR POR PANTALLA**
**El valor del gap en cada etapa** y **el estado del arreglo tras cada pasada**,
tal como pide el enunciado.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
Qué secuencia de gaps se usa (Knuth: 1, 4, 13, 40, 121, 364…) y por qué, que
Shell Sort es Insertion Sort con gap variable, y por qué la última etapa con
gap 1 es indispensable.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo `Arrays.sort`, y con los casos: vacío, un elemento, ordenado,
inverso y aleatorio. Se compara además con un Insertion Sort de referencia para
que se vea por qué el Shell hace menos trabajo.

---

## E6 — Quicksort con primer elemento como pivote

Quiero implementar en Java un **Quicksort** que use siempre el primer elemento
como pivote, y que muestre el pivote elegido, el subarreglo izquierdo, el
subarreglo derecho y el arreglo final ordenado.

**ALGORITMO ELEGIDO**
Quicksort con pivote = primer elemento y partición de **Lomuto**. Es el que pide
el enunciado, y es el algoritmo más rápido en promedio de los vistos.

**RAZONAMIENTO — divide y vencerás**
Quicksort es el ejemplo canónico de divide y vencerás, y su estructura es
exactamente la misma que la de una función recursiva común:

- **Caso base:** el subarreglo tiene 0 o 1 elementos, ya está ordenado, se
  devuelve sin hacer nada.
- **Caso recursivo:** se particiona alrededor del pivote y se ordenan las dos
  partes por separado.

La diferencia con los ejercicios de recursividad es que ahora hay **dos** llamadas
recursivas, no una. Cada una trabaja sobre la mitad del rango, y por eso el costo
es Θ(n log n) en lugar de Θ(n).

**La partición de Lomuto:** para usar este esquema con el pivote en la primera
posición, primero se lo intercambia con el último elemento del rango. A partir de
ahí el pivote está al final y el procedimiento es: `i` marca la frontera entre
"menores que el pivote" y "por clasificar"; se recorre con `j`; si
`vector[j] <= pivote`, se intercambia con `vector[i]` y `i` avanza; al terminar, el
pivote se intercambia con `vector[i]` y ese índice es su posición definitiva.

Al terminar, todo lo que está en `[bajo, p-1]` es ≤ pivote y todo lo que está
en `[p+1, alto]` es ≥ pivote. El pivote queda en su posición definitiva, y `p`
siempre cae dentro de `[bajo, alto]`, lo que garantiza que las dos llamadas
recursivas reciben rangos estrictamente más chicos.

> Esa última garantía es la razón de usar Lomuto y no Hoare: la partición de
> Hoare puede devolver un índice **fuera** del rango (por ejemplo `bajo - 1`), y en
> ese caso la llamada recursiva recibiría el rango completo otra vez, con
> recursión infinita.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola: cantidad y luego valores.

**QUÉ DEBE MOSTRAR POR PANTALLA**
Las cuatro cosas que pide el enunciado en cada paso: el pivote elegido, el
subarreglo izquierdo, el subarreglo derecho y el arreglo final ordenado. Se
agregan las llamadas recursivas y la profundidad de pila, para conectar con la
guía de recursividad.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
El caso base, el caso recursivo, cómo la partición garantiza el orden a ambos
lados del pivote, y por qué se eligió Lomuto.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo `Arrays.sort`, y con los casos: vacío, un elemento, ya
ordenado, inverso y aleatorio.

---

## E7 — Peor caso de Quicksort

Quiero implementar en Java un **Quicksort** sobre un arreglo **ya ordenado**,
usando siempre el primer elemento como pivote, y contar cuántas llamadas
recursivas realiza.

**ALGORITMO ELEGIDO**
Quicksort con pivote = primer elemento, partición de Lomuto. Es exactamente el
mismo algoritmo del ejercicio anterior, pero aplicado al **peor caso**.

**RAZONAMIENTO — por qué el arreglo ordenado es el peor caso**
Quicksort funciona bien en promedio porque, en un arreglo aleatorio, el primer
elemento tiene aproximadamente la mediana como valor: la partición divide el
rango en dos mitades parecidas, y la profundidad de la recursión queda en
Θ(log n).

En un arreglo **ya ordenado**, el primer elemento es siempre el **mínimo**:

- al particionar, todo lo demás queda a la **derecha** del pivote;
- el subarreglo izquierdo queda **vacío** (nada es menor que el mínimo);
- el subarreglo derecho recibe n−1 elementos.

La partición **no divide: recorta**. Cada llamada recursiva quita un solo
elemento del rango, así que hay n−1 llamadas y la profundidad llega a n−1. El
costo es **Θ(n²) en tiempo y Θ(n) en pila de recursión**.

Es el mismo problema de fondo que suffer las recursiones que avanzan de a uno en
vez de dividirse a la mitad.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola. La demostración usa el ejemplo del
enunciado `{1, 2, 3, 4, 5, 6, 7}` y lo compara contra el caso aleatorio.

**QUÉ DEBE MOSTRAR POR PANTALLA**
La cantidad de **llamadas recursivas** y la **profundidad de pila**, que es lo
que pide el enunciado. Se agrega la comparación con el caso promedio para que se
vea la diferencia.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
Por qué la partición no divide, por qué el pivote siempre es el mínimo, la
comparación con el caso promedio, y el riesgo de `StackOverflowError`.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo `Arrays.sort`. Y demostrar que con un arreglo grande la
recursión desborda la pila, capturando el `StackOverflowError` con `try/catch`.

> **Cuidado con el tamaño:** con n grande la recursión desborda la pila (~15 000
> marcos en la JVM). Usá un tope modesto y capturá la excepción para que el
> programa termine limpiamente.

---

## E8 — MergeSort paso a paso

Quiero implementar en Java un **MergeSort** que muestre cómo divide el arreglo,
cuándo llega a la condición de corte y cómo va fusionando los subarreglos.

**ALGORITMO ELEGIDO**
Merge Sort. Es el único de la guía con complejidad **Θ(n log n) garantizada en
todos los casos**, incluido el peor. Es el precio: usa Θ(n) de memoria auxiliar.

**RAZONAMIENTO — la diferencia entre dividir y fusionar**
MergeSort tiene dos fases muy distintas que no deben confundirse:

- **Dividir** (de arriba hacia abajo): partir el arreglo por la mitad hasta
  llegar a subarreglos de 1 elemento. Un arreglo de un elemento ya está
  ordenado, así que ahí termina el proceso recursivo. Esta fase es **barata**:
  cada corte cuesta Θ(1), y sólo se decide dónde está la mitad. Hay Θ(log n)
  niveles de recursión.

- **Fusionar** (de abajo hacia arriba): dos subarreglos **ya ordenados** se
  combinan en uno ordenado. Se fusionan comparando los primeros elementos y
  tomando siempre el menor. Esta fase es **cara**: fusionar dos subarreglos de
  tamaño k cuesta Θ(k).

En cada nivel de la recursión los bloques que se fusionan suman n elementos en
total, así que cada nivel cuesta Θ(n). Con log n niveles → **Θ(n log n)**, lo que
se obtiene de la ecuación T(n) = 2T(n/2) + Θ(n).

**Punto que el prompt debe destacar:** MergeSort **no** es in-place. Usa un
arreglo temporal para la fusión, y ese Θ(n) de memoria auxiliar es
precisamente el precio de la garantía: a cambio de memoria, nunca tiene el
peor caso Θ(n²) que tienen Selection, Bubble e Insertion.

También es **estable**: al fusionar, cuando dos elementos son iguales se toma
primero el del rango **izquierdo**, lo que conserva el orden relativo original.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola: cantidad y luego valores.

**QUÉ DEBE MOSTRAR POR PANTALLA**
Las tres cosas que pide el enunciado: cómo divide el arreglo, cuándo llega a la
condición de corte, y cómo se van fusionando los subarreglos.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
La condición de corte y por qué 1 elemento ya está ordenado, el invariante de la
fusión (los dos rangos de entrada están ordenados), por qué MergeSort no es
in-place, y por qué la comparación debe ser `<=` y no `<` (de eso depende la
estabilidad).

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo `Arrays.sort`, y demostrar que es **estable** usando claves
repetidas: el identificador de llegada debe conservarse en orden.

---

## E9 — Comparación de algoritmos simples

Quiero implementar en Java los algoritmos **Bubble Sort, Selection Sort e
Insertion Sort**, ordenar el mismo arreglo con los tres y comparar cuántos
intercambios, comparaciones y desplazamientos realiza cada uno.

**ALGORITMO ELEGIDO**
Los tres que el enunciado pide. Son los algoritmos "simples" por excelencia, y su
valor pedagógico está en que tienen estructuras muy distintas aunque la
complejidad formal sea la misma.

**RAZONAMIENTO — por qué el objetivo es comparar estrategias y no resultados**
Los tres ordenan el mismo arreglo y producen **exactamente el mismo resultado**.
Si sólo se mirara el resultado final, no habría nada que comparar. Lo que cambia
es el **trabajo** que hacen para llegar ahí:

- **Bubble** compara vecinos y su costo depende del orden de los datos.
- **Selection** busca el mínimo en todo el rango: su costo es siempre el mismo,
  esté el arreglo como esté, pero hace pocos intercambios.
- **Insertion** inserta cada elemento en el prefijo: su costo depende de la
  distancia que cada elemento recorre, así que es el mejor con datos casi
  ordenados.

**DATOS QUE RECIBE**
Un arreglo de `int[]` leído por consola. Los tres algoritmos **parten de copias**
del mismo original, para que la comparación sea justa.

**QUÉ DEBE MOSTRAR POR PANTALLA**
Para cada algoritmo: el arreglo ordenado, las comparaciones y los
intercambios/desplazamientos. Y una tabla que confronte los tres sobre cuatro
tipos de arreglo (ordenado, inverso, aleatorio, casi ordenado), mostrando cómo
cambia cada columna.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
Cómo se verían las diferencias en la complejidad, y por qué Insertion Sort es el
mejor de los tres con datos casi ordenados.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Los tres resultados deben coincidir entre sí **y** con el oráculo `Arrays.sort`.
Sin esa comprobación cruzada, si uno de los tres tuviera un error la tabla
comparativa igual "funcionaría" mostrando números.

---

## E10 — Sistema de ranking de puntajes

Quiero implementar en Java un programa que reciba un arreglo de jugadores con
nombre y puntaje, y ordene el ranking de mayor a menor puntaje.

**ALGORITMO ELEGIDO — Insertion Sort, y por qué**
El enunciado deja libre la elección con la condición de justificarla. Elijo
**Insertion Sort**, y la justificación es esta:

Un ranking se actualiza de forma **incremental**: cada vez que termina una
partida, entra un jugador nuevo o cambia un puntaje, y hay que volver a ordenar
el ranking. Insertion Sort resuelve exactamente ese caso: si el ranking ya está
ordenado, insertar un jugador nuevo cuesta **Θ(n)** —buscar su posición y
desplazarla— en lugar de los Θ(n²) que costaría Selection Sort o los
Θ(n log n) de Quicksort.

Un Bubble Sort sería Θ(n) en este caso gracias a su bandera de corte, pero hace
**muchos intercambios**: si el jugador nuevo tiene el mejor puntaje, el recorrido
completo lo empuja desde el final, con n−1 intercambios. Insertion Sort hace un
solo desplazamiento.

Quicksort sería Θ(n log n) en promedio, pero desaprovecha que el ranking ya está
ordenado, que es información valiosa.

En resumen: Insertion Sort es el que **mejor se adapta a la forma en que el
problema se presenta en la realidad** (un dato nuevo cada vez).

**RAZONAMIENTO**
Insertion Sort recorre el arreglo de izquierda a derecha. En cada posición i
toma el jugador y lo inserta en el lugar correcto del ranking (prefijo ya
ordenado), desplazando hacia atrás a los que tienen menor puntaje. El "lugar
correcto" se busca comparando por puntaje **decreciente**, así que el mejor
queda siempre arriba.

**DESEMPATE — decisión documentada**
El enunciado no especifica qué hacer con puntajes iguales. Se elige el **orden
alfabético ascendente por nombre**. Es una decisión arbitraria, pero debe ser
explícita y consistente: sin ella, dos ejecuciones del mismo ranking podrían dar
resultados distintos y el ordenamiento parecería no ser determinista.

**DATOS QUE RECIBE**
Un arreglo de objetos `Jugador`, con `record Jugador(String nombre, int puntaje)`
y un `Comparator` que ordene por puntaje descendente y por nombre ascendente. La
demostración usa el ejemplo del enunciado: Ana(1200), Pedro(900), Lucia(1500).

**QUÉ DEBE MOSTRAR POR PANTALLA**
El ranking ordenado de mayor a menor, las comparaciones y los desplazamientos, y
la justificación del algoritmo elegido.

**COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO**
Por qué se eligió Insertion Sort, cómo se decide la posición de inserción en un
ranking descendente, y por qué el desempate por nombre es necesario.

**CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO**
Contra el oráculo: `Arrays.sort` con el **mismo `Comparator`**, que es la única
forma de confirmar que el criterio manual y el de la biblioteca aplican
exactamente la misma regla, incluso en los empates.