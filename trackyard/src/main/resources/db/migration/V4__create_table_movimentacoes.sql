-- V4__create_table_movimentacoes.sql  (SQL Server / Azure SQL)

-- Cria a tabela apenas se não existir
IF OBJECT_ID(N'dbo.Movimentacoes', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Movimentacoes (
        id_movimentacao BIGINT IDENTITY(1,1) NOT NULL,
        id_moto         VARCHAR(50) NULL,
        id_ponto        BIGINT NULL,
        -- Em SQL Server, TIMESTAMP é sinônimo de ROWVERSION (não é data/hora!)
        -- Use DATETIME2 para armazenar data/hora:
        data_hora       DATETIME2 NULL,

        CONSTRAINT PK_Movimentacoes PRIMARY KEY (id_movimentacao),
        CONSTRAINT FK_Mov_Moto
            FOREIGN KEY (id_moto)  REFERENCES dbo.Motos(id_moto),
        CONSTRAINT FK_Mov_Ponto
            FOREIGN KEY (id_ponto) REFERENCES dbo.Pontos_Leitura(id_ponto)
            ON DELETE CASCADE
    );
END;

-- Índice para acelerar buscas por moto
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'idx_mov_id_moto'
      AND object_id = OBJECT_ID(N'dbo.Movimentacoes')
)
BEGIN
    CREATE INDEX idx_mov_id_moto ON dbo.Movimentacoes(id_moto);
END;

-- Índice para acelerar buscas por ponto de leitura
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'idx_mov_id_ponto'
      AND object_id = OBJECT_ID(N'dbo.Movimentacoes')
)
BEGIN
    CREATE INDEX idx_mov_id_ponto ON dbo.Movimentacoes(id_ponto);
END;

-- (Opcional) Índice por data/hora para relatórios/ordenação
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'idx_mov_data_hora'
      AND object_id = OBJECT_ID(N'dbo.Movimentacoes')
)
BEGIN
    CREATE INDEX idx_mov_data_hora ON dbo.Movimentacoes(data_hora);
END;
