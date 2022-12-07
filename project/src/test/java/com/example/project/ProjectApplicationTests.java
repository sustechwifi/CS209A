package com.example.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test(){
        long eightHours = 8 * 60 * 60 * 1000L;
        long oneWeek = 7 * 24 * 60 * 60 * 1000L;
        long fourDays = 4 * 24 * 60 * 60 * 1000L;
        long oneDay = 24 * 60 * 60 * 1000L;
        long oneHour = 60 * 60 * 1000L;
        long oneMinute = 60 * 1000L;
        long tmp = System.currentTimeMillis()  + fourDays + eightHours;
        long weekRemainder = tmp % oneWeek;
        long dayRemainder = weekRemainder % oneDay;
        long hourRemainder = dayRemainder % oneHour;
        long week = weekRemainder / oneDay;
        long hour = dayRemainder / oneHour;
        long minute = hourRemainder / oneMinute;
        System.out.println("week:" + week + ",hour:" + hour + ",minute:" + minute);
    }

}
