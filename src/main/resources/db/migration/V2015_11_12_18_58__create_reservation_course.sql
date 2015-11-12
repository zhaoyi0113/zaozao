CREATE TABLE `reservation_course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `mobile` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `date` DATETIME NULL,
  `children_birthdate` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
