CREATE TABLE `course_tag` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `course_tag_rel` change COLUMN `course_type_id` `course_tag_id` int DEFAULT 0;