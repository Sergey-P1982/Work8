package com.epam.task8_2;

public class CounterTest {
    public int count;
    boolean value = false;

    synchronized int incrementCount() {
        while (!value)

            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        count++;
        value = false;
        notify(); 
        return count;
    }

    synchronized void printCount() {
        while (value)
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }

        System.out.println("Count = " + count);
        value = true;
        notify();
    }

    public static void main(String[] args) {
        CounterTest obj = new CounterTest();
        new Counter(obj);
        new Printer(obj);
    }
}

class Counter implements Runnable {
    CounterTest test;

    Counter(CounterTest counter) {
        this.test = counter;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (test.count <= 1_000) {

            test.incrementCount();
        }
    }
}

class Printer implements Runnable {
    CounterTest test;

    Printer(CounterTest counter) {
        this.test = counter;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (test.count < 1_000) {

            test.printCount();

        }
    }
}