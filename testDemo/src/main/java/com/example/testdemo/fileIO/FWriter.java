package com.example.testdemo.fileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FWriter implements DBWriter {
    static final int MALE = 1;
    static final int FEMALE = 0;
    static final int RETRIEVAL = 1;
    static final int DELIVERY = 4;
    static final int EXPORT = 2;
    static final int IMPORT = 3;
    static final int BATCH_SIZE = 50000;

    MyHashMap companyMap = new MyHashMap();
    MyHashMap cityMap = new MyHashMap();
    MyHashMap containerMap = new MyHashMap(60000, 1);
    MyHashMap shipMap = new MyHashMap();
    MyHashMap courierMap = new MyHashMap(15000, 1);
    MyHashMap recordMap = new MyHashMap(500000, 1);

    static String companyData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\company.csv";
    static String containerData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\container.csv";
    static String shipData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\ship.csv";
    static String cityData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\city.csv";
    static String courierData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\courier.csv";
    static String recordData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\record.csv";
    static String transitData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\transit.csv";
    static String handleData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\handle.csv";

    @Override
    public void write(FileRecord record) throws Exception {
        recordMap.put(record.itemName, new FileRecordWrapper(record));
        putMap(companyMap, record.company, record);
        putMap(containerMap, record.containerCode, record);
        putMap(shipMap, record.shipName, record);
        putMap(cityMap, record.retrievalCity, record);
        putMap(cityMap, record.deliveryCity, record);
        putMap(cityMap, record.exportCity, record);
        putMap(cityMap, record.importCity, record);
        putCourierMap(record.retrievalCourier, record.retrievalCourierPhone, record);
        putCourierMap(record.deliveryCourier, record.deliveryCourierPhone, record);
    }

    private void putMap(HashMap map, String key, FileRecord record) {
        if (key != null) {
            if (!map.containsKey(key)) {
                map.put(key, new FileRecordWrapper(record));
            }
        }
    }

    private void putCourierMap(String courier, String phone, FileRecord record) {
        if (courier != null) {
            String key = phone + "," + courier;
            if (!courierMap.containsKey(key)) {
                courierMap.put(key, new FileRecordWrapper(record));
            }
        }
    }


    @Override
    public void onFinish() throws Exception {
        writeCompany();
        writeCity();
        writeShip();
        writeContainer();
        writeCourier();
        writeRecord();
        writeHandle();
        writeTransit();
    }

    private void writeCompany() throws Exception {
        writeFile("Company", companyData, companyMap, (String key, FileRecord record) -> {
            return key;
        });
    }

    private void writeCity() throws Exception {
        writeFile("City", cityData, cityMap, (String key, FileRecord record) -> {
            return key;
        });
    }

    private void writeShip() throws Exception {
        writeFile("Ship", shipData, shipMap, (String key, FileRecord record) -> {
            return String.format("%s,%s", key, record.company);
        });
    }

    private void writeContainer() throws Exception {
        writeFile("Container", containerData, containerMap, (String key, FileRecord record) -> {
            return String.format("%s,%s", key, record.containerType);
        });
    }

    private void writeCourier() throws Exception {
        writeFile("Courier", courierData, courierMap, (String key, FileRecord record) -> {
            String[] s = key.split(",");
            String phone = s[0];
            String name = s[1];
            String gender;
            String age;
            String city;
            if (phone.equals(record.retrievalCourierPhone)) {
                gender = record.retrievalCourierGender;
                age = record.retrievalCourierAge;
                city = record.retrievalCity;
            } else {
                gender = record.deliveryCourierGender;
                age = record.deliveryCourierAge;
                city = record.deliveryCity;
            }
            return String.format("%s,%d,%s,%d,%s,%s",
                    name, gender.equals("男") ? MALE : FEMALE, phone, (int) (Float.parseFloat(age)), record.company, city);
        });
    }

    private void writeRecord() throws Exception {
        writeFile("Record", recordData, recordMap, (String key, FileRecord record) -> {
            return String.format("%s,%s,%s,%s,%s,%s,%s",
                    record.itemName, record.itemType, record.itemPrice, record.logTime, record.company, record.shipName, record.containerCode);
        });
    }

    private void writeHandle() throws Exception {
        writeFile("Handle retrieval", handleData, recordMap, (String key, FileRecord record) -> {
            return String.format("%d,%s,%s,%s,%s",
                    RETRIEVAL, record.retrievalStartTime, record.itemName, record.retrievalCourierPhone, record.retrievalCourier);
        }, false);

        writeFile("Handle delivery", handleData, recordMap, (String key, FileRecord record) -> {
            return String.format("%d,%s,%s,%s,%s",
                    DELIVERY, record.deliveryFinishedTime, record.itemName, record.deliveryCourierPhone, record.deliveryCourier);
        }, false);
    }

    private void writeTransit() throws Exception {
        writeFile("Retrieval", transitData, recordMap, (String key, FileRecord record) -> {
            return String.format("%d,%s,%s", RETRIEVAL, record.itemName, record.retrievalCity);
        }, false);

        writeFile("Export", transitData, recordMap, (String key, FileRecord record) -> {
            return String.format("%d,%s,%s,%s,%s", EXPORT, record.itemName, record.exportCity, record.exportTime, record.exportTax);
        }, false);

        writeFile("Import", transitData, recordMap, (String key, FileRecord record) -> {
            return String.format("%d,%s,%s,%s,%s", IMPORT, record.itemName, record.importCity, record.importTime, record.importTax);
        }, false);

        writeFile("Delivery", transitData, recordMap, (String key, FileRecord record) -> {
            return String.format("%d,%s,%s", DELIVERY, record.itemName, record.deliveryCity);
        }, false);
    }

    private void writeFile(String table, String path, HashMap<String, FileRecordWrapper> map,
                           CallBack callback) throws Exception {
        writeFile(table, path, map, callback, true);
    }

    private void writeFile(String table, String path, HashMap<String, FileRecordWrapper> map,
                           CallBack callback, boolean return_keys) throws Exception {
        ArrayList<String> st = new ArrayList<>();
        Iterator<Map.Entry<String, FileRecordWrapper>> iter = map.entrySet().iterator();
        ArrayList<FileRecordWrapper> arr = new ArrayList(BATCH_SIZE);
        int num = 0;
        int total = 0;

        while (iter.hasNext()) {
            Map.Entry<String, FileRecordWrapper> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            FileRecordWrapper rw = entry.getValue();
            st.add(callback.format(key, rw.record));
            arr.add(rw);

            total++;
            num++;
            if (num >= BATCH_SIZE) {
                writeFlush(path, st, return_keys);
                arr.clear();
                st.clear();
                num = 0;
                System.out.println(table + ": " + total);
            }
        }

        if (st.size() > 0) {
            writeFlush(path, st, return_keys);
            System.out.println(table + ": " + total);
        }
    }

    private void writeFlush(String path, ArrayList<String> st, boolean return_keys) throws Exception {
        File file = new File(path);
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file, true));
        for (String s : st) {
            writer.write(s);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    private java.sql.Date toDate(String time) throws Exception {
        if (time == null) {
            return null;
        }
        return java.sql.Date.valueOf(time);
    }

    private java.sql.Timestamp toTimestamp(String time) throws Exception {
        if (time == null) {
            return null;
        }

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date t = ft.parse(time);
        return new java.sql.Timestamp(t.getTime());
    }

    @Override
    public void close() throws Exception {

    }
}


interface CallBack {
    String format(String key, FileRecord record) throws Exception;
}