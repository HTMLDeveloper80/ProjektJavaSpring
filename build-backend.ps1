$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$maven = Join-Path $root "tools\apache-maven-3.9.9\bin\mvn.cmd"
$repo = Join-Path $root ".m2\repository"

if (!(Test-Path $maven)) {
  Write-Host "Brakuje lokalnego Mavena w tools\apache-maven-3.9.9."
  Write-Host "Uruchom najpierw komendę pobierającą Mavena albo daj znać Codexowi, żeby go pobrał."
  exit 1
}

& $maven "-Duser.home=$root" "-Dmaven.repo.local=$repo" "-pl" "fitness-gym-api-spring" "-am" "package" "-DskipTests"
