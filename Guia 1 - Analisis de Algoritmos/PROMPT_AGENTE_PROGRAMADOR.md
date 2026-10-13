# PROMPT MAESTRO — Agente Programador

> **Uso:** copiá todo el bloque de abajo y pegalo como argument del agente `Programmer`.
> Referencia al plan: `PLAN_GUIA_1.md` (mismo directorio).

---

```text
# TAREA — Resolver los 10 ejercicios de la "Guía 1: Análisis de Algoritmos"

## 0. ROL Y ALCANCE

Sos el PROGRAMADOR del proyecto. Vas a implementar los 10 ejercicios de la
Guía 1 de Análisis de Algoritmos en Java 21.

El plan técnico completo está en `Guia 1 - Analisis de Algoritmos/PLAN_GUIA_1.md`.
Leelo antes de escribir código. Este prompt resume el plan; si el plan y el código
existente se contradicen, **gana el código real** y lo reportás.

## 1. CONTEXTO DEL PROYECTO (verificado — no lo re-descubras)

- Workspace: `c:\Facu Things\2026\Estructura de Datos y Algoritmo\Guias de Practico`
- Carpeta de trabajo: `Guia 1 - Analisis de Algoritmos`
- **JDK instalado: 21.0.10 LTS.** Java 21. Compilá con `javac`, sin Maven ni Gradle.
- **NO hay** `pom.xml`, `build.gradle`, `.project` ni sistema de build. **NO lo crees.**
- **NO hay** JUnit ni ningún framework de tests. **NO lo agregues.**
- `Guia 1 - Analisis de Algoritmos/Ejercicio1_1.java` ya existe y es un **stub vacío**:
      public class Ejercicio1_1 {}
  → Debés completarlo, no crear un archivo paralelo.
- Los ejercicios 2 a 10 **no existen**. Debés crearlos.
- Todos los archivos van en **default package** (sin declaración `package`), porque
  `javac` directo no usa classpath de proyecto.

## 2. CONVENCIÓN DE ARCHIVOS

| Ejercicio | Archivo | Acción |
|-----------|---------|--------|
| 1 | `Ejercicio1_1.java` | MODIFICAR (ya existe como stub) |
| 2 | `Ejercicio1_2.java` | CREAR |
| 3 | `Ejercicio1_3.java` | CREAR |
| 4 | `Ejercicio1_4.java` | CREAR |
| 5 | `Ejercicio1_5.java` | CREAR |
| 6 | `Ejercicio1_6.java` | CREAR |
| 7 | `Ejercicio1_7.java` | CREAR |
| 8 | `Ejercicio1_8.java` | CREAR |
| 9 | `Ejercicio1_9.java` | CREAR |
| 10 | `Ejercicio1_10.java` | CREAR |

En cada uno: `public class EjercicioN` + `public static void main(String[] args)`.
Todos los archivos en `Guia 1 - Analisis de Algoritmos/`.

## 3. ⚠️ OVERRIDES A TUS REGLAS — leelos antes de nada

Tu definición de agente dice "No agregues comentarios obvios" y "toda nueva
funcionalidad debería tener tests". **En esta tarea ambas reglas se modifican:**

### 3.1 Comentarios — PREVALECE LA GUÍA
Los 10 ejercicios exigen **código comentado con fines didácticos**. Está
**autorizado y requerido** comentar:

- La **invariante** del bucle (qué garantiza que sea cierta en cada vuelta).
- El **motivo** de un corte temprano (y por qué es seguro).
- Los **casos límite** manejados y por qué.
- El **contador de instrumentación** y qué está contando exactamente.
- La **complejidad** de la función, en el Javadoc de cabecera.

**Prohibido** comentar lo autoevidente (`// i++`, `// cerrar scanner`).

### 3.2 Tests — NO HAY FRAMEWORK. Usá un `main` de auto-demostración
No existe JUnit y no vas a agregar dependencias. La verificación se reemplaza por
un bloque `main` que:
1. Ejercuta el algoritmo sobre **al menos 3 casos**: uno típico, uno límite
   (vacío / un solo elemento) y uno adversario.
2. Muestra los contadores de instrumentación.
3. Imprime el resultado de forma legible.

### 3.3 NO podés compilar ni ejecutar
No tenés herramienta de terminal. **No afirmes que el código compila** si no lo
verificaste. En su lugar:

1. Hacé **revisión estática** de cada archivo: imports correctos, firmas válidas,
   balance de llaves, variables declaradas antes de uso, tipos compatibles.
2. Entregá al final los comandos exactos de compilación y ejecución para que
   los ejecute el usuario.
3. Si detectás un error de compilación por análisis, corregilo antes de cerrar.

### 3.4 PROHIBICIONES
- ❌ **NO uses `Arrays.sort()`, `Collections.sort()`, `List.sort()` ni ningún
  sort de biblioteca** en los ejercicios 1, 3 y 10. Implementá el ordenamiento a
  mano cuando el enunciado lo pida. (En el 3 sólo podés ordenar el vector de
  *prueba* para preparar la precondición.)
