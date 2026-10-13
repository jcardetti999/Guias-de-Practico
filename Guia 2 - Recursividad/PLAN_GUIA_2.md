# Plan de resolución — Guía 2: Recursividad

> Documento de **plan técnico**. No es código.
> Consumido por el agente `Programmer` (`.github/agents/programmer.agent.md`).
> Fuente: `Ejercicios2.docx` (10 ejercicios + consigna general).

---

## 1. Objetivo

Resolver los 10 ejercicios de recursividad en Java, donde **el prompt de
ingeniería es parte de la entrega y está dentro del propio archivo `.java`**.

La consigna general es explícita en tres puntos que cambian la forma del trabajo
respecto de la Guía 1:

1. **Antes de pedir código**, hay que analizar el problema y redactar un prompt
   que describa el caso base, el caso recursivo y cómo se reduce el problema.
2. **Cada `.java` debe empezar con el prompt inicial usado, como comentario.**
3. **Si el prompt se modificó durante el desarrollo**, hay que agregar al final
   del archivo un comentario con qué se cambió y por qué.
4. Se evalúa *"tanto el correcto funcionamiento del programa como la calidad del
   razonamiento reflejado en el prompt"*.

> Consecuencia: el prompt **no es un paso previo que se descarta**. Es un
> artefacto versionado dentro del código, y debe leerse junto al algoritmo.

---

## 2. Estado actual

| Ruta | Estado |
|------|--------|
| `Guia 2 - Recursividad/Ejercicios2.docx` | Enunciados (material de cátedra) |
| `Guia 2 - Recursividad/Ejercicio2_1.java` … `Ejercicio2_10.java` | **No existen** |
| `Guia 2 - Recursividad/` | Sin código, sin build, sin tests |

**No hay** sistema de build, ni JUnit, ni dependencias. Se compila con `javac`
directo, igual que la Guía 1.

### El agente sí tiene terminal

`programmer.agent.md` quedó con `bash` en su lista de `tools:`. El agente
**puede** compilar y ejecutar: los tests se corren y se reportan, no se delegan.
En la Guía 1 esto costó 6 idas y vueltas porque asumí lo contrario sin
verificarlo. **No volver a afirmar "no puedo ejecutar" sin comprobar.**

---

## 3. Requisitos nuevos de la Guía 2 (no existen en la Guía 1)

| Requisito | Implementación |
|-----------|----------------|
| El prompt va embebido arriba del `.java` | Bloque `/* PROMPT INICIAL ... */` antes del Javadoc de clase |
| Log de cambios del prompt | Bloque `/* AJUSTES DEL PROMPT ... */` al final del archivo |
| Formato de prompt explícito | La cátedra da una plantilla de 7 secciones (ver §5) |
| Se evalúa el razonamiento del prompt | El prompt debe explicar el *porqué*, no sólo el *qué* |
| Mencionar **la pila de llamadas** | La plantilla pide comentarios sobre "la pila de llamadas" |

---

## 4. Conflictos con las reglas del agente

| Regla de `programmer.agent.md` | Requisito de la Guía 2 | Resolución |
|-------------------------------|------------------------|------------|
| *"No agregues comentarios obvios"* | Se exige prompt embebido + comentarios de caso base, caso recursivo y pila | **Prevalece la guía.** Se autoriza y requiere documentar caso base, caso recursivo, reducción y pila. Prohibido comentar lo autoevidente. |
| *"Toda funcionalidad debería tener tests"* | No hay JUnit | Sustituido por `main` de auto-demostración que incluye la **traza de la pila de llamadas**. |
| *"No inventes lo que no se pide"* | La consigna pide formato de prompt | El prompt se redacta, pero debe ser **fiel al algoritmo implementado**: si divergen, es un defecto. |

---

## 5. Plantilla obligatoria de cada archivo

