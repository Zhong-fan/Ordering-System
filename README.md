# 点餐系统 Ordering System

一个基于 Java Swing + JDBC + MySQL 的桌面点餐系统课程设计项目，包含普通用户点餐流程、购物车结算、订单持久化，以及管理员订单/菜品管理功能。

当前项目目录除了 Java 源码外，还包含数据库脚本、使用说明、课程设计报告和菜品图片资源，适合直接作为课程设计归档或上传到 GitHub 展示。

## 项目资料

- [使用说明](使用说明.md)
- [数据库初始化脚本](food_ordering_system.sql)
- [课程设计报告（PDF）](课程设计报告.pdf)
- [课程设计报告（Word）](课程设计报告.docx)

## 项目简介

本项目实现了一个本地运行的桌面端点餐系统，使用 Java Swing 构建图形界面，使用 JDBC 连接 MySQL 数据库完成用户、菜品、订单和订单明细的数据管理。

系统分为两类角色：

- 普通用户：注册、登录、浏览菜品、加入购物车、修改数量、提交订单
- 管理员：登录后台、查看订单列表与订单明细、添加菜品、删除菜品

## 功能模块

### 1. 用户端功能

- 用户登录
- 用户注册
- 首页分类导航
- 早餐类菜品浏览
- 日常餐品浏览
- 菜品详情展示
- 加入购物车
- 修改购物车商品数量
- 删除购物车商品
- 购物车结算并生成订单

### 2. 管理端功能

- 管理员登录
- 查看全部订单
- 查看订单明细
- 新增菜品
- 删除菜品
- 菜品分类管理（`breakfast` / `daily`）

## 技术栈

- Java
- Java Swing
- JDBC
- MySQL 8.0
- IntelliJ IDEA 工程结构
- MySQL Connector/J 9.4.0

## 项目结构

```text
Ordering-System/
├─ README.md
├─ food_ordering_system.sql          # 数据库建表与初始化数据脚本
├─ 使用说明.md                        # 原始使用说明
├─ 课程设计报告.docx
├─ 课程设计报告.pdf
└─ OrderingSystem/
   ├─ src/
   │  ├─ Main.java                   # 程序入口
   │  ├─ dao/                        # 数据访问层
   │  ├─ model/                      # 实体类
   │  ├─ ui/                         # Swing 界面层
   │  └─ util/                       # 工具类（数据库连接、图片处理等）
   ├─ lib/
   │  └─ mysql-connector-j-9.4.0/
   ├─ 点餐系统图片/                    # 菜品图片资源
   ├─ .gitignore
   └─ OrderingSystem.iml
```

## 核心类说明

### 入口与主流程

- `OrderingSystem/src/Main.java`
  - 程序启动入口
  - 初始化主窗口 `MainFrame`
  - 打开登录窗口 `LoginFrame`

- `OrderingSystem/src/ui/MainFrame.java`
  - 主窗口容器
  - 根据用户角色切换普通用户界面或管理员界面
  - 管理首页、分类页、购物车页、后台页之间的跳转
  - 管理购物车数据和结算逻辑

### 数据访问层

- `dao/UserDao.java`
  - 处理登录、注册等用户相关数据库操作

- `dao/FoodDao.java`
  - 查询菜品列表
  - 按分类加载菜品
  - 新增/删除菜品

- `dao/OrderDao.java`
  - 创建订单
  - 写入订单明细
  - 查询全部订单
  - 查询指定订单详情

### 工具类

- `util/DBUtil.java`
  - MySQL 驱动加载
  - JDBC 连接创建
  - 数据库资源关闭

- `util/ImageUtil.java`
  - 菜品图片处理相关逻辑

## 数据库设计

项目数据库名为：

```sql
food_ordering_system
```

数据库脚本文件：

```text
food_ordering_system.sql
```

脚本中包含以下 4 张核心表：

- `users`：用户表
- `food`：菜品表
- `orders`：订单表
- `order_details`：订单明细表

### 已初始化的测试数据

脚本中已预置：

- 管理员账号 1 个
- 普通用户账号若干
- 17 条菜品数据
- 若干测试订单数据

## 默认账号

根据项目现有说明文件和数据库脚本，默认测试账号如下：

### 管理员账号

- 用户名：`admin`
- 密码：`123`

### 普通用户账号

- 用户名：`hsc`
- 密码：`123`

也可以直接通过注册功能创建新的普通用户账号。

## 运行环境

建议环境：

