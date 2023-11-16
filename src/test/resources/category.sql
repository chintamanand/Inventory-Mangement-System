create table USERS(
  product_category_id int not null AUTO_INCREMENT,
  category_name varchar(100) not null,
  last_updated_at date,
  PRIMARY KEY ( product_category_id )
);

-- Run below queries to have category List in database.
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Home & Kitchen');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Beauty & Personal Care');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Clothing, Shoes & Jewelry');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Toys & games');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Health & Baby Care');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Appliances');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Sports');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Pet Supplies');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Office Supplies');
INSERT INTO `ivms`.`product_category` (`category_name`) VALUES ('Outdoor Supplies');