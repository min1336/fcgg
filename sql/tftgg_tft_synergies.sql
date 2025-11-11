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
-- Table structure for table `tft_synergies`
--

DROP TABLE IF EXISTS `tft_synergies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tft_synergies` (
  `synergy_id` varchar(100) NOT NULL,
  `korean_name` varchar(100) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`synergy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tft_synergies`
--

LOCK TABLES `tft_synergies` WRITE;
/*!40000 ALTER TABLE `tft_synergies` DISABLE KEYS */;
INSERT INTO `tft_synergies` VALUES ('TFT15_Bastion','요새','2025-10-28 06:47:11'),('TFT15_BattleAcademia','전투사관학교','2025-10-28 06:47:11'),('TFT15_Captain','해적선장','2025-10-28 06:47:11'),('TFT15_Destroyer','처형자','2025-10-28 06:47:11'),('TFT15_DragonFist','태세의 대가','2025-10-28 06:47:11'),('TFT15_Duelist','결투가','2025-10-28 06:47:11'),('TFT15_Edgelord','이단아','2025-10-28 06:47:11'),('TFT15_ElTigre','레슬링 챔피언','2025-10-28 06:47:11'),('TFT15_Empyrean','악령','2025-10-28 06:47:11'),('TFT15_GemForce','수정 갬빗','2025-10-28 06:47:11'),('TFT15_Heavyweight','헤비급','2025-10-28 06:47:11'),('TFT15_Juggernaut','전쟁기계','2025-10-28 06:47:11'),('TFT15_Luchador','프로레슬러','2025-10-28 06:47:11'),('TFT15_MonsterTrainer','괴물 트레이너','2025-10-28 06:47:11'),('TFT15_OldMentor','멘토','2025-10-28 06:47:11'),('TFT15_Prodigy','신동','2025-10-28 06:47:11'),('TFT15_Protector','봉쇄자','2025-10-28 06:47:11'),('TFT15_Rosemother','장미 어머니','2025-10-28 06:47:11'),('TFT15_SentaiRanger','거대 메크','2025-10-28 06:47:11'),('TFT15_Sniper','저격수','2025-10-28 06:47:11'),('TFT15_SoulFighter','소울 파이터','2025-10-28 06:47:11'),('TFT15_Spellslinger','마법사','2025-10-28 06:47:11'),('TFT15_StarGuardian','별 수호자','2025-10-28 06:47:11'),('TFT15_Strategist','책략가','2025-10-28 06:47:11'),('TFT15_SupremeCells','슈프림 셀','2025-10-28 06:47:11'),('TFT15_TheCrew','크루','2025-10-28 06:47:11'),('TFT7_Astral','별','2025-10-30 00:37:36'),('TFT7_Bard','바드','2025-10-30 00:37:36'),('TFT7_Bruiser','난동꾼','2025-10-30 00:37:36'),('TFT7_Cavalier','기병대','2025-10-30 00:37:36'),('TFT7_Dragon','용','2025-10-30 00:37:36'),('TFT7_Evoker','기원자','2025-10-30 00:37:36'),('TFT7_Guardian','수호자','2025-10-30 00:37:36'),('TFT7_Guild','길드','2025-10-30 00:37:36'),('TFT7_Legend','전설','2025-10-30 00:37:36'),('TFT7_Mage','마법사','2025-10-30 00:37:36'),('TFT7_Mystic','신비술사','2025-10-30 00:37:36'),('TFT7_Ragewing','분노날개','2025-10-30 00:37:36'),('TFT7_Shimmerscale','빛비늘','2025-10-30 00:37:36'),('TFT7_Swiftshot','신속사수','2025-10-30 00:37:36'),('TFT7_Tempest','폭풍','2025-10-30 00:37:36');
/*!40000 ALTER TABLE `tft_synergies` ENABLE KEYS */;
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
