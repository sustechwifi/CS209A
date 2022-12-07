package com.example.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Commit {
    Wrapper commit;

    public String getDeveloper() {
        return commit.author.name;
    }

    public Long getTime() {
        return commit.committer.getDate().getTime();
    }
}


@Data
@AllArgsConstructor
class Author {
    String name;
    String email;
    Date date;
}

@Data
@AllArgsConstructor
class Committer {
    String name;
    String email;
    Date date;
}

@Data
@AllArgsConstructor
class Wrapper {
    Author author;
    Committer committer;
    String message;
}

