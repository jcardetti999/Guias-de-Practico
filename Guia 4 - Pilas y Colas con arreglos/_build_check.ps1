param(
    # Borra las clases generadas y sale, sin compilar ni ejecutar.
    [switch]$Clean,
    # Compila pero no ejecuta (util para revisar errores de compilacion).
    [switch]$NoRun
)

<#
    Guia 4 - Pilas y Colas con Arreglos
    Compila y ejecuta los 10 ejercicios.

    Puede estar en la raiz del repo o dentro de la carpeta de la guia: el script
    resuelve la ubicacion por si misma.

    Uso:
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1"     compila y ejecuta
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1" -Clean
        powershell -ExecutionPolicy Bypass -File ".\_build_check.ps1" -NoRun
#>

# NO se usa $ErrorActionPreference = 'Stop' a nivel global: los comandos nativos
# de Java escriben la version en stderr, y con 'Stop' + 2>&1 PowerShell lo
# convierte en error terminante aunque el comando haya funcionado.
$ErrorActionPreference = 'Continue'

# Encoding: cada asignacion va en su propio try. Si Console::OutputEncoding
# lanzara, un try compartido abortaria ANTES de llegar a $OutputEncoding.
try { chcp 65001 | Out-Null } catch { }
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding($false) } catch { }
try { $OutputEncoding = New-Object System.Text.UTF8Encoding($false) } catch { }

# ============================================================
#  1. UBICAR LA CARPETA DE LA GUIA
# ============================================================
# Este script vive DENTRO de la carpeta de la guia. NO se hardcodea el nombre de
# la guia: si el script se copiara a otra, compilaria la guia equivocada en
# silencio. Se busca el .docx de enunciados.
$guia = $PSScriptRoot

if (-not (Get-ChildItem -Path $guia -Filter '*.docx' -ErrorAction SilentlyContinue)) {
    $nombreCarpeta = Split-Path $guia -Leaf
    $padre = Split-Path $guia -Parent
    $hermana = Join-Path $padre $nombreCarpeta
    if (Get-ChildItem -Path $hermana -Filter '*.docx' -ErrorAction SilentlyContinue) {
        $guia = $hermana
    }
}

if (-not (Get-ChildItem -Path $guia -Filter 'Ejercicio1.java' -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: no se encuentra Ejercicio1.java en: $guia" -ForegroundColor Red
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
function Resolve-JavaExe {
    param([string]$Nombre)   # 'java' o 'javac'

    # (a) Override manual
    $override = [Environment]::GetEnvironmentVariable("JAVAEXE_$($Nombre.ToUpper())")
    if ($override -and (Test-Path $override)) { return $override }

    # (b) JAVA_HOME
    if ($env:JAVA_HOME) {
        $candidato = Join-Path (Join-Path $env:JAVA_HOME 'bin') "$Nombre.exe"
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
    Write-Host '  Diagnostico:' -ForegroundColor Yellow
    Write-Host '      where.exe java'
    Write-Host '      java -version'
    Write-Host ''
    Write-Host '  Si no aparece nada, instala un JDK 21:' -ForegroundColor Yellow
    Write-Host '      winget install --id EclipseAdoptium.Temurin.21.JDK -e'
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
# Entrada por stdin para los 10 ejercicios.
#
# TODOS reciben entrada, incluidos los que no la necesitan: con un archivo
# VACIO el Scanner no encuentra EOF y se queda leyendo la consola, que es
# exactamente lo que no queremos en un build automatico.
$inputs = @{
    1  = "5`n10`n3`n7`n9`n1`n"
    2  = "5`n1`n2`n3`n"
    3  = "(5 + 3) * (2 + 1)`n(5 + 3)) * (2 + 1`n((2 + 3)`n"
    4  = "3`ngoogle.com`ngithub.com`nstackoverflow.com`n"
    5  = "3`n10`nhola`n5`n"
    6  = "5`n10`n20`n30`n40`n50`n"
    7  = "3`nAna`nPedro`nLucia`n"
    8  = "3`nInforme 10`nCurriculum 2`nFactura 5`n"
    9  = "5`n1`n2`n3`n4`n5`n"
    10 = "5`n1`n2`n3`n4`n5`n"
}

foreach ($n in 1..10) {
    Write-Host ''
    Write-Host "=== EJERCICIO $n ===" -ForegroundColor Cyan

    # La ENTRADA va por un archivo temporal y se redirige con '<'.
    #
    # NO se usa el pipeline ("... | & java") porque PowerShell 5.1 antepone
    # SIEMPRE un BOM al enviar texto a un proceso nativo, incluso con
    # UTF8Encoding($false). Ese BOM convierte el primer token en "\uFEFF5" y
    # Scanner.hasNextInt() devuelve false.
    $archivoEntrada = Join-Path $env:TEMP "guia4_entrada_$n.txt"
    Set-Content -Path $archivoEntrada -Value $inputs[$n] -NoNewline -Encoding ascii

    cmd /c "`"$javaExe`" Ejercicio$n < `"$archivoEntrada`""
    $codigo = $LASTEXITCODE
    Remove-Item $archivoEntrada -Force -ErrorAction SilentlyContinue

    if ($codigo -ne 0) {
        Write-Host "  El ejercicio $n termino con codigo $codigo" -ForegroundColor Yellow
    }
}

Write-Host ''
Write-Host '=== FIN: los 10 ejercicios se ejecutaron ===' -ForegroundColor Green
Write-Host "Clases (.class) generadas en: $guia"