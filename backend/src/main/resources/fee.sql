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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;