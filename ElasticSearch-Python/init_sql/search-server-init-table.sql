
DROP DATABASE IF EXISTS search_server;

CREATE DATABASE IF NOT EXISTS search_server DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE search_server;



SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for series
-- ----------------------------
DROP TABLE IF EXISTS `series`;
CREATE TABLE If Not Exists `series`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ContentId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SeriesId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CreateDate` datetime(0) NULL DEFAULT NULL,
  `PublishTime` datetime(0) NULL DEFAULT NULL,
  `ValidTime` datetime(0) NULL DEFAULT NULL,
  `InvalidTime` datetime(0) NULL DEFAULT NULL,
  `Sequence` int(8) NULL DEFAULT NULL,
  `Status` int(8) NULL DEFAULT NULL,
  `Actors` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Directors` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Intro` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Keywords` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ProgramType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DetailPicUrl` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PosterPicUrl` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Rating` int(4) NULL DEFAULT NULL,
  `CpId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Counts` int(4) NULL DEFAULT NULL,
  `TotalNum` int(4) NULL DEFAULT NULL,
  `Tags` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UpdateNum` int(4) NULL DEFAULT NULL,
  `Score` int(4) NULL DEFAULT NULL,
  `ReleaseYear` varchar(255) NULL DEFAULT NULL,
  `Kind` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Price` double NULL DEFAULT NULL,
  `isDelete` int(4) NULL DEFAULT 0,
  `isSync` int(4) NULL DEFAULT 0,
  `insertDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `isAnalyse` int(4) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_series_Name`(`Name`) USING BTREE,
  INDEX `index_series_SeriesId`(`SeriesId`) USING BTREE,
  INDEX `index_series_ContentId`(`ContentId`) USING BTREE,
  INDEX `index_series_Actors`(`Actors`) USING BTREE,
  INDEX `index_series_Directors`(`Directors`) USING BTREE,
  INDEX `index_series_isDelete`(`isDelete`) USING BTREE,
  INDEX `index_series_isSync`(`isSync`) USING BTREE,
  INDEX `index_series_insertDate`(`insertDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;




SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for suggest
-- ----------------------------
DROP TABLE IF EXISTS `suggest`;
CREATE TABLE If Not Exists `suggest`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL unique,
  `describe` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keywords` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isDelete` int(4) NULL DEFAULT 0,
  `isSync` int(4) NULL DEFAULT 0,
  `insertDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_suggest_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `ik_hot_words`;
CREATE TABLE If Not Exists `ik_hot_words`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `words` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL unique,
  `isDelete` int(4) NULL DEFAULT 0,
  `isSync` int(4) NULL DEFAULT 0,
  `insertDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_ik_hot_words_word`(`words`) USING BTREE,
  INDEX `index_ik_hot_words_isDelete`(`isDelete`) USING BTREE,
  INDEX `index_ik_hot_words_isSync`(`isSync`) USING BTREE,
  INDEX `index_ik_hot_words_insertDate`(`insertDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `ik_hot_stopwords`;
CREATE TABLE If Not Exists `ik_hot_stopwords`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `words` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL unique,
  `isDelete` int(4) NULL DEFAULT 0,
  `isSync` int(4) NULL DEFAULT 0,
  `insertDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_ik_hot_stopwords_word`(`words`) USING BTREE,
  INDEX `index_ik_hot_stopwords_isDelete`(`isDelete`) USING BTREE,
  INDEX `index_ik_hot_stopwords_isSync`(`isSync`) USING BTREE,
  INDEX `index_ik_hot_stopwords_insertDate`(`insertDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `search_hot_words`;
CREATE TABLE If Not Exists `search_hot_words`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `words` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contentId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Counts` int(8) NULL DEFAULT 0,
  `searchDate` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_search_hot_words_words`(`words`) USING BTREE,
  INDEX `index_search_hot_words_contentId`(`contentId`) USING BTREE,
  INDEX `index_search_hot_words_searchDate`(`searchDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

alter table search_hot_words add unique index shw(words,contentId,searchDate);

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `search_report_words`;
CREATE TABLE If Not Exists `search_report_words`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `searchWord` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contentId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `userToken` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `searchTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_search_report_words_name`(`name`) USING BTREE,
  INDEX `index_search_report_words_searchWord`(`searchWord`) USING BTREE,
  INDEX `index_search_report_words_contentId`(`contentId`) USING BTREE,
  INDEX `index_search_report_words_userToken`(`userToken`) USING BTREE,
  INDEX `index_search_report_words_searchTime`(`searchTime`) USING BTREE,
  INDEX `index_search_report_words_createTime`(`createTime`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `cast`;
CREATE TABLE If Not Exists `cast`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `castId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL unique,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `height` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `weight` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `eduction` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nation` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hometown` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gender` int(4) DEFAULT 0,
  `birthday` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `poster` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `thumbnail` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `constellation` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ename` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tags` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `subname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keyword` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isDelete` int(4) NULL DEFAULT 0,
  `isSync` int(4) NULL DEFAULT 0,
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_cast_name`(`name`) USING BTREE,
  INDEX `index_cast_isDelete`(`isDelete`) USING BTREE,
  INDEX `index_cast_isSync`(`isSync`) USING BTREE,
  INDEX `index_cast_updateTime`(`updateTime`) USING BTREE,
  INDEX `index_cast_createTime`(`createTime`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
