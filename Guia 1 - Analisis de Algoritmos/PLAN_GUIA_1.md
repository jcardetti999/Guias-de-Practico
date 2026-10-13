# Plan de resolución — Guía 1: Análisis de Algoritmos

> Documento de **plan técnico**. No es código.
> Consumido por el agente `Programmer` (`.github/agents/programmer.agent.md`).
> Fuente: `Ejercicios.docx` (10 ejercicios).

---

## 1. Objetivo

Resolver los 10 ejercicios de la Guía 1 en Java, produciendo no solo el código
sino el **razonamiento algorítmico** que la cátedra exige: estrategia, justificación,
complejidad temporal y espacial, y casos límite.

El objetivo no es "que compile". Es que cada `EjercicioN.java` sirva como material
de estudio: alguien que lo lea debe poder reconstruir el análisis de complejidad
desde el propio código.

---

## 2. Estado actual

| Ruta | Estado |
|------|--------|
| `Guia 1 - Analisis de Algoritmos/Ejercicio1_1.java` | **Stub vacío**: `public class Ejercicio1_1 {}` |
| `Guia 1 - Analisis de Algoritmos/Ejercicios.docx` | Enunciados (material de cátedra) |
| `Ejercicio1_2.java` … `Ejercicio1_10.java` | No existen |

**No hay** sistema de build (no hay `pom.xml`, `build.gradle`, `.project`).
**No hay** framework de tests (no hay JUnit).
**No hay** Lombok ni dependencias externas.

### Consecuencias del plan

1. **No se agregan dependencias ni build system.** Se compila con `javac` directo.
2. **No se escriben tests JUnit** — el agente `Programmer` exige tests, pero sin
   framework disponible la verificación se resuelve con un `main` de demostración
   que ejercita casos límite e imprime la traza. Esto es una desviación
   consciente y justificada del estándar del agente.
3. **El agente `Programmer` no tiene herramienta de terminal.** No puede ejecutar
   `javac`/`java`. La verificación debe ser **estática** (revisión de tipos,
   imports, firmas, balance de llaves) + **comandos de compilación entregados
   al usuario** para que los ejecute.

---

## 3. Roles y agentes

```
┌────────────────────────┐      plan técnico       ┌────────────────────────┐
│  Technical Analyst     │ ──────────────────────► │  Programmer            │
│  (no modifica archivos)│      (este documento)   │  (crea EjercicioN.java)│
└────────────────────────┘                         └────────────────────────┘
```

| Agente | Modelo | Tools | Rol en esta guía |
|--------|--------|-------|------------------|
| `technical-analyst` | Claude Sonnet 4 | read, glob, grep, list, lsp, webfetch, websearch | Produce este plan |
| `programmer` | Claude Sonnet 4 | read, glob, grep, list, lsp, edit, webfetch, websearch | Implementa los 10 `.java` |

> **Nota crítica:** `programmer` **no tiene terminal**. Cualquier instrucción del
> prompt que le exija compilar o ejecutar es **inejecutable** y hará que el agente
> improvise o reporte un éxito falso.

---

## 4. Conflicto de reglas detectado

| Regla del agente `Programmer` | Requisito de la Guía 1 | Resolución |
|-------------------------------|------------------------|------------|
| *"No agregues comentarios obvios"* | Los 10 ejercicios exigen **código comentado** | **Prevalece la guía.** Se autoriza comentarios Brake-style con fines didácticos:ocumentar la invariante del bucle, el motivo de un corte temprano y los casos borde. **Prohibido** comentar lo autoevidente (`// i++`). |
| *"Toda nueva funcionalidad importante debería tener tests"* | No hay framework de tests en el repo | **Desviación consciente.** Se reemplaza por un `main` de auto-demostración que cubre casos límite e instrumenta las operaciones. |
| *"Ejecutá tests / build si corresponde"* | No hay build ni terminal | Se entrega la **ruta de compilación exacta** para que la ejecute el usuario. |

---

## 5. Convenciones de código

