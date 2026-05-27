$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$source = Join-Path $root "src\main\java"
$output = Join-Path $root "build\classes"

if (Test-Path $output) {
  Remove-Item $output -Recurse -Force
}

New-Item -ItemType Directory -Path $output | Out-Null

$files = Get-ChildItem $source -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d $output $files

Write-Host "Java backend compiled to $output"
