DROP DATABASE IF EXISTS grades_manager;
CREATE DATABASE IF NOT EXISTS grades_manager DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE grades_manager;

CREATE TABLE IF NOT EXISTS `curso` (
	`codigo` INTEGER NOT NULL,
	`nome` VARCHAR(100) NOT NULL,
	PRIMARY KEY(`codigo`)
);
CREATE TABLE IF NOT EXISTS `disciplina` (
	`codigo` VARCHAR(100) NOT NULL,
	`nome` VARCHAR(100) NOT NULL,
	`professor`	VARCHAR(100) NOT NULL,
	`cod_curso`	INTEGER NOT NULL,
	PRIMARY KEY(codigo),
    FOREIGN KEY(cod_curso) REFERENCES curso(codigo)
);
CREATE TABLE IF NOT EXISTS `aluno` (
	`ra` INTEGER,
	`nome` VARCHAR(100) NOT NULL,
	`periodo` INTEGER NOT NULL,
	`cod_curso`	INTEGER NOT NULL,
	PRIMARY KEY(`ra`),
    FOREIGN KEY(`cod_curso`) REFERENCES `curso`(`codigo`)
);
CREATE TABLE IF NOT EXISTS `matricula` (
	`ano` INTEGER NOT NULL,
	`semestre` INTEGER NOT NULL,
	`cod_disciplina` VARCHAR(100) NOT NULL,
	`ra` INTEGER NOT NULL,
	`nota` FLOAT NOT NULL DEFAULT 0,
	`faltas` INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT PK_matricula PRIMARY KEY (ra, cod_disciplina, ano, semestre),
	FOREIGN KEY(`ra`) REFERENCES `aluno`(`ra`),
	FOREIGN KEY(`cod_disciplina`) REFERENCES `disciplina`(`codigo`)
);
