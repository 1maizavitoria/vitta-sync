DROP DATABASE vittasync;
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

    CONSTRAINT fk_sinais_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_sinais_registrado_por
        FOREIGN KEY (registrado_por_usuario_id)
        REFERENCES Usuario(id)
        ON DELETE SET NULL
);

CREATE TABLE Habitos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    registrado_por_usuario_id INT,
    horas_sono INT,
    minutos_exercicio INT,
    data_referencia DATE NOT NULL,
    data_registro DATETIME NOT NULL,
    data_modificacao DATETIME,

    CONSTRAINT fk_habitos_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_habitos_registrado_por
        FOREIGN KEY (registrado_por_usuario_id)
        REFERENCES Usuario(id)
        ON DELETE SET NULL
);

CREATE TABLE LembreteMedicao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    dias_semana VARCHAR(100) NOT NULL,
    horario TIME NOT NULL,
    enviar_email BOOLEAN DEFAULT TRUE,
    enviar_sms BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_usuario_lembrete
        FOREIGN KEY (usuario_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
);

CREATE TABLE DiarioSintomas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    sintoma VARCHAR(255) NOT NULL,
    intensidade_dor INT NOT NULL,
    data_referencia DATE NOT NULL,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,

    FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
);

CREATE TABLE Vinculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    usuario_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    funcao VARCHAR(50),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (paciente_id, usuario_id),

    FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id),

    FOREIGN KEY (usuario_id)
        REFERENCES Usuario(id)
);

CREATE TABLE ConviteVinculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    expira_em TIMESTAMP NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
);

CREATE TABLE ContatoEmergencia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    data_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_modificacao DATETIME NULL,

    CONSTRAINT fk_contato_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE
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

    CONSTRAINT fk_medico
        FOREIGN KEY (medico_id)
        REFERENCES Usuario(id),

    CONSTRAINT fk_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
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

    CONSTRAINT fk_evento_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES Usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_evento_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES Usuario(id)
        ON DELETE SET NULL
);


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