package com.example.testdemo.contorller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.example.testdemo.component.PageBean;
import com.example.testdemo.component.Result;
import com.example.testdemo.entity.User;
import com.example.testdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Autowired
    HttpServletResponse response;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 获取当前用户
     * @param token
     * @return
     */
    @GetMapping("/me")
    public Result<?> me(@RequestParam("token") Object token){
        String user = stringRedisTemplate.opsForValue().get(token);
        if (StrUtil.isBlank(user)){
            return Result.error("-1", "token 已过期或无效");
        }
        return Result.success(user);
    }

    @GetMapping("/logout")
    public Result<?> logout(@RequestParam("token") Object token){
        String user = stringRedisTemplate.opsForValue().get(token);
        if (StrUtil.isNotBlank(user)){
            stringRedisTemplate.delete(token.toString());
        }
        return Result.success();
    }

    @GetMapping("/info")
    public Result<?> info(@RequestParam("id") Long id){
        User user = userService.getUserById(id);
        return Result.success(JSONUtil.toJsonStr(user));
    }

    @PostMapping("/add")
    public Result<?> save(@RequestBody User user){
        int result = userService.addUser(user);
        System.out.println("add result: "+result);
        return result == 0 ? Result.success() : Result.error("101","添加失败");
    }

    @PostMapping("/check")
    public Result<?> checkUser(@RequestBody User user){
        boolean result = userService.checkUser(user);
        return result ? Result.success():Result.error("110","用户名已重复");
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        return userService.login(user);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user){
        int result = userService.updateUser(user);
        return result == 0 ?Result.success():Result.error("105","更新失败");
    }

    @PutMapping("/changeImg")
    public Result<?> changeImg(@RequestBody User user){
        userService.updateUserImg(user.getImg(), user.getId());
        return Result.success();
    }


    @GetMapping("/select")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "all")String type){
        System.out.println("search:"+search);
        User user = new User();
        if ("username".equals(type)) {
            user.setUsername(search);
        }else if ("nickName".equals(type)) {
            user.setNickName(search);
        }
        PageBean<User> userPageBean = userService.selectByCondition(pageNum, pageSize, user);

        String s = JSON.toJSONString(userPageBean);
        try {
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            Result.error("103",e.getMessage());
        }

        return Result.success();
    }

    @GetMapping("/findImg")
    public Result<?> findImg(@RequestParam("username") String username){
        System.out.println(username);
        String url = userService.findUserImg(username);
        return Result.success(url);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return Result.success();
    }

    @PostMapping("/deleteByids")
    public Result<?> deleteUserByIds(@RequestBody long[] ids) {
        userService.deleteUserByIds(ids);
        return Result.success();
    }
}
