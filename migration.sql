-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: easybili
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category_info`
--

DROP TABLE IF EXISTS `category_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_info` (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing category ID',
  `category_code` varchar(30) NOT NULL COMMENT 'Category code',
  `category_name` varchar(30) NOT NULL COMMENT 'Category name',
  `p_category_id` int NOT NULL COMMENT 'Parent category ID',
  `icon` varchar(50) DEFAULT NULL COMMENT 'Icon',
  `background` varchar(50) DEFAULT NULL COMMENT 'Background image',
  `sort` tinyint NOT NULL COMMENT 'Sort order',
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE KEY `idx_key_category_code` (`category_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='Category information';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_info`
--

LOCK TABLES `category_info` WRITE;
/*!40000 ALTER TABLE `category_info` DISABLE KEYS */;
INSERT INTO `category_info` VALUES (40,'java','java coding',0,'cover/202501/9Q9cgfAfCj6rlvvuchyhy0duY9PBlb.jpg','',0),(41,'python ','python coding',0,NULL,NULL,2),(42,'C++','C++ coding',0,NULL,NULL,3);
/*!40000 ALTER TABLE `category_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statistics_info`
--

DROP TABLE IF EXISTS `statistics_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statistics_info` (
  `statistics_date` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Statistics date',
  `data_type` tinyint(1) NOT NULL COMMENT 'Data type',
  `statistics_count` int DEFAULT NULL COMMENT 'Statistics count',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  PRIMARY KEY (`statistics_date`,`user_id`,`data_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Statistics table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistics_info`
--

LOCK TABLES `statistics_info` WRITE;
/*!40000 ALTER TABLE `statistics_info` DISABLE KEYS */;
INSERT INTO `statistics_info` VALUES ('20250112',0,1,'8288876498'),('20250112',1,1,'9270646332'),('20250112',3,6,'9270646332'),('20250113',2,2,'8288876498'),('20250113',0,10,'9270646332'),('20250113',3,3,'9270646332'),('20250114',0,1,'8288876498'),('20250114',0,26,'9270646332'),('20250114',2,4,'9270646332'),('20250114',3,3,'9270646332'),('20250114',5,4,'9270646332');
/*!40000 ALTER TABLE `statistics_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_action`
--

DROP TABLE IF EXISTS `user_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_action` (
  `action_id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `video_user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Uploader User ID',
  `comment_id` int NOT NULL DEFAULT '0',
  `action_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Action type: 0 - No action; 1 - Like; 2 - Dislike; 3 - Coin; 4 - Favorite',
  `action_count` int NOT NULL COMMENT 'Action count',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `action_time` datetime NOT NULL COMMENT 'Action timestamp',
  PRIMARY KEY (`action_id`) USING BTREE,
  UNIQUE KEY `idx_key_video_comment_type_user` (`video_id`,`comment_id`,`action_type`,`user_id`) USING BTREE,
  KEY `idx_video_id` (`video_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_type` (`action_type`) USING BTREE,
  KEY `idx_action_time` (`action_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='User action: Like, dislike, coin, and favorite';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action`
--

LOCK TABLES `user_action` WRITE;
/*!40000 ALTER TABLE `user_action` DISABLE KEYS */;
INSERT INTO `user_action` VALUES (63,'JJrfTWkSZv','9270646332',0,2,1,'9270646332','2025-01-10 11:00:28'),(65,'JJrfTWkSZv','9270646332',0,4,2,'8288876498','2025-01-10 11:31:51'),(66,'j2d41TWFPF','9270646332',0,4,2,'8288876498','2025-01-10 11:32:59'),(79,'JJrfTWkSZv','9270646332',70,0,1,'9270646332','2025-01-11 09:11:45'),(80,'JJrfTWkSZv','9270646332',0,3,1,'8288876498','2025-01-12 08:06:49'),(81,'j2d41TWFPF','9270646332',0,3,1,'8288876498','2025-01-12 08:06:57'),(82,'eRvb7qufSL','8288876498',0,2,1,'9270646332','2025-01-13 01:06:04'),(83,'tV2mg33HFz','9270646332',0,3,1,'8288876498','2025-01-13 06:19:24'),(84,'UPFPJczArw','9270646332',0,2,1,'9270646332','2025-01-14 07:11:41'),(89,'UPFPJczArw','9270646332',0,4,1,'8288876498','2025-01-15 06:59:06'),(96,'UPFPJczArw','9270646332',0,3,1,'8288876498','2025-01-16 06:28:40'),(99,'UPFPJczArw','9270646332',83,0,1,'8288876498','2025-01-16 06:29:53'),(100,'UPFPJczArw','9270646332',0,2,1,'8288876498','2025-01-16 06:32:13'),(101,'tV2mg33HFz','9270646332',0,2,1,'8288876498','2025-01-16 06:32:28'),(102,'UpjdRd8QWF','8288876498',0,4,1,'9270646332','2025-01-16 06:38:28');
/*!40000 ALTER TABLE `user_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_focus`
--

DROP TABLE IF EXISTS `user_focus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_focus` (
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `focus_user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Focused User ID',
  `focus_time` datetime DEFAULT NULL COMMENT 'Focus Timestamp',
  PRIMARY KEY (`user_id`,`focus_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_focus`
--

LOCK TABLES `user_focus` WRITE;
/*!40000 ALTER TABLE `user_focus` DISABLE KEYS */;
INSERT INTO `user_focus` VALUES ('8288876498','9270646332','2025-01-15 10:06:53');
/*!40000 ALTER TABLE `user_focus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sex` tinyint(1) DEFAULT NULL COMMENT '0: Male; 1: Female; 2: Other',
  `birthday` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `school` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `person_introduction` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `join_time` datetime NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'active: 1; banned: 0',
  `notice_info` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'the notice in user profile',
  `total_coin_count` int DEFAULT NULL COMMENT 'the most coins a person hold',
  `current_coin_count` int DEFAULT NULL COMMENT 'the current coins the person hold',
  `theme` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'the theme for a person in his profile',
  `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_username` (`user_name`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES ('0044153483','Dog Catcher','1482221315@qq.com','080f317e1d74000f176744a0d73f0faa',2,NULL,NULL,NULL,'2024-12-31 13:25:20','2025-01-02 00:28:11','0:0:0:0:0:0:0:1',1,NULL,NULL,20,1,NULL),('1','alex','alex.gmail.com','alex',NULL,NULL,NULL,NULL,'2024-12-30 11:49:45','2024-12-30 11:49:54',NULL,1,NULL,NULL,20,1,NULL),('5396653863','yjj','yjj@gmail.com','f36bb566605afb6c0d9d7a04489de0d6',2,NULL,NULL,NULL,'2025-01-07 11:49:18','2025-01-07 11:49:30','0:0:0:0:0:0:0:1',1,NULL,NULL,20,1,NULL),('8288876498','air','air@gmail.com','010347a9e36ec87e3f3747c05a8c379c',2,'1998-01-01','清华大学','詹密','2025-01-01 22:37:56','2025-01-15 09:56:23','0:0:0:0:0:0:0:1',1,'男士勿扰',NULL,6,1,'cover/20250111/XxucUTid6I1K0KEBMHEzqtpwFsTh7w.png'),('9270646332','LongRoot','xcg@gmail.com','8117f565b591642f481dd4f9b2e01cd6',1,'2001-01-04','清华大学','文明6爱好者，Carla选手','2025-01-08 11:50:57','2025-01-16 06:31:41','0:0:0:0:0:0:0:1',1,'Onlyfans: longroot.onlyfans.com',NULL,16,9,'cover/20250111/O7F9Wb2mzim32WFgjWJeLgLTXltIWk.png');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_message`
--

DROP TABLE IF EXISTS `user_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_message` (
  `message_id` int NOT NULL AUTO_INCREMENT COMMENT 'Message ID (auto-increment)',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'entity id',
  `message_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Message content',
  `send_user_id` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'message sender id',
  `read_type` tinyint(1) DEFAULT NULL COMMENT 'Read type 0: not read; 1: read',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `extend_json` text COLLATE utf8mb4_unicode_ci COMMENT 'extensions',
  PRIMARY KEY (`message_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_read_type` (`read_type`) USING BTREE,
  KEY `idx_message_type` (`message_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='User message table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_message`
--

LOCK TABLES `user_message` WRITE;
/*!40000 ALTER TABLE `user_message` DISABLE KEYS */;
INSERT INTO `user_message` VALUES (5,'8288876498','UPFPJczArw','4','9270646332',1,'2025-01-14 23:06:45','{\"messageContent\":\"方老师闹麻了\",\"messageContentReply\":\"这个up讲的不行\"}'),(8,'9270646332','tV2mg33HFz','4','8288876498',1,'2025-01-14 23:12:30','{\"messageContent\":\"Man\"}'),(10,'9270646332','UPFPJczArw','2','8288876498',1,'2025-01-15 06:59:06','{}'),(11,'9270646332','UPFPJczArw','4','8288876498',1,'2025-01-15 06:59:22','{\"messageContent\":\"psy全栈\"}'),(12,'9270646332','j2d41TWFPF','1',NULL,1,'2025-01-15 12:47:56','{\"auditStatus\":3}'),(13,'8288876498','UpjdRd8QWF','1',NULL,1,'2025-01-16 06:27:11','{\"auditStatus\":3}'),(14,'9270646332','UPFPJczArw','3','8288876498',1,'2025-01-16 06:28:25','{}'),(15,'9270646332','tV2mg33HFz','2','8288876498',1,'2025-01-16 06:32:28','{}'),(16,'8288876498','UPFPJczArw','4','9270646332',1,'2025-01-16 06:35:35','{\"messageContent\":\"闹麻了\",\"messageContentReply\":\"psy全栈\"}'),(17,'8288876498','UpjdRd8QWF','2','9270646332',1,'2025-01-16 06:38:28','{}'),(18,'8288876498','LXrGG9v2uR','1',NULL,1,'2025-01-16 06:57:16','{\"auditStatus\":3}'),(19,'8288876498','UpjdRd8QWF','1',NULL,1,'2025-01-16 07:02:01','{\"auditStatus\":3}'),(20,'8288876498','2G5gSmfuY7','1',NULL,0,'2025-01-16 07:29:05','{\"auditStatus\":3}');
/*!40000 ALTER TABLE `user_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_video_series`
--

DROP TABLE IF EXISTS `user_video_series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_video_series` (
  `series_id` int NOT NULL AUTO_INCREMENT COMMENT 'Series ID',
  `series_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Series Name',
  `series_description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Description',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `sort` tinyint NOT NULL COMMENT 'Sort Order',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  PRIMARY KEY (`series_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='User Video Series Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_video_series`
--

LOCK TABLES `user_video_series` WRITE;
/*!40000 ALTER TABLE `user_video_series` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_video_series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_video_series_video`
--

DROP TABLE IF EXISTS `user_video_series_video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_video_series_video` (
  `series_id` int NOT NULL COMMENT 'Series ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `sort` tinyint NOT NULL COMMENT 'Sort Order',
  PRIMARY KEY (`series_id`,`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_video_series_video`
--

LOCK TABLES `user_video_series_video` WRITE;
/*!40000 ALTER TABLE `user_video_series_video` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_video_series_video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_comment`
--

DROP TABLE IF EXISTS `video_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT 'Comment ID',
  `p_comment_id` int DEFAULT NULL COMMENT 'Parent comment ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `video_user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Uploader User ID',
  `content` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Comment content',
  `img_path` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Image path',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `reply_user_id` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'replyer ID',
  `top_type` tinyint DEFAULT '0' COMMENT '0: not top 1: top',
  `post_time` datetime NOT NULL COMMENT 'post time',
  `like_count` int DEFAULT '0' COMMENT 'like count',
  `hate_count` int DEFAULT '0' COMMENT 'hate count',
  PRIMARY KEY (`comment_id`) USING BTREE,
  KEY `idx_post_time` (`post_time`) USING BTREE,
  KEY `idx_top` (`top_type`) USING BTREE,
  KEY `idx_p_id` (`p_comment_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_video_id` (`video_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video comments';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_comment`
--

LOCK TABLES `video_comment` WRITE;
/*!40000 ALTER TABLE `video_comment` DISABLE KEYS */;
INSERT INTO `video_comment` VALUES (62,0,'j2d41TWFPF','9270646332','太厉害了！','','9270646332',NULL,0,'2025-01-10 13:39:20',NULL,NULL),(64,0,'JJrfTWkSZv','9270646332','up好美','','9270646332',NULL,1,'2025-01-10 23:48:37',NULL,NULL),(65,0,'JJrfTWkSZv','9270646332','siu','cover/20250110/slSe7rVpytgahcEQccTt6NAmPZi7in.jpg','9270646332',NULL,0,'2025-01-10 23:55:24',NULL,NULL),(66,0,'j2d41TWFPF','9270646332','hellop','','9270646332',NULL,0,'2025-01-10 23:58:04',NULL,NULL),(68,66,'j2d41TWFPF','9270646332','man!',NULL,'9270646332',NULL,0,'2025-01-11 01:03:12',NULL,NULL),(69,66,'j2d41TWFPF','9270646332','What can I say?',NULL,'9270646332','9270646332',0,'2025-01-11 01:05:03',NULL,NULL),(70,64,'JJrfTWkSZv','9270646332','大家好我叫xcg',NULL,'8288876498',NULL,0,'2025-01-11 03:58:02',NULL,NULL),(71,64,'JJrfTWkSZv','9270646332','air',NULL,'9270646332',NULL,0,'2025-01-11 04:00:48',NULL,NULL),(72,64,'JJrfTWkSZv','9270646332','qs',NULL,'9270646332',NULL,0,'2025-01-11 05:28:45',NULL,NULL),(81,0,'tV2mg33HFz','9270646332','Man','','8288876498',NULL,NULL,'2025-01-14 23:12:30',NULL,NULL),(82,0,'tV2mg33HFz','9270646332','What Can I say','','8288876498',NULL,NULL,'2025-01-14 23:12:44',NULL,NULL),(83,0,'UPFPJczArw','9270646332','psy全栈','','8288876498',NULL,NULL,'2025-01-15 06:59:21',NULL,NULL),(84,83,'UPFPJczArw','9270646332','闹麻了',NULL,'9270646332',NULL,NULL,'2025-01-16 06:35:35',NULL,NULL);
/*!40000 ALTER TABLE `video_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_danmu`
--

DROP TABLE IF EXISTS `video_danmu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_danmu` (
  `danmu_id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `file_id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'File ID',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `post_time` datetime DEFAULT NULL COMMENT 'Post time',
  `text` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Danmaku text',
  `mode` tinyint(1) DEFAULT NULL COMMENT 'diaplay location',
  `time` tinyint(1) DEFAULT NULL COMMENT 'Display time',
  `color` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Text color',
  PRIMARY KEY (`danmu_id`) USING BTREE,
  KEY `idx_file_id` (`file_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video danmaku';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_danmu`
--

LOCK TABLES `video_danmu` WRITE;
/*!40000 ALTER TABLE `video_danmu` DISABLE KEYS */;
INSERT INTO `video_danmu` VALUES (23,'JJrfTWkSZv','37Zx9nghnpnZ5GtOqMVd','9270646332','2025-01-10 08:41:18','up',0,3,'#FFFFFF'),(24,'JJrfTWkSZv','37Zx9nghnpnZ5GtOqMVd','9270646332','2025-01-10 08:47:27','up好美！',0,1,'#FFFFFF'),(25,'JJrfTWkSZv','37Zx9nghnpnZ5GtOqMVd','8288876498','2025-01-12 13:23:27','来了',0,0,'#FFFFFF'),(28,'UPFPJczArw','9dmkNi8NQcu7PqmdEufS','8288876498','2025-01-16 06:28:11','hello',0,2,'#FFFFFF');
/*!40000 ALTER TABLE `video_danmu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_info`
--

DROP TABLE IF EXISTS `video_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_info` (
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `video_cover` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video cover',
  `video_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video name',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `last_update_time` datetime DEFAULT NULL COMMENT 'Last update time',
  `p_category_id` int DEFAULT NULL COMMENT 'Parent category ID',
  `category_id` int DEFAULT NULL COMMENT 'Category ID',
  `post_type` tinyint(1) DEFAULT NULL COMMENT 'Post type: 0 - Original; 1 - Repost',
  `origin_info` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Original source description',
  `tags` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Tags',
  `introduction` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Introduction',
  `interaction` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Interaction settings',
  `duration` int DEFAULT '0' COMMENT 'Duration (seconds)',
  `play_count` int DEFAULT '0' COMMENT 'Play count',
  `like_count` int DEFAULT '0' COMMENT 'Like count',
  `danmu_count` int DEFAULT '0' COMMENT 'Danmu (comments on screen) count',
  `comment_count` int DEFAULT '0' COMMENT 'Comment count',
  `coin_count` int DEFAULT '0' COMMENT 'Coin count',
  `collect_count` int DEFAULT '0' COMMENT 'Collection count',
  `recommend_type` tinyint(1) DEFAULT '0' COMMENT 'Recommendation type: 0 - Not recommended; 1 - Recommended',
  `last_play_time` datetime DEFAULT NULL COMMENT 'Last playback time',
  PRIMARY KEY (`video_id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  KEY `idx_tags` (`tags`) USING BTREE,
  KEY `idx_recommend_type` (`recommend_type`) USING BTREE,
  KEY `idx_last_play_time` (`last_play_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video information table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_info`
--

LOCK TABLES `video_info` WRITE;
/*!40000 ALTER TABLE `video_info` DISABLE KEYS */;
INSERT INTO `video_info` VALUES ('2G5gSmfuY7','cover/20250115/MdJmHCYBoOIZ24fSMY0QVPecvjy9YS.png','飞跃云海','8288876498','2025-01-16 07:28:52',NULL,40,NULL,0,NULL,'Pal world','1111',NULL,32,1,0,NULL,0,0,NULL,0,'2025-01-15 18:29:17'),('eRvb7qufSL','cover/20250111/TKMhUmLBs6tDRW80WMRvXBhtWmRGMa.png','Coco李玟','8288876498','2025-01-12 02:43:39',NULL,41,NULL,0,NULL,'Coco','1234',NULL,20,3,1,NULL,0,0,NULL,0,'2025-01-15 11:54:14'),('j2d41TWFPF','cover/20250109/Y5vjHWxivZqxp94LoFMfhSpZbNhshY.png','精彩操作','9270646332','2025-01-10 11:16:31',NULL,40,NULL,0,NULL,'123','1234','',14,1,0,NULL,0,0,NULL,1,'2025-01-15 00:44:57'),('JJrfTWkSZv','cover/20250108/gyaSfoSI10dn85FrkWmCLe7BLZdtQH.png','请给广末凉子面子','9270646332','2025-01-09 12:55:01',NULL,40,NULL,1,NULL,'Kobe Bryant,Lakers','11',NULL,31,0,1,3,2,2,1,0,NULL),('MyBoPw5YEw','cover/20250108/ZzRZ0iokFy8xqP4osksOU8fmdEsxf1.png','大家好我是蟹长根','9270646332','2025-01-09 10:02:53',NULL,40,NULL,0,NULL,'man','1234',NULL,5,0,0,0,0,0,0,0,NULL),('tV2mg33HFz','cover/20250112/09XtjpfAN5ATh5WP2C0VXM67VTnV3d.png','Man','9270646332','2025-01-13 06:14:22',NULL,40,NULL,0,NULL,'123','111',NULL,7,19,1,NULL,2,0,NULL,0,'2025-01-15 17:32:27'),('UPFPJczArw','cover/20250112/SHFEYkmnyqLjk1uoHEg3nLCFihwL8D.png','文明6的正确打开方式','9270646332','2025-01-13 12:50:42',NULL,42,NULL,0,NULL,'文明6,Alex','第一个游戏视频，希望大家支持',NULL,30,43,2,NULL,1,1,NULL,0,'2025-01-15 17:35:50'),('VglJnHAU1p','cover/20250108/AgM42q6LRub64CURucCLEAEiJkDqmm.png','宫水三叶','9270646332','2025-01-09 12:18:02',NULL,40,NULL,1,NULL,'Kobe Bryant,Air','1234',NULL,31,0,0,0,0,0,0,1,NULL);
/*!40000 ALTER TABLE `video_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_info_file`
--

DROP TABLE IF EXISTS `video_info_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_info_file` (
  `file_id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Unique ID',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `file_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'File name',
  `file_index` int NOT NULL COMMENT 'File sequence number',
  `file_size` bigint DEFAULT NULL COMMENT 'File size',
  `file_path` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'File path',
  `duration` int DEFAULT NULL COMMENT 'Duration (seconds)',
  PRIMARY KEY (`file_id`) USING BTREE,
  KEY `idx_video_id` (`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video file information table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_info_file`
--

LOCK TABLES `video_info_file` WRITE;
/*!40000 ALTER TABLE `video_info_file` DISABLE KEYS */;
INSERT INTO `video_info_file` VALUES ('37Zx9nghnpnZ5GtOqMVd','9270646332','JJrfTWkSZv','gmlz001',1,4830766,'video/20250108/92706463329KZopvZqWDtp6zO',15),('5XSxmYLY4hd77GLU6VEN','8288876498','eRvb7qufSL','coco',1,6689918,'video/20250111/8288876498Z094ieLQBBSMbqj',20),('9dmkNi8NQcu7PqmdEufS','9270646332','UPFPJczArw','文明6的正确打开方式',1,3021874,'video/20250112/92706463322981YjR2Molk6X7',30),('b2u7jutndkntt1Ry246x','9270646332','MyBoPw5YEw','snowDC',1,1353321,'cover/20250108/92706463323ZVDUPeplMBRVHR',5),('dHMzmdZrV41SG4uUw9os','9270646332','JJrfTWkSZv','gmlz2',2,5408612,'video/20250108/9270646332bOftonTgVuYCEZ0',16),('EgQo256PJxsBfSZOHnfG','9270646332','VglJnHAU1p','gmlz001',1,4830766,'cover/20250108/9270646332IphUSKQO2NlvLtl',15),('fgaY7Zx1SCqJVsmXFncw','8288876498','2G5gSmfuY7','飞跃云海',1,40257374,'video/20250115/82888764985bgzWZTKlM9npFl',32),('n2j1rN7kjtSqxmOpy39k','9270646332','VglJnHAU1p','gmlz2',2,5408612,'cover/20250108/9270646332QnuviH2LWcwaWfE',16),('WbeCw7VHceoxSpGwu0xn','9270646332','j2d41TWFPF','精彩操作',1,3607905,'video/20250111/9270646332rlUCjeei8NREUJn',14),('XJySgYhfmePScJ4UMSXs','9270646332','tV2mg33HFz','Man',1,709276,'video/20250112/9270646332wBQeBBB8qrIAOd6',7);
/*!40000 ALTER TABLE `video_info_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_info_file_post`
--

DROP TABLE IF EXISTS `video_info_file_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_info_file_post` (
  `file_id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Unique ID',
  `upload_id` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Upload ID',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `file_index` int NOT NULL COMMENT 'File sequence number',
  `file_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'File name',
  `file_size` bigint DEFAULT NULL COMMENT 'File size',
  `file_path` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'File path',
  `update_type` tinyint DEFAULT NULL COMMENT 'Update type: 0 - No update; 1 - Update available',
  `transfer_result` tinyint DEFAULT NULL COMMENT 'Transfer result: 0 - Transferring; 1 - Success; 2 - Failure',
  `duration` int DEFAULT NULL COMMENT 'Duration (seconds)',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE KEY `idx_key_upload_id` (`upload_id`,`user_id`) USING BTREE,
  KEY `idx_video_id` (`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video file information table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_info_file_post`
--

LOCK TABLES `video_info_file_post` WRITE;
/*!40000 ALTER TABLE `video_info_file_post` DISABLE KEYS */;
INSERT INTO `video_info_file_post` VALUES ('37Zx9nghnpnZ5GtOqMVd','9KZopvZqWDtp6zO','9270646332','JJrfTWkSZv',1,'gmlz001',4830766,'video/20250108/92706463329KZopvZqWDtp6zO',0,1,15),('3LKfX3MmgaEuny4xRgjf','UuDwd8PnNXKS9zA','9270646332','A4SAsl8X6D',1,'Man',709276,'cover/20250108/9270646332UuDwd8PnNXKS9zA',0,1,7),('5XSxmYLY4hd77GLU6VEN','Z094ieLQBBSMbqj','8288876498','eRvb7qufSL',1,'coco',6689918,'video/20250111/8288876498Z094ieLQBBSMbqj',0,1,20),('9dmkNi8NQcu7PqmdEufS','2981YjR2Molk6X7','9270646332','UPFPJczArw',1,'文明6的正确打开方式',3021874,'video/20250112/92706463322981YjR2Molk6X7',0,1,30),('b2u7jutndkntt1Ry246x','3ZVDUPeplMBRVHR','9270646332','MyBoPw5YEw',1,'snowDC',1353321,'cover/20250108/92706463323ZVDUPeplMBRVHR',0,1,5),('dHMzmdZrV41SG4uUw9os','bOftonTgVuYCEZ0','9270646332','JJrfTWkSZv',2,'gmlz2',5408612,'video/20250108/9270646332bOftonTgVuYCEZ0',0,1,16),('EgQo256PJxsBfSZOHnfG','IphUSKQO2NlvLtl','9270646332','VglJnHAU1p',1,'gmlz001',4830766,'cover/20250108/9270646332IphUSKQO2NlvLtl',0,1,15),('fgaY7Zx1SCqJVsmXFncw','5bgzWZTKlM9npFl','8288876498','2G5gSmfuY7',1,'飞跃云海',40257374,'video/20250115/82888764985bgzWZTKlM9npFl',0,1,32),('n2j1rN7kjtSqxmOpy39k','QnuviH2LWcwaWfE','9270646332','VglJnHAU1p',2,'gmlz2',5408612,'cover/20250108/9270646332QnuviH2LWcwaWfE',0,1,16),('o6tsw4hxyCXt3Gnsw0g0','7w4Nw2tll3qMu3b','9270646332','3tmWI1luhc',1,'css modified login',2501675,'video/20250112/92706463327w4Nw2tll3qMu3b',0,1,18),('oXAW1V2sYokywVjDFpqR','9ZrTwsoqAJ2ZXY9','9270646332','sGTVH5cM00',1,'精彩操作',3607905,'cover/20250108/92706463329ZrTwsoqAJ2ZXY9',0,1,14),('pKVpno9JoKZY3s6LtYs9','6kKpgEdWbvJPKhz','9270646332','w9JSzOCNXG',1,'navigate pages',1940666,'video/20250112/92706463326kKpgEdWbvJPKhz',0,1,13),('vv1sdsouvxmKRyVaarHG','2H1QxBdyTIVQz2i','9270646332','XGDhoA9l79',1,'css modified login',NULL,NULL,0,NULL,NULL),('WbeCw7VHceoxSpGwu0xn','rlUCjeei8NREUJn','9270646332','j2d41TWFPF',1,'精彩操作',3607905,'video/20250111/9270646332rlUCjeei8NREUJn',0,1,14),('XJySgYhfmePScJ4UMSXs','wBQeBBB8qrIAOd6','9270646332','tV2mg33HFz',1,'Man',709276,'video/20250112/9270646332wBQeBBB8qrIAOd6',0,1,7);
/*!40000 ALTER TABLE `video_info_file_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_info_post`
--

DROP TABLE IF EXISTS `video_info_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_info_post` (
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT 'Video ID',
  `video_cover` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video cover image',
  `video_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video name',
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `create_time` datetime NOT NULL COMMENT 'Creation time',
  `last_update` datetime NOT NULL COMMENT 'Last update time',
  `p_category_id` int NOT NULL COMMENT 'Parent category ID',
  `category_id` int DEFAULT NULL COMMENT 'Category ID',
  `status` tinyint(1) NOT NULL COMMENT 'Status: 0 = draft; 1 = pending approval; 2 = approved; 3 = approval failed',
  `post_type` tinyint NOT NULL COMMENT 'Post type: 0 = original; 1 = repost',
  `origin_info` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Original information description',
  `tags` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Tags',
  `introduction` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Introduction',
  `interaction` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Interaction count',
  `duration` int DEFAULT NULL COMMENT 'Video duration (seconds)',
  PRIMARY KEY (`video_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  KEY `idx_p_category_id` (`p_category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video information table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_info_post`
--

LOCK TABLES `video_info_post` WRITE;
/*!40000 ALTER TABLE `video_info_post` DISABLE KEYS */;
INSERT INTO `video_info_post` VALUES ('2G5gSmfuY7','cover/20250115/MdJmHCYBoOIZ24fSMY0QVPecvjy9YS.png','飞跃云海','8288876498','2025-01-16 07:28:52','2025-01-16 07:28:52',40,NULL,3,0,NULL,'Pal world','1111',NULL,32),('eRvb7qufSL','cover/20250111/TKMhUmLBs6tDRW80WMRvXBhtWmRGMa.png','Coco李玟','8288876498','2025-01-12 02:43:39','2025-01-12 02:43:39',41,NULL,3,0,NULL,'Coco','1234',NULL,20),('j2d41TWFPF','cover/20250109/Y5vjHWxivZqxp94LoFMfhSpZbNhshY.png','精彩操作','9270646332','2025-01-10 11:16:31','2025-01-12 12:30:51',40,NULL,3,0,NULL,'123','1234','',14),('JJrfTWkSZv','cover/20250108/gyaSfoSI10dn85FrkWmCLe7BLZdtQH.png','请给广末凉子面子','9270646332','2025-01-09 12:55:01','2025-01-09 12:55:01',40,NULL,3,1,NULL,'Kobe Bryant,Lakers','11',NULL,31),('MyBoPw5YEw','cover/20250108/ZzRZ0iokFy8xqP4osksOU8fmdEsxf1.png','大家好我是蟹长根','9270646332','2025-01-09 10:02:53','2025-01-09 10:02:53',40,NULL,3,0,NULL,'man','1234',NULL,5),('tV2mg33HFz','cover/20250112/09XtjpfAN5ATh5WP2C0VXM67VTnV3d.png','Man','9270646332','2025-01-13 06:14:22','2025-01-13 06:14:22',40,NULL,3,0,NULL,'123','111',NULL,7),('UPFPJczArw','cover/20250112/SHFEYkmnyqLjk1uoHEg3nLCFihwL8D.png','文明6的正确打开方式','9270646332','2025-01-13 12:50:42','2025-01-13 12:50:42',42,NULL,3,0,NULL,'文明6,Alex','第一个游戏视频，希望大家支持',NULL,30),('VglJnHAU1p','cover/20250108/AgM42q6LRub64CURucCLEAEiJkDqmm.png','宫水三叶','9270646332','2025-01-09 12:18:02','2025-01-09 12:18:02',40,NULL,3,1,NULL,'Kobe Bryant,Air','1234',NULL,31);
/*!40000 ALTER TABLE `video_info_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_play_history`
--

DROP TABLE IF EXISTS `video_play_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_play_history` (
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'User ID',
  `video_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Video ID',
  `file_index` int NOT NULL COMMENT 'file index',
  `last_update_time` datetime NOT NULL COMMENT 'Last play time',
  KEY `idx_video_id` (`video_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Video playback history table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_play_history`
--

LOCK TABLES `video_play_history` WRITE;
/*!40000 ALTER TABLE `video_play_history` DISABLE KEYS */;
INSERT INTO `video_play_history` VALUES ('9270646332','UPFPJczArw',1,'2025-01-16 06:35:39'),('9270646332','tV2mg33HFz',1,'2025-01-15 13:44:03'),('9270646332','j2d41TWFPF',1,'2025-01-15 13:44:57'),('9270646332','eRvb7qufSL',1,'2025-01-16 00:54:14'),('9270646332','UpjdRd8QWF',1,'2025-01-16 06:38:23'),('8288876498','UpjdRd8QWF',1,'2025-01-16 07:02:09'),('8288876498','LXrGG9v2uR',1,'2025-01-16 06:57:30'),('8288876498','2G5gSmfuY7',1,'2025-01-16 07:29:17');
/*!40000 ALTER TABLE `video_play_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-16 10:14:27
