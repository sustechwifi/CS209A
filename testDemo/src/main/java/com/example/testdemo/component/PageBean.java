package com.example.testdemo.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageBean<T> {
    private int totalCount;

    private List<T> rows;

    private String msg;


}
