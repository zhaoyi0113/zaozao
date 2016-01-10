CREATE TABLE `user_course_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `course_id` INT NOT NULL,
  `access_flag` VARCHAR(45) NOT NULL,
  `time_created` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