```java
/* ============================================================================
 * PROMPT INICIAL (redactado antes de pedir la implementación)
 * ============================================================================
 * Quiero implementar en Java una función recursiva que [problema].
 *
 * Este problema puede resolverse recursivamente porque [explicación].
 *
 * El caso base es [condición], porque [motivo].
 *
 * El caso recursivo es [llamada], porque en cada llamada [cómo se reduce].
 *
 * La función debe devolver [resultado esperado].
 *
 * Quiero que el código tenga comentarios explicando el caso base, el caso
 * recursivo y la pila de llamadas.
 *
 * También quiero probarlo con estos casos:
 *   - [caso 1]
 *   - [caso 2]
 *   - [caso 3]
 * ========================================================================== */

/**
 * GUÍA 2 — RECURSIVIDAD
 * Ejercicio N: <Título>
 * ...complejidad, precondiciones, casos límite...
 */
public class EjercicioN { ... }

// ...al final del archivo...

/* ============================================================================
 * AJUSTES DEL PROMPT (qué se cambió durante el desarrollo y por qué)
 * ============================================================================
 * - [cambio]: [motivo]
 * ========================================================================== */
```

> El bloque de ajustes **puede decir "no hubo cambios"**. La consigna pide
> registrar los cambios *si* los hubo; declarar explícitamente que no los hubo
> también cumple y es más honesto que omitir el bloque.

---

## 6. Concepto puente con la Guía 1

La guía anteriorديق apenas se preocupaba por el **espacio**: casi todo era
$\Theta(1)$. La guía de recursividad invierte eso: casi todo será $\Theta(n)$
espacial, y **no por un descuido del algoritmo, sino porque la pila de llamadas
crece**.

| | Guía 1 (iterativo) | Guía 2 (recursivo) |
|---|---|---|
| Temporal | $\Theta(n)$ | $\Theta(n)$ — idéntico |
| **Espacial** | $\Theta(1)$ | **$\Theta(n)$** — la pila |
| Desbordamiento | no aplica | `StackOverflowError` |

Toda recursión lineal cumple $T(n) = T(n-1) + \Theta(1) \Rightarrow T(n)=\Theta(n)$,
y usa $\Theta(n)$ marcos de pila: uno por llamada pendiente de retorno.

