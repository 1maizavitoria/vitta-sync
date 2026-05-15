DROP DATABASE vittasync;
CREATE DATABASE vittasync;
USE vittasync;

CREATE TABLE Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    conselho VARCHAR(50),
    tipo VARCHAR(20) NOT NULL,
    data_nascimento DATE NOT NULL,
    priv_compartilhar_diario BOOLEAN DEFAULT FALSE,
    priv_compartilhar_habitos BOOLEAN DEFAULT FALSE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    calorias_consumidas INT,
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

SELECT * FROM Usuario;
SELECT * FROM Habitos;

