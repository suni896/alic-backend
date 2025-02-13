CREATE TABLE `chat_group` (
                              `group_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `group_name` varchar(50) NOT NULL COMMENT 'group name',
                              `group_description` varchar(500) DEFAULT NULL COMMENT 'group description',
                              `group_portrait` blob COMMENT 'group portait',
                              `group_type` tinyint NOT NULL DEFAULT '0' COMMENT '0: private\n1: public',
                              `create_time` datetime NOT NULL,
                              `delete_time` datetime DEFAULT NULL,
                              `update_time` datetime DEFAULT NULL,
                              `group_admin` bigint NOT NULL COMMENT 'group admin id',
                              `password` varchar(32) DEFAULT NULL COMMENT 'available for private group',
                              `salt` varchar(6) DEFAULT NULL,
                              PRIMARY KEY (`group_id`),
                              UNIQUE KEY `chat_group_unique` (`group_name`,`group_admin`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `chat_group_bot` (
                                  `bot_id` bigint NOT NULL AUTO_INCREMENT,
                                  `group_id` bigint NOT NULL,
                                  `bot_name` varchar(50) NOT NULL,
                                  `bot_prompt` varchar(500) NOT NULL,
                                  `bot_context` int NOT NULL,
                                  `access_type` tinyint NOT NULL COMMENT '0: only admin could access\n1: access by all group mates',
                                  `create_time` datetime NOT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  `delete_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`bot_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `chat_group_user_info` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `group_id` bigint NOT NULL,
                                        `user_id` bigint NOT NULL,
                                        `create_time` datetime NOT NULL,
                                        `update_time` datetime DEFAULT NULL,
                                        `delete_time` datetime DEFAULT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `chat_group_user_info_unique` (`group_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `chat_tag` (
                            `tag_id` bigint NOT NULL AUTO_INCREMENT,
                            `create_user` bigint NOT NULL COMMENT 'tag owner',
                            `tag_name` varchar(50) NOT NULL,
                            `create_time` datetime NOT NULL,
                            `update_time` datetime DEFAULT NULL,
                            `delete_time` datetime DEFAULT NULL,
                            `group_id_arr` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                            `tag_condition` bigint NOT NULL,
                            PRIMARY KEY (`tag_id`),
                            UNIQUE KEY `3_chat_tag_UNIQUE` (`create_user`,`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `chat_tag_group_relation` (
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `tag_id` bigint NOT NULL,
                                           `group_id` bigint NOT NULL,
                                           `create_time` datetime NOT NULL,
                                           `update_time` datetime DEFAULT NULL,
                                           `delete_time` datetime DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `chat_tag_group_relation_unique` (`tag_id`,`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_info` (
                             `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key: User ID',
                             `user_email` varchar(50) NOT NULL COMMENT 'User email',
                             `user_condition` bigint NOT NULL DEFAULT '1' COMMENT 'Account status: 0 for unactivated, 1 for active, user_id for deactivated',
                             `user_name` varchar(50) NOT NULL COMMENT 'Username',
                             `user_portrait` blob COMMENT 'User profile picture',
                             `password` varchar(32) NOT NULL COMMENT 'User password (encrypted storage)',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Registration time',
                             `delete_time` datetime DEFAULT NULL COMMENT 'Deactivation time',
                             `salt` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'salt',
                             `update_time` datetime DEFAULT NULL COMMENT 'Update time',
                             PRIMARY KEY (`user_id`),
                             UNIQUE KEY `user_email` (`user_email`,`user_condition`) COMMENT 'Unique constraint on user email and status'
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='User table';