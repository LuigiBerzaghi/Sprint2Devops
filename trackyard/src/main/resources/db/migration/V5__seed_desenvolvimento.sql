-- V5__insert_dados_iniciais.sql  (SQL Server / Azure SQL)

-- Inserindo pátios
INSERT INTO dbo.Patios (nome, telefone, endereco) VALUES
 ('Pátio Guarulhos', '11 99999-0001', 'Guarulhos - SP'),
 ('Pátio Centro',    '11 99999-0002', 'São Paulo - SP');

-- Inserindo pontos de leitura
INSERT INTO dbo.Pontos_Leitura (id_patio, nome_ponto, descricao) VALUES
 (1, 'Entrada Guarulhos', 'Ponto principal do pátio de Guarulhos'),
 (1, 'Saída Guarulhos',   'Ponto secundário do pátio de Guarulhos'),
 (2, 'Entrada Centro',    'Ponto principal do pátio do Centro');

-- Inserindo motos
INSERT INTO dbo.Motos (id_moto, modelo, placa) VALUES
 ('MOTO-001', 'CG 160 Titan', 'ABC1D23'),
 ('MOTO-002', 'XRE 300',      'EFG4H56');

-- Inserindo movimentações
INSERT INTO dbo.Movimentacoes (id_moto, id_ponto, data_hora) VALUES
 ('MOTO-001', 1, SYSDATETIME()),
 ('MOTO-001', 2, DATEADD(MINUTE, 30, SYSDATETIME())),
 ('MOTO-002', 3, SYSDATETIME());
