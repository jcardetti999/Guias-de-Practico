# Los 10 prompts individuales — Guía 2

> **Por qué existe este archivo:** el enunciado de la cátedra pide, para cada
> ejercicio, construir un prompt específico. Acá están los 10, listos para pegar.
>
> Cada bloque es autónomo. **Son los prompts que van embebidos dentro de cada
> `.java`** — el enunciado lo exige explícitamente.
>
> **Alternativa:** para resolver los 10 de una sola vez, usá
> `PROMPT_AGENTE_PROGRAMADOR_2.md`.

**Contexto común** (ya incluido en cada prompt): Java 21 · default package ·
archivo `EjercicioN.java` · clase `EjercicioN` · sin dependencias · el prompt va
como comentario al inicio del archivo · bloque de ajustes al final · UTF-8 en la
salida · comentarios de caso base, caso recursivo y pila de llamadas ·
instrumentación que muestre la profundidad de la pila.

---

## E1 — Factorial

Quiero implementar en Java una función recursiva que calcule el factorial de un
número entero no negativo.

Este problema puede resolverse recursivamente porque **el factorial se define
en términos de sí mismo**: $n!$ no es un artificio, es $n \cdot (n-1)!$. Es
literalmente la definición matemática del número, así que la estructura de la
definición ES la estructura de la solución.

El caso base es `n <= 1`, que devuelve `1`, porque por convenio $0! = 1$ y
$1! = 1$. Sin ese caso base la función no terminaría nunca: siempre llamaría a
`factorial(n - 1)` con un número menor, sin un punto donde frenar.

El caso recursivo es `factorial(n) = n * factorial(n - 1)`, porque en cada
llamada el problema se reduce en uno: pasamos de $n!$ a $(n{-}1)!$.

La función debe devolver el factorial como **`long`**, no como `int`: el factorial
crece muy rápido y $13! = 6\,227\,020\,800$ ya supera el máximo de `int`
($2^{31}-1 = 2\,147\,483\,647$).

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo y la pila de llamadas (por qué la profundidad crece hasta $n$).

También quiero probarlo con estos casos:
- $0! = 1$ y $1! = 1$ (los dos casos base)
- $5! = 120$ (caso típico)
- $12! = 479\,001\,600$ — el último factorial que entra en `int`
- $13! = 6\,227\,020\,800$ — el primero que desborda `int` pero entra en `long`
- $20!$ — el máximo que entra en `long`
- un número negativo → debe informar que no es válido, sin lanzar excepción

---

## E2 — Suma de los primeros N números

Quiero implementar en Java una función recursiva que sume los números desde `n`
hasta 1.

Este problema puede resolverse recursivamente porque la **suma de los primeros
`n` números es la última suma más la suma de los primeros `n-1`**. Separar el
elemento más grande del resto es una descomposición natural, no forzada.

El caso base es `n <= 0`, que devuelve `0`, porque no hay números que sumar. Es
el caso más simple posible y es el único que no depende de `n`.

El caso recursivo es `suma(n) = n + suma(n - 1)`, porque en cada llamada el
problema se reduce: de sumar `n` números pasamos a sumar `n-1`.

La función debe devolver la suma como `int`.

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo y la pila de llamadas.

También quiero probarlo con estos casos:
- `suma(0) = 0` y `suma(1) = 1` (casos base)
- `suma(4) = 10` (1+2+3+4, caso típico)
- `suma(10) = 55`
- **validación contra la fórmula cerrada** `n*(n+1)/2` para varios `n`: si el
  resultado recursivo coincide con la fórmula, el algoritmo es correcto
- el límite de `int`: con $n = 65\,535$ la suma es $2\,147\,450\,880$, el mayor
  valor representable; con $n$ mayor hay que advertir del desborde

---

## E3 — Multiplicación mediante sumas

Quiero implementar en Java una función recursiva que calcule `a * b` **sin usar
el operador `*`**.

Este problema puede resolverse recursivamente porque **multiplicar por `b` es
sumar `a` repetidas `b` veces**: $a \cdot b = a + \underbrace{a + \dots + a}_{b-1
\text{ veces}}$. Es la definición misma de la multiplicación como suma repetida.

