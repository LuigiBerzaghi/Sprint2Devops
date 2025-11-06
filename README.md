 # TrackYard - Sistema de Gerenciamento de Pátios

**TrackYard** é uma aplicação Java desenvolvida para apoiar a Mottu no gerenciamento de motos dentro dos pátios, evitando perdas inesperadas e melhorando a rastreabilidade.

## 🎯 Descrição da Solução
O sistema permite o **cadastro e controle de pátios, pontos de leitura, motos e movimentações**. A aplicação está hospedada na **Azure App Service** e utiliza **Azure SQL Database** como banco em nuvem.  

## 💡 Benefícios para o Negócio
- **Redução de perdas**: acompanhamento em tempo real da localização das motos.  
- **Agilidade**: controle de entradas e saídas via pontos de leitura.  
- **Escalabilidade**: arquitetura em nuvem, fácil de expandir.  
- **Visibilidade**: relatórios claros de movimentações e histórico por moto.  

## 🚀 Pré-requisitos

- [Java 17+](https://adoptium.net/)  
- [Maven 3.8+](https://maven.apache.org/)  
- [Azure CLI](https://learn.microsoft.com/cli/azure/install-azure-cli) (para provisionar recursos no Azure)  
- Conta no [GitHub](https://github.com/)
- É necessário estar logado na Azure para o funcionamento dos scripts
  
---

## ⚙️ Baixando a aplicação

Para baixar o conteúdo da aplicação, basta executar o seguinte comando no terminal com o diretório desejado:

```powershell
git clone https://github.com/LuigiBerzaghi/Sprint2Devops.git
```

---
## ⚙️ Provisionamento geral da aplicação

Após "clonar" o repositório, execute o seguinte comando para ir ao diretório correto:

```powershell
cd Sprint2Devops/trackyard/scripts
```

Agora, execute o script PowerShell para criar o **Resource Group**, **Servidor SQL**, **Database** e realizar o **Deploy** da aplicação.

Caso queira personalizar apenas usuário e senha:

```powershell
.\provision-sql.ps1 `
  -AdminUser <usuario-admin> `
  -AdminPass <senha-admin>
```

Caso queira personalizar demais parãmetros utilizado pelo script:

```powershell
.\provision-sql.ps1 `
  -Location <localizacao> `
  -ResourceGroup <nome-do-resource-group> `
  -SqlServerName <nome-unico-do-sql-server> `
  -DbName <nome-do-database> `
  -AdminUser <usuario-admin> `
  -AdminPass <senha-admin> `
  -Plan <nome-do-app-service-plan>

```

Caso queira usar valores padrões:

```powershell
.\provision-sql.ps1
```

Valores padrão definidos pelo script:
-  Location = "brazilsouth"
-  ResourceGroup = "rg-trackyard"
-  SqlServerName = "sqltrackyard"         
-  DbName = "dbtrackyard"              
-  AdminUser = "adminuser"
-  AdminPass = "SenhaSuperSegura123!"
-  Plan = "planTrackyard"

---

## 🛠️ Testes

Para realizar os testes na api, testar os endpoints com a coleção do Postman, seguindo a seguinte rota:
[Postman](https://bold-zodiac-707210.postman.co/workspace/Personal-Workspace~4701d561-f092-46f6-a63c-0560d2fd1507/collection/39387306-06cd5d63-7cab-4aaf-9c69-e5983de04042?action=share&source=copy-link&creator=39387306)

---

## Acessar o Banco H2 (opicional)

O projeto usa o banco de dados em nuvem Azure.

No app Azure Data Studio insira as seguintes credenciais:

- Server: `sqltrackyard`
- Authentication Type: `SQL Login`
- Username: `adminuser` (ou o username personalizado)
- Password: `SenhaSuperSegura123!` (ou a password personalizada)

---

## ⏹️ Ao parar a execução
Desfaz o grupo de recursos padrão:
```powershell
az group delete --name rg-trackyard --yes --no-wait
```
Caso tenha personalizado o nome do grupo de recursos:
```powershell
az group delete --name <nome-rg> --yes --no-wait
```

---

## 🗄️ Estrutura do Projeto
- `controller/` → controladores REST  
- `service/` → regras de negócio  
- `entity/` → entidades JPA  
- `repository/` → repositórios JPA  
- `dto/` → objetos de transferência  
- `exception/` → tratamento centralizado de erros  
- `scripts/` → scripts Azure CLI 

---

## 👥 Equipe

- RM555516 - Luigi Berzaghi  
- RM559093 - Guilherme Pelissari   
- RM558445 - Cauã dos Santos   
