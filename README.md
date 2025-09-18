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


## ⚙️ Provisionamento geral da aplicação

Execute o script PowerShell para criar o **Resource Group**, **Servidor SQL**, **Database** e realizar o **Deploy** da aplicação:

```powershell
cd Sprint2Devops/trackyard/scripts
.\provision-sql.ps1 -AdminPass "<senha>"
```

---

## 🛠️ Rodando a aplicação
Para acessar a aplicação, basta entrar no link:
[Trackyard](https://trackyard-2tdsb.azurewebsites.net/motos)

---

## 🧪 Exemplos de testes para demonstração do CRUD via https

### Front-end
Ao abrir o app no link retornado após a execução do deploy, o usuário pode testar o CRUD de todas as entidades através da própria interface gráfica do WebApp.

---

### Postman
Abaixo, há uma coleção pronta para testes da API Trackyard via Postman.

Dependendo do nome definido para a aplicação pelo usuário, pode ser necessário alterar o nome da aplicação na url, portanto, é importante se atentar a este fato.

Para utilizar, basta:
- Acessar [Postman](https://www.postman.com/)
- Realizar login
- Importar o arquivo "TrackYardAPI.postman_collection.json"
- Utilizar a coleção de acordo com as necessidades do usuário

📂 [Baixar Collection do Postman](Postman/TrackYardAPI.postman_collection.json)

---

## ⏹️ Ao parar a execução
Desfaz o grupo de recursos:
```powershell
az group delete --name rg-trackyard --yes --no-wait
```
---

## 🔑 Variáveis de ambiente utilizadas

| Variável                                 | Descrição                       |
|------------------------------------------|---------------------------------|
| `SPRING_DATASOURCE_URL`                  | JDBC de conexão ao Azure SQL    |
| `SPRING_DATASOURCE_USERNAME`             | Usuário do banco                |
| `SPRING_DATASOURCE_PASSWORD`             | Senha do banco                  |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME`    | Driver JDBC                     |

---

## 🗄️ Estrutura do Projeto
- `controller/` → controladores REST  
- `service/` → regras de negócio  
- `entity/` → entidades JPA  
- `repository/` → repositórios JPA  
- `dto/` → objetos de transferência  
- `exception/` → tratamento centralizado de erros  
- `scripts/` → scripts Azure CLI + DDL (`script_bd.sql`) 

---

## 👥 Equipe

- RMXXXXX - Luigi Berzaghi  
- RMXXXXX - Guilherme Pelissari   
- RMXXXXX - Cauã dos Santos   
