-- V2__create_table_pontos_leitura.sql   (SQL Server / Azure SQL)

-- Cria a tabela apenas se não existir
IF OBJECT_ID(N'dbo.Pontos_Leitura', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Pontos_Leitura (
        id_ponto   BIGINT IDENTITY(1,1) NOT NULL,
        id_patio   BIGINT NOT NULL,
        nome_ponto VARCHAR(50) NOT NULL,
        descricao  VARCHAR(200) NULL,

        CONSTRAINT PK_Pontos_Leitura PRIMARY KEY (id_ponto),
        CONSTRAINT FK_PontosLeitura_Patios
            FOREIGN KEY (id_patio)
            REFERENCES dbo.Patios(id_patio)
            ON DELETE CASCADE
    );
END;

-- Índice em id_patio (cria só se não existir)
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'idx_pontos_patio'
      AND object_id = OBJECT_ID(N'dbo.Pontos_Leitura')
)
BEGIN
    CREATE INDEX idx_pontos_patio ON dbo.Pontos_Leitura (id_patio);
END;

-- Índice em nome_ponto (cria só se não existir)
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'idx_pontos_nome'
      AND object_id = OBJECT_ID(N'dbo.Pontos_Leitura')
)
BEGIN
    CREATE INDEX idx_pontos_nome ON dbo.Pontos_Leitura (nome_ponto);
END;
