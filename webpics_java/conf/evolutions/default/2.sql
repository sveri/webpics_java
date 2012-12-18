# --- !Ups


INSERT INTO `users` (`id`, `email`, `name`, `last_login`, `active`, `email_validated`) VALUES
(1, 'admin@example.com', 'admin', '2012-10-07 15:36:39', 1, 1);

INSERT INTO `security_role` (`id`, `role_name`) VALUES
(1, 'admin'),
(2, 'user'),
(3, 'viewer');

INSERT INTO `linked_account` (`id`, `user_id`, `provider_user_id`, `provider_key`) VALUES
(1, 1, '$2a$10$F78Dy7OdmJb.ajUmEUwMpemnLbhjURMybLsJlUfRBepn8mzzzdbsK', 'password');


INSERT INTO `users_security_role` (`users_id`, `security_role_id`) VALUES
(1, 1);


INSERT INTO `album` (`id`, `name`) VALUES
(1, 'album 1');


# --- !Downs

TRUNCATE TABLE security_role;
TRUNCATE TABLE users;
TRUNCATE TABLE linked_account;
TRUNCATE TABLE users_security_role;
TRUNCATE TABLE album;