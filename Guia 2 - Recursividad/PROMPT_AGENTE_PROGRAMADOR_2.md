# PROMPT MAESTRO — Agente Programador · Guía 2

> **Uso:** copiá el bloque de abajo y pegalo como argument del agente `Programmer`.
> Plan técnico: `PLAN_GUIA_2.md` (mismo directorio).

---

```text
# TAREA — Resolver los 10 ejercicios de la "Guía 2: Recursividad"

## 0. ROL Y ALCANCE

Sos el PROGRAMADOR del proyecto. Vas a implementar los 10 ejercicios de
recursividad en Java 21.

El plan técnico completo está en `Guia 2 - Recursividad/PLAN_GUIA_2.md`. Leelo
antes de escribir código. Si el plan y el enunciado se contradicen, gana el
enunciado de `Ejercicios2.docx` y lo reportás.

## 1. ⭐ LO QUE HACE DIFERENTE A ESTA GUÍA — LEELO PRIMERO

La consigna general de la cátedra establece cuatro requisitos que son
**parte de la entrega**. No son opcionales:

1. **El prompt de ingeniería va DENTRO del archivo `.java`**, como comentario al
   principio. No es un paso previo que se descarta: es un artefacto del ejercicio.
2. **Cada archivo termina con un bloque de ajustes** que documenta qué se cambió
   del prompt durante el desarrollo y por qué.
3. **El prompt debe tener un formato específico** (ver §3) y explicar el
   razonamiento, no sólo la instrucción.
4. *"Se evaluará tanto el correcto funcionamiento del programa como la calidad
   del razonamiento reflejado en el prompt."*

> La regla más importante: **el prompt embebido debe describir el algoritmo que
> realmente escribiste**. Si el prompt dice que el caso base es X y el código usa
> Y, el ejercicio está mal aunque el programa funcione.

## 2. CONTEXTO DEL PROYECTO (verificado — no lo re-descubras)

- Carpeta de trabajo: `Guia 2 - Recursividad`
- **JDK 21.0.10 LTS** disponible en el PATH. Compilá con `javac`.
- **NO hay** `pom.xml`, `build.gradle`, ni sistema de build. **NO lo crees.**
- **NO hay** JUnit. **NO lo agregues.**
- Los 10 archivos `EjercicioN.java` **no existen**. Todos se crean.
- **No hay** `Ejercicio2_1.java` stub como en la Guía 1: acá arrancás de cero.
- Default package (sin declaración `package`).

### Tenés terminal: SÍ

Compilá y ejecutá vos mismo con `_build_check.ps1`. **No me pidas que corra los
tests**: los corrés vos y reportás el resultado. Si algo falla, lo arreglás
antes de cerrar la tarea.

## 3. FORMATO OBLIGATORIO DEL PROMPT EMBEBIDO

La cátedra da esta plantilla. Cada archivo debe abrir con este bloque, adaptado
al ejercicio que le toque:

```java
/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que [PROBLEMA].
 *
 * Este problema puede resolverse recursivamente porque [POR QUÉ].
 *
 * El caso base es [CONDICIÓN], porque [MOTIVO].
 *
 * El caso recursivo es [LLAMADA], porque en cada llamada [CÓMO SE REDUCE].
 *
 * La función debe devolver [RESULTADO].
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo y la pila de llamadas.
 *
 * También quiero probarlo con estos casos:
 *   - [CASO 1]
 *   - [CASO 2]
 *   - [CASO 3]
 * ========================================================================== */
```

Y el archivo **debe terminar** con:

```java
/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - [CAMBIO]: [MOTIVO]
 * ========================================================================== */
