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
-- Table structure for table `summoner`
--

DROP TABLE IF EXISTS `summoner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `summoner` (
  `id` int NOT NULL AUTO_INCREMENT,
  `summoner_name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `summoner_tag` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `puuid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `lovecham` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `lovesyn` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `summoner`
--

LOCK TABLES `summoner` WRITE;
/*!40000 ALTER TABLE `summoner` DISABLE KEYS */;
INSERT INTO `summoner` VALUES (24,'호날묵','kr1','b-5FlDm3Y8JbJ68R9VLef_MCfy9fGlM3l4gm41QfK1bujqPfI11EXjqNpgaS_LfcNpF_WleRCaTHJw','{\"gameName\":\"호날묵\",\"tagLine\":\"KR1\",\"puuid\":\"b-5FlDm3Y8JbJ68R9VLef_MCfy9fGlM3l4gm41QfK1bujqPfI11EXjqNpgaS_LfcNpF_WleRCaTHJw\"}','Kalista','TFT15_Edgelord','2025-11-11 06:34:10'),(25,'졸린진돗개','kr1','WYI1r_9kCDax0mOJ0xqTbHux4plb-tiiAHwqk5yOaB2UOxRx1AYdWnuaZpDyRdDTEBenD83jIenfBA','{\"gameName\":\"졸린 진돗개\",\"tagLine\":\"KR1\",\"puuid\":\"WYI1r_9kCDax0mOJ0xqTbHux4plb-tiiAHwqk5yOaB2UOxRx1AYdWnuaZpDyRdDTEBenD83jIenfBA\"}','Janna','TFT15_Bastion','2025-11-04 05:36:20'),(26,'chrome','4494','hPNJ1JOaU16asC7ATo8BUqITY19_zI3veq-k4GSMQUNbYMN3oM7e0JEjnp8K-qQiRaUcQBV8mI5_tQ','{\"gameName\":\"Chrome\",\"tagLine\":\"4494\",\"puuid\":\"hPNJ1JOaU16asC7ATo8BUqITY19_zI3veq-k4GSMQUNbYMN3oM7e0JEjnp8K-qQiRaUcQBV8mI5_tQ\"}','Garen','TFT15_OldMentor','2025-11-11 06:33:26'),(27,'난몰라','kr1','96PauoJskenCpv6cEOX1vesXTex4EZpVPgr--ZZl_tsiALDesucta_CbO_onJ7Ks11Cjyyx9xk-NQg','{\"gameName\":\"난몰라\",\"tagLine\":\"KR1\",\"puuid\":\"96PauoJskenCpv6cEOX1vesXTex4EZpVPgr--ZZl_tsiALDesucta_CbO_onJ7Ks11Cjyyx9xk-NQg\"}','Braum','TFT15_OldMentor','2025-11-10 03:17:01');
/*!40000 ALTER TABLE `summoner` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-11 15:43:26