- ❌ NO agregues dependencias externas.
- ❌ NO crees un sistema de build, `package-info.java`, ni estructura de paquetes.
- ❌ NO refactorices `Ejercicio1_1.java` más allá de su contenido.
- ❌ NO uses Lombok, streams, records ni sintaxis reciente innecesaria. Esto es
  material de cátedra de **Estructura de Datos y Algoritmos**: legibilidad
  Didáctica > elegancia funcional.
- ❌ NO inventes lo que el enunciado no pide. Si algo es ambiguo, elegí la opción
  más simple, **decila explícitamente** en el informe final y seguí adelante.

## 4. PLANTILLA DE SALTA OBLIGATORIA POR EJERCICIO

Cada `EjercicioN.java` debe contener, **en este orden**, 7 bloques:

```java
/**
 * GUÍA 1 — ANÁLISIS DE ALGORITMOS
 * Ejercicio N: <Título>
 *
 * <1. ESTRATEGIA>
 * Qué algoritmo usaste y por qué es el adecuado para este problema.
 *
 * <2. COMPLEJIDAD>
 * Temporal: Θ(...) — mejor / promedio / peor
 * Espacial: Θ(...)  — justificada
 *
 * <3. PRECONDICIONES Y CASOS LÍMITE>
 * Qué se asume de la entrada y cómo se manejan los casos borde.
 */
public class EjercicioN { ... }
```

Y en el cuerpo del archivo, claramente delimitados con comentarios de sección:

```java
    // ============================================================
    //  ALGORITMO
    // ============================================================

    // ============================================================
    //  DEMOSTRACIÓN (main)
    // ============================================================

    // ============================================================
    //  UTILIDADES (mostrar vector, formatear salida)
    // ============================================================
```

## 5. REGLAS TRANSVERSALES

1. **Inicializá los acumuladores con el primer dato, nunca con `0`.**
   Si no, un vector de negativos te da un resultado incorrecto.
2. **Vector o matriz vacío → mensaje claro**, nunca excepción no controlada.
3. Usá `try-with-resources` para el `Scanner`, o `close()` explícito.
4. Promové a `long` las sumas (Ej. 8) para no desbordar `int`.
5. En la búsqueda binaria: `mid = low + (high - low) / 2` (evita overflow).
6. Todo contador de instrumentación se incrementa **dentro de la lógica real
   del algoritmo**, no decorando un `println`.
7. Respondé **en español**, con el mismo registro del enunciado.
8. Nombres de variables descriptivos en español o inglés consistente — elegí uno
   y mantenelo. La cátedra usa español.
9. Preservá el array original en los ejercicios donde el enunciado lo exige
   (ej. 1: NO debe ordenar ni mutar el vector de entrada).

## 6. LOS 10 EJERCICIOS — requisitos específicos

### E1 — Mínimo de un vector
- Recorrido lineal con acumulador `min`.
- **Justificá explícitamente por qué NO es necesario ordenar el vector**:
  ordenar es Θ(n log n) **y además destruye el vector de entrada**. El mínimo
  sólo requiere comparar cada elemento contra el mejor candidato.
  **Comparar no es ordenar.**
- Θ(n) temporal, Θ(1) espacial. Instrumentá comparaciones.

### E2 — Buscar un elemento en un vector desordenado
- Búsqueda lineal con corte temprano.
- Análisis de **mejor, peor y promedio** caso.
- **El programa debe informar cuántas posiciones recorrió** hasta encontrar el
  elemento, o declarar que no existe (en ese caso recorrió las n).
- Instrumentá posiciones recorridas.

### E3 — Buscar un elemento en un vector ordenado (binaria)
- Búsqueda binaria con `low` / `mid` / `high`.
- **Justificá por qué es más eficiente que la lineal cuando el dato está
  ordenado**: el ordenamiento aporta monotonía, cada comparación descarta la
  mitad. Compará el costo total Θ(n log n) + Θ(log n) contra Θ(n).
- **El código debe mostrar paso a paso cómo evolucionan las variables de
  búsqueda**: nº de iteración, `low`, `mid`, `high`, valor comparado, decisión.
- Declará la precondición: vector ordenado ascendente.

### E4 — Detectar elementos duplicados — DOS soluciones
- **Solución A:** dos ciclos anidados (`i < j`).
- **Solución B:** `java.util.HashSet`.
- **Tabla comparativa** de complejidad temporal (mejor/promedio/peor) y espacial
  de ambas, + veredicto de cuándo usar cada una.
- Mencioná que HashSet degrada a Θ(n²) en el peor caso por colisiones de hash.

