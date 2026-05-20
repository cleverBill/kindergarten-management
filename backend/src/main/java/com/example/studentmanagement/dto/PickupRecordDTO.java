package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class PickupRecordDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long pickupPersonId;
    private String pickupPersonName;
    private String pickupType;
    private String pickupTime;
    private String pickupMethod;
    private String status;
    private String remark;
}