El caso base es `b == 0`, que devuelve `0`, porque sumar `a` cero veces da `0`
(la suma vacía es el elemento neutro de la suma).

El caso recursivo es `a * b = a + a * (b - 1)`, porque en cada llamada **se
reduce el segundo parámetro**: `b` es la cantidad de sumas que faltan, y por eso
es el que se descuenta. El primer parámetro `a` **no se reduce**: es la magnitud
que se repite.

La función debe devolver `int`.

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo, **por qué se reduce `b` y no `a`**, y la pila de llamadas.

También quiero probarlo con estos casos:
- `b = 0` → `0` (caso base, suma vacía)
- `a = 0` → `0` (el otro factor en cero)
- `3 * 4 = 12` (caso típico)
- **comprobación de conmutatividad**: `a * b` debe dar lo mismo que `b * a`
- `1 * 7 = 7` y `7 * 1 = 7` (extremos)

---

## E4 — Potencia

Quiero implementar en Java una función recursiva que calcule `a^n` **sin usar
`Math.pow`**.

Este problema puede resolverse recursivamente porque **una potencia es
multiplicación anidada**: $a^n$ es $a$ multiplicado por sí mismo $n$ veces, y
al quitarle una multiplicación queda exactamente $a^{n-1}$.

El caso base es `n == 0`, que devuelve `1`, porque $a^0 = 1$ por definición:
elevar cualquier número a la potencia cero da uno (incluso $0^0$, por convenio
matemático).

El caso recursivo es `potencia(a, n) = a * potencia(a, n - 1)`, porque en cada
llamada **se reduce el exponente en uno**, no la base.

La función debe devolver `long`, porque $2^{31}$ ya desborda `int`.

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo, por qué el caso base es exponente 0 (y no base 0), y la pila de
llamadas.

También quiero probarlo con estos casos:
- `a^0 = 1` para cualquier `a`, incluido `0^0 = 1` (caso base)
- `0^n = 0` con `n > 0`
- `1^n = 1` con cualquier `n`
- `2^10 = 1024` (caso típico)
- `2^31` — el primero que desborda `int` pero entra en `long`
- exponente negativo → debe informar que no es válido

---

## E5 — Conteo regresivo

Quiero implementar en Java una función recursiva que **imprima** un conteo
regresivo desde `n` hasta `0`.

Este problema puede resolverse recursivamente porque cada paso del conteo es
"imprimir el número actual y después hacer lo mismo con el siguiente". No hace
falta ninguna variable que acumule el estado: el estado está en el número que se
pasa como parámetro.

El caso base es `n < 0`, que simplemente **no imprime nada y retorna**, porque ya
se llegó a 0 y no hay nada más que mostrar.

El caso recursivo es `imprimir(n); descendente(n - 1);`, porque en cada llamada
el número se acerca en uno al caso base.

La función debe devolver **`void`**: no devuelve un valor, **produce un efecto**
(imprime en pantalla). Es la diferencia con los demás ejercicios, y vale la pena
explicarla: la recursión no obliga a devolver nada; sólo obliga a que cada
llamada se acerque al caso base.

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo y la pila de llamadas.

**Y quiero una demostración importante:** implementá también una versión que en
lugar de llamar con `n - 1` llame con `n + 1`. Esa versión **nunca alcanza el
caso base** (el caso base pide `n < 0`, pero desde un número negativo `n + 1`
vuelve a ser no negativo), así que la pila crece sin límite hasta producir un
`StackOverflowError`. Capturalo con `try/catch` y explicá en un comentario **por
qué la recursión se desborda**: no es un error del lenguaje, es la consecuencia
de que no exista ningún camino que llegue al caso base.

También quiero probarlo con estos casos:
- `n = 0` → debe imprimir sólo `0`
- `n = 3` → `3, 2, 1, 0`
- `n` negativo → no imprime nada
- la versión con `n + 1` → debe mostrar el `StackOverflowError` capturado

---

## E6 — Contar dígitos

Quiero implementar en Java una función recursiva que cuente cuántos dígitos
tiene un número entero positivo.

