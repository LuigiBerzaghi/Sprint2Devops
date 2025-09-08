# TrackYard — DevOps Tools & Cloud Computing (Sprint 3 → 4)

Aplicação Java Spring Boot com Azure SQL, preparada para entrega da Sprint 3 (deploy manual + evidências) e base para a Sprint 4 (CI/CD com Azure DevOps).

---

## 🧭 Sumário

* [Visão geral](#visão-geral)
* [Stack & requisitos](#stack--requisitos)
* [Estrutura do repositório](#estrutura-do-repositório)
* [Setup rápido (local)](#setup-rápido-local)
* [Variáveis de ambiente (.env)](#variáveis-de-ambiente-env)
* [Provisionamento de banco (Azure SQL) por script](#provisionamento-de-banco-azure-sql-por-script)
* [Rodando o script do banco (script\_bd.sql)](#rodando-o-script-do-banco-script_bdsql)
* [Executando a aplicação](#executando-a-aplicação)
* [Endpoints (API REST)](#endpoints-api-rest)
* [Evidências pedidas na Sprint 3](#evidências-pedidas-na-sprint-3)
* [Roteiro do vídeo (sugestão)](#roteiro-do-vídeo-sugestão)
* [Problemas comuns (troubleshooting)](#problemas-comuns-troubleshooting)
* [Próximos passos (Sprint 4)](#próximos-passos-sprint-4)
* [Autores](#autores)

---

## Visão geral

O TrackYard gerencia motos, pátios, pontos de leitura e movimentações. Está preparado para rodar localmente contra um **Azure SQL Database**. O repositório inclui scripts para provisionar/destruir a infraestrutura e um `script_bd.sql` para criar e popular as tabelas com dados de exemplo.

---

## Stack & requisitos

* **Java** 17+ (compatível com 21)
* **Maven** 3.9+
* **Spring Boot** 3.4.x (Web, Validation, Data JPA, Thymeleaf)
* **Driver**: `com.microsoft.sqlserver:mssql-jdbc`
* **Banco**: Azure SQL Database
* **Ferramentas**:

  * Azure CLI (para provisionamento)
  * Azure Data Studio (para consultar o banco)
  * PowerShell (Windows) / Bash (Linux/WSL/macOS)

---

## Estrutura do repositório

```
/ (raiz)
 ├── pom.xml
 ├── README.md
 ├── .gitignore
 ├── .env              # (local) NÃO versionar
 ├── .env.example      # (exemplo sem segredos) ✅ versionar
 ├── script_bd.sql     # DDL + inserts de exemplo
 ├── scripts/
 │    ├── provision-sql.ps1   # PowerShell — cria RG, servidor, DB e firewall
 │    ├── teardown-sql.ps1    # PowerShell — apaga o Resource Group
 │    ├── provision-sql.sh    # Bash (opcional)
 │    └── teardown.sh         # Bash (opcional)
 └── src/
      ├── main/java/com/mottu/trackyard/...
      └── main/resources/application.properties
```

> **Importante**: o App Service será configurado depois (Sprint 4 ou aula específica). Esta base já está pronta para **rodar local** e **mostrar CRUD + banco**.

---

## Setup rápido (local)

1. **Clone do repositório**

```bash
git clone <URL_DO_REPO>
cd trackyard
```

2. **Java & Maven**

* Verifique versões:

```bash
java -version
mvn -v
```

3. **Azure CLI** (para scripts de infra)

* Instale conforme SO: [https://learn.microsoft.com/cli/azure/install-azure-cli](https://learn.microsoft.com/cli/azure/install-azure-cli)
* Faça login:

```bash
az login
az account show   # verifica assinatura
```

---

## Variáveis de ambiente (.env)

O arquivo `.env` **não é versionado**. Use-o apenas localmente e deixe o `.env.example` no repo.

**`.env.example`** (copie para `.env` e preencha):

```
DB_URL=jdbc:sqlserver://<server>.database.windows.net:1433;database=<db>;encrypt=true;trustServerCertificate=false;loginTimeout=30;
DB_USER=<usuario@server>
DB_PASS=<sua_senha>
```

O `application.properties` lê as variáveis com `${DB_URL}`, `${DB_USER}`, `${DB_PASS}`. Para o Spring enxergar as variáveis, você tem três opções:

**Opção A — exportar no shell antes de rodar (recomendado local)**

* **PowerShell** (Windows):

```powershell
$env:DB_URL="jdbc:sqlserver://..."
$env:DB_USER="adminuser@sqltrackyard"
$env:DB_PASS="<senha>"
```

* **Bash** (Linux/WSL/macOS):

```bash
export DB_URL="jdbc:sqlserver://..."
export DB_USER="adminuser@sqltrackyard"
export DB_PASS="<senha>"
```

**Opção B — IDE (Run Configuration)**

* Defina `DB_URL`, `DB_USER`, `DB_PASS` como *Environment Variables* na configuração de execução da IDE (IntelliJ/Eclipse/VS Code).

**Opção C — persistir no Windows**

```powershell
setx DB_URL "jdbc:sqlserver://..."
setx DB_USER "adminuser@sqltrackyard"
setx DB_PASS "<senha>"
```

> (Feche e reabra o terminal para surtir efeito.)

> **Não** commitar `.env` real. Garanta que o `.gitignore` tenha:

```
.env
.env.*
!.env.example
```

---

## Provisionamento de banco (Azure SQL) por script

### 0) Registrar provedores (se necessário)

```powershell
az provider register --namespace Microsoft.Sql
az provider register --namespace Microsoft.Resources
```

### 1) Executar o script (PowerShell)

```powershell
cd scripts
powershell -ExecutionPolicy Bypass -File .\provision-sql.ps1 -AllowAzureServices -AllowClientIP
```

**O que o script faz:**

* Cria (ou garante) o Resource Group `rg-trackyard` em `brazilsouth`.
* Cria o **Servidor SQL** `sqltrackyard` e o **Database** `dbtrackyard` (S0, backup Local).
* Libera acesso para **serviços da Azure** (`0.0.0.0`) e **seu IP atual** (consulta automática via `api.ipify.org`).
* Imprime a **Connection String JDBC** pronta para uso.

> Para limpar tudo depois (teardown):

```powershell
powershell -ExecutionPolicy Bypass -File .\teardown-sql.ps1
# ou diretamente:
az group delete -n rg-trackyard --yes --no-wait
```

### 2) Alternativa: criar o servidor e DB manualmente (Azure CLI)

```powershell
az group create -n rg-trackyard -l brazilsouth
az sql server create -g rg-trackyard -n sqltrackyard -u adminuser -p "<SenhaForte>" -l brazilsouth
az sql db create -g rg-trackyard -s sqltrackyard -n dbtrackyard --service-objective S0 --backup-storage-redundancy Local
az sql server firewall-rule create -g rg-trackyard -s sqltrackyard -n AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0
# liberar seu IP (troque pelo seu IP público atual):
az sql server firewall-rule create -g rg-trackyard -s sqltrackyard -n AllowMyIP --start-ip-address 1.2.3.4 --end-ip-address 1.2.3.4
```

---

## Rodando o script do banco (`script_bd.sql`)

1. Abra o **Azure Data Studio** e conecte em:

   * Server: `sqltrackyard.database.windows.net`
   * Database: `dbtrackyard`
   * Auth: SQL Login
   * User: `adminuser@sqltrackyard`
   * Password: `<senha>`
   * Encrypt: **Yes**
2. Abra `script_bd.sql` e execute tudo.
3. Valide com:

```sql
SELECT TOP (10) * FROM Patios;
SELECT TOP (10) * FROM Motos;
SELECT TOP (10) * FROM Pontos_Leitura;
SELECT TOP (10) * FROM Movimentacoes;
```

> O script cria e popula as tabelas respeitando as entidades JPA do projeto.

---

## Executando a aplicação

### 1) Build & run

* Com variáveis definidas (ver [Variáveis de ambiente](#variáveis-de-ambiente-env)):

```bash
mvn spring-boot:run
```

> Logs esperados: `Tomcat started on port(s): 8080`.

* Para empacotar e rodar o JAR:

```bash
mvn clean package
java -jar target/trackyard-0.0.1-SNAPSHOT.jar
```

### 2) URL base

* API: `http://localhost:8080/api`
* Páginas Thymeleaf (listas):

  * `/motos`, `/patios`, `/pontos`, `/movimentacoes`

---

## Endpoints (API REST)

> **Observação:** abaixo, exemplos com `curl`. Ajuste IDs conforme dados do seu banco.

### Motos (`/api/motos`)

* **Criar**

```bash
curl -X POST http://localhost:8080/api/motos \
  -H "Content-Type: application/json" \
  -d '{
    "idMoto": "M003",
    "modelo": "Honda Biz 125",
    "placa": "DEF2G45"
  }'
```

* **Listar (paginado)**

```bash
curl "http://localhost:8080/api/motos?page=0&size=10"
```

* **Buscar por ID**

```bash
curl http://localhost:8080/api/motos/M003
```

* **Buscar por placa**

```bash
curl http://localhost:8080/api/motos/placa/DEF2G45
```

* **Atualizar**

```bash
curl -X PUT http://localhost:8080/api/motos/M003 \
  -H "Content-Type: application/json" \
  -d '{
    "idMoto": "M003",
    "modelo": "Honda Biz 125 ES",
    "placa": "DEF2G45"
  }'
```

* **Deletar**

```bash
curl -X DELETE http://localhost:8080/api/motos/M003
```

### Pátios (`/api/patios`)

* **Criar**

```bash
curl -X POST http://localhost:8080/api/patios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pátio Leste",
    "telefone": "(11) 97777-0000",
    "endereco": "São Paulo - SP"
  }'
```

* **Listar (paginado)**

```bash
curl "http://localhost:8080/api/patios?page=0&size=10"
```

* **Buscar por ID**

```bash
curl http://localhost:8080/api/patios/1
```

* **Atualizar**

```bash
curl -X PUT http://localhost:8080/api/patios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "idPatio": 1,
    "nome": "Pátio Leste (Atualizado)",
    "telefone": "(11) 96666-1111",
    "endereco": "São Paulo - SP"
  }'
```

* **Deletar**

```bash
curl -X DELETE http://localhost:8080/api/patios/1
```

### Pontos de Leitura (`/api/pontos-leitura`)

* **Criar**

```bash
curl -X POST http://localhost:8080/api/pontos-leitura \
  -H "Content-Type: application/json" \
  -d '{
    "idPatio": 1,
    "nomePonto": "Portão B",
    "descricao": "Leitura de acesso externo"
  }'
```

* **Listar (paginado)**

```bash
curl "http://localhost:8080/api/pontos-leitura?page=0&size=10"
```

* **Buscar por ID**

```bash
curl http://localhost:8080/api/pontos-leitura/1
```

* **Atualizar**

```bash
curl -X PUT http://localhost:8080/api/pontos-leitura/1 \
  -H "Content-Type: application/json" \
  -d '{
    "idPonto": 1,
    "idPatio": 1,
    "nomePonto": "Portão B (Atualizado)",
    "descricao": "Controle de acesso"
  }'
```

* **Deletar**

```bash
curl -X DELETE http://localhost:8080/api/pontos-leitura/1
```

### Movimentações (`/api/movimentacoes`)

* **Registrar**

```bash
curl -X POST http://localhost:8080/api/movimentacoes \
  -H "Content-Type: application/json" \
  -d '{
    "idMoto": "M001",
    "idPonto": 1
    // "dataHora": "2025-09-08T12:34:56"  (opcional)
  }'
```

* **Buscar por ID**

```bash
curl http://localhost:8080/api/movimentacoes/1
```

> Dica: use **Postman/Insomnia** para facilitar as chamadas e salvar a coleção para o vídeo.

---

## Evidências pedidas na Sprint 3

* ✅ CRUD completo em **pelo menos uma** entidade (ex.: `Motos`).
* ✅ Banco em nuvem (Azure SQL) **sem H2/Oracle local**.
* ✅ **Scripts** de provisionamento/teardown e `script_bd.sql` no repo.
* ✅ **Vídeo**: clone → provisionamento → rodar app → CRUD → selecionar no banco (Azure Data Studio) mostrando os dados.
* ✅ **PDF** da entrega com links (GitHub, vídeo), diagramas e descrição.

---

## Roteiro do vídeo (sugestão)

1. **Introdução**: nome do grupo/RM + visão do problema.
2. **Clone do repo** e abertura do projeto.
3. **Provisionamento** (PowerShell): executar `scripts/provision-sql.ps1` e mostrar a JDBC impressa.
4. **Azure Data Studio**: rodar `script_bd.sql` e selecionar tabelas.
5. **Variáveis de ambiente**: mostrar `.env.example` e (rápido) como definiu `DB_URL/USER/PASS`.
6. **Rodar app**: `mvn spring-boot:run`.
7. **CRUD** (Postman/Insomnia): criar/atualizar/deletar e **mostrar no banco** após cada operação.
8. **Encerramento**: resumo da arquitetura e próximos passos (CI/CD na Sprint 4).

---

## Problemas comuns (troubleshooting)

* **Erro 40615 – IP não permitido**:

  > `Client with IP address 'X.X.X.X' is not allowed to access the server`

  * Solução: liberar seu IP no firewall do servidor SQL:

    ```powershell
    $ip = (Invoke-RestMethod -Uri "https://api.ipify.org")
    az sql server firewall-rule create -g rg-trackyard -s sqltrackyard -n AllowMyIP --start-ip-address $ip --end-ip-address $ip
    ```
* **MissingSubscriptionRegistration (Microsoft.Sql)**:

  * Rode: `az provider register --namespace Microsoft.Sql`
* **Login failed / credenciais**:

  * Confirme usuário: `adminuser@sqltrackyard` (não apenas `adminuser`).
* **Driver ausente**:

  * Verifique `pom.xml` tem `com.microsoft.sqlserver:mssql-jdbc`.
* **Variáveis não reconhecidas**:

  * Garanta que `DB_URL/DB_USER/DB_PASS` estão exportadas no terminal **antes** de `mvn spring-boot:run`.

---

## Próximos passos (Sprint 4)

* Containerizar (Dockerfile) e publicar imagem em **Azure Container Registry (ACR)**.
* Configurar **Azure DevOps** (projeto privado, Scrum) + **Pipelines**:

  * **CI**: build, testes, push da imagem no ACR, artefatos.
  * **CD**: deploy automático em **App Service (container)** ou **ACI**.
* Proteger segredos (Variable Group / Key Vault) e parametrizar ambiente.

---

## Autores

* **Nome / RM**
* **Nome / RM**

> Coloque aqui os créditos, contato e links (repo, Azure DevOps, vídeo).
