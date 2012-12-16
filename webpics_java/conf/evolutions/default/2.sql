# --- !Ups

INSERT INTO `security_role` (`id`, `role_name`) VALUES
(1, 'admin'),
(2, 'user'),
(2, 'viewer');


INSERT INTO `users` (`id`, `email`, `name`, `last_login`, `active`, `email_validated`) VALUES
(1, 'admin@example.com', 'admin', '2012-10-07 15:36:39', 1, 1);

INSERT INTO `linked_account` (`id`, `user_id`, `provider_user_id`, `provider_key`) VALUES
(1, 1, '$10$2xfCbZbTs0lWuTYEfr.Sle2kQxfSnYOanzdpU3cj8Nx2KOqw5eqrS', 'password');


INSERT INTO `users_security_role` (`users_id`, `security_role_id`) VALUES
(1, 1);


INSERT INTO `album` (`id`, `name`) VALUES
(1, 'album 1');


# --- !Downs