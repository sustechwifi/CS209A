package com.example.testdemo;

import com.example.testdemo.HttpTest.HttpRestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

@SpringBootTest
class TestDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    String testHttp(){
        try{
            //api url地址
            String url = "https://api.github.com/repos/sustechwifi/CS209A/commits";
            //post请求
            HttpMethod method = HttpMethod.POST;
            String result = HttpRestUtils.get(url,null);
            System.out.print("接收反馈："+result);
            return result;
        }catch (Exception e){
            System.out.println( "------------- "+this.getClass().toString()+".PostData() : 出现异常 Exception -------------");
            System.out.println(e.getMessage());
            return "";
        }
    }
}
