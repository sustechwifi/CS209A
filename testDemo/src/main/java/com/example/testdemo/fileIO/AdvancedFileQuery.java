package com.example.testdemo.fileIO;

import java.io.*;
import java.util.*;

public class AdvancedFileQuery {
    HashSet<String> citySet = new HashSet<>();
    HashSet<String> companySet = new HashSet<>();
    HashSet<String> shipSet = new HashSet<>();
    HashSet<String> containerSet = new HashSet<>();
    HashMap<String, Integer> courierMap = new HashMap<>();
    HashMap<String, Integer> recordMap = new HashMap<>();
    HashMap<String, Integer> transitMap = new HashMap<>();
    HashMap<String, Integer> handleMap = new HashMap<>();

    String companyData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/company.csv";
    String cityData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data\\city.csv";
    String containerData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/container.csv";
    String shipData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/ship.csv";
    String courierData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/courier.csv";
    String recordData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/record.csv";
    String transitData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/transit.csv";
    String handleData = "C:\\Users\\25874\\Desktop\\CS209A\\testDemo\\data/handle.csv";

    ArrayList<String> company = new ArrayList<>();
    ArrayList<String> city = new ArrayList<>();
    ArrayList<String> container = new ArrayList<>();
    ArrayList<String> ship = new ArrayList<>();
    ArrayList<String> courier = new ArrayList<>();
    ArrayList<String> record = new ArrayList<>();
    ArrayList<String> transit = new ArrayList<>();
    ArrayList<String> handle = new ArrayList<>();

    public void main() throws Exception {
        long preparestart = System.currentTimeMillis(), start, node1, node2, end;

        prepare();

        start = System.currentTimeMillis();

        advancedQuery1();

        node1 = System.currentTimeMillis();

        advancedQuery2();

        node2 = System.currentTimeMillis();

        advancedQuery3();

        end = System.currentTimeMillis();

        System.out.printf("Advanced Query is Prepared in %dms.\n", start - preparestart);
        System.out.printf("Advanced Query 1 Finished in %dms.\n", node1 - start);
        System.out.printf("Advanced Query 2 Finished in %dms.\n", node2 - node1);
        System.out.printf("Advanced Query 3 Finished in %dms.\n", end - node2);
    }

    public void advancedQuery1() {
        HashMap<String, Integer> yearServed = new HashMap<>();
        for (String s : transit) {
            String[] tmpTransit = s.split(",");
            if (tmpTransit[0].equals("3") && !tmpTransit[3].equals("null")) {
                int year = Integer.parseInt(tmpTransit[3].split("/")[0]);
                String tmpContainer = record.get(recordMap.get(tmpTransit[1])).split(",")[6];
                if (yearServed.containsKey(tmpContainer)) {
                    yearServed.replace(tmpContainer, Math.min(yearServed.get(tmpContainer), year));
                } else {
                    yearServed.put(tmpContainer, year);
                }
            }
        }

        HashMap<String, Integer> type2year = new HashMap<>();
        type2year.put("ISO Tank Container", 6);
        type2year.put("Dry Container", 6);
        type2year.put("Reefer Container", 6);
        type2year.put("Open Top Container", 6);
        type2year.put("Flat Rack Container", 7);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int cnt = 0;

        System.out.println("Code\tType\tYear Served");
        for (String s : container) {
            String[] tmpContainer = s.split(",");
            if (currentYear - yearServed.get(tmpContainer[0]) >= type2year.get(tmpContainer[1])) {
                System.out.printf("%s %s %d\n", tmpContainer[0], tmpContainer[1], currentYear - yearServed.get(tmpContainer[0]));
                //Can output to file
                cnt++;
            }
        }
        System.out.printf("%d records in total are selected.\n", cnt);
    }

    class tmpCount implements Comparable {
        String key;
        Integer cnt;

        public tmpCount(String key, Integer cnt) {
            this.key = key;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(Object o) {
            tmpCount tmp = (tmpCount) o;
            return -cnt.compareTo((tmp).cnt);
        }
    }

