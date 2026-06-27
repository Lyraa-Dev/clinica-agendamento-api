-- DADOS INICIAIS (seed)
-- ===================================================================
-- Executado automaticamente ao subir a aplicacao (spring.sql.init).
-- Como o H2 é em memoria, o banco zera a cada reinicio e este script
-- repopula os dados - garantindo que o sempre encontre
-- pacientes e profissionais prontos para criar agendamentos.
-- ===================================================================

-- ---------- Profissionais ----------
INSERT INTO profissional (nome, especialidade, registro) VALUES ('Dr. Carlos Andrade', 'Cardiologia', 'CRM-PE 12345');
INSERT INTO profissional (nome, especialidade, registro) VALUES ('Dra. Beatriz Lima', 'Dermatologia', 'CRM-PE 67890');
INSERT INTO profissional (nome, especialidade, registro) VALUES ('Dr. Paulo Mendes', 'Ortopedia', 'CRM-PE 24680');
INSERT INTO profissional (nome, especialidade, registro) VALUES ('Dra. Renata Souza', 'Pediatria', 'CRM-PE 13579');
INSERT INTO profissional (nome, especialidade, registro) VALUES ('Dr. Felipe Costa', 'Clinica Geral', 'CRM-PE 11223');

-- ---------- Pacientes ----------
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Maria Silva', '12345678901', '81999998888');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Joao Pereira', '98765432100', '81977776666');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Ana Costa', '11122233344', '81966665555');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Pedro Oliveira', '55566677788', '81955554444');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Juliana Santos', '99988877766', '81944443333');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Lucas Almeida', '44455566677', '81933332222');