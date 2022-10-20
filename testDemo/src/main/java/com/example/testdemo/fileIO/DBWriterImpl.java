package com.example.testdemo.fileIO;

import com.alibaba.druid.util.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class DBWriterImpl implements DBWriter {
    static final int MALE = 1;
    static final int FEMALE = 0;
    static final int RETRIEVAL = 1;
    static final int DELIVERY = 4;
    static final int EXPORT = 2;
    static final int IMPORT = 3;
    static final int BATCH_SIZE = 9961;

    String dbURL = "jdbc:postgresql://127.0.0.1:5432/sustc?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatement=true";
    String dbUser = "postgres";
    String dbPassword = "20030118";
    Connection connection;

    MyHashMap companyMap = new MyHashMap();
    MyHashMap cityMap = new MyHashMap();
    MyHashMap containerMap = new MyHashMap(60000, 1);
    MyHashMap shipMap = new MyHashMap();
    MyHashMap courierMap = new MyHashMap(15000, 1);
    MyHashMap recordMap = new MyHashMap(500000, 1);

    public DBWriterImpl() throws Exception {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        connection.setAutoCommit(false);
    }

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
        String sql = "insert into company(name) values (?)";
        writeDB("Company", sql, companyMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setString(1, key);
        });
    }

    private void writeCity() throws Exception {
        String sql = "insert into city(name) values (?)";
        writeDB("City", sql, cityMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setString(1, key);
        });
    }

    private void writeShip() throws Exception {
        String sql = "insert into ship(name,company_id) values (?,?)";
        writeDB("Ship", sql, shipMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setString(1, key);
            st.setObject(2, companyMap.getID(record.company));
        });
    }

    private void writeContainer() throws Exception {
        String sql = "insert into container(code,type) values (?,?)";
        writeDB("Container", sql, containerMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setString(1, key);
            st.setString(2, record.containerType);
        });
    }

    private void writeCourier() throws Exception {
        String sql = "insert into courier(name,gender,phone_number,age,company_id,city_id) values (?,?,?,?,?,?)";
        writeDB("Courier", sql, courierMap, (PreparedStatement st, String key, FileRecord record) -> {
            String[] s = key.split(",");
            String phone = s[0];
            String name = s[1];
            String gender;
            String age;
            Integer city;
            if (phone.equals(record.retrievalCourierPhone)) {
                gender = record.retrievalCourierGender;
                age = record.retrievalCourierAge;
                city = cityMap.getID(record.retrievalCity);
            } else {
                gender = record.deliveryCourierGender;
                age = record.deliveryCourierAge;
                city = cityMap.getID(record.deliveryCity);
            }
            st.setString(1, name);
            st.setInt(2, gender.equals("男") ? MALE : FEMALE);
            st.setString(3, phone);
            st.setInt(4, Math.round(Float.parseFloat(age)));
            st.setObject(5, companyMap.getID(record.company));
            st.setInt(6, city);
        });
    }

    private void writeRecord() throws Exception {
        String sql = "insert into record(item_name,item_class,item_price,"
                + "log_time,company_id,ship_id,container_id,state) values (?,?,?,?,?,?,?,?)";
        writeDB("Record", sql, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            int state;
            if (!StringUtils.isEmpty(record.deliveryFinishedTime)) {
                state = 4;
            } else if (!StringUtils.isEmpty(record.importTime)) {
                state = 3;
            } else if (!StringUtils.isEmpty(record.exportTime)) {
                state = 2;
            } else {
                state = 1;
            }
            st.setString(1, record.itemName);
            st.setString(2, record.itemType);
            if (record.itemPrice.length() > 0) {
                st.setInt(3, (int) Double.parseDouble(record.itemPrice));
            } else {
                st.setNull(3, Types.BIGINT);
            }
            st.setTimestamp(4, toTimestamp(record.logTime));
            st.setObject(5, companyMap.getID(record.company));
            st.setObject(6, shipMap.getID(record.shipName));
            st.setObject(7, containerMap.getID(record.containerCode));
            st.setInt(8, state);
        });
    }

    private void writeHandle() throws Exception {
        String sql = "insert into handle(type,time,record_id,courier_id) values(?,?,?,?)";
        writeDB("Handle retrieval", sql, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setInt(1, RETRIEVAL);
            st.setDate(2, toDate(record.retrievalStartTime));
            st.setObject(3, recordMap.getID(record.itemName));
            st.setObject(4, courierMap.getID(record.retrievalCourierPhone, record.retrievalCourier));
        }, false);

        writeDB("Handle delivery", sql, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setInt(1, DELIVERY);
            st.setDate(2, toDate(record.deliveryFinishedTime));
            st.setObject(3, recordMap.getID(record.itemName));
            st.setObject(4, courierMap.getID(record.deliveryCourierPhone, record.deliveryCourier));
        }, false);
    }

    private void writeTransit() throws Exception {
        String sql1 = "insert into transit(type,record_id,city_id) values(?,?,?)";
        String sql2 = "insert into transit(type,time,tax,record_id,city_id) values(?,?,?,?,?)";

        writeDB("Retrieval", sql2, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setInt(1, RETRIEVAL);
            st.setDate(2, toDate(record.retrievalStartTime));
            st.setObject(3, null);
            st.setObject(4, recordMap.getID(record.itemName));
            st.setObject(5, cityMap.getID(record.retrievalCity));
        }, false);

        writeDB("Export", sql2, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setInt(1, EXPORT);
            st.setDate(2, toDate(record.exportTime));
            st.setDouble(3, Double.parseDouble(record.exportTax));
            st.setObject(4, recordMap.getID(record.itemName));
            st.setObject(5, cityMap.getID(record.exportCity));
        }, false);

        writeDB("Import", sql2, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setInt(1, IMPORT);
            st.setDate(2, toDate(record.importTime));
            st.setDouble(3, Double.parseDouble(record.importTax));
            st.setObject(4, recordMap.getID(record.itemName));
            st.setObject(5, cityMap.getID(record.importCity));
        }, false);

        writeDB("Delivery", sql2, recordMap, (PreparedStatement st, String key, FileRecord record) -> {
            st.setInt(1, DELIVERY);
            st.setDate(2, toDate(record.deliveryFinishedTime));
            st.setObject(3, null);
            st.setObject(4, recordMap.getID(record.itemName));
            st.setObject(5, cityMap.getID(record.deliveryCity));
        }, false);
    }

    private void writeDB(String table, String sql, HashMap<String, FileRecordWrapper> map,
                         ParameterSetCallback callback) throws Exception {
        writeDB(table, sql, map, callback, true);
    }

    private void writeDB(String table, String sql, HashMap<String, FileRecordWrapper> map,
                         ParameterSetCallback callback, boolean return_keys) throws Exception {
        PreparedStatement st;
        if (return_keys) {
            st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            st = connection.prepareStatement(sql);
        }

        Iterator<Map.Entry<String, FileRecordWrapper>> iter = map.entrySet().iterator();
        ArrayList<FileRecordWrapper> arr = new ArrayList(BATCH_SIZE);
        int num = 0;
        int total = 0;

        while (iter.hasNext()) {
            Map.Entry<String, FileRecordWrapper> entry = iter.next();
            String key = entry.getKey();
            FileRecordWrapper rw = entry.getValue();
            callback.setParameter(st, key, rw.record);
            st.addBatch();
            arr.add(rw);

            total++;
            num++;
            if (num >= BATCH_SIZE) {
                executeSQL(st, arr, return_keys);
                arr.clear();
                num = 0;
                System.out.println(table + ": " + total);
            }
        }

        if (arr.size() > 0) {
            executeSQL(st, arr, return_keys);
            System.out.println(table + ": " + total);
        }
    }

    private void executeSQL(PreparedStatement st, ArrayList<FileRecordWrapper> arr, boolean return_keys) throws Exception {
        st.executeBatch();
        connection.commit();

        if (return_keys) {
            ResultSet rs = st.getGeneratedKeys();
            int i = 0;
            while (rs.next()) {
                FileRecordWrapper rw = arr.get(i++);
                rw.id = rs.getInt("id");
            }
        }

        st.clearBatch();
        st.clearParameters();
    }

    private java.sql.Date toDate(String time) throws Exception {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = sdf.parse(time);
        return new java.sql.Date(date.getTime());
    }

    private Timestamp toTimestamp(String time) throws Exception {
        if (time == null) {
            return null;
        }
        return new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(time).getTime());
    }


    @Override
    public void close() throws Exception {
        connection.close();
    }
}

class FileRecordWrapper {
    FileRecord record;
    Integer id = null;

    public FileRecordWrapper(FileRecord r) {
        record = r;
    }
}

class MyHashMap extends HashMap<String, FileRecordWrapper> {
    public MyHashMap() {
        super();
    }

    public MyHashMap(int capbility, int loadfactor) {
        super(capbility, loadfactor);
    }

    public Integer getID(String key) {
        FileRecordWrapper rw = get(key);
        if (rw != null) {
            return rw.id;
        }
        return null;
    }

    public Integer getID(String phone, String name) {
        if (phone == null) {
            return null;
        }
        String key = phone + "," + name;
        return getID(key);
    }
}

interface ParameterSetCallback {
    void setParameter(PreparedStatement st, String key, FileRecord record) throws Exception;
}