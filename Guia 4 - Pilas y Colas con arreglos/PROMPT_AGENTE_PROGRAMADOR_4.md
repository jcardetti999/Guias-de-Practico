# PROMPT MAESTRO — Agente Programador · Guía 4

> **Uso:** copiá el bloque de abajo y pasalo como argument del agente `Programmer`.
> Plan técnico: `PLAN_GUIA_4.md` (mismo directorio).

---

```text
# TAREA — Resolver los 10 ejercicios de "Pilas y Colas con Arreglos" (Guía 4)

## 0. ROL Y ALCANCE

Sos el PROGRAMADOR del proyecto. Vas a implementar los 10 ejercicios de
estructuras de datos en Java 21.

El plan técnico está en `Guia 4 - Pilas y Colas con arreglos/PLAN_GUIA_4.md`.
Leelo antes de escribir código. Si el plan y el enunciado de `Ejercicios4.docx`
se contradicen, gana el enunciado y lo reportás.

## 1. LO QUE PIDE LA CONSIGNA

### Objetivo general — el prompt debe responder 5 cosas

Para **cada** ejercicio, el prompt tiene que explicar:

1. **Qué estructura** se necesita.
2. **Qué operaciones** debe tener.
3. **Qué casos límite** deben contemplarse.
4. **Cómo se maneja el arreglo interno.**
5. **Qué comportamiento** se espera obtener.

> "La calidad del código generado dependerá directamente de la calidad del
> razonamiento planteado en el prompt."

### Cierre del práctico — qué entregar en cada ejercicio

- El código Java generado.
- **Un comentario al inicio del archivo con el prompt inicial usado.**
- **Una breve explicación de si hubo que ajustar el prompt.**
- **Una prueba de ejecución con datos de ejemplo.**

Las cuatro son obligatorias. El prompt va EMBEBIDO dentro del `.java` y el
archivo cierra con el bloque de ajustes.

## 2. CONTEXTO DEL PROYECTO (verificado — no lo re-descubras)

- Carpeta: `Guia 4 - Pilas y Colas con arreglos`
- **JDK 21.0.10 LTS** en el PATH. Compilá con `javac`.
- **NO hay** sistema de build ni JUnit. **NO los agregues.**
- `Ejercicios4.docx` es el único archivo: los 10 `.java` se crean.
- **NO existen** `_build_check.ps1`, `.classpath` ni `.vscode/launch.json`:
  crealos vos (ver §10).
- Default package, sin declaración `package`.

### Tenés terminal: SÍ

Compilá y ejecutá vos mismo. **No me pidas que corra los tests.** Si algo falla,
lo arreglás antes de cerrar.

## 3. FORMATO OBLIGATORIO DEL PROMPT EMBEBIDO

Cada archivo abre con este bloque, adaptado al ejercicio:

```java
/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java [QUÉ ESTRUCTURA se necesita].
 *
 * OPERACIONES QUE DEBE TENER
 * [Lista de operaciones y qué hace cada una.]
 *
 * CÓMO SE MANEJA EL ARREGLO INTERNO
 * [Qué índices se usan, qué valor inicial tienen, cómo evolucionan.]
 *
 * CASOS LÍMITE A CONTEMPLAR
 * [Lista concreta: estructura vacía, llena, capacidad 1, etc.]
 *
 * COMPORTAMIENTO ESPERADO
 * [Qué debe devolver u observar el programa en cada situación.]
 *
 * Quiero que el código tenga comentarios explicando el índice [top/front/rear],
 * los casos límite y por qué la estructura es LIFO o FIFO.
 *
 * También quiero probarlo con estos casos:
 *   - [CASO 1]
 *   - [CASO 2]
 *   - [CASO 3]
 * ========================================================================== */
```

Y el archivo **termina** con:

```java
/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió tras probar y por qué)
 * ============================================================================
 * - [CAMBIO]: [MOTIVO]
 * ========================================================================== */
