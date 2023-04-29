-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: gradebook
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assigned`
--

DROP TABLE IF EXISTS `assigned`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assigned` (
  `assigned_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `assignment_id` int NOT NULL,
  `grade` int DEFAULT NULL,
  PRIMARY KEY (`assigned_id`),
  KEY `student_id` (`student_id`),
  KEY `assignment_id` (`assignment_id`),
  CONSTRAINT `assigned_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `assigned_ibfk_2` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`assignment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assigned`
--

LOCK TABLES `assigned` WRITE;
/*!40000 ALTER TABLE `assigned` DISABLE KEYS */;
INSERT INTO `assigned` VALUES (1,1,1,15),(2,1,2,NULL),(3,1,3,75);
/*!40000 ALTER TABLE `assigned` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
  `assignment_id` int NOT NULL AUTO_INCREMENT,
  `assignment_name` varchar(255) NOT NULL,
  `assignment_description` varchar(255) NOT NULL,
  `assignment_value` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`assignment_id`),
  UNIQUE KEY `idx_unique_assignment` (`assignment_name`,`category_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
INSERT INTO `assignments` VALUES (1,'hw1','hw',20,1),(2,'hw2','hw',20,1),(3,'exam1','exam',100,2);
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  `weight` int NOT NULL,
  `class_id` int DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `idx_unique_category` (`category_name`,`class_id`),
  KEY `class_id` (`class_id`),
  CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'homework',52,1),(2,'exams',13,1),(3,'homework',25,2),(4,'exams',73,2),(5,'exams',16,3),(6,'projects',13,1),(7,'homework',73,3),(8,'projects',54,2),(9,'homework',96,4),(10,'homework',58,5),(11,'exams',95,4),(12,'projects',8,3),(13,'exams',40,5),(14,'exams',8,6),(15,'exams',76,7),(16,'projects',77,4),(17,'homework',27,6),(18,'homework',91,7),(19,'projects',12,5),(20,'homework',97,8);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classes` (
  `class_id` int NOT NULL AUTO_INCREMENT,
  `course_number` varchar(255) NOT NULL,
  `term` varchar(255) NOT NULL,
  `section` int NOT NULL,
  `class_description` varchar(255) NOT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (1,'cs421','f22',1,'high-level'),(2,'cs452','f23',1,'toolset'),(3,'cs410','s23',1,'Total'),(4,'cs410','s24',1,'Organic'),(5,'cs452','f22',1,'help-desk'),(6,'cs452','f23',2,'methodical'),(7,'cs410','f22',1,'Digitized'),(8,'cs421','f22',2,'Visionary'),(9,'cs421','f23',1,'Fundamental'),(10,'cs421','f23',2,'Synergized');
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enroll`
--

DROP TABLE IF EXISTS `enroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enroll` (
  `enroll_id` int NOT NULL AUTO_INCREMENT,
  `class_id` int NOT NULL,
  `student_id` int NOT NULL,
  PRIMARY KEY (`enroll_id`),
  KEY `class_id` (`class_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `enroll_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`),
  CONSTRAINT `enroll_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=302 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enroll`
--

LOCK TABLES `enroll` WRITE;
/*!40000 ALTER TABLE `enroll` DISABLE KEYS */;
INSERT INTO `enroll` VALUES (1,2,73),(2,5,24),(3,8,69),(4,5,10),(5,8,7),(6,1,69),(7,2,7),(8,2,49),(9,9,86),(10,2,60),(11,9,74),(12,9,96),(13,8,94),(14,2,62),(15,3,62),(16,3,7),(17,1,15),(18,8,39),(19,7,97),(20,5,72),(21,9,98),(22,5,38),(23,8,18),(24,10,84),(25,4,20),(26,7,89),(27,8,7),(28,9,72),(29,5,43),(30,4,48),(31,2,3),(32,9,27),(33,8,26),(34,9,61),(35,10,63),(36,3,91),(37,10,58),(38,10,5),(39,5,92),(40,3,56),(41,6,94),(42,3,99),(43,2,42),(44,2,67),(45,9,30),(46,10,21),(47,10,15),(48,9,98),(49,10,22),(50,7,64),(51,7,58),(52,2,50),(53,1,8),(54,2,48),(55,1,67),(56,8,32),(57,5,56),(58,3,91),(59,1,35),(60,2,54),(61,6,1),(62,2,25),(63,2,13),(64,1,73),(65,6,37),(66,4,13),(67,9,54),(68,7,7),(69,7,23),(70,8,2),(71,6,35),(72,6,47),(73,3,79),(74,2,41),(75,10,92),(76,6,56),(77,10,1),(78,10,41),(79,10,52),(80,7,17),(81,6,2),(82,9,54),(83,9,90),(84,4,82),(85,2,6),(86,10,21),(87,7,7),(88,9,95),(89,10,5),(90,3,49),(91,8,67),(92,9,71),(93,9,62),(94,4,53),(95,1,65),(96,4,44),(97,9,21),(98,7,92),(99,10,57),(100,6,78),(101,2,53),(102,1,53),(103,1,31),(104,7,89),(105,7,94),(106,2,1),(107,4,4),(108,9,71),(109,8,55),(110,8,44),(111,8,60),(112,9,33),(113,1,82),(114,7,89),(115,7,31),(116,4,90),(117,6,30),(118,2,6),(119,7,60),(120,9,65),(121,3,89),(122,7,56),(123,1,15),(124,10,53),(125,4,97),(126,9,17),(127,9,69),(128,5,33),(129,3,55),(130,5,15),(131,6,20),(132,2,44),(133,4,60),(134,8,31),(135,8,21),(136,10,17),(137,10,9),(138,8,85),(139,1,63),(140,6,22),(141,1,11),(142,9,61),(143,3,56),(144,6,11),(145,3,89),(146,7,92),(147,3,20),(148,2,98),(149,6,53),(150,5,37),(151,5,51),(152,1,38),(153,8,64),(154,5,37),(155,1,76),(156,3,79),(157,2,81),(158,3,21),(159,9,91),(160,8,47),(161,6,44),(162,5,46),(163,10,43),(164,9,83),(165,5,76),(166,3,97),(167,9,29),(168,3,46),(169,1,60),(170,5,42),(171,1,64),(172,2,79),(173,3,32),(174,1,17),(175,3,83),(176,10,18),(177,6,92),(178,7,19),(179,9,20),(180,2,43),(181,3,58),(182,6,8),(183,6,11),(184,7,40),(185,5,43),(186,8,68),(187,4,12),(188,5,64),(189,5,65),(190,5,51),(191,3,88),(192,8,96),(193,4,51),(194,1,5),(195,6,15),(196,4,26),(197,4,58),(198,10,76),(199,7,9),(200,9,2),(201,6,70),(202,3,90),(203,1,31),(204,7,100),(205,7,57),(206,10,51),(207,3,47),(208,3,71),(209,3,32),(210,9,87),(211,8,28),(212,9,28),(213,3,13),(214,3,84),(215,8,7),(216,1,35),(217,5,37),(218,4,96),(219,2,49),(220,1,39),(221,1,14),(222,6,3),(223,5,85),(224,2,99),(225,8,32),(226,9,6),(227,8,93),(228,6,78),(229,10,95),(230,6,93),(231,4,91),(232,8,88),(233,10,59),(234,6,37),(235,7,16),(236,6,2),(237,10,10),(238,2,18),(239,7,48),(240,7,21),(241,6,24),(242,6,17),(243,10,56),(244,1,31),(245,2,84),(246,10,50),(247,10,33),(248,5,29),(249,5,65),(250,1,87),(251,10,66),(252,3,64),(253,6,62),(254,2,10),(255,2,92),(256,6,19),(257,1,98),(258,2,8),(259,3,49),(260,6,10),(261,5,3),(262,7,43),(263,3,87),(264,5,97),(265,3,42),(266,10,71),(267,10,79),(268,1,22),(269,3,52),(270,7,2),(271,10,67),(272,6,48),(273,2,46),(274,8,25),(275,3,92),(276,2,70),(277,9,66),(278,1,58),(279,10,81),(280,3,32),(281,9,61),(282,5,85),(283,7,43),(284,9,31),(285,7,39),(286,5,93),(287,6,59),(288,3,8),(289,7,80),(290,6,23),(291,1,6),(292,9,64),(293,10,79),(294,10,62),(295,7,64),(296,4,92),(297,10,3),(298,3,11),(299,9,13),(300,8,66),(301,1,1);
/*!40000 ALTER TABLE `enroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `student_firstname` varchar(255) NOT NULL,
  `student_lastname` varchar(255) NOT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'cnutkins0','Clarey','Nutkins'),(2,'rspencer1','Rosalynd','Spencer'),(3,'nsidgwick2','Nickey','Sidgwick'),(4,'gcullerne3','Gilberta','Cullerne'),(5,'iantowski4','Ingaberg','Antowski'),(6,'rsimchenko5','Reinaldos','Simchenko'),(7,'cdizlie6','Cynthia','Dizlie'),(8,'tconnichie7','Tailor','Connichie'),(9,'smacgee8','Shaughn','MacGee'),(10,'vparsons9','Vinny','Parsons'),(11,'kmackisona','Kris','Mackison'),(12,'snaullsb','Shelton','Naulls'),(13,'btoffolinic','Bordie','Toffolini'),(14,'vgriersond','Van','Grierson'),(15,'fpricharde','Frieda','Prichard'),(16,'bdupreyf','Brenna','Duprey'),(17,'bsketcherg','Bil','Sketcher'),(18,'cboorh','Crin','Boor'),(19,'csherrini','Caro','Sherrin'),(20,'fdrennanj','Francine','Drennan'),(21,'hmorriartyk','Hasheem','Morriarty'),(22,'ndorkinl','Nikoletta','Dorkin'),(23,'vrickerm','Vick','Ricker'),(24,'kfonson','Kirbie','Fonso'),(25,'kzoldo','Kerk','Zold'),(26,'glinkinp','Glory','Linkin'),(27,'twaslinq','Thain','Waslin'),(28,'rmcterlaghr','Ruth','McTerlagh'),(29,'mspurriars','Michele','Spurriar'),(30,'cshambrookt','Catharina','Shambrook'),(31,'mnieseu','Mia','Niese'),(32,'gleadbitterv','Grantley','Leadbitter'),(33,'tkarolowskiw','Tanney','Karolowski'),(34,'ocantopherx','Othello','Cantopher'),(35,'bsamwaysy','Bondy','Samways'),(36,'wdabesz','Winonah','Dabes'),(37,'orecher10','Ogden','Recher'),(38,'bwillmott11','Bryn','Willmott'),(39,'fstammer12','Felicio','Stammer'),(40,'cjeavon13','Conrad','Jeavon'),(41,'mwheelan14','Morrie','Wheelan'),(42,'aknok15','Adele','Knok'),(43,'votter16','Violante','Otter'),(44,'chitzmann17','Cass','Hitzmann'),(45,'aswansborough18','Arnold','Swansborough'),(46,'rfarnfield19','Rania','Farnfield'),(47,'gbrokenbrow1a','Gwenny','Brokenbrow'),(48,'lblaza1b','Lorne','Blaza'),(49,'dkohnemann1c','Debera','Kohnemann'),(50,'fjoris1d','Fredericka','Joris'),(51,'chulburd1e','Carolynn','Hulburd'),(52,'jhargie1f','Judd','Hargie'),(53,'mmackellar1g','Mateo','MacKellar'),(54,'rdrayn1h','Rice','Drayn'),(55,'bchristiensen1i','Briant','Christiensen'),(56,'rwindmill1j','Romain','Windmill'),(57,'mspinige1k','Marina','Spinige'),(58,'ccockett1l','Cornall','Cockett'),(59,'rlavall1m','Ramon','Lavall'),(60,'hstirland1n','Hiram','Stirland'),(61,'lmonroe1o','Lyman','Monroe'),(62,'rredsall1p','Riobard','Redsall'),(63,'clinnell1q','Clarine','Linnell'),(64,'emouncey1r','Ezekiel','Mouncey'),(65,'rskillings1s','Rosanne','Skillings'),(66,'ecrosio1t','Ewen','Crosio'),(67,'qtrouncer1u','Quentin','Trouncer'),(68,'bbelfrage1v','Brenna','Belfrage'),(69,'fcolliver1w','Flossie','Colliver'),(70,'hpalliser1x','Harri','Palliser'),(71,'kcostley1y','Kym','Costley'),(72,'tspoure1z','Tomasine','Spoure'),(73,'dbunch20','Dorisa','Bunch'),(74,'myabsley21','Maria','Yabsley'),(75,'aaked22','Arvin','Aked'),(76,'rharriot23','Reinold','Harriot'),(77,'druslin24','Darya','Ruslin'),(78,'tgribbin25','Tallulah','Gribbin'),(79,'cpavlov26','Cathyleen','Pavlov'),(80,'fstockton27','Fenelia','Stockton'),(81,'bgransden28','Becky','Gransden'),(82,'akarpeev29','Amii','Karpeev'),(83,'rrissen2a','Ruthe','Rissen'),(84,'rstendell2b','Rodger','Stendell'),(85,'mtome2c','Michail','Tome'),(86,'bsimonyi2d','Barnie','Simonyi'),(87,'yrolles2e','Ynes','Rolles'),(88,'alemanu2f','Arliene','Lemanu'),(89,'mchristoffersen2g','Marv','Christoffersen'),(90,'uwheatman2h','Udale','Wheatman'),(91,'gcrowcum2i','Gloriane','Crowcum'),(92,'chague2j','Curran','Hague'),(93,'pwoodhams2k','Pepita','Woodhams'),(94,'ntipler2l','Nikola','Tipler'),(95,'oforri2m','Obadiah','Forri'),(96,'sdarcy2n','Saw','d\'Arcy'),(97,'jtwelves2o','Josephine','Twelves'),(98,'cwegman2p','Christophe','Wegman'),(99,'tbranch2q','Toddy','Branch'),(100,'jkobieriecki2r','Jordan','Kobieriecki');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-29 17:10:35
