/* =============================================
   Script de criação do banco – Trackyard (Azure SQL)
   ============================================= */

-- Limpeza segura 
DROP TABLE IF EXISTS Movimentacoes;
DROP TABLE IF EXISTS Pontos_Leitura;
DROP TABLE IF EXISTS Motos;
DROP TABLE IF EXISTS Patios;

------------------------------------------------
-- Tabela: Patios
------------------------------------------------
CREATE TABLE Patios (
    id_patio     BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome         NVARCHAR(100)  NOT NULL,
    telefone     NVARCHAR(20)   NULL,
    endereco     NVARCHAR(200)  NULL
);

------------------------------------------------
-- Tabela: Motos
------------------------------------------------
CREATE TABLE Motos (
    id_moto  NVARCHAR(50)  NOT NULL PRIMARY KEY,
    modelo   NVARCHAR(100) NOT NULL,
    placa    NVARCHAR(10)  NOT NULL UNIQUE
);

------------------------------------------------
-- Tabela: Pontos_Leitura
------------------------------------------------
CREATE TABLE Pontos_Leitura (
    id_ponto    BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_patio    BIGINT       NOT NULL,
    nome_ponto  NVARCHAR(50) NOT NULL,
    descricao   NVARCHAR(200) NULL,

    CONSTRAINT FK_Pontos_Patios
        FOREIGN KEY (id_patio) REFERENCES Patios(id_patio)
);

------------------------------------------------
-- Tabela: Movimentacoes
------------------------------------------------
CREATE TABLE Movimentacoes (
    id_movimentacao BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_moto         NVARCHAR(50)  NOT NULL,
    id_ponto        BIGINT        NOT NULL,
    data_hora       DATETIME2     NULL,

    CONSTRAINT FK_Mov_Motos
        FOREIGN KEY (id_moto) REFERENCES Motos(id_moto),
    CONSTRAINT FK_Mov_Pontos
        FOREIGN KEY (id_ponto) REFERENCES Pontos_Leitura(id_ponto)
);

------------------------------------------------
-- Dados de exemplo
------------------------------------------------
-- Patios
INSERT INTO Patios (nome, telefone, endereco) VALUES
(N'Pátio Central', N'(11) 99999-0000', N'Guarulhos - SP'),
(N'Pátio Zona Norte', N'(11) 98888-1111', N'São Paulo - SP');

-- Motos (IDs string de acordo com a entidade)
INSERT INTO Motos (id_moto, modelo, placa) VALUES
(N'M001', N'Honda CG 160', N'ABC1D23'),
(N'M002', N'Yamaha Fazer 250', N'XYZ9E88');

-- Pontos_Leitura
INSERT INTO Pontos_Leitura (id_patio, nome_ponto, descricao) VALUES
(1, N'Portão A', N'Leitura de temperatura: 25°C'),
(2, N'Guarita 2', N'Leitura de umidade: 70%');

-- Movimentacoes (liga Motos e Pontos_Leitura)
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) VALUES
(N'M001', 1, SYSDATETIME()),
(N'M002', 2, SYSDATETIME());

------------------------------------------------
-- Validação rápida
------------------------------------------------
SELECT 'Patios'           AS Tabela, COUNT(*) AS Qtde FROM Patios;
SELECT 'Motos'            AS Tabela, COUNT(*) AS Qtde FROM Motos;
SELECT 'Pontos_Leitura'   AS Tabela, COUNT(*) AS Qtde FROM Pontos_Leitura;
SELECT 'Movimentacoes'    AS Tabela, COUNT(*) AS Qtde FROM Movimentacoes;
