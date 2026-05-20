-- 创建教师绩效表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;