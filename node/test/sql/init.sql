SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `siyuan_test` ;
CREATE SCHEMA IF NOT EXISTS `siyuan_test` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `siyuan_test` ;

-- -----------------------------------------------------
-- Table `siyuan_test`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siyuan_test`.`users` ;

CREATE TABLE IF NOT EXISTS `siyuan_test`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `regtime` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `siyuan_test`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `siyuan_test`;
INSERT INTO `siyuan_test`.`users` (`id`, `username`, `password`, `email`, `regtime`) VALUES (1, 'aaa', '123', 'sas@aa.com', '2013-09-10 01:10:50');
INSERT INTO `siyuan_test`.`users` (`id`, `username`, `password`, `email`, `regtime`) VALUES (2, '小剑伯a', '3210', 'jjk@bb.org', '2013-09-13 08:08:42');

COMMIT;

