package com.example.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Release {
    String name;
    Timestamp published_at;
}
