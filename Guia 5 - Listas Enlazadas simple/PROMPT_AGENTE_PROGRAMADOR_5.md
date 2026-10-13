# PROMPT MAESTRO — Agente Programador · Guía 5

> **Uso:** copiá el bloque de abajo y pasalo al agente `Programmer`.
> Plan técnico: `PLAN_GUIA_5.md` (mismo directorio).

---

```text
# TAREA — Resolver los 10 ejercicios de "Listas Enlazadas Simples" (Guía 5)

## 0. ROL Y ALCANCE

Sos el PROGRAMADOR del proyecto. Implementá los diez ejercicios en Java 21,
individualmente ejecutables, siguiendo el plan `PLAN_GUIA_5.md` y el enunciado
`Ejercicios5.docx` de esta misma carpeta.

Leé ambos documentos antes de tocar código. Si el plan y el enunciado se
contradicen, prevalece el enunciado y reportás la discrepancia. No modifiques
ninguna de las Guías 1–4.

La carpeta actualmente contiene el DOCX y los documentos de planificación; los
diez fuentes Java y la configuración de esta guía todavía deben crearse.

## 1. CONSIGNA Y ENTREGABLES

Para cada ejercicio:
- Implementar el código Java solicitado.
- Incluir al principio un comentario `Prompt inicial utilizado:` que explique
  problema, razonamiento, casos límite y clases/estructura necesarias.
- Cerrar el archivo con `Ajustes realizados luego de la primera respuesta de
  OpenCode:` y explicar qué se ajustó y por qué. Si no hubo cambios, declararlo.
- Incluir una demostración ejecutable con datos de ejemplo.

La calidad del razonamiento del prompt es parte de la entrega. Los prompts deben
explicar los nodos, enlaces, `head`, recorridos y actualizaciones pertinentes al
ejercicio; no uses una plantilla vacía repetida.

## 2. CONTEXTO DEL PROYECTO

- Carpeta: `Guia 5 - Listas Enlazadas simple`.
- JDK 21.0.10 LTS en PATH. Usá `javac` y el default package.
- No hay Maven, Gradle ni JUnit: no agregues build system ni dependencias.
- El workspace está en Windows PowerShell. Tenés terminal: compilá y ejecutá
  las pruebas; no delegues esa verificación al usuario.
- No cambies archivos de otras guías ni limpies cambios del usuario.

## 3. REGLAS DE IMPLEMENTACIÓN

### La estructura se implementa desde cero

Cada lista de enteros usa nodos propios: un nodo guarda `int dato` y una
referencia al siguiente nodo. La lista mantiene `head` y `size`.

Para E10, la estructura propia se generaliza como `Nodo<T>` y
`ListaEnlazada<T>`.

PROHIBIDO usar colecciones de Java para implementar o simular la lista:
`ArrayList`, `LinkedList`, `Vector`, `Stack`, `Queue`, `Deque` u otras
colecciones equivalentes. No reemplaces nodos por un arreglo. No uses streams ni
lambdas. `Scanner` para entrada y `Objects.equals` en E10 están permitidos; no
son implementaciones de la lista.

### Invariantes obligatorias

- Lista vacía si y sólo si `head == null`.
- `size` siempre coincide con los nodos alcanzables desde `head`.
- El último nodo apunta a `null`.
- Toda mutación conserva los nodos no afectados y el orden relativo restante.
- Sólo inserciones/eliminaciones exitosas cambian `size`.
- Buscar, obtener, modificar, contar e invertir no agregan ni quitan nodos.
- La lista es simplemente enlazada: no recorrer hacia atrás ni fingir acceso O(1)
  por índice.

### Posiciones inválidas y fallas

La consigna pide validar, pero no define excepciones ni centinelas. Seguí la
política del plan y declarala en el prompt:
- `obtener` informa y devuelve `-1` para índices inválidos; no lanza excepción.
- `insertarEnPosicion` permite `0..size` inclusive; fuera de rango informa y no
  modifica nada.
- `eliminarEnPosicion` y `modificar` permiten `0..size-1`; fuera de rango
  informan y no modifican nada.
- `eliminar(dato)` borra sólo la primera aparición y devuelve `false` si no está.

Documentá que el centinela `-1` de `obtener` también podría ser un dato válido;
se usa porque la firma del ejercicio retorna `int` y no especifica otra política.
No uses excepciones como flujo normal para casos inválidos.

## 4. FORMATO DE CADA ARCHIVO

Cada archivo debe ser autocontenido, compilarse con `javac EjercicioN.java` y
poder ejecutarse con `java EjercicioN`.

```java
/*
Prompt inicial utilizado:
[Prompt específico, con razonamiento y casos de prueba.]
*/

