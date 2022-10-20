package com.example.testdemo.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;

public class jdbc {
    static String[] table = {"city", "city", "city", "city", "company", "ship", "container", "courier", "courier", "record",
            "transit", "transit", "transit", "transit", "handle", "handle"};
    static int[][] column = {
            {3}, {10}, {15}, {18},
            {24},
            {23, 24},
            {21, 22},
            {5, 6, 7, 8, 24, 3},
            {11, 12, 13, 14, 24, 9},
            {0, 1, 2, 21, 23, 24, 25},
            {0, 3, 1}, {0, 15, 2, 16, 17}, {0, 18, 3, 19, 20}, {0, 10, 4},
            {0, 5, 4, 1}, {0, 11, 9, 4}
    };
    static int[][] key = {
            {3}, {10}, {15}, {18},
            {24},
            {23},
            {21},
            {5, 7},
            {11, 13},
            {0},
            {0, 3, 1}, {0, 15, 2}, {0, 18, 3}, {0, 10, 4},
            {0, 5, 4, 1}, {0, 11, 9, 4}
    };
    static String[][] attributes = {
            {"name"}, {"name"}, {"name"}, {"name"},
            {"name"},
            {"name", "company_id"},
            {"code", "type"},
            {"name", "gender", "phone_number", "age", "company_id", "city_id"},
            {"name", "gender", "phone_number", "age", "company_id", "city_id"},
            {"item_name", "item_class", "item_price", "company_id",
                    "container_id", "ship_id", "log_time"},
            {"record_id", "city_id", "type"},
            {"record_id", "city_id", "type", "tax", "time"},
            {"record_id", "city_id", "type", "tax", "time"},
            {"record_id", "city_id", "type"},
            {"record_id", "courier_id", "time", "type"},
            {"record_id", "courier_id", "time", "type"}
    };
    static HashMap<String, Integer> companyMap = new HashMap<>();
    static HashMap<String, Integer> cityMap = new HashMap<>();
    static HashMap<String, Integer> containerMap = new HashMap<>();
    static HashMap<String, Integer> shipMap = new HashMap<>();
    static HashMap<String, Integer> courierMap = new HashMap<>();
    static HashMap<String, Integer> recordMap = new HashMap<>();
    static HashMap<String, Integer> handleMap = new HashMap<>();
    static HashMap<String, Integer> transitMap = new HashMap<>();
    static HashMap<Integer, String> mapCompany = new HashMap<>();
    static HashMap<Integer, String> mapCity = new HashMap<>();
    static HashMap<Integer, String> mapContainer = new HashMap<>();
    static HashMap<Integer, String> mapShip = new HashMap<>();
    static HashMap<Integer, String> mapCourier = new HashMap<>();
    static HashMap<Integer, String> mapRecord = new HashMap<>();
    static HashMap<Integer, String> mapHandle = new HashMap<>();
    static HashMap<Integer, String> mapTransit = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://127.0.0.1:5432/sustc?useUnicode=true&characterEncoding=utf8";
        String username = "postgres";
        String password = "20030118";
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        String[] sql = new String[16];
        for (int i = 0; i < 16; i++) {
            sql[i] = "Insert into " + table[i] + "(";
            for (int j = 0; j < attributes[i].length; j++) {
                sql[i] += attributes[i][j];
                if (j < attributes[i].length - 1)
                    sql[i] += ",";
            }
            sql[i] += ") values(";
            for (int j = 0; j < attributes[i].length; j++) {
                sql[i] += "?";
                if (j < attributes[i].length - 1)
                    sql[i] += ",";
            }
            sql[i] += ");";
            System.out.println(sql[i]);
        }
        PreparedStatement[] pst = new PreparedStatement[16];
        for (int i = 0; i < 16; i++) {
            pst[i] = conn.prepareStatement(sql[i], Statement.RETURN_GENERATED_KEYS);
        }
        long iniTime = System.currentTimeMillis();
        File file = new File("C:\\Users\\25874\\Downloads\\data.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        bufferedReader.readLine();
        String currentLine;
        int total = 0;
        while ((currentLine = bufferedReader.readLine()) != null) {
            if (++total % 10000 == 0) {
                System.out.println("Imported " + total + " records in " + (System.currentTimeMillis() - iniTime) + " ms");
            }
            String[] tmpRecord = currentLine.split(",");
            /*
            Attributes: 0 Item Name, 1 Item Type, 2 Item Price
            3 Retrieval City, 4 Retrieval Start Time, 5 Retrieval Courier
            6 Retrieval Courier Gender, 7 Retrieval Courier Phone Number
            8 Retrieval Courier Age
            9 Delivery End Time, 10 Delivery City, 11 Delivery Courier
            12 Delivery Courier Gender, 13 Delivery Courier Phone Number
            14 Delivery Courier Age
            15 Item Export City, 16 Item Export Tax, 17 Item Export Time
            18 Item Import City, 19 Item Import Tax, 20 Item Import Time
            21 Container Code, 22 Container Type, 23 Ship Name, 24 Company Name
            25 Log Time
             */

            for (int i = 0; i < 16; i++) {
                int id = keyTest(i, tmpRecord);
                if (id == 0) {
                    for (int j = 0; j < column[i].length; j++) {
//                        System.out.println(tmpRecord[column[i][j]]);
                        int flag = 1;
                        if (attributes[i][j].endsWith("price") ||
                                attributes[i][j].endsWith("age"))
                            flag = 0;
                        if (attributes[i][j].equals("tax"))
                            flag = 2;
                        if (attributes[i][j].endsWith("gender"))
                            pst[i].setInt(j + 1, formatGender(tmpRecord[column[i][j]]));
                        else if (attributes[i][j].equals("type") && i != 6)
                            pst[i].setInt(j + 1, column[i][j]);
                        else if (attributes[i][j].endsWith("id"))
                            pst[i].setInt(j + 1, formatID(attributes[i][j], tmpRecord, i));
                        else if (attributes[i][j].equals("log_time"))
                            pst[i].setTimestamp(j + 1, new Timestamp(System.currentTimeMillis()));
                        else if (attributes[i][j].endsWith("time") && tmpRecord[column[i][j]].length() == 0)
                            pst[i].setNull(j + 1, Types.TIMESTAMP);
                        else if (attributes[i][j].endsWith("time"))
                            pst[i].setDate(j + 1, new Date(System.currentTimeMillis()));
                        else if (flag == 1)
                            pst[i].setString(j + 1, tmpRecord[column[i][j]]);
                        else if (flag == 0)
                            pst[i].setInt(j + 1, String.format("%s", tmpRecord[column[i][j]]).length() > 0 ?
                                    (int) Double.parseDouble(tmpRecord[column[i][j]]) : 0);
                        else
                            pst[i].setBigDecimal(j + 1, new BigDecimal(tmpRecord[column[i][j]]));
                    }
                    pst[i].executeUpdate();
                    ResultSet rs = pst[i].getGeneratedKeys();
                    rs.next();
                    insertKey(i, tmpRecord, rs.getInt("id"));
                    rs.close();
                }
            }
        }
        stmt.close();
        conn.close();
        long endTime = System.currentTimeMillis();
        System.out.printf("Import Finished with Runtime " + (endTime - iniTime) + " ms");
    }

