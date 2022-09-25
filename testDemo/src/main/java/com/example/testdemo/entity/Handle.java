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
public class Handle {
    private Integer type;
    private Date time;
    private Integer recordId;
    private Integer courierId;
    private Integer id;
}
