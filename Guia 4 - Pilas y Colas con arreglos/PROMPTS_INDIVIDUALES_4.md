# Los 10 prompts individuales — Guía 4

> **Por qué existe este archivo:** el enunciado pide, para cada ejercicio,
> construir un prompt específico ("Escribir un prompt para OpenCode que...").
> Acá están los 10, listos para copiar.
>
> Cada bloque es autónomo. **Son los mismos prompts que están embebidos dentro
> de cada `.java`**, en formato copiable.
>
> **Alternativa:** para resolver los 10 de una sola vez, usá
> `PROMPT_AGENTE_PROGRAMADOR_4.md`.

**Formato exigido por la consigna general** — cada prompt debe responder seis
cosas: qué estructura se usa, cuál es el razonamiento, qué datos recibe, qué debe
mostrar por pantalla, qué comentarios debe incluir el código, y cómo se verifica
que el resultado es correcto.

**Contexto común** (ya incluido en cada prompt): Java 21 · default package ·
archivo `EjercicioN.java` · clase `EjercicioN` · sin dependencias · las
estructuras se implementan **a mano sobre arreglos**, **prohibido usar
`ArrayDeque`, `Stack`, `LinkedList`, `ArrayList` o `Queue` de `java.util`**
(sí se puede `Scanner` para la entrada) · sin streams ni lambdas · toda
operación sobre estructura vacía o llena informa, **ninguna lanza excepción** ·
UTF-8 en la salida · el prompt va embebido como comentario al inicio del
archivo · bloque de ajustes al final.

---

## E1 — Pila de enteros

Quiero implementar en Java una **pila de enteros** usando un arreglo de tamaño
fijo.

**ESTRUCTURA**
Pila (LIFO). El último que entra es el primero que sale. Se implementa con un
índice `top` que apunta al elemento del tope y crece hacia atrás.

**RAZONAMIENTO**
`top` arranca en `-1`, que significa "pila vacía: no hay tope". Al apilar se
escribe en `elementos[top + 1]` y se incrementa `top`; al desapilar se lee
`elementos[top]` y se decrementa. La pila queda vacía exactamente cuando `top`
vuelve a `-1`, y llena cuando `top == capacidad - 1`.

**DATOS QUE RECIBE**
Capacidad inicial, valores a apilar, valores a desapilar.

**QUÉ DEBE MOSTRAR POR PANTALLA**
El estado de la pila después de cada operación: `push(x)`, `pop()`, `top()`,
`isEmpty()`, `size()`.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué `top` arranca en `-1` y no en `0`. Por qué la condición de llena es
`top == capacidad - 1`. Qué valor centinela devuelve `pop()` si la pila está
vacía y por qué ese valor y no una excepción.

**CÓMO SE VERIFICA**
Apilar 10, 3, 7, 9 y desapilarlos: deben salir 9, 7, 3, 10 (no 10, 3, 7, 9 —
ese es el error típico de confundir pila con cola).

---

## E2 — Simulador de torre de platos

Quiero implementar en Java un simulador de una **torre de platos** usando una
pila.

**ESTRUCTURA**
Pila de enteros, igual que el ejercicio 1, pero con la lógica del caso de uso.

**RAZONAMIENTO**
En una torre de platos el último plato colocado es el primero que se puede
tomar, porque los de abajo están tapados. Esa es exactamente la definición de
LIFO.

**DATOS QUE RECIBE**
Cantidad de platos y el orden en que se colocan (de abajo hacia arriba).

**QUÉ DEBE MOSTRAR POR PANTALLA**
El estado visual de la torre después de cada colocación y de cada retiro, y
mensaje claro cuando se intenta tomar un plato de una torre vacía.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué este caso es LIFO y no FIFO. Qué pasaría si se usara una cola: el
primero colocado quedaría trabado abajo y no podríaVERSE.

**CÓMO SE VERIFICA**
Colocar los platos 1, 2, 3 y retirarlos: deben salir 3, 2, 1.

---

## E3 — Paréntesis balanceados

Quiero implementar en Java un verificador de **paréntesis balanceados** usando
una pila de caracteres.

**ESTRUCTURA**
Pila de `char`. Cada carácter de apertura se apila; cada de cierre se
desapila y se compara con el último abierto.

