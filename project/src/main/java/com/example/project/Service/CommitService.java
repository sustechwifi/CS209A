package com.example.project.Service;

import com.alibaba.fastjson2.JSON;
import com.example.project.mapper.CommitMapper;
import com.example.project.model.Commit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommitService {
    @Resource
    CommitMapper commitMapper;

    public List<Commit> get(String repo,String owner) {
        return commitMapper.get(repo,owner)
                .stream()
                .map(i -> JSON.parseObject(i, Commit.class))
                .collect(Collectors.toList());
    }
}
