# PROMPT MAESTRO — Agente Programador · Guía 3

> **Uso:** copiá el bloque de abajo y pasalo como argument del agente `Programmer`.
> Plan técnico: `PLAN_GUIA_3.md` (mismo directorio).

---

```text
# TAREA — Resolver los 10 ejercicios de "Algoritmos de Ordenamiento" (Guía 3)

## 0. ROL Y ALCANCE

Sos el PROGRAMADOR del proyecto. Vas a implementar los 10 ejercicios de
ordenamiento en Java 21.

El plan técnico está en `Guia 3 - Ordenamiento/PLAN_GUIA_3.md`. Leelo antes de
escribir código. Si el plan y el enunciado de `Ejercicios3.docx` se contradicen,
gana el enunciado y lo reportás.

## 1. LO QUE PIDE LA CONSIGNA GENERAL

Para **cada** ejercicio hay que escribir PRIMERO un prompt que explique seis
cosas, y después implementar, probar y **ajustar el prompt si el resultado no
es correcto**:

1. Qué algoritmo de ordenamiento se usa.
2. Cuál es el razonamiento del algoritmo.
3. Qué datos recibe el programa.
4. Qué debe mostrar por pantalla.
5. Qué comentarios debe incluir el código.
6. Cómo se verifica que el resultado sea correcto.

Hay un ciclo de iteración, no un disparo único.

### Decisión tomada sobre el prompt

La consigna de esta guía **no dice explícitamente** que el prompt vaya dentro
del `.java` (la Guía 2 sí lo pedía). **Decisión: embeberlo como bloque de
comentario al inicio de cada archivo**, igual que en la Guía 2, y cerrar cada
archivo con un bloque de ajustes del prompt.

Es una interpretación nuestra y queda declarada como tal. Si el resultado de la
prueba no es el esperado, el bloque de ajustes dice qué se cambió del prompt y
por qué.

## 2. CONTEXTO DEL PROYECTO (verificado — no lo re-descubras)

- Carpeta: `Guia 3 - Ordenamiento`
- **JDK 21.0.10 LTS** en el PATH. Compilá con `javac`.
- **NO hay** `pom.xml`, `build.gradle`, ni sistema de build. **NO lo crees.**
- **NO hay** JUnit. **NO lo agregues.**
- `Ejercicios3.docx` es el único archivo: los 10 `.java` **no existen**, se crean.
- **No existe `_build_check.ps1`**: crealo vos (ver §10).
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
 * Quiero implementar en Java un programa que ordene [DATO] utilizando
 * [ALGORITMO].
 *
 * ALGORITMO ELEGIDO
 * [Qué algoritmo es y por qué ese y no otro.]
 *
 * RAZONAMIENTO
 * [La idea central del algoritmo. No la definición formal: la intuición que lo
 *  hace funcionar.]
 *
 * DATOS QUE RECIBE
 * [Tipo del arreglo, cantidad, de dónde salen, si es fija o por consola.]
 *
 * QUÉ DEBE MOSTRAR POR PANTALLA
 * [Requisito de visualización/instrumentación del enunciado, punto por punto.]
 *
 * COMENTARIOS QUE DEBE INCLUIR EL CÓDIGO
 * [Invariantes, contadores, decisiones de diseño.]
 *
 * CÓMO VERIFICAR QUE EL RESULTADO ES CORRECTO
 * [Casos de prueba y con qué oráculo se comparan.]
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
> que omitirlo**, porque la consigna pide registrar los ajustes.

## 4. ⚠️ OVERRIDES A TUS REGLAS

Tu definición de agente dice "No agregues comentarios obvios" y "toda
funcionalidad debería tener tests". **En esta tarea ambas se modifican:**

### 4.1 Comentarios — PREVALECE LA CONSIGNA
Está **autorizado y requerido** comentar la invariante del bucle, los
contadores, la elección de pivote o de gap, los casos límite y el desborde de
tipo. **Prohibido** comentar lo autoevidente.

### 4.2 Tests — NO HAY FRAMEWORK. Usá un `main` de auto-demostración
Sin JUnit. El `main` debe:
1. Ejecutar **al menos 4 tipos de arreglo**: ordenado, inverso, aleatorio y
   casi ordenado. Son los que revelan las diferencias entre algoritmos.
2. Mostrar los contadores que el enunciado pide.
3. **Comparar contra un oráculo**: el resultado debe coincidir con el arreglo
   ordenado construido aparte, y con `Arrays.sort` usado **sólo como
   verificador externo** (nunca dentro del algoritmo).

### 4.3 PROHIBICIONES
- ❌ **NO uses `Arrays.sort`, `Collections.sort`, `List.sort` ni `Stream.sorted`
  dentro de los algoritmos.** Todo se implementa a mano. (Sí podés usarlos como
  oráculo en la verificación, fuera del algoritmo.)
- ❌ NO uses boxed types (`Integer[]`) donde alcene `int[]`.
- ❌ NO agregues dependencias ni sistema de build.
- ❌ NO uses streams ni lambdas para implementar los algoritmos.
- ❌ NO inventes requisitos. Si el enunciado es ambiguo, elegí lo más simple,
  **decilo en el informe** y seguí.

### 4.4 Regla de tipos para los contadores
**Todos los contadores de comparaciones, intercambios y desplazamientos son
`long`, no `int`.** Con Bubble o Selection sobre 100 000 elementos el conteo
supera $2^{31}$ y desborda.

## 5. ESTRUCTURA DE CADA ARCHIVO

```java
/* ... PROMPT INICIAL ... */

