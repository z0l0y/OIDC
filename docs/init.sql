-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: oidc
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `anime`
--

DROP TABLE IF EXISTS `anime`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anime`
(
    `id`       bigint unsigned NOT NULL AUTO_INCREMENT,
    `name`     varchar(64)     NOT NULL DEFAULT '',
    `episodes` int             NOT NULL DEFAULT '1',
    `director` varchar(64)     NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anime`
--

LOCK TABLES `anime` WRITE;
/*!40000 ALTER TABLE `anime`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `anime`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authorization_state`
--

DROP TABLE IF EXISTS `authorization_state`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authorization_state`
(
    `id`    bigint unsigned NOT NULL AUTO_INCREMENT,
    `state` varchar(256)    NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorization_state`
--

LOCK TABLES `authorization_state` WRITE;
/*!40000 ALTER TABLE `authorization_state`
    DISABLE KEYS */;
INSERT INTO `authorization_state`
VALUES (1,
        'ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKamIyUmxJam9pTUdJek0ySmhaVEptWmpKbE5ESXdOamxoWW1RMFl6Umtaamc0WTJFM05UUWlMQ0psZUhBaU9qRTNNVEUxTWpBMU5ERjkuY3hjMVpIZTJhZTVBYjJlb1BTS01QX1hFZDNhTGxXclp4bGNpNVNhc2p0UQ=='),
       (2,
        'ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKamIyUmxJam9pWWpoak5UVTJPRGd6TkRBd05ESm1OR0UxTlRaaE1EVTNZV015T1dJd05qWWlMQ0psZUhBaU9qRTNNVEUxTWpFeU1qWjkuUGtEZTVRN0ZuR1FmR2lxZGpiTV9BWUlXLXlUTHJIMm9MV1RsOGl3b2RZWQ=='),
       (3,
        'ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKamIyUmxJam9pWWpFM00yUmpZamhtTURsbU5HRmtPVGd6WTJaaE16STVaalJoTWpVeFpUY2lMQ0psZUhBaU9qRTNNVEUxTWpjeE1qZDkud05yM2N2WUxJQjYxRFZ6T0N6N2d4alNaelQxN2p1aWVRUGU3TmNnaUY4dw==');
/*!40000 ALTER TABLE `authorization_state`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_info`
--

DROP TABLE IF EXISTS `client_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_info`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT,
    `name`          varchar(64)     NOT NULL DEFAULT '',
    `gmt_create`    datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_id`     varchar(128)    NOT NULL DEFAULT '',
    `client_secret` varchar(64)     NOT NULL DEFAULT '',
    `redirect_url`  varchar(128)    NOT NULL DEFAULT '',
    `is_deleted`    tinyint         NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_info`
--

LOCK TABLES `client_info` WRITE;
/*!40000 ALTER TABLE `client_info`
    DISABLE KEYS */;
INSERT INTO `client_info`
VALUES (1, 'hust', '2024-03-26 23:55:35', '2024-03-26 23:55:35', 'c556723844614ec2a13a270cc8847fc8', '123456', 'xxx',
        0);
/*!40000 ALTER TABLE `client_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_state`
--

DROP TABLE IF EXISTS `client_state`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_state`
(
    `id`    bigint unsigned NOT NULL AUTO_INCREMENT,
    `state` varchar(512)    NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_state`
--

LOCK TABLES `client_state` WRITE;
/*!40000 ALTER TABLE `client_state`
    DISABLE KEYS */;
INSERT INTO `client_state`
VALUES (5,
        'ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKamIyUmxJam9pTUdJek0ySmhaVEptWmpKbE5ESXdOamxoWW1RMFl6Umtaamc0WTJFM05UUWlMQ0psZUhBaU9qRTNNVEUxTWpBMU5ERjkuY3hjMVpIZTJhZTVBYjJlb1BTS01QX1hFZDNhTGxXclp4bGNpNVNhc2p0UQ=='),
       (6,
        'ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKamIyUmxJam9pWWpoak5UVTJPRGd6TkRBd05ESm1OR0UxTlRaaE1EVTNZV015T1dJd05qWWlMQ0psZUhBaU9qRTNNVEUxTWpFeU1qWjkuUGtEZTVRN0ZuR1FmR2lxZGpiTV9BWUlXLXlUTHJIMm9MV1RsOGl3b2RZWQ=='),
       (7,
        'ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKamIyUmxJam9pWWpFM00yUmpZamhtTURsbU5HRmtPVGd6WTJaaE16STVaalJoTWpVeFpUY2lMQ0psZUhBaU9qRTNNVEUxTWpjeE1qZDkud05yM2N2WUxJQjYxRFZ6T0N6N2d4alNaelQxN2p1aWVRUGU3TmNnaUY4dw==');
