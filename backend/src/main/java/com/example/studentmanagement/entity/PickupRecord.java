package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "pickup_person_id")
    private Long pickupPersonId;

    @Column(name = "pickup_person_name")
    private String pickupPersonName;

    @Column(name = "pickup_type", length = 20)
    private String pickupType;

    @Column(name = "pickup_time", nullable = false)
    private LocalDateTime pickupTime;

    @Column(name = "pickup_method", length = 20)
    private String pickupMethod;

    @Column(length = 20)
    private String status = "normal";

    @Column(length = 500)
    private String remark;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}