**RAZONAMIENTO**
`{`, `[` y `(` se apilan. Al encontrar un cierre, la cima de la pila tiene que
ser la apertura correspondiente; si no lo es, la expresión está mal formada. Al
terminar, la pila tiene que haber quedado vacía: si sobraron aperturas, faltan
cierres.

**DATOS QUE RECIBE**
Expresiones de texto con paréntesis de las tres clases.

**QUÉ DEBE MOSTRAR POR PANTALLA**
Si la expresión es VÁLIDA o INVÁLIDA, y en el caso inválido **dónde** falla: el
cierre que sobra, o qué apertura quedó sin cerrar.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué hace falta distinguir los tres tipos de paréntesis y no alcanza con
contar. Por qué una pila y no un simple contador: los tres tipos se anidan
(`([{}])`) y el orden de cierre importa.

**CÓMO SE VERIFICA**
- `(5 + 3) * (2 + 1)` → VÁLIDO
- `(5 + 3)) * (2 + 1` → INVÁLIDO (sobra un cierre)
- `((2 + 3)` → INVÁLIDO (falta un cierre)
- `)(` → INVÁLIDO

---

## E4 — Historial de navegación

Quiero implementar en Java el historial de navegación de un navegador, usando
una pila.

**ESTRUCTURA**
Pila de `String` con las URLs visitadas, más un índice `actual` que marca en qué
posición del historial está el navegador.

**RAZONAMIENTO**
Atrás es LIFO sobre las páginas visitadas: la última visitada es la primera a la
que se vuelve. Adelante, en cambio, necesita una **segunda pila**: las páginas
que se pueden volver a recuperar. Cuando el usuario navega a una URL nueva desde
una página que tenía historial adelante, ese historial adelante se descarta, como
en un navegador real.

**DATOS QUE RECIBE**
Lista de URLs visitadas, cuántas veces pulsar atrás y adelante.

**QUÉ DEBE MOSTRAR POR PANTALLA**
La URL actual, el historial completo, y el resultado de cada `volverAtras()` y
`irAdelante()`.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué adelante necesita pila propia y no un índice que va sumando. Por qué
visitar una URL nueva descarta el historial adelante.

**CÓMO SE VERIFICA**
Visitar `google.com`, `github.com`, `stackoverflow.com` y volver atrás una vez:
debe aparecer `stackoverflow.com` y quedar `github.com` como actual.

---

## E5 — Pila genérica

Quiero implementar en Java una **pila genérica** que funcione con cualquier tipo
de dato.

**ESTRUCTURA**
Pila genérica con `Object[]` como arreglo interno, con conversión segura
mediante `@SuppressWarnings("unchecked")` al hacer el `get`.

**RAZONAMIENTO**
Java borra los tipos genéricos: un `new T[]` no compila dentro de una clase
genérica. La solución estándar es backed por `Object[]` y castear sólo en el
punto de salida, que es donde el tipo ya fue validado por el compilador al
insertar.

**DATOS QUE RECIBE**
Tipo de elementos, cantidad y valores.

**QUÉ DEBE MOSTRAR POR PANTALLA**
`push`, `pop`, `peek`/`top`, `isEmpty`, `size` para dos tipos distintos: una
pila de `Integer` y una de `String`.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué `Object[]` y no `T[]`. Qué garantiza y qué no garantiza el
`@SuppressWarnings`: silencia al compilador, no verifica nada en runtime.

**CÓMO SE VERIFICA**
- `Pila<Integer>` con 10, 20, 30 → `pop()` devuelve 30, 20, 10
- `Pila<String>` con "hola", "adios" → `pop()` devuelve "adios", "hola"

---

## E6 — Cola de enteros

Quiero implementar en Java una **cola de enteros** usando un arreglo de tamaño
fijo.

**ESTRUCTURA**
Cola (FIFO). El primero que entra es el primero que sale. Se implementa con dos
índices: `front` (posición del primer elemento) y `rear` (posición del **próximo
espacio libre**, no la del último elemento).

**RAZONAMIENTO**
Al encolar se escribe en `elementos[rear]` y se incrementa `rear`. Al desencolar
se lee `elementos[front]` y se incrementa `front`. La franja viva va de `front`
(inclusive) hasta `rear - 1` (inclusive). La cola está vacía cuando
`front == rear`; llena cuando `front == 0 && rear == capacidad`.

**DATOS QUE RECIBE**
Capacidad y valores a encolar.

