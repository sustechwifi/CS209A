package com.example.testdemo.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class FileOperationTest {
    public void main() throws Exception {
        ArrayList<String> classes = new ArrayList<>();
        classes.add("coconut");
        ArrayList<FileRecord> records = new ArrayList<>();
        File file = new File("C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\insertTest.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            records.add(new FileRecord(tmp.split(",")));
        }
        long start = System.currentTimeMillis();
        for (FileRecord record : records) {
            FileOperation.insertRecord(record);
        }
        long node1 = System.currentTimeMillis();
        System.out.printf("Insert completed in %dms\n", node1 - start);
        FileOperation.selectNotFinished();
        long node2 = System.currentTimeMillis();
        System.out.printf("Select completed in %dms\n", node2 - node1);
        FileOperation.updateEmpty();
        long node3 = System.currentTimeMillis();
        System.out.printf("Update completed in %dms\n", node3 - node2);
        FileOperation.deleteRecordClass(classes);
        long end = System.currentTimeMillis();
        System.out.printf("Delete completed in %dms\n", end - node3);
    }
}
