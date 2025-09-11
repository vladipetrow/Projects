-- Database Triggers for Email Uniqueness Across Users and Agency Tables
-- This ensures no email can exist in both tables simultaneously

-- Drop existing triggers if they exist
DROP TRIGGER IF EXISTS prevent_duplicate_email_users;
DROP TRIGGER IF EXISTS prevent_duplicate_email_agency;

-- Trigger to prevent duplicate emails when inserting into users table
DELIMITER $$
CREATE TRIGGER prevent_duplicate_email_users
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
  IF EXISTS (SELECT 1 FROM agency WHERE email = NEW.email) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Email already registered as Agency';
  END IF;
END$$
DELIMITER ;

-- Trigger to prevent duplicate emails when inserting into agency table
DELIMITER $$
CREATE TRIGGER prevent_duplicate_email_agency
BEFORE INSERT ON agency
FOR EACH ROW
BEGIN
  IF EXISTS (SELECT 1 FROM users WHERE email = NEW.email) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Email already registered as User';
  END IF;
END$$
DELIMITER ;

-- Also add triggers for UPDATE operations to prevent changing email to existing one
DELIMITER $$
CREATE TRIGGER prevent_duplicate_email_users_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
  IF NEW.email != OLD.email AND EXISTS (SELECT 1 FROM agency WHERE email = NEW.email) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Email already registered as Agency';
  END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER prevent_duplicate_email_agency_update
BEFORE UPDATE ON agency
FOR EACH ROW
BEGIN
  IF NEW.email != OLD.email AND EXISTS (SELECT 1 FROM users WHERE email = NEW.email) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Email already registered as User';
  END IF;
END$$
DELIMITER ;

-- Verify triggers were created
SHOW TRIGGERS;