**QUÉ DEBE MOSTRAR POR PANTALLA**
El arreglo interno después de cada operación, con los espacios libres marcados
como `_` y los dos índices y el tamaño visibles.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué `front` y `rear` arrancan **ambos en 0** y no en 0 y 1. Por qué `rear`
apunta al próximo espacio libre. Por qué la condición de llena exige
`front == 0`.

**CÓMO SE VERIFICA**
Encolar 10, 20, 30, 40 y desencolarlos: deben salir 10, 20, 30, 40, en ese
orden. Y después de desencolar, avisar que los espacios del principio quedaron
libres pero inservibles, porque `rear` no retrocede.

---

## E7 — Sistema de turnos

Quiero implementar en Java un **sistema de turnos** para una oficina, usando una
cola de cadenas de texto.

**ESTRUCTURA**
Cola de `String`. Cada persona que llega se agrega al final; se atiende siempre
la que está adelante.

**RAZONAMIENTO**
El prompt debe explicar **por qué este problema usa FIFO y no LIFO**: el orden
de llegada ES el orden de atención. Si fuera una pila, se atendería a la última
persona que llegó, lo cual sería injusto — alguien que llegó a las 9 sería
atendido antes que alguien que llegó a las 8.

**DECISIÓN DOCUMENTADA**
El enunciado no pide numerar los turnos, así que el turno de cada persona **es su
posición en la cola**. Si una persona llega con la cola vacía, su turno es el 1.
La alternativa (asignar un número creciente con un contador aparte) se descarta
por ser redundante, y queda dicho en el código.

**DATOS QUE RECIBE**
Capacidad de la sala y los nombres de quienes llegan.

**QUÉ DEBE MOSTRAR POR PANTALLA**
La sala de espera con el número de turno de cada persona, y el orden en que se
atienden.

**QUÉ COMENTARIOS DEBE INCLUIR**
La comparación explícita del mismo caso atendido como cola y como pila,
enfrentados, para que la justificación sea comprobable y no una afirmación.

**CÓMO SE VERIFICA**
- Llegan Ana, Pedro y Lucia: se atienden en ese orden
- Cola vacía: `atenderSiguiente()` informa, no falla
- Consultar el primero sin sacarlo: sigue en la cola
- Sala de capacidad 2 llena: el tercero es rechazado

---

## E8 — Cola de impresión

Quiero implementar en Java una **cola de impresión** de documentos usando
arreglos. Cada documento tiene un **nombre** y una cantidad de **páginas**.

**ESTRUCTURA**
Cola de documentos (un `record Documento(String nombre, int paginas)`). La
unidad de la cola es el **documento completo**, no la página.

**RAZONAMIENTO**
El prompt tiene que dejar escrito **por qué es una cola de documentos y no de
páginas**. Una cola de páginas intercalaría los documentos (pág 1 del Informe,
pág 1 del Curriculum, pág 2 del Informe…) y produciría un documento ilegible. La
impresión es de a un documento completo a la vez.

**DATOS QUE RECIBE**
Capacidad y documentos como `"nombre páginas"`.

**QUÉ DEBE MOSTRAR POR PANTALLA**
La cola en orden, el próximo documento a imprimir sin imprimirlo, las páginas
impresas en cada paso, y el total impreso al terminar.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué la unidad es el documento. Por qué se rechaza un documento con 0 o menos
páginas: no imprimiría nada, ocuparía un lugar en la cola y haría avanzar el
orden sin motivo.

**CÓMO SE VERIFICA**
Documentos Informe (10), Curriculum (2), Factura (5):
- se imprimen en ese orden
- al imprimir Informe quedan 8 páginas menos y siguen 2 documentos pendientes
- total impreso: 10 + 2 + 5 = 17
- documento con 0 páginas: rechazado

---

## E9 — Problema del desperdicio de espacio

Quiero implementar en Java una demostración del **problema de desperdicio de
espacio** en una cola implementada con arreglos.

**EL OBJETIVO NO ES RESOLVER EL PROBLEMA: ES DEMOSTRARLO.**
Con "resolver" en el enunciado, la implementación termina siendo cola circular,
que es el ejercicio 10 y deja este sin contenido.

**ESTRUCTURA**
La misma cola del ejercicio 6, más un método `mostrarArreglo()` que marca cada
posición: `[valor]` ocupada, `[_]` libre pero inalcanzable, `[-]` nunca usada.

