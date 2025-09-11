-- =============================================
-- POSTS TABLE
-- =============================================
CREATE TABLE `post` (
  `post_id` INT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(255) NOT NULL,
  `price` INT NOT NULL CHECK (price <= 100000000),
  `area` INT NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `type_of_apart_id` INT DEFAULT NULL,
  `transaction_type` ENUM('BUY', 'RENT') DEFAULT 'BUY',
  `post_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `view_count` INT DEFAULT 0,
  `user_id` INT DEFAULT NULL,
  `agency_id` INT DEFAULT NULL,
  `is_promoted` BOOLEAN DEFAULT FALSE,
  `name_type` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `agency_id` (`agency_id`),
  KEY `user_id` (`user_id`),
  KEY `type_of_apart_id` (`type_of_apart_id`),
  KEY `idx_post_transaction_type` (`transaction_type`),
  KEY `idx_post_view_count` (`view_count`),
  KEY `idx_location` (`location`),
  KEY `idx_price` (`price`),
  KEY `idx_post_date` (`post_date`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE CASCADE,
  CONSTRAINT `post_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `post_ibfk_3` FOREIGN KEY (`type_of_apart_id`) REFERENCES `type_of_apart` (`id`),
  CONSTRAINT `post_chk_2` CHECK (
      (user_id = 0 AND agency_id != 0) OR 
      (user_id != 0 AND agency_id = 0)
  )
);


-- =============================================
-- POST IMAGES TABLE
-- =============================================
CREATE TABLE `post_images` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `post_id` INT NOT NULL,
  `image_url` VARCHAR(500) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`) ON DELETE CASCADE,
  KEY `idx_post_id` (`post_id`),
  KEY `idx_image_url` (`image_url`)
);

-- =============================================
-- POST VIEWS TABLE (for analytics)
-- =============================================
CREATE TABLE `post_views` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `post_id` INT NOT NULL,
  `user_id` INT NULL,
  `viewed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `ip_address` VARCHAR(45),
  FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`) ON DELETE CASCADE,
  KEY `idx_post_views_post_id` (`post_id`),
  KEY `idx_post_views_viewed_at` (`viewed_at`)
);