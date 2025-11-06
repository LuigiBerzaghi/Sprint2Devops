-- V3__create_table_motos.sql   (SQL Server / Azure SQL)

-- Cria a tabela apenas se não existir
IF OBJECT_ID(N'dbo.Motos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Motos (
        id_moto VARCHAR(50) NOT NULL PRIMARY KEY,
        modelo  VARCHAR(100) NOT NULL,
        placa   VARCHAR(10) NOT NULL UNIQUE
    );
END;