```

> Si el prompt no necesitó cambios, el bloque dice exactamente eso:
> `- No se modificó el prompt. El razonamiento inicial resultó correcto.`
> **Declararlo es mejor que omitirlo.**

## 4. ⚠️ OVERRIDES A TUS REGLAS

Tu definición de agente dice "No agregues comentarios obvios" y "toda
funcionalidad debería tener tests". **En esta tarea ambas reglas se modifican:**

### 4.1 Comentarios — PREVALECE LA GUÍA
Está **autorizado y requerido** comentar:
- El **caso base** y por qué es ese y no otro.
- El **caso recursivo** y cómo se reduce el problema en cada llamada.
- La **pila de llamadas**: qué se apila en cada marco y por qué la profundidad
  crece hasta $\Theta(n)$.
- El **prompt embebido** y los ajustes.
- Los **casos límite** y el desborde de tipo.

**Prohibido** comentar lo autoevidente (`// i++`, `// return`).

### 4.2 Tests — NO HAY FRAMEWORK. Usá un `main` de auto-demostración
Sin JUnit y sin dependencias. La verificación se reemplaza por un `main` que:
1. Ejercita **al menos 3 casos**: típico, límite (caso base, entrada vacía) y
   adversario (desborde o recursión infinita).
2. **Muestra la profundidad de la pila de llamadas** durante la recursión — la
   plantilla de la cátedra lo pide explícitamente.
3. Imprime el resultado y los contadores de llamadas recursivas.

### 4.3 PROHIBICIONES
- ❌ **NO uses el operador `*`** en el Ejercicio 3.
- ❌ **NO uses `Math.pow`** en el Ejercicio 4.
- ❌ **NO uses `for` ni `while`** en el Ejercicio 10 (ni como "ayuda" en ningún
  ejercicio: la resolución debe ser genuinamente recursiva).
- ❌ NO uses `Arrays.sort()` ni ninguna biblioteca que resuelva el problema.
- ❌ NO agregues dependencias externas ni sistema de build.
- ❌ NO uses streams, lambdas ni sintaxis funcional: esto es material de
  cátedra de recursividad, la claridad del caso base manda.
- ❌ NO inventes requisitos. Si el enunciado es ambiguo, elegí lo más simple,
  **decilo en el informe final** y seguí.

## 5. ESTRUCTURA DE CADA ARCHIVO

```java
/* ... PROMPT INICIAL ... */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio N: <Título>
 *
 * ESTRATEGIA — por qué es recursivo
 * CASO BASE — cuál y por qué
 * CASO RECURSIVO — llamada y reducción
 * COMPLEJIDAD — temporal y espacial, con la ecuación de recurrencia
 * PILA DE LLAMADAS — qué se apila y por qué crece
 * CASOS LÍMITE — vacío, negativos, desborde
 */
public class EjercicioN {

    // === ALGORITMO ===
    // === DEMOSTRACIÓN (main) ===
    // === UTILIDADES ===

    // Instrumentación de la pila:
    //   private static int profundidad;        // contador de marcos activos
    //   private static int llamadasTotales;    // contador de invocaciones
}

/* ... AJUSTES DEL PROMPT ... */
```

### Cómo instrumentar la pila

Para cada función recursiva:

```java
private static int profundidad = 0;
private static int llamadasTotales = 0;

public static long f(int n) {
    llamadasTotales++;
    profundidad++;
    // ... caso base: return X;
    // ... caso recursivo: return f(n - 1);
    // finally: profundidad--;   <- en un try/finally para que baje siempre
}
```

Y en el `main`, imprimir el **máximo de profundidad alcanzado**. Eso convierte
el $\Theta(n)$ espacial en algo **observable**, que es lo que la cátedra quiere.

## 6. LOS 10 EJERCICIOS

### E1 — Factorial
- `$n! = n * (n-1)!$`. Caso base: `n <= 1` → `1` (tanto $0!$ como $1!$ valen 1).
- El prompt debe explicar que el factorial **se define en sí mismo**.
- Devuelve **`long`**: `int` desborda en $13! = 6\,227\,020\,800$.
- **Mostrá el desborde**: calculá $12!$ (entra en `int`) y $13!$ (no entra).
- Casos límite: `0` → 1, `1` → 1, negativo → mensaje.

