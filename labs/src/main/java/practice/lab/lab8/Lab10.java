package practice.lab.lab8;


import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lab10 {


    @Data
    @AllArgsConstructor
    class Pokemon {
        String name;
        Integer id;
        Integer weight;
        Integer height;
        List<Ability> abilities;

        @Override
        public String toString() {
            return "\nName: " + name + '\n' +
                    "Height: " + height + "\n" +
                    "Weight=" + weight + "\n" +
                    "Abilities=" + abilities + "\n";
        }
    }


    @Data
    @AllArgsConstructor
    class Ability {
        private Integer slot;
        private Boolean isHidden;
        private Wrapper ability;

        @Override
        public String toString() {
            return ability.toString();
        }
    }

    @Data
    @AllArgsConstructor
    class Wrapper {
        String name;
        String url;

        @Override
        public String toString() {
            return "ability=" + name + " , " + " url=" + url;
        }
    }

    public static List<Pokemon> main() throws InterruptedException {
        HttpClient httpClient = new HttpClient();
        List<Pokemon> result = new ArrayList<>();
        String name = "pikachu";
        var getMethod = new GetMethod("https://pokeapi.co/api/v2/pokemon/" + name + "/");
        try {
            int code = httpClient.executeMethod(getMethod);
            if (code == 200) {
                var in = new Scanner(getMethod.getResponseBodyAsStream());
                String temp = in.next();
                result.add(JSON.parseObject(temp, Pokemon.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        var res = main();
        System.out.println(res);
    }
}
