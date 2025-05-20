use cryptomoti;
drop table `agency`;
drop table  `post`;
drop table `users`;
drop table `subscriptions`;
drop table `type_of_apart`;

select * from users;
select * from post;
select * from subscriptions;
select * from agency;

CREATE TABLE `agency` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_of_agency` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `passwordHash` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(15) DEFAULT NULL, -- Adjusted to allow for country code or better validation
  `address` VARCHAR(255) DEFAULT NULL,
  `agency_subscribed` INT DEFAULT NULL, -- Fixed typo
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `agency_chk_1` CHECK (REGEXP_LIKE(`email`, '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')),
  CONSTRAINT `agency_chk_2` CHECK (CHAR_LENGTH(`phone_number`) = 10 OR CHAR_LENGTH(`phone_number`) = 15), -- Enforces either 10 digits or 15 (with country code)
  CONSTRAINT `agency_chk_3` CHECK (`phone_number` REGEXP '^[0-9]+$') -- Ensures only numbers are in the phone number
);

CREATE TABLE `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(55) NOT NULL,
  `last_name` VARCHAR(55) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `passwordHash` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) DEFAULT NULL,
  `user_subscribed` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `users_chk_1` CHECK (REGEXP_LIKE(`email`, '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}'))
);

CREATE TABLE `post` (
  `post_id` INT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(255) NOT NULL,
  `price` INT NOT NULL CHECK (price <= 100000000),
  `area` INT NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `type_of_apart_id` INT DEFAULT NULL,
  `post_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` INT DEFAULT NULL,
  `agency_id` INT DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `agency_id` (`agency_id`),
  KEY `user_id` (`user_id`),
  KEY `type_of_apart_id` (`type_of_apart_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`),
  CONSTRAINT `post_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `post_ibfk_3` FOREIGN KEY (`type_of_apart_id`) REFERENCES `type_of_apart` (`id`),
  CONSTRAINT `post_chk_2` CHECK (
      (user_id = 0 AND agency_id != 0) OR 
      (user_id != 0 AND agency_id = 0)
  )
);

CREATE TABLE `post_images` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE `subscriptions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT DEFAULT NULL UNIQUE,
  `agency_id` INT DEFAULT NULL UNIQUE, 
  `expires_at` TIMESTAMP NULL DEFAULT NULL,
  `btc_status` ENUM('PENDING', 'ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'PENDING',
  `invoice_id` VARCHAR(255) NOT NULL UNIQUE,
  `price` DECIMAL(10, 2) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_subscription_user_or_agency` CHECK (
    (user_id = 0 AND agency_id != 0) OR 
    (user_id != 0 AND agency_id = 0)
  )
);

CREATE TABLE `type_of_apart` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_type` (`name_type`)
);

INSERT INTO `type_of_apart` VALUES (1, 'BUY'), (2, 'RENT');

