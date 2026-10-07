param(
    # Borra las clases generadas y sale, sin compilar ni ejecutar.
    [switch]$Clean,
    # Compila pero no ejecuta (util para revisar errores de compilacion).
    [switch]$NoRun
)

<#
    Guia 1 - Analisis de Algoritmos
    Compila y ejecuta los 10 ejercicios.

    Puede estar en la raiz del repo o dentro de la carpeta de la guia: el script
    resuelve la ubicacion por si misma.

    Uso:
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1"     compila y ejecuta
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1" -Clean
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1" -NoRun

    Los ejercicios 1, 2, 3 y 5 son interactivos (leen por Scanner), asi que este
    script les provee la entrada por stdin para que la demo corra completa.
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
try {
    $utf8SinBom = New-Object System.Text.UTF8Encoding($false)
    [Console]::OutputEncoding = $utf8SinBom
    $OutputEncoding = $utf8SinBom
} catch { }

# ============================================================
#  1. UBICAR LA CARPETA DE LA GUIA
# ============================================================
$guia = $PSScriptRoot
if (-not (Test-Path (Join-Path $guia 'Ejercicio1.java'))) {
    # Estamos en la raiz del repo: entrar en la carpeta de la guia.
    $guia = Join-Path (Split-Path $PSScriptRoot -Parent) 'Guia 1 - Analisis de Algoritmos'
}
if (-not (Test-Path (Join-Path $guia 'Ejercicio1.java'))) {
    Write-Host "ERROR: no se encuentra Ejercicio1.java." -ForegroundColor Red
    Write-Host "  Carpeta buscada: $guia"
    Write-Host "  Ejecuta este script desde la raiz del repo o desde la carpeta de la guia."
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
$archivos = 1..10 | ForEach-Object { "Ejercicio$_.java" }
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
# Entrada por stdin para los ejercicios interactivos.
$inputs = @{
    1 = "3`n5`n1`n9`n"
    2 = "4`n10`n20`n30`n40`n20`n"
    3 = "5`n1`n3`n5`n7`n9`n5`n"
    5 = "4`n2`n4`n2`n6`n2`n"
}

foreach ($n in 1..10) {
    Write-Host ''
    Write-Host "=== EJERCICIO $n ===" -ForegroundColor Cyan
    # La salida va directa a la consola (canalizarla con ForEach-Object la obliga a
    # pasar por el decodificador de PowerShell, que rompe los acentos).
    #
    # La ENTRADA de los interactivos se pasa por un archivo temporal y se
    # redirige con '<'. Es deliberado: canalizarla con '|' depende de
    # $OutputEncoding, que puede anteponer un BOM y hacer que el Scanner lea
    # "\uFEFF3" en vez de "3" (InputMismatchException). Con el archivo no
    # interviene PowerShell en la codificacion.
    if ($inputs.ContainsKey($n)) {
        $archivoEntrada = Join-Path $env:TEMP "guia1_entrada_$n.txt"
        Set-Content -Path $archivoEntrada -Value $inputs[$n] -NoNewline -Encoding ascii
        cmd /c "`"$javaExe`" Ejercicio$n < `"$archivoEntrada`""
        Remove-Item $archivoEntrada -Force -ErrorAction SilentlyContinue
    } else {
        & $javaExe "Ejercicio$n"
    }
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  El ejercicio $n termino con codigo $LASTEXITCODE" -ForegroundColor Yellow
    }
}

Write-Host ''
Write-Host '=== FIN: los 10 ejercicios se ejecutaron ===' -ForegroundColor Green
Write-Host "Clases (.class) generadas en: $guia"