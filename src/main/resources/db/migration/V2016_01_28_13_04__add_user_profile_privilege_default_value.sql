ALTER TABLE `user_profile_privilege`
CHANGE COLUMN `child_birthdate` `child_birthdate` INT(11) NULL DEFAULT 1 ,
CHANGE COLUMN `child_gender` `child_gender` INT(11) NULL DEFAULT 1 ,
CHANGE COLUMN `child_name` `child_name` INT(11) NULL DEFAULT 1 ,
CHANGE COLUMN `user_image` `user_image` INT(11) NULL DEFAULT 1 ,
CHANGE COLUMN `user_name` `user_name` INT(11) NULL DEFAULT 1 ;

ALTER TABLE `user_profile_privilege` DROP COLUMN `child_birthdate1`;