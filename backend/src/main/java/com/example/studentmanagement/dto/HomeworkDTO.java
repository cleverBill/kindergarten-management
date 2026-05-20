package com.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDTO {
    private Long id;
    private Long studentId;
    private String name;
    private Integer score;
    private LocalDate date;
}
