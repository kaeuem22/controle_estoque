create database controleEstoque_db;
use controleEstoque_db;

CREATE TABLE produto (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nome varchar(60) DEFAULT NULL,
  Preco double NOT NULL,
  Quantidade int NOT NULL,
  PRIMARY KEY (Id)
);