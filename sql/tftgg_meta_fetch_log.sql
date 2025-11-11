-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: tftgg
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `meta_fetch_log`
--

DROP TABLE IF EXISTS `meta_fetch_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta_fetch_log` (
  `fetch_id` int NOT NULL AUTO_INCREMENT,
  `fetch_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `tier` varchar(20) DEFAULT 'Challenger',
  `region` varchar(10) DEFAULT 'KR',
  `total_matches_analyzed` int DEFAULT NULL,
  `top_n_players` int DEFAULT '5',
  `matches_per_player` int DEFAULT NULL,
  PRIMARY KEY (`fetch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_fetch_log`
--

LOCK TABLES `meta_fetch_log` WRITE;
/*!40000 ALTER TABLE `meta_fetch_log` DISABLE KEYS */;
INSERT INTO `meta_fetch_log` VALUES (2,'2025-10-28 06:55:58','Challenger','KR',15,5,3),(3,'2025-10-28 07:09:19','Challenger','KR',15,5,3),(4,'2025-10-28 07:09:19','Challenger','KR',15,5,3),(5,'2025-10-28 07:09:19','Challenger','KR',15,5,3),(6,'2025-10-28 07:09:19','Challenger','KR',15,5,3),(7,'2025-10-28 07:09:19','Challenger','KR',15,5,3),(8,'2025-10-28 15:18:20','Challenger','KR',15,5,3),(9,'2025-10-28 15:18:20','Challenger','KR',15,5,3),(10,'2025-10-28 15:18:20','Challenger','KR',15,5,3),(11,'2025-10-28 15:18:20','Challenger','KR',15,5,3),(12,'2025-10-30 00:40:15','Challenger','KR',15,5,3),(13,'2025-10-30 00:40:15','Challenger','KR',15,5,3),(14,'2025-10-30 00:40:15','Challenger','KR',15,5,3),(15,'2025-10-30 00:40:15','Challenger','KR',15,5,3);
/*!40000 ALTER TABLE `meta_fetch_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-11 15:43:24
