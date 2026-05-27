$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$api = Join-Path $root "fitness-gym-api-dotnet"

Set-Location $api
dotnet run --urls "http://localhost:5080"
