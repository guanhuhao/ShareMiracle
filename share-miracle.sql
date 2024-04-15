/*
 Project Name : 工程实践
 Author Name  : Liutujun
 Server Type  : MySQL
 Date         : 2024-04-15 15:07:57
*/

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS `share_miracle`;
CREATE DATABASE IF NOT EXISTS share_miracle DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE share_miracle;

-- ----------------------------
-- Table structure for r_dataset_organization
-- ----------------------------
DROP TABLE IF EXISTS `r_dataset_organization`;
CREATE TABLE IF NOT EXISTS `r_dataset_organization` (
    `id` bigint UNIQUE NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    `dataset_id` bigint NOT NULL COMMENT '数据集id',
    `organization_id` bigint NOT NULL COMMENT '组织id',
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_DatasetID_r_dataset_organization` FOREIGN KEY (`dataset_id`) REFERENCES `t_dataset` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_OrganID_r_dataset_organization` FOREIGN KEY (`organization_id`) REFERENCES `t_organization` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='标明哪些组织有权使用该数据集';

-- ----------------------------
-- Table structure for r_model_organization
-- ----------------------------
DROP TABLE IF EXISTS `r_model_organization`;
CREATE TABLE IF NOT EXISTS `r_model_organization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
  `model_id` bigint NOT NULL COMMENT '模型id',
  `organization_id` bigint NOT NULL COMMENT '组织id',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_ModelID_r_model_organization` FOREIGN KEY (`model_id`) REFERENCES `t_model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_OrganID_r_model_organization` FOREIGN KEY (`organization_id`) REFERENCES `t_organization` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='标明哪些组织有权使用对应模型';

-- ----------------------------
-- Table structure for r_user_organization
-- ----------------------------
DROP TABLE IF EXISTS `r_user_organization`;
CREATE TABLE IF NOT EXISTS `r_user_organization` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
 `user_id` bigint NOT NULL COMMENT '用户id',
 `organization_id` bigint NOT NULL COMMENT '组织id',
 `authority` int NOT NULL COMMENT '用户在该组织中所拥有的权限（0 创建者、1 管理权限、2 无权限）',
 `status` tinyint NOT NULL DEFAULT 0 COMMENT '用户在组织中的状态（1代表可用 0代表禁用）',
 PRIMARY KEY (`id`),
 CONSTRAINT `FK_UserID_r_user_organization` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT `FK_OrganID_r_user_organization` FOREIGN KEY (`organization_id`) REFERENCES `t_organization` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户组织关系表';

-- ----------------------------
-- Table structure for t_dataset
-- ----------------------------
DROP TABLE IF EXISTS `t_dataset`;
CREATE TABLE IF NOT EXISTS `t_dataset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '数据集名称',
  `user_id` bigint NOT NULL COMMENT '用户主键(该数据库由谁建立)',
  `is_public` tinyint NOT NULL COMMENT '数据集是否公开',
  `dataset_url` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '存储数据集的url',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '上次更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_UserID_t_dataset` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据集信息表';

-- ----------------------------
-- Table structure for t_model
-- ----------------------------
DROP TABLE IF EXISTS `t_model`;
CREATE TABLE IF NOT EXISTS `t_model` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '模型名',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `is_public` tinyint NOT NULL COMMENT '模型是否公开',
  `model_url` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '存储模型的url',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最近一次更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_UserID_t_model` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='模型数据表';

-- ----------------------------
-- Table structure for t_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_organization`;
CREATE TABLE IF NOT EXISTS `t_organization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '组织名字',
  `type` int NOT NULL COMMENT '组织类型（如学校等）',
  `logo_url` varchar(200) CHARACTER SET utf8 NULL COMMENT '组织头像url',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组织表';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 自增',
  `name` varchar(50) CHARACTER SET utf8 NULL COMMENT '用户名字（真实姓名）',
  `username` varchar(50) CHARACTER SET utf8 UNIQUE NOT NULL COMMENT '用户名（唯一）',
  `email` varchar(20) CHARACTER SET utf8 UNIQUE NOT NULL COMMENT '用户邮箱',
  `logo_url` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '用户头像url',
  `create_time` datetime NOT NULL COMMENT '用户注册时间',
  `sex` tinyint NULL COMMENT '用户性别',
  `phone` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '用户电话号码',
  `password` varchar(300) CHARACTER SET utf8 NOT NULL COMMENT '用户密码',
  `authority` int NOT NULL COMMENT '用户权限（0 管理员 1 普通用户）',
  `status` tinyint NOT NULL COMMENT '用户状态（1代表可用 0代表禁用）',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;
