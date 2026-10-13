# Plan de resolución — Guía 5: Listas enlazadas simples

> Documento de **plan técnico**. No es código.
> Consumido por el agente `Programmer` (`.github/agents/programmer.agent.md`).
> Fuente: `Ejercicios5.docx` (consigna general + 10 ejercicios).

---

## 1. Objetivo

Implementar desde cero una lista enlazada simple en Java, primero para enteros y
luego en forma genérica. La guía trabaja con nodos conectados por referencias,
`head`, recorrido secuencial y actualización cuidadosa de enlaces.

La progresión va de las operaciones básicas a las mutaciones más delicadas:

1. Construir la estructura y agregar nodos.
2. Buscar y obtener por posición mediante recorrido.
3. Insertar y eliminar, preservando enlaces y tamaño.
4. Modificar datos, contar repeticiones e invertir enlaces.
5. Generalizar la estructura a `ListaEnlazada<T>`.

---

## 2. Estado actual

| Ruta | Estado |
|---|---|
| `Guia 5 - Listas Enlazadas simple/Ejercicios5.docx` | Enunciado fuente |
| `Ejercicio1.java` … `Ejercicio10.java` | No existen |
| `_build_check.ps1`, `.classpath`, `.vscode/launch.json` | No existen |
| `PLAN_GUIA_5.md`, `PROMPT_AGENTE_PROGRAMADOR_5.md` | Se crean con este plan |

La carpeta contiene el DOCX; aún no hay implementaciones ni configuración Java.

---

## 3. Requisitos explícitos de la consigna

### Objetivo general

Para cada ejercicio, el alumno debe escribir un prompt inicial que explique:

- El problema y el razonamiento esperado.
- Los casos que hay que contemplar.
- La estructura de clases considerada necesaria.

El prompt debe demostrar comprensión de nodos, referencias, `head`, recorrido
secuencial, inserción, eliminación y actualización de enlaces. El prompt inicial
y los ajustes posteriores deben quedar como comentarios dentro de cada `.java`:

```text
Prompt inicial utilizado:
...
Ajustes realizados luego de la primera respuesta de OpenCode:
...
```

El código debe poder probarse con datos de ejemplo. El objetivo no es sólo que
funcione, sino que el razonamiento reflejado en el prompt sea correcto.

### Restricción principal

La lista se implementa desde cero. No usar `ArrayList`, `LinkedList` ni otras
colecciones que resuelvan la estructura. El Ejercicio 10 también debe seguir
siendo una lista enlazada propia, ahora con `Nodo<T>` y `ListaEnlazada<T>`.

---

## 4. Modelo e invariantes de la estructura

### Nodo y lista

Cada nodo guarda un dato y una referencia al siguiente:

```text
Nodo: dato | siguiente -> otro nodo o null
Lista: head -> primero -> ... -> último -> null
```

- `head == null` si y sólo si la lista está vacía.
- `size` coincide con la cantidad de nodos alcanzables desde `head`.
- El último nodo tiene `siguiente == null`.
- La lista es simplemente enlazada: sólo se avanza en dirección `siguiente`.
- No hay acceso aleatorio O(1): una posición se alcanza recorriendo desde `head`.

### Reglas para mutaciones

- Insertar al inicio: el nuevo nodo apunta al head anterior y luego se actualiza
  `head`.
- Insertar después de un nodo `actual`: primero
  `nuevo.siguiente = actual.siguiente`, luego `actual.siguiente = nuevo`.
  Invertir ese orden puede perder el resto de la lista.
- Eliminar el primero: adelantar `head` a `head.siguiente`.
- Eliminar un nodo interior/final: conectar el anterior con el siguiente del
  eliminado (`anterior.siguiente = actual.siguiente`).
- Invertir: guardar `siguiente` antes de cambiar el enlace actual; mantener
  `anterior`, `actual` y `siguiente`; al final, `head = anterior`.
- Cada inserción/eliminación exitosa ajusta `size` exactamente una vez. Buscar,
  obtener, modificar, contar e invertir no alteran `size`.

---

## 5. Decisiones de contrato para casos inválidos

El enunciado pide validar posiciones, pero no prescribe una excepción ni un
valor de retorno para los métodos `void`. Para que el comportamiento sea
uniforme, seguro y comprobable:

- `obtener(posicion)` devuelve un centinela documentado (por ejemplo `-1`) y
  muestra un mensaje si la posición no existe. El valor centinela es ambiguo si
  `-1` también puede ser un dato válido; se documenta esta limitación. No se
  lanza una excepción.
