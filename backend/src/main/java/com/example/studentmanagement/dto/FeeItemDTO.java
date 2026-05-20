package com.example.studentmanagement.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FeeItemDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal amount;
    private String grade;
    private String className;
    private LocalDate deadline;
    private String description;
    private String status;
    private String createdAt;
    private Integer paidCount;
    private Integer unpaidCount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
}