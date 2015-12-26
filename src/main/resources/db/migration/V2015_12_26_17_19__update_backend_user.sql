DROP TABLE `backend_user_role`;
ALTER TABLE `backend_user` change COLUMN `role` `role_id` int DEFAULT 0;