- `insertarEnPosicion(dato, posicion)` acepta el intervalo `[0, size]`; fuera de
  él informa y no modifica la lista. Insertar en `size` equivale a agregar al
  final.
- `eliminarEnPosicion(posicion)` y `modificar(posicion, nuevoDato)` aceptan
  `[0, size - 1]`; fuera del intervalo informan y no modifican la lista.
- `eliminar(dato)` devuelve `false` si no encuentra el dato; si hay repetidos,
  elimina sólo la primera aparición.

Se puede elegir otra política si la cátedra o el enunciado completo la exige,
pero debe quedar explícita en el prompt y ser consistente en el programa.

---

## 6. Especificación por ejercicio

### E1 — Crear una lista enlazada simple desde cero

- `Nodo` interno con dato `int` y referencia `siguiente`.
- `ListaEnlazadaEnteros` con `head` y `size`.
- Operaciones exactas: `insertarAlInicio(int)`, `insertarAlFinal(int)`,
  `imprimir()`, `estaVacia()`, `getSize()`.
- Probar lista vacía, inserciones al inicio y al final, y tamaño tras cada
  mutación.
- Explicar representación del nodo, función de `head` y mantenimiento de
  `size`.

### E2 — Buscar elementos

- Agregar `boolean buscar(int dato)` a una lista de enteros.
- Recorrer desde `head` hasta hallar coincidencia o llegar a `null`.
- Probar dato presente al inicio/medio/final, ausente y lista vacía.
- Explicar por qué no hay acceso directo por índice y buscar cuesta O(n).

### E3 — Obtener un elemento por posición

- Agregar `int obtener(int posicion)`.
- Validar primero `posicion < 0` y `posicion >= size`.
- Posición válida: recorrer nodo por nodo desde `head`; no tratar la lista como
  un arreglo.
- Probar primero, medio, último, lista vacía y posiciones inválidas.
- Aplicar y documentar la política de centinela elegida para posición inválida.

### E4 — Insertar un nodo en una posición

- Agregar `void insertarEnPosicion(int dato, int posicion)`.
- Aceptar posiciones de `0` a `size`, inclusive.
- Caso `0`: insertar al inicio. Caso `size`: insertar al final. Caso intermedio:
  localizar el anterior y enlazar el nuevo entre anterior y su sucesor.
- Orden obligatorio: `nuevo.siguiente = actual.siguiente`, luego
  `actual.siguiente = nuevo`.
- Probar lista vacía, inicio, medio, final e índices inválidos.
- Explicar cómo el orden contrario puede desconectar el resto de la lista.

### E5 — Eliminar un nodo por valor

- Agregar `boolean eliminar(int dato)`; elimina sólo la primera coincidencia.
- Contemplar lista vacía, head, nodo intermedio, último, dato ausente y
  repetidos.
- Actualizar `head` en el caso inicial; para los demás, enlazar el anterior con
  el sucesor del nodo eliminado.
- Decrementar `size` sólo cuando se elimina.
- Explicar que Java no requiere liberar manualmente el nodo: al quedar
  inaccesible desde las referencias vivas, podrá recolectarlo el Garbage
  Collector.

### E6 — Eliminar un nodo por posición

- Agregar `void eliminarEnPosicion(int posicion)`.
- Validar `0 <= posicion < size` antes de recorrer.
- Si es cero, avanzar `head`; si no, recorrer hasta el nodo anterior y enlazarlo
  al siguiente del nodo eliminado.
- Actualizar `size` sólo ante eliminación exitosa.
- Probar primera, intermedia, última, lista vacía e índices inválidos.
- Explicar por qué se necesita el nodo anterior en una lista simplemente
  enlazada.

### E7 — Modificar un elemento

- Agregar `void modificar(int posicion, int nuevoDato)`.
- Validar antes del recorrido; llegar secuencialmente al nodo y actualizar sólo
  su dato.
- No modificar la referencia `siguiente` ni el `size`.
- Probar primero, medio, último e índice inválido.
- Contrastar explícitamente cambiar el dato con cambiar enlaces.

### E8 — Contar ocurrencias

- Agregar `int contarOcurrencias(int dato)`.
- Recorrer la lista completa, incrementando el contador ante cada coincidencia.
- Probar ningún resultado, uno y varios resultados; incluir el ejemplo
  `10 -> 20 -> 10 -> 30 -> 10 -> null`, que debe dar 3 para `10`.
- Explicar por qué no se detiene en la primera coincidencia.

### E9 — Invertir la lista

