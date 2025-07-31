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
  `category` varchar(30) DEFAULT 'food',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_food_ibfk_1_idx` (`author`),
  CONSTRAINT `board_food_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_food`
--

LOCK TABLES `board_food` WRITE;
/*!40000 ALTER TABLE `board_food` DISABLE KEYS */;
INSERT INTO `board_food` VALUES (8,'오늘 먹을 점심 메뉴','예상임 근데','uploads/30000a32-5b17-48bd-9c52-47962c461e1d_kfc.jpg','bbb',0,1,'food','2025-07-26 02:03:20','2025-07-26 02:03:20','VISIBLE'),(9,'오늘 점심은 불고기!','진짜 맛있게 먹었어요. 다음엔 사진도 찍어봐야지.',NULL,'aaa',0,0,'food','2025-07-27 09:30:00','2025-07-27 09:30:00','VISIBLE'),(10,'혼밥하기 좋은 식당 추천','서울 홍대 근처에 괜찮은 식당 있을까요?',NULL,'bbb',0,0,'food','2025-07-27 10:45:00','2025-07-27 10:45:00','VISIBLE'),(11,'편의점 신상 먹어봤음','세븐 신라면볶음밥 맛있어요ㅋㅋ',NULL,'ccc',0,0,'food','2025-07-27 12:00:00','2025-07-27 12:00:00','VISIBLE'),(12,'야식 뭐 먹지?','치킨 or 떡볶이 고민 중입니다.',NULL,'ddd',0,0,'food','2025-07-27 13:15:00','2025-07-27 13:15:00','VISIBLE'),(13,'요즘 빠진 과자','포카칩 갈릭버터맛 미쳤음.',NULL,'eee',0,0,'food','2025-07-27 14:30:00','2025-07-27 14:30:00','VISIBLE'),(14,'카페 추천해주세요','인스타 감성 카페 찾는 중!',NULL,'fff',0,0,'food','2025-07-28 09:00:00','2025-07-28 09:00:00','VISIBLE'),(15,'마라탕 처음 먹어봤어요','의외로 괜찮네요 ㅋㅋ',NULL,'ggg',0,0,'food','2025-07-28 10:30:00','2025-07-28 10:30:00','VISIBLE'),(16,'도시락 싸봤어요','비주얼은 별로인데 맛있음!',NULL,'hhh',0,0,'food','2025-07-28 11:45:00','2025-07-28 11:45:00','VISIBLE'),(17,'불닭볶음면 신제품','맵찔이인데 먹을만했어요.',NULL,'iii',0,0,'food','2025-07-28 13:00:00','2025-07-28 13:00:00','VISIBLE'),(18,'브런치 카페 다녀왔어요','팬케이크 퀄리티가 미쳤음. 사진은 귀찮아서 안 찍음.',NULL,'jjj',0,0,'food','2025-07-28 14:30:00','2025-07-28 14:30:00','VISIBLE');
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
  CONSTRAINT `board_food_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_food` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_food_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_food_comments`
--

LOCK TABLES `board_food_comments` WRITE;
/*!40000 ALTER TABLE `board_food_comments` DISABLE KEYS */;
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
  CONSTRAINT `board_food_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_food` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_food_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
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
  `category` varchar(30) DEFAULT 'free',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `author` (`author`),
  CONSTRAINT `board_free_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_free`
--

