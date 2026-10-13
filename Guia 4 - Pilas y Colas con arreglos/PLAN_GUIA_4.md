# Plan de resolución — Guía 4: Pilas y Colas con Arreglos

> Documento de **plan técnico**. No es código.
> Consumido por el agente `Programmer` (`.github/agents/programmer.agent.md`).
> Fuente: `Ejercicios4.docx` (10 ejercicios + objetivo general + cierre).

---

## 1. Objetivo

Implementar estructuras de datos lineales (pilas y colas) sobre arreglos fijos,
resolviendo 10 ejercicios que van desde la implementación de la estructura hasta
sus aplicaciones.

---

## 2. Estado actual

| Ruta | Estado |
|------|--------|
| `Guia 4 - Pilas y Colas con arreglos/Ejercicios4.docx` | Enunciados (único archivo) |
| `Ejercicio4_1.java` … `Ejercicio4_10.java` | **No existen** |
| `_build_check.ps1`, `.classpath`, `.vscode/launch.json` | **No existen** |

No hay código, ni build, ni configuración de VS Code.

---

## 3. Requisitos de la consigna

### Objetivo general — el prompt debe responder 5 cosas

1. Qué estructura se necesita.
2. Qué operaciones debe tener.
3. Qué casos límite deben contemplarse.
4. Cómo se maneja el arreglo interno.
5. Qué comportamiento se espera obtener.

> *"La calidad del código generado dependerá directamente de la calidad del
> razonamiento planteado en el prompt."*

### Cierre — qué hay que entregar en cada ejercicio

- El código Java generado.
- **Un comentario al inicio del archivo con el prompt inicial usado.**
- **Una breve explicación de si hubo que ajustar el prompt.**
- **Una prueba de ejecución con datos de ejemplo.**

Esto confirma la decisión que ya tomamos en las Guías 2 y 3: **el prompt va
embebido dentro del `.java`**, con su bloque de ajustes. Acá el enunciado lo pide
de forma explícita, así que deja de ser una interpretación nuestra.

---

## 4. Qué cambia respecto de las guías anteriores

| | Guías 1–3 | **Guía 4** |
|---|---|---|
| Unidad de trabajo | un algoritmo por archivo | **una estructura de datos reutilizable** |
| Datos | `int[]`, `String[]`, objetos | **`T` genérico** |
| foco | complejidad y contadores | **invariantes y estados** (llena/vacía) |
| Salida típica | tablas comparativas | **trazas del estado interno** |

**Novedad clave:** el Ejercicio 5 pide una clase `Pila<T>` **genérica**. Eso obliga
a obtener `Object[]` internamente y hacer *casting* en `pop`/`peek`. Es el
único ejercicio de las 4 guías con genéricos, y hay que documentarlo bien.

---

## 5. Conceptos centrales de la guía

### 5.1 La pila — LIFO

```
push(x)  ->  [ ][ ][x]        el nuevo elemento queda arriba
pop()    ->  [ ][ ][ ]        sale el último en entrar
peek()   ->  [ ][ ][x]        consulta sin quitar
```

- Índice `top`: cantidad de elementos **ocupados** (no la posición del tope).
- **Valor inicial de `top`: 0.** Es el error clásico: si se inicializa en
  `-1`, todas las comparaciones quedan invertidas.
- Vacía ⟺ `top == 0`. Llena ⟺ `top == capacidad`.
- **No se debe usar `ArrayDeque` ni `Stack`**: el enunciado pide arreglos.

### 5.2 La cola — FIFO

```
enqueue(x) ->  [x][ ][ ]      entra por la rear
dequeue()  ->  [ ][ ][ ]      sale por el front
front()    ->  [x][ ][ ]      consulta el primero
```

- Dos índices: `front` (el primero) y `rear` (**el siguiente espacio libre**).
- Encolar: se escribe en `rear`, luego `rear++`.
- Desencolar: se lee `front`, luego `front++`.
- Vacía ⟺ `front == rear`. Llena ⟺ `front == 0 && rear == capacidad`.

### 5.3 ⚠️ El problema del Ejercicio 9 — y su solución en el 10

**Este es el corazón de la guía.** Con dos índices que sólo avanzan:

```
enqueue(1) enqueue(2) enqueue(3) enqueue(4)     → [1][2][3][4]  rear=4
dequeue() dequeue()                            → [_][_][3][4]  front=2
```