### E2 — Suma de los primeros N números
- `suma(n) = n + suma(n-1)`. Caso base: `n <= 0` → `0`.
- El prompt debe explicar por qué `n + suma(n-1)` es una descomposición natural.
- **Validá contra la fórmula cerrada** `n*(n+1)/2` para varios `n`.
- Límite de `int`: $n \le 65\,535$.

### E3 — Multiplicación mediante sumas
- `a * b = a + a*(b-1)`. Caso base: `b == 0` → `0`.
- El prompt debe explicar que se reduce **el segundo parámetro** (`b`), porque es
  la cantidad de sumas repetidas.
- **SIN el operador `*`**.
- Probá conmutatividad: `a*b` debe igualar `b*a`.
- Casos límite: `b=0` → 0, `a=0` → 0.

### E4 — Potencia
- `a^n = a * a^(n-1)`. Caso base: `n == 0` → `1`.
- El prompt debe explicar que es multiplicación anidada.
- **SIN `Math.pow`**. Devuelve `long` (2³¹ desborda `int`).
- Casos límite: `n=0` → 1 (incluso `0^0`), `a=0` con `n>0` → 0, `a=1` → 1,
  exponente negativo → mensaje.

### E5 — Conteo regresivo
- Imprime `n, n-1, ..., 0`. Devuelve **`void`**: imprime, no retorna.
- Caso base: `n < 0` → no imprime nada y retorna.
- El prompt debe explicar qué significa que una función recursiva no devuelva valor.
- **CRÍTICO — demostración obligatoria**: implementá también una versión con
  `n + 1` que **nunca alcanza el caso base**, capturá el `StackOverflowError`
  con `try/catch` y explicá por qué la recursión se desborda.

### E6 — Contar dígitos
- `digitos(n) = 1 + digitos(n / 10)`. **Caso base: `n < 10` → `1`.**
- El prompt debe explicar que dividir entre 10 elimina el último dígito.
- $O(\log_{10} n)$, **mejor que lineal** — marcalo como contraste con E1-E5.
- Casos límite: `0` → **1** (el cero es un dígito), `7` → 1, `1000` → 4.

### E7 — Sumar dígitos
- `sumaDigitos(n) = (n % 10) + sumaDigitos(n / 10)`. **Caso base: `n == 0` → `0`.**
- El prompt debe explicar el uso de `%` para obtener el último dígito y `/` para reducir.
- **Ojo: el caso base es DISTINTO del de E6.** Acá el 0 no aporta nada a la suma;
  en E6 el 0 sí es un dígito. El prompt debe señalar esta diferencia.
- Validá contra el dígito de control de un ISBN: `1234` → `1+2+3+4 = 10`.

### E8 — Invertir una palabra
- Caso base: `""` → `""`, un carácter → ese carácter.
- `charAt(0)` = primer carácter; `substring(1)` = el resto.
- `invertir(s) = invertir(substring(1)) + charAt(0)` — el carácter se **antepone**.
- El prompt debe explicar cómo se reconstruye el resultado al **volver** de la
  recursión (las capas se desenrollan de la más profunda a la más superficial).
- Pruebas: `hola` → `aloh`, `reconocer`, `anilina`, `"a b"`.

### E9 — Palíndromo
- Caso base: `length() <= 1` → `true`. Si primero ≠ último → `false` inmediato.
- Reducción: `substring(1, length()-1)` — se eliminan **ambos** extremos.
- El prompt debe explicar por que un palíndromo se lee igual en ambos sentidos.
- Pruebas: `""` → true, `"a"` → true, `"ab"` → false, `"aba"` → true, `"anilina"` → true.
- Documentá si normalizás mayúsculas y espacios, y por qué.

### E10 — Buscar un elemento en un arreglo
- **SIN `for` ni `while`.** El índice avanza como **parámetro**: `i + 1`.
- **DOS casos base**: (a) encontró el valor → devuelve el índice; (b) llegó al
  final (`i >= length`) → devuelve `-1`.
- El prompt debe explicar que el índice es un parámetro, y por eso no hace
  falta un ciclo.
- Casos límite: vacío → `-1`, en posición 0, en la última, ausente, duplicados.