LOCK TABLES `board_free` WRITE;
/*!40000 ALTER TABLE `board_free` DISABLE KEYS */;
INSERT INTO `board_free` VALUES (18,'오늘 하루 어땠나요?','그냥 심심해서 써봅니다. 다들 뭐하고 지내세요?',NULL,'aaa',0,0,'free','2025-07-25 15:45:14','2025-07-25 15:45:14','VISIBLE'),(19,'자유롭게 글 남겨요','이 게시판엔 아무 얘기나 남겨도 되죠?',NULL,'bbb',0,0,'free','2025-07-25 16:25:14','2025-07-25 16:25:14','VISIBLE'),(20,'날씨 너무 덥다','에어컨 없으면 못 살겠어요. 다들 어떻게 지냄?',NULL,'ccc',0,0,'free','2025-07-25 18:12:14','2025-07-25 18:12:14','VISIBLE'),(21,'새로 가입했어요!','앞으로 잘 부탁드립니다~',NULL,'ddd',0,0,'free','2025-07-25 18:45:14','2025-07-25 18:45:14','VISIBLE'),(22,'주말에 뭐하세요?','혼자 영화 보러 갈까 고민 중입니다.',NULL,'eee',0,0,'free','2025-07-25 20:21:14','2025-07-25 20:21:14','VISIBLE'),(23,'요즘 너무 추운듯','보이는 에어컨 다 끄고 다니고 있음 ㅋㅋ 다 죽어봐라',NULL,'fff',0,0,'free','2025-07-26 10:45:14','2025-07-26 10:45:14','VISIBLE'),(24,'오늘의 명언','인생은 속도가 아니라 방향이다.',NULL,'ggg',0,0,'free','2025-07-26 15:57:14','2025-07-26 15:57:14','VISIBLE'),(25,'하루종일 누워있기 성공','생산성 0의 하루였음',NULL,'hhh',0,0,'free','2025-07-27 16:40:14','2025-07-27 16:40:14','VISIBLE'),(26,'자기 전 고민들','요즘 진로 고민이 많네요.',NULL,'iii',0,0,'free','2025-07-29 09:23:14','2025-07-29 09:23:14','VISIBLE'),(27,'출석체크!','출석합니다~ 좋은 하루 되세요.',NULL,'jjj',0,0,'free','2025-07-29 13:49:14','2025-07-29 13:49:14','VISIBLE');
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
  CONSTRAINT `board_free_comments_ibfk_3` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_free_comments`
--

LOCK TABLES `board_free_comments` WRITE;
/*!40000 ALTER TABLE `board_free_comments` DISABLE KEYS */;
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
  CONSTRAINT `board_free_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_free` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_free_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `category` varchar(30) DEFAULT 'info',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_info_ibfk_1_idx` (`author`),
  CONSTRAINT `board_info_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_info`
--

LOCK TABLES `board_info` WRITE;
/*!40000 ALTER TABLE `board_info` DISABLE KEYS */;
INSERT INTO `board_info` VALUES (3,'청년 정책 모음','2025년 기준 지원금 관련 정리해봤습니다.',NULL,'aaa',0,0,'info','2025-07-28 09:30:00','2025-07-28 09:30:00','VISIBLE'),(4,'자격증 공부법 공유','한 달만에 합격한 썰 풉니다.',NULL,'bbb',0,0,'info','2025-07-28 10:45:00','2025-07-28 10:45:00','VISIBLE'),(5,'갤럭시 vs 아이폰','요즘 어떤 게 더 나은가요?',NULL,'ccc',0,0,'info','2025-07-28 12:00:00','2025-07-28 12:00:00','VISIBLE'),(6,'면접 팁 모음','HR 담당자가 알려준 포인트 정리',NULL,'ddd',0,0,'info','2025-07-28 13:15:00','2025-07-28 13:15:00','VISIBLE'),(7,'노트북 추천해줘요','코딩용으로 100만 원 이하 괜찮을까요?',NULL,'eee',0,0,'info','2025-07-28 14:30:00','2025-07-28 14:30:00','VISIBLE'),(8,'스터디 모집 중입니다','온라인 Java 스터디 구해요.',NULL,'fff',0,0,'info','2025-07-29 09:00:00','2025-07-29 09:00:00','VISIBLE'),(9,'MBTI별 공부법','INTP는 계획부터 안 짬',NULL,'ggg',0,0,'info','2025-07-29 10:30:00','2025-07-29 10:30:00','VISIBLE'),(10,'공부 시간 집중법','타이머 돌려놓는 거 진짜 도움됨',NULL,'hhh',0,0,'info','2025-07-29 11:45:00','2025-07-29 11:45:00','VISIBLE'),(11,'유용한 사이트 공유','PDF 병합, PPT 템플릿, 무료 이미지 등 모음',NULL,'iii',0,0,'info','2025-07-29 13:00:00','2025-07-29 13:00:00','VISIBLE'),(12,'도서 추천 받아요','자기계발서나 에세이 계열로요.',NULL,'jjj',0,0,'info','2025-07-29 14:30:00','2025-07-29 14:30:00','VISIBLE');
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
  CONSTRAINT `board_info_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_info` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_info_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  CONSTRAINT `board_info_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_info` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_info_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `category` varchar(30) DEFAULT 'notice',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_food_ibfk_1_idx` (`author`),
  CONSTRAINT `board_notice_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_notice`
--

LOCK TABLES `board_notice` WRITE;
/*!40000 ALTER TABLE `board_notice` DISABLE KEYS */;
INSERT INTO `board_notice` VALUES (4,'404NotFound 첫 공지','방문해주신 여러분들 잘 부탁드립니다. ',NULL,'aaa',1,2,'notice','2025-07-25 02:00:59','2025-07-25 02:00:59','VISIBLE');
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
  CONSTRAINT `board_notice_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_notice` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_notice_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_notice_comments`
