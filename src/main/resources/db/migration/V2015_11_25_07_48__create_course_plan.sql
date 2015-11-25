CREATE TABLE `zaozao`.`course_plan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `sub_title` VARCHAR(45) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `price` DOUBLE NOT NULL,
  PRIMARY KEY (`id`));