- JDK 8 及以上
- MySQL 8.0
- IntelliJ IDEA
- Windows（项目内图片路径和界面字体更贴近 Windows 环境）

## 运行前准备

### 1. 创建数据库

先在 MySQL 中创建数据库，或直接执行 SQL 脚本完成初始化。

可以使用 Navicat、MySQL Workbench 或命令行执行：

```sql
source food_ordering_system.sql;
```

如果数据库不存在，也可以先手动创建：

```sql
CREATE DATABASE food_ordering_system DEFAULT CHARACTER SET utf8mb4;
USE food_ordering_system;
```

然后再执行脚本内容。

### 2. 修改数据库连接配置

数据库连接配置位于：

`OrderingSystem/src/util/DBUtil.java`

当前源码中的默认配置为：

```java
private static final String URL = "jdbc:mysql://localhost:3306/food_ordering_system?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
private static final String USER = "root";
private static final String PASSWORD = "123456";
```

如果你的 MySQL 用户名、密码或端口不同，需要先改成你本机的实际配置。

### 3. 配置 JDBC 驱动

项目已经在 `OrderingSystem/lib/` 目录中提供 MySQL 驱动：

- `mysql-connector-j-9.4.0.jar`

如果使用 IntelliJ IDEA，通常直接打开 `OrderingSystem` 工程即可识别 `.iml` 中的库配置；如果没有自动识别，需要手动把该 JAR 加入项目依赖。

## 运行方式

### IntelliJ IDEA 运行

1. 使用 IntelliJ IDEA 打开 `OrderingSystem` 目录
2. 确认项目 SDK 已配置
3. 确认 MySQL 驱动已正确加入依赖
4. 确认数据库脚本已经执行完成
5. 运行 `src/Main.java`

### 命令行运行

如果需要命令行编译运行，需要自行处理 classpath，并把 MySQL 驱动 JAR 加入运行参数。由于该项目是 IDEA 工程结构，推荐直接使用 IDEA 运行，配置成本更低。

## 使用流程

### 普通用户

1. 启动程序
2. 在登录窗口输入普通用户账号，或点击注册创建新账号
3. 登录后进入首页
4. 选择早餐或日常餐品分类
5. 浏览菜品并加入购物车
6. 在购物车中修改数量或删除菜品
7. 点击结算生成订单

### 管理员

1. 使用管理员账号登录
2. 进入管理员后台
3. 在“订单管理”中查看订单及订单详情
4. 在“菜品管理”中新增菜品
5. 删除不需要的菜品

## 图片资源说明

项目内已包含菜品图片资源目录：

```text
OrderingSystem/点餐系统图片/
```

当前包含的菜品图片示例有：

- 白菜猪肉水饺
- 炒河粉
- 螺蛳粉
- 焦糖饱饱碗
- 黄焖鸡米饭
- 烧鸭饭
- 番茄鸡蛋面
- 炸鸡
- 蟹黄拌面

数据库中的 `image_path` 字段与该目录中的图片资源相对应。

## 已实现的特点

- 采用图形化桌面界面，适合课程设计展示
- 用户端与管理员端分角色展示
- 支持注册、登录、点餐、购物车、结算完整闭环
- 支持订单持久化保存到 MySQL
- 支持管理员维护菜品数据
- 项目结构清晰，适合后续继续扩展

## 目前的局限

- 使用明文密码存储，不适合生产环境
- 数据库连接配置写死在源码中，部署灵活性较低
- 没有使用 Maven 或 Gradle 管理依赖
- 暂未实现订单状态流转、支付、库存等高级功能
- 项目主要面向本地单机演示，不是 Web 项目

## 后续可扩展方向

- 增加菜品搜索、筛选和排序
- 增加订单状态管理
- 增加用户个人历史订单查询
- 增加菜品编辑功能
- 增加数据校验和异常处理
- 使用配置文件管理数据库连接信息
- 引入 Maven/Gradle 统一依赖管理
- 优化界面样式和交互体验

## 课程设计说明

这是一个典型的 Java 基础课程设计项目，覆盖了：

- 面向对象建模
- GUI 图形界面开发
- JDBC 数据库编程
- 多表关系设计
- 业务流程串联

如果你准备把它上传到 GitHub，建议同时保留：

- 源码
- SQL 初始化脚本
- 运行说明
- 课程设计报告

这样仓库内容会更完整，也更适合作为课程项目展示。

## 许可说明

当前仓库未单独声明开源许可证。如需公开发布，建议后续补充 `LICENSE` 文件。
