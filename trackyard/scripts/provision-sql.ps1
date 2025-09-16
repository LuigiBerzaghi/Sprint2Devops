Param(
  [string]$Location = "brazilsouth",
  [string]$ResourceGroup = "rg-trackyard",
  [string]$SqlServerName = "sqltrackyard",         # precisa ser único na Azure
  [string]$DbName = "dbtrackyard",                 #valores usados caso o usuário não digite nada
  [string]$AdminUser = "adminuser",
  [string]$AdminPass = "SenhaSuperSegura123!",
  [switch]$AllowAzureServices = $true,
  [switch]$AllowClientIP = $true,
  [string]$Plan = "planTrackyard"
)

Write-Host "==> Criando Resource Group $ResourceGroup em $Location "
az group create -n $ResourceGroup -l $Location | Out-Null

Write-Host "==> Criando SQL Server $SqlServerName "
az sql server create `
  -g $ResourceGroup `
  -n $SqlServerName `
  -u $AdminUser `
  -p $AdminPass `
  -l $Location | Out-Null

Write-Host "==> Criando Database $DbName "
az sql db create `
  -g $ResourceGroup `
  -s $SqlServerName `
  -n $DbName `
  --service-objective S0 `
  --backup-storage-redundancy Local | Out-Null

if ($AllowAzureServices) {
  Write-Host "==> Liberando Azure Services (0.0.0.0)"
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
$jdbc = "jdbc:sqlserver://$serverFqdn"+":1433;database=$DbName;user=$AdminUser@$SqlServerName;password=$AdminPass;encrypt=true;trustServerCertificate=false;loginTimeout=30;"

Write-Host "==> Definindo variáveis de ambiente"
# Define variáveis de ambiente
$env:SPRING_DATASOURCE_URL = $jdbc
$env:SPRING_DATASOURCE_USERNAME = $AdminUser
$env:SPRING_DATASOURCE_PASSWORD = $AdminPass
$env:SPRING_DATASOURCE_DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver"

az provider register --namespace Microsoft.Web

Write-Host "==> criando o plano do serviço de aplicativo"
az appservice plan create -g $ResourceGroup -n $Plan -l $Location --sku B1 --is-linux

Write-Host "==> Criando o serviço de aplicativo"
az webapp create -g $ResourceGroup -p $Plan -n trackyard-2TDSB --runtime "JAVA:17-java17"

Write-Host "==> Gerando o .jar"
cd ..
mvn -q -DskipTests package

Write-Host "==> Definindo configurações do WebApp"
az webapp config appsettings set -g $ResourceGroup -n trackyard-2TDSB --settings `
  SPRING_DATASOURCE_URL=$jdbc `
  SPRING_DATASOURCE_USERNAME=$AdminUser `
  SPRING_DATASOURCE_PASSWORD=$AdminPass `
  SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver"

Write-Host "==> realizando deploy do WebApp"
az webapp deploy -g $ResourceGroup -n trackyard-2TDSB --src-path target/trackyard-0.0.1-SNAPSHOT.jar --type jar

Write-Host "==> Acesse: trackyard-2tdsb.azurewebsites.net/motos"
