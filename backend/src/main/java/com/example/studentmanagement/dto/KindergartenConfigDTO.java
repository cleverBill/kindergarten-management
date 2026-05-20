package com.example.studentmanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KindergartenConfigDTO {
    private String name;
    private String logo;
    private String address;
    private String phone;
    private String openingTime;
}
