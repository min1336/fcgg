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
-- Table structure for table `tft_game_rules`
--

DROP TABLE IF EXISTS `tft_game_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tft_game_rules` (
  `rule_category` varchar(50) NOT NULL,
  `rule_key` varchar(50) NOT NULL,
  `rule_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rule_category`,`rule_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tft_game_rules`
--

LOCK TABLES `tft_game_rules` WRITE;
/*!40000 ALTER TABLE `tft_game_rules` DISABLE KEYS */;
INSERT INTO `tft_game_rules` VALUES ('골드 획득','기본 골드 (라운드)','1-2(2), 1-3(2), 1-4(3), 2-1(4), 2-2부터(5)'),('골드 획득','연승/연패','2회(+1), 3회(+1), 4회(+1), 5회(+2), 6회+(+3)'),('골드 획득','이자 (보유 골드)','10G(+1), 20G(+2), 30G(+3), 40G(+4), 50G(+5)'),('라운드 정보','Stage 1','PVE'),('라운드 정보','Stage 2','2-4 (공동 선택), 2-7 (PVE - 골렘)'),('라운드 정보','Stage 3','3-4 (공동 선택), 3-7 (PVE - 늑대)'),('라운드 정보','Stage 4','4-4 (공동 선택), 4-7 (PVE - 야수)'),('라운드 정보','Stage 5','5-4 (공동 선택), 5-7 (PVE - 쓰레쉬)'),('레벨별 리롤 확률','Lv.1','100% / 0% / 0% / 0% / 0%'),('레벨별 리롤 확률','Lv.10','5% / 10% / 20% / 40% / 25%'),('레벨별 리롤 확률','Lv.2','100% / 0% / 0% / 0% / 0%'),('레벨별 리롤 확률','Lv.3','75% / 25% / 0% / 0% / 0%'),('레벨별 리롤 확률','Lv.4','55% / 30% / 15% / 0% / 0%'),('레벨별 리롤 확률','Lv.5','45% / 33% / 20% / 2% / 0%'),('레벨별 리롤 확률','Lv.6','30% / 40% / 25% / 5% / 0%'),('레벨별 리롤 확률','Lv.7','19% / 30% / 40% / 10% / 1%'),('레벨별 리롤 확률','Lv.8','17% / 24% / 32% / 24% / 3%'),('레벨별 리롤 확률','Lv.9','15% / 18% / 25% / 30% / 12%'),('레벨업 필요 경험치','(참고','4골드 = 4 XP 구매 가능)'),('레벨업 필요 경험치','Lv.2 -> 3','2 XP'),('레벨업 필요 경험치','Lv.3 -> 4','6 XP'),('레벨업 필요 경험치','Lv.4 -> 5','10 XP'),('레벨업 필요 경험치','Lv.5 -> 6','20 XP'),('레벨업 필요 경험치','Lv.6 -> 7','36 XP'),('레벨업 필요 경험치','Lv.7 -> 8','48 XP'),('레벨업 필요 경험치','Lv.8 -> 9','76 XP'),('레벨업 필요 경험치','Lv.9 -> 10','84 XP'),('플레이어 피해량','스테이지 기본 피해량','S2(2), S3(5), S4(8), S5(10), S6(12), S7(17)');
/*!40000 ALTER TABLE `tft_game_rules` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-11 15:43:25
