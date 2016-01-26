ALTER TABLE `backend_user_tracking`
ADD COLUMN `comments` VARCHAR(1024) NULL AFTER `time_created`;