**Nota sobre Java:** el lenguaje **no optimiza la recursión de cola**
(*Tail call optimization*. Una función `f(n)` cuya última operación es
`return f(n-1)` sigue reservando un marco por llamada y sigue consumiendo
$\Theta(n)$ de pila. Sirve para compartir el análisis de complejidad, no para
ahorrar memoria.

---

## 7. Especificación por ejercicio

### E1 — Factorial

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_1.java` (crear) |
| Definición | $n! = n \cdot (n-1)!$ |
| Por qué recursivo | El factorial **se define en términos de sí mismo**: es la definición misma del número, no un artificio |
| Caso base | $n \le 1 \Rightarrow 1$. Convenciones: $0! = 1$ y $1! = 1$ |
| Reducción | $n \to n-1$ |
| Devuelve | `long` |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| **Desborde** | `int` corta en $13! = 6\,227\,020\,800 > 2^{31}-1$. Con `long`, hasta $20!$. **Probar el límite.** |
| Casos límite | $n=0$ → 1 · $n=1$ → 1 · $n$ negativo → mensaje, no excepción |

### E2 — Suma de los primeros N números

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_2.java` |
| Definición | $\text{suma}(n) = n + \text{suma}(n-1)$ |
| Caso base | $n \le 0 \Rightarrow 0$ |
| Reducción | $n \to n-1$ |
| Devuelve | `int` |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| Validación | Comparar contra la fórmula cerrada $n(n+1)/2$ para varios $n$ |
| Casos límite | $n=0$ → 0 · $n=1$ → 1 · límite de `int`: $n(n{+}1)/2 \le 2^{31}{-}1$ ⇒ $n \le 65\,535$ |

### E3 — Multiplicación mediante sumas

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_3.java` |
| Definición | $a \cdot b = a + \underbrace{a + \dots + a}_{b-1}$ |
| **Qué se reduce** | **El segundo parámetro** $b$ (la cantidad de sumas), no $a$ |
| Caso base | $b = 0 \Rightarrow 0$ (suma vacía) |
| Reducción | $a \cdot b = a + a \cdot (b-1)$ |
| Devuelve | `int` |
| T / S | $\Theta(b)$ / $\Theta(b)$ — depende de $b$, no de $a$ |
| Prohibición | **No usar el operador `*`** en el algoritmo |
| Casos límite | $b=0$ → 0 · $a=0$ → 0 · conmutatividad $a\cdot b = b\cdot a$ |

### E4 — Potencia

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_4.java` |
| Definición | $a^n = a \cdot a^{n-1}$ |
| Por qué recursivo | Es multiplicación **anidada**: $a^n$ es $a$ multiplicado por sí mismo $n$ veces |
| Caso base | $n = 0 \Rightarrow 1$ ($a^0 = 1$, incluso para $a=0$ por convenio) |
| Reducción | $n \to n-1$ |
| Devuelve | `long` |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| Prohibición | **No usar `Math.pow`** |
| Desborde | $2^{31}$ desborda `int`. Con `long` hasta $2^{63}$ |
| Casos límite | $n=0$ → 1 · $a=0,n>0$ → 0 · $a=1$ → 1 · $n$ negativo → mensaje |

### E5 — Conteo regresivo

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_5.java` |
| Objetivo | Imprimir $n, n{-}1, \dots, 0$ |
| Devuelve | **`void`** — imprime, no retorna. Es la primera vez que la recursión no devuelve valor |
| Caso base | $n < 0 \Rightarrow$ no imprime nada y retorna |
| Reducción | Imprimir $n$, luego $\text{descender}(n-1)$ |
| **Si se usara `n + 1`** | La recursión **nunca alcanza el caso base**: el caso base exige $n<0$, y desde $n<0$ se iría a $n+1 \ge 0$. Se sale antes → **`StackOverflowError`**. Debe **demostrarse**, no sóloشرحdecirse |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| Casos límite | $n=0$ → imprime sólo `0` · $n$ negativo → no imprime nada |

### E6 — Contar dígitos

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_6.java` |
| Definición | $\text{digitos}(n) = 1 + \text{digitos}(\lfloor n/10 \rfloor)$ |
| Cómo reducir | División entera por 10: `n / 10` elimina el último dígito |
| **Caso base** | $n < 10 \Rightarrow 1$ (un número de una sola cifra, incluido el 0) |
| Devuelve | `int` |
| T / S | $\Theta(\log_{10} n)$ / $\Theta(\log_{10} n)$ — **¡menor que lineal!** |
| Casos límite | $0$ → 1 · $7$ → 1 · $10$ → 2 · $1000$ → 4 · negativo → mensaje |
| Contraste | El **último** dígito se obtiene con `n % 10`, que es justo el Ejercicio 7 |

### E7 — Sumar dígitos

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_7.java` |
| Cómo obtener el último | `n % 10` (módulo) |
| Cómo reducir | `n / 10` (división entera) |
| **Caso base** | $n = 0 \Rightarrow 0$ — **distinto del E6**, que usa $n<10$. Aquí el 0 no aporta nada a la suma |
| Definición | $\text{sumaDigitos}(n) = (n \bmod 10) + \text{sumaDigitos}(\lfloor n/10 \rfloor)$ |
| Devuelve | `int` |
| T / S | $\Theta(\log_{10} n)$ / $\Theta(\log_{10} n)$ |
| Casos límite | $0$ → 0 · $7$ → 7 · $1234$ → 10 · negativo → mensaje |
| Validación | Coincidir con el dígito de control de un ISBN o un RUT |

### E8 — Invertir una palabra

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_8.java` |
| Caso base | `""` → `""`; un solo carácter → ese mismo carácter |
| Separar | `charAt(0)` = primer carácter; `substring(1)` = el resto |
| Definición | $\text{inv}(s) = \text{inv}(\text{resto}(s)) + \text{primer}(s)$ |
| Reconstrucción | El carácter se **antepone** al resultado de la llamada recursiva |
| Devuelve | `String` |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| Casos límite | `""` · `"a"` · `"ab"` · mayúsculas y espacios se conservan |
| Palabras de prueba | `hola` → `aloh` · `reconocer` · `anilina` · `"a b"` |

### E9 — Palíndromo

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_9.java` |
| Por qué extremos | Un palíndromo se lee igual en ambos sentidos: el primer carácter debe igualar al último |
| Si difieren | **`false` inmediato** — es un caso base de rechazo |
| Reducción | Eliminar ambos extremos: `substring(1, length()-1)` |
| **Caso base** | `length() <= 1` → `true` (cero o un carácter siempre es palíndromo) |
| Devuelve | `boolean` |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| Casos límite | `""` → true · `"a"` → true · `"ab"` → false · `"aba"` → true · `"ana"` → true |
| Pruebas | 1 par, 1 impar, con espacios y mayúsculas (documentar si se normaliza) |

### E10 — Buscar un elemento en un arreglo (sin ciclos)

| Campo | Detalle |
|-------|---------|
| Archivo | `Ejercicio2_10.java` |
| Desde dónde arranca | Posición `0` |
| Cómo avanza sin `for`/`while` | El índice es un **parámetro** que crece: `buscar(vector, valor, i + 1)` |
| **Dos casos base** | (a) **encontro** → devuelve el índice; (b) **llegó al final** `i >= length` → devuelve `-1` |
| Devuelve | `int` (índice o `-1`) |
| T / S | $\Theta(n)$ / $\Theta(n)$ |
| Prohibición | **Ni `for` ni `while`** en el algoritmo |
| Casos límite | vacío → `-1` · elemento en 0 · en el último · ausente · duplicados (devuelve el primero) |

---

## 8. Matriz de trazabilidad

| # | Reduce | Caso base | T | S | Prohibición |
|---|--------|-----------|---|---|-------------|
| 1 | $n \to n{-}1$ | $n \le 1 \Rightarrow 1$ | $\Theta(n)$ | $\Theta(n)$ | — |
| 2 | $n \to n{-}1$ | $n \le 0 \Rightarrow 0$ | $\Theta(n)$ | $\Theta(n)$ | — |
| 3 | $b \to b{-}1$ | $b = 0 \Rightarrow 0$ | $\Theta(b)$ | $\Theta(b)$ | sin `*` |
| 4 | $n \to n{-}1$ | $n = 0 \Rightarrow 1$ | $\Theta(n)$ | $\Theta(n)$ | sin `Math.pow` |
| 5 | $n \to n{-}1$ | $n < 0 \Rightarrow$ nada | $\Theta(n)$ | $\Theta(n)$ | — |
| 6 | $n \to n/10$ | $n < 10 \Rightarrow 1$ | $\Theta(\log n)$ | $\Theta(\log n)$ | — |
| 7 | $n \to n/10$ | $n = 0 \Rightarrow 0$ | $\Theta(\log n)$ | $\Theta(\log n)$ | — |
| 8 | $|s| \to |s|{-}1$ | $|s| \le 1$ | $\Theta(n)$ | $\Theta(n)$ | — |
| 9 | $|s| \to |s|{-}2$ | $|s| \le 1 \Rightarrow$ `true` | $\Theta(n)$ | $\Theta(n)$ | — |
| 10 | $i \to i{+}1$ | 2 casos base (encontró / llegó al final) | $\Theta(n)$ | $\Theta(n)$ | sin `for`/`while` |

---

## 9. Casos límite transversales

1. **Desborde de tipo.** E1 usa `long` (13! desborda `int`), E4 usa `long`
   (2³¹ desborda `int`), E2 avisa el límite a $n \approx 65\,535$.
2. **`StackOverflowError`.** Demostrarlo en E5 con `n + 1`. Debe **capturarse**
   (`try/catch`) para que el programa no muera.
3. **Entrada inválida.** Negativos en E1, E4, E6, E7 → mensaje, no excepción.
4. **Cadena vacía.** E8, E9 → resultado correcto sin condición especial.
5. **Inicialización.** Los acumuladores arrancan en el valor identidad del
   operador: `1` para multiplicar (E1, E4), `0` para sumar (E2, E7).
6. **División entera.** E6 y E7 usan `/ 10` y `% 10` sobre `int` **positivos**;
   con negativos el `÷` de Java trunca hacia cero y el resultado es incorrecto.

---

## 10. Riesgos

| Riesgo | Mitigación |
|--------|------------|
| El prompt embebido **divergye** del código realmente implementado | Bloque de ajustes al final del archivo; el agente debe releer su código contra su prompt antes de cerrar |
| El bloque de ajustes se olvida | Requisito explícito en el prompt maestro y en el checklist |
| Usar iteración "auxiliar" para invertir o contar dígitos | El razonamiento debe ser genuinamente recursivo; E10 prohíbe explícitamente `for`/`while` |
| `StackOverflowError` tumba el programa y no se ve el fenómeno | Capturarlo y explicarlo en E5 |
| Confundir el caso base de E6 ($n<10 \Rightarrow 1$) con el de E7 ($n=0 \Rightarrow 0$) | Ambos documentados explícitamente; son el contraste pedagógico del par |
| Afirmar que Java optimiza recursión de cola | Documentar explícitamente que **no lo hace** |

---

## 11. Criterios de aceptación

- [ ] Existen los 10 archivos `Ejercicio2_1.java` … `Ejercicio2_10.java`.
- [ ] Cada uno compila con `javac -encoding UTF-8`.
- [ ] **Cada archivo empieza con el bloque `PROMPT INICIAL`** con las 7 secciones.
- [ ] **Cada archivo termina con el bloque `AJUSTES DEL PROMPT`** (puede decir que no hubo cambios).
- [ ] El prompt embebido **describe el algoritmo real**: caso base, caso recursivo y reducción coinciden con el código.
- [ ] Comentarios que explican caso base, caso recursivo **y pila de llamadas**.
- [ ] El `main` instrumenta y muestra la profundidad de la pila.
- [ ] E3 sin `*`; E4 sin `Math.pow`; E10 sin `for`/`while`.
- [ ] E5 demuestra el `StackOverflowError` capturado.
- [ ] E6 y E7 manejan el $0$ con bases distintas y lo explican.
- [ ] E1 y E4 usan `long` y muestran el desborde de `int`.
- [ ] La complejidad del Javadoc coincide con la del código.
- [ ] `_build_check.ps1` compila y ejecuta los 10 sin excepciones.

---

## 12. Ruta de verificación

El agente tiene terminal, así que los tests se corren y se reportan:

```powershell
cd "c:\Facu Things\2026\Estructura de Datos y Algoritmo\Guias de Practico\Guia 2 - Recursividad"
powershell -ExecutionPolicy Bypass -File .\_build_check.ps1
```

Comprobaciones específicas que el script debe dejar visibles:

| Ejercicio | Verificación |
|---|---|
| E1 | `13!` desborda `int` pero entra en `long` |
| E3 | Commutatividad: $a\cdot b = b\cdot a$ |
| E5 | La versión con `n + 1` produce `StackOverflowError` **capturado** |
| E6 vs E7 | `0` → 1 dígito, pero suma de dígitos = 0 |
| E7 | `1234` → 10 |
| E8 | `hola` → `aloh` |
| E9 | `anilina` → true, `hola` → false |
| E10 | Búsqueda en arreglo sin ciclos |

> El `_build_check.ps1` de la Guía 1 se puede reutilizar casi tal cual: cambia
> la carpeta y los casos de `stdin`. La entrada de los interactivos debe ir por
> **archivo temporal con redirección `<`**, no por pipeline — el pipeline depende
> de `$OutputEncoding` y antepone BOM, lo que rompe el `Scanner`.
