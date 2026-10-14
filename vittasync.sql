DROP DATABASE IF EXISTS vittasync;
CREATE DATABASE vittasync;
USE vittasync;

CREATE TABLE Usuario (
    id INT AUTO_INCREMENT,
    cpf VARCHAR(11) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    senha VARCHAR(64) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    conselho VARCHAR(30),
    peso_inicial DOUBLE,
    altura DOUBLE,
    data_nascimento DATE NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (cpf),
    UNIQUE (email)
);

CREATE TABLE CodigoVerificacao (
    id INT AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    codigo VARCHAR(5) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    login_token VARCHAR(100),
    expira TIMESTAMP NOT NULL,
    utilizado BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (usuario_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE SessaoToken (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE SinaisVitais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    registrado_por_usuario_id INT,
    fc_bpm INT,
    fr_rpm INT,
    pa_sistolica INT,
    pa_diastolica INT,
    temp_celcius DOUBLE,
    spo2_porcento INT,
    peso DOUBLE,
    data_registro DATETIME NOT NULL,
    data_modificacao DATETIME,
    INDEX idx_sinais_paciente_data (paciente_id, data_registro),
    CONSTRAINT fk_sinais_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_sinais_registrado_por FOREIGN KEY (registrado_por_usuario_id) REFERENCES Usuario(id) ON DELETE SET NULL
);

CREATE TABLE Habitos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    registrado_por_usuario_id INT,
    horas_sono INT,
    minutos_exercicio INT,
    indice_repouso DOUBLE,      
    repouso BOOLEAN DEFAULT FALSE,         
    canal VARCHAR(10) DEFAULT 'email',    
    data_referencia DATE NOT NULL,
    data_registro DATETIME NOT NULL,
    data_modificacao DATETIME,
    INDEX idx_habitos_paciente_data (paciente_id, data_referencia),
    CONSTRAINT fk_habitos_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_habitos_registrado_por FOREIGN KEY (registrado_por_usuario_id) REFERENCES Usuario(id) ON DELETE SET NULL
);

CREATE TABLE LembreteMedicao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    dias_semana VARCHAR(100) NOT NULL,
    horario TIME NOT NULL,
    enviar_email BOOLEAN DEFAULT TRUE,
    enviar_sms BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_lembrete_usuario UNIQUE (usuario_id),
    INDEX idx_lembrete_ativo_horario (ativo, horario),
    CONSTRAINT fk_usuario_lembrete FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE DiarioSintomas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    sintoma VARCHAR(255) NOT NULL,
    intensidade_dor INT NOT NULL,
    data_referencia DATE NOT NULL,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    INDEX idx_sintomas_paciente_data (paciente_id, data_referencia),
    FOREIGN KEY (paciente_id) REFERENCES Usuario(id)
);

CREATE TABLE Vinculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    usuario_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    funcao VARCHAR(50),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_vinculo_paciente_usuario UNIQUE (paciente_id, usuario_id),
    FOREIGN KEY (paciente_id) REFERENCES Usuario(id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

CREATE TABLE ConviteVinculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    expira_em TIMESTAMP NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE ContatoEmergencia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL, 
    receber_alerta_sinais_vitais_saudavel BOOLEAN DEFAULT FALSE,
    receber_alerta_sinais_vitais_moderado BOOLEAN DEFAULT FALSE,
    receber_alerta_sinais_vitais_critico BOOLEAN DEFAULT TRUE,
    receber_alerta_habitos_saudavel BOOLEAN DEFAULT FALSE,
    receber_alerta_habitos_moderado BOOLEAN DEFAULT TRUE,
    receber_alerta_habitos_critico BOOLEAN DEFAULT TRUE,
    receber_alerta_geral_saudavel BOOLEAN DEFAULT FALSE,
    receber_alerta_geral_moderado BOOLEAN DEFAULT FALSE,
    receber_alerta_geral_critico BOOLEAN DEFAULT TRUE,
    canal_email BOOLEAN DEFAULT TRUE,
    canal_sms BOOLEAN DEFAULT FALSE,
    data_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_modificacao DATETIME NULL,
    CONSTRAINT fk_contato_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE ArquivoMedico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    medico_id INT NOT NULL,
    paciente_id INT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    nome_original VARCHAR(255) NOT NULL,
    extensao VARCHAR(20) NOT NULL,
    arquivo LONGBLOB NOT NULL,
    data_upload DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medico FOREIGN KEY (medico_id) REFERENCES Usuario(id),
    CONSTRAINT fk_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id)
);

CREATE TABLE EventoPaciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    usuario_id INT,
    tipo_evento VARCHAR(50) NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    prioridade VARCHAR(20) DEFAULT 'normal',
    visualizado BOOLEAN DEFAULT FALSE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_evento_paciente_criado (paciente_id, criado_em),
    CONSTRAINT fk_evento_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_evento_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE SET NULL
);

