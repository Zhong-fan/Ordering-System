<div align="center">

# Ordering System · 桌面点餐系统

### 用 Java Swing 与 MySQL 串起点餐、购物车和订单管理。

Java Swing · JDBC · MySQL 8.0 · 课程设计

[快速开始](#快速开始) · [功能](#功能) · [项目结构](#项目结构) · [常见问题](#常见问题) · [项目资料](#项目资料)

</div>

一个本地运行的 Java 桌面课程项目。普通用户可以注册、浏览早餐与日常餐品、加入购物车并提交订单；管理员可以查看订单及明细、新增菜品。仓库包含源码、SQL 初始化数据、菜品图片和课程报告。

## 快速开始

准备 JDK、MySQL 8.0 和 IntelliJ IDEA。工程使用 IDEA 模块管理，随仓库提供 MySQL Connector/J 9.4.0，没有 Maven 或 Gradle 构建配置。

### 1. 获取项目

```sh
git clone https://github.com/Zhong-fan/Ordering-System.git
cd Ordering-System
```

### 2. 初始化数据库

在仓库根目录打开 MySQL 客户端：

```sh
mysql -u root -p
```

然后执行：

```sql
CREATE DATABASE IF NOT EXISTS food_ordering_system
  DEFAULT CHARACTER SET utf8mb4;
USE food_ordering_system;
SOURCE food_ordering_system.sql;
```

SQL 脚本不包含创建或选择数据库的语句，因此需要先执行前两步。脚本会删除并重建同名表，请在专用的本地演示数据库中导入。

### 3. 配置并启动

1. 在 IDEA 中打开仓库里的 `OrderingSystem/` 目录，配置 Project SDK，并将 `src/` 标记为 Sources Root。
2. 修改 [`DBUtil.java`](OrderingSystem/src/util/DBUtil.java) 中的数据库地址、用户名和密码，使其匹配本机 MySQL。
3. 检查模块依赖是否包含 `lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar`；未识别时手动添加。
4. 将运行配置的 Working directory 设为 `OrderingSystem/` 的绝对路径，运行 [`Main.java`](OrderingSystem/src/Main.java)。菜品图片按此工作目录下的相对路径读取。

普通用户可以通过登录窗口注册。初始化账号见[原始使用说明](使用说明.md)，仅用于本地演示。

## 功能

| 使用场景 | 当前代码提供的流程 |
| --- | --- |
| 用户登录 | 注册普通用户，登录后按角色进入对应界面 |
| 浏览餐品 | 按早餐、日常餐品分类查看名称、图片和价格 |
| 购物车 | 添加餐品、修改数量、计算合计并提交订单 |
| 订单管理 | 管理员查看订单列表及订单明细 |
| 菜品维护 | 管理员新增菜品，填写分类、价格、图片路径和描述 |

提交订单时，`OrderDao` 在同一数据库事务中写入订单和订单明细，失败时回滚。这里的“结算”指保存订单，不包含在线支付。

## 项目结构

```text
Ordering-System/
├── OrderingSystem/
│   ├── src/
│   │   ├── Main.java        # 启动入口
│   │   ├── ui/              # Swing 页面与交互
│   │   ├── dao/             # JDBC 数据访问
│   │   ├── model/           # 用户、菜品、购物车与订单实体
│   │   └── util/            # 数据库连接和图片工具
│   ├── lib/                 # JDBC 驱动
│   └── 点餐系统图片/         # 初始化菜品图片
├── food_ordering_system.sql
├── 使用说明.md
└── 课程设计报告.pdf / .docx
```

数据库包含 `users`、`food`、`orders` 和 `order_details` 四张表。阅读代码可以从 [`MainFrame`](OrderingSystem/src/ui/MainFrame.java) 的页面切换与结算流程开始，再查看 [`OrderDao`](OrderingSystem/src/dao/OrderDao.java) 的持久化逻辑。

## 常见问题

**菜品图片显示“图片未找到”？**

检查运行工作目录是否为仓库中的 `OrderingSystem/`，以及数据库 `food.image_path` 指向的文件是否存在。

**无法连接数据库？**

确认 MySQL 已启动、数据库已创建，并检查 `DBUtil.java` 的连接信息和 JDBC 驱动依赖。

**可以直接用于真实点餐业务吗？**

当前版本用于课程学习与本地演示。密码以明文保存，数据库连接信息写在源码中；尚未提供在线支付、库存控制或订单状态流转。购物车和管理表格中的删除交互仍需运行验证。

## 项目资料

- [使用说明](使用说明.md)
- [数据库初始化脚本](food_ordering_system.sql)
- [课程设计报告 · PDF](课程设计报告.pdf)
- [课程设计报告 · Word](课程设计报告.docx)

## 许可

仓库尚未声明项目许可证。随附 JDBC 驱动的许可见其目录内的 `LICENSE`。
