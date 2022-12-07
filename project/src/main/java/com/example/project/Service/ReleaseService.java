package com.example.project.Service;

import com.alibaba.fastjson2.JSON;
import com.example.project.mapper.ReleaseMapper;
import com.example.project.model.Release;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleaseService {
    @Resource
    ReleaseMapper releaseMapper;

    public List<Release> get(String repo, String owner) {
        return releaseMapper.get(repo, owner).stream()
                .map(i -> JSON.parseObject(i, Release.class))
                .collect(Collectors.toList());
    }
}
