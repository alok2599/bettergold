CREATE TABLE `bgdb`.`user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `email_verified` bit(1) DEFAULT NULL,
  `mobile_verified` bit(1) DEFAULT NULL, 
  `kyc_verified` bit(1) DEFAULT NULL,
  `authenticator_key` varchar(255) DEFAULT NULL,
  `confirmation_token` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `last_password_reset_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
 );

INSERT INTO `bgdb`.`user` ( `username`, `password`, `firstname`, `lastname`, `email`, `email_verified`, `mobile_verified`, `kyc_verified`,`authenticator_key`, `confirmation_token`, `enabled`, `last_password_reset_date`)
VALUES 
( 'user@bettergold.in', '$2a$10$EbtAn6fz4AfV631rdXOe5e8w6NchSDEpMGgZBxUujQV/EG9Foh0qC', 'user fname', 'user lname', 'user@bettergold.in', 0, 0, 0, 'dsslk33wdf899', 'klsdf9834543dkf895ya', 1, '2016-01-01 00:00:00.0');

INSERT INTO `bgdb`.`user` ( `username`, `password`, `firstname`, `lastname`, `email`, `email_verified`, `mobile_verified`, `kyc_verified`,`authenticator_key`, `confirmation_token`, `enabled`, `last_password_reset_date`)
VALUES 
( 'trader@bettergold.in', '$2a$10$EbtAn6fz4AfV631rdXOe5e8w6NchSDEpMGgZBxUujQV/EG9Foh0qC', 'trader fname', 'trader lname', 'trader@bettergold.in', 0, 0, 0, 'dsslk33wdf899', 'klsdf9834543dkf895ya', 1, '2016-01-01 00:00:00.0');

INSERT INTO `bgdb`.`user` ( `username`, `password`, `firstname`, `lastname`, `email`, `email_verified`, `mobile_verified`, `kyc_verified`,`authenticator_key`, `confirmation_token`, `enabled`, `last_password_reset_date`)
VALUES 
( 'admin@bettergold.in', '$2a$10$EbtAn6fz4AfV631rdXOe5e8w6NchSDEpMGgZBxUujQV/EG9Foh0qC', 'admin fname', 'admin lname', 'admin@bettergold.in', 0, 0, 0, 'dsslk33wdf899', 'klsdf9834543dkf895ya', 1, '2016-01-01 00:00:00.0');

INSERT INTO `bgdb`.`user` ( `username`, `password`, `firstname`, `lastname`, `email`, `email_verified`, `mobile_verified`, `kyc_verified`,`authenticator_key`, `confirmation_token`, `enabled`, `last_password_reset_date`)
VALUES 
( 'bisabled@bettergold.in', '$2a$10$EbtAn6fz4AfV631rdXOe5e8w6NchSDEpMGgZBxUujQV/EG9Foh0qC', 'disabled fname', 'disabled lname', 'kisabled@bettergold.in', 0, 0, 0, 'dsslk33wdf899', 'klsdf9834543dkf895ya', 0, '2016-01-01 00:00:00.0');

CREATE TABLE `bgdb`.`authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
  
 );


INSERT INTO `bgdb`.`authority` (ID, NAME) VALUES (1, 'ROLE_USER');
INSERT INTO `bgdb`.`authority` (ID, NAME) VALUES (2, 'ROLE_TRADER');
INSERT INTO `bgdb`.`authority` (ID, NAME) VALUES (3, 'ROLE_ADMIN');

CREATE TABLE `bgdb`.`user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  KEY `FKgvxjs381k6f48d5d2yi11uh89` (`authority_id`),
  KEY `FKpqlsjpkybgos9w2svcri7j8xy` (`user_id`),
  CONSTRAINT `FKgvxjs381k6f48d5d2yi11uh89` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`),
  CONSTRAINT `FKpqlsjpkybgos9w2svcri7j8xy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
  
 );


INSERT INTO `bgdb`.`user_authority` (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO `bgdb`.`user_authority` (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO `bgdb`.`user_authority` (USER_ID, AUTHORITY_ID) VALUES (2, 2);
INSERT INTO `bgdb`.`user_authority` (USER_ID, AUTHORITY_ID) VALUES (3, 1);
INSERT INTO `bgdb`.`user_authority` (USER_ID, AUTHORITY_ID) VALUES (3, 2);
INSERT INTO `bgdb`.`user_authority` (USER_ID, AUTHORITY_ID) VALUES (3, 3);

CREATE TABLE `bgdb`.`pair` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
   
  `BASE_CURRENCY` varchar(100) NOT NULL,
  `QUOTE_CURRENCY` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  -- UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`BASE_CURRENCY`,`QUOTE_CURRENCY`)
 );

INSERT INTO `bgdb`.`pair` (SYMBOL, BASE_CURRENCY, QUOTE_CURRENCY) VALUES ('BTCINR','BTC', 'INR');
INSERT INTO `bgdb`.`pair` (SYMBOL, BASE_CURRENCY, QUOTE_CURRENCY) VALUES ('ETHINR','ETH', 'INR');
INSERT INTO `bgdb`.`pair` (SYMBOL, BASE_CURRENCY, QUOTE_CURRENCY) VALUES ('XRPINR','XRP', 'INR');
INSERT INTO `bgdb`.`pair` (SYMBOL, BASE_CURRENCY, QUOTE_CURRENCY) VALUES ('ETHBTC','ETH', 'BTC');

INSERT INTO `bgdb`.`BALANCE` (USERNAME, INR, BTC, ETH, XRP) VALUES ('trader@bettergold.in', 0,0,0,0);
INSERT INTO `bgdb`.`BALANCE` (USERNAME, INR, BTC, ETH, XRP) VALUES ('user@bettergold.in', 0,0,0,0);
