INSERT INTO Patios (nome, telefone, endereco) VALUES
 ('Pátio Guarulhos', '11 99999-0001', 'Guarulhos - SP'),
 ('Pátio Centro',    '11 99999-0002', 'São Paulo - SP');

INSERT INTO Pontos_Leitura (id_patio, nome_ponto, descricao) VALUES
 (1, 'Entrada Guarulhos', 'Ponto principal do pátio de Guarulhos'),
 (1, 'Saída Guarulhos',   'Ponto secundário do pátio de Guarulhos'),
 (2, 'Entrada Centro',    'Ponto principal do pátio do Centro');

-- Como id_moto é String, usa códigos amigáveis:
INSERT INTO Motos (id_moto, modelo, placa) VALUES
 ('MOTO-001', 'CG 160 Titan', 'ABC1D23'),
 ('MOTO-002', 'XRE 300',      'EFG4H56');

INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) VALUES
 ('MOTO-001', 1, CURRENT_TIMESTAMP),
 ('MOTO-001', 2, DATEADD('MINUTE', 30, CURRENT_TIMESTAMP)),
 ('MOTO-002', 3, CURRENT_TIMESTAMP);