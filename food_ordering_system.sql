/*
 Navicat Premium Data Transfer

 Source Server         : 点餐系统
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : localhost:3306
 Source Schema         : food_ordering_system

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 26/09/2025 10:34:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of food
-- ----------------------------
INSERT INTO `food` VALUES (1, '白菜猪肉水饺', 12.00, '点餐系统图片/白菜猪肉水饺.jpg', 'daily', '新鲜手工水饺');
INSERT INTO `food` VALUES (2, '炒河粉', 10.00, '点餐系统图片/炒河粉.jpg', 'daily', '这个河粉有点宽了，我不是很爱吃');
INSERT INTO `food` VALUES (3, '螺蛳粉', 15.00, '点餐系统图片/螺蛳粉.jpg', 'daily', '不辣也不臭，但是好吃');
INSERT INTO `food` VALUES (4, '焦糖饱饱碗', 18.00, '点餐系统图片/焦糖饱饱碗.jpg', 'breakfast', '口味层次感很丰富，我不是很喜欢吃');
INSERT INTO `food` VALUES (5, '干捞玉米饺子', 12.00, '点餐系统图片/干捞玉米饺子.jpg', 'daily', '手工包的饺子，很好吃');
INSERT INTO `food` VALUES (6, '小炒肉盖浇饭', 20.00, '点餐系统图片/小炒肉盖浇饭.jpg', 'daily', '米饭太黏糊了，不太好吃');
INSERT INTO `food` VALUES (7, '肉丝拌面', 10.00, '点餐系统图片/肉丝拌面.jpg', 'breakfast', '份量很大，味道还行，性价比高');
INSERT INTO `food` VALUES (8, '黄焖鸡米饭', 25.00, '点餐系统图片/黄焖鸡米饭.jpg', 'daily', '不知道吃什么就吃这个吧');
INSERT INTO `food` VALUES (9, '烧鸭饭', 22.00, '点餐系统图片/烧鸭饭.jpg', 'daily', '广东味道，小时候的感觉');
INSERT INTO `food` VALUES (10, '肠粉', 8.00, '点餐系统图片/肠粉.jpg', 'breakfast', '甜甜的肠粉，和我以前吃的不太一样');
INSERT INTO `food` VALUES (11, '菠萝包', 6.00, '点餐系统图片/菠萝包.jpg', 'breakfast', '一个太大了，吃不完');
INSERT INTO `food` VALUES (12, '炸鸡', 28.00, '点餐系统图片/炸鸡.jpg', 'daily', '好吃，但是小贵');
INSERT INTO `food` VALUES (13, '芒果蛋糕', 16.00, '点餐系统图片/芒果蛋糕.jpg', 'breakfast', '伟大的蛋糕');
INSERT INTO `food` VALUES (14, '卤肉饭', 18.00, '点餐系统图片/卤肉饭.jpg', 'daily', '有点咸，不太下饭');
INSERT INTO `food` VALUES (15, '番茄鸡蛋面', 12.00, '点餐系统图片/番茄鸡蛋面.jpg', 'daily', '妈妈做的，伟大无需多言');
INSERT INTO `food` VALUES (16, '窑鸡', 48.00, '点餐系统图片/窑鸡.jpg', 'daily', '很嫩的鸡肉');
INSERT INTO `food` VALUES (17, '蟹黄拌面', 32.00, '点餐系统图片/蟹黄拌面.jpg', 'daily', '真的很贵');

-- ----------------------------
-- Table structure for order_details
-- ----------------------------
DROP TABLE IF EXISTS `order_details`;
CREATE TABLE `order_details`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `quantity` int NOT NULL,
  `subtotal` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_detail_order`(`order_id`) USING BTREE,
  INDEX `fk_detail_food`(`dish_id`) USING BTREE,
  CONSTRAINT `fk_detail_food` FOREIGN KEY (`dish_id`) REFERENCES `food` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_detail_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_details
-- ----------------------------
INSERT INTO `order_details` VALUES (1, 1, 7, 1, 10.00);
INSERT INTO `order_details` VALUES (2, 2, 4, 1, 18.00);
INSERT INTO `order_details` VALUES (3, 3, 7, 1, 10.00);
INSERT INTO `order_details` VALUES (4, 4, 7, 1, 10.00);
INSERT INTO `order_details` VALUES (5, 4, 13, 1, 16.00);
INSERT INTO `order_details` VALUES (6, 5, 7, 1, 10.00);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `total_price` decimal(10, 2) NOT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 2, 10.00, '2025-09-23 16:50:16');
INSERT INTO `orders` VALUES (2, 2, 18.00, '2025-09-23 17:16:51');
INSERT INTO `orders` VALUES (3, 2, 10.00, '2025-09-23 18:40:18');
INSERT INTO `orders` VALUES (4, 2, 26.00, '2025-09-23 23:18:56');
INSERT INTO `orders` VALUES (5, 2, 10.00, '2025-09-25 13:17:24');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '123', 'admin');
INSERT INTO `users` VALUES (2, 'hsc', '123', 'user');
INSERT INTO `users` VALUES (3, 'cjx', '123', 'user');

SET FOREIGN_KEY_CHECKS = 1;
