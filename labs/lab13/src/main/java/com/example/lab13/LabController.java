package com.example.lab13;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class LabController {
    private static Student[] students = new Student[]{
            new Student(1, "Mary", "mary@gmail.com"),
            new Student(2, "Alex", "alex@gmail.com"),
            new Student(3, "Dean", "dean@yahoo.com"),
    };

    @GetMapping("/students")
    public Student[] getStudents(@RequestParam(value = "email", required = false) String email) {
        if (email == null) {
            return students;
        }
        return Arrays.stream(students)
                .filter(i -> i.getEmail().contains(email))
                .toList()
                .toArray(Student[]::new);
    }

    @PutMapping("/students/{id}")
    public void changeInfo(@PathVariable("id") Integer id,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email) {
        for (Student student : students) {
            if (Objects.equals(student.getId(), id)) {
                student.setName(name);
                student.setEmail(email);
            }
        }
    }
}

class Student {
    private Integer id;
    private String name;
    private String email;

    public Student(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
