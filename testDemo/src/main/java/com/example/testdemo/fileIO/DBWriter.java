package com.example.testdemo.fileIO;

public interface DBWriter {
    public void write(FileRecord record) throws Exception;  
    public void onFinish() throws Exception;   
    public void close() throws Exception; 
}