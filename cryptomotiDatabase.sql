CREATE DATABASE cryptomoti;
use cryptomoti;
drop table `agency`;
drop table  `post`;
drop table `users`;
drop table `post_images`;
drop table `subscriptions`;
drop table `type_of_apart`;
drop table `subscription_tiers`;


USE cryptomoti;
SHOW TABLES LIKE 'post_images';

show tables;

delete from users where id = 6;

select * from users;
select * from post;
select * from subscriptions;
select * from agency;
select * from post_images;

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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
ALTER TABLE subscriptions CHANGE COLUMN btc_status payment_status ENUM('PENDING', 'ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'PENDING';

-- Rename invoice_id to charge_id (matches Coinbase Commerce terminology)
ALTER TABLE subscriptions CHANGE COLUMN invoice_id charge_id VARCHAR(255) NOT NULL UNIQUE;

-- Add comment to clarify the purpose
ALTER TABLE subscriptions MODIFY COLUMN payment_status ENUM('PENDING', 'ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'PENDING' COMMENT 'Payment status for crypto subscriptions (supports Bitcoin, Ethereum, Litecoin, etc.)';
ALTER TABLE subscriptions MODIFY COLUMN charge_id VARCHAR(255) NOT NULL UNIQUE COMMENT 'Coinbase Commerce charge ID for crypto payments';

INSERT INTO `type_of_apart` VALUES (1, 'BUY'), (2, 'RENT');


-- Update subscription tiers and pricing
-- Add new subscription types to the database

-- First, let's add a new column for subscription tier if it doesn't exist
ALTER TABLE subscriptions ADD COLUMN subscription_tier VARCHAR(50) DEFAULT 'BASIC';

-- Update existing subscriptions to BASIC tier
UPDATE subscriptions SET subscription_tier = 'BASIC' WHERE subscription_tier IS NULL;

-- Create a new table for subscription tiers configuration
CREATE TABLE IF NOT EXISTS subscription_tiers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tier_name VARCHAR(50) NOT NULL UNIQUE,
    tier_type ENUM('USER', 'AGENCY') NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    max_posts INT NOT NULL,
    has_24_7_support BOOLEAN DEFAULT FALSE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert the new subscription tiers
INSERT INTO subscription_tiers (tier_name, tier_type, price, max_posts, has_24_7_support, description) VALUES
('USER_PREMIUM', 'USER', 9.99, 10, FALSE, 'User Premium - Up to 10 posts'),
('AGENCY_PREMIUM', 'AGENCY', 16.99, 15, FALSE, 'Agency Premium - Up to 15 posts'),
('USER_PREMIUM_ULTRA', 'USER', 19.99, 30, TRUE, 'User Premium Ultra - Up to 30 posts with 24/7 support'),
('AGENCY_PREMIUM_ULTRA', 'AGENCY', 39.99, 50, TRUE, 'Agency Premium Ultra - Up to 50 posts with 24/7 support');

-- Add index for better performance
CREATE INDEX idx_subscription_tier ON subscriptions(subscription_tier);
CREATE INDEX idx_tier_type ON subscription_tiers(tier_type);


-- Add view_count column to post table (this is missing and causing issues)
ALTER TABLE post ADD COLUMN view_count INT DEFAULT 0;

-- Create index for better performance on view_count
CREATE INDEX idx_post_view_count ON post(view_count);

-- Verify the changes
DESCRIBE post;
DESCRIBE post_images;

-- Show that post_images table exists and has the right structure
SELECT 'post_images table structure:' as info;
SHOW CREATE TABLE post_images;

SELECT 'post table structure:' as info;
SHOW CREATE TABLE post;
