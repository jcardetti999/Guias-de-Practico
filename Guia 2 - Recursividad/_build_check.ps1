param(
    # Borra las clases generadas y sale, sin compilar ni ejecutar.
    [switch]$Clean,
    # Compila pero no ejecuta (util para revisar errores de compilacion).
    [switch]$NoRun
)

<#
    Guia 2 - Recursividad
    Compila y ejecuta los 10 ejercicios.

    Puede estar en la raiz del repo o dentro de la carpeta de la guia: el script
    resuelve la ubicacion por si misma.

    Uso:
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1"     compila y ejecuta
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1" -Clean
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1" -NoRun

    Los ejercicios 1, 2, 4, 5, 6 y 7 son interactivos (leen por Scanner), asi
    que este script les provee la entrada por stdin para que la demo corra
    completa.
#>

# NO se usa $ErrorActionPreference = 'Stop' a nivel global: los comandos nativos
# de Java escriben la version en stderr, y con 'Stop' + 2>&1 PowerShell lo
# convierte en error terminante aunque el comando haya funcionado.
$ErrorActionPreference = 'Continue'

# Encoding de la salida. Son DOS controles distintos y hacen falta los dos:
#   chcp 65001                  -> cambia la codificacion de la consola real.
#   [Console]::OutputEncoding   -> es lo que PowerShell USA PARA DECODIFICAR la
#                                  salida de un proceso nativo. Sin esto, Java
#                                  escribe UTF-8 y PowerShell lo lee como CP850,
#                                  y "Solucion" sale como "Soluci├│n".
# $OutputEncoding es la codificacion con la que PowerShell ENVIA texto al
# stdin de los procesos nativos (los ejercicios interactivos leen de ahi).
# OJO: tiene que ser UTF-8 SIN BOM. Con [System.Text.Encoding]::UTF8, que si
# emite BOM, java recibe EF BB BF al inicio del flujo, el Scanner lee "\uFEFF3"
# en lugar de "3" y revienta con InputMismatchException.
try { chcp 65001 | Out-Null } catch { }

