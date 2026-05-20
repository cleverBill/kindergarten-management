-- 创建园所配置表
CREATE TABLE IF NOT EXISTS kindergarten_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(255) NOT NULL UNIQUE,
    config_value TEXT,
    created_at DATETIME,
    updated_at DATETIME
);

-- 插入默认园所名称
INSERT INTO kindergarten_config (config_key, config_value, created_at, updated_at)
VALUES ('name', '田头小学幼儿园', NOW(), NOW());