| Elemento | Convención |
|----------|-----------|
| Nombre de archivo | `EjercicioN.java` (`N` = 1..10) |
| Nombre de clase | `EjercicioN`, `public class` |
| Paquete | **Ninguno** (default package — `javac` directo lo requiere simple) |
| Versión de Java | **21** (JDK 21.0.10 LTS instalado) |
| Estructura | `main` → instancia `Scanner` → invocación de algos → cierre |
| Arrays | `int[]` (el término "vector" de la cátedra = array) |
| Salida | `System.out.println` — **sin** frameworks ni logging |
| Constantes | literales en `main`; nada de archivos de config |
| Constantes de conteo | prefijadas `CONTAR` / `contar`, incrementos explícitos |

### Plantilla de cada archivo

```java
/**
 * Guía 1 — Análisis de Algoritmos · Ejercicio N: <Título>
 * ...
 */
public class EjercicioN {

    // ===== Algoritmo =====
    // <firma> + <contrato: precondiciones, postcondiciones> + <cuerpo comentado>

    // ===== Demostración =====
    public static void main(String[] args) { ... }

    // ===== Utilidades =====
    private static void mostrarVector(int[] v) { ... }
    private static String formatearVector(int[] v) { ... }
}
```

---

## 6. Especificación por ejercicio

### Ejercicio 1 — Encontrar el mínimo de un vector

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_1.java` (ya existe como stub → **modificar**) |
| Estrategia | Recorrido lineal único, acumulador `min` inicializado en `v[0]` |
| T | $\Theta(n)$ — todos los casos |
| S | $\Theta(1)$ |
| Instrumentación | `contarComparaciones` |
| **Justificar** | Por qué **no** ordena el vector: ordenar es $\Theta(n\log n)$ y además **muta** el vector. El mínimo sólo necesita comparar cada elemento contra el mejor candidato. **Comparar ≠ ordenar.** |
| Casos límite | Vector vacío → mensaje, no excepción. Vector de 1 elemento → 0 comparaciones. Valores negativos. |

### Ejercicio 2 — Buscar un elemento en un vector desordenado

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_2.java` (crear) |
| Estrategia | Búsqueda lineal con corte temprano |
| T | Mejor $\Theta(1)$ · Peor $\Theta(n)$ · Promedio $\Theta(n)$ (coeficiente $\approx n/2$) |
| S | $\Theta(1)$ |
| Instrumentación | `contarPosicionesRecorridas` |
| Requisito | Informar cuántas posiciones recorrió hasta encontrar el elemento, **o** declarar que no existe (y entonces recorrió las $n$) |
| Casos límite | Elemento ausente · elemento en primera y última posición · vector vacío |

### Ejercicio 3 — Buscar un elemento en un vector ordenado (búsqueda binaria)

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_3.java` (crear) |
| Estrategia | Divide y vencercer con `low` / `mid` / `high` |
| T | Mejor $\Theta(1)$ · Promedio $\Theta(\log n)$ · Peor $\Theta(\log n)$ |
| S | $\Theta(1)$ |
| **Justificar** | El ordenamiento aporta **monotonía**: cada comparación descarta la mitad del rango. Coste real $\Theta(n\log n) + \Theta(\log n)$ vs $\Theta(n)$ de la lineal. Sólo conviene si el vector se consulta muchas veces o ya viene ordenado. |
| Instrumentación | **Traza paso a paso**: nº de iteración, `low`, `mid`, `high`, valor comparado, decisión |
| Precondición | **Declarar explícitamente** que el vector está ordenado ascendente |
| Casos límite | Vector vacío · duplicados · elemento mayor/menor que todos · overflow de `mid` |

### Ejercicio 4 — Detectar elementos duplicados (dos soluciones)

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_4.java` (crear) |
| Solución A | Dos ciclos anidados `i < j` |
| Solución B | `java.util.HashSet` |
| T (A) | Mejor $\Theta(1)$ · Peor $\Theta(n^2)$ · Promedio $\Theta(n^2)$ |
| S (A) | $\Theta(1)$ |
| T (B) | Peor $\Theta(n^2)$ (degradación por colisiones) · Promedio $\Theta(n)$ |
| S (B) | $\Theta(n)$ |
| Requisito | **Tabla comparativa** tiempo vs. espacio, con veredicto sobre cuándo usar cada una |
| Casos límite | Sin duplicados · todos duplicados · un solo elemento |

