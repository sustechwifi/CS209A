package practice.lab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Practice6 {

    public static String readZipFile(String path, String str) throws IOException {
        ZipEntry zipEntry;
        File file = new File(path);
        if (file.exists()) {
            ZipInputStream zipInputStream = new ZipInputStream(
                    new FileInputStream(path), StandardCharsets.UTF_8
            );
            int cnt = 0;
            List<String> res = new ArrayList<>();
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    str += ";" + zipEntry.getName().substring(zipEntry.getName()
                            .lastIndexOf("/") + 1);
                    String[] ss = zipEntry.getName().split("/");
                    if ("io".equals(ss[1]) || "nio".equals(ss[1])) {
                        cnt++;
                        res.add(zipEntry.getName());
                    }
                }
            }
            System.out.println("of .java files in java.io/java.nio: " + cnt);
            res.forEach(System.out::println);
        }
        return str;

    }

    private static void test(String path) {
        try {
            JarEntry jarEntry;
            JarFile jarFile = new JarFile(path);
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(path));
            int cnt = 0;
            List<String> res = new ArrayList<>();
            while ((jarEntry = (JarEntry) jarInputStream.getNextEntry()) != null) {
                if (!jarEntry.isDirectory()) {
                    String[] ss = jarEntry.getName().split("/");
                    if (!"java".equals(ss[0])) {
                        continue;
                    }
                    if (List.of(ss).contains("nio") || List.of(ss).contains("io")) {
                        cnt++;
                        res.add(jarEntry.getName());
                    }
                }
            }
            System.out.println("of .class files in java.io/java.nio: " + cnt);
            res.forEach(System.out::println);
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        readZipFile("C:\\Users\\25874\\Desktop\\CS209A\\labs\\src\\main\\resources\\src.zip", "");
        //test("C:\\Users\\25874\\Desktop\\CS209A\\labs\\src\\main\\resources\\rt.jar");
    }
}
