package com.example.testdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Courier {
    private String phoneNumber;
    private String name;
    private Integer birthYear;
    private Integer gender;
    private Integer companyId;
    private Integer cityId;
    private Integer id;

}
