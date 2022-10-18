package com.example.testdemo.fileIO;

public class FileRecord {
    public String itemName;
    public String itemType;
    public String itemPrice;
    public String retrievalCity;
    public String retrievalStartTime;
    public String retrievalCourier;
    public String retrievalCourierGender;
    public String retrievalCourierPhone;
    public String retrievalCourierAge;
    public String deliveryFinishedTime;
    public String deliveryCity;
    public String deliveryCourier;
    public String deliveryCourierGender;
    public String deliveryCourierPhone;
    public String deliveryCourierAge;
    public String exportCity;
    public String exportTax;
    public String exportTime;
    public String importCity;
    public String importTax;
    public String importTime;
    public String containerCode;
    public String containerType;
    public String shipName;
    public String company;
    public String logTime;
    
    public FileRecord(String[] words) throws Exception {
        itemName = words[0];
        itemType = words[1];
        itemPrice = words[2];
        retrievalCity = words[3];
        retrievalStartTime = words[4];
        retrievalCourier = words[5];
        retrievalCourierGender = words[6];
        retrievalCourierPhone = words[7];
        retrievalCourierAge = words[8];
        deliveryFinishedTime = words[9];
        deliveryCity = words[10];
        deliveryCourier = words[11];
        deliveryCourierGender = words[12];
        deliveryCourierPhone = words[13];
        deliveryCourierAge = words[14];
        exportCity = words[15];
        exportTax = words[16];
        exportTime = words[17];
        importCity = words[18];
        importTax = words[19];
        importTime = words[20];
        containerCode = words[21];
        containerType = words[22];
        shipName = words[23];
        company = words[24];
        logTime = words[25];
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(itemName);builder.append(",");builder.append(itemType);builder.append(",");
        builder.append(itemPrice);builder.append(",");builder.append(retrievalCity);builder.append(",");
        builder.append(retrievalStartTime);builder.append(",");builder.append(retrievalCourier);builder.append(",");
        builder.append(retrievalCourierGender);builder.append(",");builder.append(retrievalCourierPhone);builder.append(",");
        builder.append(retrievalCourierAge);builder.append(",");builder.append(deliveryFinishedTime);builder.append(",");
        builder.append(deliveryCity);builder.append(",");builder.append(deliveryCourier);builder.append(",");
        builder.append(deliveryCourierGender);builder.append(",");builder.append(deliveryCourierPhone);builder.append(",");
        builder.append(deliveryCourierAge);builder.append(",");builder.append(exportCity);builder.append(",");
        builder.append(exportTax);builder.append(",");builder.append(exportTime);builder.append(",");
        builder.append(importCity);builder.append(",");builder.append(importTax);builder.append(",");
        builder.append(importTime);builder.append(",");builder.append(containerCode);builder.append(",");
        builder.append(containerType);builder.append(",");builder.append(shipName);builder.append(",");
        builder.append(company);builder.append(",");builder.append(logTime);
        return builder.toString();
    }
}