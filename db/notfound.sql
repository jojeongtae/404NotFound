-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: notfound
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `board_food`
--

DROP TABLE IF EXISTS `board_food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_food` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` tinytext NOT NULL,
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'FOOD',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_food_ibfk_1_idx` (`author`),
  CONSTRAINT `board_food_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_food`
--

LOCK TABLES `board_food` WRITE;
/*!40000 ALTER TABLE `board_food` DISABLE KEYS */;
INSERT INTO `board_food` VALUES (1,'bulgogi','jonmattang',NULL,'aaa',0,0,'FOOD','2025-07-21 12:17:17','2025-07-21 12:17:17','VISIBLE'),(2,'제목','글내용',NULL,'aaa',0,0,'FOOD','2025-07-21 07:49:44','2025-07-21 07:49:44','VISIBLE'),(3,'dfd','dsfd',NULL,'bbb',3,1,'FOOD','2025-07-22 12:34:05','2025-07-22 12:50:03','VISIBLE'),(4,'dds','hello',NULL,'aaa',0,1,'FOOD','2025-07-22 03:39:05','2025-07-22 12:49:56','VISIBLE');
/*!40000 ALTER TABLE `board_food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_food_comments`
--

DROP TABLE IF EXISTS `board_food_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_food_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_food_comments_ibfk_1_idx` (`board_id`),
  KEY `board_food_comments_ibfk_2_idx` (`author`),
  CONSTRAINT `board_food_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_food` (`id`),
  CONSTRAINT `board_food_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_food_comments`
--

LOCK TABLES `board_food_comments` WRITE;
/*!40000 ALTER TABLE `board_food_comments` DISABLE KEYS */;
INSERT INTO `board_food_comments` VALUES (1,1,NULL,'bbb','ㅋㅋㅇㅈ','2025-07-21 03:36:41',NULL),(2,1,NULL,'aaa','댓글내용','2025-07-21 07:52:32',NULL),(3,3,NULL,'aaa','dsf','2025-07-22 12:34:45','VISIBLE'),(4,3,NULL,'bbb','dfgddgg','2025-07-22 12:34:45','VISIBLE'),(5,3,NULL,'aaa','dfgddfgdf','2025-07-22 12:34:45','VISIBLE'),(6,4,NULL,'bbb','agree','2025-07-22 03:41:41',NULL),(7,4,NULL,'aaa','ddagree','2025-07-22 03:52:01','VISIBLE'),(8,4,NULL,'aaa','ddagreedfsd','2025-07-22 03:52:42','VISIBLE');
/*!40000 ALTER TABLE `board_food_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_food_recommend`
--

DROP TABLE IF EXISTS `board_food_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_food_recommend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_board_user` (`board_id`,`username`),
  KEY `board_food_recommend_ibfk_1_idx` (`board_id`),
  KEY `board_food_recommend_ibfk_2_idx` (`username`),
  CONSTRAINT `board_food_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_food` (`id`),
  CONSTRAINT `board_food_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_food_recommend`
--

LOCK TABLES `board_food_recommend` WRITE;
/*!40000 ALTER TABLE `board_food_recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_food_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_free`
--

DROP TABLE IF EXISTS `board_free`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_free` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` tinytext NOT NULL,
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'NORMAL',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `author` (`author`),
  CONSTRAINT `board_free_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_free`
--

LOCK TABLES `board_free` WRITE;
/*!40000 ALTER TABLE `board_free` DISABLE KEYS */;
INSERT INTO `board_free` VALUES (5,'제목','글내용',NULL,'bbb',6,0,'NORMAL','2025-07-21 07:28:07','2025-07-22 16:06:34','VISIBLE'),(8,'제목','글내용',NULL,'aaa',3,0,'NORMAL','2025-07-21 07:48:25','2025-07-22 16:06:40','VISIBLE'),(9,'제목','글내용',NULL,'aaa',1,0,'NORMAL','2025-07-21 07:48:42','2025-07-22 16:08:25','BLOCKED'),(10,'제목','글내용',NULL,'ccc',2,0,'NORMAL','2025-07-22 07:04:46','2025-07-22 16:08:52','BLOCKED'),(11,'제목','글내용22',NULL,'ddd',0,0,'NORMAL','2025-07-22 07:05:02','2025-07-22 07:05:02','VISIBLE'),(12,'제목','글내용22',NULL,'eee',10,0,'NORMAL','2025-07-22 07:05:05','2025-07-22 16:07:01','VISIBLE'),(13,'제목33','글내용22',NULL,'eee',3,0,'NORMAL','2025-07-22 07:05:07','2025-07-22 16:27:11','VISIBLE');
/*!40000 ALTER TABLE `board_free` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_free_comments`
--

DROP TABLE IF EXISTS `board_free_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_free_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_id` (`board_id`),
  KEY `parent_id` (`parent_id`),
  KEY `board_free_comments_ibfk_3` (`author`),
  CONSTRAINT `board_free_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_free` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_free_comments_ibfk_3` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_free_comments`
--

LOCK TABLES `board_free_comments` WRITE;
/*!40000 ALTER TABLE `board_free_comments` DISABLE KEYS */;
INSERT INTO `board_free_comments` VALUES (1,5,NULL,'ccc','댓글내용','2025-07-22 07:24:21','VISIBLE'),(2,5,NULL,'ccc','댓글내용22','2025-07-22 07:24:27','VISIBLE'),(3,8,NULL,'bbb','댓글내용22','2025-07-22 07:24:57','BLOCKED'),(4,9,NULL,'ccc','asdfasdf','2025-07-22 07:25:10','VISIBLE'),(5,9,NULL,'ccc','fsdfsdadf','2025-07-22 07:25:15','VISIBLE'),(6,10,NULL,'eee','fsdfsdadf','2025-07-22 07:25:24','VISIBLE'),(7,10,NULL,'eee','fsdfsdadf','2025-07-22 07:25:25','VISIBLE');
/*!40000 ALTER TABLE `board_free_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_free_recommend`
--

DROP TABLE IF EXISTS `board_free_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_free_recommend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_board_user` (`board_id`,`username`),
  KEY `board_free_recommend_ibfk_1_idx` (`board_id`),
  KEY `board_free_recommend_ibfk_2_idx` (`username`),
  CONSTRAINT `board_free_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_free` (`id`),
  CONSTRAINT `board_free_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_free_recommend`
--

LOCK TABLES `board_free_recommend` WRITE;
/*!40000 ALTER TABLE `board_free_recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_free_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_info`
--

DROP TABLE IF EXISTS `board_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` tinytext NOT NULL,
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'INFO',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_info_ibfk_1_idx` (`author`),
  CONSTRAINT `board_info_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_info`
--

LOCK TABLES `board_info` WRITE;
/*!40000 ALTER TABLE `board_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_info_comments`
--

DROP TABLE IF EXISTS `board_info_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_info_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_info_comments_ibfk_1_idx` (`board_id`),
  KEY `board_info_comments_ibfk_2_idx` (`author`),
  CONSTRAINT `board_info_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_info` (`id`),
  CONSTRAINT `board_info_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_info_comments`
--

LOCK TABLES `board_info_comments` WRITE;
/*!40000 ALTER TABLE `board_info_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_info_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_info_recommend`
--

DROP TABLE IF EXISTS `board_info_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_info_recommend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_board_user` (`board_id`,`username`),
  KEY `board_info_recommend_ibfk_1_idx` (`board_id`),
  KEY `board_info_recommend_ibfk_2_idx` (`username`),
  CONSTRAINT `board_info_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_info` (`id`),
  CONSTRAINT `board_info_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_info_recommend`
--

LOCK TABLES `board_info_recommend` WRITE;
/*!40000 ALTER TABLE `board_info_recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_info_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_notice`
--

DROP TABLE IF EXISTS `board_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` tinytext NOT NULL,
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'NOTICE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_food_ibfk_1_idx` (`author`),
  CONSTRAINT `board_notice_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_notice`
--

LOCK TABLES `board_notice` WRITE;
/*!40000 ALTER TABLE `board_notice` DISABLE KEYS */;
INSERT INTO `board_notice` VALUES (1,'제목','글내용',NULL,'aaa',0,0,'NORMAL','2025-07-21 07:53:02','2025-07-21 07:53:02','VISIBLE');
/*!40000 ALTER TABLE `board_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_notice_comments`
--

DROP TABLE IF EXISTS `board_notice_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_notice_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_free_comments_ibfk_2_idx` (`author`),
  KEY `board_notice_comments_ibfk_1_idx` (`board_id`),
  CONSTRAINT `board_notice_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_notice` (`id`),
  CONSTRAINT `board_notice_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_notice_comments`
--

LOCK TABLES `board_notice_comments` WRITE;
/*!40000 ALTER TABLE `board_notice_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_notice_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_notice_recommend`
--

DROP TABLE IF EXISTS `board_notice_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_notice_recommend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_board_user` (`board_id`,`username`),
  KEY `board_notice_recommend_ibfk_1_idx` (`board_id`),
  KEY `board_notice_recommend_ibfk_2_idx` (`username`),
  CONSTRAINT `board_notice_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_notice` (`id`),
  CONSTRAINT `board_notice_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_notice_recommend`
--

LOCK TABLES `board_notice_recommend` WRITE;
/*!40000 ALTER TABLE `board_notice_recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_notice_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_qna`
--

DROP TABLE IF EXISTS `board_qna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_qna` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` tinytext NOT NULL,
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'QNA',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_qna_ibfk_1_idx` (`author`),
  CONSTRAINT `board_qna_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_qna`
--

LOCK TABLES `board_qna` WRITE;
/*!40000 ALTER TABLE `board_qna` DISABLE KEYS */;
INSERT INTO `board_qna` VALUES (1,'제목','글내용',NULL,'aaa',0,0,'FOOD','2025-07-21 07:53:59','2025-07-21 07:53:59','VISIBLE');
/*!40000 ALTER TABLE `board_qna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_qna_comments`
--

DROP TABLE IF EXISTS `board_qna_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_qna_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_qna_comments_ibfk_1_idx` (`board_id`),
  KEY `board_qna_comments_ibfk_2_idx` (`author`),
  CONSTRAINT `board_qna_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_qna` (`id`),
  CONSTRAINT `board_qna_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_qna_comments`
--

LOCK TABLES `board_qna_comments` WRITE;
/*!40000 ALTER TABLE `board_qna_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_qna_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_qna_recommend`
--

DROP TABLE IF EXISTS `board_qna_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_qna_recommend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_board_user` (`board_id`,`username`),
  KEY `board_qna_recommend_ibfk_1_idx` (`board_id`),
  KEY `board_qna_recommend_ibfk_2_idx` (`username`),
  CONSTRAINT `board_qna_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_qna` (`id`),
  CONSTRAINT `board_qna_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_qna_recommend`
--

LOCK TABLES `board_qna_recommend` WRITE;
/*!40000 ALTER TABLE `board_qna_recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_qna_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_used`
--

DROP TABLE IF EXISTS `board_used`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_used` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` tinytext NOT NULL,
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'USED',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_used_ibfk_1_idx` (`author`),
  CONSTRAINT `board_used_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_used`
--

LOCK TABLES `board_used` WRITE;
/*!40000 ALTER TABLE `board_used` DISABLE KEYS */;
INSERT INTO `board_used` VALUES (1,'제목','글내용',NULL,'aaa',0,0,'USED','2025-07-21 07:54:15','2025-07-21 07:54:15','VISIBLE');
/*!40000 ALTER TABLE `board_used` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_used_comments`
--

DROP TABLE IF EXISTS `board_used_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_used_comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `content` tinytext NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_used_comments_ibfk_1_idx` (`board_id`),
  KEY `board_used_comments_ibfk_2_idx` (`author`),
  CONSTRAINT `board_used_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_used` (`id`),
  CONSTRAINT `board_used_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_used_comments`
--

LOCK TABLES `board_used_comments` WRITE;
/*!40000 ALTER TABLE `board_used_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_used_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_used_recommend`
--

DROP TABLE IF EXISTS `board_used_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_used_recommend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_board_user` (`board_id`,`username`),
  KEY `board_used_recommend_ibfk_1_idx` (`board_id`),
  KEY `board_used_recommend_ibfk_2_idx` (`username`),
  CONSTRAINT `board_used_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_used` (`id`),
  CONSTRAINT `board_used_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_used_recommend`
--

LOCK TABLES `board_used_recommend` WRITE;
/*!40000 ALTER TABLE `board_used_recommend` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_used_recommend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `author` varchar(30) DEFAULT NULL,
  `receiver` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `message_ibfk_1_idx` (`author`),
  KEY `message_ibfk_2_idx` (`receiver`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`),
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`receiver`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (2,'asd','sddsfd','aaa','bbb','2025-07-23 13:06:15'),(3,'dsf','sdfsww','ccc','ddd','2025-07-23 13:06:15');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quiz`
--

DROP TABLE IF EXISTS `quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `question` tinytext NOT NULL,
  `answer` varchar(255) NOT NULL,
  `author` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `level` int DEFAULT '1',
  `category` varchar(30) DEFAULT 'QUIZ',
  `type` enum('MULTI','SUBJECTIVE','OX') DEFAULT 'MULTI',
  `views` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `author` (`author`),
  CONSTRAINT `quiz_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`),
  CONSTRAINT `quiz_chk_1` CHECK ((`level` in (1,2,3)))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz`
--

LOCK TABLES `quiz` WRITE;
/*!40000 ALTER TABLE `quiz` DISABLE KEYS */;
INSERT INTO `quiz` VALUES (1,'test','who am i','seonah','aaa','2025-07-21 09:49:30',1,NULL,'SUBJECTIVE',NULL),(2,'test2','hi','ok','aaa','2025-07-21 00:55:22',2,'QUIZ','SUBJECTIVE',0),(3,'제목','문제','답','aaa','2025-07-21 07:57:41',1,'QUIZ','MULTI',0);
/*!40000 ALTER TABLE `quiz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quiz_results`
--

DROP TABLE IF EXISTS `quiz_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz_results` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quiz_id` int NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `user_answer` varchar(255) NOT NULL,
  `result` tinyint NOT NULL,
  `solved_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `quiz_id` (`quiz_id`),
  KEY `username` (`username`),
  CONSTRAINT `quiz_results_ibfk_1` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`) ON DELETE CASCADE,
  CONSTRAINT `quiz_results_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz_results`
--

LOCK TABLES `quiz_results` WRITE;
/*!40000 ALTER TABLE `quiz_results` DISABLE KEYS */;
INSERT INTO `quiz_results` VALUES (1,1,'bbb','ok',0,'2025-07-21 15:07:36');
/*!40000 ALTER TABLE `quiz_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reason` varchar(255) NOT NULL COMMENT '신고사유 (간단유형)',
  `description` tinytext,
  `reporter` varchar(30) NOT NULL COMMENT '신고자',
  `reported` varchar(30) NOT NULL COMMENT '피신고자',
  `target_table` varchar(30) DEFAULT NULL COMMENT '신고대상 위치 (게시판 또는 댓글 등의 table명)',
  `target_id` int NOT NULL COMMENT '신고대상 ID (board_id, comment_id)',
  `status` enum('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '신고시간',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '처리시간',
  PRIMARY KEY (`id`),
  KEY `reporter` (`reporter`),
  KEY `reported` (`reported`),
  CONSTRAINT `report_ibfk_1` FOREIGN KEY (`reporter`) REFERENCES `user_auth` (`username`),
  CONSTRAINT `report_ibfk_2` FOREIGN KEY (`reported`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (1,'비방','비방을 너무무 많이이 해요!!','ccc','ddd','board_free',2,'PENDING','2025-07-21 17:39:04',NULL),(2,'비방','비방을 너무무 많이이 해요!!','ccc','eee','board_free',2,'PENDING','2025-07-21 17:39:08',NULL),(3,'비방','비방을 너무무 많이이 해요!!','eee','ddd','board_free',2,'REJECTED','2025-07-21 17:39:15','2025-07-22 10:19:31'),(4,'비방','비방을 너무무 많이이 해요!!','eee','bbb','board_free',2,'ACCEPTED','2025-07-21 17:39:19','2025-07-22 10:19:14'),(5,'비방','비방을 너무무 많이이 해요!!','eee','aaa','board_free',2,'ACCEPTED','2025-07-21 17:39:23','2025-07-22 10:18:56');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey`
--

DROP TABLE IF EXISTS `survey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `survey` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `question` tinytext NOT NULL,
  `column1` varchar(255) NOT NULL,
  `column2` varchar(255) NOT NULL,
  `column3` varchar(255) DEFAULT NULL,
  `column4` varchar(255) DEFAULT NULL,
  `column5` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `category` varchar(30) DEFAULT 'SURVEY',
  `views` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `board_survey_ibfk_1_idx` (`author`),
  CONSTRAINT `board_survey_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey`
--

LOCK TABLES `survey` WRITE;
/*!40000 ALTER TABLE `survey` DISABLE KEYS */;
INSERT INTO `survey` VALUES (1,'hey','num','1','2','3','4',NULL,'aaa','2025-07-18 18:02:19','SURVEY',0);
/*!40000 ALTER TABLE `survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey_answers`
--

DROP TABLE IF EXISTS `survey_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `survey_answers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) DEFAULT NULL,
  `answers` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `parent_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `board_survey_answers_ibfk_1_idx` (`username`),
  KEY `board_survey_answers_ibfk_2_idx` (`parent_id`),
  CONSTRAINT `board_survey_answers_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`),
  CONSTRAINT `board_survey_answers_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `survey` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey_answers`
--

LOCK TABLES `survey_answers` WRITE;
/*!40000 ALTER TABLE `survey_answers` DISABLE KEYS */;
INSERT INTO `survey_answers` VALUES (1,'aaa','1','2025-07-18 09:06:21',1),(2,'bbb','2','2025-07-18 09:06:56',1);
/*!40000 ALTER TABLE `survey_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_auth`
--

DROP TABLE IF EXISTS `user_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_auth` (
  `username` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) DEFAULT 'ROLE_USER',
  `joindate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_auth`
--

LOCK TABLES `user_auth` WRITE;
/*!40000 ALTER TABLE `user_auth` DISABLE KEYS */;
INSERT INTO `user_auth` VALUES ('aaa','$2a$10$phHE8n8HJDgsWWXV2k2Vl.WvRbDsSO3x8xT5HcJ9AAAUQhxjKrWbS','ROLE_ADMIN','2025-07-17 14:45:27'),('bbb','$2a$10$hmN3p05XOqO85h6uTyQAlOGC4wJgSARxkhy/mJT90efMMP0qECjQm','ROLE_USER','2025-07-17 15:00:26'),('ccc','$2a$10$q5vL8W14KFGmxewXqwyc7.5f9Gho7hGuPtlP.Lq8yRA4722MDS/qC','ROLE_USER','2025-07-21 17:38:48'),('ddd','$2a$10$1Zf46K.B4u95ZWP6j5Mj0eCNMPjIVBfyq/YktuhiiZ6OPvWGs1QAm','ROLE_USER','2025-07-21 17:38:52'),('eee','$2a$10$U.z3bm8B.Q5W0s6AQAATHOeFO0/BWx5c6R9l5k62XlHnu3HL/Qb.m','ROLE_USER','2025-07-21 17:38:54'),('fff','$2a$10$TDxlspJ4UbIYgI/cZD9wnuuYaQA9oRcyNbuI/5yye8wxWUxW9JqQG','ROLE_USER','2025-07-21 17:38:56'),('ggg','$2a$10$ak8THnlxye2exGAwyZl/HO8TfiXspDxy.X/c0BuL/vpiVXZx0SCc6','ROLE_USER','2025-07-22 10:56:29'),('hhh','$2a$10$QpSoToQOn9x45HDp9pm0.Of.fBLyNiCYY3Y6OHJTtvFUQfMEGgaA2','ROLE_USER','2025-07-22 11:07:16');
/*!40000 ALTER TABLE `user_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `point` int DEFAULT '0',
  `warning` int DEFAULT '0',
  `status` enum('ACTIVE','SUSPENDED','BANNED','INACTIVE') DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  KEY `username` (`username`),
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'aaa','janghy','0000000','서울특별시 강남구 어딘가22233',27,1,'ACTIVE'),(2,'bbb','jang','01012341234','서울특별시 강남구 어딘가',82,1,'ACTIVE'),(3,'ccc','asdf','01012341234','전라도도도',7,0,'ACTIVE'),(4,'ddd','asdf','01012341234','전라도도도',3,0,'ACTIVE'),(5,'eee','asdf','01012341234','전라도도도',8,0,'ACTIVE'),(6,'fff','asdf','01012341234','전라도도도',0,0,'ACTIVE'),(7,'ggg','asdf','01012341234','서울',0,0,NULL),(8,'hhh','asdf','01012341234','서울',0,0,'ACTIVE');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting`
--

DROP TABLE IF EXISTS `voting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `question` varchar(255) NOT NULL,
  `author` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `category` varchar(30) DEFAULT 'VOTING',
  `views` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `voting_ibfk_1_idx` (`author`),
  CONSTRAINT `voting_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting`
--

LOCK TABLES `voting` WRITE;
/*!40000 ALTER TABLE `voting` DISABLE KEYS */;
/*!40000 ALTER TABLE `voting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting_answers`
--

DROP TABLE IF EXISTS `voting_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voting_answers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) DEFAULT NULL,
  `answers` enum('o','x') NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `voting_answers_ibfk_1_idx` (`username`),
  KEY `voting_answers_ibfk_2_idx` (`parent_id`),
  CONSTRAINT `voting_answers_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`),
  CONSTRAINT `voting_answers_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `voting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_answers`
--

LOCK TABLES `voting_answers` WRITE;
/*!40000 ALTER TABLE `voting_answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `voting_answers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-23 13:06:41
