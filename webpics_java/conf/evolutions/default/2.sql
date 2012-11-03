# --- !Ups

INSERT INTO `security_role` (`id`, `role_name`) VALUES
(1, 'admin'),
(2, 'user');

INSERT INTO `users` (`id`, `email`, `name`, `last_login`, `active`, `email_validated`) VALUES
(1, 'sveri@sveri.de', 'admin', '2012-10-07 15:36:39', 1, 1);

INSERT INTO `linked_account` (`id`, `user_id`, `provider_user_id`, `provider_key`) VALUES
(1, 1, '$2a$10$C.7lWcHjBVmLpdP96tfUp.0TsAh68QJaNAbCBoPXCjG3uI1zKVAbG', 'password');


INSERT INTO `users_security_role` (`users_id`, `security_role_id`) VALUES
(1, 1);


INSERT INTO `album` (`id`, `name`) VALUES
(1, 'album 1');


# --- !Downs