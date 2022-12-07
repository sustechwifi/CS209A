package com.example.project.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Issue {
    private String state;
    private String title;
    private String body;
    private Timestamp created_at;
    private Timestamp closed_at;
}
