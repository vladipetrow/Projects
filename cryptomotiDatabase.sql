use cryptomoti;

drop table `agency`;
drop table  `post`;
drop table `user`;
drop table `subscriptions`;
drop table `type_of_apart`;

select * from users;
select * from post;
select * from subscriptions;
select * from agency;


CREATE TABLE `agency` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_of_agency` varchar(255) DEFAULT NULL,
  `email` varchar(55) NOT NULL,
  `passwordHash` varchar(255) NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `agency_subscibred` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `passwordHash` (`passwordHash`),
  KEY `agency_subscibred` (`agency_subscibred`),
  CONSTRAINT `agency_ibfk_1` FOREIGN KEY (`agency_subscibred`) REFERENCES `subscriptions` (`id`),
  CONSTRAINT `agency_chk_1` CHECK (regexp_like(`email`,_utf8mb4'[a-zA-Z0-9]+@[0-9a-zA-Z.]+')),
  CONSTRAINT `agency_chk_2` CHECK ((char_length(`phone_number`) = 10))
);

CREATE TABLE `subscriptions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `agency_id` int DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `type_of_apart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_type` (`name_type`)
);

INSERT INTO `type_of_apart` VALUES (1,'BUY'),(2,'RENT');

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(55) NOT NULL,
  `last_name` varchar(55) NOT NULL,
  `email` varchar(55) NOT NULL,
  `passwordHash` varchar(255) NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `user_subscribed` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `passwordHash` (`passwordHash`),
  KEY `user_subscribed` (`user_subscribed`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`user_subscribed`) REFERENCES `subscriptions` (`id`),
  CONSTRAINT `users_chk_1` CHECK (regexp_like(`email`,_utf8mb4'[a-zA-Z0-9]+@[0-9a-zA-Z.]+'))
);

CREATE TABLE `post` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `location` varchar(255) NOT NULL,
  `price` int NOT NULL,
  `area` int NOT NULL,
  `description` varchar(255) NOT NULL,
  `type_of_apart_id` int DEFAULT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int DEFAULT NULL,
  `agency_id` int DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `agency_id` (`agency_id`),
  KEY `user_id` (`user_id`),
  KEY `type_of_apart_id` (`type_of_apart_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`),
  CONSTRAINT `post_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `post_ibfk_3` FOREIGN KEY (`type_of_apart_id`) REFERENCES `type_of_apart` (`id`),
  CONSTRAINT `post_chk_1` CHECK ((`price` <= 100000000)),
  CONSTRAINT `post_chk_2` CHECK (((`user_id` = NULL) xor (`agency_id` = NULL)))
); 

--    SELECT expires_at FROM subscriptions WHERE user_id = 1;
--    
-- SELECT COUNT(p.post_id) as number_of_posts
-- FROM Users u
-- LEFT JOIN Post p ON u.id = p.user_id
-- WHERE u.id = 1;


-- SELECT *
-- 				FROM
--                    Post si
--                    JOIN type_of_apart t ON si.type_of_apart_id = t.id
--                 WHERE si.location =  "Пловдив" AND si.price <= 700 AND t.name_type = "BUY";