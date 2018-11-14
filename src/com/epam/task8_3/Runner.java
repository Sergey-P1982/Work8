package com.epam.task8_3;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Runner {
    private Map<Integer, Integer> map = new Hashtable<>();
    private Map<Integer, Integer> mapCon = new ConcurrentHashMap<>();
    final int COUNT = 10000;

    public void run() {
        testHashMap();
        testConcurrentHashMap();
    }

    private void testHashMap() {
        Thread[] threadsWrite = new Thread[COUNT];
        Thread[] threadsRead = new Thread[COUNT];

        for (int i = 0; i < COUNT; i++) {
            int j = i;
            threadsWrite[i] = new Thread(() -> {
                synchronized (map) {
                    map.put(j, j);
                    System.out.println(map.getClass());

                }
            });
            threadsRead[i] = new Thread(() -> {
                synchronized (map) {
//                    System.out.println(map.get(j));
                    map.get(j);
                }
            });

        }
        long startTime = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            threadsWrite[i].start();
            threadsRead[i].start();
        }
        for (int i = 0; i < COUNT; i++) {
            try {
                threadsWrite[i].join();
                threadsRead[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long finish = System.nanoTime();
        System.out.println("\nTime = " + (finish - startTime) / 1000000);
        System.out.println("Method  testHashMap() has been ended");
    }

    private void testConcurrentHashMap() {

        Thread[] threadsWrite = new Thread[COUNT];
        Thread[] threadsRead = new Thread[COUNT];

        for (int i = 0; i < COUNT; i++) {
            int j = i;
            threadsWrite[i] = new Thread(() -> {
                synchronized (this.mapCon) {
                    mapCon.put(j, j);

                }
            });
            threadsRead[i] = new Thread(() -> {
                synchronized (this.mapCon) {
                    System.out.println(mapCon.getClass());
                    mapCon.get(j);
                }
            });

        }
        long startTime = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            threadsWrite[i].start();
            threadsRead[i].start();
        }
        for (int i = 0; i < COUNT; i++) {
            try {
                threadsWrite[i].join();
                threadsRead[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long finish = System.nanoTime();
        System.out.println("\nTime = " + (finish - startTime) / 1000000);
        System.out.println("Method  testConcurrentHashMap() has been ended");
    }
}
