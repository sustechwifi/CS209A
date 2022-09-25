package com.example.testdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transit {
    private Integer type;
    private Date time;
    private Double tax;
    private Integer cityId;
    private Integer recordId;
    private Integer id;
}