/** GUÍA 5 — LISTAS ENLAZADAS SIMPLES — Ejercicio N: título */
public class EjercicioN {
    // Nodo/lista propia y operaciones del ejercicio
    public static void main(String[] args) {
        // Demostración reproducible de operaciones y casos límite
    }
}

/*
Ajustes realizados luego de la primera respuesta de OpenCode:
- [Cambio y motivo, o declarar que no fueron necesarios.]
*/
```

No uses `package`. Cada ejercicio puede definir internamente una clase propia
`ListaEnlazadaEnteros` y `Nodo`; E10 implementa el tipo genérico. No es necesario
forzar una única clase compartida entre los diez ejecutables: priorizá que cada
archivo se entienda y ejecute independientemente.

## 5. LOS 10 EJERCICIOS

### E1 — Crear una lista enlazada simple desde cero

Implementá `insertarAlInicio(int dato)`, `insertarAlFinal(int dato)`, `imprimir()`,
`estaVacia()` y `getSize()`.

El prompt explica qué guarda el nodo, qué significa `head`, cómo se enlaza el
nuevo primer/último nodo y cuándo cambia `size`. Demostrá inserciones al inicio y
al final, lista vacía y tamaños correctos.

### E2 — Buscar elementos

Implementá `boolean buscar(int dato)`: recorrer desde `head` hasta hallar el
dato o llegar a `null`. Demostrá lista vacía, coincidencia al principio/medio/
final y valor ausente. Explicá que no existe acceso directo por índice y que la
búsqueda es O(n).

### E3 — Obtener un elemento por posición

Implementá `int obtener(int posicion)`. Validá `posicion < 0` y
`posicion >= size`; después, para una posición válida, avanzá nodo por nodo.
Demostrá primera, intermedia, última y posiciones inválidas. Usá la política
centinela definida en §3.

### E4 — Insertar un nodo en una posición

Implementá `void insertarEnPosicion(int dato, int posicion)` aceptando el
intervalo `[0, size]`.
- `0`: actualizar head.
- `size`: insertar al final.
- Intermedia: encontrar el nodo anterior y actualizar en este orden:
  `nuevo.siguiente = actual.siguiente;`
  `actual.siguiente = nuevo;`

Explicá qué se perdería si se conecta primero `actual` con `nuevo` sin guardar
el sucesor. Demostrá lista vacía, inicio, medio, final e índices inválidos.

### E5 — Eliminar un nodo por valor

Implementá `boolean eliminar(int dato)`, borrando sólo la primera aparición.
Contemplá lista vacía, eliminar `head`, medio, último, dato ausente y duplicados.
Ajustá `head` o el enlace anterior y decrementá `size` sólo al eliminar.
Explicá que Java no borra manualmente el nodo: al quedar inaccesible desde la
lista podrá recolectarlo el Garbage Collector.

### E6 — Eliminar un nodo por posición

Implementá `void eliminarEnPosicion(int posicion)`. Validá antes de recorrer.
Para posición 0, adelantá `head`; en otro caso, encontrá el nodo anterior y
conectalo con el sucesor del nodo eliminado. Actualizá `size` sólo si se elimina.
Demostrá primera, intermedia, última, lista vacía e índices inválidos. Explicá por
qué se necesita el nodo anterior.

### E7 — Modificar un elemento

Implementá `void modificar(int posicion, int nuevoDato)`. Validá antes de
recorrer; modificá el dato del nodo correspondiente sin cambiar `siguiente` ni
`size`. Demostrá posiciones inicial, intermedia, final e inválida. Explicá la
diferencia entre modificar el dato y modificar una referencia.

### E8 — Contar ocurrencias

Implementá `int contarOcurrencias(int dato)`, recorriendo toda la lista. No
termines el recorrido en la primera coincidencia. Probá cero, una y varias
coincidencias, incluyendo `10 -> 20 -> 10 -> 30 -> 10 -> null`: para `10`, el
resultado es `3`.

### E9 — Invertir una lista enlazada simple

Implementá `void invertir()` iterativamente en O(n), sin crear nodos. Usá
`anterior`, `actual` y `siguiente`: guardá el sucesor antes de cambiar el enlace,
redirigí `actual.siguiente` a `anterior`, avanzá y al terminar actualizá `head`.
Demostrá lista vacía, un nodo y
`10 -> 20 -> 30 -> 40 -> null` convirtiéndose en
`40 -> 30 -> 20 -> 10 -> null`. `size` no cambia. Explicá cómo se perdería el
resto de la lista si se modifica el enlace antes de salvar `siguiente`.

### E10 — Convertir a lista genérica

Implementá `Nodo<T>` y `ListaEnlazada<T>` y reutilizá la lógica de nodos y
referencias sin duplicar por tipo. Demostrá:
- `ListaEnlazada<Integer>`
- `ListaEnlazada<String>`
- `ListaEnlazada<Alumno>` con un `record` o clase pequeña `Alumno`

Explicá qué cambia (el tipo del dato y los tipos parametrizados) y qué se
mantiene (recorridos, inserciones, eliminaciones y enlaces). No uses casts ni
`Object[]`. No compares datos con `==`: para buscar/eliminar usá igualdad por
valor null-safe, preferentemente `Objects.equals`.

## 6. DEMOSTRACIÓN Y PRUEBAS

No hay JUnit; cada `main` es una demostración reproducible. Debe mostrar el
resultado de operaciones y la lista, y cubrir:
- Caso normal con más de un nodo.
- Lista vacía cuando aplique.
- Borde inicial/final y errores de posición o valor, según el ejercicio.
- `size` y cadena de nodos antes/después de mutaciones.
- Para E9, orden inverso exacto y tamaño sin cambios.
- Para E10, funcionamiento con los tres tipos indicados.

Ninguna operación inválida debe producir una excepción no controlada. Para
entradas interactivas, protegé cada `nextInt()` con `hasNextInt()`; después de
`nextInt()` consumí el salto de línea antes de leer texto. Preferí demostraciones
sin entrada interactiva para que el build check sea determinista.

## 7. ARCHIVOS DE PROYECTO

Creá en esta guía, y sólo aquí:
- `Ejercicio5_1.java` … `Ejercicio5_10.java`.
- `_build_check.ps1`: compila UTF-8 los diez fuentes y ejecuta cada clase con
  salida de estado/código de salida visible. No ocultes errores, no agregues
  dependencias y usá una estrategia de entrada compatible con PowerShell 5.1 si
  algún main lee stdin.
- `.classpath` con un único elemento raíz `<classpath>`, source `.` y output
  `bin`, necesario para el Java Language Server de VS Code.
- `.vscode/launch.json` con diez configuraciones Java; `mainClass` correcto,
  `cwd: "${fileDirname}"` y consola integrada.

No reemplaces ni edites `PLAN_GUIA_5.md` o este prompt maestro.

## 8. VERIFICACIÓN OBLIGATORIA

Antes de cerrar:
1. Ejecutá `javac -encoding UTF-8 Ejercicio*.java` y corregí errores.
2. Ejecutá `powershell -ExecutionPolicy Bypass -File .\_build_check.ps1` desde
   la carpeta de esta guía. Confirmá compilación y código de salida 0 de los
   diez ejercicios, sin excepciones no controladas.
3. Verificá también `javac -encoding UTF-8 -d bin Ejercicio*.java` para validar
   el classpath del botón Run.
4. Inspeccioná en particular inserción/eliminación en `head`, conservación de
   `size`, posición inválida, duplicados, e inversión sin pérdida de nodos.
5. Revisá que cada archivo tenga su prompt inicial y bloque de ajustes, y que no
   haya colecciones prohibidas, errores de texto ni fragmentos de otro idioma.

Al reportar, enumerá archivos creados, pruebas ejecutadas y cualquier
ambigüedad del enunciado que hayas tenido que decidir.
```