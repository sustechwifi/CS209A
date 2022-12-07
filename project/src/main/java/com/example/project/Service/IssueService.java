package com.example.project.Service;

import com.alibaba.fastjson2.JSON;
import com.example.project.mapper.IssueMapper;
import com.example.project.model.Issue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    @Resource
    IssueMapper issueMapper;

    public List<Issue> get(String repo,String owner) {
        return issueMapper.get(repo,owner)
                .stream()
                .map(i -> JSON.parseObject(i, Issue.class))
                .collect(Collectors.toList());
    }
}
