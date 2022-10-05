package com.example.testdemo.utils;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "test:login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "test:login:token:";
    public static final Long LOGIN_USER_TTL = 3600L;

    public static final Long INSERT_TTL = 600L;
    public static final String INSERT_CITY_KEY = "test:insert:city:";
    public static final String INSERT_SHIP_KEY = "test:insert:ship:";
    public static final String INSERT_COMPANY_KEY = "test:insert:company:";
    public static final String INSERT_CONTAINER_KEY = "test:insert:container:";
    public static final String INSERT_COURIER_KEY = "test:insert:courier:";

    public static final String SEARCH_PRECISE_KEY = "test:search:precise:";
    public static final String SEARCH_BATCH_KEY = "test:search:batch:";

    public static final String SEARCH_ADVANCE_KEY1 = "test:search:advance1:";
    public static final String SEARCH_ADVANCE_KEY2 = "test:search:advance2:";
    public static final String SEARCH_ADVANCE_KEY3 = "test:search:advance3:";


    public static String formatPageBean(int begin, int size, int type) {
        return ":" + begin + ":" + size + ":" + type;
    }

    public static String formatPageBeanBatch(String item, String search, int begin, int size, int type) {
        return item + ":" + search + ":" + begin + ":" + size + ":" + type;
    }

}