/**
 * GUÍA 3 — ALGORITMOS DE ORDENAMIENTO
 * Ejercicio N: <Título>
 *
 * ALGORITMO — cuál y por qué
 * RAZONAMIENTO — la idea central
 * INVARIANTE — qué garantiza cada pasada
 * COMPLEJIDAD — temporal y espacial, con la justificación
 * INSTRUMENTACIÓN — qué mide cada contador
 * CASOS LÍMITE — vacío, 1 elemento, ya ordenado, inverso, duplicados
 */
public class EjercicioN {

    // === ALGORITMO ===
    // === DEMOSTRACIÓN (main) ===
    // === UTILIDADES ===
}

/* ... AJUSTES DEL PROMPT ... */
```

## 6. REFERENCIA RÁPIDA DE COMPLEJIDADES

Usala para las tablas, pero **los números que importan son los que miden los
contadores**, no los de esta tabla.

| Algoritmo | Mejor | Promedio | Peor | Espacio | Estable |
|---|---|---|---|---|---|
| Bubble Sort | Θ(n)¹ | Θ(n²) | Θ(n²) | Θ(1) | Sí |
| Selection Sort | Θ(n²) | Θ(n²) | Θ(n²) | Θ(1) | No² |
| Insertion Sort | Θ(n) | Θ(n²) | Θ(n²) | Θ(1) | Sí |
| Shell Sort | Θ(n log n) | Θ(n log² n)³ | Θ(n^1.5)³ | Θ(1) | No |
| Quick Sort | Θ(n log n) | Θ(n log n) | Θ(n²)⁴ | Θ(log n) | No |
| Merge Sort | Θ(n log n) | Θ(n log n) | Θ(n log n) | **Θ(n)** | Sí |

¹ Con bandera de corte temprano. ² No estable porque el intercambio salta un
igual. ³ Secuencia de Knuth (1, 4, 13, 40…). ⁴ Pivote = primero y arreglo
ordenado.

## 7. LOS 10 EJERCICIOS

### E1 — Bubble Sort paso a paso
- Mostrar el arreglo original y **el estado después de cada pasada**.
- El razonamiento debe explicar **por qué el mayor se desplaza hacia el final**.
- Invariante: tras la pasada *i*, los *i* mayores ya están en posición final.
- Contadores: comparaciones e intercambios. Bandera `huboIntercambio`.

### E2 — Bubble vs. Selection
- **Dos métodos con las firmas exactas**: `bubbleSort(int[])` y
  `selectionSort(int[])`.
- Contar comparaciones e intercambios de ambos **sobre el mismo arreglo**.
- El razonamiento debe contrastar **comparar vecinos** vs. **buscar el mínimo**.
- **Hallazgo clave a destacar**: Selection hace **exactamente n(n−1)/2
  comparaciones siempre**, sin importar el orden; Bubble varía de n−1 a
  n(n−1)/2. El costo de Selection es *independiente de los datos*.

### E3 — Insertion Sort sobre arreglo casi ordenado
- Datos: `int[] numeros = {1, 2, 3, 5, 4, 6, 7}`.
- Mostrar **cuántos desplazamientos** realiza.
- Razonamiento: se inserta cada elemento en el prefijo ya ordenado; si ya está
  en su lugar, no hay desplazamiento.
- **Contar comparaciones y desplazamientos por separado**: no son lo mismo.
- Contraste: casi ordenado → Θ(n) comparaciones y muy pocos desplazamientos.

### E4 — Selection Sort sobre nombres
- Datos: `String[] nombres = {"Lucia", "Ana", "Pedro", "Juan"}`.
- Esperado: `["Ana", "Juan", "Lucia", "Pedro"]`.
- Razonar **cómo cambia la comparación** al ordenar textos.
- **Punto crítico**: `String.compareTo` compara por valor Unicode, no
  alfabéticamente. `'A'`=65, `'Z'`=90, `'a'`=97 → `"ana"` resulta **mayor** que
  `"Lucia"`. Documentá la política y **mostrá también** la variante con
  `compareToIgnoreCase` para que se vea la diferencia.

### E5 — ShellSort mostrando los gaps
- Mostrar **el gap de cada etapa** y **el estado del arreglo tras cada pasada**.
- Gaps de Knuth: `1, 4, 13, 40, 121, 364, …` hasta que el gap ≥ n.
- Razonamiento: es **Insertion Sort con gap variable**; el salto permite que un
  elemento viaje varias posiciones de una vez.
- Por qué mejora: las etapas con gap grande dejan los elementos casi en su
  lugar; las finales pulen.

### E6 — Quicksort con primer elemento como pivote
- Mostrar: **pivote elegido · subarreglo izquierdo · subarreglo derecho ·
  arreglo final**.
- Razonamiento: **divide y vencerás**. Es conexión directa con la Guía 2:
  caso base = subarreglo de 0 o 1 elemento; caso recursivo = dos llamadas.
- Documentá qué partición usás (Lomuto u otra).

### E7 — Peor caso de Quicksort
- Datos: `int[] numeros = {1, 2, 3, 4, 5, 6, 7}`.
- **Contar las llamadas recursivas** y la profundidad de pila.
- Explicar por qué: con el arreglo ordenado y pivote = primero, el pivote es
  siempre el **mínimo**, el subarreglo izquierdo queda vacío y el derecho
  recibe n−1. La partición no divide: **rebana**.
- **Cuidado**: con n grande la recursión desborda la pila (~15 000 marcos).
  Usá un tope modesto y capturá el `StackOverflowError` si lo excedés.

### E8 — MergeSort paso a paso
- Mostrar: **cómo divide · cuándo llega al corte · cómo fusiona**.
- Razonamiento: dividir hasta subarreglos de 1 elemento, luego fusionar dos
  mitas ordenadas.
- **Distinción clave**: dividir es Θ(1) por nivel, fusionar es Θ(k). Total
  Θ(n log n) por T(n) = 2T(n/2) + Θ(n).
- **Espacial Θ(n)**: es el único que NO es in-place. Explicá que ése es el
  precio de la garantía de Θ(n log n) en todos los casos.
- Demostrá que es **estable**.

### E9 — Los tres algoritmos simples
- Bubble, Selection e Insertion sobre **el mismo arreglo**.
- Mostrar: arreglo ordenado · comparaciones · intercambios/desplazamientos.
- Ejecutar los tres sobre **ordenado, inverso, aleatorio y casi ordenado**, y
  mostrar cómo cambia cada columna.
- El objetivo es comparar **las estrategias**, no sólo el resultado: los tres
  ordenan igual, lo que difiere es cuánto trabajo hacen y **cómo depende ese
  trabajo del orden de los datos**.

### E10 — Ranking de puntajes
- Datos: `Jugador("Ana", 1200)`, `Jugador("Pedro", 900)`,
  `Jugador("Lucia", 1500)`.
- Ordenar de **mayor a menor**. Esperado: Lucia 1500 · Ana 1200 · Pedro 900.
- **Elegís el algoritmo y lo justificás.**
  Recomendación: **Insertion Sort**, porque un ranking se actualiza de a uno
  (llega una puntuación nueva cada vez) e insertar en un arreglo ya ordenado
  cuesta Θ(n). Es el caso de uso natural.
- **Desempate**: definí el criterio (propuesta: a igual puntaje, orden alfabético
  por nombre) y **documentalo**.
- Usá `record Jugador(String nombre, int puntaje)` y `Comparator`.

## 8. ORDEN DE TRABAJO

1. Leé `PLAN_GUIA_3.md` y `Ejercicios3.docx`.
2. Redactá el prompt del ejercicio **antes** del algoritmo, y ponelo en el archivo.
3. Implementá los 10 en orden del 1 al 10.
4. **Creá `_build_check.ps1`** (ver §10) y usalo para compilar y ejecutar.
5. Releé cada archivo: ¿el prompt describe el código que realmente escribiste?
6. Si un resultado no coincide con lo esperado, ajustá el prompt y **anotá el
   ajuste** en el bloque final.

## 9. REVISIÓN FINAL OBLIGATORIA

- [ ] Existen los 10 archivos.
- [ ] **Cada uno empieza con `PROMPT INICIAL`** con las **6 secciones**.
- [ ] **Cada uno termina con `AJUSTES DEL PROMPT`**.
- [ ] El prompt describe el algoritmo real.
- [ ] **Ningún archivo usa `Arrays.sort` / `Collections.sort` dentro de un algoritmo.**
- [ ] Todos los contadores son `long`.
- [ ] E1 estado por pasada · E2 tabla Bubble vs Selection · E3 desplazamientos
- [ ] E4 política de mayúsculas documentada · E5 gaps visibles
- [ ] E6 pivote + ambos subarreglos · E7 llamadas recursivas
- [ ] E8 división + corte + fusión · E9 los tres sobre 4 tipos de datos
- [ ] E10 ranking ordenado de mayor a menor + justificación del algoritmo
- [ ] Todos los resultados verificados contra un oráculo.
- [ ] La complejidad del Javadoc coincide con los contadores medidos.
- [ ] Encoding UTF-8 en la salida.

## 10. `_build_check.ps1`

**No existe: crealo.** Debe:

1. **Localizar la carpeta por sí solo** — buscar el `.docx` en `$PSScriptRoot` y,
   si no está, en una carpeta hermana del mismo nombre. **NO hardcodear
   "Guia 3"**: si el script se copia a otra guía, compilaría la guía equivocada
   en silencio.
2. **Dar entrada a los 10 ejercicios por archivo temporal** con redirección
   `cmd /c "... < archivo"`.
   - ❌ **NO uses el pipeline** (`$input | & java`): PowerShell 5.1 **siempre**
     antepone BOM al enviar texto a un proceso nativo, incluso con
     `UTF8Encoding($false)`. Ese BOM convierte `"5"` en `"\uFEFF5"` y
     `Scanner.hasNextInt()` devuelve false.
   - **Todos** los ejercicios reciben entrada, incluso los que no la necesitan:
     un archivo vacío no produce EOF y el proceso queda colgado.
3. Poner `[Console]::OutputEncoding` y `$OutputEncoding` en bloques `try`
   **separados**: si el primero lanza, el segundo nunca se asigna.
4. Aceptar los flags `-Clean` (borra `.class` y sale) y `-NoRun` (sólo compila).
5. Dar un mensaje claro si no encuentra los `.java`.

## 11. INFORME FINAL

### Archivos
| # | Archivo | Líneas | Algoritmo | T (peor) | S |
|---|---------|--------|-----------|-----------|---|
| 1 | Ejercicio1.java | N | Bubble | Θ(n²) | Θ(1) |

### Verificación ejecutada
Qué corriste, qué salida dio, si algún test falló. **No afirmes que funciona
algo que no ejecutaste.**

### Decisiones y supuestos
- Qué algoritmo elegiste en E10 y por qué.
- Política de mayúsculas en E4.
- Criterio de desempate en E10.
- Qué tipo de partición usaste en E6/E7.
- Secuencia de gaps en E5.

### Incumplimientos o dudas
Si algo no pudiste hacer o el enunciado era ambiguo, decilo acá.

## 12. EMPEZÁ

No me preguntes qué hacer: el enunciado está arriba y el plan tiene el detalle.
Arrancá por el Ejercicio 1 redactando su prompt, después el algoritmo, y seguí.
Al terminar corré `_build_check.ps1` y reportá.
```