# Cada asignacion va en su propio try. Si Console::OutputEncoding lanzara
# (puede pasar en algunas consolas), un try compartido abortaria ANTES de
# llegar a $OutputEncoding, y la entrada enviaria BOM: el Scanner leeria
# "\uFEFFhola" en vez de "hola".
try {
    [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding($false)
} catch { }

# $OutputEncoding es como PowerShell ENVIA texto al stdin de los procesos
# nativos. Debe ser UTF-8 SIN BOM: con BOM, el Scanner de java lee "\uFEFF5"
# en lugar de "5" y falla con InputMismatchException.
try {
    $OutputEncoding = New-Object System.Text.UTF8Encoding($false)
} catch { }

# ============================================================
#  1. UBICAR LA CARPETA DE LA GUIA
# ============================================================
# Este script vive DENTRO de la carpeta de la guia, asi que $PSScriptRoot ya es
# la respuesta. Solo si el usuario lo movio a la raiz del repo hay que buscar.
# NO se hardcodea el nombre de la guia: se busca una carpeta hermana que tenga
# el mismo nombre que la carpeta donde vive este script.
$guia = $PSScriptRoot
$numeroGuia = [regex]::Match((Split-Path $guia -Leaf), '^Guia\s+(\d+)').Groups[1].Value

# La guia esta identificada por su .docx de enunciados (siempre presente).
$tieneEnunciado = { param($ruta) Test-Path (Join-Path $ruta '*.docx') }

if (-not (Get-ChildItem -Path $guia -Filter '*.docx' -ErrorAction SilentlyContinue)) {
    $nombreCarpeta = Split-Path $guia -Leaf
    $padre = Split-Path $guia -Parent
    $hermana = Join-Path $padre $nombreCarpeta
    if ((Get-ChildItem -Path $hermana -Filter '*.docx' -ErrorAction SilentlyContinue)) {
        $guia = $hermana
    }
}

if (-not (Get-ChildItem -Path $guia -Filter "Ejercicio${numeroGuia}_1.java" -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: no se encuentra Ejercicio${numeroGuia}_1.java en: $guia" -ForegroundColor Red
    Write-Host "  Ese archivo lo genera el agente Programmer al implementar la guia." -ForegroundColor Yellow
    Write-Host "  Si la guia ya esta implementada, revisa que el script este dentro de su carpeta." -ForegroundColor Yellow
    exit 1
}
Set-Location $guia

# Con -Clean no hace falta Java: solo borrar las clases y salir.
if ($Clean) {
    $clases = Get-ChildItem -Path $guia -Filter '*.class' -ErrorAction SilentlyContinue
    if ($clases) {
        $clases | Remove-Item -Force
        Write-Host "Borradas $($clases.Count) clases de: $guia" -ForegroundColor Green
    } else {
        Write-Host "No hay clases compiladas en: $guia" -ForegroundColor Green
    }
    exit 0
}

# ============================================================
#  2. LOCALIZAR EL JDK
# ============================================================
# Busca java/javac en este orden: override manual, JAVA_HOME, PATH, y por ultimo
# las carpetas de instalacion habituales de Windows.
function Resolve-JavaExe {
    param([string]$Nombre)   # 'java' o 'javac'

    $raiz = $Nombre -replace 'javac$', 'java'
    $directorio = if ($raiz -eq 'java') { 'bin' } else { 'bin' }

    # (a) Override manual
    $override = [Environment]::GetEnvironmentVariable("JAVAEXE_$($Nombre.ToUpper())")
    if ($override -and (Test-Path $override)) { return $override }

    # (b) JAVA_HOME
    if ($env:JAVA_HOME) {
        $candidato = Join-Path (Join-Path $env:JAVA_HOME $directorio) "$Nombre.exe"
        if (Test-Path $candidato) { return $candidato }
    }

    # (c) PATH
    $enPath = Get-Command $Nombre -ErrorAction SilentlyContinue
    if ($enPath) { return $enPath.Source }

    # (d) Carpetas de instalacion habituales
    $patrones = @(
        "$env:ProgramFiles\Java\*\bin\$Nombre.exe",
        "${env:ProgramFiles(x86)}\Java\*\bin\$Nombre.exe",
        "$env:ProgramFiles\Eclipse Adoptium\*\bin\$Nombre.exe",
        "$env:ProgramFiles\Microsoft\*\bin\$Nombre.exe",
        "$env:ProgramFiles\Amazon Corretto\*\bin\$Nombre.exe",
        "$env:ProgramFiles\Zulu\*\bin\$Nombre.exe",
        "$env:ProgramFiles\BellSoft\*\bin\$Nombre.exe",
        "$env:LOCALAPPDATA\Programs\*\bin\$Nombre.exe"
    )
    foreach ($patron in $patrones) {
        $encontro = Get-ChildItem -Path $patron -ErrorAction SilentlyContinue |
                    Sort-Object FullName -Descending | Select-Object -First 1
        if ($encontro) { return $encontro.FullName }
    }

    return $null
}

$javaExe  = Resolve-JavaExe -Nombre 'java'
$javacExe = Resolve-JavaExe -Nombre 'javac'

Write-Host ''
Write-Host '=== ENTORNO ===' -ForegroundColor Cyan

if (-not $javaExe -or -not $javacExe) {
    Write-Host '  ERROR: no se encontro un JDK completo.' -ForegroundColor Red
    if (-not $javaExe)  { Write-Host '    - falta java'  -ForegroundColor Red }
    if (-not $javacExe) { Write-Host '    - falta javac' -ForegroundColor Red }
    Write-Host ''
    Write-Host '  Diagnostico: ejecuta esto para ver si Java esta en el PATH:' -ForegroundColor Yellow
    Write-Host '      where.exe java'
    Write-Host '      where.exe javac'
    Write-Host '      java -version'
    Write-Host ''
    Write-Host '  Si no aparece nada, instala un JDK 21:' -ForegroundColor Yellow
    Write-Host '      winget install --id EclipseAdoptium.Temurin.21.JDK -e'
    Write-Host ''
    Write-Host '  O, si ya lo tenes en otra carpeta, define JAVA_HOME:' -ForegroundColor Yellow
    Write-Host '      $env:JAVA_HOME = "C:\ruta\al\jdk-21"'
    Write-Host '      (luego volve a ejecutar este script)'
    exit 1
}

Write-Host "  java  : $javaExe"
Write-Host "  javac : $javacExe"

# La version se lee por cmd para que el stderr de Java no se mezcle con errores
# de PowerShell y no dispare un falso negativo.
$version = cmd /c "`"$javaExe`" -version 2>&1"
foreach ($linea in $version) {
    if ($linea -match 'version') { Write-Host "  $($linea.Trim())" }
}

# ============================================================
#  3. COMPILAR
# ============================================================

# Se borran las clases de compilaciones anteriores: si queda alguna de una
# version vieja de un ejercicio, java podria ejecutarla en vez de la nueva.
$clasesViejas = Get-ChildItem -Path $guia -Filter '*.class' -ErrorAction SilentlyContinue
if ($clasesViejas) {
    $clasesViejas | Remove-Item -Force
    Write-Host "  Limpiadas $($clasesViejas.Count) clases de una compilacion anterior."
}

Write-Host ''
Write-Host '=== COMPILACION ===' -ForegroundColor Cyan
$archivos = 1..10 | ForEach-Object { "Ejercicio${numeroGuia}_$_.java" }
$salida = & $javacExe -encoding UTF-8 $archivos 2>&1
if ($LASTEXITCODE -ne 0) {
    $salida | ForEach-Object { Write-Host "  $_" }
    Write-Host '  FALLO: errores de compilacion.' -ForegroundColor Red
    exit $LASTEXITCODE
}
if ($salida) { $salida | ForEach-Object { Write-Host "  $_" } }
Write-Host '  OK: los 10 archivos compilan sin errores.' -ForegroundColor Green

if ($NoRun) {
    Write-Host ''
    Write-Host '=== FIN: compilacion correcta (NoRun: no se ejecuto nada) ===' -ForegroundColor Green
    exit 0
}

# ============================================================
#  4. EJECUTAR
# ============================================================
# Entrada por stdin para los 10 ejercicios.
# TODOS reciben entrada, incluidos los que no la necesitan: con un archivo
# VACIO el Scanner no encuentra EOF y se queda leyendo la consola, que es
# exactamente lo que no queremos en un build automático.
$inputs = @{
    1  = "3`n5`n"                    # factorial:      n = 3, luego n = 5
    2  = "4`n10`n"                   # suma:           n = 4, luego n = 10
    3  = "3`n4`n"                    # multiplicar:    a = 3, b = 4
    4  = "2`n10`n3`n4`n"             # potencia:       a=2 n=10, luego a=3 n=4
    5  = "5`n"                       # descendente:    n = 5
    6  = "1234`n7`n"                 # digitos:        n = 1234, luego n = 7
    7  = "1234`n55`n"                # sumarDigitos:   n = 1234, luego n = 55
    8  = "hola`n"                    # invertir:       "hola"
    9  = "anilina`n"                 # palindromo:     "anilina"
    10 = "5`n10`n20`n30`n40`n50`n30`n" # buscar: 5 elementos, luego buscar 30
}

foreach ($n in 1..10) {
    Write-Host ''
    Write-Host "=== EJERCICIO ${numeroGuia}_$n ===" -ForegroundColor Cyan

    # La ENTRADA va por un archivo temporal y se redirige con '<'.
    #
    # NO se usa el pipeline ("| & java") porque PowerShell 5.1 antepone SIEMPRE
    # un BOM al enviar texto a un proceso nativo, incluso con
    # UTF8Encoding($false). Ese BOM convierte el primer token en "\uFEFF5" y
    # hasNextInt() devuelve false, dejando todos los modos interactivos vacios.
    #
    # El archivo se escribe con -Encoding ascii (que no emite BOM) y se
    # redirige con '<' desde cmd. OJO: cmd no parsea bien el redirect si la
    # ruta de java lleva espacios entre comillas anidadas; por eso el comando
    # se arma con comillas escapadas para PowerShell (comilla invertida).
    $archivoEntrada = Join-Path $env:TEMP "guia2_entrada_$n.txt"
    Set-Content -Path $archivoEntrada -Value $inputs[$n] -NoNewline -Encoding ascii

    cmd /c "`"$javaExe`" Ejercicio${numeroGuia}_$n < `"$archivoEntrada`""
    $codigo = $LASTEXITCODE
    Remove-Item $archivoEntrada -Force -ErrorAction SilentlyContinue

    if ($codigo -ne 0) {
        Write-Host "  El ejercicio $n termino con codigo $codigo" -ForegroundColor Yellow
    }
}

Write-Host ''
Write-Host '=== FIN: los 10 ejercicios se ejecutaron ===' -ForegroundColor Green
Write-Host "Clases (.class) generadas en: $guia"