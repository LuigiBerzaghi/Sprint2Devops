-- V1__create_table_patios.sql  (SQL Server / Azure SQL)

IF OBJECT_ID(N'dbo.Patios', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Patios (
        id_patio BIGINT IDENTITY(1,1) PRIMARY KEY,
        nome     VARCHAR(100) NOT NULL,
        telefone VARCHAR(20) NULL,
        endereco VARCHAR(200) NULL
    );
END;