Este problema puede resolverse recursivamente porque **dividir un número entre 10
le elimina su último dígito**: `1234 / 10 = 123`, que tiene un dígito menos. El
problema se reduce solo con una división entera.

El caso base es `n < 10`, que devuelve `1`, porque cualquier número menor que 10
tiene exactamente un dígito (incluido el `0`, que se cuenta como un dígito).

El caso recursivo es `digitos(n) = 1 + digitos(n / 10)`, porque en cada llamada
se reduce el número en un dígito, y el `1 +` va contando los dígitos que se van
quitando.

La función debe devolver `int`.

Quiero que el código tenga comentarios explicando el caso base (y por qué es
`n < 10` y no `n == 0`), el caso recursivo y la pila de llamadas. **Notá que
esta función es $O(\log_{10} n)$**, o sea **mejor que lineal**: cada llamada
reduce el número a un dígito menos. Es un buen contraste con los ejercicios 1 a
5, que eran $\Theta(n)$.

También quiero probarlo con estos casos:
- `0` → **1** (el cero es un dígito)
- `7` → 1 (caso base)
- `10` → 2
- `1234` → 4 (caso típico)
- `1000` → 4 (tiene ceros al final, que también son dígitos)
- `999999999` → 9 (el máximo de un `int` positivo)
- número negativo → debe informar que no es válido

---

## E7 — Sumar dígitos

Quiero implementar en Java una función recursiva que sume todos los dígitos de
un número entero positivo.

Este problema puede resolverse recursivamente porque **cada número se descompone
en su último dígito más el resto**: el último dígito se obtiene con `n % 10` y
el resto con `n / 10`. Esas dos operaciones, juntas, hacen que el problema se
reduzca en un dígito.

El caso base es `n == 0`, que devuelve `0`, porque cuando ya no queda ningún
dígito por sumar, no hay nada que agregar.

El caso recursivo es `sumarDigitos(n) = (n % 10) + sumarDigitos(n / 10)`, porque
en cada llamada se separa el último dígito (con módulo) y se sigue con el
número reducido (con división entera), y los resultados se **combinan** al
volver de la recursión sumando el dígito extraído.

La función debe devolver `int`.

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo y la pila de llamadas.

**Un detalle importante que quiero que el prompt destaque:** el caso base de este
ejercicio es `n == 0` → `0`, y es **distinto** del caso base del ejercicio de
contar dígitos, que era `n < 10` → `1`. La diferencia parece mínima pero no lo
es: al **contar**, el `0` es un dígito y vale `1`; al **sumar**, el `0` no aporta
nada y vale `0`.

También quiero probarlo con estos casos:
- `0` → **0** (a diferencia del ejercicio anterior, que daba 1)
- `7` → 7 (un solo dígito)
- `10` → 1 (1+0)
- `1234` → **10** (1+2+3+4, caso típico)
- `999` → 27 (el máximo: 9+9+9)
- validar el resultado contra el **dígito de control de un ISBN**, que es
  exactamente esta suma
- número negativo → debe informar que no es válido

---

## E8 — Invertir una palabra

Quiero implementar en Java una función recursiva que invierta un `String`.

Este problema puede resolverse recursivamente porque **invertir una cadena es
invertir el resto y anteponer el primer carácter al frente**: el carácter que
queda al principio de la cadena original termina al final de la invertida.

El caso base es `length() <= 1` (cadena vacía o un solo carácter), que devuelve
la cadena tal cual, porque una cadena de 0 o 1 caracteres ya está invertida.

El caso recursivo es `invertir(s) = invertir(s.substring(1)) + s.charAt(0)`,
porque en cada llamada **se reduce la cadena en un carácter**: se separa el
primero (`charAt(0)`) del resto (`substring(1)`), y se sigue con el resto.

La función debe devolver `String`.

Quiero que el código tenga comentarios explicando el caso base, el caso
recursivo, y **cómo se reconstruye el resultado al volver de la recursión**:
cada llamada sólo tiene un carácter, pero al desenrollarse la pila (de la
llamada más profunda a la más superficial) cada capa **antepone** su carácter al
resultado que le devolvió la capa inferior.