    static int formatGender(String gender) {
        return gender.equals("男") ? 1 : 0;
    }

    static int formatID(String id, String[] record, int line) {
        if (id.equals("company_id"))
            return keyTest(4, record);
        if (id.equals("ship_id"))
            return keyTest(5, record);
        if (id.equals("container_id"))
            return keyTest(6, record);
        if (id.equals("record_id"))
            return keyTest(9, record);
        if (id.equals("courier_id"))
            if (line == 14)
                return keyTest(7, record);
            else
                return keyTest(8, record);
        if (id.equals("city_id"))
            if (line == 10)
                return keyTest(0, record);
            else if (line == 11)
                return keyTest(1, record);
            else if (line == 12)
                return keyTest(2, record);
            else
                return keyTest(3, record);
        return 0;
    }

    static int keyTest(int line, String[] record) {
        StringBuilder testKey = new StringBuilder();
        for (int i = 0; i < key[line].length; i++) {
            testKey.append(record[key[line][i]].trim());
            if (i < key[line].length - 1)
                testKey.append(",");
        }
        if (table[line].equals("city") &&
                cityMap.containsKey(testKey.toString()))
            return cityMap.get(testKey.toString());
        if (table[line].equals("container") &&
                containerMap.containsKey(testKey.toString()))
            return containerMap.get(testKey.toString());
        if (table[line].equals("company") &&
                companyMap.containsKey(testKey.toString()))
            return companyMap.get(testKey.toString());
        if (table[line].equals("courier") &&
                courierMap.containsKey(testKey.toString()))
            return courierMap.get(testKey.toString());
        if (table[line].equals("ship") &&
                shipMap.containsKey(testKey.toString()))
            return shipMap.get(testKey.toString());
        if (table[line].equals("record") &&
                recordMap.containsKey(testKey.toString()))
            return recordMap.get(testKey.toString());
        if (table[line].equals("handle") &&
                handleMap.containsKey(testKey.toString()))
            return handleMap.get(testKey.toString());
        if (table[line].equals("transit") &&
                transitMap.containsKey(testKey.toString()))
            return transitMap.get(testKey.toString());
        return 0;
    }

    static int insertKey(int line, String[] record, int k) {
        StringBuilder testKey = new StringBuilder();
        for (int i = 0; i < key[line].length; i++) {
            testKey.append(record[key[line][i]].trim());
            if (i < key[line].length - 1)
                testKey.append(",");
        }
        String tmpKey = testKey.toString();
        if (table[line].equals("city")) {
            mapCity.put(k, tmpKey);
            cityMap.put(tmpKey, k);
        } else if (table[line].equals("company")) {
            mapCompany.put(k, tmpKey);
            companyMap.put(tmpKey, k);
        } else if (table[line].equals("courier")) {
            mapCourier.put(k, tmpKey);
            courierMap.put(tmpKey, k);
        } else if (table[line].equals("container")) {
            mapContainer.put(k, tmpKey);
            containerMap.put(tmpKey, k);
        } else if (table[line].equals("ship")) {
            mapShip.put(k, tmpKey);
            shipMap.put(tmpKey, k);
        } else if (table[line].equals("record")) {
            mapRecord.put(k, tmpKey);
            recordMap.put(tmpKey, k);
        } else if (table[line].equals("handle")) {
            mapHandle.put(k, tmpKey);
            handleMap.put(tmpKey, k);
        } else if (table[line].equals("transit")) {
            mapTransit.put(k, tmpKey);
            transitMap.put(tmpKey, k);
        }
        return 0;
    }
}