### Ejercicio 5 — Contar ocurrencias

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_5.java` (crear) |
| Estrategia | Recorrido completo con contador |
| T | $\Theta(n)$ — todos los casos |
| S | $\Theta(1)$ |
| **Justificar** | A diferencia del Ejercicio 2, **no puede** haber corte temprano: una coincidencia no descarta que haya más. Sólo cortando temprano el resultado puede ser incorrecto. |
| Instrumentación | `contarComparaciones` |
| Casos límite | 0 apariciones · todas las apariciones · valor ausente |

### Ejercicio 6 — Comparar dos vectores

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_6.java` (crear) |
| Estrategia | Comparación índice a índice con **salida temprana** |
| T | Mejor $\Theta(1)$ · Peor $\Theta(n)$ · Promedio $\Theta(n)$ (menor constante por corte) |
| S | $\Theta(1)$ |
| Precondición | **Si las longitudes difieren → unequal, sin entrar al ciclo.** Decisión a documentar. |
| Casos límite | Ambos vacíos · distintas longitudes · distintos en el índice 0 · distintos en el último índice |

### Ejercicio 7 — Invertir un vector (dos versiones)

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_7.java` (crear) |
| Versión A | Vector auxiliar → T $\Theta(n)$, S $\Theta(n)$ |
| Versión B | In-place con dos punteros / `swap` → T $\Theta(n)$, S $\Theta(1)$ |
| Requisito | Tabla de **ventajas, desventajas, T y S** para ambas |
| Cases | Vector vacío · 1 elemento · 2 elementos (verificar que no se cruzan los punteros) |

### Ejercicio 8 — Sumar todos los elementos de una matriz

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_8.java` (crear) |
| Estrategia | Doble ciclo `fila` → `columna`, total $m \times n$ celdas |
| T | $\Theta(m \cdot n)$ |
| S | $\Theta(1)$ (un único acumulador) |
| Instrumentación | `contarOperaciones` (adiciones) + **invariante**: tras la celda $(i,j)$ el acumulador contiene la suma del sub-rectángulo recorrido |
| Casos límite | Matriz vacía · matriz 1×1 · rectangular vs. cuadrada · `long` para evitar desborde de `int` |

### Ejercicio 9 — Encontrar el mayor elemento de una matriz

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_9.java` (crear) |
| Estrategia | Doble ciclo con acumulador `max` |
| T | $\Theta(m \cdot n)$ |
| S | $\Theta(1)$ |
| **Justificar** | Es obligatorio recorrer **todas** las celdas: el máximo actual sólo se descarta a sí mismo, nunca descarta la región restante. No hay información que habilite un corte temprano. |
| Casos límite | Matriz vacía · todos negativos · valores duplicados |

### Ejercicio 10 — Ordenamiento burbuja (Bubble Sort)

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio1_10.java` (crear) |
| Estrategia | Burbuja clásica + **bandera `swapped`** para salida temprana |
| T | Mejor $\Theta(n)$ (ya ordenado) · Promedio $\Theta(n^2)$ · Peor $\Theta(n^2)$ |
| S | $\Theta(1)$ — **in-place** |
| **Justificar** | AdECUado sólo para **fines didácticos** o conjuntos muy pequeños. No es un algoritmo de producción: en el caso promedio su costo $\Theta(n^2)$ supera al de Timsort ($\Theta(n\log n)$). |
| Instrumentación | `contarComparaciones` **y** `contarIntercambios` (por separado — es el punto del ejercicio) |
| Casos límite | Vector vacío · 1 elemento · ya ordenado (0 intercambios) · orden inverso (máximo de intercambios) |

---

## 7. Matriz de trazabilidad

