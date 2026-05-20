# 深圳市幼儿园管理系统

基于 Spring Boot 3.x + MySQL 的幼儿园管理系统，提供学生管理、教师管理、考勤管理、收费管理等功能。

## 📁 项目结构

```
AI study/
├── backend/                    # Spring Boot 后端服务
│   ├── src/main/java/          # Java 源码
│   ├── src/main/resources/     # 配置文件和静态资源
│   └── pom.xml                # Maven 依赖配置
├── frontend/                   # React 前端（备用）
├── uploads/                   # 文件上传目录
└── .gitignore                 # Git 忽略配置
```

## ✨ 功能模块

| 模块 | 说明 |
|------|------|
| 学生管理 | 添加、编辑、删除学生信息 |
| 教师管理 | 用户注册、审核、角色管理 |
| 考勤管理 | 学生出勤记录管理 |
| 作业管理 | 作业布置与评分 |
| 奖惩管理 | 学生奖惩记录 |
| 公告管理 | 通知公告发布 |
| 班级圈 | 动态发布与互动 |
| 收费管理 | 费用项目与缴费记录 |
| 接送管理 | 接送人员与记录 |
| 私信消息 | 用户间私信沟通 |

## 🛠️ 技术栈

- **后端**: Spring Boot 3.2.5 + Java 17
- **数据库**: MySQL 8.0+
- **数据库迁移**: Flyway
- **安全**: Spring Security + BCrypt
- **前端**: HTML5 + CSS3 + JavaScript

## 🚀 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置

创建数据库：
```sql
CREATE DATABASE student_management 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;
```

### 3. 修改配置

编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_management
    username: your_username
    password: your_password
```

### 4. 启动服务

```bash
cd backend
mvn spring-boot:run
```

服务启动后访问：http://localhost:8080

### 5. 默认账号

- 用户名: `admin`
- 密码: `123456`

## 📝 数据库迁移

项目使用 Flyway 进行数据库版本管理：

- 迁移脚本位置: `backend/src/main/resources/db/migration/`
- 首次启动自动执行初始迁移
- 新增表结构请创建新的迁移脚本

## 🔒 权限说明

| 角色 | 权限 |
|------|------|
| ROLE_SUPER_ADMIN | 超级管理员，全部权限 |
| 园长 | 用户管理、系统设置 |
| 教师 | 学生管理、考勤、作业 |
| 财务 | 收费管理 |
| 保洁 | 有限权限 |

## 📄 许可证

MIT License
