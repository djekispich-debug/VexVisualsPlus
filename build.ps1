# VexVisualsPlus — сборка: .\build.ps1 build
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

$env:GRADLE_USER_HOME = 'D:\gradle-home'
if (-not (Test-Path $env:GRADLE_USER_HOME)) {
    New-Item -ItemType Directory -Path $env:GRADLE_USER_HOME -Force | Out-Null
}

# java.home в корне проекта
$javaHomeFile = Join-Path $PSScriptRoot 'java.home'
if (Test-Path $javaHomeFile) {
    $env:JAVA_HOME = (Get-Content $javaHomeFile -Raw).Trim()
}

if (-not $env:JAVA_HOME) {
    $candidates = @(
        'D:\Programs\Java',
        'D:\Programs\Java\jdk-21',
        'C:\Program Files\Java\jdk-21'
    )
    if (Test-Path 'C:\Program Files\Eclipse Adoptium') {
        $candidates += Get-ChildItem 'C:\Program Files\Eclipse Adoptium' -Directory |
            Where-Object { $_.Name -like 'jdk-*' } |
            Select-Object -ExpandProperty FullName
    }
    foreach ($p in $candidates) {
        if (Test-Path (Join-Path $p 'bin\java.exe')) {
            $env:JAVA_HOME = $p
            break
        }
    }
}

if (-not $env:JAVA_HOME -and (Get-Command java -ErrorAction SilentlyContinue)) {
    $javaBin = (Get-Command java).Source
    $env:JAVA_HOME = (Resolve-Path (Join-Path (Split-Path $javaBin -Parent) '..')).Path
}

Write-Host "GRADLE_USER_HOME = $env:GRADLE_USER_HOME"
if ($env:JAVA_HOME) {
    Write-Host "JAVA_HOME = $env:JAVA_HOME"
    & (Join-Path $env:JAVA_HOME 'bin\java.exe') -version
} else {
    Write-Host "JAVA_HOME not set, trying PATH..."
    java -version
}

foreach ($n in @("$env:USERPROFILE\.gradle\native", "D:\gradle-home\native")) {
    if (Test-Path $n) {
        Remove-Item -LiteralPath $n -Recurse -Force
        Write-Host "Deleted: $n"
    }
}

& "$PSScriptRoot\gradlew.bat" @args
exit $LASTEXITCODE
