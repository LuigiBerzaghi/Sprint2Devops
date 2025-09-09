# TrackYard - Challenge 2025

Aplicação **Spring Boot** com integração ao **Azure SQL Database**, desenvolvida como parte do Challenge 2025 (2º semestre - FIAP).

---

## 🚀 Pré-requisitos

- [Java 17+](https://adoptium.net/)  
- [Maven 3.8+](https://maven.apache.org/)  
- [Azure CLI](https://learn.microsoft.com/cli/azure/install-azure-cli) (para provisionar recursos no Azure)  
- Conta no [GitHub](https://github.com/)  

---

## ⚙️ Provisionamento do Banco (Azure SQL)

Execute o script PowerShell para criar o **Resource Group**, **Servidor SQL** e **Database**:

```powershell
cd scripts
.\provision-sql.ps1 -AdminPass "SuaSenhaForte123!"
```

Ao final, o script mostra:
- **Bloco pronto para o `application.properties`**
- **Comandos para exportar variáveis de ambiente**

Exemplo de saída:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:sqlserver://sqltrackyard.database.windows.net:1433;databaseName=dbtrackyard;encrypt=true;trustServerCertificate=false;loginTimeout=30"
$env:SPRING_DATASOURCE_USERNAME = "adminuser"
$env:SPRING_DATASOURCE_PASSWORD = "SuaSenhaForte123!"
$env:SPRING_DATASOURCE_DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
```

> Rode esses comandos **na mesma janela do PowerShell** antes de iniciar o projeto.

---

## 🛠️ Rodando a aplicação

Na raiz do projeto:

```powershell
# Limpa e compila
mvn clean install

# Executa
mvn spring-boot:run
```

A aplicação sobe em:  
👉 http://localhost:8080

---

## 📂 Estrutura relevante

```
/src          # Código fonte (Spring Boot)
/scripts      # Script provision-sql.ps1 para criar Azure SQL
/pom.xml      # Configuração Maven
```
## 📌Observações

- É necessário estar logado na Azure para o funcionamento dos scripts

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

- Nome do integrante 1 - RMXXXXX  
- Nome do integrante 2 - RMXXXXX  
- Nome do integrante 3 - RMXXXXX  
