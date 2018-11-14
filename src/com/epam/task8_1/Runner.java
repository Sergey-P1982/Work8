package com.epam.task8_1;

public class Runner {

    public void run() {

        new Thread(() -> {
            for (int i = 10; i > 0; i--) {
                System.out.println("Time left - " + i + " seconds");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Bomb!!!!!!!!!!!!!!!!");
        }).start();
    }
}

