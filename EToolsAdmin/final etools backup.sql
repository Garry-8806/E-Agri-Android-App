-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.19


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema etools
--

CREATE DATABASE IF NOT EXISTS etools;
USE etools;

--
-- Definition of table `tbl_admin_transaction`
--

DROP TABLE IF EXISTS `tbl_admin_transaction`;
CREATE TABLE `tbl_admin_transaction` (
  `sr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(45) NOT NULL,
  `sp_id` varchar(45) NOT NULL,
  `total_amount` varchar(45) NOT NULL,
  `commision` varchar(45) NOT NULL,
  `date_time` varchar(45) NOT NULL,
  PRIMARY KEY (`sr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_admin_transaction`
--

/*!40000 ALTER TABLE `tbl_admin_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_admin_transaction` ENABLE KEYS */;


--
-- Definition of table `tbl_confirm_booking`
--

DROP TABLE IF EXISTS `tbl_confirm_booking`;
CREATE TABLE `tbl_confirm_booking` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(45) NOT NULL,
  `sp_id` varchar(45) NOT NULL,
  `vehicle_no` varchar(45) NOT NULL,
  `mobile_no` varchar(45) NOT NULL,
  `lat` varchar(45) NOT NULL,
  `lon` varchar(45) NOT NULL,
  `pickup_time` varchar(45) NOT NULL,
  `date` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  `return_time` varchar(45) NOT NULL,
  `amount` varchar(45) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_confirm_booking`
--

/*!40000 ALTER TABLE `tbl_confirm_booking` DISABLE KEYS */;
INSERT INTO `tbl_confirm_booking` (`id`,`customer_id`,`sp_id`,`vehicle_no`,`mobile_no`,`lat`,`lon`,`pickup_time`,`date`,`status`,`return_time`,`amount`,`description`) VALUES 
 (7,'dd@gmail.com','Ss@gmail.com','MH12NJ3156','7350456969','18.5604891','73.9278848','12:34 pm','11/05/2018','Delivered','','1000','Testing.');
/*!40000 ALTER TABLE `tbl_confirm_booking` ENABLE KEYS */;


--
-- Definition of table `tbl_delivery_details`
--

DROP TABLE IF EXISTS `tbl_delivery_details`;
CREATE TABLE `tbl_delivery_details` (
  `id` int(10) unsigned NOT NULL,
  `description` varchar(45) NOT NULL,
  `amount` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_delivery_details`
--

/*!40000 ALTER TABLE `tbl_delivery_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_delivery_details` ENABLE KEYS */;


--
-- Definition of table `tbl_services`
--

DROP TABLE IF EXISTS `tbl_services`;
CREATE TABLE `tbl_services` (
  `sr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `service1` varchar(45) NOT NULL,
  `service2` varchar(45) NOT NULL,
  `service3` varchar(45) NOT NULL,
  `service4` varchar(45) NOT NULL,
  `service5` varchar(45) NOT NULL,
  PRIMARY KEY (`sr`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_services`
--

/*!40000 ALTER TABLE `tbl_services` DISABLE KEYS */;
INSERT INTO `tbl_services` (`sr`,`email`,`service1`,`service2`,`service3`,`service4`,`service5`) VALUES 
 (1,'Ss@gmail.com','true','true','true','false','false');
/*!40000 ALTER TABLE `tbl_services` ENABLE KEYS */;


--
-- Definition of table `tbl_tool_requests`
--

DROP TABLE IF EXISTS `tbl_tool_requests`;
CREATE TABLE `tbl_tool_requests` (
  `sr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tool_name` varchar(45) NOT NULL,
  `sp_email` varchar(45) NOT NULL,
  `cust_email` varchar(45) NOT NULL,
  `request_on` varchar(45) NOT NULL,
  `delivered_on` varchar(45) NOT NULL DEFAULT '',
  `return_on` varchar(45) NOT NULL DEFAULT '',
  `time_in_hour` int(11) NOT NULL DEFAULT '0',
  `total_payment` double NOT NULL DEFAULT '0',
  `request_status` varchar(45) NOT NULL DEFAULT 'Not Viewed',
  `cost_per_hour` double NOT NULL DEFAULT '0',
  `latitude` varchar(45) NOT NULL DEFAULT '18.5808',
  `longitude` varchar(45) NOT NULL DEFAULT '73.9787',
  PRIMARY KEY (`sr`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_tool_requests`
--

/*!40000 ALTER TABLE `tbl_tool_requests` DISABLE KEYS */;
INSERT INTO `tbl_tool_requests` (`sr`,`tool_name`,`sp_email`,`cust_email`,`request_on`,`delivered_on`,`return_on`,`time_in_hour`,`total_payment`,`request_status`,`cost_per_hour`,`latitude`,`longitude`) VALUES 
 (2,'Tractor','ss@gmail.com','dd@gmail.com','28/02/2020 18:50:48','09/03/2020 14:37:01','09/03/2020 17:34:47',2,400,'Completed',200,'',''),
 (3,'Tractor','ss@gmail.com','dd@gmail.com','09/03/2020 18:39:35','09/03/2020 17:40:20','09/03/2020 18:55:01',1,200,'Completed',200,'0.0','0.0');
/*!40000 ALTER TABLE `tbl_tool_requests` ENABLE KEYS */;


--
-- Definition of table `tbl_tools_info`
--

DROP TABLE IF EXISTS `tbl_tools_info`;
CREATE TABLE `tbl_tools_info` (
  `sr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `cost` varchar(45) NOT NULL,
  `description` varchar(500) NOT NULL,
  `email` varchar(45) NOT NULL,
  `image_name` varchar(200) NOT NULL,
  `added_on` varchar(200) NOT NULL,
  `tool_status` varchar(45) NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`sr`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_tools_info`
--

/*!40000 ALTER TABLE `tbl_tools_info` DISABLE KEYS */;
INSERT INTO `tbl_tools_info` (`sr`,`title`,`cost`,`description`,`email`,`image_name`,`added_on`,`tool_status`) VALUES 
 (1,'Tractor','200','Testuing','ss@gmail.com','1582799038642.jpg','02/27/2020 15:53:58','Active');
/*!40000 ALTER TABLE `tbl_tools_info` ENABLE KEYS */;


--
-- Definition of table `tbl_user_reg`
--

DROP TABLE IF EXISTS `tbl_user_reg`;
CREATE TABLE `tbl_user_reg` (
  `username` varchar(45) NOT NULL,
  `address` text NOT NULL,
  `mobile` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `latitude` varchar(45) NOT NULL,
  `longitude` varchar(45) NOT NULL,
  `utype` varchar(45) NOT NULL,
  `registeron` varchar(45) NOT NULL,
  `status_` varchar(45) NOT NULL,
  `sr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`sr`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_user_reg`
--

/*!40000 ALTER TABLE `tbl_user_reg` DISABLE KEYS */;
INSERT INTO `tbl_user_reg` (`username`,`address`,`mobile`,`email`,`password`,`latitude`,`longitude`,`utype`,`registeron`,`status_`,`sr`) VALUES 
 ('Dinesh','pune','7350456969','dd@gmail.com','12345','0.0','0.0','User','11/05/2018 12:24:40','Active',1),
 ('Neelima Sutar','Maruti Nagar','9028430200','ss@gmail.com','12345','0.0','0.0','Service Provider','11/05/2018 12:24:15','Active',2);
/*!40000 ALTER TABLE `tbl_user_reg` ENABLE KEYS */;


--
-- Definition of table `tbl_valet_info`
--

DROP TABLE IF EXISTS `tbl_valet_info`;
CREATE TABLE `tbl_valet_info` (
  `sr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) NOT NULL,
  `valet_amount` double NOT NULL,
  PRIMARY KEY (`sr`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_valet_info`
--

/*!40000 ALTER TABLE `tbl_valet_info` DISABLE KEYS */;
INSERT INTO `tbl_valet_info` (`sr`,`user_id`,`valet_amount`) VALUES 
 (3,'Ss@gmail.com',2700),
 (4,'dd@gmail.com',1000);
/*!40000 ALTER TABLE `tbl_valet_info` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
