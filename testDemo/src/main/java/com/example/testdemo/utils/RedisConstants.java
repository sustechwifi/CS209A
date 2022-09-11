package com.example.testdemo.utils;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "demo:login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "demo:login:token:";
    public static final Long LOGIN_USER_TTL = 3600L;
    public static final String HOME_NOTICE_KEY = "demo:home:notice:";
}
