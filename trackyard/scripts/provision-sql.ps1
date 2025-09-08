Param(
  [string]$Location = "brazilsouth",
  [string]$ResourceGroup = "rg-trackyard",
  [string]$SqlServerName = "sqltrackyard",         # precisa ser único na Azure
  [string]$DbName = "dbtrackyard",                 #valores usados caso o usuário não digite nada
  [string]$AdminUser = "adminuser",
  [string]$AdminPass = "SenhaSuperSegura123!",
  [switch]$AllowAzureServices = $true,
  [switch]$AllowClientIP = $true
)

Write-Host "==> Criando Resource Group $ResourceGroup em $Location …"
az group create -n $ResourceGroup -l $Location | Out-Null

Write-Host "==> Criando SQL Server $SqlServerName …"
az sql server create `
  -g $ResourceGroup `
  -n $SqlServerName `
  -u $AdminUser `
  -p $AdminPass `
  -l $Location | Out-Null

Write-Host "==> Criando Database $DbName (S0, backup Local)…"
az sql db create `
  -g $ResourceGroup `
  -s $SqlServerName `
  -n $DbName `
  --service-objective S0 `
  --backup-storage-redundancy Local | Out-Null

if ($AllowAzureServices) {
  Write-Host "==> Liberando Azure Services (0.0.0.0)…"
  az sql server firewall-rule create `
    -g $ResourceGroup -s $SqlServerName `
    -n AllowAzureServices `
    --start-ip-address 0.0.0.0 `
    --end-ip-address 0.0.0.0 | Out-Null
}

if ($AllowClientIP) {
  $ip = (Invoke-RestMethod -Uri "https://api.ipify.org")
  Write-Host "==> Liberando IP do cliente: $ip …"
  az sql server firewall-rule create `
    -g $ResourceGroup -s $SqlServerName `
    -n AllowMyIP `
    --start-ip-address $ip `
    --end-ip-address $ip | Out-Null
}

# Monta JDBC pronta
$serverFqdn = "$SqlServerName.database.windows.net"
$jdbc = "jdbc:sqlserver://$serverFqdn:1433;database=$DbName;user=$AdminUser@$SqlServerName;password=$AdminPass;encrypt=true;trustServerCertificate=false;loginTimeout=30;"

Write-Host "`n===== SAÍDA ====="
Write-Host "Server FQDN : $serverFqdn"
Write-Host "Database    : $DbName"
Write-Host "Username    : $AdminUser@$SqlServerName"
Write-Host "JDBC        : $jdbc"
