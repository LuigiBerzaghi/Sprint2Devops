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
  $ip = (Invoke-RestMethod -Uri "https://api.ipify.org?format=text" -TimeoutSec 10)
  Write-Host "==> Liberando IP do cliente: $ip …"
  az sql server firewall-rule create `
    -g $ResourceGroup -s $SqlServerName `
    -n AllowMyIP `
    --start-ip-address $ip `
    --end-ip-address $ip | Out-Null
}

# Monta JDBC 
$serverFqdn = "$SqlServerName.database.windows.net"
$jdbc = "jdbc:sqlserver://$serverFqdn"+":1433;databaseName=$DbName;encrypt=true;trustServerCertificate=false;loginTimeout=30"

# Bloco pronto para o Spring Boot
$springBlock = @"
spring.datasource.url=$jdbc
spring.datasource.username=$AdminUser
spring.datasource.password=$AdminPass
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
"@

Write-Host "`n===== SAÍDA ====="
Write-Host "Server FQDN : $serverFqdn"
Write-Host "Database    : $DbName"
Write-Host "Username    : $AdminUser"   
Write-Host "JDBC        : $jdbc"
Write-Host "`nAgora rode os seguintes comandos nesta janela do PowerShell para exportar as variáveis de ambiente:"
Write-Host ""
Write-Host "`$env:SPRING_DATASOURCE_URL = `"$jdbc`""
Write-Host "`$env:SPRING_DATASOURCE_USERNAME = `"$AdminUser`""
Write-Host "`$env:SPRING_DATASOURCE_PASSWORD = `"$AdminPass`""
Write-Host "`$env:SPRING_DATASOURCE_DRIVER_CLASS_NAME = `"com.microsoft.sqlserver.jdbc.SQLServerDriver`""
Write-Host ""
Write-Host "Depois rode sua aplicação com: mvn spring-boot:run"



