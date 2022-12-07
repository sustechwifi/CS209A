package com.example.project.onlineFetch;

import com.alibaba.fastjson.JSON;
import com.example.project.HttpTest.HttpRestUtils;
import com.example.project.POJO.RankWrap;
import com.example.project.model.Commit;
import com.example.project.model.Issue;
import com.example.project.utils.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class OnlineController {

    private static  <T> List<T> mutGet(Class<T> clazz, String s, int times) {
        Integer page = 100;
        List<Thread> threads = new ArrayList<>();
        List<T> ts = new ArrayList<>();
        StringBuilder sb = new StringBuilder(s);
        for (int i = 1; i <= times; i++) {
            String url = sb.append(String.format("&per_page=%d&page=%d", page, i)).toString();
            Runnable r = () -> {
                try {
                    String result = HttpRestUtils.get(url, null);
                    ts.addAll(JSON.parseArray(result, clazz));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            var t = new Thread(r);
            t.start();
            threads.add(t);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ts;
    }


    public static Result<?> getCommitNum(String owner, String repo) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/commits?", owner, repo);
            var commit = mutGet(Commit.class, url, 1);
            Map<String, Integer> data = new HashMap<>(1);
            data.put("amount", commit.size());
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(-1, "error");
        }
    }



    public static Result<?> getCommitRank(String owner, String repo, Integer limit) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/commits?", owner, repo);
            var commit = mutGet(Commit.class, url, 10);
            List<RankWrap> res = commit.stream()
                    .collect(Collectors.groupingBy(
                            Commit::getDeveloper,
                            Collectors.summingInt(e -> 1)))
                    .entrySet()
                    .stream()
                    .map(i -> new RankWrap(i.getKey(), i.getValue()))
                    .sorted((x,y) -> y.commits() - x.commits())
                    .limit(limit)
                    .toList();
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(-1, "error");
        }
    }

    public static Result<?> getIssuesNum(String owner,  String repo) {
        try {
            String url1 = String.format("https://api.github.com/repos/%s/%s/issues?state=%s", owner, repo, "closed");
            var issues1 = mutGet(Issue.class, url1, 1);
            Long num1 = issues1.stream().filter(i -> "closed".equals(i.getState())).count();

            String url2 = String.format("https://api.github.com/repos/%s/%s/issues?", owner, repo);
            var issues2 = mutGet(Issue.class, url2, 1);
            Long num2 = issues2.stream().filter(i -> "open".equals(i.getState())).count();

            Long num3 = num1 + num2;
            Map<String, Long> data = new HashMap<>(2);
            data.put("closed", num1);
            data.put("open", num2);
            data.put("total", num3);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(-1, "error");
        }
    }

}