| # | Archivo | Acción | Complejidad T | Complejidad S | Instrumentación |
|---|---------|--------|---------------|---------------|-----------------|
| 1 | `Ejercicio1_1.java` | MODIFICAR | $\Theta(n)$ | $\Theta(1)$ | comparaciones |
| 2 | `Ejercicio1_2.java` | CREAR | $\Theta(1)/\Theta(n)$ | $\Theta(1)$ | posiciones recorridas |
| 3 | `Ejercicio1_3.java` | CREAR | $\Theta(\log n)$ | $\Theta(1)$ | traza paso a paso |
| 4 | `Ejercicio1_4.java` | CREAR | $\Theta(n^2)$ / $\Theta(n)$ | $\Theta(1)$ / $\Theta(n)$ | comparaciones |
| 5 | `Ejercicio1_5.java` | CREAR | $\Theta(n)$ | $\Theta(1)$ | comparaciones |
| 6 | `Ejercicio1_6.java` | CREAR | $\Theta(1)/\Theta(n)$ | $\Theta(1)$ | comparaciones |
| 7 | `Ejercicio1_7.java` | CREAR | $\Theta(n)$ | $\Theta(n)$ / $\Theta(1)$ | intercambios |
| 8 | `Ejercicio1_8.java` | CREAR | $\Theta(m\cdot n)$ | $\Theta(1)$ | operaciones |
| 9 | `Ejercicio1_9.java` | CREAR | $\Theta(m\cdot n)$ | $\Theta(1)$ | comparaciones |
| 10 | `Ejercicio1_10.java` | CREAR | $\Theta(n^2)$ / $\Theta(n)$ | $\Theta(1)$ | comparaciones + intercambios |

---

## 8. Casos límite transversales

Aplican a **todos** los ejercicios:

1. **Vector/matriz vacío** → mensaje claro, nunca excepción no controlada.
2. **Un solo elemento** → el algoritmo debe ser correcto sin entrar al bucle.
3. **Valores negativos y cero** → inicializar el acumulador con `v[0]`, **nunca** con `0`.
4. **Valores duplicados**.
5. **`Scanner` cerrado** — try-with-resources o `close()` explícito.
6. **Desborde de `int`** en sumas → promover a `long`.
7. **`mid` de la búsqueda binaria** → `low + (high - low) / 2` para evitar overflow.

---

## 9. Riesgos

| Riesgo | Mitigación |
|--------|------------|
| El agente no tiene terminal y no puede verificar | Pedir **revisión estática** + entregar los comandos `javac` al usuario |
| Conflicto "no comentarios obvios" vs "código comentado" | Override explícito en el prompt (§4) |
| El agente usa `Arrays.sort()` y anula el propósito didáctico | Prohibición explícita en los ejercicios 1, 3 y 10 |
| El agente omite el análisis de complejidad por ser "solo código" | Plantilla de salida obligatoria con 7 secciones |
| El agente "mejora" `Ejercicio1_1.java` con una arquitectura que rompa la convención | Regla de cambios mínimos + convención `EjercicioN.java` |
| Respuestas en inglés | Idioma forzado a español en el prompt |

---

## 10. Criterios de aceptación

La tarea está terminada cuando:

- [ ] Existen los 10 archivos `Ejercicio1_1.java` … `Ejercicio1_10.java`.
- [ ] Cada archivo compila con `javac EjercicioN.java` sin warnings de error.
- [ ] Cada `main` imprime una salida de demostración con casos límite.
- [ ] Cada ejercicio responde las 7 secciones de la plantilla de salida.
- [ ] La complejidad declarada en el comentario de cabecera **coincide** con la del código.
- [ ] Los contadores de instrumentación son incremented en la lógica real del algoritmo, no en un println decorativo.
- [ ] Los ejercicios 4 y 7 contienen **dos** implementaciones cada uno + tabla comparativa.
- [ ] El ejercicio 3 emite la traza paso a paso.
- [ ] No se agregaron dependencias externas ni sistema de build.
- [ ] `git status` muestra únicamente los archivos nuevos/modificados del ejercicio 1.

---

## 11. Ruta de compilación (para el usuario)

El agente no tiene terminal. El usuario ejecuta:

```powershell
cd "c:\Facu Things\2026\Estructura de Datos y Algoritmo\Guias de Practico\Guia 1 - Analisis de Algoritmos"

# Compilar los 10
javac -encoding UTF-8 Ejercicio1_1.java Ejercicio1_2.java Ejercicio1_3.java Ejercicio1_4.java Ejercicio1_5.java `
        Ejercicio1_6.java Ejercicio1_7.java Ejercicio1_8.java Ejercicio1_9.java Ejercicio1_10.java

# Ejecutar
java Ejercicio1_1
java Ejercicio1_2
# ... etc
```

> Ejecutar desde el directorio del paquete (default package): los `.class` quedan
> junto a los `.java` y no hay `package` declarado.