CREATE DATABASE vittasync;
USE vittasync;

CREATE TABLE Usuario (
    id INT AUTO_INCREMENT,
    cpf VARCHAR(11) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(64) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    conselho VARCHAR(30),
    priv_compartilhar_diario BOOLEAN,
    priv_compartilhar_habitos BOOLEAN,
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
    fc_bpm INT,
    fr_rpm INT,
    pa_sistolica INT,
    pa_diastolica INT,
    temp_celcius DOUBLE,
    spo2_porcento INT,
    data_registro DATETIME NOT NULL,
    data_modificacao DATETIME,
    CONSTRAINT fk_sinais_paciente FOREIGN KEY (paciente_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE Habitos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    horas_sono INT,
    minutos_exercicio INT,
    data_referencia DATE NOT NULL,
    data_registro DATETIME NOT NULL,
    data_modificacao DATETIME,
    CONSTRAINT fk_habitos_paciente FOREIGN KEY (paciente_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE LembreteMedicao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    dias_semana VARCHAR(100) NOT NULL,
    horario TIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuario_lembrete FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

CREATE TABLE DiarioSintomas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    sintoma VARCHAR(255) NOT NULL,
    intensidade_dor INT NOT NULL,
    data_referencia DATE NOT NULL,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    FOREIGN KEY (paciente_id) REFERENCES usuario(id)
);

CREATE TABLE SolicitacaoVinculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitante_id INT NOT NULL,
    paciente_id INT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
	tipo VARCHAR(20) NOT NULL,
    status ENUM(
        'PENDENTE',
        'ACEITO',
        'RECUSADO',
        'EXPIRADO'
    ) DEFAULT 'PENDENTE',
    utilizado BOOLEAN DEFAULT FALSE,
    expira_em TIMESTAMP NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (solicitante_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE,
    FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
);

CREATE TABLE PacienteMedico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (paciente_id, medico_id),
    FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE,
    FOREIGN KEY (medico_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
);

CREATE TABLE PacienteResponsavel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    responsavel_id INT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (paciente_id, responsavel_id),
    FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE,
    FOREIGN KEY (responsavel_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
);


SHOW TABLES;

DESCRIBE Usuario;
DESCRIBE CodigoVerificacao;
DESCRIBE SessaoToken;
DESCRIBE SinaisVitais;
DESCRIBE Habitos;
DESCRIBE LembreteMedicao;
DESCRIBE DiarioSintomas;

SELECT * FROM Usuario;
SELECT * FROM CodigoVerificacao;
SELECT * FROM SessaoToken;
SELECT * FROM SinaisVitais;
SELECT * FROM Habitos;
SELECT * FROM LembreteMedicao;
SELECT * FROM DiarioSintomas;