    public void advancedQuery2() {
        HashMap<String, Integer> handleCount = new HashMap<>();

        for (String s : handle) {
            String tmpCourier = s.split(",")[4] + "," + s.split(",")[3];
            if (courierMap.containsKey(tmpCourier)) {
                if (handleCount.containsKey(tmpCourier)) {
                    handleCount.replace(tmpCourier, handleCount.get(tmpCourier) + 1);
                } else {
                    handleCount.put(tmpCourier, 1);
                }
            }
        }

        HashMap<String, tmpCount> cityCount = new HashMap<>();
        PriorityQueue<tmpCount> courierCount = new PriorityQueue<>();
        for (String i : handleCount.keySet()) {
            courierCount.add(new tmpCount(i, handleCount.get(i)));
        }
        while (!courierCount.isEmpty()) {
            tmpCount tmp = courierCount.remove();
            String[] courierInfo = courier.get(courierMap.get(tmp.key)).split(",");
            String tmpKey = courierInfo[4] + " " + courierInfo[5];
            if (!cityCount.containsKey(tmpKey)) {
                cityCount.put(tmpKey, tmp);
            }
        }

        System.out.println("Company City Courier Phone Count");
        int cnt = cityCount.size();
        for (String s : cityCount.keySet()) {
            tmpCount tmp = cityCount.get(s);
            System.out.printf("%s %s %s %d\n", s, tmp.key.split(",")[0], tmp.key.split(",")[1], tmp.cnt);
            //Can output into file
        }
        System.out.printf("%d records in total are selected.\n", cnt);
    }

    public List<String> advancedQuery2(String city, String company) {
        HashMap<String, Integer> handleCount = new HashMap<>();

        for (String s : handle) {
            String tmpCourier = s.split(",")[4] + "," + s.split(",")[3];
            if (courierMap.containsKey(tmpCourier)) {
                if (handleCount.containsKey(tmpCourier)) {
                    handleCount.replace(tmpCourier, handleCount.get(tmpCourier) + 1);
                } else {
                    handleCount.put(tmpCourier, 1);
                }
            }
        }

        HashMap<String, tmpCount> cityCount = new HashMap<>();
        PriorityQueue<tmpCount> courierCount = new PriorityQueue<>();
        for (String i : handleCount.keySet()) {
            courierCount.add(new tmpCount(i, handleCount.get(i)));
        }
        while (!courierCount.isEmpty()) {
            tmpCount tmp = courierCount.remove();
            String[] courierInfo = courier.get(courierMap.get(tmp.key)).split(",");
            String tmpKey = courierInfo[4] + " " + courierInfo[5];
            if (!cityCount.containsKey(tmpKey)) {
                cityCount.put(tmpKey, tmp);
            }
        }

        System.out.println("Company City Courier Phone Count");
        List<String> res = new ArrayList<>();
        int cnt = cityCount.size();
        for (String s : cityCount.keySet()) {
            tmpCount tmp = cityCount.get(s);
            if (s.equals(company) && tmp.key.split(",")[0].equals(city)) {
                res.add(String.format("%s %s %s %d\n", s, tmp.key.split(",")[0],
                        tmp.key.split(",")[1], tmp.cnt));
            }
        }
        return res;
    }

    public void advancedQuery3() {
        HashSet<String> classes = new HashSet<>();
        HashMap<String, Double> exportTaxMap = new HashMap<>();

        for (String i : transit) {
            String[] tmpTransit = i.split(",");
            if (Integer.parseInt(tmpTransit[0]) == 2) {
                int recordID = recordMap.get(tmpTransit[1]);
                String[] tmpRecord = record.get(recordID).split(",");
                double exportTaxRate = Double.parseDouble(tmpTransit[4]) / Double.parseDouble(tmpRecord[2]);
                String tmpExt = tmpTransit[2] + "," + tmpRecord[1];
                if (!exportTaxMap.containsKey(tmpExt)) {
                    exportTaxMap.put(tmpExt, exportTaxRate);
                }
                if (!classes.contains(tmpRecord[1])) {
                    classes.add(tmpRecord[1]);
                }
            }
        }

        System.out.println("Class City ExportRate");
        for (String i : classes) {
            String tmpCity = null;
            double tmpRate = 2.0;
            for (String j : citySet) {
                String tmpExt = j + "," + i;
                if (exportTaxMap.containsKey(tmpExt) && exportTaxMap.get(tmpExt) < tmpRate) {
                    tmpCity = j;
                    tmpRate = exportTaxMap.get(tmpExt);
                }
            }
            System.out.printf("%s %s %.2f%%\n", i, tmpCity, tmpRate * 100);
            //Can output into file
        }
        System.out.printf("%d records in total are selected.\n", classes.size());
    }

