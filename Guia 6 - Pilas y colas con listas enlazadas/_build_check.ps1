param(
    [switch]$Clean,
    [switch]$NoRun
)

$ErrorActionPreference = 'Continue'
try { chcp 65001 | Out-Null } catch { }
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding($false) } catch { }
try { $OutputEncoding = New-Object System.Text.UTF8Encoding($false) } catch { }

$guia = $PSScriptRoot
$javac = Get-Command javac -ErrorAction SilentlyContinue
$java = Get-Command java -ErrorAction SilentlyContinue
if (-not $javac -or -not $java) {
    Write-Host 'ERROR: se necesitan java y javac en PATH.' -ForegroundColor Red
    exit 1
}

if ($Clean) {
    Get-ChildItem -Path $guia -Filter '*.class' -ErrorAction SilentlyContinue |
        Remove-Item -Force
    Write-Host 'Clases compiladas eliminadas.' -ForegroundColor Green
    exit 0
}

Set-Location $guia
$archivos = 1..10 | ForEach-Object { "Ejercicio$_.java" }
foreach ($archivo in $archivos) {
    if (-not (Test-Path (Join-Path $guia $archivo))) {
        Write-Host "ERROR: falta $archivo" -ForegroundColor Red
        exit 1
    }
}

Write-Host '=== COMPILACION ===' -ForegroundColor Cyan
$salida = & $javac.Source -encoding UTF-8 $archivos 2>&1
if ($LASTEXITCODE -ne 0) {
    $salida | ForEach-Object { Write-Host $_ }
    Write-Host 'FALLO: compilacion.' -ForegroundColor Red
    exit $LASTEXITCODE
}
if ($salida) { $salida | ForEach-Object { Write-Host $_ } }
Write-Host 'OK: los 10 archivos compilan sin errores.' -ForegroundColor Green

if ($NoRun) {
    Write-Host 'Fin: no se ejecutaron los ejercicios (-NoRun).' -ForegroundColor Green
    exit 0
}

$fallos = 0
foreach ($n in 1..10) {
    Write-Host "`n=== EJERCICIO $n ===" -ForegroundColor Cyan
    & $java.Source -cp $guia "Ejercicio$n"
    $codigo = $LASTEXITCODE
    if ($codigo -ne 0) {
        Write-Host "FALLO: Ejercicio$n termino con codigo $codigo." -ForegroundColor Red
        $fallos++
    } else {
        Write-Host "OK: Ejercicio$n termino con codigo 0." -ForegroundColor Green
    }
}

if ($fallos -gt 0) {
    Write-Host "FIN: $fallos ejercicio(s) fallaron." -ForegroundColor Red
    exit 1
}
Write-Host "`nFIN: los 10 ejercicios compilaron y ejecutaron correctamente." -ForegroundColor Green
exit 0
