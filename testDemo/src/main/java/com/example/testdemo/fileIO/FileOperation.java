package com.example.testdemo.fileIO;

import java.io.*;
import java.util.ArrayList;

public class FileOperation {

    static String companyData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\company.csv";
    static String containerData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\container.csv";
    static String shipData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\ship.csv";
    static String cityData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\city.csv";
    static String courierData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\courier.csv";
    static String recordData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\record.csv";
    static String transitData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\transit.csv";
    static String handleData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\handle.csv";

    public static int deleteRecord(ArrayList<String> item_names) throws Exception {
        File file = new File(recordData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        ArrayList<String> buffer = new ArrayList<>();
        ArrayList<String> deleted = new ArrayList<>();
        int tot = 0;
        while ((tmp = reader.readLine()) != null) {
            String[] current = tmp.split(",");
            if (!item_names.contains(current[0])) {
                buffer.add(tmp);
            } else {
                deleted.add(current[0]);
            }
            if ((++tot) % 100000 == 0) {
                System.out.printf("Deleted record: %d\n", tot);
            }
        }
        reader.close();

        deleteHandle(deleted);
        deleteTransit(deleted);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for (String i : buffer) {
            writer.write(i);
            writer.newLine();
        }
        writer.flush();
        writer.close();

        return 0;
    }


    public static int deleteRecordClass(ArrayList<String> item_classes) throws Exception {
        File file = new File(recordData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        ArrayList<String> buffer = new ArrayList<>();
        ArrayList<String> deleted = new ArrayList<>();
        int tot = 0;
        while ((tmp = reader.readLine()) != null) {
            String[] current = tmp.split(",");
            if (!item_classes.contains(current[1])) {
                buffer.add(tmp);
            } else {
                deleted.add(current[0]);
            }
            if ((++tot) % 100000 == 0) {
                System.out.printf("Deleted record: %d\n", tot);
            }
        }
        reader.close();

        deleteHandle(deleted);
        deleteTransit(deleted);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for (String i : buffer) {
            writer.write(i);
            writer.newLine();
        }
        writer.flush();
        writer.close();

        return 0;
    }

    public static int deleteTransit(ArrayList<String> items) throws Exception {
        File file = new File(transitData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        ArrayList<String> buffer = new ArrayList<>();
        int tot = 0;
        while ((tmp = reader.readLine()) != null) {
            String[] current = tmp.split(",");
            if (!items.contains(current[1]))
                buffer.add(tmp);
            if ((++tot) % 100000 == 0) {
                System.out.printf("Deleted transit: %d\n", tot);
            }
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for (String i : buffer) {
            writer.write(i);
            writer.newLine();
        }
        writer.flush();
        writer.close();

        return 0;
    }

    public static int deleteHandle(ArrayList<String> items) throws Exception {
        File file = new File(handleData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        ArrayList<String> buffer = new ArrayList<>();
        int tot = 0;
        while ((tmp = reader.readLine()) != null) {
            String[] current = tmp.split(",");
            if (!items.contains(current[2]))
                buffer.add(tmp);
            if ((++tot) % 100000 == 0) {
                System.out.printf("Deleted handle: %d\n", tot);
            }
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for (String i : buffer) {
            writer.write(i);
            writer.newLine();
        }
        writer.flush();
        writer.close();

        return 0;
    }

    public static int insertRecord(FileRecord record) throws Exception {
        File file = new File(recordData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            if (tmp.split(",")[0].equals(record.itemName)) {
                return -1;
            }
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(String.format("%s,%s,%s,%s,%s,%s,%s",
                record.itemName, record.itemType, record.itemPrice, record.logTime, record.company, record.shipName, record.containerCode));
        writer.newLine();
        writer.close();
        insertCity(record.deliveryCity);
        insertCity(record.retrievalCity);
        insertCity(record.exportCity);
        insertCity(record.importCity);
        insertContainer(record.containerCode, record.containerType);
        insertCompany(record.company);
        insertShip(record.shipName, record.company);
        insertCourier(record.retrievalCourier, record.retrievalCourierGender, record.retrievalCourierPhone, record.retrievalCourierAge, record.company);
        insertCourier(record.deliveryCourier, record.deliveryCourierGender, record.deliveryCourierPhone, record.deliveryCourierAge, record.company);
        insertTransit(FWriter.RETRIEVAL, record.itemName, record.retrievalCity, null, null);
        insertTransit(FWriter.EXPORT, record.itemName, record.exportCity, record.exportTime, record.exportTax);
        insertTransit(FWriter.IMPORT, record.itemName, record.importCity, record.importTime, record.importTax);
        insertTransit(FWriter.DELIVERY, record.itemName, record.deliveryCity, null, null);
        insertHandle(FWriter.RETRIEVAL, record.retrievalStartTime, record.itemName, record.retrievalCourierPhone, record.retrievalCourier);
        insertHandle(FWriter.DELIVERY, record.deliveryFinishedTime, record.itemName, record.deliveryCourierPhone, record.deliveryCourier);
        return 0;
    }

    private static int insertHandle(int type, String time, String itemName, String phone, String courier) throws Exception {
        File file = new File(handleData);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(String.format("%d,%s,%s,%s,%s", type, time, itemName, phone, courier));
        writer.newLine();
        writer.close();
        return 0;
    }

    private static int insertTransit(int type, String itemName, String city, String time, String tax) throws Exception {
        if (city == null || city.equals("null")) {
            return -1;
        }
        File file = new File(transitData);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        if (type == 2 || type == 3) {
            writer.write(String.format("%d,%s,%s,%s,%s", type, itemName, city, time, tax));
        } else
            writer.write(String.format("%d,%s,%s", type, itemName, city));
        writer.newLine();
        writer.close();
        return 0;
    }

    private static int insertCourier(String courier, String gender, String phone, String age, String company) throws Exception {
        if (courier == null || courier.equals("null"))
            return -1;
        File file = new File(courierData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            if (tmp.split(",")[0].equals(courier) && tmp.split(",")[2].equals(phone)) {
                return -1;
            }
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(String.format("%s,%d,%s,%d,%s",
                courier, gender.equals("男") ? FWriter.MALE : FWriter.FEMALE, phone, (int) (Float.parseFloat(age)), company));
        writer.newLine();
        writer.close();
        return 0;
    }

    private static int insertShip(String name, String company) throws Exception {
        if (name == null || name.equals("null"))
            return -1;
        File file = new File(shipData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            if (tmp.split(",")[0].equals(name)) {
                return -1;
            }
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(String.format("%s,%s", name, company));
        writer.newLine();
        writer.close();
        return 0;
    }

    private static int insertCompany(String name) throws Exception {
        if (name == null || name.equals("null"))
            return -1;
        File file = new File(companyData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            if (tmp.equals(name)) {
                return -1;
            }
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(name);
        writer.newLine();
        writer.close();
        return 0;
    }

    private static int insertContainer(String code, String type) throws Exception {
        if (code == null || code.equals("null"))
            return -1;
        File file = new File(containerData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            if (tmp.split(",")[0].equals(code)) {
                return -1;
            }
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(String.format("%s,%s", code, type));
        writer.newLine();
        writer.close();
        return 0;
    }

    private static int insertCity(String city) throws Exception {
        if (city == null || city.equals("null"))
            return -1;
        File file = new File(cityData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            if (tmp.equals(city)) {
                return -1;
            }
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(city);
        writer.newLine();
        writer.close();
        return 0;
    }

    public static int updateEmpty() throws Exception {
        ArrayList<String> records = new ArrayList<>();
        String tmp = null;

        File file = new File(handleData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while ((tmp = reader.readLine()) != null) {
            String[] tmpRecord = tmp.split(",");
            StringBuilder k = new StringBuilder();
            for (int i = 0; i < tmpRecord.length; i++) {
                if (tmpRecord[i].length() == 0 || tmpRecord[i].equals("null")) {
                    k.append("empty");
                } else {
                    k.append(tmpRecord[i]);
                }
                if (i < tmpRecord.length - 1)
                    k.append(",");
            }
            records.add(k.toString());
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String i : records) {
            writer.write(i);
            writer.newLine();
            writer.flush();
        }
        writer.close();
        return 0;
    }

    public static int selectNotFinished() throws Exception {
        File out = new File("C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\NotFinished.csv");
        File file = new File(handleData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
        String tmp = null;
        while ((tmp = reader.readLine()) != null) {
            String[] s = tmp.split(",");
            if (s[0].equals("4") && (s[1].length() == 0 || s[1].equals("null"))) {
                writer.write(s[2]);
                writer.newLine();
                writer.flush();
            }
        }
        reader.close();
        writer.close();
        return 0;
    }

    public void queryTest(String itemName) throws IOException {
        File file = new File(recordData);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split(",");
            if (s[1].equals(itemName)) {
                System.out.println(line);
            }
        }
        reader.close();
    }
}
