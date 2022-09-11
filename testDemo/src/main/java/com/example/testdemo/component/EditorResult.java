package com.example.testdemo.component;

import java.util.HashMap;
import java.util.Map;

public class EditorResult {
    private int errno;

    private Map<String,String> data = new HashMap<>();

    private String message;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static EditorResult success(String url,String alt,String href){
        EditorResult result = new EditorResult();
        result.setErrno(0);
        result.data.put("url",url);
        result.data.put("alt",alt);
        result.data.put("href",href);
        return result;
    }

    public static EditorResult error(String message) {
        EditorResult result = new EditorResult();
        result.setErrno(1);
        result.setMessage(message);
        return result;
    }
}
