package com.example.testdemo.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class FileDataReader {
    String dataFile = "C:\\Users\\25874\\Downloads\\data.csv";
    int lineNo = 0;
    BufferedReader reader;

    public FileDataReader() throws Exception {
        File file = new File(dataFile);
        reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), Charset.forName("GBK")));
        reader.readLine(); //skip header
    }

    public FileRecord readNext() throws Exception {
        String line;
        if ((line = reader.readLine()) != null) {
            lineNo++;

            if (lineNo > 500000) {
                return null;
            }

            String[] words = line.split(",");
            for (int i = 0; i < words.length; i++) {
                String w = words[i].trim();
                if (w.length() > 0) {
                    words[i] = w;
                } else {
                    words[i] = null;
                }
            }

            return new FileRecord(words);
        }
        return null;
    }

    public void close() throws Exception {
        reader.close();
    }
}