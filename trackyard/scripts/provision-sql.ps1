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

<# Container único (ACR): defaults. Ajuste conforme necessário #>
$AcrName = "acrtrackyard"
$AcrRepo = "trackyard"
$ImageTag = "latest"
$WebAppName = "trackyard-2TDSB"

Write-Host "==> Criando Resource Group $ResourceGroup em $Location"
az group create -n $ResourceGroup -l $Location -o none

Write-Host "==> Criando SQL Server $SqlServerName "
az sql server create `
  -g $ResourceGroup `
  -n $SqlServerName `
  -u $AdminUser `
  -p $AdminPass `
  -l $Location -o none

Write-Host "==> Criando Database $DbName "
az sql db create `
  -g $ResourceGroup `
  -s $SqlServerName `
  -n $DbName `
  --service-objective S0 `
  --backup-storage-redundancy Local -o none

if ($AllowAzureServices) {
  Write-Host "==> Liberando Azure Services (0.0.0.0)"
  az sql server firewall-rule create `
    -g $ResourceGroup -s $SqlServerName `
    -n AllowAzureServices `
    --start-ip-address 0.0.0.0 `
    --end-ip-address 0.0.0.0 -o none
}

if ($AllowClientIP) {
  $ip = (Invoke-RestMethod -Uri "https://api.ipify.org?format=text" -TimeoutSec 10)
  Write-Host "==> Liberando IP do cliente: $ip …"
  az sql server firewall-rule create `
    -g $ResourceGroup -s $SqlServerName `
    -n AllowMyIP `
    --start-ip-address $ip `
    --end-ip-address $ip -o none
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

az provider register --namespace Microsoft.Web -o none

Write-Host "==> criando o plano do serviço de aplicativo"
az appservice plan create -g $ResourceGroup -n $Plan -l $Location --sku B1 --is-linux -o none

Write-Host "==> Criando o serviço de aplicativo"
$AcrLoginServer = (az acr show -n $AcrName --query loginServer -o tsv)
$acrCreds = az acr credential show -n $AcrName | ConvertFrom-Json
$acrUser = $acrCreds.username
$acrPass = ($acrCreds.passwords | Where-Object { $_.name -eq 'password' } | Select-Object -First 1 -ExpandProperty value)
$imageRef = "$AcrLoginServer/$AcrRepo:$ImageTag"

az webapp create -g $ResourceGroup -p $Plan -n $WebAppName -i $imageRef -o none

az webapp config container set `
  -g $ResourceGroup -n $WebAppName `
  --docker-custom-image-name $imageRef `
  --docker-registry-server-url "https://$AcrLoginServer" `
  --docker-registry-server-user $acrUser `
  --docker-registry-server-password $acrPass `
  -o none

Write-Host "==> Preparando configurações do WebApp (container)"
# cd .. (não necessário para container)
# mvn -q -DskipTests package (deploy via imagem container)

Write-Host "==> Definindo configurações do WebApp"
az webapp config appsettings set -g $ResourceGroup -n $WebAppName --settings `
  SPRING_DATASOURCE_URL=$jdbc `
  SPRING_DATASOURCE_USERNAME=$AdminUser `
  SPRING_DATASOURCE_PASSWORD=$AdminPass `
  SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver" `
  -o none

# Write-Host "==> realizando deploy do WebApp" (não aplicável para container)
# az webapp deploy -g $ResourceGroup -n $WebAppName --src-path target/trackyard-0.0.1-SNAPSHOT.jar --type jar

Write-Host "==> Acesse: https://$WebAppName.azurewebsites.net/motos"
