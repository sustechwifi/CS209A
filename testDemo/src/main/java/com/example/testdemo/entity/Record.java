package com.example.testdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private Timestamp logTime;
    private String itemName;
    private String itemClass;
    private Long itemPrice;
    private Integer companyId;
    private Integer containerId;
    private Integer shipId;
    private Integer id;
}
