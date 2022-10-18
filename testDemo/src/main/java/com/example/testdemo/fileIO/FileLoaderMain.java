package com.example.testdemo.fileIO;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class FileLoaderMain {
    static FileDataReader fileReader;
    static FWriter fileWriter;


    public void main() {
        try {
            long start = System.currentTimeMillis();
            fileReader = new FileDataReader();
            fileWriter = (FWriter) Class.forName("com.example.testdemo.fileIO.FWriter")
                    .getDeclaredConstructor().newInstance();
            FileRecord record;
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\insertTest.csv"));

            int cnt = 0;
            while ((record = fileReader.readNext()) != null) {
                if (++cnt <= 499900) {
                    fileWriter.write(record);
                } else {
                    writer.write(record.toString());
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
            fileWriter.onFinish();
            long end = System.currentTimeMillis();

            long total_time = (end - start);
            System.out.printf("Import finished in %dms", total_time);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}