```

> Si el prompt no necesitó cambios, el bloque lo dice. **Declararlo es mejor
> que omitirlo.**

## 4. ⚠️ OVERRIDES A TUS REGLAS

Tu definición de agente dice "No agregues comentarios obvios" y "toda
funcionalidad debería tener tests". **En esta tarea ambas se modifican:**

### 4.1 Comentarios — PREVALECE LA CONSIGNA
Está **autorizado y requerido** comentar el índice `top` (o `front`/`rear`), su
valor inicial, los casos límite, el motivo de LIFO o FIFO, y el estado interno de
la estructura. **Prohibido** comentar lo autoevidente.

### 4.2 Tests — NO HAY FRAMEWORK
Sin JUnit. La verificación es un `main` de auto-demostración que:
1. Ejercita **al menos 4 casos**: operación normal, estructura vacía, estructura
   llena, y el caso límite propio del ejercicio.
2. Muestra el **estado interno** (el arreglo) tras cada operación cuando el
   enunciado lo pide.
3. Imprime una **prueba de ejecución con datos de ejemplo** (requisito del
   "cierre del práctico").

### 4.3 PROHIBICIONES
- ❌ **NO uses `ArrayDeque`, `Stack`, `LinkedList`, `ArrayList` ni `Queue` de
  `java.util`.** El enunciado pide **arreglos**, y toda la guía consiste en
  implementar la estructura a mano.
- ❌ NO uses streams ni lambdas.
- ❌ NO agregues dependencias ni sistema de build.
- ❌ NO inventes requisitos. Si el enunciado es ambiguo, elegí lo más simple,
  **decilo en el informe** y seguí.

### 4.4 Regla dura — operación sobre estructura vacía o llena
**Ninguna operación puede lanzar una excepción** cuando la estructura está vacía
o llena. Se informa con un mensaje. Es la diferencia entre una pila bien
implementada y una que revienta el programa.

## 5. ESTRUCTURA DE CADA ARCHIVO

```java
/* ... PROMPT INICIAL ... */

/**
 * GUÍA 4 — PILAS Y COLAS CON ARREGLOS
 * Ejercicio N: <Título>
 *
 * ESTRUCTURA — qué es y por qué LIFO o FIFO
 * ÍNDICES — top / front / rear, valor inicial y evolución
 * OPERACIONES — contrato de cada una
 * CASOS LÍMITE — vacía, llena, capacidad mínima
 * COMPLEJIDAD — todas las operaciones
 */
public class EjercicioN {

    // === ESTRUCTURA ===
    // === DEMOSTRACIÓN (main) ===
    // === UTILIDADES ===
}