/*!40000 ALTER TABLE `client_state`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection`
(
    `id`         bigint unsigned NOT NULL AUTO_INCREMENT,
    `username`   varchar(32)     NOT NULL DEFAULT '',
    `anime_name` varchar(64)     NOT NULL DEFAULT '',
    `type`       varchar(4)      NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `collection`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendship`
(
    `id`     bigint unsigned NOT NULL AUTO_INCREMENT,
    `user1`  varchar(32)     NOT NULL DEFAULT '',
    `user2`  varchar(32)     NOT NULL DEFAULT '',
    `status` int             NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendship`
--

LOCK TABLES `friendship` WRITE;
/*!40000 ALTER TABLE `friendship`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `friendship`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `rating_value` int             NOT NULL DEFAULT '10',
    `anime_name`   varchar(64)     NOT NULL DEFAULT '',
    `commentary`   varchar(512)    NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `rating`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_info`
--

DROP TABLE IF EXISTS `resource_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_info`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT,
    `gmt_create`    datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `username`      varchar(64)     NOT NULL DEFAULT '',
    `password`      varchar(64)     NOT NULL DEFAULT '',
    `email`         varchar(32)     NOT NULL DEFAULT '',
    `nickname`      varchar(64)     NOT NULL DEFAULT '',
    `avatar`        varchar(512)    NOT NULL DEFAULT '',
    `bio`           varchar(128)    NOT NULL DEFAULT '',
    `is_deleted`    tinyint         NOT NULL DEFAULT '0',
    `code`          varchar(256)    NOT NULL DEFAULT '',
    `access_token`  varchar(256)    NOT NULL DEFAULT '',
    `refresh_token` varchar(256)    NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_info`
--

LOCK TABLES `resource_info` WRITE;
/*!40000 ALTER TABLE `resource_info`
    DISABLE KEYS */;
INSERT INTO `resource_info`
VALUES (1, '2024-03-26 23:03:37', '2024-03-26 23:03:37', 'zoloy', 'root', 'hust@hust.edu.com', 'zoloy',
        'https://picture-zoloy.oss-cn-wuhan-lr.aliyuncs.com/409f6616590c402ea63689f7a694595e.jpg', 'Hello World!', 0,
        '762f179f230f4fc8b44c4b2a96ff46c4', '9ea7ff3263a545eba479f061ba28b952', 'c0f3d61b916a410486b5014e81311be1'),
       (2, '2024-03-26 23:04:37', '2024-03-26 23:04:37', 'biyan', 'hust', 'hust@by.com', 'by',
        'https://picture-zoloy.oss-cn-wuhan-lr.aliyuncs.com/409f6616590c402ea63689f7a694595e.jpg', 'Hello BY!', 0, '',
        '', '');
/*!40000 ALTER TABLE `resource_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info`
(
    `user_id`      bigint unsigned                                              NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `gmt_create`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号注册时间',
    `gmt_modified` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号更新时间',
    `username`     varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
    `password`     varchar(64)                                                  NOT NULL DEFAULT '' COMMENT '用户密码',
    `email`        varchar(32)                                                  NOT NULL DEFAULT '' COMMENT '用户邮箱',
    `nickname`     varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
    `avatar`       varchar(512)                                                 NOT NULL DEFAULT '' COMMENT '用户头像',
    `bio`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '用户间简介',
    `is_deleted`   tinyint unsigned                                             NOT NULL DEFAULT '0' COMMENT '''逻辑删除标志0有效，1无效''',
    PRIMARY KEY (`user_id`),
    KEY `idx_username` (`username`),
    KEY `pk_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info`
    DISABLE KEYS */;
INSERT INTO `user_info`
VALUES (1, '2024-03-24 13:03:24', '2024-03-24 13:03:24', 'root', 'roothust', 'hust@qq.com', 'root',
        'https://picture-zoloy.oss-cn-wuhan-lr.aliyuncs.com/409f6616590c402ea63689f7a694595e.jpg', '12', 0),
       (2, '2024-03-25 15:51:59', '2024-03-25 15:51:59', '123333333333', '1233333333333333', '421634412@qq.com',
        '123333333333', '123', '', 0);
/*!40000 ALTER TABLE `user_info`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2024-03-27 17:44:39