--

LOCK TABLES `board_notice_comments` WRITE;
/*!40000 ALTER TABLE `board_notice_comments` DISABLE KEYS */;
INSERT INTO `board_notice_comments` VALUES (1,4,NULL,'bbb','넹','2025-07-31 02:01:35','VISIBLE');
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
  CONSTRAINT `board_notice_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_notice` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_notice_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_notice_recommend`
--

LOCK TABLES `board_notice_recommend` WRITE;
/*!40000 ALTER TABLE `board_notice_recommend` DISABLE KEYS */;
INSERT INTO `board_notice_recommend` VALUES (1,4,'bbb',_binary '','2025-07-31 02:01:34','2025-07-31 02:01:34');
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
  `category` varchar(30) DEFAULT 'qna',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_qna_ibfk_1_idx` (`author`),
  CONSTRAINT `board_qna_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_qna`
--

LOCK TABLES `board_qna` WRITE;
/*!40000 ALTER TABLE `board_qna` DISABLE KEYS */;
INSERT INTO `board_qna` VALUES (4,'스프링 시큐리티 질문','JWT 토큰 만료되면 자동 로그아웃 어떻게 하나요?',NULL,'aaa',0,0,'qna','2025-07-29 09:30:00','2025-07-29 09:30:00','VISIBLE'),(5,'이직 준비 어떻게?','비전공자인데 포트폴리오 어떻게 준비하셨나요?',NULL,'bbb',0,0,'qna','2025-07-29 10:45:00','2025-07-29 10:45:00','VISIBLE'),(6,'면접 질문 도움 요청','팀 프로젝트에서 맡은 역할을 어떻게 어필해야 할까요?',NULL,'ccc',0,0,'qna','2025-07-29 12:00:00','2025-07-29 12:00:00','VISIBLE'),(7,'DB 설계 관련 질문입니다','다대다 관계일 때 조인 테이블 구조 어떻게 잡나요?',NULL,'ddd',0,0,'qna','2025-07-29 13:15:00','2025-07-29 13:15:00','VISIBLE'),(8,'운영체제 공부 어떻게 하세요?','개념이 너무 헷갈려요ㅠㅠ',NULL,'eee',0,0,'qna','2025-07-29 14:30:00','2025-07-29 14:30:00','VISIBLE'),(9,'리눅스 명령어 정리 어디서 보시나요?','좋은 문서 있으면 공유 부탁드려요',NULL,'fff',0,0,'qna','2025-07-30 09:00:00','2025-07-30 09:00:00','VISIBLE'),(10,'웹소켓 질문','React랑 연결해서 실시간 처리하고 싶은데 어떻게?',NULL,'ggg',0,0,'qna','2025-07-30 10:30:00','2025-07-30 10:30:00','VISIBLE'),(11,'Spring Batch 사용해본 분?','대용량 처리할 때 성능 괜찮은가요?',NULL,'hhh',0,0,'qna','2025-07-30 11:45:00','2025-07-30 11:45:00','VISIBLE'),(12,'코딩 테스트 어디서 준비해요?','백준/프로그래머스 말고 괜찮은 곳 있나요?',NULL,'iii',0,0,'qna','2025-07-30 13:00:00','2025-07-30 13:00:00','VISIBLE'),(13,'API 응답속도 개선 팁','속도가 너무 느려서 고민입니다.',NULL,'jjj',0,0,'qna','2025-07-30 14:30:00','2025-07-30 14:30:00','VISIBLE');
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
  CONSTRAINT `board_qna_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_qna` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_qna_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  CONSTRAINT `board_qna_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_qna` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_qna_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
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
  `price` int NOT NULL,
  `author` varchar(30) NOT NULL,
  `recommend` int DEFAULT '0',
  `views` int DEFAULT '0',
  `category` varchar(30) DEFAULT 'used',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('VISIBLE','DELETED','PRIVATE','BLOCKED') DEFAULT 'VISIBLE',
  PRIMARY KEY (`id`),
  KEY `board_used_ibfk_1_idx` (`author`),
  CONSTRAINT `board_used_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_used`