Los espacios `0` y `1` están **libres pero no reutilizables**: `rear` ya llegó al
final del arreglo y no puede volver atrás. Con 10 operaciones la cola queda
llena aunque sólo haya 2 elementos.

**Solución del Ejercicio 10 — cola circular.** Los índices "dan la vuelta":

```java
rear = (rear + 1) % capacidad;
front = (front + 1) % capacidad;
```

Con el módulo, la posición 9 + 1 se convierte en 0, y el espacio liberado se
reutiliza.

**Detección de llena/vacía con el módulo** — hay dos convenciones:

| Convención | Llena | Vacía |
|---|---|---|
| **A: contador** | `contador == capacidad` | `contador == 0` |
| **B: una posición libre** | `rear == front` (llena) | `(rear + 1) % cap == front` (vacía) |

**Decisión: usar el contador (A).** Es más claro, no tiene el caso ambiguo de la
convención B, y el contador sirve además para `size()`. El enunciado del 10 pide
explicar *"cómo se detecta si la cola está llena o vacía"*, y con el contador la
respuesta es directa.

> Nota: con la convención B hay que reservar una posición (capacidad efectiva
> `n - 1`). Conviene **mencionarlo** en el Javadoc como alternativa descartada.

---

## 6. Especificación por ejercicio

### E1 — Pila de enteros

| Campo | Detalle |
|---|---|
| Clase | `PilaEnteros` con arreglo de tamaño **fijo** |
| Operaciones | `push`, `pop`, `peek`, `isEmpty`, `isFull`, `size` |
| `top` | Índice de cantidad de elementos ocupados, **inicializado en 0** |
| Al_macenar_llegar | `pop` y `peek` sobre pila vacía → **mensaje**, no excepción |
|push_sobre_llena | `push` cuando `isFull()` → **mensaje**, no excepción |
| Complejidad | Todas las operaciones **Θ(1)** |

**Justificación que debe ir en el prompt:** por qué `top` es un *contador* y no la
posición del tope. Si `top` fuera la posición, el vacío sería `-1` y el lleno
`capacidad-1`, lo que obliga a castear en cada acceso.

### E2 — Simulador de torre de platos

| Campo | Detalle |
|---|---|
| Datos | Platos identificados por número o nombre |
| Operaciones | agregar (`push`), retirar el superior (`pop`), consultar el superior (`peek`) |
| **Por qué pila y no cola** | Sólo se puede tomar el **último plato agregado**. Con una cola habría que sacar el primero, que es el de abajo, y no se podría alcanzar el de arriba. Es LIFO por la naturaleza del problema. |
| Trace | Mostrar la torre después de cada operación, de abajo hacia arriba |

### E3 — Validar paréntesis balanceados

| Campo | Detalle |
|---|---|
| Entrada | Expresión matemática como `String` |
| Ejemplo válido | `(5 + 3) * (2 + 1)` |
| Ejemplo inválido | `(5 + 3)) * (2 + 1` |
| Algoritmo | `push` al abrir, `pop` al cerrar, **`pop` sobre vacía → inválido** |
| **Detalle clave** | Al terminar, la pila debe estar **vacía**; si quedan abiertos, es inválido |
| Sintaxis | `switch` sobre el carácter, **no `if` encadenados** |

> El enunciado dice "expresión matemática", así que se **`pop`ea en cada `)`**
> sin comprobar el tipo de operador. Si se quisiera validar también corchetes y
> llaves, sería una extensión; queda fuera de alcance y así se documenta.

### E4 — Historial de navegación

| Campo | Detalle |
|---|---|
| Datos | URLs ( Strings) |
| Operaciones | visitar (`push`), volver atrás (`pop`), consultar la actual |
| **Por qué LIFO** | La página visitada más recientemente es la que se abandona primero. El "atrás" del navegador es una pila. |
| Detalle a documentar | En un navegador real el historial **no se pop**ea: se apila y se consulta por índice. Se menciona como simplificación |

### E5 — Pila genérica `Pila<T>`

| Campo | Detalle |
|---|---|
| Clase | `Pila<T>` con arreglo `Object[]` |
| **Qué resuelve** | Permite `Pila<Integer>`, `Pila<String>`, `Pila<Jugador>` con **una sola implementación** |
| **Casting obligatorio** | Al hacer `pop`, el arreglo devuelve `Object`; hay que castear a `T`. Es el precio de los genéricos en Java |
| Por qué no `int[]` | Un `int[]` obliga a escribir una clase distinta por cada tipo |
| Operación | `push`, `pop`, `peek`, `isEmpty`, `isFull`, `size` |