    public List<String> advancedQuery3(List<String> itemClass) {
        HashSet<String> classes = new HashSet<>();
        HashMap<String, Double> exportTaxMap = new HashMap<>();

        for (String i : transit) {
            String[] tmpTransit = i.split(",");
            if (Integer.parseInt(tmpTransit[0]) == 2) {
                int recordID = recordMap.get(tmpTransit[1]);
                String[] tmpRecord = record.get(recordID).split(",");
                double exportTaxRate = Double.parseDouble(tmpTransit[4]) / Double.parseDouble(tmpRecord[2]);
                String tmpExt = tmpTransit[2] + "," + tmpRecord[1];
                if (!exportTaxMap.containsKey(tmpExt)) {
                    exportTaxMap.put(tmpExt, exportTaxRate);
                }
                if (!classes.contains(tmpRecord[1])) {
                    classes.add(tmpRecord[1]);
                }
            }
        }
        ArrayList<String> res = new ArrayList<>();
        System.out.println("Class City ExportRate");
        for (String i : classes) {
            String tmpCity = null;
            double tmpRate = 2.0;
            for (String j : citySet) {
                String tmpExt = j + "," + i;
                if (exportTaxMap.containsKey(tmpExt) && exportTaxMap.get(tmpExt) < tmpRate) {
                    tmpCity = j;
                    tmpRate = exportTaxMap.get(tmpExt);
                }
            }
            String t = String.format("%s %s %.2f%%\n", i, tmpCity, tmpRate * 100);
            if (itemClass.contains(i)) {
                res.add(t);
            }
        }
        return res;
    }

    public void prepare() throws Exception {
        File file;
        BufferedReader reader;
        String tmp = null;

        file = new File(companyData);
        reader = new BufferedReader(new FileReader(file));
        while ((tmp = reader.readLine()) != null) {
            String tmpCompany = tmp.split(",")[0];
            companySet.add(tmpCompany);
            company.add(tmp);
        }
        reader.close();

        file = new File(cityData);
        reader = new BufferedReader(new FileReader(file));
        while ((tmp = reader.readLine()) != null) {
            String tmpCity = tmp.split(",")[0];
            citySet.add(tmpCity);
            city.add(tmp);
        }
        reader.close();

        file = new File(containerData);
        reader = new BufferedReader(new FileReader(file));
        while ((tmp = reader.readLine()) != null) {
            String tmpContainer = tmp.split(",")[0];
            containerSet.add(tmpContainer);
            container.add(tmp);
        }
        reader.close();

        file = new File(shipData);
        reader = new BufferedReader(new FileReader(file));
        while ((tmp = reader.readLine()) != null) {
            String tmpShip = tmp.split(",")[0];
            shipSet.add(tmpShip);
            ship.add(tmp);
        }
        reader.close();

        file = new File(courierData);
        reader = new BufferedReader(new FileReader(file));
        int cnt = 0;
        while ((tmp = reader.readLine()) != null) {
            String tmpCourier = tmp.split(",")[0] + "," + tmp.split(",")[2];
            courierMap.put(tmpCourier, cnt++);
            courier.add(tmp);
        }
        reader.close();

        file = new File(recordData);
        reader = new BufferedReader(new FileReader(file));
        cnt = 0;
        while ((tmp = reader.readLine()) != null) {
            String tmpRecord = tmp.split(",")[0];
            recordMap.put(tmpRecord, cnt++);
            record.add(tmp);
        }
        reader.close();

        file = new File(transitData);
        reader = new BufferedReader(new FileReader(file));
        cnt = 0;
        while ((tmp = reader.readLine()) != null) {
            String tmpTransit = tmp.split(",")[0] + "," + tmp.split(",")[1] + tmp.split(",")[2];
            transitMap.put(tmpTransit, cnt++);
            transit.add(tmp);
        }
        reader.close();

        file = new File(handleData);
        reader = new BufferedReader(new FileReader(file));
        cnt = 0;
        while ((tmp = reader.readLine()) != null) {
            String tmpHandle = tmp.split(",")[0] + "," + tmp.split(",")[1] + tmp.split(",")[2];
            handleMap.put(tmpHandle, cnt++);
            handle.add(tmp);
        }
        reader.close();
    }
}
