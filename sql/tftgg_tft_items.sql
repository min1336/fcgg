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
-- Table structure for table `tft_items`
--

DROP TABLE IF EXISTS `tft_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tft_items` (
  `item_id` varchar(100) NOT NULL,
  `korean_name` varchar(100) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tft_items`
--

LOCK TABLES `tft_items` WRITE;
/*!40000 ALTER TABLE `tft_items` DISABLE KEYS */;
INSERT INTO `tft_items` VALUES ('TFT_Item_AdaptiveHelm','적응형 투구','2025-10-28 06:47:11'),('TFT_Item_ArchangelsStaff','대천사의 지팡이','2025-10-28 15:42:28'),('TFT_Item_BFSword','B.F. 대검','2025-10-28 06:47:11'),('TFT_Item_Bloodthirster','피바라기','2025-10-28 06:47:11'),('TFT_Item_BlueBuff','푸른 파수꾼','2025-10-28 06:47:11'),('TFT_Item_ChainVest','쇠사슬 조끼','2025-10-28 15:42:28'),('TFT_Item_Crownguard','크라운가드','2025-10-28 06:47:11'),('TFT_Item_DragonsClaw','용의 발톱','2025-10-28 06:47:11'),('TFT_Item_FrozenHeart','얼어붙은 심장','2025-10-28 15:42:28'),('TFT_Item_GargoyleStoneplate','가고일 돌갑옷','2025-10-28 06:47:11'),('TFT_Item_GiantsBelt','거인의 허리띠','2025-10-28 15:42:28'),('TFT_Item_GuardianAngel','수호 천사','2025-10-28 06:47:11'),('TFT_Item_GuinsoosRageblade','구인수의 격노검','2025-10-28 06:47:11'),('TFT_Item_HextechGunblade','마법공학 총검','2025-10-28 06:47:11'),('TFT_Item_InfinityEdge','무한의 대검','2025-10-28 06:47:11'),('TFT_Item_JeweledGauntlet','보석 건틀릿','2025-10-28 06:47:11'),('TFT_Item_LastWhisper','최후의 속삭임','2025-10-28 15:42:28'),('TFT_Item_Leviathan','거인의 유산','2025-10-28 06:47:11'),('TFT_Item_MadredsBloodrazor','스태틱의 단검','2025-10-28 15:42:28'),('TFT_Item_Morellonomicon','모렐로노미콘','2025-10-28 15:42:28'),('TFT_Item_NeedlesslyLargeRod','쓸데없이 큰 지팡이','2025-10-28 15:42:28'),('TFT_Item_NegatronCloak','음전자 망토','2025-10-28 06:47:11'),('TFT_Item_NightHarvester','밤의 수확자','2025-10-28 06:47:11'),('TFT_Item_PowerGauntlet','정의의 손길','2025-10-28 06:47:11'),('TFT_Item_Quicksilver','수은','2025-10-28 15:42:28'),('TFT_Item_RabadonsDeathcap','라바돈의 죽음모자','2025-10-28 06:47:11'),('TFT_Item_RapidFireCannon','고속 연사포','2025-10-28 06:47:11'),('TFT_Item_RecurveBow','곡궁','2025-10-28 15:42:28'),('TFT_Item_RedBuff','붉은 덩굴 정령','2025-10-28 06:47:11'),('TFT_Item_Redemption','구원','2025-10-28 15:42:28'),('TFT_Item_RunaansHurricane','루난의 허리케인','2025-10-30 00:37:36'),('TFT_Item_SparringGloves','연습용 장갑','2025-10-28 15:42:28'),('TFT_Item_SpearOfShojin','쇼진의 창','2025-10-28 06:47:11'),('TFT_Item_SpectralGauntlet','그림자 보석 건틀릿','2025-10-28 15:42:28'),('TFT_Item_StatikkShiv','스태틱의 단검','2025-10-28 15:42:28'),('TFT_Item_SteraksGage','스테락의 도전','2025-10-28 15:42:28'),('TFT_Item_TearOfTheGoddess','여신의 눈물','2025-10-28 15:42:28'),('TFT_Item_ThiefsGloves','도적의 장갑','2025-10-28 06:47:11'),('TFT_Item_TitansResolve','거인의 결의','2025-10-28 06:47:11'),('TFT_Item_UnstableConcoction','불안정한 화공 탱크','2025-10-28 06:47:11'),('TFT_Item_WarmogsArmor','워모그의 갑옷','2025-10-28 06:47:11'),('TFT15_Item_ProtectorEmblemItem','봉쇄자 상징','2025-10-28 06:47:11'),('TFT15_RoboRanger_Core','메크 파일럿 코어','2025-10-28 15:42:28'),('TFT15_RoboRanger_Slicer','메크 슬라이서','2025-10-28 15:42:28'),('TFT15_RoboRanger_Sword','메크 소드','2025-10-28 15:42:28'),('TFT5_Item_SpearOfShojinRadiant','찬란한 쇼진의 창','2025-10-28 06:47:11'),('TFT7_Item_CavalierEmblemItem','기병대 상징','2025-10-30 00:37:36');
/*!40000 ALTER TABLE `tft_items` ENABLE KEYS */;
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