--

LOCK TABLES `board_used` WRITE;
/*!40000 ALTER TABLE `board_used` DISABLE KEYS */;
INSERT INTO `board_used` VALUES (6,'아이패드 미니 팝니다','거의 새 제품이고 구성품 다 있습니다.',NULL,350000,'aaa',0,0,'used','2025-07-30 09:30:00','2025-07-30 09:30:00','VISIBLE'),(7,'중고 모니터 구매 원해요','27인치 이상 모니터 찾습니다.',NULL,150000,'bbb',0,0,'used','2025-07-30 10:45:00','2025-07-30 10:45:00','VISIBLE'),(8,'책장 나눔해요','직접 가지러 오셔야 해요. 서울 성북구입니다.',NULL,0,'ccc',0,0,'used','2025-07-30 12:00:00','2025-07-30 12:00:00','VISIBLE'),(9,'게이밍 키보드 팝니다','리니어 축 사용, 상태 A급',NULL,50000,'ddd',0,0,'used','2025-07-30 13:15:00','2025-07-30 13:15:00','VISIBLE'),(10,'공기청정기 팔아요','1년 썼습니다. 필터 교체했어요.',NULL,120000,'eee',0,0,'used','2025-07-30 14:30:00','2025-07-30 14:30:00','VISIBLE'),(11,'노트북 스탠드 나눔','가져가실 분 계신가요?',NULL,0,'fff',0,0,'used','2025-07-31 09:00:00','2025-07-31 09:00:00','VISIBLE'),(12,'닌텐도 스위치 OLED 팝니다','케이스 포함이고 흠집 없습니다.',NULL,280000,'ggg',0,0,'used','2025-07-31 10:30:00','2025-07-31 10:30:00','VISIBLE'),(13,'중고 책 대량 판매','한 권에 3000원 정도로 계산해드려요.',NULL,30000,'hhh',0,0,'used','2025-07-31 11:45:00','2025-07-31 11:45:00','VISIBLE'),(14,'의자 팝니다','허먼밀러는 아니고 그냥 편한 의자예요.',NULL,40000,'iii',0,0,'used','2025-07-31 13:00:00','2025-07-31 13:00:00','VISIBLE'),(15,'전자레인지 판매','고장 전혀 없고 깨끗해요.',NULL,60000,'jjj',0,0,'used','2025-07-31 14:30:00','2025-07-31 14:30:00','VISIBLE');
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
  CONSTRAINT `board_used_comments_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_used` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_used_comments_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  CONSTRAINT `board_used_recommend_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board_used` (`id`) ON DELETE CASCADE,
  CONSTRAINT `board_used_recommend_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (5,'안녕하세요!','혹시 중고책 아직 판매 중이신가요?','aaa','bbb','2025-07-31 15:50:05'),(6,'답장 드립니다','네 아직 있어요! 연락 주세요.','bbb','aaa','2025-07-31 15:50:05'),(7,'궁금한 점이 있어요','게시판에 쓴 글 잘 봤습니다. 질문이 있어요.','ccc','ddd','2025-07-31 15:50:05'),(8,'중고거래 관련 문의','공기청정기 직거래 가능할까요?','eee','fff','2025-07-31 15:50:05'),(9,'감사합니다','덕분에 좋은 정보 얻었어요!','ggg','hhh','2025-07-31 15:50:05'),(10,'글 잘 읽었습니다','추천 누르고 갑니다~','iii','jjj','2025-07-31 15:50:05'),(11,'거래 희망합니다','닌텐도 관심 있는데 연락드려요.','jjj','ggg','2025-07-31 15:50:05'),(12,'책 관련 문의','어떤 책들이 포함돼 있나요?','hhh','ccc','2025-07-31 15:50:05'),(13,'의자 구매하고 싶어요','편한 의자면 좋겠네요! 쪽지 주세요.','bbb','iii','2025-07-31 15:50:05'),(14,'도움 주셔서 감사합니다','질문에 친절히 답변해주셔서 감사해요.','ddd','ccc','2025-07-31 15:50:05');
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
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `level` int DEFAULT '1',
  `category` varchar(30) DEFAULT 'QUIZ',
  `type` enum('MULTI','SUBJECTIVE','OX') DEFAULT 'MULTI',
  `views` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `author` (`author`),
  CONSTRAINT `quiz_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE,
  CONSTRAINT `quiz_chk_1` CHECK ((`level` in (1,2,3)))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz`
--

LOCK TABLES `quiz` WRITE;
/*!40000 ALTER TABLE `quiz` DISABLE KEYS */;
INSERT INTO `quiz` VALUES (5,'넌센스)','머리가 아플 때 얼마나 먹어야 할까','두통',NULL,'bbb','2025-07-31 02:06:26',NULL,'quiz','MULTI',2),(6,'인물퀴즈','이 사람은 누구일까요?','세종대왕','uploads/725bba8e-b5ee-419b-bb62-61bd3eca4044_세종대왕.jpg','bbb','2025-07-31 02:36:05',NULL,'quiz','SUBJECTIVE',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz_results`
--

