-- ==============================================
-- Flyway Migration: V1__initial_schema.sql
-- 深圳市幼儿园管理系统 - 初始数据库结构
-- ==============================================

-- ==============================================
-- 1. 园所配置表
-- ==============================================
CREATE TABLE IF NOT EXISTS kindergarten_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL,
    config_value TEXT,
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY UK_etfjtgmghake0twt2u9uq8m8d (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='园所配置表';

-- ==============================================
-- 2. 用户表
-- ==============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(50) DEFAULT 'teacher',
    status VARCHAR(20) DEFAULT 'pending',
    kindergarten VARCHAR(200),
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ==============================================
-- 3. 学生表
-- ==============================================
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    grade VARCHAR(20),
    class_name VARCHAR(50),
    gender VARCHAR(10),
    age INT,
    parent_contact VARCHAR(20),
    address VARCHAR(500),
    avatar_url VARCHAR(500),
    status VARCHAR(20) DEFAULT '正常',
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- ==============================================
-- 4. 奖惩记录表
-- ==============================================
CREATE TABLE IF NOT EXISTS rewards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    type VARCHAR(50),
    description VARCHAR(500),
    date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='奖惩记录表';

-- ==============================================
-- 5. 考勤表
-- ==============================================
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    reason VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤表';

-- ==============================================
-- 6. 作业表
-- ==============================================
CREATE TABLE IF NOT EXISTS homework (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    score INT,
    date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业表';

-- ==============================================
-- 7. 公告表
-- ==============================================
CREATE TABLE IF NOT EXISTS announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    image_url VARCHAR(500),
    scope VARCHAR(20) DEFAULT 'all',
    grade VARCHAR(20),
    class_name VARCHAR(50),
    is_top TINYINT DEFAULT 0,
    is_urgent TINYINT DEFAULT 0,
    created_by BIGINT,
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ==============================================
-- 8. 公告阅读记录表
-- ==============================================
CREATE TABLE IF NOT EXISTS announcement_read_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    announcement_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    username VARCHAR(255),
    read_at DATETIME,
    UNIQUE KEY UK_announcement_user (announcement_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告阅读记录表';

-- ==============================================
-- 9. 私信消息表
-- ==============================================
CREATE TABLE IF NOT EXISTS private_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_read TINYINT DEFAULT 0,
    created_at DATETIME,
    INDEX idx_sender (sender_id),
    INDEX idx_receiver (receiver_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='私信消息表';

-- ==============================================
-- 10. 教师绩效表
-- ==============================================
CREATE TABLE IF NOT EXISTS teacher_performance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    month VARCHAR(50),
    attendance_count INT DEFAULT 0,
    homework_count INT DEFAULT 0,
    reward_count INT DEFAULT 0,
    post_count INT DEFAULT 0,
    message_count INT DEFAULT 0,
    parent_rating DECIMAL(5,2) DEFAULT 0.0,
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师绩效表';

-- ==============================================
-- 11. 接送人员表
-- ==============================================
CREATE TABLE IF NOT EXISTS pickup_person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    relation VARCHAR(50) COMMENT 'father-父亲,mother-母亲,grandpa-爷爷,grandma-奶奶,other-其他',
    id_card VARCHAR(20),
    is_verified TINYINT(1) DEFAULT 0,
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接送人员表';

-- ==============================================
-- 12. 接送记录表
-- ==============================================
CREATE TABLE IF NOT EXISTS pickup_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    pickup_person_id BIGINT,
    pickup_person_name VARCHAR(50),
    pickup_type VARCHAR(20) COMMENT 'in-入园,out-离园',
    pickup_time DATETIME NOT NULL,
    pickup_method VARCHAR(20) COMMENT 'qrcode-二维码,face-人脸,manual-手动',
    status VARCHAR(20) DEFAULT 'normal' COMMENT 'normal-正常,abnormal-异常',
    remark VARCHAR(500),
    operator_id BIGINT,
    created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接送记录表';

-- ==============================================
-- 13. 收费项目表
-- ==============================================
CREATE TABLE IF NOT EXISTS fee_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL COMMENT 'tuition-学费,meals-伙食费,activity-活动费,other-其他',
    amount DECIMAL(10,2) NOT NULL,
    grade VARCHAR(20),
    class_name VARCHAR(50),
    deadline DATE,
    description VARCHAR(500),
    status VARCHAR(20) DEFAULT 'active' COMMENT 'active-进行中,closed-已结束',
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收费项目表';

-- ==============================================
-- 14. 收费记录表
-- ==============================================
CREATE TABLE IF NOT EXISTS fee_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fee_item_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'unpaid' COMMENT 'paid-已缴费,unpaid-未缴费,overdue-逾期',
    paid_at DATETIME,
    payment_method VARCHAR(50),
    operator_id BIGINT,
    remark VARCHAR(500),
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY UK_fee_student (fee_item_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收费记录表';

-- ==============================================
-- 15. 班级圈动态表
-- ==============================================
CREATE TABLE IF NOT EXISTS class_circle_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT,
    image_urls VARCHAR(2000),
    scope VARCHAR(20) DEFAULT 'all',
    grade VARCHAR(20),
    class_name VARCHAR(50),
    like_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    created_at DATETIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级圈动态表';

-- ==============================================
-- 16. 班级圈评论表
-- ==============================================
CREATE TABLE IF NOT EXISTS class_circle_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级圈评论表';

-- ==============================================
-- 17. 班级圈点赞表
-- ==============================================
CREATE TABLE IF NOT EXISTS class_circle_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME,
    UNIQUE KEY UK_post_user (post_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级圈点赞表';

-- ==============================================
-- 插入初始数据
-- ==============================================

-- 插入超级管理员用户
-- 密码: 123456 (BCrypt加密)
INSERT IGNORE INTO users (username, password, email, role, status, kindergarten, created_at) VALUES 
('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'admin@kindergarten.com', 'ROLE_SUPER_ADMIN', 'approved', '深圳市幼儿园管理系统', NOW());

-- 插入默认园所配置
INSERT IGNORE INTO kindergarten_config (config_key, config_value, created_at) VALUES 
('system_name', '深圳市幼儿园管理系统', NOW()),
('system_logo', '', NOW()),
('business_hours', '08:00-17:30', NOW()),
('contact_phone', '', NOW()),
('address', '', NOW());

-- ==============================================
-- 创建索引优化查询性能
-- ==============================================

-- 学生表索引
CREATE INDEX IF NOT EXISTS idx_students_grade ON students(grade);
CREATE INDEX IF NOT EXISTS idx_students_class ON students(class_name);
CREATE INDEX IF NOT EXISTS idx_students_status ON students(status);

-- 考勤表索引
CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendance(student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(date);

-- 作业表索引
CREATE INDEX IF NOT EXISTS idx_homework_student ON homework(student_id);
CREATE INDEX IF NOT EXISTS idx_homework_date ON homework(date);

-- 公告表索引
CREATE INDEX IF NOT EXISTS idx_announcement_scope ON announcement(scope);
CREATE INDEX IF NOT EXISTS idx_announcement_top ON announcement(is_top);

-- 收费记录表索引
CREATE INDEX IF NOT EXISTS idx_fee_record_student ON fee_record(student_id);
CREATE INDEX IF NOT EXISTS idx_fee_record_status ON fee_record(status);

-- 接送记录表索引
CREATE INDEX IF NOT EXISTS idx_pickup_record_student ON pickup_record(student_id);
CREATE INDEX IF NOT EXISTS idx_pickup_record_time ON pickup_record(pickup_time);
