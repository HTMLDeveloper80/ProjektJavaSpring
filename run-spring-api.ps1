$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$maven = Join-Path $root "tools\apache-maven-3.9.9\bin\mvn.cmd"
$repo = Join-Path $root ".m2\repository"
$api = Join-Path $root "fitness-gym-api-spring"

if (!(Test-Path $maven)) {
  Write-Host "Brakuje lokalnego Mavena w tools\apache-maven-3.9.9."
  Write-Host "Uruchom najpierw komendę pobierającą Mavena albo daj znać Codexowi, żeby go pobrał."
  exit 1
}

Set-Location $root
& $maven "-Duser.home=$root" "-Dmaven.repo.local=$repo" "-pl" "fitness-gym-backend-java" "-am" "install" "-DskipTests"

Set-Location $api
& $maven "-Duser.home=$root" "-Dmaven.repo.local=$repo" "org.springframework.boot:spring-boot-maven-plugin:2.7.18:run"