### E5 — Contar ocurrencias
- Recorrido completo con contador.
- **Justificá por qué necesita recorrer TODO el vector**: a diferencia del E2 no
  puede cortar temprano, porque una coincidencia no descarta que haya más
  apariciones; cortar temprano haría el resultado incorrecto.
- Θ(n) temporal, Θ(1) espacial.

### E6 — Comparar dos vectores
- Comparación índice a índice con **salida apenas detecta una diferencia**.
- Análisis de mejor, peor y promedio caso.
- **Decidí y documentá** qué pasa si las longitudes difieren (recomendado:
  unequal sin entrar al ciclo) y por qué.

### E7 — Invertir un vector — DOS versiones
- **Versión A:** con vector auxiliar → Θ(n) tiempo, Θ(n) espacio.
- **Versión B:** in-place con dos punteros / swap → Θ(n) tiempo, Θ(1) espacio.
- **Tabla comparativa** con ventajas, desventajas, T y S de ambas.

### E8 — Sumar todos los elementos de una matriz
- Explicá **cómo se recorrerá** la matriz (doble ciclo fila → columna).
- Análisis de complejidad en función de m (filas) y n (columnas).
- **Contabilizá la cantidad de operaciones** realizadas.
- Mencioná la invariante del acumulador.

### E9 — Mayor elemento de una matriz
- **Justificá por qué es necesario recorrer todos los elementos**: el máximo
  actual sólo se descarta a sí mismo, nunca descarta la región restante. No hay
  información que habilite un corte temprano.
- Complejidad temporal Θ(m·n) y espacial Θ(1). Instrumentá comparaciones.

### E10 — Ordenamiento burbuja (Bubble Sort)
- Burbuja clásica + **bandera `swapped`** para salida temprana.
- **Justificá por qué sólo sirve para fines didácticos o conjuntos muy
  pequeños**: su caso promedio Θ(n²) supera a cualquier sort de producción
  (Timsort = Θ(n log n)).
- **Contabilizá comparaciones e intercambios por separado** — es el punto del
  ejercicio.
- Complejidad: mejor Θ(n) (ya ordenado), promedio y peor Θ(n²), espacial Θ(1).

## 7. ORDEN DE TRABAJO

1. Leé `PLAN_GUIA_1.md` y `Ejercicios.docx`.
2. Verificá el estado real de `Ejercicio1_1.java` y confirmá que los otros 9 no existen.
3. Implementá los 10 ejercicios **en orden del 1 al 10**.
4. Revisá estáticamente cada archivo antes de pasar al siguiente.
5. Al terminar, corré una revisión de consistencia (§8).

## 8. REVISIÓN FINAL OBLIGATORIA

Antes de reportar, verificá una por una:

- [ ] Existen los 10 archivos `Ejercicio1_1.java` … `Ejercicio1_10.java`.
- [ ] `Ejercicio1_1.java` se **modificó**; no se creó un duplicado.
- [ ] Todos en default package, `public class EjercicioN`, con `main`.
- [ ] Cada archivo tiene los 7 bloques de la plantilla (§4).
- [ ] **La complejidad del Javadoc coincide con la del código.** Esto es lo más
      importante: si el código hace 2 pasadas y vos declarás Θ(n), es un error.
- [ ] Los contadores se incrementan dentro de la lógica real del algoritmo.
- [ ] Los ejercicios 4 y 7 tienen 2 implementaciones + tabla comparativa.
- [ ] El ejercicio 3 emite la traza paso a paso.
- [ ] Ningún `Arrays.sort()` en los ejercicios 1, 3 y 10.
- [ ] Los vectores de entrada no se mutan donde el enunciado lo prohíbe.
- [ ] No se agregaron dependencias ni archivos de build.

## 9. INFORME FINAL

Respondé con exactamente esta estructura:

### Archivos
| # | Archivo | Acción | Líneas | Complejidad T | Complejidad S |
|---|---------|--------|--------|---------------|---------------|
| 1 | Ejercicio1_1.java | MODIFICADO | N | Θ(n) | Θ(1) |
| ... | ... | ... | ... | ... | ... |

### Verificación estática
Qué revisaste en cada archivo (imports, firmas, balance, tipos) y qué
conclusionés. **Declará explícitamente que no pudiste compilar** y por qué.

### Comandos para el usuario
Los comandos `javac` y `java` exactos, listos para copiar.

### Decisiones y supuestos
- Precondiciones que asumiste (ej. longitudes distintas en el E6).
- Casos límite cómo los resolviste.
- Cualquier ambigüedad del enunciado y cómo la interpretaste.

### Incumplimientos o dudas
Si algo no pudiste hacer o el enunciado era contradictorio, decilo acá.
No lo ocultes ni lo resuelvas silenciosamente.

## 10. EMPEZÁ

No me preguntes qué hacer: el enunciado de los 10 ejercicios está completo
arriba. Arrancá por el Ejercicio 1, mostrame el análisis de complejidad **antes**
de escribir el algoritmo, y seguí.
```