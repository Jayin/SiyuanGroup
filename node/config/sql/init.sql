SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `siyuan` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `siyuan` ;

-- -----------------------------------------------------
-- Table `siyuan`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `emailverified` TINYINT NOT NULL,
  `olinetime` INT NOT NULL,
  `regip` VARCHAR(45) NULL,
  `regdate` DATETIME NULL,
  `gender` TINYINT NULL,
  `birthday` DATE NULL,
  `signature` VARCHAR(140) NULL COMMENT '个性签名',
  `tags` VARCHAR(200) NULL COMMENT '标签',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `regdate` DATETIME NULL,
  `lastip` VARCHAR(45) NULL,
  `lastdate` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`tweets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`tweets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `content` VARCHAR(280) NOT NULL,
  `posttime` DATETIME NOT NULL,
  `location` VARCHAR(45) NULL,
  `device` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tweets_users_idx` (`userid` ASC),
  CONSTRAINT `fk_tweets_users`
    FOREIGN KEY (`userid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`source_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`source_types` (
  `id` TINYINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
COMMENT = 'tweet/post/photo/comment';


-- -----------------------------------------------------
-- Table `siyuan`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`comments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `sourcetypeid` TINYINT NOT NULL,
  `sourceid` INT NOT NULL,
  `content` VARCHAR(280) NOT NULL,
  `posttime` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_comments_users1_idx` (`userid` ASC),
  INDEX `fk_comments_source_types1_idx` (`sourcetypeid` ASC),
  CONSTRAINT `fk_comments_users1`
    FOREIGN KEY (`userid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_source_types1`
    FOREIGN KEY (`sourcetypeid`)
    REFERENCES `siyuan`.`source_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`at`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`at` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `targetid` INT NOT NULL,
  `sourcetypeid` TINYINT NOT NULL,
  `sourceid` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_at_users1_idx` (`targetid` ASC),
  INDEX `fk_at_source_types1_idx` (`sourcetypeid` ASC),
  CONSTRAINT `fk_at_users1`
    FOREIGN KEY (`targetid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_at_source_types1`
    FOREIGN KEY (`sourcetypeid`)
    REFERENCES `siyuan`.`source_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`groups` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ownerid` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(280) NOT NULL,
  `createtime` DATETIME NOT NULL,
  `avatar` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_groups_users1_idx` (`ownerid` ASC),
  CONSTRAINT `fk_groups_users1`
    FOREIGN KEY (`ownerid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`albums`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`albums` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `createtime` DATETIME NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_albums_users1_idx` (`userid` ASC),
  CONSTRAINT `fk_albums_users1`
    FOREIGN KEY (`userid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`photos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`photos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `albumid` INT NOT NULL,
  `filename` VARCHAR(45) NOT NULL,
  `content` VARCHAR(280) NULL,
  `posttime` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_photos_albums1_idx` (`albumid` ASC),
  CONSTRAINT `fk_photos_albums1`
    FOREIGN KEY (`albumid`)
    REFERENCES `siyuan`.`albums` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`posts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`posts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `content` VARCHAR(8000) NOT NULL,
  `posttime` DATETIME NOT NULL,
  `location` VARCHAR(45) NULL,
  `ip` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_posts_users1_idx` (`userid` ASC),
  CONSTRAINT `fk_posts_users1`
    FOREIGN KEY (`userid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`activity_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`activity_status` (
  `id` TINYINT NOT NULL,
  `name` VARCHAR(45) NULL COMMENT '活动状态。',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ownerid` INT NOT NULL,
  `groupid` INT NULL,
  `content` VARCHAR(45) NOT NULL,
  `maxnum` SMALLINT NOT NULL COMMENT '最大人数',
  `createtime` DATETIME NOT NULL,
  `starttime` DATETIME NOT NULL COMMENT '开始时间',
  `duration` INT NULL COMMENT '单位为分钟',
  `statusid` TINYINT NULL COMMENT '状态：接受报名、截止报名、活动结束、活动取消等',
  PRIMARY KEY (`id`),
  INDEX `fk_activities_users1_idx` (`ownerid` ASC),
  INDEX `fk_activities_groups1_idx` (`groupid` ASC),
  INDEX `statusid_idx` (`statusid` ASC),
  CONSTRAINT `fk_activities_users1`
    FOREIGN KEY (`ownerid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activities_groups1`
    FOREIGN KEY (`groupid`)
    REFERENCES `siyuan`.`groups` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `statusid`
    FOREIGN KEY (`statusid`)
    REFERENCES `siyuan`.`activity_status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`activity_registration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`activity_registration` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`user_activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`user_activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `activityid` INT NOT NULL,
  `iscanceled` VARCHAR(45) NULL COMMENT '取消报名',
  PRIMARY KEY (`id`),
  INDEX `fk_activity_reg_users1_idx` (`userid` ASC),
  INDEX `fk_activity_reg_activities1_idx` (`activityid` ASC),
  CONSTRAINT `fk_activity_reg_users1`
    FOREIGN KEY (`userid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_reg_activities1`
    FOREIGN KEY (`activityid`)
    REFERENCES `siyuan`.`activities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `siyuan`.`user_login`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `siyuan`.`user_login` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `time` DATETIME NOT NULL,
  `ip` VARCHAR(45) NULL,
  `location` VARCHAR(45) NULL,
  `device` VARCHAR(45) NULL,
  `onlinetime` INT NULL COMMENT '在线时间分钟\n',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `userid_idx` (`userid` ASC),
  CONSTRAINT `userid`
    FOREIGN KEY (`userid`)
    REFERENCES `siyuan`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `siyuan`.`source_types`
-- -----------------------------------------------------
START TRANSACTION;
USE `siyuan`;
INSERT INTO `siyuan`.`source_types` (`id`, `name`) VALUES (3, 'tweets');
INSERT INTO `siyuan`.`source_types` (`id`, `name`) VALUES (1, 'photos');
INSERT INTO `siyuan`.`source_types` (`id`, `name`) VALUES (2, 'posts');
INSERT INTO `siyuan`.`source_types` (`id`, `name`) VALUES (0, 'activities');

COMMIT;

