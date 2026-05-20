package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class PickupPersonDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String name;
    private String phone;
    private String relation;
    private String idCard;
    private Boolean isVerified;
}