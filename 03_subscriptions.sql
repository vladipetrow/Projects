
-- =============================================
-- SUBSCRIPTION TIERS TABLE
-- =============================================
CREATE TABLE `subscription_tiers` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `tier_name` VARCHAR(50) NOT NULL UNIQUE,
  `tier_type` ENUM('USER', 'AGENCY') NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `max_posts` INT NOT NULL,
  `has_24_7_support` BOOLEAN DEFAULT FALSE,
  `description` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_tier_type` (`tier_type`)
);

-- Insert subscription tiers
INSERT INTO `subscription_tiers` (`tier_name`, `tier_type`, `price`, `max_posts`, `has_24_7_support`, `description`) VALUES
('USER_PREMIUM', 'USER', 9.99, 10, FALSE, 'User Premium - Up to 10 posts'),
('AGENCY_PREMIUM', 'AGENCY', 16.99, 15, FALSE, 'Agency Premium - Up to 15 posts'),
('USER_PREMIUM_ULTRA', 'USER', 19.99, 30, TRUE, 'User Premium Ultra - Up to 30 posts with 24/7 support'),
('AGENCY_PREMIUM_ULTRA', 'AGENCY', 39.99, 50, TRUE, 'Agency Premium Ultra - Up to 50 posts with 24/7 support');


-- =============================================
-- SUBSCRIPTIONS TABLE
-- =============================================
CREATE TABLE `subscriptions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT DEFAULT NULL UNIQUE,
  `agency_id` INT DEFAULT NULL UNIQUE,
  `expires_at` TIMESTAMP NULL DEFAULT NULL,
  `payment_status` ENUM('PENDING', 'ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'PENDING',
  `charge_id` VARCHAR(255) NOT NULL UNIQUE,
  `price` DECIMAL(10, 2) NOT NULL,
  `subscription_tier` VARCHAR(50) DEFAULT 'BASIC',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE CASCADE,
  KEY `idx_subscription_tier` (`subscription_tier`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_expires_at` (`expires_at`),
  CONSTRAINT `chk_subscription_user_or_agency` CHECK (
    (user_id = 0 AND agency_id != 0) OR 
    (user_id != 0 AND agency_id = 0)
  )
);