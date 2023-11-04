CREATE TABLE localizacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(255) NOT NULL,
    data_posicao DATETIME NOT NULL,
    velocidade INT NOT NULL,
    longitude VARCHAR(20) NOT NULL,
    latitude VARCHAR(20) NOT NULL,
    ignicao BOOLEAN NOT NULL
);