CREATE TABLE MetaAcompanhamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    tipo_dado VARCHAR(50) NOT NULL,
    valor_alvo DOUBLE NOT NULL,
    data_limite DATE NOT NULL,
    progresso DOUBLE DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    data_conclusao TIMESTAMP NULL,
    status VARCHAR(30) DEFAULT 'em_andamento',
    CONSTRAINT fk_meta_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE EstabilidadeClinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,     
    indice INT,          
    categoria VARCHAR(20) NOT NULL, 
    peso DOUBLE DEFAULT 1, 
    data_calculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_estabilidade_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE ExportacaoRelatorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    usuario_id INT NOT NULL, 
    formato VARCHAR(10) NOT NULL,
    categorias VARCHAR(50) NOT NULL,
    data_inicio DATE NULL,
    data_fim DATE NULL,
    data_exportacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_exportacao_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_exportacao_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE LinhaBase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    sinal VARCHAR(30) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    media DOUBLE NOT NULL,
    desvio_padrao DOUBLE NOT NULL,
    limite_inferior DOUBLE,
    limite_superior DOUBLE,
    data_formacao DATETIME NOT NULL,
    CONSTRAINT uk_linha_base_paciente_sinal UNIQUE (paciente_id, sinal),
    CONSTRAINT fk_linha_base_paciente FOREIGN KEY (paciente_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

DELIMITER $$
CREATE PROCEDURE sp_lembretes_do_minuto(IN p_dia VARCHAR(20), IN p_hora TIME)
BEGIN
    SELECT *
    FROM LembreteMedicao
    WHERE ativo = TRUE
      AND horario >= p_hora
      AND horario < ADDTIME(p_hora, '00:01:00')
      AND UPPER(dias_semana) LIKE CONCAT('%', p_dia, '%');
END$$

CREATE PROCEDURE sp_medias_estabilidade(IN p_paciente_id INT)
BEGIN
    SELECT
        COUNT(s.fc_bpm), AVG(s.fc_bpm),
        COUNT(s.fr_rpm), AVG(s.fr_rpm),
        COUNT(s.pa_sistolica), AVG(s.pa_sistolica),
        COUNT(s.pa_diastolica), AVG(s.pa_diastolica),
        COUNT(s.temp_celcius), AVG(s.temp_celcius),
        COUNT(s.spo2_porcento), AVG(s.spo2_porcento),
        COUNT(s.peso), AVG(s.peso),
        (SELECT COUNT(horas_sono) FROM Habitos WHERE paciente_id = p_paciente_id),
        (SELECT AVG(horas_sono) FROM Habitos WHERE paciente_id = p_paciente_id),
        (SELECT COUNT(minutos_exercicio) FROM Habitos WHERE paciente_id = p_paciente_id),
        (SELECT COUNT(*) FROM Habitos WHERE paciente_id = p_paciente_id AND minutos_exercicio = 0),
        (SELECT AVG(minutos_exercicio) FROM Habitos WHERE paciente_id = p_paciente_id)
    FROM SinaisVitais s
    WHERE s.paciente_id = p_paciente_id;
END$$
DELIMITER ;

SHOW INDEX FROM LembreteMedicao;
SHOW INDEX FROM DiarioSintomas;
SHOW INDEX FROM EventoPaciente;
SHOW INDEX FROM Vinculo;
SHOW INDEX FROM evento_visualizacao;

SHOW TABLES;

DESCRIBE Usuario;
DESCRIBE CodigoVerificacao;
DESCRIBE SessaoToken;
DESCRIBE SinaisVitais;
DESCRIBE Habitos;
DESCRIBE LembreteMedicao;
DESCRIBE DiarioSintomas;
DESCRIBE Vinculo;
DESCRIBE ConviteVinculo;
DESCRIBE ContatoEmergencia;
DESCRIBE ArquivoMedico;
DESCRIBE EstabilidadeClinica;
DESCRIBE ExportacaoRelatorio;
DESCRIBE LinhaBase;

SELECT * FROM Usuario;
SELECT * FROM CodigoVerificacao;
SELECT * FROM SessaoToken;
SELECT * FROM SinaisVitais;
SELECT * FROM Habitos;
SELECT * FROM LembreteMedicao;
SELECT * FROM DiarioSintomas;
SELECT * FROM Vinculo;
SELECT * FROM ConviteVinculo;
SELECT * FROM ContatoEmergencia;
SELECT * FROM ArquivoMedico;
SELECT * FROM EstabilidadeClinica;
SELECT * FROM ExportacaoRelatorio;
SELECT * FROM LinhaBase;