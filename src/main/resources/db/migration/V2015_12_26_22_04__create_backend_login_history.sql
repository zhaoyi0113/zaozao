CREATE TABLE `backend_login_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `login_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));
