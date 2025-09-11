-- =============================================
-- USERS TABLE
-- =============================================
CREATE TABLE `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(55) NOT NULL,
  `last_name` VARCHAR(55) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `passwordHash` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) DEFAULT NULL,
  `user_subscribed` INT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `users_chk_1` CHECK (REGEXP_LIKE(`email`, '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}'))
);


-- =============================================
-- AGENCY TABLE
-- =============================================
CREATE TABLE `agency` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_of_agency` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `passwordHash` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(15) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `agency_subscribed` INT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `agency_chk_1` CHECK (REGEXP_LIKE(`email`, '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')),
  CONSTRAINT `agency_chk_2` CHECK (CHAR_LENGTH(`phone_number`) = 10 OR CHAR_LENGTH(`phone_number`) = 15),
  CONSTRAINT `agency_chk_3` CHECK (`phone_number` REGEXP '^[0-9]+$')
);
