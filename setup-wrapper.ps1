$ErrorActionPreference = 'Stop'
$project = $PSScriptRoot
$jar = Join-Path $project 'gradle\wrapper\gradle-wrapper.jar'
New-Item -ItemType Directory -Force -Path (Split-Path $jar) | Out-Null
Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.14.3/gradle/wrapper/gradle-wrapper.jar' -OutFile $jar
Write-Host "Downloaded wrapper jar:" (Get-Item $jar).Length "bytes"
