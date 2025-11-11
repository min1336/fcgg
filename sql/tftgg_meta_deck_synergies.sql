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
-- Table structure for table `meta_deck_synergies`
--

DROP TABLE IF EXISTS `meta_deck_synergies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta_deck_synergies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deck_id` int NOT NULL,
  `synergy_id` varchar(100) NOT NULL,
  `style` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `deck_id` (`deck_id`),
  CONSTRAINT `meta_deck_synergies_ibfk_1` FOREIGN KEY (`deck_id`) REFERENCES `meta_decks` (`deck_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_deck_synergies`
--

LOCK TABLES `meta_deck_synergies` WRITE;
/*!40000 ALTER TABLE `meta_deck_synergies` DISABLE KEYS */;
INSERT INTO `meta_deck_synergies` VALUES (1,1,'TFT15_ElTigre',3),(2,1,'TFT15_SoulFighter',2),(3,1,'TFT15_Bastion',1),(4,1,'TFT15_Edgelord',1),(5,1,'TFT15_Juggernaut',1),(6,1,'TFT15_Luchador',1),(7,1,'TFT15_Spellslinger',1),(8,2,'TFT15_BattleAcademia',4),(9,2,'TFT15_Bastion',1),(10,2,'TFT15_Destroyer',1),(11,2,'TFT15_Prodigy',1),(12,2,'TFT15_OldMentor',1),(13,3,'TFT15_Captain',3),(14,3,'TFT15_Rosemother',3),(15,3,'TFT15_DragonFist',3),(16,3,'TFT15_ElTigre',3),(17,3,'TFT15_Juggernaut',4),(18,3,'TFT15_Luchador',1),(19,3,'TFT15_SoulFighter',1),(20,3,'TFT15_OldMentor',1),(21,4,'TFT15_StarGuardian',4),(22,4,'TFT15_Heavyweight',1),(23,4,'TFT15_Protector',1),(24,4,'TFT15_Sniper',1),(25,4,'TFT15_Empyrean',1),(26,4,'TFT15_OldMentor',1),(27,5,'TFT15_MonsterTrainer',3),(28,5,'TFT15_ElTigre',3),(29,5,'TFT15_Bastion',4),(30,5,'TFT15_Edgelord',1),(31,5,'TFT15_OldMentor',1),(32,6,'TFT15_Prodigy',4),(33,6,'TFT15_BattleAcademia',2),(34,6,'TFT15_Bastion',1),(35,6,'TFT15_Protector',1),(36,6,'TFT15_StarGuardian',1),(37,6,'TFT15_Empyrean',1),(38,7,'TFT15_Bastion',4),(39,7,'TFT15_Edgelord',1),(40,7,'TFT15_StarGuardian',1),(41,7,'TFT15_OldMentor',1),(42,8,'TFT15_BattleAcademia',4),(43,8,'TFT15_Bastion',1),(44,8,'TFT15_Heavyweight',1),(45,8,'TFT15_Prodigy',1),(46,8,'TFT15_OldMentor',1),(47,9,'TFT15_Heavyweight',2),(48,9,'TFT15_StarGuardian',2),(49,9,'TFT15_Protector',1),(50,9,'TFT15_Spellslinger',1),(51,9,'TFT15_SupremeCells',1),(52,9,'TFT15_OldMentor',1),(53,10,'TFT15_MonsterTrainer',3),(54,10,'TFT15_Heavyweight',4),(55,10,'TFT15_SupremeCells',1),(56,10,'TFT15_OldMentor',1),(57,11,'TFT15_MonsterTrainer',3),(58,11,'TFT15_Heavyweight',4),(59,11,'TFT15_SupremeCells',1),(60,11,'TFT15_MonsterTrainer',1),(61,12,'TFT15_Rosemother',3),(62,12,'TFT15_DragonFist',3),(63,12,'TFT15_Juggernaut',2),(64,12,'TFT15_Empyrean',2),(65,12,'TFT15_Edgelord',1),(66,12,'TFT15_Heavyweight',1),(67,12,'TFT15_Sniper',1),(68,12,'TFT15_OldMentor',1),(69,13,'TFT15_Empyrean',4),(70,13,'TFT15_Duelist',1),(71,13,'TFT15_Heavyweight',1),(72,13,'TFT15_Juggernaut',1),(73,13,'TFT15_Sniper',1),(74,13,'TFT15_OldMentor',1),(75,14,'TFT15_DragonFist',3),(76,14,'TFT15_Destroyer',2),(77,14,'TFT15_SupremeCells',2),(78,14,'TFT15_SentaiRanger',1),(79,14,'TFT15_Heavyweight',1),(80,14,'TFT15_Protector',1),(81,14,'TFT15_Strategist',1),(82,14,'TFT15_OldMentor',1),(83,15,'TFT15_DragonFist',3),(84,15,'TFT15_SentaiRanger',4),(85,15,'TFT15_Destroyer',2),(86,15,'TFT15_Spellslinger',1),(87,15,'TFT15_Strategist',1),(88,15,'TFT15_OldMentor',1),(137,16,'TFT15_Heavyweight',4),(138,16,'TFT15_Prodigy',1),(139,16,'TFT15_StarGuardian',1),(140,16,'TFT15_Empyrean',1),(141,16,'TFT15_OldMentor',1),(142,17,'TFT15_MonsterTrainer',3),(143,17,'TFT15_Protector',4),(144,17,'TFT15_Strategist',2),(145,17,'TFT15_OldMentor',1),(146,18,'TFT15_OldMentor',4),(147,18,'TFT15_Strategist',4),(148,18,'TFT15_SentaiRanger',1),(149,18,'TFT15_Destroyer',1),(150,18,'TFT15_Heavyweight',1),(151,18,'TFT15_Juggernaut',1),(152,18,'TFT15_Protector',1),(153,19,'TFT15_MonsterTrainer',3),(154,19,'TFT15_ElTigre',3),(155,19,'TFT15_Bastion',4),(156,19,'TFT15_Edgelord',1),(157,19,'TFT15_OldMentor',1),(158,20,'TFT15_ElTigre',3),(159,20,'TFT15_Luchador',4),(160,20,'TFT15_StarGuardian',2),(161,20,'TFT15_Bastion',1),(162,20,'TFT15_Edgelord',1),(163,20,'TFT15_Heavyweight',1),(164,20,'TFT15_Sniper',1),(165,20,'TFT15_OldMentor',1),(166,21,'TFT15_ElTigre',3),(167,21,'TFT15_Luchador',4),(168,21,'TFT15_Edgelord',2),(169,21,'TFT15_Bastion',1),(170,21,'TFT15_Juggernaut',1),(171,21,'TFT15_SoulFighter',1),(172,22,'TFT15_BattleAcademia',4),(173,22,'TFT15_Bastion',1),(174,22,'TFT15_Heavyweight',1),(175,22,'TFT15_Prodigy',1),(176,22,'TFT15_OldMentor',1),(177,23,'TFT15_Rosemother',3),(178,23,'TFT15_ElTigre',3),(179,23,'TFT15_GemForce',4),(180,23,'TFT15_Bastion',1),(181,23,'TFT15_Duelist',1),(182,23,'TFT15_Juggernaut',1),(183,23,'TFT15_Strategist',1),(184,23,'TFT15_OldMentor',1),(185,24,'TFT15_Captain',3),(186,24,'TFT15_Rosemother',3),(187,24,'TFT15_GemForce',4),(188,24,'TFT15_Prodigy',1),(189,24,'TFT15_Protector',1),(190,24,'TFT15_Strategist',1),(191,24,'TFT15_Empyrean',1),(192,25,'TFT15_Captain',3),(193,25,'TFT15_ElTigre',3),(194,25,'TFT15_TheCrew',4),(195,25,'TFT15_Strategist',4),(196,25,'TFT15_Bastion',1),(197,25,'TFT15_Protector',1),(198,25,'TFT15_OldMentor',1),(199,26,'TFT15_Protector',2),(200,26,'TFT15_Sniper',2),(201,26,'TFT15_StarGuardian',1),(202,26,'TFT15_TheCrew',1),(203,26,'TFT15_Empyrean',1),(204,27,'TFT15_OldMentor',4),(205,27,'TFT15_Destroyer',2),(206,27,'TFT15_BattleAcademia',1),(207,27,'TFT15_Heavyweight',1),(208,27,'TFT15_Protector',1),(209,27,'TFT15_Strategist',1),(210,28,'TFT15_DragonFist',3),(211,28,'TFT15_ElTigre',3),(212,28,'TFT15_Juggernaut',4),(213,28,'TFT15_Duelist',1),(214,28,'TFT15_Heavyweight',1),(215,28,'TFT15_Luchador',1),(216,28,'TFT15_SoulFighter',1),(217,28,'TFT15_Empyrean',1),(218,28,'TFT15_OldMentor',1),(219,29,'TFT15_ElTigre',3),(220,29,'TFT15_Spellslinger',4),(221,29,'TFT15_SentaiRanger',1),(222,29,'TFT15_Bastion',1),(223,29,'TFT15_SoulFighter',1),(224,29,'TFT15_Strategist',1),(225,29,'TFT15_OldMentor',1),(226,30,'TFT15_Captain',3),(227,30,'TFT15_Rosemother',3),(228,30,'TFT15_ElTigre',3),(229,30,'TFT15_GemForce',1),(230,30,'TFT15_Bastion',1),(231,30,'TFT15_Luchador',1),(232,30,'TFT15_Protector',1),(233,30,'TFT15_Sniper',1),(234,30,'TFT15_Strategist',1),(235,30,'TFT15_Empyrean',1),(236,31,'TFT15_Empyrean',4),(237,31,'TFT15_Heavyweight',2),(238,31,'TFT15_Sniper',1),(239,31,'TFT15_OldMentor',1),(240,32,'TFT15_MonsterTrainer',3),(241,32,'TFT15_SupremeCells',4),(242,32,'TFT15_Duelist',1),(243,32,'TFT15_Heavyweight',1),(244,32,'TFT15_Juggernaut',1),(245,32,'TFT15_Protector',1),(246,32,'TFT15_OldMentor',1),(247,33,'TFT15_DragonFist',3),(248,33,'TFT15_Juggernaut',4),(249,33,'TFT15_Luchador',1),(250,33,'TFT15_Sniper',1),(251,33,'TFT15_SoulFighter',1),(252,33,'TFT15_OldMentor',1),(253,34,'TFT15_DragonFist',3),(254,34,'TFT15_ElTigre',3),(255,34,'TFT15_Juggernaut',4),(256,34,'TFT15_Luchador',4),(257,34,'TFT15_SoulFighter',1),(258,34,'TFT15_OldMentor',1),(259,35,'TFT15_Protector',4),(260,35,'TFT15_SupremeCells',2),(261,35,'TFT15_Destroyer',1),(262,35,'TFT15_Strategist',1),(263,35,'TFT15_OldMentor',1),(264,36,'TFT15_Empyrean',4),(265,36,'TFT15_Heavyweight',1),(266,36,'TFT15_Prodigy',1),(267,36,'TFT15_Sniper',1),(268,36,'TFT15_OldMentor',1),(269,37,'TFT15_StarGuardian',4),(270,37,'TFT15_Protector',1);
/*!40000 ALTER TABLE `meta_deck_synergies` ENABLE KEYS */;
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