> **Alternativa a considerar y descartar:** usar `ArrayList<T>` internamente. La
> cátedra pidió arreglos, así que se usa `Object[]`, y el motivo queda escrito.

### E6 — Cola de enteros

| Campo | Detalle |
|---|---|
| Clase | `ColaEnteros` con arreglo |
| Operaciones | `enqueue`, `dequeue`, `front`, `isEmpty`, `isFull`, `size` |
| `front` | Índice del **primer** elemento |
| `rear` | Índice del **próximo espacio libre** (no del último elemento) |
| Vacía | `front == rear` |
| Llena | `front == 0 && rear == capacidad` |
| Evolución | `rear++` al encolar, `front++` al desencolar |

### E7 — Sistema de turnos

| Campo | Detalle |
|---|---|
| Datos | Personas con nombre (y opcionalmente un número de turno) |
| Operaciones | agregar (`enqueue`), atender siguiente (`dequeue`), consultar primero (`front`) |
| **Por qué FIFO y no LIFO** | El orden de llegada ES el orden de atención. Servir al último que llegó sería injusto. |
| Decisión a documentar | Si se asigna número de turno o se usa el orden de llegada. Ambas son válidas; se elige una y se explica |

### E8 — Cola de impresión

| Campo | Detalle |
|---|---|
| Datos | Documentos con `nombre` y `paginas` |
| Orden | "en el mismo orden en que fueron agregados" → **FIFO estricto** |
| Operaciones | agregar documento, imprimir el primero, consultar el primero |
| Detalle | Al imprimir, mostrar cuántas páginas procesa y actualizar un total acumulado |

> Ojo con la palabra "páginas": en el `.docx` aparece "páginas" mal codificado
> como "p�ginas". Hay que escribirlo correctamente.

### E9 — Desperdicio de espacio en cola simple

| Campo | Detalle |
|---|---|
| Objetivo | **Demostrar** el problema, no resolverlo |
| Requisito | Mostrar el arreglo tras varias `enqueue` y `dequeue`, **con los espacios libres visibles** (por ejemplo `[_]` o `.`) |
| **El hallazgo a probar** | Tras encolar N y desencolar k, quedan k espacios **libres pero inservibles**: `front` avanzó y `rear` no puede retroceder |
| Consecuencia | Con un arreglo de 5, hacer 3 enqueues y 3 dequeues deja la cola vacía pero **sin espacio para reencolar** |

**Éste es el ejercicio que justifica el 10.** El prompt debe pedir explícitamente
el estado del arreglo con los huecos marcados, y el comentario debe anticipar que
el 10 lo resuelve.

### E10 — Cola circular

| Campo | Detalle |
|---|---|
| Objetivo | Reutilizar los espacios liberados al inicio |
| Operador módulo | `rear = (rear + 1) % capacidad`, `front = (front + 1) % capacidad` |
| Detección | Con **contador**: `contador == capacidad` (llena), `contador == 0` (vacía) |
| Demostración clave | Repetir la **misma** secuencia del E9 y mostrar que ahora **sí** hay espacio |
| Trace | Mostrar el arreglo con los índices `front` y `rear` señalados |

**Justificación del módulo:** es una **división entera**: al llegar al final del
arreglo, la posición `capacidad` se transforma en `0`. Así el índice "rebota" y
el espacio liberado al principio vuelve a estar disponible.

---

## 7. Matriz de trazabilidad

| # | Estructura | Reduce | Casos límite obligatorios | Θ |
|---|---|---|---|---|
| 1 | `PilaEnteros` | — | push llena, pop vacía, peek vacía | Θ(1) |
| 2 | pila | — | torre vacía al retirar | Θ(1) |
| 3 | pila | expresión | `)` sin `(` , paréntesis sin cerrar | Θ(n) |
| 4 | pila | — | historial sin páginas anteriores | Θ(1) |
| 5 | `Pila<T>` | — | tipos heterogéneos, pila vacía | Θ(1) |
| 6 | `ColaEnteros` | — | enqueue llena, dequeue vacía | Θ(1) |
| 7 | cola | — | atender sin nadie en la cola | Θ(1) |
| 8 | cola | — | impresión sin documentos | Θ(1) |
| 9 | cola simple | — | **espacios libres inservibles** | Θ(1) |
| 10 | cola circular | — | **reutilización** de espacios | Θ(1) |

