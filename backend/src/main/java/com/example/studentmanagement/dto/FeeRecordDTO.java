package com.example.studentmanagement.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FeeRecordDTO {
    private Long id;
    private Long feeItemId;
    private String feeItemName;
    private Long studentId;
    private String studentName;
    private BigDecimal amount;
    private String status;
    private String paidAt;
    private String paymentMethod;
    private String remark;
}