- Agregar `void invertir()` iterativo y O(n), sin crear nodos nuevos.
- Mantener `anterior`, `actual` y `siguiente`.
- Guardar `siguiente` antes de apuntar el enlace de `actual` hacia `anterior`;
  avanzar ambos punteros y asignar al final `head = anterior`.
- Probar lista vacía, un nodo y varios nodos:
  `10 -> 20 -> 30 -> 40 -> null` pasa a
  `40 -> 30 -> 20 -> 10 -> null`.
- Confirmar que `size` se mantiene.
- Explicar qué pérdida ocurriría si se modifica el enlace antes de guardar el
  siguiente nodo.

### E10 — Lista enlazada genérica

- Generalizar a `ListaEnlazada<T>` y `Nodo<T>`; no duplicar una implementación
  por tipo.
- Mantener genéricas las operaciones de recorrido, inserción y eliminación.
- Probar `ListaEnlazada<Integer>`, `ListaEnlazada<String>` y
  `ListaEnlazada<Alumno>` con un `record` o clase simple `Alumno`.
- No usar `Object[]` ni casts: los genéricos se aplican directamente a los
  nodos y referencias.
- Explicar qué cambia (tipo del dato/nodo y clase parametrizada) y qué no (la
  lógica de enlaces y recorridos).
- No usar `==` para comparar valores genéricos. Elegir y documentar la semántica
  de igualdad; para `buscar`/`eliminar`, usar `Objects.equals(a, b)` de
  `java.util.Objects` (esto no es una colección y evita fallos con `null`).
  Si se prohíbe también esa utilidad, usar comparación null-safe explícita.

---

## 7. Pruebas y demostraciones

No hay Maven, Gradle ni JUnit. Cada archivo debe poder compilarse y ejecutarse
independientemente. Su `main` hará demostraciones verificables que impriman
operaciones, resultado y representación actual de la lista.

Cobertura mínima por ejercicio:

- Caso normal con varias posiciones.
- Lista vacía cuando la operación lo permita.
- Límites: primero/último, posición inválida, repetidos o ausencia, según el
  ejercicio.
- Verificación visible de que el orden de nodos y `size` siguen correctos.

En E9 verificar además el orden inverso y el tamaño sin cambios. En E10 probar
al menos tres tipos distintos. Ningún caso límite debe terminar en una
excepción no controlada.

---

## 8. Entregables y configuración

- `Ejercicio1.java` … `Ejercicio10.java`, cada uno con su propio `main`.
- En cada fuente: comentario inicial con el prompt adaptado al ejercicio y
  bloque final `Ajustes realizados luego de la primera respuesta de OpenCode`.
- `_build_check.ps1`: compilar los diez archivos con UTF-8 y ejecutar cada uno
  con entrada determinista/EOF; informar claramente fallas de compilación o
  ejecución. No depender de JUnit ni agregar build system.
- `.classpath` con un único elemento raíz `<classpath>`, source `.` y output
  `bin`, para que el Java Language Server de VS Code compile y ejecute.
- `.vscode/launch.json`: diez configuraciones de Java, `mainClass` correcto y
  `cwd` `${fileDirname}`.
- No tocar ejercicios ni configuraciones de las otras guías.

---

## 9. Verificación final

1. Compilar: `javac -encoding UTF-8 Ejercicio*.java`.
2. Ejecutar el `_build_check.ps1` de esta guía; confirmar que los 10 procesos
   terminan con código 0 y no hay excepciones no controladas.
3. Probar la compilación del lenguaje Java hacia `bin/` (como el botón Run):
   `javac -encoding UTF-8 -d bin Ejercicio*.java`.
4. Revisar los diez prompts embebidos y los diez bloques de ajustes; cada prompt
   describe la implementación real.
5. Revisar referencias y tamaños tras mutaciones, especialmente los casos de
   inserción/eliminación en head y la inversión.

---

## 10. Riesgos a evitar

- Confundir una lista enlazada con un arreglo: no hay indexado aleatorio.
- Perder la cadena al insertar/invertir por actualizar enlaces en mal orden.
- Olvidar actualizar `head` al insertar/eliminar el primer nodo.
- Desincronizar `size` respecto de los nodos alcanzables.
- En E5, eliminar más de una coincidencia cuando se pide la primera.
- En E9, sobrescribir `actual.siguiente` antes de guardar el sucesor.
- En E10, comparar referencias con `==` para buscar datos por valor, o hacer
  casts innecesarios.
- No inventar semántica de errores en silencio: declararla en el prompt y
  aplicarla igual en todos los ejercicios.