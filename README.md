# TrackYard - Challenge 2025

O **TrackYard** é uma aplicação web para gestão de pátios de veículos.  
Ela permite **cadastrar, consultar, atualizar e remover** informações de motos, pátios e movimentações, com persistência em banco de dados na nuvem.

---

## 🚀 Pré-requisitos

- [Java 17+](https://adoptium.net/)  
- [Maven 3.8+](https://maven.apache.org/)  
- [Azure CLI](https://learn.microsoft.com/cli/azure/install-azure-cli) (para provisionar recursos no Azure)  
- Conta no [GitHub](https://github.com/)
- É necessário estar logado na Azure para o funcionamento dos scripts
---


## ⚙️ Provisionamento do Banco (Azure SQL)

Execute o script PowerShell para criar o **Resource Group**, **Servidor SQL**, **Database** e realizar o **Deploy** da aplicação:

```powershell
cd Sprint2Devops/trackyard/scripts
.\provision-sql.ps1 -AdminPass "<senha>"
```

---

## 🛠️ Rodando a aplicação
Para acessar a aplicação, basta entrar no link:
[Trackyard](trackyard-2tdsb.azurewebsites.net/motos)

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

## 👥 Equipe

- RMXXXXX - Luigi Berzaghi  
- RMXXXXX - Guilherme Pelissari   
- RMXXXXX - Cauã dos Santos   
