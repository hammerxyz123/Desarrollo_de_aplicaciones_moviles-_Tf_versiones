-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema colegio
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema colegio
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `colegio` DEFAULT CHARACTER SET utf8 ;
USE `colegio` ;

-- -----------------------------------------------------
-- Table `colegio`.`festividad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colegio`.`festividad` (
  `fes_id` INT NOT NULL AUTO_INCREMENT,
  `fes_nombre` VARCHAR(45) NOT NULL,
  `fes_descripcion` VARCHAR(45) NOT NULL,
  `fes_abono` VARCHAR(45) NOT NULL,
  `fes_fecha` VARCHAR(45) NOT NULL,
  `fes_observacion` VARCHAR(225) NOT NULL,
  PRIMARY KEY (`fes_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `colegio`.`alumno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colegio`.`alumno` (
  `alu_id` INT NOT NULL AUTO_INCREMENT,
  `alu_nombre` VARCHAR(45) NOT NULL,
  `alu_fechaNac` VARCHAR(45) NOT NULL,
  `alu_contacto` VARCHAR(45) NOT NULL,
  `alu_seccion` VARCHAR(45) NOT NULL,
  `alu_observacion` VARCHAR(225) NOT NULL,
  PRIMARY KEY (`alu_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `colegio`.`calificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colegio`.`calificacion` (
  `cal_id` INT NOT NULL AUTO_INCREMENT,
  `cal_nota1` INT NOT NULL,
  `cal_nota2` INT NOT NULL,
  `cal_nota3` INT NOT NULL,
  `cal_nota4` INT NOT NULL,
  `cal_observacion` VARCHAR(225) NOT NULL,
  PRIMARY KEY (`cal_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `colegio`.`pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `colegio`.`pago` (
  `pa_id` INT NOT NULL AUTO_INCREMENT,
  `pa_pago1` TINYINT(1) NOT NULL,
  `pa_pago2` TINYINT(1) NOT NULL,
  `pa_pago3` TINYINT(1) NOT NULL,
  `pa_pago4` TINYINT(1) NOT NULL,
  `pa_observacion` VARCHAR(225) NOT NULL,
  PRIMARY KEY (`pa_id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO alumno values(0,'Alvaro','12/12/2019','987654321','5 años','');
INSERT INTO alumno values(0,'Marco','11/11/2019','987654311','4 años','');
select * from alumno
INSERT INTO calificacion values(0,1,0,0,0,'');
INSERT INTO calificacion values(0,19,20,0,0,'');
select * from calificacion
INSERT INTO pago values(0,1,1,0,0,'seguir pagando');
INSERT INTO pago values(0,1,0,0,0,'seguir pagando');
select * from pago