También quiero probarlo con estos casos:
- `""` → `""` (cadena vacía)
- `"a"` → `"a"` (un solo carácter)
- `"ab"` → `"ba"` (dos caracteres)
- `"hola"` → `"aloh"` (caso típico)
- `"reconocer"` → `"reconocer"` (palindromo, queda igual)
- `"anilina"` → `"anilina"`
- `"a b"` → `"b a"` (con espacio, que también es un carácter y debe conservarse)
- `"Hola Mundo"` → comprobar que se invierte incluyendo mayúsculas y espacios

---

## E9 — Palíndromo

Quiero implementar en Java una función recursiva que determine si una palabra es
palíndromo.

Este problema puede resolverse recursivamente porque **un palíndromo se lee igual
en ambos sentidos**: eso obliga a que el primer carácter sea igual al último, y
esa condición se cumple para los caracteres del medio con el mismo criterio. La
propiedad es "simétrica", y eso la hace naturalmente recursiva sobre los
extremos.

El caso base es `length() <= 1`, que devuelve `true`, porque una cadena vacía o
de un solo carácter se lee igual en ambos sentidos.

Si el **primer carácter y el último son distintos**, se devuelve `false`
**inmediatamente**, sin seguir comparando: ese es un caso base de rechazo, y es
correcto porque un solo par distinto ya descarta que toda la cadena sea
palíndromo.

El caso recursivo es `esPalindromo(s) = esPalindromo(s.substring(1, s.length() - 1))`,
porque en cada llamada **se eliminan los dos extremos** y el problema se reduce
en dos caracteres.

La función debe devolver `boolean`.

Quiero que el código tenga comentarios explicando el caso base, el caso de
rechazo temprano, el caso recursivo y la pila de llamadas. También quiero que
comente que esta recursión usa $\Theta(n/2)$ profundidad, no $\Theta(n)$.

También quiero probarlo con estos casos:
- `""` → `true` (caso base, cadena vacía)
- `"a"` → `true` (caso base, un carácter)
- `"ab"` → `false` (se rechaza en el primer par)
- `"ana"` → `true` (palíndromo de longitud impar)
- `"anilina"` → `true` (palíndromo de longitud impar)
- `"reconocer"` → `true` (palíndromo de longitud par)
- `"hola"` → `false`
- `"Ana"` → documentar si se normalizan mayúsculas, y por qué

---

## E10 — Buscar un elemento en un arreglo

Quiero implementar en Java una función recursiva que busque un número dentro de
un arreglo de enteros **sin usar `for` ni `while`**.

Este problema puede resolverse recursivamente porque **la posición actual del
barrido puede ser un parámetro**. Si en vez de una variable que se incrementa
dentro de un ciclo, la posición es un argumento de la función, entonces cada
llamada puede examinar "la siguiente posición" sin necesidad de ninguna
estructura de control iterativa.

El caso base (de éxito) es `vector[indice] == valorBuscado`, que devuelve
`indice`: se encontró el elemento y no hay nada más que hacer.

El caso base (de fracaso) es `indice >= vector.length`, que devuelve `-1`: se
recorrieron todas las posiciones disponibles y el elemento no existe. **Son dos
casos base distintos**, y los dos son necesarios: sin el primero nunca se
encontraría nada, y sin el segundo la función no terminaría nunca.

El caso recursivo es `buscar(vector, valor, indice + 1)`, porque en cada llamada
el índice **avanza una posición**, que es exactamente lo que haría el ciclo.

La función debe devolver `int`: el índice del elemento si lo encuentra, o `-1`
si no existe.

Quiero que el código tenga comentarios explicando los dos casos base, el caso
recursivo, y la pila de llamadas. Quiero que explique explícitamente **por qué
el índice es un parámetro y no una variable**: ése es el punto del ejercicio.

También quiero probarlo con estos casos:
- arreglo vacío → `-1` (llega directo al caso base de fracaso)
- el valor está en la posición `0` → devuelve `0` en una llamada
- el valor está en la última posición → devuelve el último índice
- el valor **no existe** → devuelve `-1` tras recorrer todo el arreglo
- arreglo con duplicados → debe devolver la **primera** posición, no otra
- un arreglo de 10.000 elementos → para ver que la pila crece a $\Theta(n)$
