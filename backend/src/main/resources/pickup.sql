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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;