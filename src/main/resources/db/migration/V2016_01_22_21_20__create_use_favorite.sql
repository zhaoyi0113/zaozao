CREATE TABLE `user_favorite` (
  `id` INT UNSIGNED NOT NULL,
  `user_id` INT NOT NULL,
  `course_id` INT NOT NULL,
  `time_created` DATETIME NULL,
  PRIMARY KEY (`id`));