## 7. COMPARTE CON LA GUÍA 1

La guía anterior era casi toda iteración: $\Theta(1)$ de memoria. Acá es al
revés: **todo es $\Theta(n)$ temporal y $\Theta(n)$ espacial**, y el espacio se
va en la pila de llamadas, no en un arreglo auxiliar.

| | Guía 1 (iterativo) | Guía 2 (recursivo) |
|---|---|---|
| Temporal | $\Theta(n)$ | $\Theta(n)$ — igual |
| **Espacial** | $\Theta(1)$ | **$\Theta(n)$** — la pila |
| Desbordamiento | no aplica | `StackOverflowError` |

**Java NO optimiza la recursión de cola.** Aunque la última línea sea
`return f(n-1)`, cada llamada reserva su marco y la pila sigue creciendo a
$\Theta(n)$. Mencionalo donde aporte.

## 8. ORDEN DE TRABAJO

1. Leé `PLAN_GUIA_2.md` y `Ejercicios2.docx`.
2. **Redactá el prompt del ejercicio ANTES de escribir el algoritmo**, y ponelo
   en el archivo. Ese es el orden que pide la consigna.
3. Implementá los 10 en orden del 1 al 10.
4. Ejecutá `_build_check.ps1` y verificá.
5. Releé cada archivo: **¿el prompt describe el código que realmente escribí?**
6. Agregá el bloque de ajustes si algo cambió.

## 9. REVISIÓN FINAL OBLIGATORIA

- [ ] Existen los 10 archivos.
- [ ] **Cada uno empieza con `PROMPT INICIAL`** con las 7 secciones.
- [ ] **Cada uno termina con `AJUSTES DEL PROMPT`**.
- [ ] El prompt describe el algoritmo real (caso base, recursivo y reducción coinciden).
- [ ] Comentarios de caso base, caso recursivo **y pila de llamadas**.
- [ ] El `main` **muestra la profundidad máxima de la pila**.
- [ ] E3 sin `*`; E4 sin `Math.pow`; E10 sin `for`/`while`.
- [ ] E5 demuestra el `StackOverflowError` **capturado**, no sólo mencionado.
- [ ] E6 con `0` → 1 dígito; E7 con `0` → suma 0. **Distintos casos base.**
- [ ] E1 y E4 usan `long` y muestran el desborde de `int`.
- [ ] La complejidad del Javadoc coincide con el código: `T(n) = T(n-1) + Θ(1)`.
- [ ] Encoding UTF-8 en la salida (ver §10).
- [ ] No se agregaron dependencias ni archivos de build.

## 10. CODIFICACIÓN DE LA SALIDA

Todos los `main` arrancan con:

    System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

Sin esto la consola de Windows (CP850) convierte `Θ` y los acentos en bytes
ilegibles. Los símbolos van también en los comentarios y el Javadoc.

## 11. INFORME FINAL

### Archivos
| # | Archivo | Líneas | Reduce | Caso base | T | S |
|---|---------|--------|--------|-----------|---|---|
| 1 | Ejercicio2_1.java | N | n→n-1 | n<=1 → 1 | Θ(n) | Θ(n) |

### Verificación ejecutada
Qué corriste, qué salida dio, y si algún test falló. **No afirmes que funciona
algo que no ejecutaste.**

### Comandos
Los comandos exactos para que el usuario reproduzca la verificación.

### Decisiones y supuestos
- Cómo resolviste las ambigüedades del enunciado.
- Qué casos base elegiste y por qué (sobre todo E6 vs E7).
- Si modificaste algún prompt y por qué.

### Incumplimientos o dudas
Si algo no pudiste hacer o el enunciado era contradictorio, decilo acá.
No lo ocultes ni lo resuelvas silenciosamente.

## 12. EMPEZÁ

No me preguntes qué hacer: el enunciado está completo arriba y el plan técnico
tiene el detalle. Arrancá por el Ejercicio 1 **redactando primero su prompt**,
luego el algoritmo, y seguí. Al terminar corré los tests y reportá.
```