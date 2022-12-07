package com.example.project.Controller;

import com.example.project.POJO.*;
import com.example.project.Service.CommitService;
import com.example.project.Service.IssueService;
import com.example.project.Service.ReleaseService;
import com.example.project.model.Commit;
import com.example.project.model.Issue;
import com.example.project.model.Release;
import com.example.project.utils.Result;
import com.example.project.utils.keyword.TFIDFAnalyzer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIController {

    @Resource
    CommitService comitService;

    @Resource
    IssueService issueService;

    @Resource
    ReleaseService releaseService;

    private final Function<Long, TimeWrapper> mapTime = (i) -> {
        long day = 1000L * 60 * 60 * 24;
        long month = day * 7;
        if (i < day) {
            return TimeWrapper.LESS_ONE_DAY;
        } else if (i < day * 7) {
            return TimeWrapper.LEES_ONE_WEEK;
        } else if (i < month) {
            return TimeWrapper.LESS_ONE_MONTH;
        } else if (i < 12 * month) {
            return TimeWrapper.LESS_ONE_YEAR;
        } else {
            return TimeWrapper.MORE_ONE_YEAR;
        }
    };


    @GetMapping("/{owner}/{repo}/developers/amount")
    public Result<?> getCommitNum(@PathVariable("owner") String owner, @PathVariable("repo") String repo) {
        try {
            Map<String, Integer> data = new HashMap<>(1);
            data.put("amount", comitService.get(repo, owner).size());
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(-1, "error");
        }
    }

    @CrossOrigin
    @GetMapping("/{owner}/{repo}/developers/rank")
    public Result<?> getCommitRank(@PathVariable("owner") String owner,
                                   @PathVariable("repo") String repo,
                                   @RequestParam("limit") Integer limit) {
        try {
            var commit = comitService.get(repo, owner);
            List<RankWrap> res = commit.stream()
                    .collect(Collectors.groupingBy(
                            Commit::getDeveloper,
                            Collectors.summingInt(e -> 1)))
                    .entrySet()
                    .stream()
                    .map(i -> new RankWrap(i.getKey(), i.getValue()))
                    .sorted((x, y) -> y.commits() - x.commits())
                    .limit(limit)
                    .toList();
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(-1, "error");
        }
    }

    @GetMapping("/{owner}/{repo}/issues/amount")
    public Result<?> getIssuesNum(@PathVariable("owner") String owner, @PathVariable("repo") String repo) {
        try {

            var issues = issueService.get(repo, owner);
            Long num1 = issues.stream().filter(i -> "closed".equals(i.getState())).count();
            Long num2 = issues.stream().filter(i -> "open".equals(i.getState())).count();

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


    @GetMapping("/{owner}/{repo}/issues/duration")
    public Result<?> getIssueDuration(@PathVariable("owner") String owner, @PathVariable("repo") String repo) {
        var issues = issueService.get(repo, owner);
        List<IssueDurationWrapper> res = issues.stream()
                .filter(i -> "closed".equals(i.getState()))
                .collect(
                        Collectors.groupingBy(
                                i -> mapTime.apply(
                                        i.getClosed_at().getTime() - i.getCreated_at().getTime()
                                ),
                                Collectors.summingInt(e -> 1)
                        )
                ).entrySet()
                .stream()
                .map(
                        i -> new IssueDurationWrapper(i.getKey().begin, i.getKey().end, i.getKey().label, i.getValue())
                ).toList();
        List<Issue> closedIssues = issues.stream().filter(i -> "closed".equals(i.getState())).toList();
        List<Long> timeCost = closedIssues.stream()
                .map(i -> (i.getClosed_at().getTime() - i.getCreated_at().getTime()) / 1000)
                .toList();
        Long avg = (long) timeCost.stream()
                .collect(Collectors.summarizingLong(Long::longValue))
                .getAverage();
        Double std = getStd(timeCost, avg);
        Map<String, Object> ans = new LinkedHashMap<>(3);
        ans.put("avg", avg);
        ans.put("std", std);
        ans.put("durations", res);
        return Result.success(ans);
    }

    private List<IssueDistributeWrapper> distributeWrap(long step, List<Issue> issues, Long from, int dataCnt) {
        List<IssueDistributeWrapper> res = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i <= dataCnt; i++) {
            long tmp = i * step + from;
            int amount = (int) issues.stream()
                    .filter(e -> e.getCreated_at().getTime() < tmp
                            && e.getCreated_at().getTime() > tmp - step
                    ).count();
            String label = simpleDateFormat.format(new Date(tmp));
            res.add(new IssueDistributeWrapper(tmp, label, amount));
        }
        return res;
    }

    private double getStd(List<Long> arr, Long avg) {
        double variance = 0.0;
        for (double p : arr) {
            variance += (p - avg) * (p - avg);
        }
        return variance / arr.size();
    }


    @GetMapping("/{owner}/{repo}/issues/distribution")
    public Result<?> getDistribution(@PathVariable("owner") String owner,
                                     @PathVariable("repo") String repo,
                                     @RequestParam("from") Long from,
                                     @RequestParam("to") Long to,
                                     @RequestParam("datapoint") Integer datapoint) {
        var issues = issueService.get(repo, owner);
        long step = (to - from) / datapoint;
        List<Issue> openIssues = issues.stream().filter(
                i -> i.getCreated_at().getTime() >= from
                        && i.getCreated_at().getTime() <= to
                        && "open".equals(i.getState())
        ).toList();

        List<Issue> closedIssues = issues.stream().filter(
                i -> i.getCreated_at().getTime() >= from
                        && i.getCreated_at().getTime() <= to
                        && "closed".equals(i.getState())
        ).toList();

        List<Issue> totalIssues = issues.stream().filter(
                i -> i.getCreated_at().getTime() >= from
                        && i.getCreated_at().getTime() <= to
        ).toList();
        Map<String, List<IssueDistributeWrapper>> res = new HashMap<>(3);
        res.put("open", distributeWrap(step, openIssues, from, datapoint));
        res.put("closed", distributeWrap(step, closedIssues, from, datapoint));
        res.put("total", distributeWrap(step, totalIssues, from, datapoint));
        return Result.success(res);
    }

    private List<WordCloudWrapper> getChineseKeyWord(String content, Integer limit) {
        TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();
        return tfidfAnalyzer.analyze(content, limit)
                .stream()
                .map(i -> new WordCloudWrapper(i.getName(), (int) (i.getTfidfvalue() * 100)))
                .toList();
    }


    @GetMapping("/{owner}/{repo}/issues/wordcloud")
    public Result<?> getWordCloud(@PathVariable("owner") String owner,
                                  @PathVariable("repo") String repo,
                                  @RequestParam("limit") Integer limit) {
        List<String> contents = issueService.get(repo, owner)
                .stream()
                .map(r -> r.getTitle() + r.getBody())
                .toList();
        StringBuilder sb = new StringBuilder();
        for (String content : contents) {
            sb.append(content).append("\n");
        }
        return Result.success(getChineseKeyWord(sb.toString(), limit));
    }

    @GetMapping("/{owner}/{repo}/releases/amount")
    public Result<?> getReleasesAmount(@PathVariable("owner") String owner,
                                       @PathVariable("repo") String repo) {
        var releases = releaseService.get(repo, owner);
        Map<String, Integer> res = new HashMap<>(1);
        res.put("amount", releases.size());
        return Result.success(res);
    }

    private ReleaseCommitWrapper mapCommitCount(String owner, String repo, Long from, Long end, Release release) {
        var commits = comitService.get(repo, owner).stream()
                .filter(i -> i.getTime() < from && i.getTime() > end).count();
        return new ReleaseCommitWrapper(release.getName(), end, (int) commits);
    }

    @GetMapping("/{owner}/{repo}/releases/commits")
    public Result<?> getReleaseCommitAmount(@PathVariable("owner") String owner,
                                            @PathVariable("repo") String repo,
                                            @RequestParam("limit") Integer limit) {
        var releases = releaseService.get(repo, owner).stream()
                .sorted((x, y) -> (int) (x.getPublished_at().getTime() - y.getPublished_at().getTime()))
                .limit(limit).toList();
        long from = System.currentTimeMillis();
        List<ReleaseCommitWrapper> res = new ArrayList<>();
        for (var release : releases) {
            res.add(mapCommitCount(owner, repo, from, release.getPublished_at().getTime(), release));
            from = release.getPublished_at().getTime();
        }
        return Result.success(res);
    }

    private List<CommitTimeWrapper> getInfoTime(List<Commit> commits) {
        int[][] flag = new int[7][24];
        long eightHours = 8 * 60 * 60 * 1000L;
        long oneWeek = 7 * 24 * 60 * 60 * 1000L;
        long fourDays = 4 * 24 * 60 * 60 * 1000L;
        long oneDay = 24 * 60 * 60 * 1000L;
        long oneHour = 60 * 60 * 1000L;
        for (Commit commit : commits) {
            long tmp = commit.getTime() + fourDays + eightHours;
            long weekRemainder = tmp % oneWeek;
            long dayRemainder = weekRemainder % oneDay;
            int week = (int) (weekRemainder / oneDay);
            int hour = (int) (dayRemainder / oneHour);
            flag[week][hour]++;
        }
        var res = new ArrayList<CommitTimeWrapper>();
        for (int i = 0; i < flag.length; i++) {
            for (int j = 0; j < flag[i].length; j++) {
                res.add(new CommitTimeWrapper(i, j, flag[i][j]));
            }
        }
        return res;
    }

    @GetMapping("/{owner}/{repo}/commits/distribution")
    public Result<?> getCommitDistribution(@PathVariable("owner") String owner,
                                           @PathVariable("repo") String repo) {
        var commits = comitService.get(repo, owner);
        return Result.success(getInfoTime(commits));
    }
}
