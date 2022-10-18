package com.example.testdemo.fileIO;

public class DBLoaderMain {
    static FileDataReader fileReader;
    static DBWriter dbWriter;

    public void dataInsert() {
        try {
            long start = System.currentTimeMillis();
            fileReader = new FileDataReader();
            dbWriter = (DBWriter)
                    Class.forName("com.example.testdemo.fileIO.DBWriterImpl")
                    .getDeclaredConstructor().newInstance();
            FileRecord record;

            while ((record = fileReader.readNext()) != null) {
                dbWriter.write(record);
            }
            dbWriter.onFinish();
            long end = System.currentTimeMillis();

            long totalTime = (end - start);
            System.out.printf("Import finished in %dms", totalTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dbWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}