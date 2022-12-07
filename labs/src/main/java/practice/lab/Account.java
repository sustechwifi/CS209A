package practice.lab;

import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private double balance;
    private static ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * @param money
     */
    public synchronized void deposit(double money) {
        try {
            double newBalance = balance + money;
            try {
                Thread.sleep(10);
                // Simulating this service takes some processing time
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            balance = newBalance;
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

    }

    public void depositWithLock(double money) {
        reentrantLock.lock();
        try {
            double newBalance = balance + money;
            try {
                Thread.sleep(10);
                // Simulating this service takes some processing time
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            balance = newBalance;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }

    }

    public double getBalance() {
        return balance;
    }
}