**RAZONAMIENTO**
`front` y `rear` sólo avanzan hacia adelante, así que los huecos que deja
`front` quedan siempre del lado izquierdo y `rear` nunca vuelve a alcanzarlos.
La condición de llena es `front == 0 && rear == capacidad`: el `front == 0` es
el defecto, porque en cuanto se desencola algo la condición nunca más se cumple.

**LA SECUENCIA EXACTA, con capacidad 5**
1. Encolar 10, 20, 30, 40, 50 → la cola queda llena
2. Desencolar 3 veces → salen 10, 20, 30
3. Mostrar el arreglo → debe verse `[_] [_] [30] [40] [50]`
4. Intentar encolar 60, 70, 80, 90 → **todos tienen que fallar**
5. Vaciar la cola por completo y mostrar que **sigue sin poder encolar**

El paso 5 es el que descarta la explicación cómoda: si el espacio se recuperara
al vaciarse, el defecto sería tolerable.

**QUÉ DEBE MOSTRAR POR PANTALLA**
El arreglo con las tres marcas distintas en cada paso. La distinción entre `[_]`
y `[-]` es lo que hace visible el defecto: sin ella, el final del ejercicio se lee
como un bug y no como la prueba.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué la condición de llena tiene esa forma. Por qué el encolado se rechaza
aunque `isLlena()` siga diciendo `false`. Por qué la solución NO se implementa acá.

**CÓMO SE VERIFICA**
El punto final: un arreglo de 5 posiciones termina sirviendo como una cola de 3.
Hay que poder llegar a "cola vacía, 0 elementos" y que `enqueue` falle.

---

## E10 — Cola circular

Quiero implementar en Java una **cola circular** sobre arreglos, que es la
solución al problema del ejercicio 9.

**ESTRUCTURA**
Cola circular con dos índices que dan la vuelta:
```
enqueue -> rear  = (rear  + 1) % capacidad
dequeue -> front = (front + 1) % capacidad
```

**DETECCIÓN DE VACÍA Y LLENA — CON CONTADOR**
Se agrega un campo `contador`:
```
isVacia() -> contador == 0
isLlena()  -> contador == capacidad
size()     -> contador
```

**RAZONAMIENTO**
Sin contador, `front == rear` es **ambiguo**: el mismo estado de índices puede
significar cola vacía o cola llena. El contador elimina la ambigüedad.

**LA ALTERNATIVA QUE SE DESCARTA**
Reservar una posición (nunca encolar en la última), para que `front == rear`
siempre signifique vacía. El costo: la capacidad efectiva pasa a ser `n - 1` —
un arreglo de 5 sirve para 4 elementos y queda una posición muerta. Hay que
documentar el costo, no sólo la opción descartada.

**Punto clave:** el módulo **no es la solución completa**. Sólo con el módulo la
detección de cola llena se rompe. Hacen falta las dos piezas.

**LA COMPARACIÓN CON EL EJERCICIO 9**
Hay que repetir **la misma secuencia y la misma capacidad (5)**:
1. Encolar 10, 20, 30, 40, 50
2. Desencolar 3 veces
3. Encolar 60, 70, 80, 90 → **ahora los tres primeros tienen que entrar**

Comparar con datos distintos no prueba que el cambio sea el módulo.

**QUÉ COMENTARIOS DEBE INCLUIR**
Por qué el módulo reemplaza a la condición `front == 0` del ejercicio 6. Por qué
la alternativa sin contador es ambigua. Por qué la de reservar una posición se
descarta y a qué costo.

**CASOS LÍMITE A CONTEMPLAR**
- Cola vacía y cola llena: informan, no fallan
- **Capacidad 0**: `contador == capacidad` da `0 == 0`. Vacía y llena a la vez, y
  no es contradicción porque no hay ningún elemento. Hay que verlo funcionando
- Prueba de esfuerzo: contrastar contra un modelo FIFO propio **sin módulo**
  durante 40 vueltas, comparando valor por valor y tamaño. Si divergen, el
  módulo está mal

**CÓMO SE VERIFICA**
El ciclo completo: llenar 5, fallar el sexto, vaciar, y volver a llenar 5 con los
mismos lugares. Eso es lo que la cola circular resuelve y lo que el ejercicio 9
rompía.