/* ... AJUSTES DEL PROMPT ... */
```

## 6. LOS 10 EJERCICIOS

### E1 — Pila de enteros
- Clase `PilaEnteros` con **arreglo de tamaño fijo**.
- Operaciones: `push`, `pop`, `peek`, `isEmpty`, `isFull`, `size`.
- **El prompt debe explicar cómo se usa el índice `top`, qué valor inicial debe
  tener (`0`) y qué pasa con pila llena o vacía.**
- **Por qué `top` es un contador y no la posición del tope**: si fuera posición,
  el vacío sería `-1` y habría que castear en cada acceso.
- Todas las operaciones Θ(1).

### E2 — Simulador de torre de platos
- Platos por número o nombre. Agregar (`push`), retirar el superior (`pop`),
  consultar el de arriba (`peek`).
- **El prompt debe explicar por qué este problema se resuelve con PILA y no con
  COLA**: sólo se puede tomar el último plato agregado.
- Mostrar la torre después de cada operación.

### E3 — Validar paréntesis balanceados
- Entrada: expresión matemática como `String`.
- `(5 + 3) * (2 + 1)` → **válido** · `(5 + 3)) * (2 + 1` → **inválido**.
- `push` al abrir, `pop` al cerrar; **`pop` sobre pila vacía → inválido**.
- Al terminar, **la pila debe quedar vacía**: si quedan paréntesis abiertos, es
  inválido.
- Usar `switch` sobre el carácter, no `if` encadenados.
- **Documentar** que se valida sólo `()`, no `[]` ni `{}` (queda fuera de alcance).

### E4 — Historial de navegación
- URLs. Visitar (`push`), volver atrás (`pop`), consultar la actual.
- **El prompt debe explicar por qué el historial funciona bajo LIFO**: la página
  visitada más recientemente es la que se abandona primero.
- **Documentar** que un navegador real no hace `pop` sino que consulta por índice:
  es una simplificación.

### E5 — Pila genérica `Pila<T>`
- Arreglo interno `Object[]`. Operaciones: `push`, `pop`, `peek`, `isEmpty`,
  `isFull`, `size`.
- **El prompt debe explicar qué problema resuelven los genéricos y cómo se
  diferencia de una pila hecha sólo con `int`.**
- **El cast es obligatorio**: el arreglo devuelve `Object` y hay que castear a `T`.
  Documentar que ese warning es el precio de usar arreglos.
- Probar con `Integer`, `String` y un objeto simple.

### E6 — Cola de enteros
- Clase `ColaEnteros`. Operaciones: `enqueue`, `dequeue`, `front`, `isEmpty`,
  `isFull`, `size`.
- **El prompt debe explicar para qué sirven `front` y `rear`, y cómo cambian.**
- **`rear` es el próximo espacio LIBRE, no la posición del último elemento.**
- Vacía ⟺ `front == rear`. Llena ⟺ `front == 0 && rear == capacidad`.

### E7 — Sistema de turnos
- Personas con nombre. Agregar (`enqueue`), atender siguiente (`dequeue`),
  consultar el primero (`front`).
- **El prompt debe explicar por qué este problema usa FIFO y no LIFO**: el orden
  de llegada ES el orden de atención.
- **Documentar** si se asigna número de turno o se usa el orden de llegada.

### E8 — Cola de impresión
- Documentos con `nombre` y `paginas`, en el **mismo orden** en que se agregaron.
- Al imprimir, mostrar cuántas páginas se procesan y acumular el total.
- **El prompt debe explicar cómo la cola representa correctamente ese orden.**

### E9 — Problema de desperdicio de espacio
- **El objetivo es DEMOSTRAR el problema, no resolverlo.**
- Mostrar el arreglo tras varias `enqueue` y `dequeue`, **con los espacios libres
  marcados** (por ejemplo `[_]`).
- **El prompt debe pedir explícitamente que se vea cómo quedan espacios libres
  al inicio que NO pueden reutilizarse.**
- Tras encolar y desencolar la misma cantidad, la cola queda vacía pero **sin
  espacio para reencolar**.

### E10 — Cola circular
- Reutiliza los espacios liberados al inicio del arreglo.
- **El prompt debe explicar cómo se usa el módulo `%` para mover `front` y `rear`,
  y cómo se detecta si la cola está llena o vacía.**
- `rear = (rear + 1) % capacidad` · `front = (front + 1) % capacidad`.
- **Detección con contador**: llena ⟺ `contador == capacidad`, vacía ⟺
  `contador == 0`. Documentar que la alternativa sin contador (reservar una
  posición) tiene una capacidad efectiva de `n - 1` y es ambigua.
- **Repetir la misma secuencia del E9** y mostrar que ahora sí reutiliza espacio.

## 7. ORDEN DE TRABAJO

1. Leé `PLAN_GUIA_4.md` y `Ejercicios4.docx`.
2. Redactá el prompt del ejercicio **antes** del algoritmo, y ponelo en el archivo.
3. Implementá los 10 en orden del 1 al 10.
4. **Creá `_build_check.ps1`, `.classpath` y `.vscode/launch.json`** (ver §10).
5. Ejecutá `_build_check.ps1` y verificá.
6. Releé cada archivo: ¿el prompt describe el código que realmente escribiste?

## 8. REVISIÓN FINAL OBLIGATORIA

- [ ] Existen los 10 archivos.
- [ ] **Cada uno empieza con `PROMPT INICIAL`** con las 5 secciones.
- [ ] **Cada uno termina con `AJUSTES DEL PROMPT`**.
- [ ] **Cada uno incluye una prueba de ejecución con datos de ejemplo.**
- [ ] Ninguna operación sobre estructura vacía o llena lanza excepción.
- [ ] `top` se inicializa en **0** y se explica por qué.
- [ ] `rear` es el próximo espacio libre, y `front`/`rear` evolucionan bien.
- [ ] `Pila<T>` funciona con `Integer`, `String` y un objeto.
- [ ] E9 **muestra los espacios libres inservibles** con el arreglo visible.
- [ ] E10 **reutiliza** esos espacios y lo demuestra.
- [ ] **No se usa `ArrayDeque`, `Stack`, `LinkedList` ni `ArrayList`.**
- [ ] Encoding UTF-8 en la salida.

## 9. CODIFICACIÓN DE LA SALIDA

Todos los `main` arrancan con:

    System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

Sin esto la consola de Windows (CP850) convierte `Θ` y los acentos en bytes
ilegibles.

## 10. ARCHIVOS DE CONFIGURACIÓN A CREAR

### `_build_check.ps1`

1. **Localizar la carpeta por sí sola** — buscar el `.docx` en `$PSScriptRoot` y,
   si no está, en una carpeta hermana con el mismo nombre. **NO hardcodear
   "Guia 4"**.
2. **Entrada por archivo temporal** con redirección `cmd /c "... < archivo"`.
   - ❌ **NO uses el pipeline** (`$input | & java`): PowerShell 5.1 **siempre**
     antepone BOM al enviar texto a un proceso nativo, incluso con
     `UTF8Encoding($false)`. Ese BOM convierte `"5"` en `"\uFEFF5"` y
     `Scanner.hasNextInt()` devuelve false.
   - **Todos** los ejercicios reciben entrada, incluso los que no la necesitan:
     un archivo vacío no produce EOF y el proceso queda colgado.
3. `[Console]::OutputEncoding` y `$OutputEncoding` en bloques `try` **separados**.
4. Flags `-Clean` (borra `.class` y sale) y `-NoRun` (sólo compila).
5. Mensaje claro si no encuentra los `.java`.

### `.classpath`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="src" path="."/>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
```

Sin este archivo el Java Language Server no reconoce la carpeta y **no aparece
el botón "Run"** de VS Code.

### `.vscode/launch.json`

**Va DENTRO de la carpeta de la guía**, no en la raíz del repo. Un `launch.json`
en la raíz sería ambiguo: las 4 guías tienen un `Ejercicio1` en default package y
el depurador no sabría cuál ejecutar. Cada configuración lleva
`"cwd": "${fileDirname}"`.

## 11. INFORME FINAL

### Archivos
| # | Archivo | Líneas | Estructura | Operaciones | Θ |
|---|---------|--------|------------|-------------|---|
| 1 | Ejercicio1.java | N | `PilaEnteros` | push, pop, peek, isEmpty, isFull, size | Θ(1) |

### Verificación ejecutada
Qué corriste, qué salida dio, si algún test falló. **No afirmes que funciona
algo que no ejecutaste.**

### Decisiones y supuestos
- Cómo detectaste cola llena y vacía en E10.
- Si asignaste número de turno en E7.
- Qué política de mayúsculas o de alcance elegiste en E3.

### Incumplimientos o dudas
Si algo no pudiste hacer o el enunciado era ambiguo, decilo acá.

## 12. EMPEZÁ

No me preguntes qué hacer: el enunciado está arriba y el plan tiene el detalle.
Arrancá por el Ejercicio 1 **redactando primero su prompt**, luego la estructura,
y seguí. Al terminar corré `_build_check.ps1` y reportá.
```