LOCK TABLES `quiz_results` WRITE;
/*!40000 ALTER TABLE `quiz_results` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
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
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `category` varchar(30) DEFAULT 'SURVEY',
  `views` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `board_survey_ibfk_1_idx` (`author`),
  CONSTRAINT `board_survey_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey`
--

LOCK TABLES `survey` WRITE;
/*!40000 ALTER TABLE `survey` DISABLE KEYS */;
INSERT INTO `survey` VALUES (2,'흔한 설문조사','퇴근 후 차로 집에 데려다 준다고 나를 기다리고있다. 가장 타고 가고 싶은 차는?','카리나가 타고 온 벤틀리 컨티넨탈GT','장원영이 타고 온 롤스로이스 팬텀','고윤정이 타고 온 람보르기니 우르스','이재용이 타고 온 현대 팰리세이드','',NULL,'bbb','2025-07-31 02:46:51','survey',0);
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
  CONSTRAINT `board_survey_answers_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE,
  CONSTRAINT `board_survey_answers_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `survey` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey_answers`
--

LOCK TABLES `survey_answers` WRITE;
/*!40000 ALTER TABLE `survey_answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `survey_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uploaded_image`
--

DROP TABLE IF EXISTS `uploaded_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uploaded_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `original_name` varchar(255) NOT NULL,
  `saved_name` varchar(255) NOT NULL,
  `file_path` varchar(500) NOT NULL,
  `uploaded_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `file_size` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uploaded_image`
--

LOCK TABLES `uploaded_image` WRITE;
/*!40000 ALTER TABLE `uploaded_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `uploaded_image` ENABLE KEYS */;
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
INSERT INTO `user_auth` VALUES ('aaa','$2a$10$4eKBybT/HccoEbN5XW6kCu/jBTOMfOfhGguROrqlkR77H5Fzx2CMy','ROLE_ADMIN','2025-07-31 10:05:01'),('bbb','$2a$10$hTKH7I3RLnj1DRlq.Nec.uiLeKgfSdcQkhVyK7eMY1F/Wp/5oXNwC','ROLE_USER','2025-07-31 10:06:04'),('ccc','$2a$10$cUnfhbFI0DR9QGggOA3FJOQSfUB6qw6KdNNcVWlzxZieg0x2ticTy','ROLE_USER','2025-07-31 10:07:01'),('ddd','$2a$10$z8yDG2HIcSowmF5f.uczO.ZT4LEBSr3PETBngSSsV.eMOLmuF2/w.','ROLE_USER','2025-07-31 10:08:50'),('eee','$2a$10$6KGEWyIrIy1K5oWSgpTdkuPE2Xi9r/VtkFW/2hWcAPrVQdmjvy3O2','ROLE_USER','2025-07-31 10:09:46'),('fff','$2a$10$2x9fbAGYBZ7b3i2nrqz0MO3XTRpx4pC5..8PTDHwJH5ezKPAKJSgK','ROLE_USER','2025-07-31 10:11:22'),('ggg','$2a$10$zeplzC3AoFBX0i6GlGxdoOGqGIojPrntt.oKHvHORvh/32VOV72iu','ROLE_USER','2025-07-31 10:12:14'),('hhh','$2a$10$fCg6iqFXS6/EQ5wsayH5BOzorMeWLzekDfeFr6m.cYsd6tUXgFifa','ROLE_USER','2025-07-31 10:13:29'),('iii','$2a$10$iTdGHu64GyxQTSS9eFaR7uaVdFG02qyD6C25S7wZuP/Gn0GU9QLKC','ROLE_USER','2025-07-31 10:14:31'),('jjj','$2a$10$8v7MMLAsQjBHQTl9NeAD2uUn3dHzw2NqNLtuvYWgo9NliWxk491pu','ROLE_USER','2025-07-31 10:15:56'),('kkk','$2a$10$hRfQTyI6B0c9guVEPPDrZuavgfEoJIYuiDRnx/nyn6ulGa18aiUW2','ROLE_USER','2025-07-31 10:19:35'),('lll','$2a$10$fIrpqL23U5PmyZKuT1auh.VPetzrvTd2AfMN2nBtOb7sdxpj5hxbq','ROLE_USER','2025-07-31 10:21:05'),('mmm','$2a$10$pzno01CvzkgQmh0.zpadrO8OeJPg6HFgU8VG1G/I3jRGg/QoKte8u','ROLE_USER','2025-07-31 10:36:52'),('nnn','$2a$10$sgoZBE9KJj0JXyRUXWDSfebss2caJLUgy2MIGebvg34U2pt8RPpUW','ROLE_USER','2025-07-31 10:38:03'),('ooo','$2a$10$cZucuj/DRPAz5B7X7g1gU.B7MMKgldcUrsO9Ojisxjacu0g3yKwLG','ROLE_USER','2025-07-31 10:39:20'),('ppp','$2a$10$606n4vnRIIZ9V5CK8J7t4OMVNrsnnNEJPzBLuVx.YRzPK6gydlMUm','ROLE_USER','2025-07-31 10:42:05'),('qqq','$2a$10$988y8axERlrzufpZuiQuWO86qCC9KX5oaA9xSHJFGwkG7KdDCjW5K','ROLE_USER','2025-07-31 10:45:58'),('rrr','$2a$10$5dgkKM8z9oVDUHM87YR9qOeMMVjrIQfAO9RIq1j5KKQNuFNrLEQN6','ROLE_USER','2025-07-31 10:47:34'),('sss','$2a$10$DULA/iCsleQa6Y6zK79jtuV2ae3Aaw/Py7R/WiIKbqm1wRAEvf7YW','ROLE_USER','2025-07-31 10:49:53'),('ttt','$2a$10$86hexl8f6CfpQUJvNeTrCesxDybMk3IVAohcz7xaQs./crzN/Oj6C','ROLE_USER','2025-07-31 10:50:48'),('uuu','$2a$10$yy6j6CYqAejw9IWvU0PPouAEAVf7TBJP.op2n6SG3mfrCSrfXpCYO','ROLE_USER','2025-07-31 10:51:45'),('vvv','$2a$10$2hDLM9dmRgyFTMEfP7/6vOYdYSntLwBGQTJ1gc6k2h9jowIt5i8Ee','ROLE_USER','2025-07-31 10:52:51'),('www','$2a$10$eHl5PJxbjj.UtW64EwOLUOSfmfQ6yy2hPHY93rh1GIlag.0y36C42','ROLE_USER','2025-07-31 10:55:11'),('xxx','$2a$10$.Zpg4j/6lyQ8kvnSwqfRFeURIZ6oKpaBkBqSBzEdm6VE13zE9nJAS','ROLE_USER','2025-07-31 10:56:21'),('yyy','$2a$10$igeMMWGyYnS25/HHQ2UcN.GhFZmUdfTgIestwMTXBkYh5NPEYVfMq','ROLE_USER','2025-07-31 10:57:13'),('zzz','$2a$10$A6MQTK7XlB5kwudHLoF.4OUsnEVzANs37uaro38FGri8QhukpITl6','ROLE_USER','2025-07-31 10:59:14');
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
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (9,'aaa','admin','01012345678','서울 어딘가',3,0,'ACTIVE'),(10,'bbb','조정태','01012345679','서울시 성동구 송정동 66-146 3층',11,0,'ACTIVE'),(11,'ccc','뼈마디아픔','01012356789','서울 광진구 중곡동 어딘가',0,0,'ACTIVE'),(12,'ddd','김준홍','01034567892','경기도 안산 어딘가',0,0,'ACTIVE'),(13,'eee','토끼띠페리','01098765432','경기도 안산 어딘가',0,0,'ACTIVE'),(14,'fff','맴맴왕자','01036655813','동탄 어딘가',0,0,'ACTIVE'),(15,'ggg','커뮤망령','01035652885','서울특별시 잠실',0,0,'ACTIVE'),(16,'hhh','선동쟁이','01038849324','서울 어딘가',0,0,'ACTIVE'),(17,'iii','믹스커피','01045621598','대구 중구',0,0,'ACTIVE'),(18,'jjj','개발자','01099875521','서울 강남',0,0,'ACTIVE'),(19,'kkk','로악귀','01033665544','서울 청량리',0,0,'ACTIVE'),(20,'lll','귀신','01052146325','부산광역시 범천동',0,0,'ACTIVE'),(21,'mmm','아리스단','01015411541','경기도',0,0,'ACTIVE'),(22,'nnn','멀록','01069874563','경상남도 진해',0,0,'ACTIVE'),(23,'ooo','슈퍼쏜','01051638456','강원도 화천',0,0,'ACTIVE'),(24,'ppp','익산주먹왕','01099625656','전라북도 익산',0,0,'ACTIVE'),(25,'qqq','맞춤법검사기','01022255552','부산 중구 남포동',0,0,'ACTIVE'),(26,'rrr','청소부','01058796159','대구 동구 둔산동',0,0,'ACTIVE'),(27,'sss','박치기공룡','01088997788','인천 문학동',0,0,'ACTIVE'),(28,'ttt','존윅','01033336666','경기도 평택',0,0,'ACTIVE'),(29,'uuu','무료맨','01077789998','강원도 강릉',0,0,'ACTIVE'),(30,'vvv','과메기남','01045298963','경상북도 포항',0,0,'ACTIVE'),(31,'www','전기공','01033652221','제주도 서귀포시',0,0,'ACTIVE'),(32,'xxx','예비군','01032132110','경기도 남양주',0,0,'ACTIVE'),(33,'yyy','환장의나라','01010502030','경기도 용인',0,0,'ACTIVE'),(34,'zzz','키라','01030250514','서울 마포구',0,0,'ACTIVE');
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
  `imgsrc` varchar(255) DEFAULT NULL,
  `author` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `category` varchar(30) DEFAULT 'VOTING',
  `views` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `voting_ibfk_1_idx` (`author`),
  CONSTRAINT `voting_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting`
--

LOCK TABLES `voting` WRITE;
/*!40000 ALTER TABLE `voting` DISABLE KEYS */;
INSERT INTO `voting` VALUES (1,'상식 OX','달팽이도 이빨이 있다.',NULL,'bbb','2025-07-31 02:47:51','voting',5);
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
  CONSTRAINT `voting_answers_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user_auth` (`username`) ON DELETE CASCADE,
  CONSTRAINT `voting_answers_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `voting` (`id`) ON DELETE CASCADE
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

-- Dump completed on 2025-07-31 17:27:26
