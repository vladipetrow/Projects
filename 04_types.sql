-- =============================================
-- APARTMENT TYPES TABLE
-- =============================================
CREATE TABLE `type_of_apart` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_type` (`name_type`)
);

-- Insert apartment types
INSERT INTO `type_of_apart` (`id`, `name_type`) VALUES 
(1, 'STUDIO'),
(2, 'ONE_BEDROOM'),
(3, 'TWO_BEDROOM'),
(4, 'THREE_BEDROOM'),
(5, 'FOUR_BEDROOM'),
(6, 'MULTI_BEDROOM'),
(7, 'HOUSE'),
(8, 'PENTHOUSE');