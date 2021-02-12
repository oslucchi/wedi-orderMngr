-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: 192.168.60.184    Database: orderMngr
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.45-MariaDB-0+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Articles`
--

DROP TABLE IF EXISTS `Articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Articles` (
  `idArticle` int(11) NOT NULL AUTO_INCREMENT,
  `refERP` varchar(15) DEFAULT NULL,
  `description` varchar(120) DEFAULT NULL,
  `length` decimal(6,1) DEFAULT NULL,
  `width` decimal(6,1) DEFAULT NULL,
  `heigth` decimal(6,1) DEFAULT NULL,
  `weigth` decimal(6,2) DEFAULT NULL,
  `category` varchar(3) DEFAULT NULL,
  `unityOfMeasure` varchar(3) DEFAULT NULL,
  `rateOfConversion` decimal(6,4) DEFAULT NULL,
  `grossPrice` decimal(6,2) DEFAULT NULL,
  `buyPrice` decimal(6,2) DEFAULT NULL,
  PRIMARY KEY (`idArticle`)
) ENGINE=InnoDB AUTO_INCREMENT=656 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CustomerDelivery`
--

DROP TABLE IF EXISTS `CustomerDelivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CustomerDelivery` (
  `idCustomerDelivery` int(11) NOT NULL AUTO_INCREMENT,
  `idCustomer` int(11) DEFAULT NULL,
  `address` varchar(90) DEFAULT NULL,
  `zipCode` varchar(5) DEFAULT NULL,
  `city` varchar(90) DEFAULT NULL,
  `province` varchar(2) DEFAULT NULL,
  `notes` varchar(90) DEFAULT NULL,
  `logisticCommEmail` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`idCustomerDelivery`)
) ENGINE=InnoDB AUTO_INCREMENT=1564 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Customers`
--

DROP TABLE IF EXISTS `Customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Customers` (
  `idCustomers` int(11) NOT NULL AUTO_INCREMENT,
  `refERP` varchar(12) DEFAULT NULL,
  `description` varchar(60) DEFAULT NULL,
  `logisticCommEmail` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`idCustomers`)
) ENGINE=InnoDB AUTO_INCREMENT=1378 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Journal`
--

DROP TABLE IF EXISTS `Journal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Journal` (
  `idJournal` int(11) NOT NULL AUTO_INCREMENT,
  `tableName` varchar(45) DEFAULT NULL,
  `actionPerformed` varchar(45) DEFAULT NULL,
  `statement` blob,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idJournal`)
) ENGINE=InnoDB AUTO_INCREMENT=51411 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Message`
--

DROP TABLE IF EXISTS `Message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Message` (
  `idMessage` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `recipient` varchar(32) DEFAULT NULL,
  `sender` varchar(32) DEFAULT NULL,
  `text` varchar(2048) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  `senderToken` varchar(64) DEFAULT NULL,
  `recipientToken` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`idMessage`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OrderDetails`
--

DROP TABLE IF EXISTS `OrderDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrderDetails` (
  `idOrderDetails` int(11) NOT NULL AUTO_INCREMENT,
  `idOrder` int(11) DEFAULT NULL,
  `idArticle` int(11) DEFAULT NULL,
  `quantity` decimal(6,2) DEFAULT NULL,
  `cost` decimal(6,2) DEFAULT NULL,
  `sourceIssue` tinyint(4) DEFAULT NULL,
  `piecesFromSqm` int(11) DEFAULT NULL,
  PRIMARY KEY (`idOrderDetails`)
) ENGINE=InnoDB AUTO_INCREMENT=3974 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OrderNotes`
--

DROP TABLE IF EXISTS `OrderNotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrderNotes` (
  `idOrderNotes` int(11) NOT NULL AUTO_INCREMENT,
  `idOrder` int(11) DEFAULT NULL,
  `note` blob,
  PRIMARY KEY (`idOrderNotes`)
) ENGINE=InnoDB AUTO_INCREMENT=306 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OrderShipment`
--

DROP TABLE IF EXISTS `OrderShipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrderShipment` (
  `idOrderShipment` int(11) NOT NULL AUTO_INCREMENT,
  `idOrder` int(11) DEFAULT NULL,
  `forwarder` varchar(3) DEFAULT NULL,
  `palletLength` int(11) DEFAULT NULL,
  `palletWidth` int(11) DEFAULT NULL,
  `palletHeigth` int(11) DEFAULT NULL,
  `palletWeigth` int(11) DEFAULT NULL,
  `numberOfItemsToShip` int(11) DEFAULT NULL,
  `forwarderCost` decimal(6,2) DEFAULT NULL,
  `clientCost` decimal(6,2) DEFAULT NULL,
  `assemblyDate` datetime DEFAULT NULL,
  `shipmentDate` datetime DEFAULT NULL,
  `note` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`idOrderShipment`)
) ENGINE=InnoDB AUTO_INCREMENT=967 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Orders`
--

DROP TABLE IF EXISTS `Orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Orders` (
  `idOrder` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(3) DEFAULT NULL,
  `idCustomer` int(11) NOT NULL,
  `idCustomerDelivery` int(11) DEFAULT NULL,
  `customerOrderRef` varchar(64) DEFAULT NULL,
  `requestedAssemblyDate` datetime DEFAULT NULL,
  `effectiveAssemblyDate` datetime DEFAULT NULL,
  `shipmentDate` datetime DEFAULT NULL,
  `orderRef` varchar(12) DEFAULT NULL,
  `transportDocNum` varchar(15) DEFAULT NULL,
  `forwarder` varchar(3) DEFAULT NULL,
  `forwarderCost` decimal(6,2) DEFAULT NULL,
  `clientCost` decimal(6,2) DEFAULT NULL,
  `assemblyTimeAuto` int(11) DEFAULT NULL,
  `assemblyTime` int(11) DEFAULT NULL,
  `palletCost` int(11) DEFAULT NULL,
  `insuranceCost` int(11) DEFAULT NULL,
  `serviceShipment` tinyint(4) DEFAULT NULL,
  `compositionBoards` int(11) DEFAULT '-1',
  `compositionTrays` int(11) DEFAULT '-1',
  `compositionDesign` int(11) DEFAULT '-1',
  `compositionAccessories` int(11) DEFAULT '-1',
  `orderValue` double DEFAULT '-1',
  `confirmationEmail` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`idOrder`),
  KEY `Status_IDX` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=838 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PackagingStats`
--

DROP TABLE IF EXISTS `PackagingStats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PackagingStats` (
  `idPackagingStats` int(11) NOT NULL AUTO_INCREMENT,
  `idOrder` int(11) DEFAULT NULL,
  `autoStartTime` decimal(13,0) DEFAULT NULL,
  `autoEndTime` decimal(13,0) DEFAULT NULL,
  `manualTime` int(11) DEFAULT NULL,
  PRIMARY KEY (`idPackagingStats`)
) ENGINE=InnoDB AUTO_INCREMENT=793 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UsersData`
--

DROP TABLE IF EXISTS `UsersData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UsersData` (
  `idUsersData` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(45) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`idUsersData`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-04 19:09:45