---

## 8. Casos límite transversales

1. **Operación sobre estructura vacía** → mensaje claro, **nunca excepción**.
   Es la diferencia entre una pila bien implementada y una que revienta.
2. **Operación sobre estructura llena** → mensaje, no excepción ni desbordamiento.
3. **Capacidad 0 o 1** → documentar el comportamiento.
4. **`top` debe empezar en 0**, no en -1 (ver §5.1).
5. **Cast en `pop`/`peek` de `Pila<T>`** → si se castea mal, `ClassCastException`.
6. **El cast en `pop`/`peek` de `Pila<T>`** genera un warning que debe quedar
   documentado, porque es el precio inevitable de usar `Object[]` internamente.
7. **Sin usar `ArrayDeque`, `Stack`, `LinkedList` ni `ArrayList`** — el enunciado
   pide arreglos.

---

## 9. Riesgos

| Riesgo | Mitigación |
|--------|------------|
| Inicializar `top` en -1 | Exigido explícitamente por el enunciado ("qué valor inicial debe tener") |
| `rear` como "último elemento" en vez de "próximo libre" | Documentar la diferencia; el error desplaza todos los índices en 1 |
| Usar `ArrayDeque` en lugar de arreglos | Prohibición explícita en el prompt |
| El E9 se resuelve por accidente y no demuestra el problema | Pedir el trace **con huecos marcados** |
| El casting de `Pila<T>` rompe en runtime | Probar con `Integer`, `String` y un objeto |
| Confundir LIFO con FIFO en E2/E4 vs E7/E8 | La justificación va **en el prompt**, es lo que evalúa la cátedra |
| Olvidar el bloque de ajustes del prompt | Requisito explícito del "cierre del práctico" |

---

## 10. Criterios de aceptación

- [ ] Existen los 10 archivos `Ejercicio4_1.java` … `Ejercicio4_10.java`.
- [ ] Compilan con `javac -encoding UTF-8`.
- [ ] **Cada archivo empieza con `PROMPT INICIAL`** con las **5 secciones** del
      objetivo general.
- [ ] **Cada archivo termina con `AJUSTES DEL PROMPT`** — el enunciado lo pide
      explícitamente en el "cierre del práctico".
- [ ] Cada archivo tiene **prueba de ejecución con datos de ejemplo**.
- [ ] Ninguna operación sobre estructura vacía o llena lanza excepción.
- [ ] `top` se inicializa en **0** y se explica por qué.
- [ ] `Pila<T>` funciona con `Integer`, `String` y un objeto.
- [ ] E9 **muestra los espacios libres inservibles** con el arreglo visible.
- [ ] E10 **reutiliza** esos espacios y lo demuestra.
- [ ] No se usa `ArrayDeque`, `Stack`, `LinkedList` ni `ArrayList`.
- [ ] Encoding UTF-8 en la salida.

---

## 11. Configuración de VS Code (no existe todavía)

Hay que crear, igual que en las Guías 1–3:

| Archivo | Para qué |
|---|---|
| `_build_check.ps1` | compilar y ejecutar los 10, con `-Clean` y `-NoRun` |
| `.classpath` | para que el Java Language Server reconozca la carpeta |
| `.vscode/launch.json` | 10 configuraciones, una por ejercicio |

> **Importante:** el `launch.json` va **dentro de la carpeta de la guía**, no en la
> raíz. Un `launch.json` en la raíz con 40 configuraciones sería ambiguo: las 4
> guías tienen un `Ejercicio4_1` en default package, y el depurador no sabría
> cuál ejecutar. Es el error que se cometió en la Guía 3.

---

## 12. Ruta de verificación

```powershell
cd "c:\Facu Things\2026\Estructura de Datos y Algoritmo\Guias de Practico\Guia 4 - Pilas y Colas con arreglos"
powershell -ExecutionPolicy Bypass -File .\_build_check.ps1
```

Comprobaciones que el script debe dejar visibles:

| Ejercicio | Verificación |
|---|---|
| E1 | `push` sobre pila llena informa, no revienta |
| E3 | `(5 + 3) * (2 + 1)` → válido · `(5 + 3)) * (2 + 1` → inválido |
| E5 | La misma `Pila<T>` usada con `Integer`, `String` y `Jugador` |
| E6 | `front` y `rear` evolucionan correctamente |
| E9 | Se ven los huecos inservibles |
| E10 | Los mismos casos del E9 **ahora reutilizan** el espacio |