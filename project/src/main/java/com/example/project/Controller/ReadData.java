package com.example.project.Controller;

import com.alibaba.fastjson2.JSON;
import com.example.project.mapper.CommitMapper;
import com.example.project.mapper.IssueMapper;
import com.example.project.mapper.ReleaseMapper;
import com.example.project.model.Commit;
import com.example.project.model.Issue;
import com.example.project.model.Release;
import com.example.project.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

@RestController
@RequestMapping("/read")
public class ReadData {

    @Resource
    CommitMapper commitMapper;

    @Resource
    IssueMapper issueMapper;

    @Resource
    ReleaseMapper releaseMapper;

    Stack<String> names = new Stack<>();

    public <T> void listFilesForFolder(Class<T> clazz, final File folder) throws FileNotFoundException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                names.push(fileEntry.getName());
                listFilesForFolder(clazz, fileEntry);
            } else {
                String repo = names.pop();
                if (names.empty()) {
                    break;
                }
                Scanner scanner = new Scanner(new FileInputStream(fileEntry));
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }
                List<T> res = new ArrayList<>(JSON.parseArray(sb.toString(), clazz));
                if (clazz.equals(Commit.class)) {
                    for (T re : res) {
                        commitMapper.add(JSON.toJSONString(re), repo, names.peek());
                    }
                } else if (clazz.equals(Issue.class)) {
                    for (T re : res) {
                        issueMapper.add(JSON.toJSONString(re), repo, names.peek());
                    }
                } else if (clazz.equals(Release.class)) {
                    for (T re : res) {
                        releaseMapper.add(JSON.toJSONString(re), repo, names.peek());
                    }
                }
                names.push(repo);
            }
        }
    }


    @GetMapping("/commits")
    public Result<?> readCommit() throws FileNotFoundException {
        File folder = new File("C:\\Users\\25874\\Desktop\\data\\commits");
        listFilesForFolder(Commit.class, folder);
        return Result.success();
    }

    @GetMapping("/issues")
    public Result<?> readIssue() throws FileNotFoundException {
        File folder = new File("C:\\Users\\25874\\Desktop\\data\\issues");
        listFilesForFolder(Issue.class, folder);
        return Result.success();
    }

    @GetMapping("/release")
    public Result<?> readRelease() throws FileNotFoundException {
        File folder = new File("C:\\Users\\25874\\Desktop\\data\\releases");
        listFilesForFolder(Release.class, folder);
        return Result.success();
    }
}
